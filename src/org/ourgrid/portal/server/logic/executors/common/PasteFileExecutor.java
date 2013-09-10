package org.ourgrid.portal.server.logic.executors.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ourgrid.portal.client.common.to.model.FileTO;
import org.ourgrid.portal.client.common.to.response.PasteFileResponseTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.PasteFileTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class PasteFileExecutor extends AbstractExecutor {

	public PasteFileExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		PasteFileTO pasteFileTO = (PasteFileTO) serviceTO;
		
		String storagePath = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.STORAGE_DIRECTORY);
		boolean isCopy = pasteFileTO.isCopy(); 
		
		pasteFiles(pasteFileTO, storagePath);
		
		if (!isCopy) {
			deleteFiles(pasteFileTO, storagePath);
		}
		
		PasteFileResponseTO responseTO = new PasteFileResponseTO();
		responseTO.setIsCopy(isCopy);
		responseTO.setListFiles(pasteFileTO.getListFiles());
		responseTO.setSource(pasteFileTO.getSource());
		
		return responseTO;
	}
	
	private void pasteFiles(PasteFileTO pasteFileTO, String storagePath) throws ExecutionException {
		
		String outPath = pasteFileTO.getRoot().getLocation();
			
		for (FileTO fileTO : pasteFileTO.getListFiles()) {
			
			if(!pasteFileTO.getRoot().getLocation().equals(fileTO.getLocation()) || pasteFileTO.isCopy()){
				
				String path = "";
				if(fileTO.isFolder()){
					path = fileTO.getLocation();
				}else{
					path = fileTO.getLocation() + fileTO.getName();
				}
				
				File inputFile = new File(storagePath + path);
			
			    if (!inputFile.exists()){
			    	throw new ExecutionException(OurGridPortalServiceMessages.NO_SUCH_FILE_OR_DIRECTORY +  fileTO.getName() );
			    }
			    
			    int index = 1;
			    File outputFile = new File(storagePath + outPath + fileTO.getName());
			    while (outputFile.exists()) {
			    	
			    	String nameBase = getNewName(fileTO);
					
			    	if(fileTO.isFolder()){
						outputFile = new File(storagePath + outPath + nameBase + "(" + index + ")");
					}else{
						String name = fileTO.getName();
						String ext = "";
						if(name.lastIndexOf(".") > 0){
							ext = name.substring(name.lastIndexOf(".")); 
						}
						String newName =  nameBase + "(" + index + ")"; 
						outputFile = new File(storagePath + outPath + newName + ext);
					}
					index++;
				}
			    
			    if(fileTO.isFolder()){
			    	copyDirectory(inputFile, outputFile);
			    }else{
			    	copyFile(inputFile, outputFile);
			    }
			}    
		}
	}
	
	private String getNewName(FileTO fileTO) {
		
		String name = fileTO.getName();
		if(name.lastIndexOf("(") >= 0 && name.lastIndexOf(")") >= 0 && name.lastIndexOf(")") > (name.lastIndexOf("(") + 1)){
			
			if(fileTO.isFolder()){
				if(name.lastIndexOf(")") == name.length()-1){
					String newName1 = name.substring(name.lastIndexOf("(")+1, name.lastIndexOf(")"));
					String newName2 = newName1.replaceAll("[a-zA-Z]", "" );
					if(newName1.equals(newName2)){
						name = name.substring(0, name.lastIndexOf("("));
					}
				}
			}else{
				if(name.lastIndexOf(".") > 0){
					if(name.lastIndexOf(")") == name.lastIndexOf(".")-1){
						String newName1 = name.substring(name.lastIndexOf("(")+1, name.lastIndexOf(")"));
						String newName2 = newName1.replaceAll("[a-zA-Z]", "" );
						if(newName1.equals(newName2)){
							name = name.substring(0, name.lastIndexOf("("));
					
						}
					}
				}else if(name.lastIndexOf(")") == (name.length()-1)){
					String newName1 = name.substring(name.lastIndexOf("(")+1, name.lastIndexOf(")"));
					String newName2 = newName1.replaceAll("[a-zA-Z]", "" );
					if(newName1.equals(newName2)){
						name = name.substring(0, name.lastIndexOf("("));
				
					}
				}
			}
		}else{
			if(!fileTO.isFolder() && name.lastIndexOf(".") > 0){
				name = name.substring(0,name.lastIndexOf("."));
			}
		}
		return name;
	}

	public void copyFile(File inputFile, File outputFile) throws ExecutionException{
	    
		FileReader in;
	    FileWriter out;
	    try {
			in = new FileReader(inputFile);
			out = new FileWriter(outputFile);
		} catch (FileNotFoundException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.NO_SUCH_FILE_OR_DIRECTORY +  inputFile + " " + outputFile );
		} catch (IOException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.NO_SUCH_FILE_OR_DIRECTORY +  inputFile + " " + outputFile );
		}

		int c;

	    try {
			while ((c = in.read()) != -1){
			  out.write(c);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.WRITE_PROTECTED +  inputFile );
		}
	}
	
	public void copyDirectory(File inputFile, File outputFile) throws ExecutionException{

		if (inputFile.isDirectory()){
		
			if (!outputFile.exists()){
				outputFile.mkdir();
			}
		
			String files[] = inputFile.list();
			for(int i = 0; i < files.length; i++){
				copyDirectory(new File(inputFile, files[i]), new File(outputFile, files[i]));
			}
		}else{
		
			if(!inputFile.exists()){
				throw new ExecutionException(OurGridPortalServiceMessages.NO_SUCH_FILE_OR_DIRECTORY +  inputFile);
			}else{
				copyFile(inputFile, outputFile);
			}
		}
	}

	private List<String> deleteFiles(PasteFileTO pasteFileTO, String storagePath)throws ExecutionException {
		
		List<String> locations = new ArrayList<String>();
		
		for (FileTO fileTO : pasteFileTO.getListFiles()) {
			
			if(!pasteFileTO.getRoot().getLocation().equals(fileTO.getLocation())){
				String fileName = fileTO.getName();
				
				String path = "";
				if(fileTO.isFolder()){
					path = fileTO.getLocation();
				}else{
					path = fileTO.getLocation() + fileTO.getName();
				}
	
				File file = new File(storagePath + path);
				
			    if (!file.exists()){
			    	throw new ExecutionException(OurGridPortalServiceMessages.NO_SUCH_FILE_OR_DIRECTORY + fileName );
			    }
			
			    if (!file.canWrite()){
			      throw new  ExecutionException(OurGridPortalServiceMessages.WRITE_PROTECTED + fileName );
			    }
			    
			    boolean success =  deleteRecursive(file);
	
			    if (!success){
			      throw new ExecutionException(OurGridPortalServiceMessages.DELETE_FILE_ERROR + fileName);
			    }
			    
			    locations.add(path + File.separator);
			}
		}
		return locations;
	}
	
	private boolean deleteRecursive(File path) throws ExecutionException {

		if( path.exists() ) {
			if (path.isDirectory()) {
				File[] files = path.listFiles();
                
				for(int i=0; i<files.length; i++) {
                
					if(files[i].isDirectory()) {
						deleteRecursive(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
        return(path.delete());
	}

	public void logTransaction(ServiceTO serviceTO) {
		
		PasteFileTO pasteFileTO = (PasteFileTO) serviceTO;
		
		getLogger().info("Executor Name: Paste File" + LINE_SEPARATOR +
				"Parameters: " + "File Name: " + pasteFileTO.getListFiles() + LINE_SEPARATOR + 
				"Location:" + pasteFileTO.getRoot().getLocation());
	}
}