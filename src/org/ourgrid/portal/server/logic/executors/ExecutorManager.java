package org.ourgrid.portal.server.logic.executors;

import java.util.HashMap;
import java.util.Map;

import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.common.CancelJobExecutor;
import org.ourgrid.portal.server.logic.executors.common.CaptchaGeneratorExecutor;
import org.ourgrid.portal.server.logic.executors.common.CheckProxyExecutor;
import org.ourgrid.portal.server.logic.executors.common.CheckQuotaExceedWithUploadExecutor;
import org.ourgrid.portal.server.logic.executors.common.CheckQuotaExecutor;
import org.ourgrid.portal.server.logic.executors.common.CompressFilesExecutor;
import org.ourgrid.portal.server.logic.executors.common.DeleteFileExecutor;
import org.ourgrid.portal.server.logic.executors.common.DeleteJobExecutor;
import org.ourgrid.portal.server.logic.executors.common.DeleteUploadStoreExecutor;
import org.ourgrid.portal.server.logic.executors.common.ExtractFilesExecutor;
import org.ourgrid.portal.server.logic.executors.common.FileExplorerExecutor;
import org.ourgrid.portal.server.logic.executors.common.ForcedCancelJobExecutor;
import org.ourgrid.portal.server.logic.executors.common.GetJobsUploadedNameExecutor;
import org.ourgrid.portal.server.logic.executors.common.JdfCompilationExecutor;
import org.ourgrid.portal.server.logic.executors.common.JobStatusRetrievalExecutor;
import org.ourgrid.portal.server.logic.executors.common.OurGridJobSubmissionExecutor;
import org.ourgrid.portal.server.logic.executors.common.NewFolderExecutor;
import org.ourgrid.portal.server.logic.executors.common.PasteFileExecutor;
import org.ourgrid.portal.server.logic.executors.common.RefreshFileExplorerExecutor;
import org.ourgrid.portal.server.logic.executors.common.RemoveUserExecutor;
import org.ourgrid.portal.server.logic.executors.common.RenameFileExecutor;
import org.ourgrid.portal.server.logic.executors.common.RequestPagedTasksExecutor;
import org.ourgrid.portal.server.logic.executors.common.UploadSessionIDExecutor;
import org.ourgrid.portal.server.logic.executors.common.UserApprovalExecutor;
import org.ourgrid.portal.server.logic.executors.common.UserLoginExecutor;
import org.ourgrid.portal.server.logic.executors.common.UserPasswordRecoverExecutor;
import org.ourgrid.portal.server.logic.executors.common.UserRegistrationExecutor;
import org.ourgrid.portal.server.logic.executors.common.UserSettingsExecutor;
import org.ourgrid.portal.server.logic.executors.common.UsersAccountsExecutor;
import org.ourgrid.portal.server.logic.executors.common.UsersEditQuotaExecutor;
import org.ourgrid.portal.server.logic.executors.plugin.autodock.AutodockJobSubmissionExecutor;
import org.ourgrid.portal.server.logic.executors.plugin.blender.BlenderJobSubmissionExecutor;
import org.ourgrid.portal.server.logic.executors.plugin.cisternas.CisternasJobSubmissionExecutor;
import org.ourgrid.portal.server.logic.executors.plugin.epanet.EpanetJobSubmissionExecutor;
import org.ourgrid.portal.server.logic.executors.plugin.fibonacci.FibonacciJobSubmissionExecutor;
import org.ourgrid.portal.server.logic.executors.plugin.genecodis.GenecodisJobSubmissionExecutor;
import org.ourgrid.portal.server.logic.executors.plugin.genecodis.GenecodisSendInputExecutor;
import org.ourgrid.portal.server.logic.executors.plugin.lvc.LvcJobImageJoinerExecutor;
import org.ourgrid.portal.server.logic.executors.plugin.lvc.LvcJobSubmissionExecutor;
import org.ourgrid.portal.server.logic.executors.plugin.marbs.MarbsJobSubmissionExecutor;
import org.ourgrid.portal.server.logic.executors.plugin.reservatorios.ReservatoriosJobSubmissionExecutor;
import org.ourgrid.portal.server.logic.executors.plugin.rho.RhoJobSubmissionExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;

public class ExecutorManager {
	
	Map<String, ExecutorIF> executors;
	
	public ExecutorManager(OurGridPortalIF portal) {
		executors = new HashMap<String, ExecutorIF>();
		initCommonExecutors(portal);
		initPluginsExecutors(portal);
	}
	
	public void initCommonExecutors(OurGridPortalIF portal) {
		addExecutor(CommonServiceConstants.USER_LOGIN_EXECUTOR, new UserLoginExecutor(portal));
		addExecutor(CommonServiceConstants.USER_REGISTRATION_EXECUTOR, new UserRegistrationExecutor(portal));
		addExecutor(CommonServiceConstants.USER_PASSWORD_RECOVER_EXECUTOR, new UserPasswordRecoverExecutor(portal));
		addExecutor(CommonServiceConstants.REMOVE_USER_EXECUTOR, new RemoveUserExecutor(portal));
		addExecutor(CommonServiceConstants.USER_APPROVAL_EXECUTOR, new UserApprovalExecutor(portal));
		addExecutor(CommonServiceConstants.EDIT_USER_QUOTA_EXECUTOR, new UsersEditQuotaExecutor(portal));
		addExecutor(CommonServiceConstants.USER_SETTINGS_EXECUTOR, new UserSettingsExecutor(portal));
		addExecutor(CommonServiceConstants.USERS_ACCOUNTS_EXECUTOR, new UsersAccountsExecutor(portal));
		
		addExecutor(CommonServiceConstants.CAPTCHA_GENERATOR_EXECUTOR, new CaptchaGeneratorExecutor());
		addExecutor(CommonServiceConstants.UPLOAD_SESSION_ID_EXECUTOR, new UploadSessionIDExecutor(portal));
		addExecutor(CommonServiceConstants.EXTRACT_FILES_EXECUTOR, new ExtractFilesExecutor(portal));
		addExecutor(CommonServiceConstants.COMPRESS_FILES_EXECUTOR, new CompressFilesExecutor(portal));
		addExecutor(CommonServiceConstants.JDF_COMPILATION_EXECUTOR, new JdfCompilationExecutor(portal));
		addExecutor(CommonServiceConstants.CHECK_QUOTA_EXCEED_WITH_UPLOAD_EXECUTOR, new CheckQuotaExceedWithUploadExecutor(portal));
		addExecutor(CommonServiceConstants.CHECK_QUOTA_EXECUTOR, new CheckQuotaExecutor(portal));
		addExecutor(CommonServiceConstants.DELETE_UPLOAD_STORE_EXECUTOR, new DeleteUploadStoreExecutor(portal));
		
		addExecutor(CommonServiceConstants.GET_JOBS_UPLOADED_NAME_EXECUTOR, new GetJobsUploadedNameExecutor(portal));
		addExecutor(CommonServiceConstants.JOB_SUBMISSION_EXECUTOR, new OurGridJobSubmissionExecutor(portal));
		addExecutor(CommonServiceConstants.CANCEL_JOB_EXECUTOR, new CancelJobExecutor(portal));
		addExecutor(CommonServiceConstants.FORCED_CANCEL_JOB_EXECUTOR, new ForcedCancelJobExecutor(portal));
		addExecutor(CommonServiceConstants.JOB_STATUS_RETRIEVAL_EXECUTOR, new JobStatusRetrievalExecutor(portal));
		addExecutor(CommonServiceConstants.GET_PAGED_TASKS_EXECUTOR, new RequestPagedTasksExecutor(portal));
		addExecutor(CommonServiceConstants.DELETE_JOB_EXECUTOR, new DeleteJobExecutor(portal));
		addExecutor(CommonServiceConstants.BLENDER_JOB_SUBMISSION_EXECUTOR, new BlenderJobSubmissionExecutor(portal));
		addExecutor(CommonServiceConstants.GENECODIS_SEND_INPUT_EXECUTOR, new GenecodisSendInputExecutor(portal));
		addExecutor(CommonServiceConstants.GENECODIS_JOB_SUBMISSION_EXECUTOR, new GenecodisJobSubmissionExecutor(portal));
		addExecutor(CommonServiceConstants.RHO_JOB_SUBMISSION_EXECUTOR, new RhoJobSubmissionExecutor(portal));
		addExecutor(CommonServiceConstants.RESERVATORIOS_JOB_SUBMISSION_EXECUTOR, new ReservatoriosJobSubmissionExecutor(portal));
		addExecutor(CommonServiceConstants.EPANET_JOB_SUBMISSION_EXECUTOR, new EpanetJobSubmissionExecutor(portal));
		addExecutor(CommonServiceConstants.CISTERNAS_JOB_SUBMISSION_EXECUTOR, new CisternasJobSubmissionExecutor(portal));
		addExecutor(CommonServiceConstants.AUTODOCK_JOB_SUBMISSION_EXECUTOR, new AutodockJobSubmissionExecutor(portal));
		addExecutor(CommonServiceConstants.MARBS_JOB_SUBMISSION_EXECUTOR, new MarbsJobSubmissionExecutor(portal));
		addExecutor(CommonServiceConstants.FIBONACCI_JOB_SUBMISSION_EXECUTOR, new FibonacciJobSubmissionExecutor(portal));
		addExecutor(CommonServiceConstants.LVC_JOB_SUBMISSION_EXECUTOR, new LvcJobSubmissionExecutor(portal));
		addExecutor(CommonServiceConstants.LVC_JOB_IMAGE_JOINER_EXECUTOR, new LvcJobImageJoinerExecutor(portal));
		
		addExecutor(CommonServiceConstants.FILE_EXPLORER_EXECUTOR, new FileExplorerExecutor(portal));
		addExecutor(CommonServiceConstants.REFRESH_FILE_EXPLORER_EXECUTOR, new RefreshFileExplorerExecutor(portal));
		addExecutor(CommonServiceConstants.NEW_FOLDER_EXECUTOR, new NewFolderExecutor(portal));
		addExecutor(CommonServiceConstants.DELETE_FILE_EXECUTOR, new DeleteFileExecutor(portal));
		addExecutor(CommonServiceConstants.RENAME_FILE_EXECUTOR, new RenameFileExecutor(portal));
		addExecutor(CommonServiceConstants.PASTE_FILE_EXECUTOR, new PasteFileExecutor(portal));
		
		addExecutor(CommonServiceConstants.CHECK_PROXY_EXECUTOR, new CheckProxyExecutor(portal));
	}
	
	public void initPluginsExecutors(OurGridPortalIF portal) {}

	private void addExecutor(String executorName, ExecutorIF exe) {
		this.executors.put(executorName, exe);
	}
	
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		ExecutorIF exe = getExecutor(serviceTO.getExecutorName());
		
		ResponseTO executionResult = exe.execute(serviceTO);
		exe.logTransaction(serviceTO);
		
		return executionResult;
	}

	private ExecutorIF getExecutor(String executorName) {
		return this.executors.get(executorName);
	}
}