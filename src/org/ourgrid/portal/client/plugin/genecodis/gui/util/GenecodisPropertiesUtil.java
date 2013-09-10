package org.ourgrid.portal.client.plugin.genecodis.gui.util;

import java.util.Map;
import java.util.TreeMap;

public class GenecodisPropertiesUtil {
	
	public static Map<String, Integer> getGenecodisPropertiesAnalysisType(){
		
		Map<String,Integer> map = new TreeMap<String,Integer>();
		
		map.put("CONCURRENCE ANALYSIS",1);
		map.put("SINGULAR ANALYSIS", 2);
		
		return map;
	}

	public static Map<String, Integer> getGenecodisPropertiesStatisticalTestType(){
		
		Map<String,Integer> map = new TreeMap<String,Integer>();
		
		map.put("HIPERGEOMETRIC TEST", 0);
		map.put("CHI SQUARE TEST", 1);
		map.put("BOTH", 2);
		
		return map;
	}
}
