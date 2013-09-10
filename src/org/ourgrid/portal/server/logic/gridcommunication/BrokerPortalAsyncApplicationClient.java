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
import java.util.concurrent.BlockingQueue;

import org.ourgrid.common.interfaces.management.BrokerManager;
import org.ourgrid.common.specification.job.JobSpecification;
import org.ourgrid.portal.server.logic.interfaces.JobStatusUpdateListener;

import br.edu.ufcg.lsd.commune.container.control.ControlOperationResult;
import br.edu.ufcg.lsd.commune.container.servicemanager.client.InitializationContext;
import br.edu.ufcg.lsd.commune.container.servicemanager.client.async.AsyncApplicationClient;
import br.edu.ufcg.lsd.commune.container.servicemanager.client.sync.SyncContainerUtil;
import br.edu.ufcg.lsd.commune.context.ModuleContext;
import br.edu.ufcg.lsd.commune.network.xmpp.CommuneNetworkException;
import br.edu.ufcg.lsd.commune.processor.ProcessorStartException;

public class BrokerPortalAsyncApplicationClient extends AsyncApplicationClient<BrokerManager, BrokerPortalAsyncManagerClient> {

	private BrokerPortalModel model;

	public BrokerPortalAsyncApplicationClient(ModuleContext context, BrokerPortalModel model) 
		throws CommuneNetworkException, ProcessorStartException {
		super("BROKER_PORTAL_ASYNC_UI", context);

		this.model = model;
	}
	
	protected InitializationContext<BrokerManager, BrokerPortalAsyncManagerClient> createInitializationContext() {
		return new BrokerPortalAsyncInitializationContext();
	}
	
 	/*
	 * Adds job described in the job specification
	 */
	public synchronized ControlOperationResult addJob(JobSpecification theJob) {
		
		BlockingQueue<Object> blockingQueue = getManagerClient().getAddJobQueue();
		getManager().addJob(getManagerClient(), theJob);
		return SyncContainerUtil.busyWaitForResponseObject(blockingQueue, ControlOperationResult.class);
	}
	
	public synchronized ControlOperationResult cancelJob(Integer jobId) {
		getManager().cancelJob(getManagerClient(), jobId);
		BlockingQueue<Object> blockingQueue = getManagerClient().getCancelJobQueue();
		return SyncContainerUtil.busyWaitForResponseObject(blockingQueue, ControlOperationResult.class);
	}
	
	public BrokerPortalModel getModel() {
		return this.model;
	}

	public void getJobsStatus(List<Integer> jobIds) {
		
		if (isServerApplicationUp()) {
			getManager().getJobsStatus(getManagerClient(), jobIds);
		}
	}

	public void addJobStatusUpdateListener(JobStatusUpdateListener jobStatusRetrieval) {
		this.model.addJobStatusListener(jobStatusRetrieval);
	}

	public void getPagedTasks(Integer jobId, Integer offset, Integer pageSize) {
		
		if (isServerApplicationUp()) {
			getManager().getPagedTasks(getManagerClient(), jobId, offset, pageSize);
		}
	}
	
}