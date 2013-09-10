package org.ourgrid.portal.server.logic.gridcommunication;

import java.io.Serializable;

public class PagedTaskRequest implements Serializable {
	
	private static final long serialVersionUID = 8552061572315941087L;

	public Integer pageOffset;
	
	public Integer pageSize;
	
	public PagedTaskRequest(Integer pageOffset, Integer pageSize) {
		this.pageOffset = pageOffset;
		this.pageSize = pageSize;
	}

	public Integer getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((pageOffset == null) ? 0 : pageOffset.hashCode());
		result = prime * result
				+ ((pageSize == null) ? 0 : pageSize.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PagedTaskRequest other = (PagedTaskRequest) obj;
		if (pageOffset == null) {
			if (other.pageOffset != null)
				return false;
		} else if (!pageOffset.equals(other.pageOffset))
			return false;
		if (pageSize == null) {
			if (other.pageSize != null)
				return false;
		} else if (!pageSize.equals(other.pageSize))
			return false;
		return true;
	}

}
