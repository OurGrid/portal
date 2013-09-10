package org.ourgrid.portal.client.common.to.response;

import java.io.Serializable;
import java.util.List;

import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.common.to.model.UserTO;


public class UserLoginResponseTO extends ResponseTO implements Serializable {
	
	private static final long serialVersionUID = 3364738242452557570L;

	private UserTO loggedUser;
	
	private List<AbstractRequest>jobsRequestList;

	public UserLoginResponseTO() {
		super();
	}
	
	public UserTO getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(UserTO loggedUser) {
		this.loggedUser = loggedUser;
	}

	public List<AbstractRequest> getJobsRequestList() {
		return jobsRequestList;
	}

	public void setJobsRequestList(List<AbstractRequest> jobsRequestList) {
		this.jobsRequestList = jobsRequestList;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((jobsRequestList == null) ? 0 : jobsRequestList.hashCode());
		result = prime * result
				+ ((loggedUser == null) ? 0 : loggedUser.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserLoginResponseTO other = (UserLoginResponseTO) obj;
		if (jobsRequestList == null) {
			if (other.jobsRequestList != null)
				return false;
		} else if (!jobsRequestList.equals(other.jobsRequestList))
			return false;
		if (loggedUser == null) {
			if (other.loggedUser != null)
				return false;
		} else if (!loggedUser.equals(other.loggedUser))
			return false;
		return true;
	}
	
}