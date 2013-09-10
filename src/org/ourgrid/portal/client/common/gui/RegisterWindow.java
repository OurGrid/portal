package org.ourgrid.portal.client.common.gui;

import java.util.List;

import org.ourgrid.portal.client.common.CaptchaChallenge;
import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.exceptions.OurGridPortalClientException;
import org.ourgrid.portal.client.common.to.response.CaptchaGeneratorResponseTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.common.to.service.UserRegistrationTO;
import org.ourgrid.portal.client.common.util.OurGridPortalServerUtil;
import org.ourgrid.portal.client.common.util.VType;
import org.ourgrid.portal.client.common.util.VTypeValidator;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;

public class RegisterWindow extends Window {

	private final static String CAPTCHA_IMAGE_MSG = "Please write this words in the box below.";

	private static final String PASSWORD_FIELD_NAME = "Password";

	private static final String EMAIL_FIELD_NAME = "Email";

	private static final String LOGIN_FIELD_NAME = "Login";
	
	private FormData formData = new FormData();
	
    private FormPanel formPanel;

	private TextField<String> email;

	private TextField<String> password;

	private TextField<String> confirmPassw;

	private TextField<String> login;

	private TextField<String> captcha;

	private LayoutContainer captchaContainer;
	
	private CaptchaChallenge challenge;
	
	private Image captchaImage;
		
	public RegisterWindow() {
		super();
		
		init();
		
		createRegisterForm();
		createRegisterLayout();
		createCaptchaLayout();
	  
		createSubmitButton();
		createCancelButton();
		
		formPanel.setButtonAlign(HorizontalAlignment.CENTER);  
  
	    this.add(formPanel);
	}

	private void init() {
		this.setSize(500, 365);  
		this.setResizable(false);
		this.setPlain(true);  
		this.setActive(true);
		this.setBodyBorder(true);
		this.setHeaderVisible(false);
		this.setResizable(false);
		this.setHeading("Register Form");
	}

	private void createCancelButton() {
		Button cancelButton = new Button("Cancel");
		cancelButton.setBorders(true);
		cancelButton.setEnabled(true);
		
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				closeRegisterWindow();
			}
		});
		
		formPanel.addButton(cancelButton);	
	}

	private void createSubmitButton() {
		Button submitButton = new Button("Submit");  
		submitButton.setBorders(true);
		submitButton.setEnabled(true);
		submitButton.setId("submitButtonIdRegister");
		
		formPanel.addButton(submitButton);  
		
		submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

			public void componentSelected(ButtonEvent ce) {
				doSubmitRegistration();
			}
		});
	}
	
	protected void doSubmitRegistration() {
		if (validateFields()) {
			submitRegistration();
			formPanel.el().mask("Loading...");
		} 		
	}

	protected boolean validateFields() {
		
        
        if (!login.isValid()) {
           	MessageBox.alert("Login Failed", "Your login is not valid. Please, insert a alphanumeric characters.", null);
        	return false;
        }
        
        if (getPasswordFieldValue() == null || !getPasswordFieldValue().equals(getConfirmationPasswordFieldValue())) {
        	MessageBox.alert("Password Failed", "The passwords doesn't match!", null);
        	return false;
        }
        
        if (!email.isValid()) {
        	MessageBox.alert("Email Failed", "Your email is not valid.", null);
        	return false;
        }

        String answer = getCaptchaFieldValue();
        if (answer == null || !answer.trim().equals(challenge.getAnswer())){
        	MessageBox.alert("Captcha Failed", "The given words don't match.", null);
        	return false;
        }
        
		return true;
	}

	private void createCaptchaLayout() {
		
		LayoutContainer layoutContainer = new LayoutContainer();  
		layoutContainer.setLayout(new ColumnLayout());

	    //Left
	    LayoutContainer left = new LayoutContainer();  
	    left.setStyleAttribute("paddingLeft", "1px");  
	    FormLayout layout = new FormLayout();  
	    layout.setLabelAlign(LabelAlign.LEFT);  
	    left.setLayout(layout); 
	    
	    captcha = createField("Captcha", true);
	    captcha.setId("captchaIdRegister");
		left.add(captcha, formData);
		
	    //Right
	    LayoutContainer right = new LayoutContainer();  
	    right.setStyleAttribute("paddingRight", "4px");  
	    layout = new FormLayout();  
	    layout.setLabelAlign(LabelAlign.RIGHT);  
	    right.setLayout(layout);  
		
		Image info = new Image("resources/images/silk/information.gif");  
		info.setTitle("A CAPTCHA is a challenge-response test to determine whether the user is human.");

		right.add(info, formData);
		
		layoutContainer.add(left, new ColumnData(.68));
		layoutContainer.add(right, new ColumnData(.32)); 
		
		formPanel.add(layoutContainer);
	}

	private void createRegisterLayout() {
		
		LayoutContainer layoutContainer = new LayoutContainer();  
		layoutContainer.setLayout(new ColumnLayout());  
	  
	    captchaContainer = new LayoutContainer();  
	    captchaContainer.setStyleAttribute("paddingLeft", "1px");  
	    FormLayout layout = new FormLayout();  
	    layout.setLabelAlign(LabelAlign.LEFT);  
	    captchaContainer.setLayout(layout);  
		
	   
	    login = createField(LOGIN_FIELD_NAME, false);
	    login.setValidator(new VTypeValidator(VType.ALPHANUMERIC));
	    login.setId("loginIdRegister");
	    captchaContainer.add(login, formData);
	    
	    password = createField(PASSWORD_FIELD_NAME, false);
	    password.setPassword(true);
	    password.setId("passwordIdRegister");
	    captchaContainer.add(password, formData); 
	    
	    confirmPassw = createField("Confirm", false);
	    confirmPassw.setPassword(true);
	    confirmPassw.setId("confirmPasswordIdRegister");
	    captchaContainer.add(confirmPassw, formData); 
	    
	    email = createField(EMAIL_FIELD_NAME, false);
	    email.setValidator(new VTypeValidator(VType.EMAIL));
	    email.setId("emailIdRegister");
	    captchaContainer.add(email, formData); 
		
	    email.isValid();
	    
	    captchaImage = new Image();
	    
		captchaContainer.add(captchaImage, formData);
		captchaContainer.add(new Text(""));

		layoutContainer.add(captchaContainer, new ColumnData(.72));  
		
		formPanel.add(layoutContainer);
		
	}

	private TextField<String> createField(String field, boolean isAllowBlank) {

		TextField<String> textField = new TextField<String>();
		textField.setFieldLabel(field);
		textField.setAllowBlank(isAllowBlank); 
		return textField;
	}

	private void createRegisterForm() {
		formPanel = new FormPanel();
		formPanel.setFrame(true); 
		formPanel.setBorders(false);
		formPanel.setAutoHeight(true);
		formPanel.setAutoWidth(true);
		formPanel.setHeading("Register Form");
		
		new KeyNav<ComponentEvent>(formPanel) {
			
			public void onEnter(ComponentEvent ce) {
				doSubmitRegistration();
			}
		};
	}

	private void closeRegisterWindow() {
		cleanFields();
		this.setVisible(false);
	}
	
	public void updateChallenge(){

		ServiceTO captchaGeneratorTO = createCaptchaGeneratorTO();
		
		OurGridPortalServerUtil.getInstance().execute(captchaGeneratorTO, new AsyncCallback<CaptchaGeneratorResponseTO>() {

			public void onFailure(Throwable caught) {}

			public void onSuccess(CaptchaGeneratorResponseTO result) {
				updateChallenge(result.getCaptchaChallenge());
		    	refreshCaptchaImage();
			}
		});
    }
	
	protected void updateChallenge(CaptchaChallenge captchaChallenge) {
		this.challenge = captchaChallenge;
	}

	protected void refreshCaptchaImage() {
		
    	captchaContainer.remove(captchaImage);
    	
		String imageURL = "";
		if (challenge != null) {
			imageURL = challenge.getImageURL();
		}

		captchaImage = new Image(imageURL);
    	captchaImage.setTitle(CAPTCHA_IMAGE_MSG);
    	
    	captchaContainer.add(captchaImage);
    	this.doLayout();
	}

	protected void submitRegistration() {
		
		ServiceTO serviceTO;
		try {
			serviceTO = createUserRegistrationTO();
			OurGridPortalServerUtil.getInstance().execute(serviceTO, new AsyncCallback<ResponseTO>() {
				
				public void onSuccess(ResponseTO result) {
					formPanel.el().unmask();
					MessageBox.info("User Registration", result.getMessage(), null);
					closeRegisterWindow();
				}
				
				public void onFailure(Throwable caught) {
					formPanel.el().unmask();
					MessageBox.alert("User Registration Error", caught.getMessage(), null);
				}
			});
			
		} catch (OurGridPortalClientException e) {
			MessageBox.alert("User Registration Error", e.getMessage(), null);
		}
	}

	protected void cleanFields() {
		for (Field<?> field : getFields()) {
			field.setValue(null);
			field.clearInvalid();
		}
	}

	private ServiceTO createUserRegistrationTO() throws OurGridPortalClientException {
		
		UserRegistrationTO userRegistrationTO = new UserRegistrationTO();
		userRegistrationTO.setExecutorName(CommonServiceConstants.USER_REGISTRATION_EXECUTOR);
		userRegistrationTO.setLogin(getLoginFieldValue());
		userRegistrationTO.setPassword(getEncryptedPassword());
		userRegistrationTO.setEmail(getEmailFieldValue());
		
		return userRegistrationTO;
	}
	
	private ServiceTO createCaptchaGeneratorTO() {
		ServiceTO captchaGeneratorTO = new ServiceTO();
		captchaGeneratorTO.setExecutorName(CommonServiceConstants.CAPTCHA_GENERATOR_EXECUTOR);
		return captchaGeneratorTO;
	}
	
	public void show() {
		super.show();
		this.updateChallenge();
	}


	private String getPasswordFieldValue() {
		return password.getValue() == null ? "" : password.getValue();
	}

	private String getConfirmationPasswordFieldValue() {
		return confirmPassw.getValue() == null ? "" : confirmPassw.getValue();
	}
	
	private String getEncryptedPassword() throws OurGridPortalClientException {
		return getPasswordFieldValue();
	}
	
	private List<Field<?>> getFields() {
		return formPanel.getFields();
	}

	private String getEmailFieldValue() {
		return email.getValue() == null ? "" : email.getValue();
	}

	private String getLoginFieldValue() {
		return login.getValue() == null ? "" : login.getValue();
	}

	private String getCaptchaFieldValue() {
		return captcha.getValue() == null ? "" : captcha.getValue();
	}

	
}