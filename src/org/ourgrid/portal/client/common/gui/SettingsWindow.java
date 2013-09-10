package org.ourgrid.portal.client.common.gui;

import java.util.List;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.model.UserTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.response.UserSettingsResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.common.to.service.UserSettingsTO;
import org.ourgrid.portal.client.common.util.OurGridPortalServerUtil;
import org.ourgrid.portal.client.common.util.VType;
import org.ourgrid.portal.client.common.util.VTypeValidator;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SettingsWindow extends Window{
	
	private final FormPanel formPanel = new FormPanel();
	private TextField<String> email;
	private TextField<String> newPassword;
	private TextField<String> confirmNewPassword;
	private TextField<String> currentPassword;
	
	private UserTO user;
	
	public SettingsWindow(UserTO user) {
		super();
		
		this.user = user;
		init();
		
		createLoginForm();
		createCurrentPasswField();
		createNewPasswField();
		createConfirmNewPasswField();
		createNewEmailField();

        createSubmitButton();
        createCancelButton();
		
		formPanel.setButtonAlign(HorizontalAlignment.CENTER);  
        
        this.add(formPanel);
	}

	public SettingsWindow() {}

	private void createNewEmailField() {
		email = createField("Email", true);
		email.setName("Email");
		email.setValue(user.getEmail());
		email.setValidator(new VTypeValidator(VType.EMAIL));
		email.setId("emailIdSettings");
		formPanel.add(email);
	}

	private void createConfirmNewPasswField() {
		confirmNewPassword = createField("Confirm new password", true);
		confirmNewPassword.setName("Confirm new password");
		confirmNewPassword.setPassword(true);
		confirmNewPassword.setAutoValidate(true);
		confirmNewPassword.setId("confirmNewPasswIdSettings");
        formPanel.add(confirmNewPassword);
	}

	private void createNewPasswField() {
		newPassword = createField("New password", true);
		newPassword.setName("New password");
		newPassword.setPassword(true);
		newPassword.setAutoValidate(true);
		newPassword.setId("newPasswIdSettings");
        formPanel.add(newPassword);
	}

	private void createCurrentPasswField() {
		currentPassword = createField("Current password", true);
		currentPassword.setName("password");
		currentPassword.setPassword(true);
		currentPassword.setAutoValidate(true);
		currentPassword.setId("currentPasswIdSettings");
        formPanel.add(currentPassword);
	}

	private void init() {
		this.setSize(500, 250);  
		this.setPlain(true);  
		this.setActive(true);
		this.setBodyBorder(true);
		this.setHeaderVisible(false);
		this.setHeading("Settings Form");
		this.setClosable(true);
	}

	private void createLoginForm() {
		formPanel.setFrame(true); 
		formPanel.setBorders(false);
		formPanel.setAutoHeight(true);
		formPanel.setAutoWidth(true);
		formPanel.setHeading("Settings Form");
	}

	private TextField<String> createField(String field, boolean isAllowBlank) {

		TextField<String> textField = new TextField<String>();
		textField.setFieldLabel(field);
		textField.setAllowBlank(isAllowBlank);
		
		return textField;
	}
	
	private void createSubmitButton() {
		Button submitButton = new Button("Submit");  
		submitButton.setBorders(true);
		submitButton.setEnabled(true);
		submitButton.setId("submitButtonIdSettings");
		
		submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				
				if (validateFields()) {
					formPanel.el().mask("Loading...");
					submitUserSettings();
				}
			}
			
		});
		
		formPanel.addButton(submitButton);
	}
	
	protected boolean validateFields() {
		
        if (null != getNewPasswordFieldValue() && !getNewPasswordFieldValue().equals(getConfirmationNewPasswordFieldValue())) {
        	MessageBox.alert("Password Failed", "The news passwords doesn't match!", null);
        	return false;
        }
        
        if (!email.isValid()) {
        	MessageBox.alert("Email Failed", "Your new email is not valid!", null);
        	return false;
        }

		return true;
	}

	private void createCancelButton() {
		Button cancelButton = new Button("Cancel");
		cancelButton.setBorders(true);
		cancelButton.setEnabled(true);
		
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				closeWindow();
			}
		});
		
		formPanel.addButton(cancelButton);	
	}

	protected void submitUserSettings() {
		ServiceTO serviceTO = createUserSettingsTO();

		OurGridPortalServerUtil.getInstance().execute(serviceTO, new AsyncCallback<ResponseTO>() {
			
			public void onSuccess(ResponseTO result) {
				MessageBox.info("User Settings", result.getMessage(), null);
				formPanel.el().unmask();
				user = ((UserSettingsResponseTO) result).getUser();
				closeWindow();
			}
			
			public void onFailure(Throwable caught) {
				MessageBox.alert("User Settings Error", caught.getMessage(), null);
				formPanel.el().unmask();
			}
		});
		
	}

	private ServiceTO createUserSettingsTO() {
		
		UserSettingsTO userSettingsTO = new UserSettingsTO();
		userSettingsTO.setExecutorName(CommonServiceConstants.USER_SETTINGS_EXECUTOR);
		userSettingsTO.setLogin(user.getLogin());
		userSettingsTO.setPassword(getCurrentPasswordFieldValue());
		userSettingsTO.setNewPassword(getNewPasswordFieldValue());
		userSettingsTO.setEmail(getNewEmailFieldValue());
		
		return userSettingsTO;
	}
	
	private List<Field<?>> getFields() {
		return formPanel.getFields();
	}
	
	public void closeWindow() {
		cleanFields();
		this.email.setValue(user.getEmail());
		this.setVisible(false);
		this.setEnabled(false);
		OurGridPortal.removeWindow(this);
	}

	protected void cleanFields() {
		for (Field<?> field : getFields()) {
			field.setValue(null);
			field.clearInvalid();
		}
	}
	
	private String getNewPasswordFieldValue() {
		return newPassword.getValue() == null ? "" : newPassword.getValue();
	}

	private String getConfirmationNewPasswordFieldValue() {
		return confirmNewPassword.getValue() == null ? "" : confirmNewPassword.getValue();
	}
	
	private String getCurrentPasswordFieldValue() {
		return currentPassword.getValue() == null ? "" : currentPassword.getValue();
	}
	
	private String getNewEmailFieldValue() {
		return email.getValue() == null ? "" : email.getValue();
	}

}