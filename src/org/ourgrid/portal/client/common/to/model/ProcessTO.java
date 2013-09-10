package org.ourgrid.portal.client.common.to.model;

import java.io.Serializable;
import java.util.List;

public class ProcessTO extends AbstractTreeNodeTO implements Serializable {
	
	private static final long serialVersionUID = -717960249853452551L;
	
	private static final String id = "id";
	private static final String status = "status";
	
	private static final String jobId = "jobId";
	private static final String taskId = "taskId";
	
	private static final String phase = "phase";
	private static final String initPhaseTime = "initPhaseTime";
	private static final String remotePhaseTime = "remotePhaseTime";
	private static final String finalPhaseTime = "finalPhaseTime";
	
	private static final String exitValue = "exitValue";
	private static final String assignedTo = "assignedTo";
	
	private static final String stdOut = "stdOur";
	private static final String stdErr = "stdErr";
	private static final String executionErrorType = "executionErrorType";
	private static final String errorCause = "errorCause";
	
	private static final String HTML_INIT = "<span style=\"font-size:70%;\">";
	private static final String LINE_SEPARATOR = "<br>";
	private static final String HTML_FINISH = "</span>";
	
	public ProcessTO() {
		super();
		set(ProcessTO.type, "ProcessTO");
	}
	
	public Integer getId() {
		return get(id);
	}
	
	public String getStatus() {
		return get(status);
	}
	
	public Integer getJobId() {
		return get(jobId);
	}
	
	public Integer getTaskId() {
		return get(taskId);
	}

	public String getPhase() {
		return get(phase);
	}
	
	public Long getInitPhase() {
		return get(initPhaseTime);
	}

	public Long getRemotePhase() {
		return get(remotePhaseTime);
	}

	public Long getFinalPhase() {
		return get(finalPhaseTime);
	}

	public Integer getExitValue() {
		return get(exitValue);
	}

	public String getAssignedTo() {
		return get(assignedTo);
	}
	
	public String getStdOut() {
		return get(stdOut);
	}

	public String getStdErr() {
		return get(stdErr);
	}
	
	public String getExecutionErrorType() {
		return get(executionErrorType);
	}
	
	public String getErrorCause() {
		return get(errorCause);
	}
	
	public void setStatus(String status) {
		set(ProcessTO.status, status);
	}
	
	public void setJobId(Integer jobId) {
		set(ProcessTO.jobId, jobId);
	}
	
	public void setTaskId(Integer taskId) {
		set(ProcessTO.taskId, taskId);
	}
	
	public void setPhase(String phase) {
		set(ProcessTO.phase, phase);
	}
	
	public void setInitPhase(Long initPhaseTime) {
		set(ProcessTO.initPhaseTime, initPhaseTime);
	}
	
	public void setRemotePhase(Long remotePhaseTime) {
		set(ProcessTO.remotePhaseTime, remotePhaseTime);
	}
	
	public void setFinalPhase(Long finalPhaseTime) {
		set(ProcessTO.finalPhaseTime, finalPhaseTime);
	}
	
	public void setExitValue(Integer exitValue) {
		set(ProcessTO.exitValue, exitValue);
	}
	
	public void setAssignedTo(String assignedTo) {
		set(ProcessTO.assignedTo, assignedTo);
	}
	
	public void setStdOut(String stdOut) {
		set(ProcessTO.stdOut, stdOut);
	}
	
	public void setStdErr(String stdErr) {
		set(ProcessTO.stdErr, stdErr);
	}
	
	public void setExecutionErrorType(String error) {
		set(ProcessTO.executionErrorType, error);
	}
	
	public void setErrorCause(String cause) {
		set(ProcessTO.errorCause, cause);
	}
	
	public void setDescription() {
		String description = "";
		
		description += HTML_INIT;
//		description += " JOB: " + getJobId() + LINE_SEPARATOR;
		description += " TASK: " + getTaskId() + LINE_SEPARATOR;
		description += " PROCESS: " + getId() + LINE_SEPARATOR;
		description += " ASSIGNED TO: " + getAssignedTo() + LINE_SEPARATOR;
		description += " STATE: " + getStatus() + LINE_SEPARATOR;
		description += " PHASE: " + getPhase() + LINE_SEPARATOR;
		
		if (getExecutionErrorType().length() != 0) {
			description += " -----" + LINE_SEPARATOR;
			description += " ERROR CAUSE: " + LINE_SEPARATOR + getExecutionErrorType() + LINE_SEPARATOR;
		}
		
		if (getErrorCause().length() != 0){
			description += " ADDITIONAL INFORMATION: " + LINE_SEPARATOR + getErrorCause() + LINE_SEPARATOR;
		}
		
		if (getInitPhase() != 0){
			description += " -----" + LINE_SEPARATOR;
			description += " EXECUTION TIMES:" + LINE_SEPARATOR;
			description += "    INIT PHASE: " + getInitPhase() + " ms." + LINE_SEPARATOR;
			description += "    REMOTE PHASE: " + getRemotePhase() + " ms." + LINE_SEPARATOR;
			description += "    FINAL PHASE: " + getFinalPhase() + " ms." + LINE_SEPARATOR;
		}
		
		if (getExitValue() != -1){
			description += " -----" + LINE_SEPARATOR;
			description += " PROCESS OUTPUT:" + LINE_SEPARATOR;
			description += " ExitValue: " + getExitValue() + LINE_SEPARATOR;
			description += " StdOut: " + getStdOut() + LINE_SEPARATOR;
			description += " StdErr: " + getStdErr() + LINE_SEPARATOR;
		}
		description += HTML_FINISH;
		
		setDescription(description);
	}
	
	public void setResultTO(List<ResultTO> result) {
		for (ResultTO resultTO : result) {
			add(resultTO);
		}
	}
	
	public boolean hasResultTO() {
		return !getChildren().isEmpty();
	}

	public String toString() {
		return "Process " + this.getId() + ": [" + this.getStatus() + " ]";
	}
}