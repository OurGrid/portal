package org.ourgrid.portal.server.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipPackFileProcessor implements Extractor{

	private static final int BUFFER_SIZE = 512;
	
	private String root;
	
	public List<File> extract(String path, String destination) {

		File zipFile = new File(path);
		if(!zipFile.exists()){
			throw new IllegalArgumentException("The zip file should be informed.");
		}

		File destinationDir = new File(destination);
		if(!destinationDir.exists()){
			destinationDir.mkdirs();
		}

		if(!destinationDir.isDirectory()){
			throw new IllegalArgumentException("Destination should be a directory.");
		}

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(zipFile);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("The zip file should be informed.");
		}

		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
		ZipEntry entry;

		try {
			while ((entry = zis.getNextEntry()) != null) {
				byte data[] = new byte[BUFFER_SIZE];

				String entryPath = destinationDir.getAbsolutePath() + System.getProperty("file.separator") + entry.getName();

				if (entry.isDirectory()) {

					File dir = new File(entryPath);
					dir.mkdirs();

				} else {

					File file = new File(entryPath);
					
					if(!file.getParentFile().exists()){
						file.getParentFile().mkdirs();
					}
					
					FileOutputStream fos = new FileOutputStream(file);
					BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE);

					int count;
					while ((count = zis.read(data, 0, BUFFER_SIZE)) != -1) {
						dest.write(data, 0, count);
					}

					dest.flush();
					dest.close();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			zis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return getFiles(destinationDir);
	}

	private List<File> getFiles(File directory) {
		List<File> files = Arrays.asList(directory.listFiles());

		List<File> results = new ArrayList<File>();

		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					results.addAll(getFiles(file));
				}
				results.add(file);
			}
		}
		return results;
	}

	public File compress(String path, String parentPath, String destination){
	
	
		File dirObj = new File(path); 
	
		if(isEmpty(dirObj)){
			return null;
		}
		
		if(!dirObj.isDirectory()) { 
			return null;
		} 
	
		try { 
	
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(destination)); 
			dirObj.getName();
			root = parentPath;
			addDir(dirObj, out); 
			out.close(); 
	
	
		} catch (IOException e) { 
			e.printStackTrace(); 
		}
		return new File(destination); 
	
	} 

	private boolean isEmpty(File dirObj) {
		
		for (File file : dirObj.listFiles()) {
			if(file.isFile()){
				return false;
			}
			return isEmpty(file);
		}
		return true;
	}

	private void addDir(File dirObj, ZipOutputStream out) throws IOException { 
		File[] files = dirObj.listFiles(); 
		byte[] tmpBuf = new byte[BUFFER_SIZE]; 
	
		for (int i=0; i < files.length; i++) { 
			if(files[i].isDirectory()) { 
				addDir(files[i], out); 
				continue; 
			} 
	
			FileInputStream in = new FileInputStream(files[i].getAbsolutePath()); 
	
			String path = files[i].getAbsolutePath();
			path = path.replace(root, "");

			out.putNextEntry(new ZipEntry(path)); 
	
			int len; 
			while((len = in.read(tmpBuf)) > 0) { 
				out.write(tmpBuf, 0, len); 
			} 
	
			out.closeEntry(); 
			in.close(); 
		} 
	}

}
