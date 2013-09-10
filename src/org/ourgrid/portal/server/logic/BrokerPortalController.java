package org.ourgrid.portal.server.logic;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.RootLogger;
import org.apache.log4j.xml.DOMConfigurator;
import org.ourgrid.portal.client.OurGridPortalException;
import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.exceptions.OurGridPortalClientException;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.ExecutorManager;
import org.ourgrid.portal.server.logic.gridcommunication.BrokerPortalAsyncApplicationClient;
import org.ourgrid.portal.server.logic.gridcommunication.BrokerPortalContextFactory;
import org.ourgrid.portal.server.logic.gridcommunication.BrokerPortalModel;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;
import org.ourgrid.portal.server.logic.model.DAOManager;
import org.ourgrid.portal.server.logic.util.Encryptor;
import org.ourgrid.portal.server.messages.OurGridPortalServiceMessages;
import org.ourgrid.portal.server.util.OurgridPortalProperties;

import br.edu.ufcg.lsd.commune.container.logging.CommuneLoggerFactory;
import br.edu.ufcg.lsd.commune.context.ModuleContext;
import br.edu.ufcg.lsd.commune.context.PropertiesFileParser;
import br.edu.ufcg.lsd.commune.network.xmpp.CommuneNetworkException;
import br.edu.ufcg.lsd.commune.processor.ProcessorStartException;


public class BrokerPortalController implements OurGridPortalIF {
	
	private static BrokerPortalController instance;
	
	private BrokerPortalAsyncApplicationClient brokerPortalClient;
	
	private ExecutorManager executorManager;
	
	private DAOManager daoManager;
	
	private Logger logger;
	
	private BrokerPortalController() throws CommuneNetworkException, ProcessorStartException,
					 URISyntaxException {
		
		this.daoManager = new DAOManager();
		this.executorManager = new ExecutorManager(this);
		
		initGridCommunication();
		configureAdminAccount();
		startLogger();
	}
	
	private void startLogger() {
		
		DOMConfigurator.configure(CommuneLoggerFactory.class.getResource("/log4j.cfg.xml"));
		this.logger = RootLogger.getLogger(BrokerPortalController.class);
	}
	
	private void configureAdminAccount() {
		
		String adminLogin = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.ADMIN_LOGIN);
		String adminPasswd = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.ADMIN_PASSWORD);
		String adminEmail = OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.ADMIN_EMAIL);
		Integer defaultStorageSize = Integer.parseInt(OurgridPortalProperties.getInstance().getProperty(OurgridPortalProperties.DEFAULT_STORAGE_SIZE));
		
		final boolean NO_PENDING_USER = false;
		
		try {
			adminPasswd = Encryptor.go(adminPasswd);
		} catch (OurGridPortalClientException e) {
			throw new RuntimeException(OurGridPortalServiceMessages.PORTAL_SERVICE_UNAVAIABLE_MSG);
		}
		
		getDAOManager().updateUser(adminLogin, adminPasswd, adminEmail, CommonServiceConstants.ADMIN_PROFILE, NO_PENDING_USER, defaultStorageSize);
	}

	private void initGridCommunication() throws CommuneNetworkException, ProcessorStartException, URISyntaxException {
		
		URL brokerResource = BrokerPortalController.class.getResource(OurGridPortalConstants.BROKER_RESOURCE);
		
		File brokerProperties = new File(brokerResource.toURI());

		ModuleContext brokerClientcontext = new BrokerPortalContextFactory(
				new PropertiesFileParser(brokerProperties.getAbsolutePath())).createContext();
	
		BrokerPortalModel model = new BrokerPortalModel();
		
		brokerPortalClient = new BrokerPortalAsyncApplicationClient(brokerClientcontext, model);
		brokerPortalClient.addJobStatusUpdateListener(new JobStatusHandler(this));
	}

	public static void reset() {
		instance = null;
	}
	
	public synchronized static OurGridPortalIF getInstance() throws OurGridPortalException {
		if (instance == null) {
			try {
				instance = new BrokerPortalController();
			} catch (CommuneNetworkException e) {
				throw new OurGridPortalException(e);
			} catch (ProcessorStartException e) {
				throw new OurGridPortalException(e);
			} catch (URISyntaxException e) {
				throw new OurGridPortalException(e);
			}
		}
		return instance;
	}

	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		return executorManager.execute(serviceTO);
	}

	public DAOManager getDAOManager() {
		return this.daoManager;
	}
	
	public BrokerPortalAsyncApplicationClient getBrokerPortalClient() {
		return brokerPortalClient;
	}

	public Logger getLogger() {
		return logger;
	}
}