package org.ourgrid.portal.server.logic.executors.plugin.cisternas.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ourgrid.portal.server.logic.executors.plugin.cisternas.MontaArquivos;

import br.edu.ufcg.lsd.seghidro.cisternas.entities.Parametros;
import br.edu.ufcg.lsd.seghidro.cisternas.entities.input.ParametrosReader;
import br.edu.ufcg.lsd.seghidro.cisternas.exceptions.ParametrosReaderException;
import br.edu.ufcg.lsd.seghidro.cisternasbalhid.entities.Coordenadas;

/**
 * Classe responsável por paralelizar os arquivos de parâmetros e de precipitação.
 * 
 * @author Sávio Canuto de Oliveira Sousa.
 * @since 12/03/2009.
 */
public class ParalelizarArquivos {
	
	/**
	 * Variável responsável por finalizar uma linha no arquivo de saída.
	 */
	private static final String FIM_DE_LINHA = System
			.getProperty("line.separator");
	
	private final String FILE_SEPARATOR = System.getProperty("file.separator");
	
	/** Lista com os paths dos arquivos de parâmetros quando foram criados. */
	Map<String, String> listaPathsParametros = new HashMap<String, String>();
	
	/** Lista com os paths dos arquivos de precipitação quando foram criados. */
	Map<String, String> listaPathsPrecipitacao =  new HashMap<String, String>();
	
	private static ParalelizarArquivos instance = null;
	
	public static ParalelizarArquivos getInstance() {
		if (instance == null)
			instance = new ParalelizarArquivos();
		return instance;
	}
	
	/**
	 * 
	 * @param precipitacaoFile
	 * @param parametrosFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParametrosReaderException
	 * 
	 * Este método quebra o arquivo de precipitação em pequenas partes de acordo com sua 
	 * coordenada.
	 */
	public void paralelizar(String precipitationFile, String parametersFile, String rootFolder)
			throws FileNotFoundException, IOException,
			ParametrosReaderException {
		
		String pathPrecipitacao = precipitationFile;
		String pathParametros = parametersFile;
		
		Map<Coordenadas, Parametros> listaParametros = (new ParametrosReader(
				pathParametros)).getParametros();
		PrecipitacaoReader precipitacaoReader = new PrecipitacaoReader(
				pathPrecipitacao);
		
		Map<Coordenadas, String> listaPrecipitacoes = precipitacaoReader.read();
		int i = 1;
		for (Coordenadas coordenadas : listaParametros.keySet()) {

			if (listaPrecipitacoes.containsKey(coordenadas)) {
				// Lista com uma coordenada e uma string representando o arquivo
				// a ser criado.
				String nomeArquivo = listaParametros.get(coordenadas)
						.getDescricao().replace('(', '_');
				nomeArquivo = nomeArquivo.replace(')', '_')+i;
				
				File tmp = new File(rootFolder + FILE_SEPARATOR + "tmp");
				
				if(!tmp.exists()) {
					tmp.mkdir();
				}
				
				String pathTemp = tmp.getAbsolutePath() + FILE_SEPARATOR + nomeArquivo;

				this.criarArquivosPrecipitacao(pathTemp + ".pmh",
						listaPrecipitacoes.get(coordenadas));
				this.criarArquivosParametros(pathTemp + ".par", this
						.recuperarDadosParametros(coordenadas, listaParametros
								.get(coordenadas)));

				this.listaPathsPrecipitacao.put(nomeArquivo, pathTemp + ".pmh");
				this.listaPathsParametros.put(nomeArquivo, pathTemp + ".par");
				i++;
			}
		}
	}
	
	/**
	 * Método que cria os arquivos de parametros.
	 * 
	 * @param path
	 * @param conteudo
	 */
	public void criarArquivosParametros(String path, String conteudo) {
		
		MontaArquivos montarArquivo = new MontaArquivos(path);
		montarArquivo.writeLine(conteudo);
		montarArquivo.closeFile();
	}
	
	/**
	 * 
	 * @param path
	 * @param conteudo
	 *            Este método cria um arquivo de precipitação uma coordenada e
	 *            grava a lista de precipitações referentes a esta coordenada.
	 */
	public void criarArquivosPrecipitacao(String path, String conteudo) {

		MontaArquivos montarArquivo = new MontaArquivos(path);
		montarArquivo.writeLine(conteudo);
		montarArquivo.closeFile();
	}
	
	/**
	 * Método que recupera os dados do parâmetro corrente para a criação dos
	 * arquivos.
	 * 
	 * @param coord
	 * @param parametro
	 * @return string com os dados para a criação do arquivo.
	 */
	private String recuperarDadosParametros(Coordenadas coord, Parametros parametro) {
		
		String line = "";
		line += "!LATITUDE;LONGITUDE;DESCRIÇÃO;ACAP;CAPACIDADE;DEM;PERDAS"
				+ FIM_DE_LINHA;
		line += coord.getCoordX() + ";" + coord.getCoordY() + ";"
				+ parametro.getDescricao() + ";" + parametro.getAreaCapitacao()
				+ ";" + parametro.getCapacidade() + ";"
				+ parametro.getDemanda() + ";" + parametro.getPerdas()
				+ FIM_DE_LINHA;
		
		return line;
		
	}

	/**
	 * @return the listaPathsParametros
	 */
	public Map<String, String> getListaPathsParametros() {
		return listaPathsParametros;
	}

	/**
	 * @param listaPathsParametros the listaPathsParametros to set
	 */
	public void setListaPathsParametros(Map<String, String> listaPathsParametros) {
		this.listaPathsParametros = listaPathsParametros;
	}

	/**
	 * @return the listaPathsPrecipitacao
	 */
	public Map<String, String> getListaPathsPrecipitacao() {
		return listaPathsPrecipitacao;
	}

	/**
	 * @param listaPathsPrecipitacao the listaPathsPrecipitacao to set
	 */
	public void setListaPathsPrecipitacao(Map<String, String> listaPathsPrecipitacao) {
		this.listaPathsPrecipitacao = listaPathsPrecipitacao;
	}


}
