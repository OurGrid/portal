package org.ourgrid.portal.server.logic.executors.common;

import org.ourgrid.portal.client.common.exceptions.OurGridPortalClientException;
import org.ourgrid.portal.client.common.to.model.UserTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.response.UserSettingsResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.common.to.service.UserSettingsTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.logic.model.persistance.User;
import org.ourgrid.portal.server.logic.util.Encryptor;

public class UserSettingsExecutor extends AbstractExecutor {

	private static String USER_SUCESSFULLY_MODIFIED = "User successfully modified.";
	private static String INVALID_PASSWORD = "Invalid Password.";
	private static String INVALID_EMAIL = "Invalid Email.";
	private static String UNAVAIBLE_SERVICE = "Service unavailable. Try again later.";
	
	public UserSettingsExecutor(OurGridPortalIF portal) {
		super(portal);
	}
	
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {

		UserSettingsTO userSettingsTO = (UserSettingsTO) serviceTO;
		UserSettingsResponseTO userSettingsResponseTO = new UserSettingsResponseTO();
		
		String password = userSettingsTO.getPassword();
		String login = userSettingsTO.getLogin();
		String email = userSettingsTO.getEmail();
		
		try {
			password = Encryptor.go(password);
		} catch (OurGridPortalClientException e) {
			throw new ExecutionException(UNAVAIBLE_SERVICE);
		}

		User uLogin = getPortal().getDAOManager().getUserByLogin(login);
		User uEmail = getPortal().getDAOManager().getUserByMail(email);
		
		if(!uLogin.getPassw().equals(password)){
			throw new ExecutionException(INVALID_PASSWORD);
		}
		
		if(null != uEmail && !uLogin.getLogin().equals(uEmail.getLogin())){
			throw new ExecutionException(INVALID_EMAIL);
		}
		
		String newPassword = userSettingsTO.getNewPassword();
		
		if(null == newPassword){
			newPassword = uLogin.getPassw();
		}
		
		try {
			newPassword = Encryptor.go(newPassword);
		} catch (OurGridPortalClientException e) {
			throw new ExecutionException(UNAVAIBLE_SERVICE);
		}
		
		getPortal().getDAOManager().updateUser(login, newPassword, email, uLogin.getProfile(), uLogin.isPending(), uLogin.getStorageSize());

		User u = getPortal().getDAOManager().getUserByLogin(login);
		
		userSettingsResponseTO.setMessage(USER_SUCESSFULLY_MODIFIED);
		userSettingsResponseTO.setUser(generateUserCopy(u));
		
		return userSettingsResponseTO;
	}
	
	public UserTO generateUserCopy(User user) {
		UserTO uCopy = new UserTO();
		
		uCopy.setLogin(user.getLogin());
		uCopy.setEmail(user.getEmail());
		uCopy.setPassword(user.getPassw());
		uCopy.setPending(user.isPending());
		uCopy.setProfile(user.getProfile());
		
		return uCopy;
	}
	
	public void logTransaction(ServiceTO serviceTO) {
		
		UserSettingsTO userSettingsTO = (UserSettingsTO) serviceTO;
		
		getLogger().info("Executor Name: User Settings" + LINE_SEPARATOR + 
				"Parameters: "  + LINE_SEPARATOR + 
				" User Login: " + userSettingsTO.getLogin());
	}
}