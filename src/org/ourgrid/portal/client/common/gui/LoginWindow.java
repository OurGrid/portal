package org.ourgrid.portal.client.common.gui;

import java.util.List;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.common.to.model.UserTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.response.UserLoginResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.common.to.service.UserLoginTO;
import org.ourgrid.portal.client.common.util.OurGridPortalServerUtil;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LoginWindow extends Window {
	
	private final RegisterWindow registerWindow = new RegisterWindow();
	private final RecoverPasswordWindow recoverPasswordWindow = new RecoverPasswordWindow();
	private final FormPanel formPanel = new FormPanel();
	private final LoginHelpWindow loginHelpWindow = new LoginHelpWindow();
	private final AcknowledgementWindow acknowledgementWindow = new AcknowledgementWindow();

	private TextField<String> login;
	private TextField<String> password;
	
	private OurGridPortal portal;
	
	public LoginWindow(OurGridPortal portal) {
		super();
		
		this.portal = portal;
		init();
		
		createLoginForm();
		createLoginField();
		createPasswordField();
        createSubmitButton();
		createRegisterUserButton();
		createRecoverPasswordButton();
		
        this.add(formPanel);
        
        login.focus();
	}

	private void createPasswordField() {
		password = createField("Password", false);
        password.setAllowBlank(false);
        password.setPassword(true);
        password.setAutoValidate(true);
        password.setId("passwordIdLogin");
        
        formPanel.add(password);
	}

	private void createLoginField() {
		login = createField("Login", false);
		login.setId("loginIdLogin");
		login.setAutoValidate(true);
        
        formPanel.add(login);
	}

	private void init() {
		this.setSize(400, 160);  
		this.setPlain(true);
		this.setActive(true);
		this.setBodyBorder(true);
		this.setHeaderVisible(false);
		this.setHeading("");
		this.setClosable(true);
		this.setModal(true);
		this.setResizable(false);
	}

	private void createLoginForm() {
		formPanel.setFrame(true); 
		formPanel.setBorders(false);
		formPanel.setAutoHeight(true);
		formPanel.setAutoWidth(true);
		formPanel.setHeading("OurGridPortal " + OurGridPortal.VERSION + " - Login Form");
		formPanel.setButtonAlign(HorizontalAlignment.CENTER);
		
		Button acknowledgement = new Button("Acknowledgement");
		acknowledgement.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent arg0) {
				showAcknowledgementWindow();
			}
			
		});
		
		Button help = new Button("Help");
		help.addSelectionListener(new SelectionListener<ButtonEvent>() {

			@Override
			public void componentSelected(ButtonEvent arg0) {
				showLoginHelpWindow();
			}
			
		});
		
		formPanel.getHeader().addTool(help);
		formPanel.getHeader().addTool(acknowledgement); 
		
		new KeyNav<ComponentEvent>(formPanel) {
			
			public void onEnter(ComponentEvent ce) {
				doLogin();
			}
		};
	}

	protected void doLogin() {
		maskLoginFormPanel("Loading...");
		submitUserLogin();
	}

	private TextField<String> createField(String field, boolean isAllowBlank) {

		TextField<String> textField = new TextField<String>();
		textField.setFieldLabel(field);
		textField.setAllowBlank(isAllowBlank);
		
		return textField;
	}
	
	private void createRecoverPasswordButton() {
		Button recoverPassw = new Button("Recover Password");
		recoverPassw.setBorders(true);
		recoverPassw.setEnabled(true);
		recoverPassw.setId("recoverPasswButtonIdLogin");
		
		recoverPassw.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent bt) {
				recoverPasswordWindow.show();
			}
		});
		
		formPanel.addButton(recoverPassw);
	}

	private void createRegisterUserButton() {
		Button registerButton = new Button("Register");
		registerButton.setBorders(true);
		registerButton.setEnabled(true);
		registerButton.setId("registerButtonIdLogin");
		
		registerButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent bt) {
				cleanFields();
				registerWindow.show();
			}
		});
		
		formPanel.addButton(registerButton);
	}

	private void createSubmitButton() {
		Button submitButton = new Button("Submit");  
		submitButton.setBorders(true);
		submitButton.setEnabled(true);
		submitButton.setId("submitButtonIdLogin");
		
		submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				doLogin();
 			}
		});
		
		formPanel.addButton(submitButton);
	}

	protected void submitUserLogin() {
		ServiceTO serviceTO;
		
		serviceTO = createUserLoginTO();
		
		OurGridPortalServerUtil.getInstance().execute(serviceTO, new AsyncCallback<ResponseTO>() {
			
			public void onSuccess(ResponseTO result) {
				
				UserLoginResponseTO loginResponse = (UserLoginResponseTO) result;
				closeLoginWindow();
				desktopConfig(loginResponse.getLoggedUser(), loginResponse.getJobsRequestList());
				unmaskLoginFormPanel();
			}
			
			public void onFailure(Throwable caught) {
				unmaskLoginFormPanel();
				MessageBox.alert("User Login Error", caught.getMessage(), null);
			}
			
		});
	}
	
	public void unmaskLoginFormPanel() {
		formPanel.el().unmask();
	}

	public void maskLoginFormPanel(String message) {
		formPanel.el().mask(message);
	}

	private void desktopConfig(UserTO currentUser, List<AbstractRequest> jobs) {
		portal.desktopConfig(currentUser, jobs);
	}

	private ServiceTO createUserLoginTO() {
		
		UserLoginTO userLoginTO = new UserLoginTO();
		userLoginTO.setExecutorName(CommonServiceConstants.USER_LOGIN_EXECUTOR);
		userLoginTO.setLogin(getLoginFieldValue());
		userLoginTO.setPassword(getEncryptedPassword());
		
		return userLoginTO;
	}
	
	private List<Field<?>> getFields() {
		return formPanel.getFields();
	}
	
	private void closeLoginWindow() {
		cleanFields();
		this.setVisible(false);
		this.loginHelpWindow.closeWindow();
		this.acknowledgementWindow.closeWindow();
	}
	
	protected void cleanFields() {
		for (Field<?> field : getFields()) {
			field.setValue(null);
			field.clearInvalid();
		}
	}
	
	private String getPasswordFieldValue() {
		return password.getValue() == null ? "" : password.getValue();
	}
	
	private String getEncryptedPassword() {
		return getPasswordFieldValue();
	}

	private String getLoginFieldValue() {
		return login.getValue() == null ? "" : login.getValue();
	}
	
	public Window getLoginHelpWindow() {
		return loginHelpWindow;
	}
	
	public void showLoginHelpWindow(){
//		Desktop desktop = OurGridPortal.getDesktop();
		
//		desktop.addWindow(loginHelpWindow);
//		desktop.minimizeWindow(loginHelpWindow);
		loginHelpWindow.show();
	}
	
	public AcknowledgementWindow getAcknowledgementWindow() {
		return acknowledgementWindow;
	}

	public void showAcknowledgementWindow() {
		acknowledgementWindow.show();
	}
}