package org.ourgrid.portal.server.logic.executors.plugin.cisternas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ufcg.lsd.seghidro.cisternas.entities.output.OutputManager;
import br.edu.ufcg.lsd.seghidro.cisternas.entities.output.OutputManagerASCII;

public class MontaArquivos {

	/**
	 * Variável responsável por finalizar uma linha no arquivo de saída.
	 */
	private static final String FIM_DE_LINHA = System
			.getProperty("line.separator");

	private String fileOut;

	private OutputManager outputManager;

	/**
	 * @param fileOut
	 * @param outputManager
	 */
	public MontaArquivos(String fileOut, OutputManager outputManager) {
		this.fileOut = fileOut;
		this.outputManager = outputManager;
	}

	/**
	 * @param fileOut
	 */
	public MontaArquivos(String fileOut) {
		this.fileOut = fileOut;
		outputManager = new OutputManagerASCII(fileOut);
		try {
			outputManager.createFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void escreveArquivoCenarios(Map<String, ArrayList<String>> cenario) {
		writeLine("!Cenários");
		escreve("Demanda=",cenario.get("demd_"));
		escreve("Volume=" , cenario.get("vol_"));
		escreve("AreaCaptacao=" , cenario.get("areacap_"));
		closeFile();
	}

	private void escreve(String inicioLinha , List<String> valores) {

		String line = inicioLinha;

		for (int i = 0; i < valores.size(); i++) {
			line += valores.get(i) + ";";
		}
		line = line.substring(0, line.lastIndexOf(";"));
		System.out.println("escrevendo valores no arquivo " + line);
		writeLine(line);
	}

	
	
	public void writeLine(String line) {
		try {
			outputManager.writeLine(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeFile() {
		try {
			outputManager.closeFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the fileOut
	 */
	public String getFileOut() {
		return fileOut;
	}

	/**
	 * @param fileOut
	 *            the fileOut to set
	 */
	public void setFileOut(String fileOut) {
		this.fileOut = fileOut;
	}

	/**
	 * @return the outputManager
	 */
	public OutputManager getOutputManager() {
		return outputManager;
	}

	/**
	 * @param outputManager
	 *            the outputManager to set
	 */
	public void setOutputManager(OutputManager outputManager) {
		this.outputManager = outputManager;
	}

	/**
	 * @return the fIM_DE_LINHA
	 */
	public static String getFIM_DE_LINHA() {
		return FIM_DE_LINHA;
	}

	public static void main(String[] args) {
		Map<String, ArrayList<String>> valores = new HashMap<String, ArrayList<String>>();

		ArrayList<String> lista = new ArrayList<String>();
		for (int j = 0; j < 3; j++) {
			String s = Math.random() + "";
			s.replace(".",",");
			lista.add(s);
		}
		ArrayList<String> lista1 = new ArrayList<String>();
		for (int j = 0; j < 3; j++) {
			String s = Math.random() + "";
			s.replace(".",",");
			lista1.add(s);
		}
		ArrayList<String> lista2 = new ArrayList<String>();
		for (int j = 0; j < 3; j++) {
			String s = Math.random() + "";
			s.replace(".",",");
			lista2.add(s);
		}
		valores.put("demd_", lista);
		valores.put("vol_", lista);
		valores.put("areacap_", lista);
		
		MontaArquivos m = new MontaArquivos("cenarioTeste");
		m.escreveArquivoCenarios(valores);
		
	}

}
