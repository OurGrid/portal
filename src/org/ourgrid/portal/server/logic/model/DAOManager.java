package org.ourgrid.portal.server.logic.model;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.ourgrid.common.statistics.util.hibernate.HibernateUtil;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.server.logic.model.persistance.User;

public class DAOManager {
	
	private static final String XML_CONF_FILE = "ourgridportal-hibernate.cfg.xml";
	
	private DataStoredDAO dataStoredDAO;
	private RequestDAO requestDAO;
	private UserDAO userDAO;
	
	public DAOManager() {
		
		HibernateUtil.setUp(XML_CONF_FILE);
		
		this.requestDAO = new RequestDAO();
		this.dataStoredDAO = new DataStoredDAO();
		this.userDAO = new UserDAO();
	}
	
	public void addRequest(Integer jobId, AbstractRequest request) {
		requestDAO.addRequest(jobId, request);
	}
	
	public void removeRequest(Integer jobId) {
		requestDAO.removeRequest(jobId);
	}
	
	public AbstractRequest getRequest(int jobID) {
		return requestDAO.getRequest(jobID);
	}

	public void createDataStore(Long uploadId, String rootDir) {
		dataStoredDAO.createDataStore(uploadId, rootDir);
	}

	public User getUserByLogin(String login) {
		User user = null;
		try {
			user = userDAO.getUserByLogin(login);
		} catch (Exception e){
			HibernateUtil.rollbackAndCloseSession();
		}

		return user;
	}

	public boolean addUser(String login, String password, String email, String profile, boolean isPending,
			Integer storageSize) {
		boolean addUser = false;
		
		try {
			addUser = userDAO.addUser(login, password, email, profile, isPending, storageSize);
		} catch (Exception e) {
			HibernateUtil.rollbackAndCloseSession();
		}
		
		return addUser; 
	}

	public User getUserByMail(String email) {
		User user = null;
		try {
			user = userDAO.getUserByMail(email);
		} catch (Exception e){
			HibernateUtil.rollbackAndCloseSession();
		}
		
		return user;
	}

	public void updateUser(String login, String password, String email, String profile, Boolean pending, Integer storageSize) {
		try {
			userDAO.updateUser(login, password, email, profile, pending, storageSize);
		} catch (Exception e){
			HibernateUtil.rollbackAndCloseSession();
		}
	}
	
	public void approveUser(String login) {
		try {
			userDAO.approveUser(login);
		} catch (Exception e){
			HibernateUtil.rollbackAndCloseSession();
		}
	}
	
	public List<User> getUsers(int pageNumber, int limit, String adminLogin) {
		List<User> users = new LinkedList<User>();
		try {
			users = userDAO.getUsers(pageNumber, limit, adminLogin);
		} catch (Exception e){
			HibernateUtil.rollbackAndCloseSession();
		}
		
		return users;
	}
	
	public void removeUserByLogin(String login) {
		try {
			userDAO.removeUserByLogin(login);
		} catch (Exception e){
			HibernateUtil.rollbackAndCloseSession();
		}
	}

	public List<AbstractRequest> getJobsRequestByLogin(String login) {
		return requestDAO.getJobsRequestByLogin(login);
	}
	
	public String getUploadDirName(long uploadId) {
		return dataStoredDAO.getDataStore(uploadId);
	}

	public File getStoredFileByName(Long uploadId, String jdfName) {
		return dataStoredDAO.getStoredFileByName(uploadId, jdfName);
	}
	
	public void setStorageSize(String login, Integer storageSize) {
		try {
			userDAO.setStorageSize(login, storageSize);
		} catch (Exception e){
			HibernateUtil.rollbackAndCloseSession();
		}
	}
	
}