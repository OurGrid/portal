/**
 * 
 */
package org.ourgrid.portal.server.logic.executors.plugin.epanet.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Esta classe tem como fun��o genrenciar tarefas de leitura em arquivos padr�o ASCII
 * 
 * @author Jos� Fl�vio Mendes Vieira J�nior
 *
 */
public class InputManagerASCII implements InputManager{
	
	private String filePath;
	private BufferedReader input;
	
	/**
	 * @param filePath Nome do arquivo a ser lido
	 * @throws FileNotFoundException 
	 */
	public InputManagerASCII(String filePath) throws FileNotFoundException {
		this.setFilePath(filePath);
		this.input = (BufferedReader) getReader(filePath);
	}
	
	public Reader getReader(String path) throws FileNotFoundException{
		try {
			input = new BufferedReader( new FileReader(path) );
		} catch (FileNotFoundException fnfe) {
			System.err.println("Erro ao abrir o arquivo "+ path);
			System.err.println("Verifique se o arquivo "+ path +" existe");
			System.err.println(fnfe.getMessage());
			fnfe.printStackTrace();
			throw fnfe;
		}
		return input; 
	}

	/* (non-Javadoc)
	 * @see interfaces.io.InputManager#setFilePath(java.lang.String)
	 */
	public void setFilePath(String path) {
		this.filePath = path;
	}

	/* (non-Javadoc)
	 * @see interfaces.io.InputManager#openFile()
	 */
	public void openFile() throws FileNotFoundException{
		try {
			input = new BufferedReader( new FileReader(this.filePath) );
		} catch (FileNotFoundException fnfe) {
			System.out.println("InputManagerASCII.openFile()");
			System.err.println("Erro ao abrir o arquivo "+ filePath);
			System.err.println(fnfe.getMessage());
			//fnfe.printStackTrace();
			throw fnfe;
		}		 
	}

	/* (non-Javadoc)
	 * @see interfaces.io.InputManager#closeFile()
	 */
	public void closeFile() throws IOException{
		try {
			input.close();	
		} catch (IOException ioe) {
			System.out.println("InputManagerASCII.closeFile()");
			System.err.println("Erro ao fechar o arquivo "+ filePath);
			System.err.println(ioe.getMessage());
			//ioe.printStackTrace();
			throw ioe;
		}		 
	}
	
	public boolean isEndOfFile() throws IOException {
		
		return !input.ready();
	}
	
	/* (non-Javadoc)
	 * @see interfaces.io.InputManager#readLine()
	 */
	public String readLine() throws IOException{
		String line = null;
		try {
			if( input.ready() ){
				line = input.readLine();
			}
		} catch (IOException ioe) {
			System.out.println("InputManagerASCII.readLine()");
			System.err.println("Erro ao ler linha do arquivo "+ filePath);
			System.err.println(ioe.getMessage());
			throw ioe;
		}
		return line;
	}

	/* (non-Javadoc)
	 * @see interfaces.io.InputManager#readLine(int)
	 */
	public String readLine(int lineNumber) throws FileNotFoundException, IOException{
		String line   = "";
		int lineCount = 1;
		try {
			BufferedReader input = new BufferedReader( new FileReader(this.filePath) );
			while( input.ready() && (lineCount < lineNumber) ){
				input.readLine();
				lineCount++;
			}
			line = input.readLine();
			input.close();
		} catch( FileNotFoundException fnfe){
			System.out.println("InputManagerASCII.readLine()");
			System.err.println("Erro ao abir arquivo "+ filePath);
			System.err.println(fnfe.getMessage());
			//fnfe.printStackTrace();
			throw fnfe;
		} catch (IOException ioe) {
			System.out.println("InputManagerASCII.readLine()");
			System.err.println("Erro ao ler linha "+ lineNumber+ " do arquivo "+ filePath);
			System.err.println(ioe.getMessage());
			//ioe.printStackTrace();
			throw ioe;
		}
		return line;
	}

	/* (non-Javadoc)
	 * @see interfaces.io.InputManager#readColumn(int, int, int)
	 */
    public String readNextLine() throws IOException {
        String line = "";
        try {
            line = input.readLine();
            while( (input.ready()) && ((line.trim().equals("")) || (line.charAt(0) == '!')) ){
                line = input.readLine();
            }     

        } catch (IOException ioe) {
        	System.out.println("InputManagerASCII.readNextLine()");
			System.err.println("Erro ao ler pr�xima linha");
			throw ioe;
        }
        return line;
    }
	
	/* (non-Javadoc)
	 * @see interfaces.io.InputManager#readColumn(int, int, int)
	 */
	public String readColumn(int lineNumber, int startColumn, int endColumn) throws IOException{
		String line = "";
		line = this.readLine(lineNumber);
		line = (line == null ? "" : line.substring(startColumn, endColumn));
		return line;
	}

	public String getFilePath() {
		return filePath;
	}

}
