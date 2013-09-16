package org.ourgrid.portal.server.logic.util;

import java.util.LinkedList;
import java.util.List;

import org.ourgrid.broker.status.GridProcessStatusInfo;
import org.ourgrid.broker.status.GridProcessStatusInfoResult;
import org.ourgrid.broker.status.JobStatusInfo;
import org.ourgrid.broker.status.TaskStatusInfo;
import org.ourgrid.common.executor.ExecutorResult;
import org.ourgrid.common.interfaces.to.GridProcessState;
import org.ourgrid.common.specification.job.IOEntry;
import org.ourgrid.portal.client.common.to.model.JobTO;
import org.ourgrid.portal.client.common.to.model.ProcessTO;
import org.ourgrid.portal.client.common.to.model.TaskPageTO;
import org.ourgrid.portal.client.common.to.model.TaskTO;

public class JobTOFactory {

	public static final Integer TASK_PAGE_SIZE = 10;
	
	private static Integer num_processes;
	private static Integer num_unstarted_tasks;
	private static Integer num_running_tasks;
	private static Integer num_finished_tasks;
	private static Integer num_failed_tasks;
	private static Integer num_cancelled_tasks;
	
	private static Integer num_running_processes;
	private static Integer num_finished_processes;
	private static Integer num_aborted_processes;
	private static Integer num_failed_processes;
	private static Integer num_cancelled_processes;
	private static Integer num_sabotaged_processes;
	
	public JobTOFactory(){
		init();
	}
	
	private static void init(){
		num_processes = 0;
		num_unstarted_tasks = 0;
		num_running_tasks = 0;
		num_finished_tasks = 0;
		num_failed_tasks = 0;
		num_cancelled_tasks = 0;
		
		num_running_processes = 0;
		num_finished_processes = 0;
		num_aborted_processes = 0;
		num_failed_processes = 0;
		num_cancelled_processes = 0;
		num_sabotaged_processes = 0;
	}
	
	public static JobTO generateJobTO(JobStatusInfo job){
		init();
		JobTO ourJob = new JobTO();
		
		ourJob.setNumTasks(job.getSpec().getTaskSpecs().size()); // #tasks
		ourJob.setNumUnstartedTasks(num_unstarted_tasks); // # unstarted tasks
		ourJob.setNumRunningTasks(num_running_tasks); // # running tasks
		ourJob.setNumFinishedTasks(num_finished_tasks); // # finished tasks
		ourJob.setNumFailedTasks(num_failed_tasks); // # failed tasks
		ourJob.setNumCancelledTasks(num_cancelled_tasks); // # cancelled tasks
		
		ourJob.setNumProcesses(num_processes); // # processes
		ourJob.setNumRunningProcesses(num_running_processes); // # running processes
		ourJob.setNumFinishedProcesses(num_finished_processes); // # finished processes
		ourJob.setNumAbortedProcesses(num_aborted_processes); // # aborted processes
		ourJob.setNumFailedProcesses(num_failed_processes); // # failed processes
		ourJob.setNumCancelledProcesses(num_cancelled_processes); // # cancelled processes
		ourJob.setNumSabotagedProcesses(num_sabotaged_processes); // # sabotaged processes
		
		ourJob.setId(job.getJobId()); // jobId
		ourJob.setLabel(job.getSpec().getLabel()); // label
		ourJob.setRequirements(job.getSpec().getRequirements());
		ourJob.setStatus(JobStatusInfo.getState(job.getState())); // status
		ourJob.setDescription();
		ourJob.setName(ourJob.toString());
		
		Integer offset = 0;
		while (offset < ourJob.getNumTasks()) {
			ourJob.add(generateSuperTasksTO(ourJob, offset));
			
			offset += TASK_PAGE_SIZE;
		}
		
		return ourJob;
	}
	
	private static TaskPageTO generateSuperTasksTO(JobTO ourJob, Integer offset) {
		
		TaskPageTO superTaskTO = new TaskPageTO();
		superTaskTO.setFirstTaskId(offset);
		superTaskTO.setLastTaskId(Math.min(offset + TASK_PAGE_SIZE, ourJob.getNumTasks()));
		superTaskTO.setId(ourJob.getId() +  "." +  superTaskTO.getFirstTaskId());
		superTaskTO.setName(superTaskTO.toString());
		
		return superTaskTO;
	}
	
	public static TaskTO generateTaskTO(TaskStatusInfo task){
		TaskTO ourTask = new TaskTO();
		
		if (task.getState().equals(GridProcessState.UNSTARTED.toString()))
			num_unstarted_tasks++;
		else if (task.getState().toString().equals(GridProcessState.RUNNING.toString()))
			num_running_tasks++;
		else if (task.getState().toString().equals(GridProcessState.FAILED.toString()))
			num_failed_tasks++;
		else if (task.getState().toString().equals(GridProcessState.FINISHED.toString()))
			num_finished_tasks++;
		else num_cancelled_tasks++; // ABORTED, CANCELLED, SABOTAGED
		
		num_processes += task.getGridProcesses().size();
		List<ProcessTO> ourProcesses = new LinkedList<ProcessTO>();
		for (GridProcessStatusInfo process : task.getGridProcesses()){
			ProcessTO ourProcess = generateProcess(process);
			ourProcesses.add(ourProcess);
		}
		
		ourTask.setId(task.getTaskId());
		ourTask.setActualFails(task.getActualFails());
		ourTask.setJobId(task.getJobId());
		ourTask.setNumProcesses(task.getGridProcesses().size());
		ourTask.setNumRunningProcesses(task.getNumberOfRunningReplicas());
		ourTask.setSpec(task.getSpec().toString());
		ourTask.setProcesses(ourProcesses);
		ourTask.setStatus(task.getState().toString());
		
		List<IOEntry> entries = task.getSpec().getFinalBlock().getEntry("");
		List<String> resultFiles = new LinkedList<String>();
		
		if (entries != null) {
			for (IOEntry entry : entries) {
				resultFiles.add(entry.getDestination());
			}
		} 
		
		ourTask.setResultsName(resultFiles);
		ourTask.setName(ourTask.toString());
		ourTask.setDescription();
		
		return ourTask;
	}
	
	private static ProcessTO generateProcess(GridProcessStatusInfo process){
		ProcessTO ourProcess = new ProcessTO();
		
		if (process.getState().equals(GridProcessState.RUNNING.toString()))
			num_running_processes++;
		else if (process.getState().equals(GridProcessState.FINISHED.toString()))
			num_finished_processes++;
		else if (process.getState().equals(GridProcessState.ABORTED.toString()))
			num_aborted_processes++;
		else if (process.getState().equals(GridProcessState.FAILED.toString()))
			num_failed_processes++;
		else if (process.getState().equals(GridProcessState.CANCELLED.toString()))
			num_cancelled_processes++;
		else if (process.getState().equals(GridProcessState.SABOTAGED.toString()))
			num_sabotaged_processes++;
		
		ourProcess.setId(process.getId());
		
		String state = process.getState();
		
		ourProcess.setInitPhase(0L);
		ourProcess.setRemotePhase(0L);
		ourProcess.setFinalPhase(0L);
		
		ourProcess.setStdOut("");
		ourProcess.setStdErr("");
		ourProcess.setExitValue(-1);
		
		ourProcess.setExecutionErrorType("");
		ourProcess.setErrorCause("");
		
		if (state.equals(GridProcessState.FINISHED.toString()) 
				|| state.equals(GridProcessState.CANCELLED.toString())
				|| state.equals(GridProcessState.FAILED.toString())) {
			
			ourProcess.setInitPhase(process.getReplicaResult().getInitDataTime());
			ourProcess.setRemotePhase(process.getReplicaResult().getRemoteDataTime());
			ourProcess.setFinalPhase(process.getReplicaResult().getFinalDataTime());
				
			GridProcessStatusInfoResult replicaResult = process.getReplicaResult();
			
			if (replicaResult != null) {
			
				ourProcess.setExecutionErrorType(replicaResult.getExecutionError());
				
				if (replicaResult.getExecutionErrorCause() != null) {
					ourProcess.setErrorCause(replicaResult.getExecutionErrorCause());
				}
				
				ExecutorResult executorResult = replicaResult.getExecutorResult();
				
				if (executorResult != null) {
					
					String stdout = executorResult.getStdout();
					ourProcess.setStdOut(stdout != null?
							stdout : "" );
					String stdErrr = executorResult.getStderr();
					ourProcess.setStdErr(stdErrr != null?
							stdErrr : "" );
					
					ourProcess.setExitValue(executorResult.getExitValue());
				}
			}
		}
		
		ourProcess.setJobId(process.getJobId());
		ourProcess.setTaskId(process.getTaskId());
		ourProcess.setPhase(process.getPhase());
		ourProcess.setAssignedTo(process.getWorkerInfo().getWorkerSpec().getUserAndServer());
		ourProcess.setStatus(process.getState());
		ourProcess.setName(ourProcess.toString());
		ourProcess.setDescription();
		
		return ourProcess;
	}
	
}
