package org.ourgrid.portal.server.logic.executors.common;

import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.response.UserQuotaResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.common.to.service.UserQuotaTO;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;

public class UsersEditQuotaExecutor extends AbstractExecutor {

	public UsersEditQuotaExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	public ResponseTO execute(ServiceTO serviceTO) {
		
		UserQuotaTO userQuotaTO = (UserQuotaTO) serviceTO;
		UserQuotaResponseTO responseTO = new UserQuotaResponseTO();
		
		String login = userQuotaTO.getUser().getLogin();
		int storageSize = userQuotaTO.getUser().getStorageSize();
		
		getPortal().getDAOManager().setStorageSize(login, storageSize);

		return responseTO;
	}
	
	public void logTransaction(ServiceTO serviceTO) {
		
		UserQuotaTO userQuota = (UserQuotaTO) serviceTO;
		
		getLogger().info("Executor Name: UsersEditQuotaExecutor" + LINE_SEPARATOR + 
				"Parameters: "  + LINE_SEPARATOR + 
				" User: " + userQuota.getUser().getLogin() + LINE_SEPARATOR + 
				" New Quota Value: " + userQuota.getUser().getStorageSize());
	}
}