package org.ourgrid.portal.client.plugin.autodock.to.service;

import org.ourgrid.portal.client.common.to.service.JobSubmissionTO;
import org.ourgrid.portal.client.plugin.autodock.to.model.AutodockInputFileTO;

public class AutodockJobSubmissionTO extends JobSubmissionTO {

	private static final long serialVersionUID = -6611551079605888649L;

	private AutodockInputFileTO macromoleculeRigidModelInputFile;
	private AutodockInputFileTO macromoleculeFlexibleModelInputFile;
	private AutodockInputFileTO gridParametersInputFile;
	private AutodockInputFileTO ligantModelInputFile;
	private AutodockInputFileTO dockingParametersInputFile;

	public AutodockInputFileTO getMacromoleculeRigidModelInputFile() {
		return macromoleculeRigidModelInputFile;
	}
	
	public void setMacromoleculeRigidModelInputFile(AutodockInputFileTO macromoleculeRigidModelInputFile) {
		this.macromoleculeRigidModelInputFile = macromoleculeRigidModelInputFile;
	}
	
	public AutodockInputFileTO getMacromoleculeFlexibleModelInputFile() {
		return macromoleculeFlexibleModelInputFile;
	}
	
	public void setMacromoleculeFlexibleModelInputFile(AutodockInputFileTO macromoleculeFlexibleModelInputFile) {
		this.macromoleculeFlexibleModelInputFile = macromoleculeFlexibleModelInputFile;
	}
	
	public AutodockInputFileTO getGridParametersInputFile() {
		return gridParametersInputFile;
	}
	
	public void setGridParametersInputFile(AutodockInputFileTO gridParametersInputFile) {
		this.gridParametersInputFile = gridParametersInputFile;
	}
	
	public AutodockInputFileTO getLigantModelInputFile() {
		return ligantModelInputFile;
	}
	
	public void setLigantModelInputFile(AutodockInputFileTO ligantModelInputFile) {
		this.ligantModelInputFile = ligantModelInputFile;
	}
	
	public AutodockInputFileTO getDockingParametersInputFile() {
		return dockingParametersInputFile;
	}
	
	public void setDockingParametersInputFile(AutodockInputFileTO dockingParametersInputFile) {
		this.dockingParametersInputFile = dockingParametersInputFile;
	}
	
}