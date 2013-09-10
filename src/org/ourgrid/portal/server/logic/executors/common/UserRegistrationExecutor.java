package org.ourgrid.portal.server.logic.executors.common;

import org.ourgrid.portal.client.common.exceptions.OurGridPortalClientException;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.response.UserLoginResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.common.to.service.UserRegistrationTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.logic.model.persistance.User;
import org.ourgrid.portal.server.logic.util.Encryptor;
import org.ourgrid.portal.server.mailer.MailFactory;
import org.ourgrid.portal.server.mailer.Mailer;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class UserRegistrationExecutor extends AbstractExecutor {

	private static String USER_SUCESSFULLY_REGISTERED = "Welcome to Ourgrid Portal. Thank you for subscribing." +
														" You should receive a confirmation email." ;
	
	private static String USER_ALREADY_EXISTS = "User already exists.";
	
	public static String UNAVAIBLE_SERVICE = "Service unavailable. Try again later.";
	
	public UserRegistrationExecutor(OurGridPortalIF portal) {
		super(portal);
	}
	
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		UserRegistrationTO userRegistrationTO = (UserRegistrationTO) serviceTO;
		UserLoginResponseTO userRegistrationResponseTO = new UserLoginResponseTO();
		
		String login = userRegistrationTO.getLogin();
		String password;
		try {
			password = Encryptor.go(userRegistrationTO.getPassword());
		} catch (OurGridPortalClientException e) {
			throw new ExecutionException(UNAVAIBLE_SERVICE);
		}
		String email = userRegistrationTO.getEmail();
		String profile = userRegistrationTO.getProfile();
		
		
		Integer defaultStorageSize = Integer.parseInt(OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.DEFAULT_STORAGE_SIZE));
		
		boolean success = getPortal().getDAOManager().addUser(login, password, email, profile, true, defaultStorageSize);

		if (!success) {
			throw new ExecutionException(USER_ALREADY_EXISTS);
		}
		
		userRegistrationResponseTO.setMessage(USER_SUCESSFULLY_REGISTERED);
		sendConfirmationEmailToUser(email, login);
		sendNotifyEmailToAdmin(login);
		
		return userRegistrationResponseTO;
	}

	private String getAdminEmail() {
		String adminLogin = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.ADMIN_LOGIN);
		User admin = getPortal().getDAOManager().getUserByLogin(adminLogin);
		return admin.getEmail();
	}

	private void sendNotifyEmailToAdmin(String login) {
		Mailer.send(MailFactory.buildNewAccountEmail(getAdminEmail(), login));
	}

	private void sendConfirmationEmailToUser(String email, String login) {
		Mailer.send(MailFactory.buildAccountWaitConfirmationMail(email, login));
	}
	
	public void logTransaction(ServiceTO serviceTO) {
		
		UserRegistrationTO userRegistrationTO = (UserRegistrationTO) serviceTO;
		
		getLogger().info("Executor Name: User Registration" + LINE_SEPARATOR + 
				"Parameters: "  + LINE_SEPARATOR + 
				" User Login: " + userRegistrationTO.getLogin());
	}
}