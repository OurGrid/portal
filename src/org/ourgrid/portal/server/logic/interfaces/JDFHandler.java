package org.ourgrid.portal.server.logic.interfaces;

import java.io.File;

import org.ourgrid.common.specification.job.JobSpecification;
import org.ourgrid.portal.server.exceptions.InvalidJdfException;

public interface JDFHandler {

	public JobSpecification compileJdf(File jdfFile) throws InvalidJdfException;
	
	public void verifyJobIO(JobSpecification job, String relativePath) throws InvalidJdfException;
	
}
