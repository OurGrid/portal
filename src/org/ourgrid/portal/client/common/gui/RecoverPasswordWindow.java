package org.ourgrid.portal.client.common.gui;

import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.common.to.service.UserPasswordRecoverTO;
import org.ourgrid.portal.client.common.util.OurGridPortalServerUtil;
import org.ourgrid.portal.client.common.util.VType;
import org.ourgrid.portal.client.common.util.VTypeValidator;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RecoverPasswordWindow extends Window {

	private final FormPanel formPanel = new FormPanel();
	
	private TextField<String> email;
	
	public RecoverPasswordWindow() {
		super();
		
		init();
		createRecoverPasswordForm();
		createEmailField();
		
		createRecoverButton();
		createCancelButton();
		
		formPanel.setButtonAlign(HorizontalAlignment.CENTER);  
		this.add(formPanel);
	}
	
	private void createCancelButton() {
		
		Button cancelButton = new Button("Cancel");
		cancelButton.setBorders(true);
		cancelButton.setEnabled(true);
		cancelButton.setId("cancelButtonIdRecoverPassw");
		
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				closeRegisterWindow();
			}
		});
		
		formPanel.addButton(cancelButton);	
	}

	protected void closeRegisterWindow() {
		cleanEmailField();
		this.setVisible(false);
	}

	private void createRecoverButton() {
		
		Button submitButton = new Button("Recover");  
		submitButton.setBorders(true);
		submitButton.setEnabled(true);
		submitButton.setId("recoverButtonIdRecoverPassw");
		
		formPanel.addButton(submitButton);  
		
		submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			public void componentSelected(ButtonEvent ce) {
				doUserRecoverPassword();
			}
		});
	}

	protected void doUserRecoverPassword() {
		if (validateFields()) {
			submitUserRecoverPassword();
			formPanel.el().mask("Loading...");
		} 
	}

	protected boolean validateFields() {
		
        if (!email.isValid()) {
        	MessageBox.alert("Email Failed", "Your email is not valid.", null);
        	return false;
        }
		
		return true;
	}

	private void createEmailField() {
		email = new TextField<String>();
		email.setId("emailIdRecoverPassw");
		email.setFieldLabel("Email");
		email.setAllowBlank(false);
        email.setAutoValidate(true);
	    email.setValidator(new VTypeValidator(VType.EMAIL));
        
        formPanel.add(email);
	}

	private void createRecoverPasswordForm() {
		formPanel.setFrame(true); 
		formPanel.setBorders(false);
		formPanel.setAutoHeight(true);
		formPanel.setAutoWidth(true);
		formPanel.setHeading("Recover Password Form");
		
		new KeyNav<ComponentEvent>(formPanel) {
			
			public void onEnter(ComponentEvent ce) {
				doUserRecoverPassword();
			}
		};
	}

	public void init() {  
		this.setSize(370, 135);  
		this.setPlain(true);  
		this.setActive(true);
		this.setBodyBorder(true);
		this.setHeaderVisible(false);
		this.setHeading("Recover Password");
		this.setClosable(false);
		this.setResizable(false);
		this.setModal(true);
	}
	
	protected void submitUserRecoverPassword() {
		ServiceTO serviceTO = createUserRecoverPasswordTO();
		
		OurGridPortalServerUtil.getInstance().execute(serviceTO, new AsyncCallback<ResponseTO>() {
			
			public void onSuccess(ResponseTO result) {
				formPanel.el().unmask();
				MessageBox.info("Password recovered", result.getMessage(), null);
				closeRecoverWindow();
			}
			
			public void onFailure(Throwable caught) {
				formPanel.el().unmask();
				MessageBox.alert("Recover Password Error", caught.getMessage(), null);
			}
		});
	}
	
	protected void closeRecoverWindow() {
		cleanEmailField();
		this.setVisible(false);
	}

	private void cleanEmailField() {
		email.setValue("");
		email.clearInvalid();
	}
	
	private String getEmailFieldValue() {
		return email.getValue() == null ? "" : email.getValue();
	}

	private ServiceTO createUserRecoverPasswordTO() {
		UserPasswordRecoverTO userPasswordRecoverTO = new UserPasswordRecoverTO();
		userPasswordRecoverTO.setExecutorName(CommonServiceConstants.USER_PASSWORD_RECOVER_EXECUTOR);
		userPasswordRecoverTO.setEmail(getEmailFieldValue());
		return userPasswordRecoverTO;
	}
}
