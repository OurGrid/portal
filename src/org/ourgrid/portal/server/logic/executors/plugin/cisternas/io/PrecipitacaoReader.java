/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.ourgrid.portal.server.logic.executors.plugin.cisternas.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.edu.ufcg.lsd.seghidro.cisternas.entities.input.InputManagerASCII;
import br.edu.ufcg.lsd.seghidro.cisternas.util.Parser;
import br.edu.ufcg.lsd.seghidro.cisternasbalhid.entities.Coordenadas;

/**
 * Classe responsável por realizar a leitura do arquivo de precipitação.
 * 
 * @author Sávio Canuto de Oliveira Sousa.
 * @since 12/03/2009.
 */
public class PrecipitacaoReader {
	
	/** Variável responsável por finalizar uma linha no arquivo de saída. */
	private static final String FIM_DE_LINHA = System
			.getProperty("line.separator");

	/** Representa o objeto de criação do arquivo. */
	private InputManagerASCII reader;
	
	/** Variável que verifica se é a primeira vez que lê o arquivo de precipitação. */
	boolean ehPrimeiraVez = true;
	
	/** Construtor vazio. */
	public PrecipitacaoReader() {
	}
	
	/**
	 * Construtor.
	 * @param file
	 * @throws FileNotFoundException
	 */
    public PrecipitacaoReader( String file ) throws FileNotFoundException{
		this.reader = new InputManagerASCII( file );
		this.reader.openFile();
    }
    
	/**
     * Método de leitura do arquivo.
     * @return
     * @throws IOException
     */
	public Map<Coordenadas, String> read() throws IOException {
		
		String lineResult = "";
		Map<Coordenadas, String> listaDadosArquivos = new HashMap<Coordenadas, String>();
		boolean first = true;
		
		try {
			while (true) {
			String line = "";
			
			/* 
			 * se for a primeira vez que for executado pega a primeira linha do
			 * arquivo de precipitação. 
			 */
			if (ehPrimeiraVez) {
				line = reader.readNextLine();
				this.ehPrimeiraVez = false;
			} else {
				line = "";
			//	listaDadosArquivos.clear();
			}
			
			while ((line.trim().equals("")) || (line.contains("!"))) {
				line = reader.readNextLine(); 
			}
			
			Coordenadas pontoAtual;
			Coordenadas pontoAnterior = lerPonto(line);
			lineResult = "";
			first = true;
			// enquanto existir linha no PMH
			while (line != null) {
				
				pontoAtual = lerPonto(line);
				
				// se as coordenadas lidas da linha são iguais
				if (pontoAnterior.equals(pontoAtual)) {
					// se é primeira linha do arquivo
					if (first) {
						first = false;
						lineResult += line;
					} else
						lineResult += FIM_DE_LINHA + line ;
					
					line = reader.readNextLine();
					
				} else 
					break;
			
			} //while
			listaDadosArquivos.put(pontoAnterior, lineResult);
			System.out.println(pontoAnterior);
			System.out.println();
			System.out.println(lineResult);
			
			// caso só contenha um ponto 
			if ( (line == null) && (lineResult != "") ) {
				this.reader.closeFile();
				break;
			}
		}
		} catch (IOException ioe) {
			System.err.println("Erro ao ler arquivo de precipitação");
			System.err.println(ioe.getMessage());
			throw ioe;
		}
		
		// retorna a lista de precipitações.
		return listaDadosArquivos;
	}

	/**
	 * Realiza a leitura da ponto de coordenada.
	 * @param line
	 * @return coordenada.
	 */
	public Coordenadas lerPonto(String line) {
		double x = new Double(line.substring(0, 21).replace(',', '.').trim()).doubleValue();
		double coordx = Parser.roundDouble(4,x);
		double y = new Double(line.substring(22, 40).replace(',', '.').trim()).doubleValue();
		double coordy = Parser.roundDouble(4,y);
		
		return new Coordenadas(coordx, coordy);
	}
	
}
