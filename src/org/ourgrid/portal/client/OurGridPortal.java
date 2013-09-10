package org.ourgrid.portal.client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.gui.AccountsWindow;
import org.ourgrid.portal.client.common.gui.FileExplorerWindow;
import org.ourgrid.portal.client.common.gui.LoginWindow;
import org.ourgrid.portal.client.common.gui.SettingsWindow;
import org.ourgrid.portal.client.common.gui.SubmitJobsWindow;
import org.ourgrid.portal.client.common.gui.UploadProxyCertificateWindow;
import org.ourgrid.portal.client.common.gui.model.UserModel;
import org.ourgrid.portal.client.common.to.model.AbstractRequest;
import org.ourgrid.portal.client.common.to.model.FileTO;
import org.ourgrid.portal.client.common.to.model.UserTO;
import org.ourgrid.portal.client.common.to.response.CheckProxyResponseTO;
import org.ourgrid.portal.client.common.to.response.DeleteFileResponseTO;
import org.ourgrid.portal.client.common.to.response.FileExplorerResponseTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.CheckProxyTO;
import org.ourgrid.portal.client.common.to.service.DeleteFileTO;
import org.ourgrid.portal.client.common.to.service.FileExplorerTO;
import org.ourgrid.portal.client.common.to.service.ForcedCancelJobTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.common.util.FileExplorerUtil;
import org.ourgrid.portal.client.common.util.OurGridPortalServerUtil;
import org.ourgrid.portal.client.plugin.autodock.gui.SubmitAutodockJobsWindow;
import org.ourgrid.portal.client.plugin.blender.gui.SubmitBlenderJobsWindow;
import org.ourgrid.portal.client.plugin.cisternas.gui.CisternasSubmitJobsWindow;
import org.ourgrid.portal.client.plugin.epanetgrid.gui.EpanetSubmitJobsWindow;
import org.ourgrid.portal.client.plugin.fibonacci.gui.FibonacciSubmitJobsWindow;
import org.ourgrid.portal.client.plugin.genecodis.gui.SubmitGenecodisJobsWindow;
import org.ourgrid.portal.client.plugin.gui.PluginSubmitJobsWindow;
import org.ourgrid.portal.client.plugin.lvc.gui.SubmitLvcJobsWindow;
import org.ourgrid.portal.client.plugin.marbs.gui.MarbsSubmitJobsWindow;
import org.ourgrid.portal.client.plugin.reservatorios.gui.ReservatoriosSubmitJobsWindow;
import org.ourgrid.portal.client.plugin.rho.gui.SubmitRhoJobsWindow;

import com.extjs.gxt.desktop.client.Desktop;
import com.extjs.gxt.desktop.client.Shortcut;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class OurGridPortal implements EntryPoint {

	public final static String VERSION = "2.2";

	private static LoginWindow      			 loginWindow;
	private static SettingsWindow   			 settingsWindow;
	private static AccountsWindow   			 accountsWindow;
	private static SubmitJobsWindow 			 submitJobsWindow;
	private static SubmitAutodockJobsWindow 	 submitAutodockJobsWindow;
	private static SubmitBlenderJobsWindow 		 submitBlenderJobsWindow;
	private static SubmitGenecodisJobsWindow 	 submitGenecodisJobsWindow;
	private static SubmitRhoJobsWindow 			 submitRhoJobsWindow;
	private static FibonacciSubmitJobsWindow 	 submitFibonacciJobsWindow;
	private static ReservatoriosSubmitJobsWindow submitReservatoriosJobsWindow; 
	private static EpanetSubmitJobsWindow		 submitEpanetJobsWindow;
	private static CisternasSubmitJobsWindow	 submitCisternasJobsWindow;
	private static MarbsSubmitJobsWindow		 submitMarbsJobsWindow;
	private static FileExplorerWindow 			 fileExplorerWindow;
	private static UploadProxyCertificateWindow  uploadCertificateWindow;
	private static SubmitLvcJobsWindow			 submitLvcJobsWindow;

	private static MenuItem 					 accounts;
	private static MenuItem 					 submitJobs;
	private static MenuItem 					 fileExplorer;
	private static MenuItem						 uploadCertificate;
	private static MenuItem						 removeCertificate;
	private static MenuItem 				 	 checkCertificate;
	private static MenuItem 				 	 submitOurGridJobs;

	private static UserModel 					 userModel;

	private static List<PluginSubmitJobsWindow>  pluginList;

	private static boolean 						 isFirstQuotaNotification;

	private static Desktop 						 desktop;



	public void onModuleLoad() {
		init();
	}

	private void init() {

		isFirstQuotaNotification = true;

		pluginList = new ArrayList<PluginSubmitJobsWindow>();
		loginWindow = new LoginWindow(this);
		loginWindow.show();
	}



	public static void notifyQuota(String quotaExceededValue){
		if(isFirstQuotaNotification){
			MessageBox.alert("Disk quota exceeded", "Your account exceed " + quotaExceededValue, null);
			isFirstQuotaNotification = false;
		}
	}

	public static void setFristQuotaNotification(boolean isFristQN) {
		isFirstQuotaNotification = isFristQN;
	}

	private void createFileExplorerShortcut() {
		AbstractImagePrototype icon = IconHelper.createPath("resources/images/silk/fileExplorer.gif");

		Shortcut fileExplorerShortcut = new Shortcut("fileExplorerShortcutId", "File Explorer");
		fileExplorerShortcut.setBorders(false);
		fileExplorerShortcut.setEnabled(true);
		fileExplorerShortcut.setIcon(icon);
		fileExplorerShortcut.addSelectionListener(new SelectionListener<ComponentEvent>() {
			@Override
			public void componentSelected(ComponentEvent ce) {
				getDesktop().getStartMenu().setVisible(false);
				getDesktop().addWindow(getFileExplorerWindow());
				getDesktop().minimizeWindow(getFileExplorerWindow());
				getFileExplorerWindow().show();
				getFileExplorerWindow().expandRoot();
				getFileExplorerWindow().updateFileInfoBar();
			}
		});

		getDesktop().addShortcut(fileExplorerShortcut);
	}

	private void createPluginShortcut(final PluginSubmitJobsWindow window, String shortcutName, String shortcutID, String imagePath) {
		AbstractImagePrototype icon = IconHelper.createPath(imagePath);

		Shortcut shortcut = new Shortcut(shortcutID, shortcutName);
		shortcut.setBorders(false);
		shortcut.setEnabled(true);
		shortcut.setIcon(icon);
		shortcut.addSelectionListener(new SelectionListener<ComponentEvent>() {
			@Override
			public void componentSelected(ComponentEvent ce) {
				getDesktop().getStartMenu().setVisible(false);
				getDesktop().addWindow(window);
				getDesktop().minimizeWindow(window);
				window.show();
			}
		});

		getDesktop().addShortcut(shortcut);
	}

	public static void activateSettingsMenu(UserTO user){
		settingsWindow = new SettingsWindow(user);
		getDesktop().getStartMenu().setHeading(user.getLogin());
	}

	public void activateLogoutMenu(){
		loginWindow = new LoginWindow(this);
	}

	public static void activateAccountsMenu(){
		accountsWindow = new AccountsWindow();
		accounts.setVisible(true);
		accounts.setEnabled(true);
	}

	private static void activateJobSubmissionMenu(List<AbstractRequest> jobsList) {
		submitJobsWindow = new SubmitJobsWindow(jobsList);
		submitJobsWindow.show();
		submitJobsWindow.setVisible(false);
		submitJobsWindow.configureTabs();
	}

	public static void createAndAddJobStatusTab(int jobViewId){
		submitJobsWindow.createAndAddJobStatusTab(jobViewId);
		getDesktop().getStartMenu().setVisible(false);
		getDesktop().addWindow(getSubmitJobsWindow());
		getSubmitJobsWindow().show();
		getDesktop().minimizeWindow(getSubmitJobsWindow());


	}

	private static void activatePluginsJobSubmissionMenu() {
		for (PluginSubmitJobsWindow plugin : pluginList) {
			plugin.show();
			plugin.setVisible(false);
			plugin.configureTabs();
		}
	}

	private static void activateFileExplorerMenu() {
		createFileExplorerWindow("/" + userModel.getUserLogin() + "/" ,userModel.getUserLogin());
	}

	private static void createFileExplorerWindow(String location, String nodeName) {

		ServiceTO serviceTO = createFileExplorerTO(location, nodeName, true);
		OurGridPortalServerUtil.getInstance().execute(serviceTO, new AsyncCallback<ResponseTO>() {

			public void onSuccess(ResponseTO result) {
				FileExplorerResponseTO fileExplorerResponse = (FileExplorerResponseTO) result;
				fileExplorerWindow = new FileExplorerWindow(fileExplorerResponse.getFileRoot(), fileExplorerResponse.getQuotaPercentage());
				if(fileExplorerResponse.checkQuota()){
					if(fileExplorerResponse.isQuotaExed() ){
						notifyQuota(fileExplorerResponse.getQuotaExceeded());
						desactivateSubmission();
						desactivateQuotaOperations();
						cancelJobs(fileExplorerResponse.getJobsRequestList());
					}else{
						setFristQuotaNotification(true);
						activateSubmission();
						activateQuotaOperations();
					}
				}
			}

			public void onFailure(Throwable caught) {
				fileExplorer.disable();
				fileExplorer.isVisible(false);
				MessageBox.alert("File Explorer Error", caught.getMessage(), null);
			}

		});
	}
	
	private static void createUploadProxyCertificateWindow() {
		uploadCertificateWindow = new UploadProxyCertificateWindow();
	}

	public static void activateSubmission() {
		submitJobsWindow.activateSubmission();
		for (PluginSubmitJobsWindow plugin : pluginList) {
			plugin.activateSubmission();
		}
	}

	public static void desactivateSubmission() {
		submitJobsWindow.desactivateSubmission();
		for (PluginSubmitJobsWindow plugin : pluginList) {
			plugin.desactivateSubmission();
		}
	}

	public static void cancelJobs(List<AbstractRequest> jobs){

		ForcedCancelJobTO forcedCancelJobTO = createForcedCancelJobTO(jobs);
		OurGridPortalServerUtil.getInstance().execute(forcedCancelJobTO, new AsyncCallback<ResponseTO>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(ResponseTO result) {
			}

		});
	}

	private static ForcedCancelJobTO createForcedCancelJobTO(List<AbstractRequest> jobs) {

		ForcedCancelJobTO forcedCancelJobTO = new ForcedCancelJobTO();
		forcedCancelJobTO.setExecutorName(CommonServiceConstants.FORCED_CANCEL_JOB_EXECUTOR);
		forcedCancelJobTO.setJobsRequestList(jobs);
		return forcedCancelJobTO;
	}


	public static void activateQuotaOperations() {
		fileExplorerWindow.activateQuotaOperations();
	}

	public static void desactivateQuotaOperations() {
		fileExplorerWindow.desactivateQuotaOperations();
	}

	private void createSettingsMenu(){

		AbstractImagePrototype icon = IconHelper.createPath("resources/images/silk/settings.gif");  

		MenuItem settings = new MenuItem("Settings");
		settings.setBorders(false);
		settings.setEnabled(true);
		settings.setIcon(icon);
		settings.setId("settingsItem");

		settings.addSelectionListener(new SelectionListener<MenuEvent>() {
			public void componentSelected(MenuEvent bt) {
				getDesktop().getStartMenu().setVisible(false);
				getDesktop().addWindow(getSettingsWindow());
				getDesktop().minimizeWindow(getSettingsWindow());
				getSettingsWindow().show();
			}
		});
		getDesktop().getStartMenu().addTool(settings);
	}

	private void createAccountsMenu(){

		AbstractImagePrototype icon = IconHelper.createPath("resources/images/silk/accounts.gif");  

		accounts = new MenuItem("Accounts");
		accounts.setIcon(icon);
		accounts.setBorders(false);
		accounts.setEnabled(false);
		accounts.setVisible(false);
		accounts.setId("accountsItem");

		accounts.addSelectionListener(new SelectionListener<MenuEvent>() {
			public void componentSelected(MenuEvent bt) {
				getDesktop().getStartMenu().setVisible(false);
				getDesktop().addWindow(getAccountsWindow());
				getDesktop().minimizeWindow(getAccountsWindow());
				getAccountsWindow().show();
			}
		});
		getDesktop().getStartMenu().addTool(accounts);
	}

	private void createLogoutMenu() {

		AbstractImagePrototype icon = IconHelper.createPath("resources/images/silk/logout.gif");  

		MenuItem logout = new MenuItem("Logout");
		logout.setIcon(icon);
		logout.setBorders(false);
		logout.setEnabled(true);
		logout.setId("logoutItem");

		logout.addSelectionListener(new SelectionListener<MenuEvent>() {
			public void componentSelected(MenuEvent bt) {
				Location.reload();
			}
		});

		getDesktop().getStartMenu().addToolSeperator();
		getDesktop().getStartMenu().addTool(logout);
	}

	private void createSubmitJobsMenu() {
		AbstractImagePrototype submitJobIcon = IconHelper.createPath("resources/images/silk/ourgrid.png");

		submitOurGridJobs = new MenuItem("Submit OurGrid Job");
		submitOurGridJobs.setBorders(true);
		submitOurGridJobs.setEnabled(true);
		submitOurGridJobs.setIcon(submitJobIcon);
		submitOurGridJobs.setId("submitJobItem");

		submitOurGridJobs.addSelectionListener(new SelectionListener<MenuEvent>() {
			public void componentSelected(MenuEvent ce) {
				getDesktop().getStartMenu().setVisible(false);
				getDesktop().addWindow(getSubmitJobsWindow());
				getDesktop().minimizeWindow(getSubmitJobsWindow());
				getSubmitJobsWindow().show();
			}
		});
		getDesktop().getStartMenu().add(submitOurGridJobs);
	}

	private void createPluginSubmitJobsMenu(final PluginSubmitJobsWindow window, String menuName, String menuID, String imagePath) {

		AbstractImagePrototype submitJobIcon = IconHelper.createPath(imagePath);

		submitJobs = new MenuItem(menuName);
		submitJobs.setBorders(true);
		submitJobs.setEnabled(true);
		submitJobs.setIcon(submitJobIcon);
		submitJobs.setId(menuID);

		submitJobs.addSelectionListener(new SelectionListener<MenuEvent>() {
			public void componentSelected(MenuEvent ce) {
				getDesktop().getStartMenu().setVisible(false);
				getDesktop().addWindow(window);
				getDesktop().minimizeWindow(window);
				window.show();
			}
		});
		getDesktop().getStartMenu().add(submitJobs);		
	}

	private void createFileExplorerMenu() {
		AbstractImagePrototype submitJobIcon = IconHelper.createPath("resources/images/silk/fileExplorer.gif");

		fileExplorer = new MenuItem("File Explorer");
		fileExplorer.setBorders(false);
		fileExplorer.setEnabled(true);
		fileExplorer.setIcon(submitJobIcon);
		fileExplorer.setId("fileExplorerItem");

		fileExplorer.addSelectionListener(new SelectionListener<MenuEvent>() {
			public void componentSelected(MenuEvent ce) {
				getDesktop().getStartMenu().setVisible(false);
				getDesktop().addWindow(getFileExplorerWindow());
				getDesktop().minimizeWindow(getFileExplorerWindow());
				getFileExplorerWindow().show();
				getFileExplorerWindow().expandRoot();
				getFileExplorerWindow().updateFileInfoBar();
			}
		});
		getDesktop().getStartMenu().addTool(fileExplorer);
	}

	private void createUploadCertificateMenu() {
		AbstractImagePrototype submitJobIcon = IconHelper.createPath("resources/images/silk/upload.gif");

		uploadCertificate = new MenuItem("Upload Proxy");
		uploadCertificate.setBorders(false);
		uploadCertificate.setEnabled(true);
		uploadCertificate.setIcon(submitJobIcon);
		uploadCertificate.setId("uploadProxyItem");

		uploadCertificate.addSelectionListener(new SelectionListener<MenuEvent>() {
			public void componentSelected(MenuEvent ce) {
				getDesktop().getStartMenu().setVisible(false);
				getDesktop().addWindow(getUploadCertificateWindow());
				getDesktop().minimizeWindow(getUploadCertificateWindow());
				getUploadCertificateWindow().show();
			}
		});
		getDesktop().getStartMenu().addTool(uploadCertificate);
	}

	private void createRemoveCertificateMenu() {
		AbstractImagePrototype submitJobIcon = IconHelper.createPath("resources/images/silk/delete.gif");

		removeCertificate = new MenuItem("Remove Proxy");
		removeCertificate.setBorders(false);
		removeCertificate.setEnabled(true);
		removeCertificate.setIcon(submitJobIcon);
		removeCertificate.setId("removeProxyItem");

		removeCertificate.addSelectionListener(new SelectionListener<MenuEvent>() {
			public void componentSelected(MenuEvent ce) {
				submitProxyFile();
			}
		});
		getDesktop().getStartMenu().addTool(removeCertificate);
	}
	
	private void createCheckCertificateMenu() {
		AbstractImagePrototype checkProxyIcon = IconHelper.createPath("resources/images/silk/fileExplorer.gif");

		checkCertificate = new MenuItem("Check Proxy");
		checkCertificate.setBorders(false);
		checkCertificate.setEnabled(true);
		checkCertificate.setIcon(checkProxyIcon);
		checkCertificate.setId("checkProxyItem");

		checkCertificate.addSelectionListener(new SelectionListener<MenuEvent>() {
			public void componentSelected(MenuEvent ce) {
				checkProxyFile();
			}
		});
		getDesktop().getStartMenu().addTool(checkCertificate);
	}
	
	private static ServiceTO createFileExplorerTO(String location, String nodeName, boolean checkQuota) {

		FileExplorerTO fileExplorerTO = new FileExplorerTO();
		fileExplorerTO.setExecutorName(CommonServiceConstants.FILE_EXPLORER_EXECUTOR);
		fileExplorerTO.setLocation(location);
		fileExplorerTO.setNodeName(nodeName);
		fileExplorerTO.setUserName(userModel.getUserLogin());
		fileExplorerTO.setCheckQuota(checkQuota);

		return fileExplorerTO;
	}

	public static void removeWindow(Window window) {
		getDesktop().removeWindow(window);
	}

	private static void createUserModel(String login) {
		userModel = new UserModel(login);
	}

	public void desktopConfig(UserTO currentUser, List<AbstractRequest> jobs) {

		desktop = new Desktop();

		createUserModel(currentUser.getLogin());
		createAccountsMenu();
		createSettingsMenu();
		createFileExplorerMenu();
		createUploadCertificateMenu();
		createRemoveCertificateMenu();
		createCheckCertificateMenu();
		createLogoutMenu();
		createSubmitJobsMenu();
		createFileExplorerShortcut();
		createSubmitJobShortcut();


		setPluginsJobWindow(jobs);

		addToList(getSubmitAutodockJobsWindow());
		addToList(getSubmitBlenderJobsWindow());
		addToList(getSubmitGenecodisJobsWindow());
		addToList(getSubmitRhoJobsWindow());
		addToList(getSubmitReservatoriosJobsWindow());
		addToList(getSubmitEpanetJobsWindow());
		addToList(getSubmitCisternasJobsWindow());
		addToList(getSubmitMarbsJobsWindow());
		addToList(getSubmitFibonacciJobsWindow());
		addToList(getSubmitLvcJobsWindow());

		createPluginMenuItens();
		createPluginsShortcuts();

		activateSettingsMenu(currentUser);
		activateFileExplorerMenu();
		createUploadProxyCertificateWindow();
		activateLogoutMenu();
		activateJobSubmissionMenu(filterJobs(jobs, CommonServiceConstants.SIMPLE_OURGRID_JOB));

		activatePluginsJobSubmissionMenu();

		if (CommonServiceConstants.ADMIN_PROFILE.equals(currentUser.getProfile())){
			OurGridPortal.activateAccountsMenu();
		}
	}

	private void setPluginsJobWindow(List<AbstractRequest> jobs) {
		setSubmitAutodockJobsWindow(new SubmitAutodockJobsWindow(filterJobs(jobs, CommonServiceConstants.AUTODOCK_JOB)));
		setSubmitBlenderJobsWindow(new SubmitBlenderJobsWindow(filterJobs(jobs, CommonServiceConstants.BLENDER_JOB)));
		setSubmitRhoJobsWindow(new SubmitRhoJobsWindow(filterJobs(jobs, CommonServiceConstants.RHO_JOB)));
		setSubmitGenecodisJobsWindow(new SubmitGenecodisJobsWindow(filterJobs(jobs, CommonServiceConstants.GENECODIS_JOB)));
		setSubmitReservatoriosJobsWindow(new ReservatoriosSubmitJobsWindow(filterJobs(jobs, CommonServiceConstants.RESERVATORIOS_JOB)));
		setSubmitEpanetJobsWindow(new EpanetSubmitJobsWindow(filterJobs(jobs, CommonServiceConstants.EPANET_JOB)));
		setSubmitCisternasJobsWindow(new CisternasSubmitJobsWindow(filterJobs(jobs, CommonServiceConstants.CISTERNAS_JOB)));
		setSubmitMarbsJobsWindow(new MarbsSubmitJobsWindow(filterJobs(jobs, CommonServiceConstants.MARBS_JOB)));
		setSubmitFibonacciJobsWindow(new FibonacciSubmitJobsWindow(filterJobs(jobs, CommonServiceConstants.FIBONACCI_JOB)));
		setSubmitLvcJobsWindow(new SubmitLvcJobsWindow(filterJobs(jobs, CommonServiceConstants.LVC_JOB)));
	}

	private void createPluginsShortcuts() {
		createPluginShortcut(getSubmitAutodockJobsWindow(), "Autodock", "autodockShortcutID","resources/images/silk/autodock.gif");
		createPluginShortcut(getSubmitGenecodisJobsWindow(), "Genecodis", "genecodisShortcutID","resources/images/silk/genecodis.gif");
		createPluginShortcut(getSubmitBlenderJobsWindow(), "Blender", "blenderShortcutID","resources/images/silk/blender.gif");
		createPluginShortcut(getSubmitRhoJobsWindow(), "SLinCA", "SLinCAShortcutID","resources/images/silk/fileExplorer.gif");
		createPluginShortcut(getSubmitReservatoriosJobsWindow(), "Reservoir", "reservoirShortcusID", "resources/images/silk/reservatorios.gif");
		createPluginShortcut(getSubmitEpanetJobsWindow(),"EpanetGrid", "epanetShortcutID", "resources/images/silk/epanet.gif");
		createPluginShortcut(getSubmitCisternasJobsWindow(), "Cisterns", "cisternShortcutID", "resources/images/silk/fileExplorer.gif");
		createPluginShortcut(getSubmitMarbsJobsWindow(),"Marbs", "marbsShortcutID", "resources/images/silk/marbs.gif");
		createPluginShortcut(getSubmitFibonacciJobsWindow(), "Fibonacci", "fibonacciShortcutID", "resources/images/silk/fileExplorer.gif");
		createPluginShortcut(getSubmitLvcJobsWindow(), "LVC", "LvcShortcutID", "resources/images/silk/fileExplorer.gif");
	}

	private void createPluginMenuItens() {
		createPluginSubmitJobsMenu(getSubmitAutodockJobsWindow(), "Submit Autodock Job", "submitAutodockJobID", "resources/images/silk/autodock.gif");
		createPluginSubmitJobsMenu(getSubmitGenecodisJobsWindow(), "Submit Genecodis Job", "submitGenecodisJobID", "resources/images/silk/genecodis.gif");
		createPluginSubmitJobsMenu(getSubmitBlenderJobsWindow(), "Submit Blender Job", "submitBlenderJobID", "resources/images/silk/blender.gif");
		createPluginSubmitJobsMenu(getSubmitRhoJobsWindow(), "Submit SLinCA Job", "submitSLinCAJobID", "resources/images/silk/fileExplorer.gif");
		createPluginSubmitJobsMenu(getSubmitReservatoriosJobsWindow(), "Submit Reservoir Job", "submitReservoirJobID",  "resources/images/silk/reservatorios.gif");
		createPluginSubmitJobsMenu(getSubmitEpanetJobsWindow(), "Submit EpanetGrid Job", "submitEpanetJobID", "resources/images/silk/epanet.gif");
		createPluginSubmitJobsMenu(getSubmitCisternasJobsWindow(), "Submit Cisterns Job", "submitCisternsJobID", "resources/images/silk/fileExplorer.gif");
		createPluginSubmitJobsMenu(getSubmitMarbsJobsWindow(), "Submit Marbs Job", "submitMarbsJobID", "resources/images/silk/marbs.gif");
		createPluginSubmitJobsMenu(getSubmitFibonacciJobsWindow(), "Submit Fibonacci Job", "submitFibonacciJobID", "resources/images/silk/fileExplorer.gif");
		createPluginSubmitJobsMenu(getSubmitLvcJobsWindow(), "Submit LVC Job", "submitLvcJobID", "resources/images/silk/fileExplorer.gif");
	}

	private void addToList(PluginSubmitJobsWindow window) {
		pluginList.add(window);
	}

	private void createSubmitJobShortcut() {

		AbstractImagePrototype icon = IconHelper.createPath("resources/images/silk/ourgrid.png");

		Shortcut submitOurGridJobShortcut = new Shortcut("SubmitJobShortcut", "OurGrid");
		submitOurGridJobShortcut.setBorders(false);
		submitOurGridJobShortcut.setEnabled(true);
		submitOurGridJobShortcut.setIcon(icon);
		submitOurGridJobShortcut.addSelectionListener(new SelectionListener<ComponentEvent>() {
			@Override
			public void componentSelected(ComponentEvent ce) {
				getDesktop().getStartMenu().setVisible(false);
				getDesktop().addWindow(getSubmitJobsWindow());
				getDesktop().minimizeWindow(getSubmitJobsWindow());
				getSubmitJobsWindow().show();
			}
		});

		getDesktop().addShortcut(submitOurGridJobShortcut);		
	}

	private static List<AbstractRequest> filterJobs(List<AbstractRequest> jobs, String type) {
		List<AbstractRequest> jobsList = new LinkedList<AbstractRequest>();

		for (AbstractRequest job : jobs) {

			if (job.getJobType().equals(type)) {
				jobsList.add(job);
			}
		}

		return jobsList;
	}

	public static UserModel getUserModel() {
		return userModel;
	}

	public static Desktop getDesktop() {
		return desktop;
	}

	public static SubmitJobsWindow getSubmitJobsWindow() {
		return submitJobsWindow;
	}

	public static SubmitAutodockJobsWindow getSubmitAutodockJobsWindow() {
		return submitAutodockJobsWindow;
	}

	public static SubmitBlenderJobsWindow getSubmitBlenderJobsWindow() {
		return submitBlenderJobsWindow;
	}

	public static SubmitRhoJobsWindow getSubmitRhoJobsWindow() {
		return submitRhoJobsWindow;
	}

	public static SettingsWindow getSettingsWindow() {
		return settingsWindow;
	}

	public static AccountsWindow getAccountsWindow() {
		return accountsWindow;
	}

	public static LoginWindow getLoginWindow() {
		return loginWindow;
	}

	public static void setSubmitAutodockJobsWindow(
			SubmitAutodockJobsWindow submitAutodockJobsWindow) {
		OurGridPortal.submitAutodockJobsWindow = submitAutodockJobsWindow;
	}

	public static void setSubmitBlenderJobsWindow(
			SubmitBlenderJobsWindow submitBlenderJobsWindow) {
		OurGridPortal.submitBlenderJobsWindow = submitBlenderJobsWindow;
	}

	public static void setSubmitGenecodisJobsWindow(
			SubmitGenecodisJobsWindow submitGenecodisJobsWindow) {
		OurGridPortal.submitGenecodisJobsWindow = submitGenecodisJobsWindow;
	}

	public static void setSubmitRhoJobsWindow(
			SubmitRhoJobsWindow submitRhoJobsWindow) {
		OurGridPortal.submitRhoJobsWindow = submitRhoJobsWindow;
	}

	public static SubmitGenecodisJobsWindow getSubmitGenecodisJobsWindow() {
		return submitGenecodisJobsWindow;
	}

	public static ReservatoriosSubmitJobsWindow getSubmitReservatoriosJobsWindow() {
		return submitReservatoriosJobsWindow;
	}

	public static EpanetSubmitJobsWindow getSubmitEpanetJobsWindow() {
		return submitEpanetJobsWindow;
	}

	private PluginSubmitJobsWindow getSubmitMarbsJobsWindow() {
		return submitMarbsJobsWindow;
	}
	
	public static SubmitLvcJobsWindow getSubmitLvcJobsWindow() {
		return submitLvcJobsWindow;
	}

	public static void setSubmitLvcJobsWindow(
			SubmitLvcJobsWindow submitLvcJobsWindow) {
		OurGridPortal.submitLvcJobsWindow = submitLvcJobsWindow;
	}

	public static FibonacciSubmitJobsWindow getSubmitFibonacciJobsWindow() {
		return submitFibonacciJobsWindow;
	}

	public static void setSubmitFibonacciJobsWindow(
			FibonacciSubmitJobsWindow submitFibonacciJobsWindow) {
		OurGridPortal.submitFibonacciJobsWindow = submitFibonacciJobsWindow;
	}

	public static void setSubmitEpanetJobsWindow(
			EpanetSubmitJobsWindow submitEpanetJobsWindow) {
		OurGridPortal.submitEpanetJobsWindow = submitEpanetJobsWindow;
	}

	public static void setSubmitMarbsJobsWindow(
			MarbsSubmitJobsWindow submitMarbsJobsWindow) {
		OurGridPortal.submitMarbsJobsWindow = submitMarbsJobsWindow;
	}

	public static void setSubmitReservatoriosJobsWindow(ReservatoriosSubmitJobsWindow submitReservatoriosJobsWindow) {
		OurGridPortal.submitReservatoriosJobsWindow = submitReservatoriosJobsWindow;
	}

	public static CisternasSubmitJobsWindow getSubmitCisternasJobsWindow() {
		return submitCisternasJobsWindow;
	}

	public static void setSubmitCisternasJobsWindow(
			CisternasSubmitJobsWindow submitCisternasJobsWindow) {
		OurGridPortal.submitCisternasJobsWindow = submitCisternasJobsWindow;
	}

	public static FileExplorerWindow getFileExplorerWindow() {
		return fileExplorerWindow;
	}

	public static void refreshFileExplorer() {
		getFileExplorerWindow().refreshFileExplorer();
	}

	public static void refreshFileExplorerRoot() {
		getFileExplorerWindow().refreshRoot();
	}

	public static void setUploadCertificateWindow(
			UploadProxyCertificateWindow uploadCertificateWindow) {
		OurGridPortal.uploadCertificateWindow = uploadCertificateWindow;
	}

	public static UploadProxyCertificateWindow getUploadCertificateWindow() {
		return uploadCertificateWindow;
	}
	
	protected void submitProxyFile() {
		
		final UserModel userModel = OurGridPortal.getUserModel();
		
		
		ServiceTO serviceTO = createFileExplorerTO("/" + userModel.getUserLogin() + "/", 
				userModel.getUserLogin(), true);
		OurGridPortalServerUtil.getInstance().execute(serviceTO, new AsyncCallback<ResponseTO>() {

			public void onSuccess(ResponseTO result) {
				FileExplorerResponseTO fileExplorerResponse = (FileExplorerResponseTO) result;
				FileTO fileRoot = fileExplorerResponse.getFileRoot();
				List<FileTO> proxyList = new LinkedList<FileTO>();
				proxyList.add(new FileTO(".usercert"));
				
				DeleteFileTO deleteFileTO = FileExplorerUtil.createDeleteFileTO(
						proxyList, 
						fileRoot, 
						userModel.getUserLogin());
				
				OurGridPortalServerUtil.getInstance().execute(deleteFileTO, new AsyncCallback<DeleteFileResponseTO>() {

					public void onFailure(Throwable caught) {
						MessageBox.alert("Delete File Error", caught.getMessage(), null);
					}

					@Override
					public void onSuccess(DeleteFileResponseTO result) {
						MessageBox.alert("Delete File", "Your proxy has been deleted.", null);
					}

				});
			}

			public void onFailure(Throwable caught) {
				MessageBox.alert("Delete File Error", caught.getMessage(), null);
			}

		});
		
		
		
	}
	
	protected void checkProxyFile() {
		
		UserModel userModel = OurGridPortal.getUserModel();
		
		CheckProxyTO checkProxyTO = new CheckProxyTO();
		checkProxyTO.setUserName(userModel.getUserLogin());
		
		OurGridPortalServerUtil.getInstance().execute(checkProxyTO, new AsyncCallback<CheckProxyResponseTO>() {

			public void onFailure(Throwable caught) {
				MessageBox.alert("Check Proxy Error", caught.getMessage(), null);
			}

			@Override
			public void onSuccess(CheckProxyResponseTO result) {
				String message = null;
				if (!result.getProxyExists()) {
					message = "You do not have a proxy file";
				} else {
					if (result.getTimeLeft() > 0) {
						message = "Your proxy is valid for more " + (result.getTimeLeft() / 60) + " minutes.";
					} else {
						message = "Your proxy is not valid.";
					}
				}
				
				MessageBox.alert("Proxy check", message, null);
			}

		});
	}
	
}