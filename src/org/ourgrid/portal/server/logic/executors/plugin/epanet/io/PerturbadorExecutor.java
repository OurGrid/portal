package org.ourgrid.portal.server.logic.executors.plugin.epanet.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;

import org.epanetgrid.AcceptFacade;





public class PerturbadorExecutor {

	private AcceptFacade facade;
	private String malhaFile;

	/**
	 * @return the facade
	 */
	public AcceptFacade getFacade() {
		return facade;
	}

	/**
	 * @param facade
	 *            the facade to set
	 */
	public void setFacade(AcceptFacade facade) {
		this.facade = facade;
	}

	/**
	 * @param facade
	 * @param inputManager
	 */
	public PerturbadorExecutor(AcceptFacade facade) {
		this.facade = facade;

	}

	/**
	 * @param facade
	 * @param inputManager
	 */
	public PerturbadorExecutor(AcceptFacade facade, String malhaFile) {
		this.facade = facade;
		this.malhaFile = malhaFile;
	}

	public PerturbadorExecutor(String malhaFile) {
		this.facade = new AcceptFacade();
		this.malhaFile = malhaFile;
	}

	/**
	 * @return the malhaFile
	 */
	public String getMalhaFile() {
		return malhaFile;
	}

	/**
	 * @param malhaFile
	 *            the malhaFile to set
	 */
	public void setMalhaFile(String malhaFile) {
		this.malhaFile = malhaFile;
	}

	public void addPerturbacao(String file) {

		try {
			facade.setMalhaEntrada(getMalhaFile());
			InputManager in = new InputManagerASCII(file);
			String line = in.readNextLine();
			while (line != null) {
				StringTokenizer st = new StringTokenizer(line);
				String elemento = st.nextToken();
				String propriedade = st.nextToken();
				double valorMinimo = Double.parseDouble(st.nextToken());
				double discretizacao = Double.parseDouble(st.nextToken());
				int quantidadeMalhas = new Integer(st.nextToken()).intValue();
				facade.addElementoPerturbado(elemento, propriedade,
						valorMinimo, discretizacao, quantidadeMalhas);
				line = in.readNextLine();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void geraPerturbacao(String outputdir, String outputNetworkPattern) {
		try {
			facade.geraPerturbacao(outputdir, outputNetworkPattern);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
