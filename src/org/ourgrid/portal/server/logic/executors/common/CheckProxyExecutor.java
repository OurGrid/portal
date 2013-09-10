package org.ourgrid.portal.server.logic.executors.common;

import java.io.File;
import java.io.FileInputStream;

import org.globus.gsi.GlobusCredential;
import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.response.CheckProxyResponseTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.CheckProxyTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class CheckProxyExecutor extends AbstractExecutor {

	private final String FILE_SEPARATOR = System.getProperty("file.separator");
	
	public CheckProxyExecutor(OurGridPortalIF portal) {
		super(portal);
	}
	
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {

		CheckProxyTO checkProxyTO = (CheckProxyTO) serviceTO;

		String storageRoot = OurgridPortalProperties.getInstance().getProperty(
				OurgridPortalProperties.STORAGE_DIRECTORY);
		String userHome = storageRoot + FILE_SEPARATOR + checkProxyTO.getUserName();
		
		CheckProxyResponseTO response = new CheckProxyResponseTO();
		
		File proxyFile = new File(userHome + 
				FILE_SEPARATOR + CommonServiceConstants.USER_CERT);
		response.setProxyExists(proxyFile.exists());
		
		if (response.getProxyExists()) {
			try {
				
				GlobusCredential proxy = new GlobusCredential(
						new FileInputStream(proxyFile.getCanonicalPath()));
				response.setTimeLeft(proxy.getTimeLeft());
			} catch (Exception e) {
				response.setTimeLeft(0L);
			}
		}
		
		return response;
	}

	@Override
	public void logTransaction(ServiceTO serviceTO) {
		
	}
	
}