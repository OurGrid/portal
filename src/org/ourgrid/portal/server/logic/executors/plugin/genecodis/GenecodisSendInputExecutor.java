package org.ourgrid.portal.server.logic.executors.plugin.genecodis;

import java.util.ArrayList;
import java.util.List;

import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.plugin.genecodis.to.model.GenecodisInputFileTO;
import org.ourgrid.portal.client.plugin.genecodis.to.response.GenecodisSendInputResponseTO;
import org.ourgrid.portal.client.plugin.genecodis.to.service.GenecodisSendInputTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;

public class GenecodisSendInputExecutor extends AbstractExecutor {

	private final int ANALYSIS_TYPE_MIN = 1;
	private final int ANALYSIS_TYPE_MAX = 2;
	
	private final int STATISTICAL_TEST_MIN = 0;
	private final int STATISTICAL_TEST_MAX = 2;
	
	private final int CORRECTION_METHOD_MIN = -1;
	private final int CORRECTION_METHOD_MAX = 1;
	
	private final int GENES_SUPPORT = 3;
	
	private final int SUPPORT_FOR_RANDON = 3;

	public GenecodisSendInputExecutor(OurGridPortalIF portal) {
		super(portal);
	}
	
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		GenecodisSendInputTO genecodisSendInputTO = (GenecodisSendInputTO) serviceTO;
		GenecodisSendInputResponseTO genecodisSendInputResponseTO = new GenecodisSendInputResponseTO();
		
		List<GenecodisInputFileTO> inputsFileTO = compileGenecodisInputsFileTO(genecodisSendInputTO);
		genecodisSendInputResponseTO.setInputFile(inputsFileTO);
		
		return genecodisSendInputResponseTO;
	}
	
	private List<GenecodisInputFileTO> compileGenecodisInputsFileTO(GenecodisSendInputTO genecodisSendInputTO) {
		
		if(genecodisSendInputTO.isAllParameters()){
			return createAllInputs(genecodisSendInputTO);
		}
		return createInput(genecodisSendInputTO);
	}

	private List<GenecodisInputFileTO> createInput(GenecodisSendInputTO genecodisSendInputTO){
		
		List<GenecodisInputFileTO> inputsFileTO = new ArrayList<GenecodisInputFileTO>();
		GenecodisInputFileTO genecodisInputFileTO = new GenecodisInputFileTO();
		
		genecodisInputFileTO.setInputFilename(genecodisSendInputTO.getInputFileName());
		genecodisInputFileTO.setGenesSupport(genecodisSendInputTO.getGenesSupport());
		genecodisInputFileTO.setSupportForRandom(genecodisSendInputTO.getSupportForRandom());
		genecodisInputFileTO.setAnalysisType(genecodisSendInputTO.getAnalysisType());
		genecodisInputFileTO.setReferenceSize(genecodisSendInputTO.getReferenceSize());
		genecodisInputFileTO.setSelectedRefSize(genecodisSendInputTO.getSelectedRefSize());
		genecodisInputFileTO.setStatisticalTest(genecodisSendInputTO.getStatisticalTest());
		genecodisInputFileTO.setCorrectionMethod(genecodisSendInputTO.getCorrectionMethod());
		
		inputsFileTO.add(genecodisInputFileTO);
		return inputsFileTO;
	}

	private List<GenecodisInputFileTO> createAllInputs(GenecodisSendInputTO genecodisSendInputTO) {
		
		Integer referenceSize = genecodisSendInputTO.getReferenceSize();
		Integer selectedRefSize = genecodisSendInputTO.getSelectedRefSize();
		
		List<GenecodisInputFileTO> inputsFileTO = new ArrayList<GenecodisInputFileTO>();
				
		for (int a = ANALYSIS_TYPE_MIN; a <= ANALYSIS_TYPE_MAX; a++) {
			for (int t = STATISTICAL_TEST_MIN; t <= STATISTICAL_TEST_MAX; t++) {
				for (int s = CORRECTION_METHOD_MIN; s <= CORRECTION_METHOD_MAX; s++) {

					GenecodisInputFileTO inputFileTO = new GenecodisInputFileTO();
					
					inputFileTO.setInputFilename(genecodisSendInputTO.getInputFileName());
					inputFileTO.setGenesSupport(GENES_SUPPORT);
					inputFileTO.setAnalysisType(a);
					inputFileTO.setReferenceSize(referenceSize);
					inputFileTO.setSelectedRefSize(selectedRefSize);
					inputFileTO.setSupportForRandom(SUPPORT_FOR_RANDON);
					inputFileTO.setStatisticalTest(t);
					inputFileTO.setCorrectionMethod(s);
					
					inputsFileTO.add(inputFileTO);
				}
			}
		}
		return inputsFileTO;
	}
	
	public void logTransaction(ServiceTO serviceTO) {
		
		getLogger().info("Executor Name: Genecodis Send Input");
	}
}