package org.ourgrid.portal.server.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtil {
	
	public static List<File> filterFiles (String filePath, String extension) {
		File rootDir = new File(filePath);
		List<File> results = new ArrayList<File>();
		
		for (File file : getFiles(rootDir)) {
			if (file.getAbsolutePath().endsWith(extension)) {
				results.add(file);
			}
		}
		return results;
	}
	
	public static List<File> getFiles(File directory) {
		List<File> files = Arrays.asList(directory.listFiles());
		
		List<File> results = new ArrayList<File>();
		if (files != null)
			for (File file : files) {
				if (file.isDirectory()) {
					results.addAll(getFiles(file));
				}
				results.add(file);
			}
		return results;
	}
}