package org.ourgrid.portal.client.common.to.model;

import java.io.Serializable;
import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;

public class TaskTO extends AbstractTreeNodeTO implements Serializable {

	private static final long serialVersionUID = -3158063179859231079L;
	
	private static final String status = "status";
	private static final String spec = "spec";
	
	private static final String results = "results";
	private static final String actualFails = "actualFails";
	private static final String jobId = "jobId";
	
	private static final String numProcesses = "numProcesses";
	private static final String numRunningProcesses = "numRunningProcesses";
	
	private static final String HTML_INIT = "<span style=\"font-size:70%;\">";
	private static final String LINE_SEPARATOR = "<br>";
	private static final String HTML_FINISH = "</span>";
	
	public TaskTO() {
		super();
		setType("TaskTO");
	}
	
	public Integer getId() {
		return get(id);
	}
	
	public String getStatus() {
		return get(status);
	}
	
	public String getSpec() {
		return get(spec);
	}
	
	public List<String> getResults() {
		return get(results);
	}
	
	public Integer getActualFails() {
		return get(actualFails);
	}
	
	public Integer getJobId() {
		return get(jobId);
	}
	
	public Integer getNumProcesses() {
		return get(numProcesses);
	}
	
	public Integer getNumRunningProcesses() {
		return get(numRunningProcesses);
	}
	
	public void setStatus(String status) {
		set(TaskTO.status, status);
	}

	public void setSpec(String spec) {
		set(TaskTO.spec, spec);
	}
	
	public void setResultsName(List<String> results) {
		set(TaskTO.results, results);
	}

	public void setActualFails(Integer actualFails) {
		set(TaskTO.actualFails, actualFails);
	}

	public void setJobId(Integer jobId) {
		set(TaskTO.jobId, jobId);
	}
	
	public void setNumProcesses(Integer numProcesses) {
		set(TaskTO.numProcesses, numProcesses);
	}

	public void setNumRunningProcesses(Integer runningProcesses) {
		set(TaskTO.numRunningProcesses, runningProcesses);
	}
	
	public void setDescription() {
		String description = "";
		
		description += HTML_INIT;
//		description += " JOB: " + getJobId() + LINE_SEPARATOR;
		description += " TASK: " + getId() + LINE_SEPARATOR;
		description += " TOTAL PROCESSES: " + getNumProcesses() + LINE_SEPARATOR;
		description += " RUNNING PROCESSES: " + getNumRunningProcesses() + LINE_SEPARATOR;
		description += " ACTUAL FAILS: " + getActualFails() + LINE_SEPARATOR;
		description += " STATE: " + getStatus() + LINE_SEPARATOR;
		description += "-----" + LINE_SEPARATOR;
		description += getSpec().toString().replaceAll("\n", LINE_SEPARATOR);
		description += HTML_FINISH;
		
		set(TaskTO.description, description);
	}

	public void setProcesses(List<ProcessTO> processes) {
		for (ProcessTO processTO : processes) {
			add(processTO);
		}
	}
	
	public String toString() {
		return "Task " + this.getId() + ": [ " + this.getStatus() + " ]"; 
	}

	public List<ModelData> getProcesses() {
		return getChildren();
	}
}