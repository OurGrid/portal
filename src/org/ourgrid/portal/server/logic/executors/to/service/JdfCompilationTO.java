package org.ourgrid.portal.server.logic.executors.to.service;

import java.io.File;

import org.ourgrid.portal.client.common.to.service.ServiceTO;

public class JdfCompilationTO extends ServiceTO {

	private static final long serialVersionUID = 7617422579728408805L;
	
	private File jdfFile;

	public void setJdfFile(File jdfFile) {
		this.jdfFile = jdfFile;
	}

	public File getJdfFile() {
		return jdfFile;
	}
}