package org.ourgrid.portal.server.logic.executors.plugin.cisternas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ourgrid.common.specification.exception.JobSpecificationException;
import org.ourgrid.common.specification.exception.TaskSpecificationException;
import org.ourgrid.common.specification.job.IOBlock;
import org.ourgrid.common.specification.job.IOEntry;
import org.ourgrid.common.specification.job.JobSpecification;
import org.ourgrid.common.specification.job.TaskSpecification;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.plugin.cisternas.gui.model.CisternasJobRequest;
import org.ourgrid.portal.client.plugin.cisternas.to.model.CisternasInputFileTO;
import org.ourgrid.portal.client.plugin.cisternas.to.response.CisternasJobSubmissionResponseTO;
import org.ourgrid.portal.client.plugin.cisternas.to.service.CisternasJobSubmissionTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.JobSubmissionExecutor;
import org.ourgrid.portal.server.logic.executors.plugin.cisternas.io.ParalelizarArquivos;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

import br.edu.ufcg.lsd.seghidro.cisternas.exceptions.ParametrosReaderException;

public class CisternasJobSubmissionExecutor extends JobSubmissionExecutor {

	private Long    uploadId;

	private File    scenarioFile;
	private File  	parametrosFile;
	private File  	pmhFile;
	private File	pesoPeriodoFile;

	private final String CISTERNAS_BAL_HID = "cisternasBalhid.jar";
	private final String CISTERNAS= "cisternas.jar";

	private Double initialVolume;

	private String  type;

	/** Lista com os paths dos arquivos de parâmetros quando foram criados. */
	private Map<String, String> listaPathsParametros = new HashMap<String, String>();

	/** Lista com os paths dos arquivos de precipitação quando foram criados. */
	private Map<String, String> listaPathsPrecipitacao =  new HashMap<String, String>();

	private final String FILE_SEPARATOR = System.getProperty("file.separator");

	public CisternasJobSubmissionExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	public void logTransaction(ServiceTO serviceTO) {
		// TODO Auto-generated method stub

	}

	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {

		CisternasJobSubmissionTO cisternasJobSubmissionTO = (CisternasJobSubmissionTO) serviceTO;
		CisternasJobSubmissionResponseTO cisternasJobSubmissionResponseTO = new CisternasJobSubmissionResponseTO();

		initializeClient();

		uploadId = cisternasJobSubmissionTO.getUploadId();
		String userLogin = cisternasJobSubmissionTO.getUserLogin();
		boolean emailNotification = cisternasJobSubmissionTO.isEmailNotification();
		CisternasInputFileTO inputFile = cisternasJobSubmissionTO.getInputFile();
		setInitialVolume(inputFile.getInitialVolume());

		JobSpecification theJob = new JobSpecification();
		try {
			theJob = compileInputs(inputFile, cisternasJobSubmissionTO);
		} catch (JobSpecificationException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		} catch (TaskSpecificationException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		} catch (IOException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}


		Integer jobId = addJob(cisternasJobSubmissionTO.getUserLogin(),theJob);

		cisternasJobSubmissionResponseTO.setJobID(jobId);
		cisternasJobSubmissionResponseTO.getJobIDs().add(jobId);

		addRequest(jobId, new CisternasJobRequest(jobId, uploadId, userLogin, emailNotification, inputFile));
		reeschedule();

		return cisternasJobSubmissionResponseTO;
	}


	private JobSpecification compileInputs(CisternasInputFileTO inputFile, CisternasJobSubmissionTO cisternasJobSubmissionTO) throws JobSpecificationException, TaskSpecificationException, IOException, ExecutionException {

		List<TaskSpecification> tasksList = new ArrayList<TaskSpecification>();

		setType(inputFile.getType());

		File remoteFolder = new File(getFilesPath());

		for (String files : remoteFolder.list()) {
			if(!files.endsWith(".zip")) {
				if(files.endsWith(".pmh")) {
					setPmhFile(new File(remoteFolder.getAbsolutePath() + FILE_SEPARATOR + files));
				}else if(files.endsWith(".pes")) {
					setPesoPeriodoFile(new File(remoteFolder.getAbsolutePath() + FILE_SEPARATOR + files));
				}else if(files.endsWith(".par")) {
					setParametrosFile(new File(remoteFolder.getAbsolutePath() + FILE_SEPARATOR + files));
				}else {
					setScenarioFile(new File(remoteFolder.getAbsoluteFile() + FILE_SEPARATOR + files));
				}
			}
		}

		if(verifyFiles()) {

			ParalelizarArquivos arquivo = ParalelizarArquivos.getInstance();
			try {
				arquivo.paralelizar(getPmhFile().getAbsolutePath(), getParametrosFile().getAbsolutePath(),remoteFolder.getCanonicalPath());
				this.listaPathsParametros = arquivo.getListaPathsParametros();
				this.listaPathsPrecipitacao = arquivo.getListaPathsPrecipitacao();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParametrosReaderException e) {
				e.printStackTrace();
			}
		}

		String outputFiles = "";
		int i = 1;
		for (String path : this.listaPathsParametros.keySet()) {
			setPmhFile(new File(this.listaPathsPrecipitacao.get(path)));
			setParametrosFile(new File(this.listaPathsParametros.get(path)));
			outputFiles =  "output" + getType() + i;
			i++;
			createJobSpecification(tasksList, outputFiles, remoteFolder.getAbsolutePath() + FILE_SEPARATOR + "output" );
		}
		return new JobSpecification("Cisterns Job", "", tasksList);
	}

	private void createJobSpecification(List<TaskSpecification> tasksList, String outputFile,String outputPath) throws ExecutionException, TaskSpecificationException {

		String cisternsPath = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.CISTERNS_PATH); 
		String cisternas = cisternsPath  + FILE_SEPARATOR + CISTERNAS;
		String cisternasBalHid = cisternsPath + FILE_SEPARATOR + CISTERNAS_BAL_HID;

		File cisternaFilePath = new File (cisternas);
		File cisternasBalHidFilePath = new File(cisternasBalHid);
		try {
			cisternas = cisternaFilePath.getCanonicalPath();
			cisternasBalHid = cisternasBalHidFilePath.getCanonicalPath();
		} catch (IOException e1) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}



		// create initBlock
		IOBlock initBlock = new IOBlock();
		IOEntry cisternasFile = new IOEntry("put", cisternas, CISTERNAS);
		IOEntry cisternasBalHidFile = new IOEntry("put", cisternasBalHid, CISTERNAS_BAL_HID);
		IOEntry parametrosFileJob = new IOEntry("put", getParametrosFile().getAbsolutePath(), getParametrosFile().getName());
		IOEntry pesoPeriodoFileJob = new IOEntry("put", getPesoPeriodoFile().getAbsolutePath(), getPesoPeriodoFile().getName());
		IOEntry pmhFileJob = new IOEntry("put", getPmhFile().getAbsolutePath(), getPmhFile().getName());
		IOEntry scenarioFileJob = new IOEntry("put", getScenarioFile().getAbsolutePath(), getScenarioFile().getName());

		initBlock.putEntry(cisternasFile);
		initBlock.putEntry(cisternasBalHidFile);
		initBlock.putEntry(parametrosFileJob);
		initBlock.putEntry(pesoPeriodoFileJob);
		initBlock.putEntry(pmhFileJob);
		initBlock.putEntry(scenarioFileJob);

		//create remotExe
		String type = "";
		String remoteExe = "";
		if(getType().equalsIgnoreCase("complete")) {
			type = "completo";
			//		}else if(getType().equalsIgnoreCase("consense")) {
			//			type = "consenso";
			//		}else if(getType().equalsIgnoreCase("standard")) {
			//			type = "normal";
		}else {
			type = "variacao";
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("java -jar cisternas.jar ");
		stringBuilder.append(initialVolume.toString());
		stringBuilder.append(" ");
		stringBuilder.append(type);
		stringBuilder.append(" ");
		stringBuilder.append(pmhFile.getName());
		stringBuilder.append(" ");
		stringBuilder.append(parametrosFile.getName());
		stringBuilder.append(" ");
		stringBuilder.append(pesoPeriodoFile.getName());
		stringBuilder.append(" ");
		stringBuilder.append(outputFile);
		stringBuilder.append(" ");
		stringBuilder.append(scenarioFile.getName());
		remoteExe = stringBuilder.toString();

		//create finalBlock
		IOBlock finalBlock = new IOBlock();
		IOEntry finalEntry = new IOEntry("get",outputFile + ".zip", outputPath + FILE_SEPARATOR + outputFile + ".zip" );
		finalBlock.putEntry(finalEntry);


		TaskSpecification taskSpec = new TaskSpecification(initBlock, remoteExe, finalBlock, null);
		tasksList.add(taskSpec);


	}

	private boolean verifyFiles() {
		return (getScenarioFile() != null && parametrosFile != null && getPesoPeriodoFile() != null &&
				getPmhFile() != null && getPesoPeriodoFile() != null); 
	}

	private String getFilesPath() {

		return getPortal().getDAOManager().getUploadDirName(uploadId);
	}

	public File getScenarioFile() {
		return scenarioFile;
	}

	public void setScenarioFile(File scenarioFile) {
		this.scenarioFile = scenarioFile;
	}

	public File getParametrosFile() {
		return parametrosFile;
	}

	public void setParametrosFile(File reservoirFile) {
		this.parametrosFile = reservoirFile;
	}

	public File getPmhFile() {
		return pmhFile;
	}

	public void setPmhFile(File phrFile) {
		this.pmhFile = phrFile;
	}

	public File getPesoPeriodoFile() {
		return pesoPeriodoFile;
	}

	public void setPesoPeriodoFile(File pesoPeriodoFile) {
		this.pesoPeriodoFile = pesoPeriodoFile;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getInitialVolume() {
		return initialVolume;
	}

	public void setInitialVolume(Double initialVolume) {
		this.initialVolume = initialVolume;
	}

}
