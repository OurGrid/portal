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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.ourgrid.broker.status.TaskStatusInfo;
import org.ourgrid.common.interfaces.control.BrokerControlClient;
import org.ourgrid.common.interfaces.management.BrokerManager;
import org.ourgrid.common.interfaces.status.BrokerStatusProviderClient;
import org.ourgrid.common.interfaces.to.BrokerCompleteStatus;
import org.ourgrid.common.interfaces.to.JobsPackage;
import org.ourgrid.portal.server.logic.interfaces.JobStatusUpdateListener;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;

import br.edu.ufcg.lsd.commune.api.FailureNotification;
import br.edu.ufcg.lsd.commune.api.RecoveryNotification;
import br.edu.ufcg.lsd.commune.container.control.ControlOperationResult;
import br.edu.ufcg.lsd.commune.container.servicemanager.client.async.AsyncManagerClient;
import br.edu.ufcg.lsd.commune.container.servicemanager.client.sync.SyncContainerUtil;
import br.edu.ufcg.lsd.commune.identification.ServiceID;

public class BrokerPortalAsyncManagerClient extends AsyncManagerClient<BrokerManager> implements BrokerControlClient, BrokerStatusProviderClient {

	public static final String GET_JOB_STATUS_ACTION_NAME = "getJobStatus";
	public static final String GET_PAGED_TASK_ACTION_NAME = "getPagedTask";
	
	// Seconds
	public static final int GET_PAGED_TASK_ACTION_DELAY = 10;
	public static final int GET_JOB_STATUS_ACTION_DELAY = 10;
	
	private BlockingQueue<Object> addJobBlockingQueue;
	private BlockingQueue<Object> cancelJobBlockingQueue;

	public BrokerPortalAsyncManagerClient(){
		super();
		addJobBlockingQueue = new ArrayBlockingQueue<Object>(1);
		cancelJobBlockingQueue = new ArrayBlockingQueue<Object>(1);
	}
	
	private BrokerPortalAsyncApplicationClient getBrokerPortalAsyncApplicationClient() {
		return (BrokerPortalAsyncApplicationClient) getServiceManager().getApplication();
	}
	
	@RecoveryNotification
	public void controlIsUp(BrokerManager control) {
		super.controlIsUp(control);
		getServiceManager().getLog().info("Broker Control is now UP");
		scheduleGetJobStatusAction();
	}
	
	@FailureNotification
	public void controlIsDown(BrokerManager control) {
		super.controlIsDown(control);

		getServiceManager().getLog().info("Broker Control is now DOWN");
		
		SyncContainerUtil.putResponseObject(
				addJobBlockingQueue, new ControlOperationResult(new Exception(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG)));
		SyncContainerUtil.putResponseObject(
				cancelJobBlockingQueue, new ControlOperationResult(new Exception(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG)));
	}

	public void hereIsConfiguration(Map<String, String> arg0) {
		
	}

	public void hereIsUpTime(long arg0) {
		
	}

	public void operationSucceed(ControlOperationResult controlOperationResult) {
		
		if (controlOperationResult.getResult() instanceof Integer) {
			SyncContainerUtil.putResponseObject(addJobBlockingQueue, controlOperationResult);
		} else {
			SyncContainerUtil.putResponseObject(cancelJobBlockingQueue, controlOperationResult);
		}
	}

	public BlockingQueue<Object> getAddJobQueue() {
		return this.addJobBlockingQueue;
	}
	
	public BlockingQueue<Object> getCancelJobQueue() {
		return this.cancelJobBlockingQueue;
	}

	public void hereIsCompleteStatus(ServiceID statusProviderServiceID,
			BrokerCompleteStatus status) {

	}

	private void scheduleGetJobStatusAction() {
		BrokerPortalAsyncApplicationClient applicationClient = getBrokerPortalAsyncApplicationClient();
		getApplicationClient().addActionForRepetition(GET_JOB_STATUS_ACTION_NAME, 
				new GetJobStatusAction());
		applicationClient.scheduleActionWithFixedDelay(GET_JOB_STATUS_ACTION_NAME, 0, 
				GET_JOB_STATUS_ACTION_DELAY, TimeUnit.SECONDS, null);
	}

	public void hereIsCompleteJobsStatus(ServiceID statusProviderServiceID,
			JobsPackage jobsStatus) {
		
	}

	private void callJobStatusListeners(JobsPackage jobsStatus) {
		BrokerPortalModel model = getBrokerPortalAsyncApplicationClient().getModel();
		List<JobStatusUpdateListener> jobStatusListeners = model.getJobStatusListeners();
		
		for (JobStatusUpdateListener jobStatusListener : jobStatusListeners) {
			jobStatusListener.hereIsJobDescription(jobsStatus);
		}
	}
	
	public void hereIsJobsStatus(ServiceID statusProviderServiceID,
			JobsPackage jobsStatus) {
		try {
			callJobStatusListeners(jobsStatus);
		} catch (Throwable e) {
			getServiceManager().getLog().debug("Error cause : " + e.getMessage());
		}
	}

	public void hereIsPagedTasks(ServiceID serviceID, Integer jobId,
			Integer offset, List<TaskStatusInfo> pagedTasks) {
		callJobStatusListeners(offset, pagedTasks);
	}

	private void callJobStatusListeners(Integer offset, List<TaskStatusInfo> pagedTasks) {
		BrokerPortalModel model = getBrokerPortalAsyncApplicationClient().getModel();
		List<JobStatusUpdateListener> jobStatusListeners = model.getJobStatusListeners();
		
		for (JobStatusUpdateListener jobStatusListener : jobStatusListeners) {
			jobStatusListener.hereIsPagedTasks(offset, pagedTasks);
		}		
	}
}