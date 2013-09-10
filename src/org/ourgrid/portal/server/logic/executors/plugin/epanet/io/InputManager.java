/**
 * 
 */
package org.ourgrid.portal.server.logic.executors.plugin.epanet.io;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Define m�todos necess�rios para genrenciar tarefas de leitura em arquivos
 * 
 * @author Jos� Fl�vio Mendes Vieria J�nior
 *
 */
public interface InputManager {
    
	/**
     * Define o caminho do arquivo a ser lido
     * 
     * @param fullyPath Caminho totalmente qualificado do arquivo
     */
	public void setFilePath(String fullyPath);
	
	/**
	 * Abre o arquivo
	 * 
	 * @return True se a opera��o for realizada com sucesso
	 */
	public void openFile() throws FileNotFoundException;
	
	/**
	 * L� a linha corrente
	 * 
	 * @return String com o conte�do referente a linha corrente
	 */
	public String readLine() throws IOException;
	
	/**
	 * L� a linha especificada
	 * 
	 * @param lineNumber N�mero da linha a ser lida
	 * @return String com o conte�do referente a linha especificada
	 */
	public String readLine(int lineNumber) throws FileNotFoundException, IOException;
	
    /**
     * L� a pr�xima linha v�lida, desprezando linhas em branco e com coment�rios
     * 
     * @return Pr�xima linha v�lida
     * @throws IOException
     */
    public String readNextLine() throws IOException;
	
	/**
	 * L� o conte�do, da linha especificada, entre os pontos inicial e final especificados   
	 * 
	 * @param lineNumber N�mero da linha a ser lida
	 * @param startColumn Inicio da coluna a ser lida
	 * @param endColumn Final da coluna a ser lida
	 * @return String com o valor encontrado na linha e entre os pontos especificados 
	 */
	public String readColumn(int lineNumber, int startColumn, int endColumn) throws IOException;
	
	/**
	 * Fecha o arquivo
	 * 
	 * @return True se a opera��o for realizada com sucesso
	 */
	public void closeFile() throws IOException;
	
	public String getFilePath();

	public boolean isEndOfFile() throws IOException;
	
}
