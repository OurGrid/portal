package org.ourgrid.portal.client.common.gui.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserModel {
	
	private Long uploadSessionId;
	
	private String userLogin;
	
	private Map<Integer, Integer> relativeIdToJobId;
	
	private Map<Integer, List<Integer>> jobIdToPagedTaskId;

	public UserModel(String login, Long uploadSessionId, Map<Integer, Integer> relativeIdToJobId, Map<Integer, List<Integer>> jobIdToPagedTasksId) {
		this.userLogin = login;
		this.relativeIdToJobId = relativeIdToJobId;
		this.uploadSessionId = uploadSessionId;
		this.jobIdToPagedTaskId = jobIdToPagedTasksId;
	}

	public UserModel(String login) {
		this(login, 0L, new HashMap<Integer, Integer>(), new HashMap<Integer, List<Integer>>());
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

	public List<Integer> getPagedTasksId(Integer jobId) {
		List<Integer> requests = jobIdToPagedTaskId.get(jobId);
		if (requests != null) {
			return requests;
		}
		return new LinkedList<Integer>();
	}

	public Integer getJobId(Integer index) {
		return this.relativeIdToJobId.get(index);
	}

	public void addPagedTaskRequest(Integer jobId, Integer taskId) {
		getPagedTasksId(jobId).add(taskId);
	}
	
	public void setPagedTaskIds(Integer jobId, List<Integer> requests) {
		jobIdToPagedTaskId.put(jobId, requests);
	}

	public void removeJobId(Integer index) {
		this.relativeIdToJobId.remove(index);
	}

	public void addJobId(Integer jobTabIndex, Integer jobId) {
			this.relativeIdToJobId.put(jobTabIndex, jobId);
	}

	public boolean containsPagedTaskId(Integer jobId, Integer taskId) {
		List<Integer> requests = getPagedTasksId(jobId);
		return requests.contains(taskId) ;
	}

	public void removePagedTaskRequest(Integer jobId, Integer firstTaskId) {
		getPagedTasksId(jobId).remove(firstTaskId);
	}
	
}