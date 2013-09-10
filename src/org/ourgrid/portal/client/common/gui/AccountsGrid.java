package org.ourgrid.portal.client.common.gui;

import java.util.ArrayList;
import java.util.List;

import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.gui.model.UserGridModel;
import org.ourgrid.portal.client.common.to.model.UserTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.response.UsersAccountsResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.common.to.service.UserQuotaTO;
import org.ourgrid.portal.client.common.to.service.UsersAccountsTO;
import org.ourgrid.portal.client.common.util.OurGridPortalServerUtil;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.grid.RowEditor;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
  
public class AccountsGrid extends LayoutContainer {  
  
	private ContentPanel 		 	  panel;
	private ColumnModel 		      columnModel;
	private GroupingStore<UserGridModel>  store;
	private Grid<UserGridModel> 	 	  grid;
	
	private int pageNumber = 0;
	private int pageSize = 0;
	
	public AccountsGrid() {
		this.setAutoHeight(false);
		this.setAutoWidth(false);
	}
	
  	@Override  
  	protected void onRender(Element parent, int index) {  
	    super.onRender(parent, index);  
	    setLayout(new FlowLayout(10));  
	    
	    createAccountsGridModel();
    	createGridPanel();
    	add(panel);
    	createAccountsStore();
  	}  
  
	protected void createAccountsGridModel() {
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		
	    ColumnConfig column = new ColumnConfig();  
	    column.setId("login");  
	    column.setHeader("Login");  
	    column.setWidth(200);  
	    configs.add(column);  
	  
	    column = new ColumnConfig();  
	    column.setId("email");  
	    column.setHeader("E-mail");  
	    column.setWidth(200); 	
	    configs.add(column);  
	  
	    column = new ColumnConfig();  
	    column.setId("pending");  
	    column.setHeader("Pending");  
	    column.setWidth(50);  
	    configs.add(column);  
	    
	    column = new ColumnConfig();
	    column.setId("quota");
	    column.setHeader("Quota");
	    column.setWidth(50);
	    
	    final NumberField nf = new NumberField();
	    nf.setPropertyEditorType(Integer.class);
	    
	    CellEditor cellEditor = new CellEditor(nf) {
	    	
	    	@Override  
	    	public Object postProcessValue(Object object) {  
	    		if (object == null) {  
	    			return object;  
	    		} else {
	    			GridSelectionModel<UserGridModel> gridSelectionModel = getGrid().getSelectionModel();
					if(!gridSelectionModel.getSelectedItems().isEmpty()){
						submitUserQuota( gridSelectionModel, (Integer) object );
					}
	    		}
	    		return object;  
	    		}
	    	};  
	    	
	    column.setEditor(cellEditor);
	    configs.add(column);
	    
	    this.columnModel = new ColumnModel(configs);
	}
	
	private void submitUserQuota(GridSelectionModel<UserGridModel> gridSelectionModel, Integer quotaValue) {
		UserGridModel userGridModel = gridSelectionModel.getSelectedItem();
		String login = userGridModel.getLogin();
		
		ServiceTO serviceTO = createUserQuotaTO(login, quotaValue);
		OurGridPortalServerUtil.getInstance().execute(serviceTO, new AsyncCallback<ResponseTO>() {
			
			public void onSuccess(ResponseTO result) {}

			public void onFailure(Throwable caught) {
				MessageBox.alert("Users Accounts Error", caught.getMessage(), null);
			}
		});
	}  

	private UserTO createUserTO(String login, Integer quotaValue) {
		
		UserTO userTO = new UserTO();
		userTO.setLogin(login);
		userTO.setStorageSize(quotaValue);
		
		return userTO;
		
	}
	
	private ServiceTO createUserQuotaTO(String login, Integer quotaValue) {
		UserQuotaTO userQuotaTO = new UserQuotaTO();
		userQuotaTO.setExecutorName(CommonServiceConstants.EDIT_USER_QUOTA_EXECUTOR);
		userQuotaTO.setUserSelection(createUserTO(login, quotaValue));
		
		return userQuotaTO;
	}
	
	protected void createAccountsStore() {
		
		ServiceTO serviceTO = createGetUserAccountsTO();
		OurGridPortalServerUtil.getInstance().execute(serviceTO, new AsyncCallback<ResponseTO>() {
			
			public void onSuccess(ResponseTO result) {
				UsersAccountsResponseTO responseTO = (UsersAccountsResponseTO) result;
				insertUsers(responseTO.getResult());
				refresh();
			}

			public void onFailure(Throwable caught) {
				MessageBox.alert("Users Accounts Error", caught.getMessage(), null);
			}
		});
	}
	
	protected void refresh() {
		grid.reconfigure(store, columnModel);
	}

	private ServiceTO createGetUserAccountsTO() {
		
		UsersAccountsTO usersAccountsTO = new UsersAccountsTO();
		usersAccountsTO.setPageNumber(this.pageNumber);
		usersAccountsTO.setLimit(this.pageSize);
		usersAccountsTO.setExecutorName(CommonServiceConstants.USERS_ACCOUNTS_EXECUTOR);
		
		return usersAccountsTO;
	}
	
	private void insertUsers(List<UserTO> list) {
		createListStore(list);
	}
	
	private void createListStore(List<UserTO> list) {
		this.store = new GroupingStore<UserGridModel>(); 
		for (UserTO userTO : list) {
			UserGridModel userModel = new UserGridModel(userTO.getLogin(), userTO.getEmail(), userTO.getPending(), userTO.getStorageSize());
			this.store.add(userModel);
		}
	    store.groupBy("pending");
	}
	
	protected void createGridPanel() {
		
	    panel = new ContentPanel();  
	    panel.setBodyBorder(false);  
	    panel.setButtonAlign(HorizontalAlignment.CENTER);  
	    panel.setLayout(new FitLayout());  
	    panel.setSize(530, 230); 
	    panel.setHeaderVisible(false);
	    
	    this.store = new GroupingStore<UserGridModel>();  
	    
	    GroupingView view = new GroupingView();  
	    view.setShowGroupedColumn(false);  
	    view.setForceFit(true);  
	    view.setGroupRenderer(new GridGroupRenderer() {  
	      public String render(GroupColumnData data) {  
	        String l = data.models.size() == 1 ? "User" : "Users";  
	        String groupName = data.group.equals("false") ? "Approved" : "Pending";
	        
	        return groupName + " (" + data.models.size() + " " + l + ")";  
	      }  
	    });
		
	    RowEditor<UserGridModel> rowEditor = new RowEditor<UserGridModel>();
	    
		grid = new Grid<UserGridModel>(this.store, this.columnModel);
	    grid.setStyleAttribute("borderTop", "none");  		
	    grid.setBorders(true);  
	    grid.setStripeRows(true);
	    grid.setView(view);
	    grid.addPlugin(rowEditor);
	    
	    panel.add(grid);  
	}
	
	public void updateStore(List<UserTO> list) {
		for (UserTO userTO : list) {
			
			for (UserGridModel model : this.store.getModels()) {
				
				if (userTO.getLogin().equals(model.getLogin())) {
					model.setEmail(userTO.getEmail());
					model.setPending(userTO.getPending());
					model.setQuota(userTO.getStorageSize());
				}
			}
		}
		
		refresh();
	}

	public void replaceStore(List<UserTO> list) {
		insertUsers(list);
		refresh();	
	}

	public Grid<UserGridModel> getGrid() {
		return this.grid;
	}

	public int getPageNumber() {
		return this.pageNumber;
	}

	public int getLimit() {
		return this.pageSize;
	}
  
} 
