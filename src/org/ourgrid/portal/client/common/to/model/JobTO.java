package org.ourgrid.portal.client.common.to.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;

public class JobTO extends AbstractTreeNodeTO implements Serializable  {

	private static final long serialVersionUID = -8472440531517374465L;
	
	private static final String relativeId = "relativeId";
	private static final String label = "label";

	private static final String requirements = "requirements";
	private static final String status = "status";
	
	private static final String numTasks = "numTasks";
	private static final String numUnstartedTasks = "numUnstartedTasks";
	private static final String numRunningTasks = "numRunningTasks";
	private static final String numFinishedTasks = "numFinishedTasks";
	private static final String numFailedTasks = "numFailedTasks";
	private static final String numCancelledTasks = "numCancelledTasks";
	
	private static final String numProcesses = "numProcesses";
	private static final String numRunningProcesses = "numRunningProcesses";
	private static final String numFinishedProcesses = "numFinishedProcesses";
	private static final String numAbortedProcesses = "numAbortedProcesses";
	private static final String numFailedProcesses = "numFailedProcesses";
	private static final String numCancelledProcesses = "numCancelledProcesses";
	private static final String numSabotagedProcesses = "numSabotagedProcesses";
	
	private static final String HTML_INIT = "<span style=\"font-size:70%;\">";
	private static final String LINE_SEPARATOR = "<br>";
	private static final String HTML_FINISH = "</span>";
	
	public static final String typeValue = "JobTO";
	
	public JobTO() {
		super();
		setType(typeValue);
	}
	
	public Integer getId() {
		return get(id);
	}
	
	public Integer getRelativeId(){
		return get(relativeId);
	}
	
	public String getLabel() {
		return get(label);
	}
	
	public String getRequirements() {
		return get(requirements);
	}
	
	public String getStatus() {
		return get(status);
	}
	
	public Integer getNumTasks() {
		return get(numTasks);
	}
	
	public Integer getNumUnstartedTasks() {
		return get(numUnstartedTasks);
	}
	
	public Integer getNumRunningTasks() {
		return get(numRunningTasks);
	}
	
	public Integer getNumFinishedTasks() {
		return get(numFinishedTasks);
	}
	
	public Integer getNumFailedTasks() {
		return get(numFailedTasks);
	}
	
	public Integer getNumCancelledTasks() {
		return get(numCancelledTasks);
	}
	
	public Integer getNumProcesses() {
		return get(numProcesses);
	}
	
	public Integer getNumRunningProcesses() {
		return get(numRunningProcesses);
	}
	
	public Integer getNumFinishedProcesses() {
		return get(numFinishedProcesses);
	}
	
	public Integer getNumAbortedProcesses() {
		return get(numAbortedProcesses);
	}
	
	public Integer getNumFailedProcesses() {
		return get(numFailedProcesses);
	}
	
	public Integer getNumCancelledProcesses() {
		return get(numCancelledProcesses);
	}
	
	public Integer getNumSabotagedProcesses() {
		return get(numSabotagedProcesses);
	}
	
	public String getName() {
		return get(name);
	}
	
	public void setRelativeId(Integer id){
		set(relativeId, id);
	}
	
	public void setLabel(String label) {
		set(JobTO.label, label);
	}
	
	public void setRequirements(String requirements) {
		set(JobTO.requirements, requirements);
	}
	
	public void setStatus(String status) {
		set(JobTO.status, status);
	}

	public void setNumTasks(Integer numTasks) {
		set(JobTO.numTasks, numTasks);
	}
	
	public void setNumUnstartedTasks(Integer numUnstartedTasks) {
		set(JobTO.numUnstartedTasks, numUnstartedTasks);
	}

	public void setNumRunningTasks(Integer numRunningTasks) {
		set(JobTO.numRunningTasks, numRunningTasks);
	}

	public void setNumFinishedTasks(Integer numFinishedTasks) {
		set(JobTO.numFinishedTasks, numFinishedTasks);
	}

	public void setNumFailedTasks(Integer numFailedTasks) {
		set(JobTO.numFailedTasks, numFailedTasks);
	}

	public void setNumCancelledTasks(Integer numCancelledTasks) {
		set(JobTO.numCancelledTasks, numCancelledTasks);
	}
	
	public void setNumProcesses(Integer numProcesses) {
		set(JobTO.numProcesses, numProcesses);
	}

	public void setNumRunningProcesses(Integer numRunningProcesses) {
		set(JobTO.numRunningProcesses, numRunningProcesses);
	}

	public void setNumFinishedProcesses(Integer numFinishedProcesses) {
		set(JobTO.numFinishedProcesses, numFinishedProcesses);
	}

	public void setNumAbortedProcesses(Integer numAbortedProcesses) {
		set(JobTO.numAbortedProcesses, numAbortedProcesses);
	}

	public void setNumFailedProcesses(Integer numFailedProcesses) {
		set(JobTO.numFailedProcesses, numFailedProcesses);
	}

	public void setNumCancelledProcesses(Integer numCancelledProcesses) {
		set(JobTO.numCancelledProcesses, numCancelledProcesses);
	}

	public void setNumSabotagedProcesses(Integer numSabotagedProcesses) {
		set(JobTO.numSabotagedProcesses, numSabotagedProcesses);
	}
	
	public void setDescription() {
		String description = "";
		
		description += HTML_INIT;
//		description += " JOB: " + getRelativeId() + LINE_SEPARATOR;
		description += " REQUIREMENTS: " + getRequirements() + LINE_SEPARATOR;
		description += " STATE: " + getStatus() + LINE_SEPARATOR;
		description += "----" + LINE_SEPARATOR;
		
		description += "TASKS:" + LINE_SEPARATOR + LINE_SEPARATOR;
		description += " NUMBER OF TASKS: " + getNumTasks() + LINE_SEPARATOR;
		description += " UNSTARTED: " + getNumUnstartedTasks() + LINE_SEPARATOR;
		description += " RUNNING: " + getNumRunningTasks() + LINE_SEPARATOR;
		description += " FINISHED: " + getNumFinishedTasks() + LINE_SEPARATOR;
		description += " FAILED: " + getNumFailedTasks() + LINE_SEPARATOR;
		description += " CANCELLED: " + getNumCancelledTasks() + LINE_SEPARATOR;
		description += "----" + LINE_SEPARATOR;
		
		description += "PROCESSES:" + LINE_SEPARATOR + LINE_SEPARATOR;
		description += " RUNNING: " + getNumRunningProcesses() + LINE_SEPARATOR;
		description += " FINISHED: " + getNumFinishedProcesses() + LINE_SEPARATOR;
		description += " ABORTED: " + getNumAbortedProcesses() + LINE_SEPARATOR;
		description += " FAILED: " + getNumAbortedProcesses() + LINE_SEPARATOR;
		description += " CANCELLED: " + getNumCancelledProcesses() + LINE_SEPARATOR;
		description += " SABOTAGED: " + getNumSabotagedProcesses() + LINE_SEPARATOR + LINE_SEPARATOR;
		
		description += "CURRENT NUMBER OF WORKERS: " + getNumRunningProcesses();
		description += HTML_FINISH;
		setDescription(description);
	}
	
	public void setTasks(List<TaskPageTO> superTasks) {
		for (TaskPageTO taskTO : superTasks) {
			add(taskTO);
		}
	}

	public String toString() {
		return "Job " + this.getRelativeId() + " : " + this.getLabel() + " [ " + this.getStatus() + " ]";
	}

	public TaskPageTO getTaskPage(Integer offset) {
		for (ModelData child : getChildren()) {
			TaskPageTO superTaskTO = (TaskPageTO) child;
			if (superTaskTO.getFirstTaskId().equals(offset)) {
				return superTaskTO;
			}
		}
		return null;
	}
	
	public JobTO clone() {
		JobTO copyJobTO = new JobTO();
		copyJobTO.setProperties(new HashMap<String, Object>(this.getProperties()));
		return copyJobTO;
	}
	
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		JobTO other = (JobTO) obj;
		
		if (getRelativeId() == null || other.getRelativeId() == null) return false;
		
		return getRelativeId().equals(other.getRelativeId());
	}

}