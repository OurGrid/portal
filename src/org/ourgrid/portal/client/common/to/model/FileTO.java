package org.ourgrid.portal.client.common.to.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseTreeModel;

public class FileTO extends BaseTreeModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -719145499438578302L;

	private static int ID = 0;
	
	private List<FileTO> list;
  
	public FileTO() {
		super();
		list = new ArrayList<FileTO>();
		set("id", ID++);
	}

  	public FileTO(String name, String location, boolean isFolder) {
  		super();
  		list = new ArrayList<FileTO>();
  		set("location", location);
  		set("name", name);
    	set("isFolder", isFolder);
  	}

	public FileTO(String name) {
		super();
		list = new ArrayList<FileTO>();
		set("name", name);
	}

  	public FileTO(String name, List<FileTO> children, String location, boolean isFolder) {
  		super();
  		list = new ArrayList<FileTO>();
  		set("expanded", true);
  		set("name", name);
  		set("isFolder", isFolder);
  		set("location", location);
  		set("hasChildren", !children.isEmpty());
  		for (BaseTreeModel baseTreeModel : children) {
			add(baseTreeModel);
		}
  	}

  	public FileTO(String name, String size, String date, String location, boolean isFolder) {
  		super();
  		set("expanded", true);
  		set("name", name);
  		set("size", size);
  		set("date", date);
  		set("location", location);
  		set("isFolder", isFolder);
	}

	public Integer getId() {
  		return (Integer) get("id");
  	}

  	public String getName() {
  		return (String) get("name");
  	}
  	
	public String getSize() {
		return (String) get("size");
 	}

	public String getDate() {
    	return (String) get("date");
	}
	
	public void setDate(String date) {
    	set("date", date);
	}
	
	public String getLocation() {
    	return (String) get("location");
	}
	
	public void setLocation(String location) {
    	set("location", location);
	}
	
	public void addChildrens(List<FileTO> children){
 		for (FileTO fileTO : children) {
			add(fileTO);
		}
	}
	
  	public Boolean isFolder() {
  		return (Boolean) get("isFolder");
  	}

  	public String toString() {
  		return getName();
  	}

	public List<FileTO> getListFiles() {
		return list;
	}

	public void setListFiles(List<FileTO> listFiles) {
		this.list = listFiles;
	}
	
	public void setExpanded(boolean expanded) {
		set("expanded", expanded);
	}

	public boolean isExpanded() {
		return get("expanded");
	}

	public void setName(String newName) {
		set("name", newName);
	}
	
	public void setHasChildren(boolean hasChildren) {
		set("hasChildren", hasChildren);
	}
	
	public boolean hasChildren(){
		return get("hasChildren");
	}
	
	@Override
	public boolean equals(Object o){
		FileTO file = (FileTO) o;
		return this.getName().equals(file.getName()) 
			&& this.getLocation().equals(file.getLocation())
			&& this.isFolder().equals(file.isFolder());
	}
}