package org.ourgrid.portal.client.common.to.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;

public class TaskPageTO extends AbstractTreeNodeTO implements Serializable  {

	private static final long serialVersionUID = -8472440531517374465L;
	
	private static final String firstTaskId = "firstTaskId";
	private static final String lastTaskId = "lastTaskId";
	public static final String typeValue = "TaskPageTO";
	public static final String id = "id";
	
	public TaskPageTO() {
		super();
		setType(typeValue);
		setDescription("");
	}

	public Integer getFirstTaskId() {
		return get(TaskPageTO.firstTaskId);
	}
	
	public Integer getLastTaskId() {
		return get(TaskPageTO.lastTaskId);
	}
	
	public String getId() {
		return get(TaskPageTO.id);
	}
	
	public void setFirstTaskId(Integer firstTaskId) {
		set(TaskPageTO.firstTaskId, firstTaskId);
	}
	
	public void setLastTaskId(Integer lastTaskId) {
		set(TaskPageTO.lastTaskId, lastTaskId);
	}
	
	public void setId(String id) {
		set(TaskPageTO.id, id);
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
	
	public TaskPageTO clone() {
		TaskPageTO copySuperTaskTO = new TaskPageTO();
		copySuperTaskTO.setProperties(new HashMap<String, Object>(this.getProperties()));
		return copySuperTaskTO;
	}

}