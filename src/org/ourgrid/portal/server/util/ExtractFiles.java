package org.ourgrid.portal.server.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExtractFiles {
	
	private Extractor strategy;
	private File file;
	
	public ExtractFiles(File file) {
		this.file = file;
		
		String extension = readFileExtension(file); 
		if (extension.equals(".zip")) {
			strategy = new ZipPackFileProcessor();
		} else {
			strategy = new TarPackFileProcessor();
		}
	}
	
	public List<File> unpack(String destination) throws IOException {
		File tempPackFolder = new File(destination);
		
		String filePath = file.getAbsolutePath();
		String packedFolderPath = tempPackFolder.getAbsolutePath();
		
		return strategy.extract(filePath, packedFolderPath);
	}
	
	private String readFileExtension(File file) {
		String fileAbsolutePath = file.getAbsolutePath();
		return fileAbsolutePath.substring(fileAbsolutePath.lastIndexOf("."));
	}
	
	
	
}
