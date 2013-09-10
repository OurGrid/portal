package org.ourgrid.portal.server.logic.executors;

import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;

public interface ExecutorIF {
	
	public void logTransaction(ServiceTO serviceTO);
	
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException;
}
