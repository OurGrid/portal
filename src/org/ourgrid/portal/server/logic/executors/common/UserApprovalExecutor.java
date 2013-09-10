package org.ourgrid.portal.server.logic.executors.common;

import java.io.File;
import java.util.List;

import org.ourgrid.portal.client.common.to.model.UserTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.response.UserApprovalResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.common.to.service.UserApprovalTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.mailer.MailFactory;
import org.ourgrid.portal.server.mailer.Mailer;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class UserApprovalExecutor extends AbstractExecutor {
	
	public static String USER_SUCESSFULLY_APPROVED = "Users sucessfully approved.";
	

	public UserApprovalExecutor(OurGridPortalIF portal) {
		super(portal);
	}
	
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		UserApprovalTO approveUserTO = (UserApprovalTO) serviceTO;
		UserApprovalResponseTO approveUserResponseTO = new UserApprovalResponseTO();
		
		List<UserTO> listUser = approveUserTO.getUserSelection();
		
		for (UserTO userTO : listUser) {
			getPortal().getDAOManager().approveUser(userTO.getLogin());
			sendConfirmationEmailToUser(userTO.getEmail(), userTO.getLogin());
			userTO.setPending(false);
			createHomeDir(userTO.getLogin());
			
		}
		
		approveUserResponseTO.setMessage(USER_SUCESSFULLY_APPROVED);
		approveUserResponseTO.setResult(listUser);
		
		return approveUserResponseTO;
	}

	private void sendConfirmationEmailToUser(String email, String login) {
		Mailer.send(MailFactory.buildApprovedAcountEmail(email, login));
	}
	
	public void logTransaction(ServiceTO serviceTO) {
		
		UserApprovalTO approveUserTO = (UserApprovalTO) serviceTO;
		
		getLogger().info("Executor Name: User Approval" + LINE_SEPARATOR + 
				"Parameters: " + LINE_SEPARATOR + 
				" User Logins: " + this.getUserLogins(approveUserTO.getUserSelection()));
	}
	
	private String getUserLogins(List<UserTO> users) {
		String userLogins = "";
		
		for (UserTO user : users) {
			userLogins += user.getLogin() + " ";
		}
		
		return userLogins;
	}
	
	private void createHomeDir(String userLogin) {
		
		String path = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.STORAGE_DIRECTORY);

		File homeDir = new File(path + File.separator + userLogin);

		if(!homeDir.exists()){
			homeDir.mkdirs();
		}
	}
}