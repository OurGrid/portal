package org.ourgrid.portal.client.common.to.response;

import java.util.List;

import org.ourgrid.portal.client.common.to.model.FileTO;

public class PasteFileResponseTO extends ResponseTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<FileTO> cutListFiles;
	
	private boolean isCopy;

	private FileTO root;

	private FileTO source;
	
	public PasteFileResponseTO() {
		super();
	}
	
	public List<FileTO> getListFiles() {
		return cutListFiles;
	}

	public void setListFiles(List<FileTO> listFiles) {
		this.cutListFiles = listFiles;
	}

	public boolean isCopy() {
		return isCopy;
	}

	public void setIsCopy(boolean isCopy) {
		this.isCopy = isCopy;
	}
	
	public FileTO getRoot(){
		return root;
	}
	
	public void setRoot(FileTO root){
		this.root = root;
	}
	
	public FileTO getSource(){
		return source;
	}
	
	public void setSource(FileTO source){
		this.source = source;
	}
}