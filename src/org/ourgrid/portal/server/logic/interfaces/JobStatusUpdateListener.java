package org.ourgrid.portal.server.logic.interfaces;

import java.util.List;

import org.ourgrid.broker.status.TaskStatusInfo;
import org.ourgrid.common.interfaces.to.JobsPackage;


public interface JobStatusUpdateListener {
	
	public void hereIsJobDescription(JobsPackage jobPackage);
	
	public void hereIsPagedTasks(Integer offset, List<TaskStatusInfo> pagedTasks);

}
