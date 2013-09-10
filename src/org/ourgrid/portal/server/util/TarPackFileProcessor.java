package org.ourgrid.portal.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;


public class TarPackFileProcessor implements Extractor{

	private static final int BUFFER_SIZE = 512;
	
	private String root;
	
	public List<File> extract(String path, String destination) {
		
		File destinationDir = new File(destination);
		if(!destinationDir.exists()){
			destinationDir.mkdirs();
		}
		
		if(!destinationDir.isDirectory()){
			throw new IllegalArgumentException("Destination should be a directory.");
		}
		
		try {
			readTar(getInputStream(path), destination);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return getFiles(destinationDir);
	}
	
	public static InputStream getInputStream(String path) throws Exception{  
			if(path.substring(path.lastIndexOf(".") + 1, path.lastIndexOf(".")+ 3).equalsIgnoreCase("gz")) {  
	               return new GZIPInputStream(new FileInputStream(new File(path)));  
			}  
			else {  
				return new FileInputStream(new File(path));  
	           }  
	}

	
   public static void readTar(InputStream in, String untarDir) throws IOException{  
         TarInputStream tin = new TarInputStream(in);  
         TarEntry tarEntry = tin.getNextEntry();  
         if(new File(untarDir).exists()){  
             while (tarEntry != null){  
                File destPath = new File(untarDir + File.separatorChar + tarEntry.getName());  
                if(!tarEntry.isDirectory()){  
                   FileOutputStream fout = new FileOutputStream(destPath);  
                   tin.copyEntryContents(fout);  
                   fout.close();  
                }else{  
                   destPath.mkdir();  
                }  
                tarEntry = tin.getNextEntry();  
             }  
             tin.close();  
         }else{  
        	 //TODO Lancar excecao
        	 System.out.println("That destination directory doesn't exist! " + untarDir);  
         }  
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
		return null;
	}
}
