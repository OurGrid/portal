package org.ourgrid.portal.server.logic.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.ourgrid.common.specification.job.IOBlock;
import org.ourgrid.common.specification.job.IOEntry;
import org.ourgrid.common.specification.job.JobSpecification;
import org.ourgrid.common.specification.job.TaskSpecification;
import org.ourgrid.common.specification.main.CompilerException;
import org.ourgrid.common.specification.main.DescriptionFileCompile;
import org.ourgrid.portal.server.exceptions.InvalidJdfException;
import org.ourgrid.portal.server.logic.interfaces.JDFHandler;

public class JDFProcessor implements JDFHandler {
	
	public JobSpecification compileJdf(File jdf) throws InvalidJdfException {
		JobSpecification theJob;
		
		if (jdf == null) {
			throw new InvalidJdfException();
		}
		
		try {
			 theJob = DescriptionFileCompile.compileJDF( jdf.getAbsolutePath() );
		} catch (CompilerException e) {
			throw new InvalidJdfException();
		}
		return theJob;
	}
	
	public void verifyJobIO(JobSpecification theJob, String relativePath) throws InvalidJdfException {
		for (TaskSpecification task : theJob.getTaskSpecs()) {
			checkInitBlock(task.getInitBlock(), relativePath);
			checkFinalBlock(task.getFinalBlock(), relativePath);
		}
	}
	
	private void checkFinalBlock(IOBlock finalBlock, String relativePath) throws InvalidJdfException {
		checkBlock(finalBlock, relativePath, false);
	}

	private void checkInitBlock(IOBlock initBlock, String relativePath) throws InvalidJdfException {
		checkBlock(initBlock, relativePath, true);
	}

	private void checkBlock(IOBlock block, String relativePath, boolean isInitBlock) throws InvalidJdfException {
		List<IOEntry> entries = block.getEntry("");
		
		if (entries == null) {
			return;
		}
		
		for (IOEntry entry : entries) {
			String destination = isInitBlock ? entry.getSourceFile() : entry.getDestination(); 

			try {
				if(!isRelativeTo(destination, relativePath)) {
					throw new InvalidJdfException();
				}
			} catch (IOException e) {
				throw new InvalidJdfException();
			}
		}
	}

	private boolean isRelativeTo(String filePath, String directory) throws IOException {

		final String fileCanPath = new File(filePath).getCanonicalPath();
		final String dirCanPath = new File(directory).getCanonicalPath();

		return fileCanPath.startsWith(dirCanPath);
	}

}
