package org.ourgrid.portal.server.logic.executors.common;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.ourgrid.portal.client.common.exceptions.OurGridPortalClientException;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.common.to.model.UserTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.response.UserLoginResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.common.to.service.UserLoginTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.logic.model.persistance.User;
import org.ourgrid.portal.server.logic.util.Encryptor;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class UserLoginExecutor extends AbstractExecutor {

	public static String SUCESSFULL_LOGIN = "Login Succeed.";
	
	public static String PENDING_LOGIN = "This account is pending";
	
	public static String INVALID_LOGIN_PASSWORD = "Invalid Login or Password.";
	
	public static String UNAVAIBLE_SERVICE = "Service unavailable. Try again later.";
	
	public UserLoginExecutor(OurGridPortalIF portal) {
		super(portal);
	}
	
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		UserLoginTO userLoginTO = (UserLoginTO) serviceTO;
		try {
			userLoginTO.setPassword(Encryptor.go(userLoginTO.getPassword()));
		} catch (OurGridPortalClientException e) {
			throw new ExecutionException(UNAVAIBLE_SERVICE);
		}
		
		UserLoginResponseTO userLoginResponseTO = new UserLoginResponseTO();
		
		User u = getPortal().getDAOManager().getUserByLogin(userLoginTO.getLogin());
		
		if ( u == null || !userLoginTO.getLogin().equals(u.getLogin()) ||
				!userLoginTO.getPassword().equals(u.getPassw()))  {
			throw new ExecutionException(INVALID_LOGIN_PASSWORD);
		}
		
		if (u.isPending()) {
			throw new ExecutionException(PENDING_LOGIN);
		}
		
		List<AbstractRequest> jobs = getPortal().getDAOManager().getJobsRequestByLogin(u.getLogin());
		
		if (null == jobs){
			jobs = new LinkedList<AbstractRequest>();
		}
		
		createHomeDir(userLoginTO.getLogin());
		
		userLoginResponseTO.setMessage(SUCESSFULL_LOGIN);
		userLoginResponseTO.setLoggedUser(generateUserCopy(u));
		userLoginResponseTO.setJobsRequestList(jobs);
		
		return userLoginResponseTO;
	}
	
	private void createHomeDir(String userLogin) {
		
		String path = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.STORAGE_DIRECTORY);

		File homeDir = new File(path + File.separator + userLogin);

		if(!homeDir.exists()){
			homeDir.mkdirs();
		}
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
		
		UserLoginTO userLoginTO = (UserLoginTO) serviceTO;
		
		getLogger().info("Executor Name: User Login" + LINE_SEPARATOR +
				"Parameters:" + LINE_SEPARATOR + 
				" Login = " + userLoginTO.getLogin());
	}
}