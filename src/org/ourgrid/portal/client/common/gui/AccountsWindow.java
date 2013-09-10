package org.ourgrid.portal.client.common.gui;

import java.util.ArrayList;
import java.util.List;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.gui.model.UserGridModel;
import org.ourgrid.portal.client.common.to.model.UserTO;
import org.ourgrid.portal.client.common.to.response.RemoveUserResponseTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.response.UserApprovalResponseTO;
import org.ourgrid.portal.client.common.to.service.RemoveUserTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.common.to.service.UserApprovalTO;
import org.ourgrid.portal.client.common.util.OurGridPortalServerUtil;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AccountsWindow extends Window {

	private final FormPanel formPanel = new FormPanel();
	private AccountsGrid accounts;

	public AccountsWindow() {
		super();
		init();
		createAccountsForm();
		
		accounts = new AccountsGrid();
		formPanel.add(accounts);
		
        createApproveUserButton();
		createRemoveUserButton();
		createCloseButton();
		
		formPanel.setButtonAlign(HorizontalAlignment.CENTER);
		this.add(formPanel);
	}
	
	private void createCloseButton() {
		Button submitButton = new Button("Close");  
		submitButton.setBorders(true);
		submitButton.setEnabled(true);
		
		submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				closeWindow();
 			}
		});
		formPanel.addButton(submitButton);
	}

	private void createRemoveUserButton() {
		Button removeButton = new Button("Remove"); 
		removeButton.setId("removeUserButtonIdAccounts");
		removeButton.setBorders(true);
		removeButton.setEnabled(true);
		
		removeButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				GridSelectionModel<UserGridModel> gridSelectionModel = accounts.getGrid().getSelectionModel();
				if(!gridSelectionModel.getSelectedItems().isEmpty()){
					removeUser(gridSelectionModel);
					accounts.refresh();
				}
			}
		});
		formPanel.addButton(removeButton);
	}
	
	protected void removeUser(GridSelectionModel<UserGridModel> gridSelectionModel) {

		ServiceTO serviceTO = createRemoveUserTO(gridSelectionModel.getSelectedItems());
		OurGridPortalServerUtil.getInstance().execute(serviceTO, new AsyncCallback<ResponseTO>() {
			
			public void onSuccess(ResponseTO result) {
				MessageBox.info("User Accounts", result.getMessage(), null);
				accounts.replaceStore(((RemoveUserResponseTO) result).getResult());
			}
			
			public void onFailure(Throwable caught) {
				MessageBox.alert("User Accounts Error", caught.getMessage(), null);
				formPanel.el().unmask();
			}
		});
	}

	private ServiceTO createRemoveUserTO(List<UserGridModel> list) {
		
		RemoveUserTO removeUserTO = new RemoveUserTO();
		removeUserTO.setExecutorName(CommonServiceConstants.REMOVE_USER_EXECUTOR);
		removeUserTO.setLimit(accounts.getLimit());
		removeUserTO.setPageNumber(accounts.getPageNumber());
		removeUserTO.setUserSelection(createUserTO(list));
		
		return removeUserTO;
	}

	private void createApproveUserButton() {
		Button submitButton = new Button("Approve");
		submitButton.setId("approveUserButtonIdAccounts");
		submitButton.setBorders(true);
		submitButton.setEnabled(true);
		
		submitButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				GridSelectionModel<UserGridModel> gridSelectionModel = accounts.getGrid().getSelectionModel();
				if (!gridSelectionModel.getSelectedItems().isEmpty() && validateFields(gridSelectionModel)) {
					approveUser(gridSelectionModel);
					accounts.refresh();
				}
 			}
		});
		formPanel.addButton(submitButton);
	}
	
	protected boolean validateFields(GridSelectionModel<UserGridModel> gridSelectionModel) {
		
		for (UserGridModel user : gridSelectionModel.getSelectedItems()) {
			if(Boolean.FALSE.equals(user.getPending())) {
				MessageBox.alert("User Accounts Error", "<b>" + user.getLogin() + "</b> is already approved.", null);
	        	return false;
			}
		}
		return true;
	}
	
	protected void approveUser(GridSelectionModel<UserGridModel> gridSelectionModel) {
		
		ServiceTO serviceTO = createUserApprovalTO(gridSelectionModel.getSelectedItems());
		OurGridPortalServerUtil.getInstance().execute(serviceTO, new AsyncCallback<UserApprovalResponseTO>() {
				
			public void onSuccess(UserApprovalResponseTO result) {
				MessageBox.info("User Accounts", result.getMessage(), null);
				accounts.updateStore(result.getResult());
			}
			
			public void onFailure(Throwable caught) {
				MessageBox.alert("User Accounts Error", caught.getMessage(), null);
				formPanel.el().unmask();
			}
		});
	}

	private ServiceTO createUserApprovalTO(List<UserGridModel> list) {
		
		UserApprovalTO userApprovalTO = new UserApprovalTO();
		userApprovalTO.setExecutorName(CommonServiceConstants.USER_APPROVAL_EXECUTOR);
		userApprovalTO.setUserSelection(createUserTO(list));
		
		return userApprovalTO;
	}
	
	
	private List<UserTO> createUserTO(List<UserGridModel> list) {
		
		List<UserTO> users = new ArrayList<UserTO>();
		for (UserGridModel userModel : list) {
			
			UserTO userTO = new UserTO();
			userTO.setLogin(userModel.getLogin());
			userTO.setPassword("");
			userTO.setEmail(userModel.getEmail());
			userTO.setPending(userModel.getPending());
			userTO.setStorageSize(userModel.getQuota());
			
			users.add(userTO);
		}
		return users;
	}

	protected void init() {
		
		this.setSize(600, 350);  
		this.setPlain(true);  
		this.setActive(true);
		this.setBodyBorder(true);
		this.setHeaderVisible(false);
		this.setHeading("OurGridPortal Accounts");
		this.setClosable(true);
	}
	
	private void createAccountsForm() {
		formPanel.setFrame(true); 
		formPanel.setBorders(false);
		formPanel.setAutoHeight(true);
		formPanel.setAutoWidth(true);
		formPanel.setHeading("OurGridPortal Accounts");
	}
	
	public void closeWindow() {
		this.setVisible(false);
		this.setEnabled(false);
		OurGridPortal.removeWindow(this);
	}

}