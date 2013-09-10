package org.ourgrid.portal.server.logic.executors;

import org.ourgrid.portal.client.common.to.response.ResponseTO;

public abstract class Executor implements ExecutorIF {
	
	private String name;
	
	public Executor(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public abstract ResponseTO execute();

}
