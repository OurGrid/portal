/*
 * Copyright (C) 2008 Universidade Federal de Campina Grande
 *  
 * This file is part of OurGrid. 
 *
 * OurGrid is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version. 
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details. 
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package org.ourgrid.portal.server.logic.gridcommunication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.ourgrid.portal.client.common.StateConstants;
import org.ourgrid.portal.client.common.to.model.JobTO;
import org.ourgrid.portal.client.common.to.model.SuperTaskTO;
import org.ourgrid.portal.server.logic.interfaces.JobStatusUpdateListener;

import com.extjs.gxt.ui.client.data.ModelData;

public class BrokerPortalModel  {

	private Future<?> jobStatusFuture;
	private Future<?> pagedTaskFuture;
	
	private Map<Integer, JobTO> jobs;
	private Map<Integer, List<PagedTaskRequest> > pagedTaskRequests;
	private List<Integer> activeJobIds;

	private List<JobStatusUpdateListener> jobStatusListeners;
	
	public BrokerPortalModel() {
		this.jobs = Collections.synchronizedMap(new HashMap<Integer, JobTO>());
		this.pagedTaskRequests = Collections.synchronizedMap(new HashMap<Integer, List<PagedTaskRequest>>());
		this.activeJobIds = new LinkedList<Integer>();

		this.jobStatusListeners = new LinkedList<JobStatusUpdateListener>();
	}
	
	public Map<Integer, List<PagedTaskRequest>> getPagedTaskRequests() {
		return new HashMap<Integer, List<PagedTaskRequest>>(pagedTaskRequests);
	}
	
	public boolean hasPagedTaskRequest() {
		return !pagedTaskRequests.isEmpty();
	}
	
	public List<Integer> getActiveJobIds() {
		return new ArrayList<Integer>(activeJobIds);
	}
	
	public boolean isJobStatusFutureCancelled() {
		return jobStatusFuture == null || jobStatusFuture.isCancelled() || jobStatusFuture.isDone();
	}

	public void setJobStatusFuture(Future<?> scheduledActionFuture) {
		this.jobStatusFuture = scheduledActionFuture;
	}
	
	public void cancelJobStatusFuture(){
		if (this.jobStatusFuture != null) {
			this.jobStatusFuture.cancel(true);
			this.jobStatusFuture = null;
		}
	}
	
	public boolean isPagedTaskFutureCancelled() {
		return pagedTaskFuture == null || pagedTaskFuture.isCancelled() || pagedTaskFuture.isDone();
	}

	public void setPagedTaskFuture(Future<?> scheduledActionFuture) {
		this.pagedTaskFuture = scheduledActionFuture;
	}
	
	public void cancelPagedTaskFuture(){
		if (this.pagedTaskFuture != null) {
			this.pagedTaskFuture.cancel(true);
			this.pagedTaskFuture = null;
		}
	}
	
	public void updateJobTO(JobTO newJobTO) {
		JobTO oldJobTO = jobs.get(newJobTO.getId());
		
		if (oldJobTO != null) {
			newJobTO.setChildren(new ArrayList<ModelData>(oldJobTO.getChildren()));
		}
	
		jobs.put(newJobTO.getId(), newJobTO);
	}
	
	public List<JobTO> getStatus(List<Integer> jobIds){
		if (!this.jobs.isEmpty()) return selectJobs(jobIds);
		else return new ArrayList<JobTO>();
	}
	
	public JobTO getStatus(int jobId) {
		return this.jobs.get(jobId);
	}
	
	public JobTO getStatus(int jobId, List<Integer> pagedTaskIds) {
		JobTO jobTO = getStatus(jobId);
		
		if (jobTO == null) {
			return null;
		}
		
		for (ModelData modelData : jobTO.getChildren()) {
			SuperTaskTO superTaskTO = (SuperTaskTO) modelData;
			
			if (!pagedTaskIds.contains(superTaskTO.getFirstTaskId())) {
				superTaskTO.removeAll();
			} 
		}
		
		return jobTO;
	}

	
	public boolean closeFinishedJobs() {
		
		List<Integer> finishedJobs = new LinkedList<Integer>();
		
		for (Integer jobId : activeJobIds){
			
			JobTO jobTO = jobs.get(jobId);
			if (jobTO != null && !jobIsRunning(jobTO.getStatus())) {
				finishedJobs.add(jobId);
			}
		}
		
		if (!finishedJobs.isEmpty()) {
			return activeJobIds.removeAll(finishedJobs);
		}
		return false;
	}
	
	private List<JobTO> selectJobs(List<Integer> jobIds){
		List<JobTO> ourJobs = new LinkedList<JobTO>();
		
		for (Integer jobId : jobIds){
			JobTO job = this.jobs.get(jobId);
			if (job != null) {
				ourJobs.add(job);
			}
		}

		return ourJobs;
	}
	
	private boolean jobIsRunning(String state) {
		return state.equals(StateConstants.UNSTARTED.toString()) || 
			state.equals(StateConstants.RUNNING.toString());
	}
	
	public boolean hasRunningJobs() {
		return !activeJobIds.isEmpty();
	}

	public void activateJob(Integer jobId) {
		this.activeJobIds.add(jobId);
		this.pagedTaskRequests.put(jobId, new LinkedList<PagedTaskRequest>());
	}

	public void addJobStatusListener(JobStatusUpdateListener jobStatusRetrieval) {
		jobStatusListeners.add(jobStatusRetrieval);
	}

	public List<JobStatusUpdateListener> getJobStatusListeners() {
		return jobStatusListeners;
	}

	public void updateJobTasksTO(Integer jobId, Integer offset, List<ModelData> tasksTO) {
		JobTO jobTO = this.jobs.get(jobId);
		
		if (jobTO != null) {
			SuperTaskTO superTaskTO = jobTO.getSuperTaskTO(offset);
			superTaskTO.setChildren(tasksTO);
		}
	}

	public void removeRequestedPagedTasks(Integer jobId) {
		pagedTaskRequests.remove(jobId);
	}

	public boolean isJobFinished(Integer jobId) {
		JobTO jobTO = jobs.get(jobId);
		
		if (jobTO != null) {
			return !jobIsRunning(jobTO.getStatus());
		} 
		
		return false;
	}

	public void addPagedTaskRequest(Integer jobId, PagedTaskRequest pagedTaskRequest) {
		getPagedTaskRequests(jobId).add(pagedTaskRequest);
	}

	private List<PagedTaskRequest> getPagedTaskRequests(Integer jobId) {
		
		List<PagedTaskRequest> requests = this.pagedTaskRequests.get(jobId) ;

		if (requests == null) {
			requests = new LinkedList<PagedTaskRequest>();
			this.pagedTaskRequests.put(jobId, requests);
		}
		
		return requests;
	}

	public void removeActiveJobId(Integer jobId) {
		activeJobIds.remove(jobId);
	}
	
	public void removeJob(Integer jobId) {
		this.activeJobIds.remove(jobId);
		this.jobs.remove(jobId);
		this.pagedTaskRequests.remove(jobId);
	}

	public boolean containsPagedTaskRequest(Integer jobId, PagedTaskRequest pagedTaskRequest) {
		List<PagedTaskRequest> actual = pagedTaskRequests.get(jobId);
		
		if (actual == null) {
			return false;
		}
		
		return actual.contains(pagedTaskRequest);
	}
}