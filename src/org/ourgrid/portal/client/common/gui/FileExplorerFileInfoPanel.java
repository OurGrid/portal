package org.ourgrid.portal.client.common.gui;

import java.util.List;

import org.ourgrid.portal.client.common.image.FileExplorerIcons;
import org.ourgrid.portal.client.common.to.model.FileTO;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ProgressBar;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;

public class FileExplorerFileInfoPanel extends LayoutContainer {

	public static final FileExplorerIcons ICONS = GWT.create(FileExplorerIcons.class);
	
	private LayoutContainer fileImageContainer;
	private LayoutContainer fileAndQuotaContainer;
	
	private Image fileImage;
	private Text fileDescription;
	private ProgressBar quotaInfo;
	
	private List<FileTO> filesSelected;
	
	public FileExplorerFileInfoPanel(List<FileTO> filesSelected) {
		
		setFilesSelected(filesSelected);
		init();
	}
	
	protected void init() {
		
		setLayout(new ColumnLayout());
		setSize(705, 360);
		
		createFileImageContainer();
		
		add(this.fileImageContainer, new ColumnData(.07092));
		
		createFileAndQuotaContainer();
		
		createFileDescription();
		createQuotaProgressBar();
		
		add(this.fileAndQuotaContainer, new ColumnData(.92908));
		
		setFileInfo(this.filesSelected);
	}
	
	protected void createFileImageContainer() {
		
		this.fileImageContainer = new LayoutContainer();
		this.fileImageContainer.setSize(51, 51);
	}
	
	protected void createFileAndQuotaContainer() {
		this.fileAndQuotaContainer = new LayoutContainer();
		this.fileAndQuotaContainer.setLayout(new RowLayout(Orientation.VERTICAL));
		this.fileAndQuotaContainer.setSize(653, 50);
	}
	
	protected void createFileDescription() {
		
		this.fileDescription = new Text();
		this.fileDescription.addStyleName("pad-text");
		this.fileDescription.setStyleAttribute("font-size", "x-small");
		this.fileDescription.setStyleAttribute("backgroundColor", "#C7DFFC");
		this.fileDescription.setSize(653, 25);
		this.fileDescription.setBorders(true);
		
		this.fileAndQuotaContainer.add(this.fileDescription);
	}
	
	protected void createQuotaProgressBar() {
		
		this.quotaInfo = new ProgressBar();
		this.quotaInfo.setWidth(653);
		this.quotaInfo.updateText("0% of quota");
		this.fileAndQuotaContainer.add(this.quotaInfo);
	}
	
	public void setFileInfo(List<FileTO> filesSelected) {
		
		setFilesSelected(filesSelected);
		setFileImage();
		setFileDescription();
	}
	
	protected void setFileImage() {
		
		this.fileImageContainer.removeAll();
		
		AbstractImagePrototype imagePrototype;
		
		if (!isFolder()) {
			imagePrototype = ICONS.simpleFile();
		} else if (isEmptyFolder()) {
			imagePrototype = ICONS.emptyFolder();
		} else {
			imagePrototype = ICONS.notEmptyFolder();
		}
		
		this.fileImage = imagePrototype.createImage();
		this.fileImage.getElement().getStyle().setProperty("marginTop", "10px");
		this.fileImage.getElement().getStyle().setProperty("marginLeft", "9px");  
		this.fileImage.setSize("35px", "35px");
		this.fileImage.setVisible(true);
		
		this.fileImageContainer.add(this.fileImage);
		this.fileImageContainer.layout(true);
	}
	
	private boolean isFolder() {
		
		for (FileTO file : this.filesSelected) {
			if (file.isFolder()) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isEmptyFolder() {
		
		for (FileTO file : this.filesSelected) {
			if (file.isFolder() && file.isLeaf()) {
				return true;
			}
		}
		
		return false;
	}
	
	protected void setFileDescription() {
		
		final String LINESEPARATOR = "<br>";
		String description = "";
		
		if (this.filesSelected.isEmpty()) {
			description += "No files selected" + LINESEPARATOR;
		} else if (this.filesSelected.size() == 1) {
			
			FileTO item = this.filesSelected.get(0);
			description += item.getName() + " selected" + LINESEPARATOR;
			
			if (isFolder()) {
				description += "contains " + item.getListFiles().size() + " itens";
			} else {
				description += item.getSize() + " bytes"; 
			}
		} else {
			description += this.filesSelected.size() + " items selected" + LINESEPARATOR;
		}
		
		this.fileDescription.setText(description);
	}
	
	public void setQuotaInfo(Double quotaUsedPercentage) {
		
		Integer storageUsedInteger = (int) (100 * quotaUsedPercentage);
		String storageUsedFormated = storageUsedInteger.toString();
		this.quotaInfo.updateProgress(quotaUsedPercentage, storageUsedFormated + "% of quota");
	}
	
	public void setFilesSelected(List<FileTO> filesSelected) {
		this.filesSelected = filesSelected;
	}
}