package org.ourgrid.portal.client.plugin.reservatorios.util;

import java.util.Map;
import java.util.TreeMap;

public class ReservatoriosPropertiesUtil {

	public static Map<Integer,String> getReservatoriosPropertiesSimulationPeriod(){
		
		Map<Integer,String> map = new TreeMap<Integer,String>();
		
		map.put(1,"January");
		map.put(2,"February");
		map.put(3,"March");
		map.put(4,"April");
		map.put(5,"May");
		map.put(6,"June");
		map.put(7,"July");
		map.put(8,"August");
		map.put(9,"September");
		map.put(10,"October");
		map.put(11,"November");
		map.put(12,"December");
				
		return map;
	}

}
