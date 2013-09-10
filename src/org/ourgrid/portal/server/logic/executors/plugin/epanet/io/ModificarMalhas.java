package org.ourgrid.portal.server.logic.executors.plugin.epanet.io;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.epanetgrid.data.EpaFileReader;
import org.epanetgrid.data.EpaFileWriter;
import org.epanetgrid.model.epanetNetWork.NetWork;

public class ModificarMalhas {
	
	String path;
	String nome;
	
	public ModificarMalhas(String path, String nome) {
		this.path = path;
		this.nome = nome;
	}
	
	public void modificar() throws IOException {
		String filePath = "/resources/NoDonutForYou";
		EpaFileReader fileReader = new EpaFileReader();
		
		try {
			fileReader.read(filePath);
			
		} catch (FileNotFoundException e) { }
		
		filePath = path;
		NetWork netWork = fileReader.read(filePath);
		
//		Set<IJunction<?>> junctions  = netWork.getJunctions();
//			
//		
//		Set<IReservoir> reservoirs = netWork.getReservoirs();
//		
//		
//		Set<ITank<?>> tanks = netWork.getTanks();
//		
//		
//		Set<IPipe> pipes = netWork.getPipes();
//		
//		
//		Set<ITank<?>> pumps = netWork.getPumps();
//		
//		
//		List<String> options = netWork.getOptions();
//		
//		
//		List<String> patterns = netWork.getPattern();
//		
//		
//		List<String> energy = netWork.getEnergy();
//		
//		
//		List<String> times = netWork.getTimes();
//		
//		
//		Map<String, String> reports = netWork.getReports().getValues();
		
		
//		Set<ControlAction> controls = netWork.getControls();
		netWork.getReports().setValue("FILE",nome);
		
		EpaFileWriter efw = new EpaFileWriter();
		efw.write(netWork, path);
		//assertEquals("Relatorio-1.txt" , netWork.getReports().getValue("FILE").trim());
	}
	
	public static void main(String[] args) throws IOException {
		
		
		String path = "malha-37.inp";
		String nome = "resultado-0.txt";
		
		ModificarMalhas m = new ModificarMalhas(path, nome);
		m.modificar();
	}
}
