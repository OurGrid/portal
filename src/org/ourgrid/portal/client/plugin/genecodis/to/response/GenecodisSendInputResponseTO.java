package org.ourgrid.portal.client.plugin.genecodis.to.response;

import java.util.List;

import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.plugin.genecodis.to.model.GenecodisInputFileTO;



public class GenecodisSendInputResponseTO extends ResponseTO {

	private static final long serialVersionUID = 2251204728988726972L;

	private List<GenecodisInputFileTO> inputFile;
	
	public void setInputFile(List<GenecodisInputFileTO> input) {
		this.inputFile = input;
	}
	
	public List<GenecodisInputFileTO> getInputFile() {
		return inputFile;
	}
}
