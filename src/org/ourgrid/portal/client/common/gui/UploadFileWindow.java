package org.ourgrid.portal.client.common.gui;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.gui.model.UserModel;
import org.ourgrid.portal.client.common.to.model.FileTO;
import org.ourgrid.portal.client.common.to.response.UploadSessionIDResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.common.to.service.UploadSessionIDTO;
import org.ourgrid.portal.client.common.util.OurFileUploadEventListener;
import org.ourgrid.portal.client.common.util.OurGridPortalServerUtil;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class UploadFileWindow extends Window {

	private final FormPanel formPanel = new FormPanel();
	
	private OurFileUploadField fileUploadField;
	
	private String location;

	private FileTO root;
	
	private boolean proxyCertificate;
	
	public UploadFileWindow(FileTO root, boolean proxyCertificate) {
		super();
	
		init();
		this.root = root;
		this.location = root.getLocation();
		this.proxyCertificate = proxyCertificate;
		createUploadFileForm();
		createUploadField();
	}
	
	private void init() {
		this.setSize(300, 80);  
		this.setPlain(true);  
		this.setActive(true);
		this.setBodyBorder(true);
		this.setHeaderVisible(true);
		this.setMinimizable(false);
		this.setMaximizable(false);
		this.setResizable(false);
		this.setHeading("Upload File");
		this.setClosable(true);
	}
	
	private void createUploadFileForm() {
		formPanel.setFrame(true); 
		formPanel.setBorders(false);
		formPanel.setHeight(80);
		formPanel.setWidth(300);
		formPanel.setHeaderVisible(false);
	}
	
	private void createUploadField() {
		
		fileUploadField = new OurFileUploadField();
	    fileUploadField.addListener(new OurFileUploadEventListener() {
			public void onEvent(Event event) {
				maskMainPanel("Loading...");
				requestFileUpload();
			}

  		});
	    
	    
		LayoutContainer container = new LayoutContainer();
		container.setLayout(new RowLayout());
		container.add(fileUploadField);
		
		formPanel.add(container);
		
		formPanel.addListener(Events.Submit, new Listener<FormEvent>() {

			public void handleEvent(FormEvent be) {
				unmaskMainPanel();
				if (isQuotaExceed(be.getResultHtml())) {
					MessageBox.alert("Disk quota exceeded", "The specified file exceeds your quota.", null);
				} else {
					OurGridPortal.getFileExplorerWindow().explorerNode(root);
					MessageBox.info("File Uploaded", "The file was uploaded successfully",  null);
				}
				hide();
			}
		});
		
		add(formPanel);
	}
	
  	protected boolean isQuotaExceed(String resultHtml) {
		return resultHtml.contains(CommonServiceConstants.QUOTA_EXCEED_ERROR_CODE);
	}
	
	private void requestFileUpload() {
		
  		UserModel userModel = OurGridPortal.getUserModel();
  		ServiceTO serviceTO = createUploadSessionIdTO(userModel.getUserLogin());
		OurGridPortalServerUtil.getInstance().execute(serviceTO, new AsyncCallback<UploadSessionIDResponseTO>() {

			public void onSuccess(UploadSessionIDResponseTO result) {
				sendUploadedFile(result.getUploadId());
			}
			
			public void onFailure(Throwable caught) {
				unmaskMainPanel();
				MessageBox.alert("Upload Error", caught.getMessage(), null);
			}
			
		});
	}
	
	private ServiceTO createUploadSessionIdTO(String userLogin) {
  		
  		UploadSessionIDTO uploadSessionIdTO = new UploadSessionIDTO();
  		uploadSessionIdTO.setExecutorName(CommonServiceConstants.UPLOAD_SESSION_ID_EXECUTOR);
  		uploadSessionIdTO.setUserLogin(userLogin);
  		uploadSessionIdTO.setLocation(this.location);
  		uploadSessionIdTO.setProxyCertificate(proxyCertificate);
  		
  		return uploadSessionIdTO;
  	}
	
  	private void sendUploadedFile(Long uploadId) {
  		
  		UserModel userModel = OurGridPortal.getUserModel();
  		
  		userModel.setUploadSessionId(uploadId);
  		fileUploadField.setName(uploadId.toString());
  		String formAction;
  		if(proxyCertificate) {
  			formAction = CommonServiceConstants.UPLOAD_FORM_ACTION + 
  	  		CommonServiceConstants.USER_NAME_PARAMETER + "=" + userModel.getUserLogin() + "&" +
  	  		CommonServiceConstants.TO_DESCOMPACT + "=false&" +
  	  		CommonServiceConstants.TEMP_FOLDER_PARAMETER + "=false&" +
  	  		CommonServiceConstants.UPLOAD_ID_PARAMETER + "=" + uploadId + "&" +
  	  		CommonServiceConstants.FILE_RENAME + "=" +CommonServiceConstants.USER_CERT;
  		}else {
  			formAction = CommonServiceConstants.UPLOAD_FORM_ACTION + 
  	  		CommonServiceConstants.USER_NAME_PARAMETER + "=" + userModel.getUserLogin() + "&" +
  	  		CommonServiceConstants.TO_DESCOMPACT + "=false&" +
  	  		CommonServiceConstants.TEMP_FOLDER_PARAMETER + "=false&" +
  	  		CommonServiceConstants.UPLOAD_ID_PARAMETER + "=" + uploadId;
  		}
 		
  		
  		formPanel.setAction(formAction);
  		formPanel.setMethod(Method.POST);
  		formPanel.setEncoding(Encoding.MULTIPART);
  		formPanel.submit();
  		
  	}
	
	protected void maskMainPanel(String message) {
  		this.el().mask(message);
	}
	
  	protected void unmaskMainPanel() {
		this.el().unmask();
	}

	public void closeWindow() {
		this.setVisible(false);
	}
}