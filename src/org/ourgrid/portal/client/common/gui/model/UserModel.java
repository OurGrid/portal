package org.ourgrid.portal.client.common.gui.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserModel {
	
	private Long uploadSessionId;
	
	private String userLogin;
	
	private Map<Integer, Integer> relativeIdToJobId;
	private Map<Integer, Set<Integer>> pagesFirstTaskIdPerJob;
	
	public UserModel(String login, Long uploadSessionId, Map<Integer, Integer> relativeIdToJobId, 
			Map<Integer, Set<Integer>> pagesFirstTaskIdPerJob) {
		this.userLogin = login;
		this.relativeIdToJobId = relativeIdToJobId;
		this.uploadSessionId = uploadSessionId;
		this.pagesFirstTaskIdPerJob = pagesFirstTaskIdPerJob;
	}

	public UserModel(String login) {
		this(login, 0L, new HashMap<Integer, Integer>(), 
				new HashMap<Integer, Set<Integer>>());
	}

	public UserModel() {
		this("");
	}
	
	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	/**
	 * @return the uploadSessionId
	 */
	public Long getUploadSessionId() {
		return uploadSessionId;
	}

	/**
	 * @param uploadSessionId the uploadSessionId to set
	 */
	public void setUploadSessionId(Long uploadSessionId) {
		this.uploadSessionId = uploadSessionId;
	}

	public Integer getJobId(Integer index) {
		return this.relativeIdToJobId.get(index);
	}

	public void removeJobId(Integer index) {
		Integer jobId = this.relativeIdToJobId.remove(index);
		if (jobId != null) {
			this.pagesFirstTaskIdPerJob.remove(jobId);
		}
	}
	
	public void addTaskPage(Integer jobId, Integer taskPageFirstTaskId) {
		Set<Integer> pageSet = pagesFirstTaskIdPerJob.get(jobId);
		pageSet.add(taskPageFirstTaskId);
	}

	public void addJobId(Integer jobTabIndex, Integer jobId) {
		this.relativeIdToJobId.put(jobTabIndex, jobId);
		this.pagesFirstTaskIdPerJob.put(jobId, new HashSet<Integer>());
	}

	public Collection<? extends Integer> getPagesFirstTaskIds(Integer jobId) {
		return pagesFirstTaskIdPerJob.get(jobId);
	}

}