package org.ourgrid.portal.server.util;

import java.io.File;
import java.io.IOException;

public class CompressFiles {
	
	private Extractor strategy;
	private File file;
	
	public CompressFiles(File file) {
		this.file = file;
		
		strategy = new ZipPackFileProcessor();
	}
	
	public File pack() throws IOException {
				
		String filePath = file.getAbsolutePath();
		String parentPath = file.getParentFile().getAbsolutePath() + File.separator;
		String outFileName = filePath.replace(parentPath, "");
		String destination = parentPath + outFileName + ".zip";
		
		return strategy.compress(filePath, parentPath, destination);
	}
}
