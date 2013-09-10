package org.ourgrid.portal.server.logic.executors;

import org.apache.log4j.Logger;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;

public abstract class AbstractExecutor implements ExecutorIF {

	private OurGridPortalIF portal;
	
	protected final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	public AbstractExecutor(OurGridPortalIF portal) {
		this.portal = portal;
	}

	public OurGridPortalIF getPortal() {
		return portal;
	}
	
	public Logger getLogger() {
		return portal.getLogger();
	}
	
	public abstract void logTransaction(ServiceTO serviceTO);

	public abstract ResponseTO execute(ServiceTO serviceTO) throws ExecutionException;
	
}