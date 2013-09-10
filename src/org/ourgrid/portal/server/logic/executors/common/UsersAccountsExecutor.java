package org.ourgrid.portal.server.logic.executors.common;

import java.util.LinkedList;
import java.util.List;

import org.ourgrid.portal.client.common.to.model.UserTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.response.UsersAccountsResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.common.to.service.UsersAccountsTO;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.logic.model.persistance.User;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class UsersAccountsExecutor extends AbstractExecutor {

	public static String SUCCESSFUL_REQUEST = "Pending Users Request is successful";
	
	public UsersAccountsExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	public ResponseTO execute(ServiceTO serviceTO) {
		
		UsersAccountsTO usersAccountsTO = (UsersAccountsTO) serviceTO;
		UsersAccountsResponseTO responseTO = new UsersAccountsResponseTO();
		
		String adminLogin = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.ADMIN_LOGIN);
		
		List<User> allUsers = getPortal().getDAOManager().getUsers(usersAccountsTO.getPageNumber(), usersAccountsTO.getLimit(), adminLogin);
		List<UserTO> allUsersCopy = getUsers(allUsers);
		
		if (allUsers == null) {
			allUsersCopy = new LinkedList<UserTO>();
		}
		
		responseTO.setResult(allUsersCopy);
		responseTO.setMessage(SUCCESSFUL_REQUEST);
		
		return responseTO;
	}
	
	private List<UserTO> getUsers(List<User> users) {
		List<UserTO> newUsers = new LinkedList<UserTO>();
		
		for (User u : users) {
			UserTO uCopy = new UserTO();
			
			uCopy.setLogin(u.getLogin());
			uCopy.setEmail(u.getEmail());
			uCopy.setPassword(u.getPassw());
			uCopy.setProfile(u.getProfile());
			uCopy.setPending(u.isPending());
			uCopy.setStorageSize(u.getStorageSize());
			
			newUsers.add(uCopy);
		}
		
		return newUsers;
	}
	
	public void logTransaction(ServiceTO serviceTO) {
		
		UsersAccountsTO usersAccountsTO = (UsersAccountsTO) serviceTO;
		
		getLogger().info("Executor Name: UserAccounts" + LINE_SEPARATOR + 
				"Parameters: "  + LINE_SEPARATOR + 
				" Page Number: " + usersAccountsTO.getPageNumber() + LINE_SEPARATOR + 
				" Limit: " + usersAccountsTO.getLimit());
	}
}