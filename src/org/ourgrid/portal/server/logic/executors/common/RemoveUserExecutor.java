package org.ourgrid.portal.server.logic.executors.common;

import java.util.LinkedList;
import java.util.List;

import org.ourgrid.portal.client.common.to.model.UserTO;
import org.ourgrid.portal.client.common.to.response.RemoveUserResponseTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.RemoveUserTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.logic.model.persistance.User;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class RemoveUserExecutor extends AbstractExecutor {
	
	public static String USER_SUCESSFULLY_REMOVED = "Users sucessfully removed.";

	public RemoveUserExecutor(OurGridPortalIF portal) {
		super(portal);
	}
	
	public ResponseTO execute(ServiceTO serviceTO) {
		
		RemoveUserTO removeUserTO = (RemoveUserTO) serviceTO;
		RemoveUserResponseTO removeUserResponseTO = new RemoveUserResponseTO();
		
		List<UserTO> listUser = removeUserTO.getUserSelection();
		
		for (UserTO userTO : listUser) {
			User user = getPortal().getDAOManager().getUserByLogin(userTO.getLogin());
			getPortal().getDAOManager().removeUserByLogin(user.getLogin());
		}
		
		String adminLogin = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.ADMIN_LOGIN);
		
		List<User> allUsers = getPortal().getDAOManager().getUsers(removeUserTO.getPageNumber(), removeUserTO.getLimit(), adminLogin);
		List<UserTO> allUsersCopy = getUsers(allUsers);
		
		if (allUsers == null) {
			allUsersCopy = new LinkedList<UserTO>();
		}
		
		removeUserResponseTO.setResult(allUsersCopy);
		removeUserResponseTO.setMessage(USER_SUCESSFULLY_REMOVED);
		
		return removeUserResponseTO;
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
		
		RemoveUserTO removeUserTO = (RemoveUserTO) serviceTO;
		
		getLogger().info("Executor Name: Remove Users" + LINE_SEPARATOR + 
				"Parameters: "  + LINE_SEPARATOR + 
				" User Logins: " + this.getUserLogins(removeUserTO.getUserSelection()));
	}
	
	private String getUserLogins(List<UserTO> users) {
		String userLogins = "";
		
		for (UserTO user : users) {
			userLogins += user.getLogin() + " ";
		}
		
		return userLogins;
	}
}