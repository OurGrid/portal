package org.ourgrid.portal.client.common.to.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;

public class SuperTaskTO extends AbstractTreeNodeTO implements Serializable  {

	private static final long serialVersionUID = -8472440531517374465L;
	
	private static final String firstTaskId = "firstTaskId";
	private static final String lastTaskId = "lastTaskId";
	public static final String typeValue = "SuperTaskTO";
	public static final String id = "id";
	
	public SuperTaskTO() {
		super();
		setType(typeValue);
		setDescription("");
	}

	public Integer getFirstTaskId() {
		return get(SuperTaskTO.firstTaskId);
	}
	
	public Integer getLastTaskId() {
		return get(SuperTaskTO.lastTaskId);
	}
	
	public String getId() {
		return get(SuperTaskTO.id);
	}
	
	public void setFirstTaskId(Integer firstTaskId) {
		set(SuperTaskTO.firstTaskId, firstTaskId);
	}
	
	public void setLastTaskId(Integer lastTaskId) {
		set(SuperTaskTO.lastTaskId, lastTaskId);
	}
	
	public void setId(String id) {
		set(SuperTaskTO.id, id);
	}
	
	public void setTasks(List<TaskTO> tasks) {
		
		for (TaskTO taskTO : tasks) {
			add(taskTO);
		}
	}

	public String toString() {
		return "Tasks " + (getFirstTaskId() + 1) + " to " + getLastTaskId();
	}

	public List<ModelData> getTasks() {
		return getChildren();
	}
	
	public SuperTaskTO clone() {
		SuperTaskTO copySuperTaskTO = new SuperTaskTO();
		copySuperTaskTO.setProperties(new HashMap<String, Object>(this.getProperties()));
		return copySuperTaskTO;
	}

}