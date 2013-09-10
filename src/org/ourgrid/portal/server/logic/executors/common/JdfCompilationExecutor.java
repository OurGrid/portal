package org.ourgrid.portal.server.logic.executors.common;

import java.io.File;

import org.ourgrid.common.specification.job.JobSpecification;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.exceptions.InvalidJdfException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.executors.to.response.JdfCompilationResponseTO;
import org.ourgrid.portal.server.logic.executors.to.service.JdfCompilationTO;
import org.ourgrid.portal.server.logic.interfaces.JDFHandler;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.logic.util.JDFProcessor;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;

public class JdfCompilationExecutor extends AbstractExecutor{

	public JdfCompilationExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		JdfCompilationTO jdfValidatorTO = (JdfCompilationTO) serviceTO;
		JdfCompilationResponseTO jdfCompilationResponseTO = new JdfCompilationResponseTO();
		jdfCompilationResponseTO.setJdfValidatorTO(jdfValidatorTO);
		
		JDFHandler jdfProcessor = new JDFProcessor();
		JobSpecification theJob;
		
		try {
			File jdfFile = jdfValidatorTO.getJdfFile();
			theJob = jdfProcessor.compileJdf(jdfFile);
			jdfProcessor.verifyJobIO(theJob, jdfFile.getParentFile().getAbsolutePath());
		} catch (InvalidJdfException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.SUBMIT_INVALID_JDF_MSG);
		}
		
		jdfCompilationResponseTO.setJobSpecification(theJob);
		
		return jdfCompilationResponseTO;
	}
	
	public void logTransaction(ServiceTO serviceTO) {
		
		JdfCompilationTO jdfValidatorTO = (JdfCompilationTO) serviceTO;
		
		getLogger().info("Executor Name: Jdf Validation" + LINE_SEPARATOR + 
				"Parameters: "  + LINE_SEPARATOR + 
				" Jdf File Name: " + jdfValidatorTO.getJdfFile());
	}
}