package org.ourgrid.portal.client.plugin.autodock.gui.model;

import java.io.Serializable;

import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.plugin.autodock.to.model.AutodockInputFileTO;

public class AutodockJobRequest extends AbstractRequest implements Serializable {

	private static final long serialVersionUID = 2287719427433948796L;
	
	private AutodockInputFileTO macromoleculeRigidModel;
	private AutodockInputFileTO macromoleculeFlexibleModel;
	private AutodockInputFileTO gridParameters;
	private AutodockInputFileTO ligantModel;
	private AutodockInputFileTO dockingParameters;
	
	public AutodockJobRequest() {
		super();
	}
	
	public AutodockJobRequest(int jobID, long uploadId, String userLogin, boolean emailNotification,
			AutodockInputFileTO macromoleculeRigidModel, AutodockInputFileTO macromoleculeFlexibleModel, AutodockInputFileTO gridParameters, 
			AutodockInputFileTO ligantModel, AutodockInputFileTO dockingParameters) {
		super(jobID, uploadId, userLogin, emailNotification, CommonServiceConstants.AUTODOCK_JOB);
		
		this.macromoleculeRigidModel = macromoleculeRigidModel;
		this.macromoleculeFlexibleModel = macromoleculeFlexibleModel;
		this.gridParameters = gridParameters;
		this.ligantModel = ligantModel;
		this.dockingParameters = dockingParameters;
	}

	public AutodockInputFileTO getMacromoleculeRigidModel() {
		return macromoleculeRigidModel;
	}
	
	public void setMacromoleculeRigidModel(
			AutodockInputFileTO macromoleculeRigidModel) {
		this.macromoleculeRigidModel = macromoleculeRigidModel;
	}
	
	public AutodockInputFileTO getMacromoleculeFlexibleModel() {
		return macromoleculeFlexibleModel;
	}
	
	public void setMacromoleculeFlexibleModel(
			AutodockInputFileTO macromoleculeFlexibleModel) {
		this.macromoleculeFlexibleModel = macromoleculeFlexibleModel;
	}
	
	public AutodockInputFileTO getGridParameters() {
		return gridParameters;
	}
	
	public void setGridParameters(AutodockInputFileTO gridParameters) {
		this.gridParameters = gridParameters;
	}
	
	public AutodockInputFileTO getLigantModel() {
		return ligantModel;
	}
	
	public void setLigantModel(AutodockInputFileTO ligantModel) {
		this.ligantModel = ligantModel;
	}
	
	public AutodockInputFileTO getDockingParameters() {
		return dockingParameters;
	}
	
	public void setDockingParameters(AutodockInputFileTO dockingParameters) {
		this.dockingParameters = dockingParameters;
	}
	
}