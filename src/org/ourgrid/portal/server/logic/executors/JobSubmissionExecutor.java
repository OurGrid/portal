package org.ourgrid.portal.server.logic.executors;

import java.io.File;
import java.io.FileInputStream;

import org.globus.gsi.GlobusCredential;
import org.ourgrid.common.specification.job.IOBlock;
import org.ourgrid.common.specification.job.IOEntry;
import org.ourgrid.common.specification.job.JobSpecification;
import org.ourgrid.common.specification.job.TaskSpecification;
import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.gridcommunication.BrokerPortalAsyncApplicationClient;
import org.ourgrid.portal.server.logic.gridcommunication.BrokerPortalModel;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

import br.edu.ufcg.lsd.commune.container.control.ControlOperationResult;

public abstract class JobSubmissionExecutor extends AbstractExecutor{
	
	private BrokerPortalAsyncApplicationClient brokerPortalClient;
	private BrokerPortalModel model;
	private final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String GATEWAY_REQUIREMENT = "provider != 3g-gisela@xmpp.ourgrid.org";
	
	public JobSubmissionExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	private boolean isProxyValid(String userHome) {
		File proxyFile = new File(userHome + 
				FILE_SEPARATOR + CommonServiceConstants.USER_CERT);
		if(!proxyFile.exists()) {
			return false;
		}
		try {
			
			GlobusCredential proxy = new GlobusCredential(
					new FileInputStream(proxyFile.getCanonicalPath()));
			
			return proxy.getTimeLeft() > 0;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void initializeClient() throws ExecutionException {
		brokerPortalClient = getPortal().getBrokerPortalClient();
		model = brokerPortalClient.getModel();
		
		if (!brokerPortalClient.isServerApplicationUp()) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}
	}

	public Integer addJob(String userName, JobSpecification theJob) throws ExecutionException {
		String storageRoot = OurgridPortalProperties.getInstance().getProperty(
				OurgridPortalProperties.STORAGE_DIRECTORY);
		String userHome = storageRoot + FILE_SEPARATOR + userName;
		
		if (!isProxyValid(userHome)) {
			String requirements = theJob.getRequirements();
			if(requirements != null && !requirements.trim().isEmpty()) {
				theJob.setRequirements(requirements + " && " + GATEWAY_REQUIREMENT);
			}else {
				theJob.setRequirements(GATEWAY_REQUIREMENT);
			}
		}else {
			for (TaskSpecification task : theJob.getTaskSpecs()) {
				IOBlock initBlock = task.getInitBlock();
				IOEntry proxyEntry = new IOEntry();
				proxyEntry.setSourceFile(userHome + FILE_SEPARATOR + CommonServiceConstants.USER_CERT);
				proxyEntry.setDestination(CommonServiceConstants.USER_CERT);
				proxyEntry.setCommand("put");
				initBlock.putEntry(proxyEntry);
			}
		}
		ControlOperationResult addJobResult = brokerPortalClient.addJob(theJob);
		if (addJobResult.getErrorCause() != null) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}

		Integer jobId = (Integer) addJobResult.getResult();

		if (jobId < 1) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}
		model.activateJob(jobId);
		return jobId;
	}
	
	public void addRequest(Integer jobId, AbstractRequest request) {
		getPortal().getDAOManager().addRequest(jobId, request);
	}
	
	public void reeschedule() {
//		brokerPortalClient.getManagerClient().reScheduleGetJobStatusAction();
	}
	
}
