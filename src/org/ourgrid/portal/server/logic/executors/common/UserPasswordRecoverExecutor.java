package org.ourgrid.portal.server.logic.executors.common;

import org.ourgrid.portal.client.common.exceptions.OurGridPortalClientException;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.common.to.service.UserPasswordRecoverTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.logic.model.persistance.User;
import org.ourgrid.portal.server.logic.util.Encryptor;
import org.ourgrid.portal.server.mailer.MailFactory;
import org.ourgrid.portal.server.mailer.Mailer;
import org.ourgrid.portal.server.util.StringRandomizer;

public class UserPasswordRecoverExecutor extends AbstractExecutor {

	private static String PASSWORD_RECOVERED_ERROR = "Error on recovering password."; 
	
	private static String PASSWORD_RECOVERED_SUCCESSFUL = "Password recovered successfully. Please check your e-mail.";
	
	public static String UNAVAIBLE_SERVICE = "Service unavailable. Try again later.";
	
	private static int DEFAULT_PASSWORD_LENGTH = 8; 
	
	public UserPasswordRecoverExecutor(OurGridPortalIF portal) {
		super(portal);
	}
	
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		UserPasswordRecoverTO userPasswordRecoverTO = (UserPasswordRecoverTO) serviceTO;
		ResponseTO userPasswordRecoverResponseTO = new ResponseTO();
		
		User user = getPortal().getDAOManager().getUserByMail(userPasswordRecoverTO.getEmail());
		
		if (user == null) {
			userPasswordRecoverResponseTO.setMessage(PASSWORD_RECOVERED_ERROR);
		} else {
			
			String newPasswd = changeUserPassword(user);
			
			Mailer.send(MailFactory.buildNewPasswordEmail(user.getEmail(), user.getLogin(), newPasswd));
			userPasswordRecoverResponseTO.setMessage(PASSWORD_RECOVERED_SUCCESSFUL);
		}
		
		return userPasswordRecoverResponseTO;
	}

	private String changeUserPassword(User user) throws ExecutionException {
		String newPasswd = StringRandomizer.generateRandomAlphanumericString(DEFAULT_PASSWORD_LENGTH);
		String encrypted;
		
		try {
			encrypted = Encryptor.go(newPasswd);
		} catch (OurGridPortalClientException e) {
			throw new ExecutionException(UNAVAIBLE_SERVICE);
		}
		
		getPortal().getDAOManager().updateUser(user.getLogin(), encrypted, user.getEmail(), user.getProfile(), user.isPending(), user.getStorageSize());
		
		return newPasswd;
	}
	
	public void logTransaction(ServiceTO serviceTO) {
		
		UserPasswordRecoverTO userPasswordRecoverTO = (UserPasswordRecoverTO) serviceTO;
		
		getLogger().info("Executor Name: Password Recover" + LINE_SEPARATOR + 
				"Parameters: "  + LINE_SEPARATOR + 
				" User Login: " + userPasswordRecoverTO.getEmail());
	}
}