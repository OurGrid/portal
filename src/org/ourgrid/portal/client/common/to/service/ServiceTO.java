package org.ourgrid.portal.client.common.to.service;

import java.io.Serializable;

public class ServiceTO implements Serializable {
	
	private static final long serialVersionUID = 9078611476716817733L;

	private String executorName;
	
	public ServiceTO() {
		
	}
	
	public String getExecutorName() {
		return executorName;
	}

	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((executorName == null) ? 0 : executorName.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceTO other = (ServiceTO) obj;
		if (executorName == null) {
			if (other.executorName != null)
				return false;
		} else if (!executorName.equals(other.executorName))
			return false;
		return true;
	}
}