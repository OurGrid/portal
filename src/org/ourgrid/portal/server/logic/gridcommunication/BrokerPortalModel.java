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

import org.ourgrid.portal.client.common.StateConstants;
import org.ourgrid.portal.client.common.to.model.JobTO;
import org.ourgrid.portal.client.common.to.model.TaskPageTO;
import org.ourgrid.portal.server.logic.interfaces.JobStatusUpdateListener;

import com.extjs.gxt.ui.client.data.ModelData;

public class BrokerPortalModel  {

	private Map<Integer, JobTO> jobs;
	private List<Integer> activeJobIds;
	private List<JobStatusUpdateListener> jobStatusListeners;
	
	public BrokerPortalModel() {
		this.jobs = Collections.synchronizedMap(new HashMap<Integer, JobTO>());
		this.activeJobIds = new LinkedList<Integer>();
		this.jobStatusListeners = new LinkedList<JobStatusUpdateListener>();
	}
	
	public List<Integer> getActiveJobIds() {
		return new ArrayList<Integer>(activeJobIds);
	}
	
	public void updateJobTO(JobTO newJobTO) {
		JobTO oldJobTO = jobs.get(newJobTO.getId());
		
		if (oldJobTO != null) {
			newJobTO.setChildren(new ArrayList<ModelData>(oldJobTO.getChildren()));
		}
	
		jobs.put(newJobTO.getId(), newJobTO);
	}
	
	public List<JobTO> getStatus(List<Integer> jobIds){
		if (!this.jobs.isEmpty()) {
			return selectJobs(jobIds);
		}
		return new ArrayList<JobTO>();
	}
	
	public JobTO getStatus(int jobId) {
		return this.jobs.get(jobId);
	}
	
	public JobTO getStatus(int jobId, List<Integer> firstTasksIds) {
		JobTO jobTO = getStatus(jobId);
		if (jobTO == null) {
			return null;
		}
		
		for (ModelData modelData : jobTO.getChildren()) {
			TaskPageTO taskPageTO = (TaskPageTO) modelData;
			if (!firstTasksIds.contains(taskPageTO.getFirstTaskId()) && 
					taskPageTO.getFirstTaskId() != 0) {
				taskPageTO.removeAll();
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
			TaskPageTO superTaskTO = jobTO.getTaskPage(offset);
			superTaskTO.setChildren(tasksTO);
		}
	}

	public boolean isJobFinished(Integer jobId) {
		JobTO jobTO = jobs.get(jobId);
		
		if (jobTO != null) {
			return !jobIsRunning(jobTO.getStatus());
		} 
		
		return false;
	}

	public void removeActiveJobId(Integer jobId) {
		activeJobIds.remove(jobId);
	}
	
	public void removeJob(Integer jobId) {
		this.activeJobIds.remove(jobId);
		this.jobs.remove(jobId);
	}

}