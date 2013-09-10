package org.ourgrid.portal.server.logic.executors.plugin.lvc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.ServiceTO;
import org.ourgrid.portal.client.plugin.lvc.to.response.LvcJobImageJoinerResponseTO;
import org.ourgrid.portal.client.plugin.lvc.to.service.LvcJobImageJoinerTO;
import org.ourgrid.portal.server.exceptions.ExecutionException;
import org.ourgrid.portal.server.logic.executors.AbstractExecutor;
import org.ourgrid.portal.server.logic.interfaces.OurGridPortalIF;

public class LvcJobImageJoinerExecutor extends AbstractExecutor{
	
	private final static String FINAL_IMAGE_NAME = "processedImage.jpg";
	
	private String outputDir;
	
	private Integer rows;
	
	private Integer cols;
	
	public LvcJobImageJoinerExecutor(OurGridPortalIF portal){
		super(portal);
	}
	

	public String getOutputDir() {
		return outputDir;
	}



	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	

	public Integer getRows() {
		return rows;
	}


	public void setRows(Integer rows) {
		this.rows = rows;
	}


	public Integer getCols() {
		return cols;
	}


	public void setCols(Integer cols) {
		this.cols = cols;
	}


	@Override
	public ResponseTO execute(ServiceTO serviceTO) throws ExecutionException {
		
		LvcJobImageJoinerTO lvcJobImageJoinerTO = (LvcJobImageJoinerTO) serviceTO;
		LvcJobImageJoinerResponseTO lvcJobImageJoinerResponseTO = new LvcJobImageJoinerResponseTO();
		
		this.setOutputDir(LvcJobImageJoiner.getInstance().getOutputDirs().get(lvcJobImageJoinerTO.getJobId()));
		this.setCols(LvcJobImageJoiner.getInstance().getChunks().get(lvcJobImageJoinerTO.getJobId())[0]);
		this.setRows(LvcJobImageJoiner.getInstance().getChunks().get(lvcJobImageJoinerTO.getJobId())[1]);
		
		boolean wasFinalImageGenerated;
		
		try {
			wasFinalImageGenerated = joinFinalImage();
			lvcJobImageJoinerResponseTO.setWasFinalImageGenerated(wasFinalImageGenerated);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			wasFinalImageGenerated = false;
			e.printStackTrace();
		}
		
		return lvcJobImageJoinerResponseTO;
	}
	
	public boolean joinFinalImage() throws IOException{
		boolean wasFinalImageGenerated = false;
		if(!finalImageExists()){
			int chunkWidth, chunkHeight;
			int type;
			int chunks = this.cols * this.rows;
			String dir = getOutputDir()+System.getProperty("file.separator");
			if(new File(dir).exists()){
				File[] imgFiles = new File[chunks];
				int num = 0;
				for(int i=0;i<cols;i++){
					for(int j=0;j<rows;j++){
						imgFiles[num] = new File(dir+"IN_"+i+"_"+j+".jpg");
						num++;
					}
				}
				System.out.println("Creating reads");
				BufferedImage buffImgs[] = new BufferedImage[chunks];
				for(int i=0;i<chunks;i++){
					buffImgs[i] = ImageIO.read(imgFiles[i]);
				}
				
				type = buffImgs[0].getType();
				chunkHeight = buffImgs[0].getHeight();
				chunkWidth = buffImgs[0].getWidth();
				System.out.println("FinalIMG");
				BufferedImage finalImg = new BufferedImage(chunkWidth*cols,chunkHeight*rows,type);
				
				System.out.println("Ploting final");
				num = 0;
				for(int i=0;i<cols;i++){
					for(int j=0;j<rows;j++){
						finalImg.createGraphics().drawImage(buffImgs[num],chunkWidth*j,chunkHeight*i,null);
						num++;
					}
				}
				
				ImageIO.write(finalImg, "jpeg", new File(dir+FINAL_IMAGE_NAME));
				wasFinalImageGenerated = true;
			}
		}
		return wasFinalImageGenerated;
	}
	
	private boolean finalImageExists(){
		File finalImage = new File(getOutputDir()+System.getProperty("file.separator")+FINAL_IMAGE_NAME);
		return finalImage.exists();
	}

	@Override
	public void logTransaction(ServiceTO serviceTO) {
		// TODO Auto-generated method stub
		
	}

}
