package org.ourgrid.portal.server.logic.executors.plugin.epanet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ourgrid.common.specification.exception.JobSpecificationException;
import org.ourgrid.common.specification.exception.TaskSpecificationException;
import org.ourgrid.common.specification.job.IOBlock;
import org.ourgrid.common.specification.job.IOEntry;
import org.ourgrid.common.specification.job.JobSpecification;
import org.ourgrid.common.specification.job.TaskSpecification;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.plugin.epanetgrid.gui.model.EpanetJobRequest;
import org.ourgrid.portal.client.plugin.epanetgrid.to.model.EpanetInputFileTO;
import org.ourgrid.portal.client.plugin.epanetgrid.to.response.EpanetJobSubmissionResponseTO;
import org.ourgrid.portal.client.plugin.epanetgrid.to.service.EpanetJobSubmissionTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.JobSubmissionExecutor;
import org.ourgrid.portal.server.logic.executors.plugin.epanet.io.ModificarMalhas;
import org.ourgrid.portal.server.logic.executors.plugin.epanet.io.PerturbadorExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;
import org.ourgrid.portal.server.util.OurgridPortalProperties;


public class EpanetJobSubmissionExecutor extends JobSubmissionExecutor {
	
	private final String FILE_SEPARATOR = System.getProperty("file.separator");
	private Long    uploadId;
	private File 	malhasFile;
	private File	perturbacaoFile;
	
	public EpanetJobSubmissionExecutor(OurGridPortalIF portal) {
		super(portal);
	}
	@Override
	public void logTransaction(ServiceTO serviceTO) {
		// TODO Auto-generated method stub

	}

	@Override
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		EpanetJobSubmissionTO epanetJobSubmissionTO = (EpanetJobSubmissionTO) serviceTO;
		EpanetJobSubmissionResponseTO epanetJobSubmissionResponseTO = new EpanetJobSubmissionResponseTO();

		initializeClient();

		uploadId = epanetJobSubmissionTO.getUploadId();
		String userLogin = epanetJobSubmissionTO.getUserLogin();
		boolean emailNotification = epanetJobSubmissionTO.isEmailNotification();
		EpanetInputFileTO inputFile = epanetJobSubmissionTO.getInputFile();

		JobSpecification theJob = new JobSpecification();
		try {
			theJob = compileInputs(inputFile, epanetJobSubmissionTO);
		} catch (JobSpecificationException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		} catch (TaskSpecificationException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		} catch (IOException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}

		Integer jobId = addJob(epanetJobSubmissionTO.getUserLogin(), theJob);
		
		epanetJobSubmissionResponseTO.setJobID(jobId);
		epanetJobSubmissionResponseTO.getJobIDs().add(jobId);

		addRequest(jobId, new EpanetJobRequest(jobId, uploadId, userLogin, emailNotification, inputFile));
		reeschedule();

		return epanetJobSubmissionResponseTO;
	}
	
	private String getFilesPath() {
		return getPortal().getDAOManager().getUploadDirName(uploadId);
	}
	
	public JobSpecification compileInputs(EpanetInputFileTO inputFile, EpanetJobSubmissionTO epanetJobSubmissionTO) throws JobSpecificationException, TaskSpecificationException, IOException, ExecutionException {
		
		String perturbacaoFilePath = getFilesPath() + FILE_SEPARATOR + "perturbacao";
		List<TaskSpecification> taskList = new ArrayList<TaskSpecification>();
		
		File perturbacao = new File(perturbacaoFilePath);
		if(!perturbacao.exists()) {
			perturbacao.mkdirs();
		}
		File remoteFolder = new File(getFilesPath());
		for (String fileName : remoteFolder.list()) {
			File file = new File(remoteFolder.getCanonicalPath() + FILE_SEPARATOR + fileName);
			if(file.getName().endsWith(".inp")) {
				setMalhasFile(file);
			} else if(file.getName().endsWith(".per")) {
				setPerturbacaoFile(file);
			}
		}

		System.out.println("vai criar perturbador...");
		PerturbadorExecutor pe = new PerturbadorExecutor(getMalhasFile().getCanonicalPath());
		pe.addPerturbacao(getPerturbacaoFile().getCanonicalPath());
		System.out.println("Vai gerar perturbacao");
		pe.geraPerturbacao(perturbacaoFilePath, "Malha-out-");
		
		int count = 0;
		for (String malhaFiles : perturbacao.list()) {
			count++;
			ModificarMalhas modificar = new ModificarMalhas(perturbacao.getAbsolutePath() + FILE_SEPARATOR + malhaFiles, "resultados"+count+".txt");
			modificar.modificar();
		}
		
		
		int counter = 0;
		for (String malhaFiles  : perturbacao.list()) {
			counter++;
			File malha = new File(perturbacao.getAbsolutePath() + FILE_SEPARATOR + malhaFiles);
			createJobSpecification(taskList, malha, "saida"+ counter+".txt", "Resultados"+ counter+".txt", remoteFolder.getAbsolutePath() + FILE_SEPARATOR + "output",counter);
		}
		return new JobSpecification("Epanet Job", "", taskList);
		
	}
	
	public void createJobSpecification(List<TaskSpecification> tasksList,File malha, String outputFileSaida, String outputFileResultado,String outputPath,int index) throws TaskSpecificationException, ExecutionException {
		
		String epanetPath = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.EPANET_PATH);
		
		File file = new File (epanetPath + FILE_SEPARATOR + "epanetgrid.tar.gz");
		try {
			epanetPath = file.getCanonicalPath();
		} catch (IOException e1) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}
		
			// create initBlock
			IOBlock initBlock = new IOBlock();
			IOEntry epanetFile = new IOEntry("store", epanetPath, "epanetgrid.tar.gz");
			IOEntry malhaFile = new IOEntry("put", malha.getAbsolutePath(), malha.getName());
			
			initBlock.putEntry(epanetFile);
			initBlock.putEntry(malhaFile);

			//create remotExe
			String remoteExe = "";
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("tar xzvf $STORAGE/epanetgrid.tar.gz -C $PLAYPEN ; executa.sh ");
			stringBuilder.append(malha.getName());
			stringBuilder.append(" ");
			stringBuilder.append("saida");
			stringBuilder.append(index);
			stringBuilder.append(".txt");
			stringBuilder.append(" ");
			stringBuilder.append("Resultados");
			stringBuilder.append(index);
			stringBuilder.append(".txt");
			remoteExe += stringBuilder.toString(); 

			//create finalBlock
			IOBlock finalBlock = new IOBlock();
			IOEntry finalEntrySaida = new IOEntry("get",outputFileSaida, outputPath + FILE_SEPARATOR + outputFileSaida );
			IOEntry finalEntryResultado = new IOEntry("get",outputFileResultado, outputPath + FILE_SEPARATOR + outputFileResultado);
			finalBlock.putEntry(finalEntrySaida);
			finalBlock.putEntry(finalEntryResultado);
			
			
			TaskSpecification taskSpec = new TaskSpecification(initBlock, remoteExe, finalBlock, null);
			tasksList.add(taskSpec);
	}
	public File getMalhasFile() {
		return malhasFile;
	}
	public void setMalhasFile(File malhasFile) {
		this.malhasFile = malhasFile;
	}
	public File getPerturbacaoFile() {
		return perturbacaoFile;
	}
	public void setPerturbacaoFile(File perturbacaoFile) {
		this.perturbacaoFile = perturbacaoFile;
	}
	

}
