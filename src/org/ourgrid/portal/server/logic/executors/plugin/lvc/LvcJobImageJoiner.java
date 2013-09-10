package org.ourgrid.portal.server.logic.executors.plugin.lvc;

import java.util.HashMap;


public class LvcJobImageJoiner {
	
	private static LvcJobImageJoiner instance = null;
	
	private HashMap<Integer, String> outputDirs = new HashMap<Integer, String>();
	private HashMap<Integer, Integer[]> chunks = new HashMap<Integer, Integer[]>();
	
	public static LvcJobImageJoiner getInstance(){
		if(instance == null){
			instance = new LvcJobImageJoiner();
		}
		return instance;
	}
	
	private LvcJobImageJoiner(){}

	public HashMap<Integer, String> getOutputDirs() {
		return outputDirs;
	}

	public void setOutputDirs(HashMap<Integer, String> outputDirs) {
		this.outputDirs = outputDirs;
	}
	
	public void addOutputDir(Integer jobId, String outputDir){
		this.outputDirs.put(jobId, outputDir);
	}

	public HashMap<Integer, Integer[]> getChunks() {
		return chunks;
	}

	public void setChunks(HashMap<Integer, Integer[]> chunks) {
		this.chunks = chunks;
	}
	
	public void addChunks(Integer jobId, Integer[] chunks){
		this.chunks.put(jobId, chunks);
	}

	
}
