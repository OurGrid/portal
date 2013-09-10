package org.ourgrid.portal.server.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ourgrid.portal.server.logic.BrokerPortalController;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

public class DownloadServlet extends HttpServlet {

	private static final String CONTENT_DISPOSITION = "Content-Disposition";
	private static final String HEADER_ATTACHMENT = "Attachment;Filename=";
	public static final String FILE_NAME_PARAMETER = "filename";
	public static final String FILE_LOCATION = "location";
	private static final String CONTENT_TYPE = "application/octet-stream";
	private static final long serialVersionUID = -3044240638550292951L;
	
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String location = request.getParameter(FILE_LOCATION);
		String resultFile = request.getParameter(FILE_NAME_PARAMETER);
		OurGridPortalIF portal = null;
		File downloadFile = null;
		
		try {
			
			downloadFile = null;
			portal = BrokerPortalController.getInstance();
			
			if (location != null){
				downloadFile = getFileByName(location, resultFile);
			} 
			
			if (downloadFile == null) {
				//TODO log
				return;
			}
			
			String downloadFileName = downloadFile.getName().replaceAll(" ", "_");
			String disHeader = HEADER_ATTACHMENT + downloadFileName;
			response.setHeader(CONTENT_DISPOSITION, disHeader);
			response.setContentType(CONTENT_TYPE);
			
			writeFileOnOutputStream(response.getOutputStream(), downloadFile);
			
		} catch (Exception e) {
			
			portal.getLogger().error("Error on download service.", e);
		}
	}
	
	private File getFileByName(String location, String fileName) {
		String storagePath = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.STORAGE_DIRECTORY);
		File file = new File(storagePath + location + File.separator + fileName);
		return file;
	}
	
	private void writeFileOnOutputStream(OutputStream outputStream, File downloadFile) throws IOException {

		BufferedInputStream in = new BufferedInputStream(new FileInputStream(downloadFile));
		
		byte[] buffer = new byte[1024];
		
		while((in.read(buffer)) != -1){
			outputStream.write(buffer);
		}
		
		outputStream.flush();
		outputStream.close();
		
	}
}