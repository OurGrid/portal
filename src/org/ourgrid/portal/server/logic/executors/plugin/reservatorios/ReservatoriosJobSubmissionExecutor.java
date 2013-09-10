package org.ourgrid.portal.server.logic.executors.plugin.reservatorios;

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
import org.ourgrid.portal.client.plugin.reservatorios.gui.model.ReservatoriosJobRequest;
import org.ourgrid.portal.client.plugin.reservatorios.to.model.ReservatoriosInputFileTO;
import org.ourgrid.portal.client.plugin.reservatorios.to.response.ReservatoriosJobSubmissionResponseTO;
import org.ourgrid.portal.client.plugin.reservatorios.to.service.ReservatoriosJobSubmissionTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.JobSubmissionExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;
import org.ourgrid.portal.server.util.OurgridPortalProperties;


/*
 * Complete 
 * Remote: java -jar jhidro.jar 1 6 completo volume_inicia_reservatorios_paraibal.txt reservatorio.res demanda.dem lamina.lam output.out mensal historico.his PesoPeriodo.pes cenarios.cen     Condition: []
 *  IOEntry: [get output.out to /usr/local/install/apache-tomcat-6.0.24/webapps/jhidroPortal/data/sessions/5B97FF58EB7898EA7DE3F42F2A6493AE/203/reservoirs/emidio/output.out]
 * 
 * Scenario
 * Remote: java -jar jhidro.jar 2 4 cenarios volume_inicia_reservatorios_paraibal.txt ReservatorioTaperoaII.res demandaTaperoaII.dem laminaTaperoaII.lam output.out mensal  cenarios.cen     Condition: []
 * IOEntry: [get output.out to /usr/local/install/apache-tomcat-6.0.24/webapps/jhidroPortal/data/sessions/5B97FF58EB7898EA7DE3F42F2A6493AE/240/reservoirs/taperoa_II/output.out]
 * 
 */

public class ReservatoriosJobSubmissionExecutor extends JobSubmissionExecutor {

	private Long    uploadId;
	private File    scenarioFile;
	private File    volumeFile;

	private File  	reservoirFile;
	private File  	demandaFile;
	private File  	phrFile;
	private File  	historyFile;
	private File  	laminaFile;
	private File	pesoPeriodoFile;
	private String  type;

	private Integer startMonth;
	private Integer endMonth;

	private double  dry;
	private double  normal;		
	private double  rainy;

	private final String FILE_SEPARATOR = System.getProperty("file.separator");

	public ReservatoriosJobSubmissionExecutor(OurGridPortalIF portal) {
		super(portal);
	}

	public void logTransaction(ServiceTO serviceTO) {
		// TODO Auto-generated method stub

	}

	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {

		ReservatoriosJobSubmissionTO reservatoriosJobSubmissionTO = (ReservatoriosJobSubmissionTO) serviceTO;
		ReservatoriosJobSubmissionResponseTO reservatoriosJobSubmissionResponseTO = new ReservatoriosJobSubmissionResponseTO();

		initializeClient();

		uploadId = reservatoriosJobSubmissionTO.getUploadId();
		String userLogin = reservatoriosJobSubmissionTO.getUserLogin();
		boolean emailNotification = reservatoriosJobSubmissionTO.isEmailNotification();
		ReservatoriosInputFileTO inputFile = reservatoriosJobSubmissionTO.getInputFile();

		JobSpecification theJob = new JobSpecification();
		try {
			theJob = compileInputs(inputFile, reservatoriosJobSubmissionTO);
		} catch (JobSpecificationException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		} catch (TaskSpecificationException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		} catch (IOException e) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}

		Integer jobId = addJob(reservatoriosJobSubmissionTO.getUserLogin(), theJob);

		//TODO Adicionar na Lista para aparecer todos no status.
		reservatoriosJobSubmissionResponseTO.setJobID(jobId);
		reservatoriosJobSubmissionResponseTO.getJobIDs().add(jobId);

		addRequest(jobId, new ReservatoriosJobRequest(jobId, uploadId, userLogin, emailNotification, inputFile));
		reeschedule();
		
		return reservatoriosJobSubmissionResponseTO;
	}


	private JobSpecification compileInputs(ReservatoriosInputFileTO inputFile, ReservatoriosJobSubmissionTO reservatoriosJobSubmissionTO) throws JobSpecificationException, TaskSpecificationException, IOException, ExecutionException {

		List<TaskSpecification> tasksList = new ArrayList<TaskSpecification>();
		setScenarioFile(null);
		setVolumeFile(null);
		setType("");
		setStartMonth(inputFile.getStartMonth());
		setEndMonth(inputFile.getEndMonth());
		setDry(inputFile.getDry());
		setNormal(inputFile.getNormal());
		setRainy(inputFile.getRainy());


		if(inputFile.getComplete()) {
			setType("complete");
		} else {
			setType("cenarios");
		}

		File remoteFolder = new File(getFilesPath());
		for (String folder : remoteFolder.list()) {
			File reservoir = new File(remoteFolder.getAbsolutePath()+ FILE_SEPARATOR  + folder);
			if(!reservoir.isDirectory()) {
				if(getScenarioFile() == null || getVolumeFile() == null){
					if(reservoir.toString().endsWith(".cen")) {
						setScenarioFile(reservoir);
					}else if(reservoir.toString().endsWith(".txt")) {
						setVolumeFile(reservoir);
					}
				}else if(getScenarioFile()!= null && getVolumeFile() != null) {
					break;
				}
			}
		}

		for (int i =0; i < remoteFolder.list().length; i++) {
			String folder = remoteFolder.list()[i];
			File reservoir = new File(remoteFolder.getAbsolutePath()+ FILE_SEPARATOR  + folder);
			if(reservoir.isDirectory()) {
				for (String files : reservoir.list()) {
					if(files.endsWith(".phr")) {
						this.setPhrFile(new File(reservoir.getAbsolutePath() + FILE_SEPARATOR + files));
					} else if(files.endsWith(".dem")) {
						this.setDemandaFile(new File(reservoir.getAbsolutePath() + FILE_SEPARATOR + files));
					}else if(files.endsWith(".his")) {
						this.setHistoryFile(new File(reservoir.getAbsolutePath() + FILE_SEPARATOR + files));
					}else if(files.endsWith(".pes")) {
						this.setPesoPeriodoFile(new File(reservoir.getAbsolutePath() + FILE_SEPARATOR + files));
					}else if(files.endsWith(".res")) {
						this.setReservoirFile(new File(reservoir.getAbsolutePath() + FILE_SEPARATOR + files));
					}else if(files.endsWith(".lam")){
						this.setLaminaFile(new File(reservoir.getAbsolutePath() + FILE_SEPARATOR + files));
					}
				}

				String outputFile = folder;
				if(getType().equals("complete")) {
					outputFile += ".zip";
				}else {
					outputFile += ".out";
				}
				String outputPath = remoteFolder.getAbsolutePath() + FILE_SEPARATOR + "output";
				
				createJobSpecification(tasksList, outputFile, outputPath);
			}
			
		}
		return new JobSpecification("Reservoir Job", "", tasksList);
	}

	private void createJobSpecification(List<TaskSpecification> tasksList, String outputFile, String outputPath) throws TaskSpecificationException, ExecutionException {

		String jHidroPath = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.RESERVOIR_PATH);

		File file = new File (jHidroPath);
		try {
			jHidroPath = file.getCanonicalPath();
		} catch (IOException e1) {
			throw new ExecutionException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}

		if(verifyFiles()) {
			// create initBlock
			IOBlock initBlock = new IOBlock();
			IOEntry jhidroFile = new IOEntry("put", jHidroPath, "jhidro.jar");
			IOEntry reservoirFile = new IOEntry("put", getReservoirFile().getAbsolutePath(), getReservoirFile().getName());
			IOEntry laminaFile = new IOEntry("put", getLaminaFile().getAbsolutePath(), getLaminaFile().getName());
			IOEntry demandaFile = new IOEntry("put", getDemandaFile().getAbsolutePath(), getDemandaFile().getName());
			IOEntry historyFile = new IOEntry("put", getHistoryFile().getAbsolutePath(), getHistoryFile().getName());
			IOEntry pesoPeriodoFile = new IOEntry("put", getPesoPeriodoFile().getAbsolutePath(), getPesoPeriodoFile().getName());
			IOEntry phrFile = new IOEntry("put", getPhrFile().getAbsolutePath(), getPhrFile().getName());
			IOEntry scenarioFile = new IOEntry("put", getScenarioFile().getAbsolutePath(), getScenarioFile().getName());
			IOEntry volumesFile = new IOEntry("put", getVolumeFile().getAbsolutePath(), getVolumeFile().getName());

			initBlock.putEntry(jhidroFile);
			initBlock.putEntry(reservoirFile);
			initBlock.putEntry(laminaFile);
			initBlock.putEntry(demandaFile);
			initBlock.putEntry(historyFile);
			initBlock.putEntry(pesoPeriodoFile);
			initBlock.putEntry(phrFile);
			initBlock.putEntry(scenarioFile);
			initBlock.putEntry(volumesFile);

			//create remotExe
			String remoteExe = "";
			if(getType().equalsIgnoreCase("complete")) {
				remoteExe = "java -jar jhidro.jar " + startMonth.toString() + " " + endMonth.toString() + " completo " + getVolumeFile().getName() + " " + getReservoirFile().getName() + " " +
				getDemandaFile().getName()+ " " + getLaminaFile().getName() + " " + outputFile + " mensal " + getHistoryFile().getName() + " " + getPesoPeriodoFile().getName() + " " + getScenarioFile().getName();
			}else {
				remoteExe = "java -jar jhidro.jar " + startMonth.toString() + " " + endMonth.toString() + " cenarios " + getVolumeFile().getName() + " " + getReservoirFile().getName() + " " +
				getDemandaFile().getName()+ " " + getLaminaFile().getName() + " " + outputFile + " mensal " + getPhrFile().getName()+ " " + getScenarioFile().getName();
			}

			//create finalBlock
			IOBlock finalBlock = new IOBlock();
			IOEntry finalEntry = new IOEntry("get", outputFile, outputPath + FILE_SEPARATOR +outputFile);
			finalBlock.putEntry(finalEntry);


			TaskSpecification taskSpec = new TaskSpecification(initBlock, remoteExe, finalBlock, null);
			tasksList.add(taskSpec);
		}
	}

	private boolean verifyFiles() {
		return (getScenarioFile() != null && getVolumeFile() != null && getReservoirFile() != null &&
				getLaminaFile() != null && getHistoryFile() != null && getPesoPeriodoFile() != null &&
				getPhrFile() != null && getDemandaFile() != null); 
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

	public File getVolumeFile() {
		return volumeFile;
	}

	public void setVolumeFile(File volumeFile) {
		this.volumeFile = volumeFile;
	}

	public File getReservoirFile() {
		return reservoirFile;
	}

	public void setReservoirFile(File reservoirFile) {
		this.reservoirFile = reservoirFile;
	}

	public File getDemandaFile() {
		return demandaFile;
	}

	public void setDemandaFile(File demandaFile) {
		this.demandaFile = demandaFile;
	}

	public File getPhrFile() {
		return phrFile;
	}

	public void setPhrFile(File phrFile) {
		this.phrFile = phrFile;
	}

	public File getHistoryFile() {
		return historyFile;
	}

	public void setHistoryFile(File historyFile) {
		this.historyFile = historyFile;
	}

	public File getLaminaFile() {
		return laminaFile;
	}

	public void setLaminaFile(File laminaFile) {
		this.laminaFile = laminaFile;
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

	public Integer getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(Integer startMonth) {
		this.startMonth = startMonth;
	}

	public Integer getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(Integer endMonth) {
		this.endMonth = endMonth;
	}

	public double getDry() {
		return dry;
	}

	public void setDry(double dry) {
		this.dry = dry;
	}

	public double getNormal() {
		return normal;
	}

	public void setNormal(double normal) {
		this.normal = normal;
	}

	public double getRainy() {
		return rainy;
	}

	public void setRainy(double rainy) {
		this.rainy = rainy;
	}

	//	public List<File> getPhrFileList() {
	//		return phrFileList;
	//	}
	//
	//	public void setPhrFileList(List<File> phrFileList) {
	//		this.phrFileList = phrFileList;
	//	}
	//
	//	public List<File> getDemandaFileList() {
	//		return demandaFileList;
	//	}
	//
	//	public void setDemandaFileList(List<File> demandaFileList) {
	//		this.demandaFileList = demandaFileList;
	//	}
	//
	//	public List<File> getHistoryFileList() {
	//		return historyFileList;
	//	}
	//
	//	public void setHistoryFileList(List<File> historyFileList) {
	//		this.historyFileList = historyFileList;
	//	}
	//
	//	public List<File> getPesoPeriodoFileList() {
	//		return pesoPeriodoFileList;
	//	}
	//
	//	public void setPesoPeriodoFileList(List<File> pesoPeriodoFileList) {
	//		this.pesoPeriodoFileList = pesoPeriodoFileList;
	//	}
	//
	//	public List<File> getReservoirFileList() {
	//		return reservoirFileList;
	//	}
	//
	//	public void setReservoirFileList(List<File> reservoirFileList) {
	//		this.reservoirFileList = reservoirFileList;
	//	}
	//
	//	public List<File> getLaminaFileList() {
	//		return laminaFileList;
	//	}
	//
	//	public void setLaminaFileList(List<File> laminaFileList) {
	//		this.laminaFileList = laminaFileList;
	//	}

}
