package org.ourgrid.portal.client.common.gui;

import static org.ourgrid.portal.client.common.util.JobViewIdGenerator.getNextJobViewId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.ourgrid.portal.client.OurGridPortal;
import org.ourgrid.portal.client.common.CommonServiceConstants;
import org.ourgrid.portal.client.common.gui.model.UserModel;
import org.ourgrid.portal.client.common.image.JobStatusTreeIcons;
import org.ourgrid.portal.client.common.to.model.FileTO;
import org.ourgrid.portal.client.common.to.model.ResultTO;
import org.ourgrid.portal.client.common.to.response.CompressFilesResponseTO;
import org.ourgrid.portal.client.common.to.response.DeleteFileResponseTO;
import org.ourgrid.portal.client.common.to.response.FileExplorerResponseTO;
import org.ourgrid.portal.client.common.to.response.JobSubmissionResponseTO;
import org.ourgrid.portal.client.common.to.response.NewFolderResponseTO;
import org.ourgrid.portal.client.common.to.response.PasteFileResponseTO;
import org.ourgrid.portal.client.common.to.response.RenameFileResponseTO;
import org.ourgrid.portal.client.common.to.response.ResponseTO;
import org.ourgrid.portal.client.common.to.service.CompressFilesTO;
import org.ourgrid.portal.client.common.to.service.DeleteFileTO;
import org.ourgrid.portal.client.common.to.service.ExtractFilesTO;
import org.ourgrid.portal.client.common.to.service.FileExplorerTO;
import org.ourgrid.portal.client.common.to.service.JobSubmissionTO;
import org.ourgrid.portal.client.common.to.service.NewFolderTO;
import org.ourgrid.portal.client.common.to.service.PasteFileTO;
import org.ourgrid.portal.client.common.to.service.RenameFileTO;
import org.ourgrid.portal.client.common.util.FileExplorerUtil;
import org.ourgrid.portal.client.common.util.JobSubmissionMessages;
import org.ourgrid.portal.client.common.util.OurGridPortalServerUtil;
import org.ourgrid.portal.client.common.util.VType;

import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.data.TreeModelReader;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreeGridEvent;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
  
public class FileExplorerPanel extends LayoutContainer {  
  
	private static DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm:ss");
	
	private FileExplorerTreePanel fileExplorerTreePanel;
	private TreeStore<FileTO> storeTreePanel;
	private TreeStore<FileTO> storeGrid;
	private TreeGrid<FileTO> treeGrid;
	private FileTO root;
	private FileTO rootTmp;
	private FileTO cutSource;
	private TextField<String> location;
	private LayoutContainer left;
	private LayoutContainer right;
	
	private UploadFileWindow uploadFileWindow;
	
	private Menu contextMenuGenerealOperationsForFiles;
	private Menu contextMenuSubmitAndGeneralOperations;
	private Menu contextMenuGeneralOperationsForOneFolder;
	private Menu contextMenuNewFolder;
	private Menu contextMenuGeneralOperationsForAnyElements;
	private Menu contextMenuDecompressAndGeneralOperations;
	
	private MenuItem compresssMenuItem;
	private MenuItem decompressMenuItem;
	
	private ContentPanel locationPanel;
	private HorizontalPanel navigationPanel;
	private FileExplorerFileInfoPanel fileInfoPanel;
	
	private Button homeButton;
	private Button refreshButton;
	private Button uploadButton;
	private Button archiveManagerButton;
	private Button cutButton;
	private Button copyButton;
	private Button pasteButton;
	private Button newFolderButton;
	private Button deleteButton;
	
	private List<String> expandedList;

	private List<FileTO> copyFileList;
	private List<FileTO> cutFileList;
	
	private Double quota;
	
	private ToolBar toolBar;
	private boolean isQuotaExeeded;
	private boolean submitExplorerNode;
	
	public static final JobStatusTreeIcons ICONS = GWT.create(JobStatusTreeIcons.class);
	
  	public FileExplorerPanel(FileTO fileTO, Double initialQuota) {
  		this.root = fileTO;
  		this.rootTmp = fileTO;
  		this.isQuotaExeeded = false;
  		this.expandedList = new ArrayList<String>();
  		this.copyFileList = new ArrayList<FileTO>();
  		this.cutFileList  = new ArrayList<FileTO>();
  		this.quota = initialQuota;
  		this.submitExplorerNode = false;
  		
  		init();
  	}
  	
	private void init() {
		initButtons();
		
	    createLocationPanel();
	    createNavigationPanel();
	    
	    createNavigationToolBar();
	    createLocationField();
	    
		createContextMenus();
		
	    LayoutContainer layoutContainer = new LayoutContainer();  
		layoutContainer.setLayout(new ColumnLayout());
		
		createLeftContainer(); 
	    
	    createTree(); 
		
	    createRightContainer();  
		
		createTreeGrid();
		
		layoutContainer.add(left, new ColumnData(.272));
		layoutContainer.add(right, new ColumnData(.728)); 
		
		add(layoutContainer);
		
		createFileInfoContainer();
		
		updateLocation(this.root);
		updateFileInfoBar(this.quota);
		
	}
	
	private void initButtons() {
		
		this.uploadButton = new Button("Upload", IconHelper.createPath("resources/images/silk/upload.gif"));
		this.newFolderButton = new Button("New Folder", IconHelper.createPath("resources/images/silk/newFolder.gif"));
		this.deleteButton = new Button("Delete", IconHelper.createPath("resources/images/silk/delete.gif"));
		this.pasteButton = new Button("Paste", IconHelper.createPath("resources/images/silk/paste.gif"));
		this.copyButton = new Button("Copy", IconHelper.createPath("resources/images/silk/copy.gif"));
		this.cutButton = new Button("Cut", IconHelper.createPath("resources/images/silk/cut.gif"));
		this.homeButton = new Button("Home", IconHelper.createPath("resources/images/silk/home.gif"));
		this.archiveManagerButton = new Button("Archive Manager", IconHelper.createPath("resources/images/silk/archiveManager.gif"));
	}	
  	
	private void createFileInfoContainer() {
		
		HorizontalPanel fileInfoHorizontalPanel = new HorizontalPanel();
		fileInfoHorizontalPanel.setBorders(false);
		fileInfoHorizontalPanel.setSpacing(6);
		fileInfoHorizontalPanel.setWidth(705);
		
		List<FileTO> files = new LinkedList<FileTO>();
		files.add(this.root);
		
		this.fileInfoPanel = new FileExplorerFileInfoPanel(files);
		
		fileInfoHorizontalPanel.add(this.fileInfoPanel);
		add(fileInfoHorizontalPanel);
	}

	private void createContextMenus() {
		createContextMenuGeneralOperationsForFiles();
		createContextMenuDecompressAndGeneralOperations();
		createContextMenuSubmitAndGeneralOperations();
		createContextMenuGeneralOperationsForAnyElements();
		createContextMenuGeneralOperationsForOneFolder();
		createContextMenuNewFolder(false);
	}
	
	private void createContextMenuGeneralOperationsForFiles() {
		MenuItem download  = createDownloadMenuItem();
    	
		MenuItem cut 	   = createCutMenuItem();
    	cut.setEnabled(!isQuotaExeeded);
    	
    	MenuItem copy      = createCopyMenuItem();
    	copy.setEnabled(!isQuotaExeeded);
    	
    	MenuItem delete    = createDeleteMenuItem();
		MenuItem rename	   = createRenameMenuItem();
		
		contextMenuGenerealOperationsForFiles = new Menu();  
	    contextMenuGenerealOperationsForFiles.add(download);
	    
	    contextMenuGenerealOperationsForFiles.add(new SeparatorMenuItem());
    	contextMenuGenerealOperationsForFiles.add(cut);
    	contextMenuGenerealOperationsForFiles.add(copy);
	    contextMenuGenerealOperationsForFiles.add(delete);
	    contextMenuGenerealOperationsForFiles.add(new SeparatorMenuItem());
	    contextMenuGenerealOperationsForFiles.add(rename);
	}
	
	private void createContextMenuDecompressAndGeneralOperations() {
		MenuItem decompres  = createDecompressMenuItem();
		decompres.setEnabled(!isQuotaExeeded);
		
		MenuItem download  = createDownloadMenuItem();
		
		MenuItem cut 	   = createCutMenuItem();
		cut.setEnabled(!isQuotaExeeded);
		
		MenuItem copy      = createCopyMenuItem();
		copy.setEnabled(!isQuotaExeeded);
		
		MenuItem delete    = createDeleteMenuItem();
		MenuItem rename	   = createRenameMenuItem();
		
		contextMenuDecompressAndGeneralOperations = new Menu();  
		contextMenuDecompressAndGeneralOperations.setId("contextMenuSubmitAndGeneralOperationsID");
		contextMenuDecompressAndGeneralOperations.add(decompres);
		contextMenuDecompressAndGeneralOperations.add(download);
		contextMenuDecompressAndGeneralOperations.add(new SeparatorMenuItem());
		contextMenuDecompressAndGeneralOperations.add(cut);
		contextMenuDecompressAndGeneralOperations.add(copy);
		contextMenuDecompressAndGeneralOperations.add(delete);
		contextMenuDecompressAndGeneralOperations.add(new SeparatorMenuItem());
		contextMenuDecompressAndGeneralOperations.add(rename);
	}
	
	
	private void createContextMenuSubmitAndGeneralOperations() {
		MenuItem submit    = createSubmitMenuItem(); 
		submit.setEnabled(!isQuotaExeeded);
		
		MenuItem download  = createDownloadMenuItem();
		
		MenuItem cut 	   = createCutMenuItem();
		cut.setEnabled(!isQuotaExeeded);
		
		MenuItem copy      = createCopyMenuItem();
		copy.setEnabled(!isQuotaExeeded);
		
		MenuItem delete    = createDeleteMenuItem();
		MenuItem rename	   = createRenameMenuItem();
		
		contextMenuSubmitAndGeneralOperations = new Menu();  
		contextMenuSubmitAndGeneralOperations.setId("contextMenuSubmitAndGeneralOperationsID");
		contextMenuSubmitAndGeneralOperations.add(submit);
		contextMenuSubmitAndGeneralOperations.add(download);
	    contextMenuSubmitAndGeneralOperations.add(new SeparatorMenuItem());
	    contextMenuSubmitAndGeneralOperations.add(cut);
	    contextMenuSubmitAndGeneralOperations.add(copy);
	    contextMenuSubmitAndGeneralOperations.add(delete);
	    contextMenuSubmitAndGeneralOperations.add(new SeparatorMenuItem());
	    contextMenuSubmitAndGeneralOperations.add(rename);
	}

	private void createContextMenuGeneralOperationsForAnyElements() {
		MenuItem cut 	   = createCutMenuItem();
		cut.setEnabled(!isQuotaExeeded);
		
		MenuItem copy      = createCopyMenuItem();
		copy.setEnabled(!isQuotaExeeded);
		
		MenuItem delete    = createDeleteMenuItem();
		
		contextMenuGeneralOperationsForAnyElements = new Menu();  
		contextMenuGeneralOperationsForAnyElements.add(cut);
		contextMenuGeneralOperationsForAnyElements.add(copy);
		contextMenuGeneralOperationsForAnyElements.add(delete);
	}

	private void createContextMenuGeneralOperationsForOneFolder() {
		
		MenuItem compress  = createCompressMenuItem();
		compress.setEnabled(!isQuotaExeeded);
		
		MenuItem cut 	   = createCutMenuItem();
		cut.setEnabled(!isQuotaExeeded);
		
		MenuItem copy      = createCopyMenuItem();
		copy.setEnabled(!isQuotaExeeded);
		
		MenuItem delete    = createDeleteMenuItem();
		MenuItem rename	   = createRenameMenuItem();
		
		contextMenuGeneralOperationsForOneFolder = new Menu();  
		contextMenuGeneralOperationsForOneFolder.add(compress);
		contextMenuGeneralOperationsForOneFolder.add(new SeparatorMenuItem());
		contextMenuGeneralOperationsForOneFolder.add(cut);
		contextMenuGeneralOperationsForOneFolder.add(copy);
		contextMenuGeneralOperationsForOneFolder.add(delete);
	    contextMenuGeneralOperationsForOneFolder.add(new SeparatorMenuItem());
	    contextMenuGeneralOperationsForOneFolder.add(rename);
	} 
	
	private void createContextMenuNewFolder(boolean withPasteItem) {
		MenuItem newFolder = createNewFolderMenuItem();
		contextMenuNewFolder = new Menu();  

		if(withPasteItem){
			MenuItem paste = createPasteMenuItem();
			paste.setEnabled(!isQuotaExeeded);
			contextMenuNewFolder.add(paste);
		}
		contextMenuNewFolder.add(newFolder);
	}

	private void createRightContainer() {
	    right = new LayoutContainer();  
	    FormLayout layout = new FormLayout();  
	    layout.setLabelAlign(LabelAlign.RIGHT);  
	    right.setLayout(layout);
	}

	private void createLeftContainer() {
	    left = new LayoutContainer();  
	    left.setStyleAttribute("paddingLeft", "5px");
	    FormLayout layout = new FormLayout();  
	    layout.setLabelAlign(LabelAlign.LEFT);  
	    left.setLayout(layout);
	}

	private void createNavigationPanel() {
		navigationPanel = new HorizontalPanel();
	    navigationPanel.setBorders(false);
	    navigationPanel.setSpacing(5);
	    navigationPanel.setWidth(705);
	    navigationPanel.add(locationPanel);
	    add(navigationPanel);
	}

	private void createLocationPanel() {
		locationPanel = new ContentPanel();
		locationPanel.setBorders(false);
		locationPanel.setHeaderVisible(false);
		locationPanel.setWidth(705);
	}  
	
	private void createNavigationToolBar() {  
	    
	    toolBar = new ToolBar();  
	  
	    createHomeButton();
	    createNewSeparator();  
	    createRefreshButton();  
	    createNewSeparator();
	    createCutButton();
	    createCopyButton();
	    createPasteButton();
	    createDeleteButton();
	    createNewSeparator();
	    createNewFolderButton();
	    createNewSeparator();
	    createUploadButton();
	    createNewSeparator();
	    createArchiveManager();
	  
	    locationPanel.setTopComponent(toolBar);  
	
	}

	private void createNewSeparator() {
		toolBar.add(new SeparatorToolItem());
	}

	private void createUploadButton() {
		uploadButton.setEnabled(!isQuotaExeeded);
		uploadButton.addSelectionListener(new SelectionListener<ButtonEvent>() {  
		      public void componentSelected(ButtonEvent ce) { 
		    	uploadFile();
		      }  
		});
	    toolBar.add(uploadButton);
	}
	
	private void createArchiveManager() {
		archiveManagerButton.setEnabled(!isQuotaExeeded);
		
		compresssMenuItem = createCompressMenuItem();
		decompressMenuItem = createDecompressMenuItem();
		
		Menu menu = new Menu();
		menu.add(compresssMenuItem);
		menu.add(decompressMenuItem);
		
		archiveManagerButton.setMenu(menu);
	    toolBar.add(archiveManagerButton);
	}
	
	private void createNewFolderButton() {
	    newFolderButton.addSelectionListener(new SelectionListener<ButtonEvent>() {  
	        @Override  
	        public void componentSelected(ButtonEvent ce) {  
	          MessageBox.prompt("New Folder", "Enter the new folder name:",  
	              new Listener<MessageBoxEvent>() {  
	                
	        	  	public void handleEvent(MessageBoxEvent be) {  
	                  
	        	  		if (be.getButtonClicked().getItemId().equals(Dialog.OK) && be.getValue() != null) {  
	        	  			createNewFolder(be.getValue());
	        	  		}  
	                }  
	          });  
	        }  
	      });
	    toolBar.add(newFolderButton);
	}

	private void createDeleteButton() {
	    deleteButton.setEnabled(false);
	    deleteButton.addSelectionListener(new SelectionListener<ButtonEvent>() {  
	        @Override  
	        public void componentSelected(ButtonEvent ce) {  
	          MessageBox.confirm("Delete File", "Do you really want to delete the selected files?",  
	              new Listener<MessageBoxEvent>() {  
	                
	        	  	public void handleEvent(MessageBoxEvent be) {  
	                  
	        	  		if (be.getButtonClicked().getItemId().equals(Dialog.YES)) {  
	        	  			List<FileTO> listFile = treeGrid.getSelectionModel().getSelectedItems();  
	        		        if (!listFile.isEmpty()) {  
	        		        	submitDeleteFile(listFile);
	        		        }
	        	  		}  
	                }  
	          });  
	        }  
	      });
	    toolBar.add(deleteButton);
	}

	private void createPasteButton() {
	    pasteButton.setEnabled(!this.copyFileList.isEmpty() && !isQuotaExeeded);
	    pasteButton.addSelectionListener(new SelectionListener<ButtonEvent>() {  
		      public void componentSelected(ButtonEvent ce) { 
	  				submitPasteFile();
		      }  
		});
	    toolBar.add(pasteButton);
	}

	private void createCopyButton() {
	    copyButton.setEnabled(false);
	    copyButton.addSelectionListener(new SelectionListener<ButtonEvent>() {  
		      public void componentSelected(ButtonEvent ce) { 
	  				List<FileTO> listFile = treeGrid.getSelectionModel().getSelectedItems();  
  	  				if (!listFile.isEmpty()) {  
  	  					copyFiles(listFile);
  	  				}
		      }  
		});
	    toolBar.add(copyButton);
	}

	private void createCutButton() {
		cutButton = new Button("Cut", IconHelper.createPath("resources/images/silk/cut.gif"));
	    cutButton.setEnabled(false);
	    cutButton.addSelectionListener(new SelectionListener<ButtonEvent>() {  
		      public void componentSelected(ButtonEvent ce) { 
  	  				List<FileTO> listFile = treeGrid.getSelectionModel().getSelectedItems();  
  	  				if (!listFile.isEmpty()) {  
  	  					cutFiles(listFile, rootTmp);
  	  				}
		      }

		});
	    toolBar.add(cutButton);
	}

	private void createRefreshButton() {
		refreshButton = new Button("Refresh", IconHelper.createPath("resources/images/silk/refresh.gif"));
	    refreshButton.addSelectionListener(new SelectionListener<ButtonEvent>() {  
		      public void componentSelected(ButtonEvent ce) { 
		    	  refresh();
		      }

		});
	    toolBar.add(refreshButton);
	}

	private void createHomeButton() {
	    homeButton.addSelectionListener(new SelectionListener<ButtonEvent>() {  
		      public void componentSelected(ButtonEvent ce) { 
		    	  goHome();
		      }  
		});
	    toolBar.add(homeButton);
	}
  	
	private void createLocationField() {
		
		location = new TextField<String>();
		location.setWidth(705);
		location.setName("Location");
		location.setFieldLabel("Location");
		location.setAllowBlank(true);
		location.setValue(this.root.getLocation());
		location.setEnabled(false);
		location.setSelectOnFocus(true);
		location.setId("locationIdFileExplorer");
	    
		locationPanel.add(location);
		updateLocation(root);
	}
	
	
	private void createTreeGrid() {  
		
		TreeModelReader<List<FileTO>> treeModelReader = new TreeModelReader<List<FileTO>>();
		TreeLoader<FileTO> loader = new BaseTreeLoader<FileTO>(treeModelReader) {  
			public boolean hasChildren(FileTO parent) {  
				return false;  
			}  
		};
		
	    storeGrid = new TreeStore<FileTO>(loader);  
	    
	    ColumnConfig name = new ColumnConfig("name", "Name", 100);  
	    name.setRenderer(new TreeGridCellRenderer<FileTO>());  
	  
	    ColumnConfig sizeColumn = new ColumnConfig("size", "Size", 100);  
	    ColumnConfig dateColumn = new ColumnConfig("date", "Date", 100);
	    dateColumn.setDateTimeFormat(DATE_FORMAT);
	    ColumnModel cm = new ColumnModel(Arrays.asList(name, sizeColumn, dateColumn));  
	    
	    createStoreGrid(root.getChildren(), root.getListFiles());
	    treeGrid = new TreeGrid<FileTO>(storeGrid, cm);  
	    treeGrid.setAutoExpandColumn("name");  
	    treeGrid.setTrackMouseOver(false);  
	    
	    ContentPanel cp = new ContentPanel();
	    cp.setHeaderVisible(false);
	    cp.setSize(515, 360);  
	    cp.setLayout(new FitLayout());  
	    cp.add(treeGrid);   
	    right.add(cp); 
	    
	    treeGrid.setIconProvider(new ModelIconProvider<FileTO>() {
		      public AbstractImagePrototype getIcon(FileTO model) {
		    	  
		    	  	boolean isFolder = model.isFolder();
					if (!isFolder) {
						return ICONS.file();
					}else{
						return ICONS.folder();
					}
		       }
		});
	    
	    treeGrid.addListener(Events.OnDoubleClick, new Listener<TreeGridEvent<FileTO>>() {
			
			@Override
			public void handleEvent(TreeGridEvent<FileTO> be) {
				
				FileTO fileTO = be.getModel();
				
				if (fileTO == null) return;
				
				if (fileTO.isFolder()) {
					
					if(submitExplorerNode == true){
						submitExplorerNode(fileTO, false, true);
					}
					addExpandedNode(fileTO);
					fileExplorerTreePanel.setExpanded(fileTO, true);
					treeGrid.getSelectionModel().deselectAll();
					submitExplorerNode = true;
				
				}
				cutButton.setEnabled(!isQuotaExeeded && !treeGrid.getSelectionModel().getSelectedItems().isEmpty());
				copyButton.setEnabled(!isQuotaExeeded && !treeGrid.getSelectionModel().getSelectedItems().isEmpty());
				deleteButton.setEnabled(!treeGrid.getSelectionModel().getSelectedItems().isEmpty());
				archiveManagerButton.setEnabled(!isQuotaExeeded && isArchiveManagerButtonEnabled());
				treeGrid.setContextMenu(null);
			}
		});
	    
	    treeGrid.addListener(Events.OnMouseOver, new Listener<TreeGridEvent<FileTO>>() {
			
			@Override
			public void handleEvent(TreeGridEvent<FileTO> be) {

				FileTO fileTO = be.getModel();

				if (fileTO == null) {
					treeGrid.setContextMenu(contextMenuNewFolder);
					return;
				}
				
				if(treeGrid.getSelectionModel().getSelectedItems().size() > 1){
					treeGrid.setContextMenu(contextMenuGeneralOperationsForAnyElements);
				}else if(treeGrid.getSelectionModel().getSelectedItems().size() == 1){
					if(fileTO.isFolder()){
						treeGrid.setContextMenu(contextMenuGeneralOperationsForOneFolder);
					}else{
						if(isValidJDFFile(fileTO.getName())){
							treeGrid.setContextMenu(contextMenuSubmitAndGeneralOperations);
						}else if(isValidZIPFile(fileTO.getName())){
							treeGrid.setContextMenu(contextMenuDecompressAndGeneralOperations);
						}else{
							treeGrid.setContextMenu(contextMenuGenerealOperationsForFiles);
						}
					}
				}
			}
		});
	    
	    treeGrid.addListener(Events.OnClick, new Listener<TreeGridEvent<FileTO>>() {
			
			@Override
			public void handleEvent(TreeGridEvent<FileTO> be) {

				FileTO fileTO = be.getModel();

				if (fileTO == null) {
					return;
				}
				
				if (!treeGrid.getSelectionModel().getSelectedItems().isEmpty()) {
					
					cutButton.setEnabled(true && !isQuotaExeeded);
					copyButton.setEnabled(true && !isQuotaExeeded);
					deleteButton.setEnabled(true);
					archiveManagerButton.setEnabled(!isQuotaExeeded && isArchiveManagerButtonEnabled());
					
					boolean isZIPFile = treeGrid.getSelectionModel().getSelectedItems().size() == 1 && isValidZIPFile(treeGrid.getSelectionModel().getSelectedItem().getName());
					
					updateArchiveManagerButton(isZIPFile);
					
					fileExplorerTreePanel.getSelectionModel().deselectAll();
					updateFileInfo(treeGrid.getSelectionModel().getSelectedItems());
				}
			}
		});
	    
  	}
	
	private void updateArchiveManagerButton(boolean isZIPFile) {
		
		compresssMenuItem.setEnabled(!isZIPFile);
		decompressMenuItem.setEnabled(isZIPFile);
		
	}

	private boolean isValidJDFFile(String fileName) {
		
		String validExtension = "jdf";
		
		if (fileName.endsWith(validExtension)) {
			return true;
		}
		return false;
	}
	
	private boolean isValidZIPFile(String fileName) {
		
		String validExtension = "zip";
		
		if (fileName.endsWith(validExtension)) {
			return true;
		}
		return false;
	}
	

	private void createTree() {  
  		
		TreeModelReader<List<FileTO>> treeModelReader = new TreeModelReader<List<FileTO>>();
		TreeLoader<FileTO> loader = new BaseTreeLoader<FileTO>(treeModelReader) {  
			public boolean hasChildren(FileTO parent) {  
				
				boolean isFolder = parent.isFolder();
				if (!isFolder) {
					return !parent.isLeaf();
				}else{
					return parent.hasChildren();
				}
			}  
		};

		storeTreePanel = new TreeStore<FileTO>(loader);
		
		storeTreePanel.add(root, true);
		fileExplorerTreePanel = new FileExplorerTreePanel(storeTreePanel);
	    fileExplorerTreePanel.setDisplayProperty("name");  
	    fileExplorerTreePanel.setSize(165, 360);
	    fileExplorerTreePanel.setStateful(true);
	    fileExplorerTreePanel.setTrackMouseOver(false);
	    
	    fileExplorerTreePanel.setIconProvider(new ModelIconProvider<FileTO>() {
		      public AbstractImagePrototype getIcon(FileTO model) {
		    	  
		    	  	boolean isFolder = model.isFolder();
					if (!isFolder) {
						return ICONS.file();
					}else{
						return ICONS.folder();
					}
		       }
		});
	    
	    fileExplorerTreePanel.addListener(Events.OnClick, new Listener<TreePanelEvent<FileTO>>() {
			
			public void handleEvent(TreePanelEvent<FileTO> be) {
				
				if (be.getNode() == null) return;
				
				ModelData model = be.getNode().getModel();
				boolean isFolder = (Boolean) model.get("isFolder");
				if (isFolder) {
					
					FileTO fileTO = (FileTO) be.getItem();
					if(submitExplorerNode == true){
						submitExplorerNode(fileTO, false, true);
					}
					treeGrid.getSelectionModel().deselectAll();
					submitExplorerNode = true;
				}
				archiveManagerButton.setEnabled(!isQuotaExeeded && isArchiveManagerButtonEnabled());
				cutButton.setEnabled(!isQuotaExeeded && !treeGrid.getSelectionModel().getSelectedItems().isEmpty());
				copyButton.setEnabled(!isQuotaExeeded && !treeGrid.getSelectionModel().getSelectedItems().isEmpty());
				deleteButton.setEnabled(!treeGrid.getSelectionModel().getSelectedItems().isEmpty());
				treeGrid.setContextMenu(null);
			}

		});
	    
	    fileExplorerTreePanel.addListener(Events.Expand, new Listener<TreePanelEvent<FileTO>>() {

			public void handleEvent(TreePanelEvent<FileTO> be) {
				ModelData model = be.getNode().getModel();
				if(submitExplorerNode == true){
					submitExplorerNode((FileTO)model, false, true);
				}
				addExpandedNode((FileTO)model);
				submitExplorerNode = true;
			}
			
		});
		
	    fileExplorerTreePanel.addListener(Events.Collapse, new Listener<TreePanelEvent<FileTO>>() {

			public void handleEvent(TreePanelEvent<FileTO> be) {
				ModelData model = be.getNode().getModel();
				removeExpandedNode((FileTO)model);
				treeGrid.setContextMenu(null);
			}
		});

	    ContentPanel cp = new ContentPanel();  
	    cp.setHeaderVisible(false);
	    cp.setSize(185, 360);  
	    cp.add(fileExplorerTreePanel);  
	    cp.setLayout(new FitLayout());  
	  
	    left.add(cp); 
	    
  	}

	private MenuItem createSubmitMenuItem() {
		
		MenuItem submit = new MenuItem();  
		submit.setId("submitJDFMenuItemIDFileExplorer");
	    submit.setText("Submit Job");  
	    AbstractImagePrototype submitJobIcon = IconHelper.createPath("resources/images/silk/submitjob.gif");
	    submit.setIcon(submitJobIcon);  
	    submit.addSelectionListener(new SelectionListener<MenuEvent>() {  
	    	public void componentSelected(MenuEvent ce) {  
	    		MessageBox.confirm("Submit JDF File", "Do you want to receive email notification?",  
	    				new Listener<MessageBoxEvent>() {  
		                
		        	  		public void handleEvent(MessageBoxEvent be) {  
		                  
		        		    	FileTO file = treeGrid.getSelectionModel().getSelectedItem();  
		        		        if (file != null) {  
		        		        	if (be.getButtonClicked().getItemId().equals(Dialog.YES)){  
		        		        		submitJob(file.getName(), true);
			        	  			}else{
			        	  				submitJob(file.getName(), false);
			        	  			}
		        		        }  
		        	  		}  
	    			}
	    		);  
	        }  
	    });
		return submit;
	}  
	
	private MenuItem createDownloadMenuItem() {
		
		MenuItem download = new MenuItem();  
	    download.setText("Download");  
	    AbstractImagePrototype submitJobIcon = IconHelper.createPath("resources/images/silk/download.gif");
	    download.setIcon(submitJobIcon);  
	    download.addSelectionListener(new SelectionListener<MenuEvent>() {  
	    
	    public void componentSelected(MenuEvent ce) {  

	    	FileTO file = treeGrid.getSelectionModel().getSelectedItem();  
	        if (file != null) {
				downloadActivation(file);
	        }  
	      }  
	    });
		return download;
	} 
	
	private MenuItem createCutMenuItem() {
		
		MenuItem cut = new MenuItem();  
		cut.setText("Cut");  
	    AbstractImagePrototype cutIcon = IconHelper.createPath("resources/images/silk/cut.gif");
	    cut.setIcon(cutIcon);  
	    cut.addSelectionListener(new SelectionListener<MenuEvent>() {  
	    
	    public void componentSelected(MenuEvent ce) {  

			List<FileTO> listFile = treeGrid.getSelectionModel().getSelectedItems();  
	  		if (!listFile.isEmpty()) {  
	  			cutFiles(listFile, rootTmp);
	  		}  
	      }  
	    });
		return cut;
	} 
	
	private MenuItem createCopyMenuItem() {
		
		MenuItem copy = new MenuItem();  
		copy.setText("Copy");
	    AbstractImagePrototype copyIcon = IconHelper.createPath("resources/images/silk/copy.gif");
	    copy.setIcon(copyIcon);  
	    copy.addSelectionListener(new SelectionListener<MenuEvent>() {  
	    
	    public void componentSelected(MenuEvent ce) {  
	    	
			List<FileTO> listFile = treeGrid.getSelectionModel().getSelectedItems();  
	  		if (!listFile.isEmpty()) {  
	  			copyFiles(listFile);
	  		}
	      }  
	    });
		return copy;
	} 
	
	private MenuItem createPasteMenuItem() {
		
		MenuItem paste = new MenuItem();  
		paste.setText("Paste");  
	    AbstractImagePrototype pasteIcon = IconHelper.createPath("resources/images/silk/paste.gif");
	    paste.setIcon(pasteIcon);  
	    paste.addSelectionListener(new SelectionListener<MenuEvent>() {  
	    	public void componentSelected(MenuEvent ce) {  
				submitPasteFile();
	      	}	  
	    });
		return paste;
	} 
	
	private MenuItem createCompressMenuItem() {
		
		MenuItem compress = new MenuItem();  
		compress.setText("Compress");  
	    AbstractImagePrototype compactIcon = IconHelper.createPath("resources/images/silk/compress.gif");
	    compress.setIcon(compactIcon);  
	    compress.addSelectionListener(new SelectionListener<MenuEvent>() {  
	    	public void componentSelected(MenuEvent ce) {  
	    		
	    		FileTO file = treeGrid.getSelectionModel().getSelectedItem();
	    		List<FileTO> list = new ArrayList<FileTO>();
	    		list.add(file);
		        if (file != null) {
					submitCompressFile(list);
		        } 
	      	}	  
	    });
		return compress;
	} 
	
	private MenuItem createDecompressMenuItem() {
		
		MenuItem compress = new MenuItem();  
		compress.setText("Decompress");  
	    AbstractImagePrototype compactIcon = IconHelper.createPath("resources/images/silk/decompress.gif");
	    compress.setIcon(compactIcon);  
	    compress.addSelectionListener(new SelectionListener<MenuEvent>() {  
	    	public void componentSelected(MenuEvent ce) {  
	    		
	    		FileTO file = treeGrid.getSelectionModel().getSelectedItem();  
		        if (file != null) {
					submitDecompressFile(file);
		        } 
	      	}	  
	    });
		return compress;
	}
	
	private MenuItem createDeleteMenuItem() {
		
		MenuItem delete = new MenuItem();  
		delete.setText("Delete");  
	    AbstractImagePrototype deleteIcon = IconHelper.createPath("resources/images/silk/delete.gif");
	    delete.setIcon(deleteIcon);  
	    delete.addSelectionListener(new SelectionListener<MenuEvent>() {  
	        @Override  
	        public void componentSelected(MenuEvent ce) {  
	          MessageBox.confirm("Delete File", "Do you really want to delete the selected files?",  
	              new Listener<MessageBoxEvent>() {  
	                
	        	  	public void handleEvent(MessageBoxEvent be) {  
	                  
	        	  		if (be.getButtonClicked().getItemId().equals(Dialog.YES)){  
	        	  			List<FileTO> listFiles = treeGrid.getSelectionModel().getSelectedItems();  
	        		        if (!listFiles.isEmpty()) {  
	        		        	submitDeleteFile(listFiles);
	        		        }
	        	  		}  
	                }  
	          });  
	        }  
	      });
		return delete;
	} 
	
	private MenuItem createNewFolderMenuItem() {
		
		MenuItem newFolder = new MenuItem();  
		newFolder.setText("New Folder");  
	    AbstractImagePrototype newFolderIcon = IconHelper.createPath("resources/images/silk/newFolder.gif");
	    newFolder.setIcon(newFolderIcon);  
	    
	    newFolder.addSelectionListener(new SelectionListener<MenuEvent>() {  
	        @Override  
	        public void componentSelected(MenuEvent ce) {  
	          MessageBox.prompt("New Folder", "Enter the new folder name:",  
	              new Listener<MessageBoxEvent>() {  
	                
	        	  	public void handleEvent(MessageBoxEvent be) {  
	                  
	        	  		if (be.getButtonClicked().getItemId().equals(Dialog.OK) && be.getValue() != null) {  
	        		        	createNewFolder(be.getValue());
	        	  		}  
	                }  
	          });  
	        }  
	      });
		return newFolder;
	}
	
	private MenuItem createRenameMenuItem() {
		
		MenuItem rename = new MenuItem();  
		rename.setText("Rename");  
		
		rename.addSelectionListener(new SelectionListener<MenuEvent>() {  
	        @Override  
	        public void componentSelected(MenuEvent ce) {  
	          MessageBox.prompt("Rename File", "Enter the new name:",  
	              new Listener<MessageBoxEvent>() {  
	                
	        	  	public void handleEvent(MessageBoxEvent be) {  
	                  
	        	  		if (be.getButtonClicked().getItemId().equals(Dialog.OK) && be.getValue() != null) {  
	        	  			FileTO file = treeGrid.getSelectionModel().getSelectedItem();  
	        		        if (file != null) {  
	        		        	renameFile(file, be.getValue());
	        		        }
	        	  		}  
	                }  
	          });  
	        }  
	      });
		return rename;
	}
	
  	private void createStoreGrid(List<ModelData> listFolders ,List<FileTO> listFiles) {

  		for (ModelData modelData : listFolders) {
  			FileTO fileTO = (FileTO) modelData;
			storeGrid.add(fileTO, false);
		}
  		
  		for (ModelData modelData : listFiles) {
  			FileTO fileTO = (FileTO) modelData;
			storeGrid.add(fileTO, false);
		}
	}
  	
	private void removeExpandedNode(FileTO fileTO) {
		List<String> newExpandedNodes = new ArrayList<String>();
		for (String location : expandedList) {
			if(!location.contains(fileTO.getLocation())){
				newExpandedNodes.add(location);
			}
		}
		this.expandedList = newExpandedNodes;
	}

	private void addExpandedNode(FileTO fileTO) {
		if(!this.expandedList.contains(fileTO.getLocation())){
			this.expandedList.add(fileTO.getLocation());
		}
	}

	private void updateNameNode(FileTO oldFileTO, String newName, String newLocation) {
		updateTreeNameNode(oldFileTO, newName, newLocation);
		updateStoreGrid(this.rootTmp.getChildren(), this.rootTmp.getListFiles());
	}
	
  	private void updateTreeNameNode(FileTO oldFileTO, String newName, String newLocation) {
  		
  		FileTO file = null;
  		if(oldFileTO.isFolder()){
  	  		
  			file = this.storeTreePanel.findModel(oldFileTO);
  	  		if(file != null){
  				file.setName(newName);
  				file.setLocation(newLocation);
  				this.storeTreePanel.update(file);
  				this.storeTreePanel.commitChanges();
  	  		}
  	  		
	  		for (ModelData model : this.rootTmp.getChildren()) {
				FileTO fileTO = (FileTO) model;
				if(fileTO.equals(oldFileTO)){
					fileTO.setName(newName);
					fileTO.setLocation(newLocation);
				}
			}
  		} else {
	  		for (FileTO fileTO : this.rootTmp.getListFiles()) {
				if(fileTO.equals(oldFileTO)){
					fileTO.setName(newName);
				}
			}
  		}
	}

  	private void updateLocation(FileTO fileTO) {
		this.location.setValue(fileTO.getLocation());
	}
  	
  	private void updateFileInfo(FileTO fileTO) {
  		List<FileTO> files = new LinkedList<FileTO>();
  		files.add(fileTO);
  		updateFileInfo(files);
  	}
  	
  	private void updateFileInfo(List<FileTO> files) {
  		this.fileInfoPanel.setFileInfo(files);
  	}

	private void expands(FileTO fileTO) {
		if (isExpanded(fileTO)) {
			submitExplorerNode = false;
			fileExplorerTreePanel.setExpanded(fileTO, true);
		}else{
			fileExplorerTreePanel.setExpanded(fileTO, false);
		}
		
		if(isRootTmp(fileTO)){
			this.rootTmp = fileTO;
		}
		
		List<ModelData> children = fileTO.getChildren();
		for (ModelData modelData : children) {
			
			FileTO file = (FileTO) modelData;
			if(file.isFolder()){
				expands(file);
			}
		}
	}
  	
  	private boolean isRootTmp(FileTO fileTO) {
		return this.rootTmp.getLocation().equals(fileTO.getLocation());
	}

	private boolean isExpanded(FileTO file) {
		return expandedList.contains(file.getLocation());
	}
	
	public void explorerNode(FileTO fileTO) {
		submitExplorerNode(fileTO, true, true);
	}
	
		
	private void updateNode(FileTO fileTO, boolean updateGrid) {
		updateTreeNode(fileTO, updateGrid);
		updateStoreGrid(rootTmp.getChildren(), rootTmp.getListFiles());
	}

	private void updateTreeNode(FileTO fileTO, boolean updateGrid) {
		List<FileTO> children = new ArrayList<FileTO>();
		
		FileTO file = this.storeTreePanel.findModel(fileTO);
		if(file != null){
			
			file.setListFiles(fileTO.getListFiles());
			
			for (ModelData model : fileTO.getChildren()) {
				FileTO childFile = this.storeTreePanel.findModel((FileTO) model);
				if(childFile != null){
					childFile.setHasChildren(((FileTO) model).hasChildren());
					this.storeTreePanel.update(childFile);
				}else{
					this.storeTreePanel.add(file, (FileTO) model, false);
					children.add((FileTO) model);
				}
			}
			this.storeTreePanel.commitChanges();
			file.addChildrens(children);
		}
		
		if(updateGrid || root.equals(rootTmp)){
			rootTmp = file;
		}
	}
	
	private void submitExplorerNode(FileTO fileTO, boolean checkQuota, final boolean updateGrid) {
		
		UserModel userModel = OurGridPortal.getUserModel();
		
		FileExplorerTO fileExplorerTO = new FileExplorerTO();
		fileExplorerTO.setExecutorName(CommonServiceConstants.FILE_EXPLORER_EXECUTOR);
		fileExplorerTO.setLocation(fileTO.getLocation());
		fileExplorerTO.setNodeName(fileTO.getName());
		fileExplorerTO.setUserName(userModel.getUserLogin());
		fileExplorerTO.setCheckQuota(checkQuota);
		
		OurGridPortalServerUtil.getInstance().execute(fileExplorerTO, new AsyncCallback<FileExplorerResponseTO>() {

			public void onFailure(Throwable caught) {}

			public void onSuccess(FileExplorerResponseTO result) {
				if(result.checkQuota()){
					if(result.isQuotaExed()){
						OurGridPortal.notifyQuota(result.getQuotaExceeded());
						OurGridPortal.desactivateSubmission();
						OurGridPortal.desactivateQuotaOperations();
						OurGridPortal.cancelJobs(result.getJobsRequestList());
					}else{
						OurGridPortal.setFristQuotaNotification(true);
						OurGridPortal.activateSubmission();
						OurGridPortal.activateQuotaOperations();
					}
					quota = result.getQuotaPercentage();
				}
				FileTO fileTO = result.getFileRoot();
				
				updateNode(fileTO, updateGrid);
				updateLocation(fileTO);
				updateFileInfo(fileTO);
				
				treeGrid.getSelectionModel().deselectAll();
				updateFileInfoBar(quota);
			}
		});
			
	}

  	protected void submitJob(String jobName, boolean isEmailNotification) {
  		
		if (jobName == null || jobName.length() == 0) {
			unmask();
			MessageBox.alert("Submission Error", JobSubmissionMessages.JDF_NOT_FOUND, null);
			return;
		}
		
		UserModel userModel = OurGridPortal.getUserModel();
		JobSubmissionTO jobSubmissionTO = FileExplorerUtil.createJobSubmissionTO(userModel.getUploadSessionId(), userModel.getUserLogin(), jobName, isEmailNotification, this.location.getValue());
		
		
		OurGridPortalServerUtil.getInstance().execute(jobSubmissionTO, new AsyncCallback<JobSubmissionResponseTO>() {

			public void onFailure(Throwable result) {
				unmaskMainPanel();
				MessageBox.alert("Submission Error", result.getMessage(), null);
			}

			public void onSuccess(JobSubmissionResponseTO result) {
				unmaskMainPanel();
				MessageBox.info("Submit jdf succeed", result.getMessage(), null);
				processJobSubmissionResponse(result);
			}
		});
	}
  	
  	protected void submitCompressFile(List<FileTO> fileTO) {
  		
  		UserModel userModel = OurGridPortal.getUserModel();
  		
		CompressFilesTO compressFileTO = FileExplorerUtil.createCompressFileTO(fileTO, userModel.getUserLogin());
		
		OurGridPortalServerUtil.getInstance().execute(compressFileTO, new AsyncCallback<CompressFilesResponseTO>() {

			public void onFailure(Throwable result) {
				unmaskMainPanel();
				MessageBox.alert("Compression Error", result.getMessage(), null);
			}
			
			public void onSuccess(CompressFilesResponseTO result) {
				if(result.isQuotaExed()){
					OurGridPortal.notifyQuota(result.getQuotaExceeded());
					OurGridPortal.desactivateSubmission();
					OurGridPortal.desactivateQuotaOperations();
					OurGridPortal.cancelJobs(result.getJobsRequestList());
				}else{
					OurGridPortal.setFristQuotaNotification(true);
					OurGridPortal.activateSubmission();
					OurGridPortal.activateQuotaOperations();
				}
				unmaskMainPanel();
				addNode(result.getFile());
				updateFileInfoBar(result.getQuotaPercentage());
			}
		});
	}
  	
  	protected void submitDecompressFile(FileTO fileTO) {
  		
  		UserModel userModel = OurGridPortal.getUserModel();
  		
		ExtractFilesTO extractFileTO = FileExplorerUtil.createExtractFileTO(fileTO, userModel.getUserLogin());
		
		OurGridPortalServerUtil.getInstance().execute(extractFileTO, new AsyncCallback<ResponseTO>() {

			public void onFailure(Throwable result) {
				unmaskMainPanel();
				MessageBox.alert("Decompression Error", result.getMessage(), null);
			}
			
			public void onSuccess(ResponseTO result) {
				submitExplorerNode(rootTmp, true, true);
			}
		});
	}

	protected void processJobSubmissionResponse(JobSubmissionResponseTO result) {
        
		Integer jobID = result.getJobID();
		UserModel userModel = OurGridPortal.getUserModel();
		int nextJobViewId = getNextJobViewId();
		
		userModel.addJobId(nextJobViewId, jobID);
		userModel.setPagedTaskIds(jobID, new LinkedList<Integer>());
		
		OurGridPortal.createAndAddJobStatusTab(nextJobViewId);
	}
	
	protected void maskMainPanel(String message) {
  		this.el().mask(message);
	}
  	
  	protected void unmaskMainPanel() {
		this.el().unmask();
	}
  	
	protected void downloadActivation(FileTO fileTO) {
		
		ResultTO resultTO = new ResultTO();
		resultTO.setText(fileTO.getName());
		resultTO.setUrl("download?filename=" + fileTO.getName() + "&location=" + this.location.getValue());
		resultTO.setName(resultTO.toString());
		
		String link = GWT.getModuleBaseURL() + resultTO.getUrl();
		com.google.gwt.user.client.Window.open(link, "", "");
	}
	
	public void refresh() {
		
		UserModel userModel = OurGridPortal.getUserModel();
		FileExplorerTO fileExplorerTO = new FileExplorerTO();
		fileExplorerTO.setExecutorName(CommonServiceConstants.REFRESH_FILE_EXPLORER_EXECUTOR);
		fileExplorerTO.setLocation(this.root.getLocation());
		fileExplorerTO.setUserName(userModel.getUserLogin());
		fileExplorerTO.setNodeName(this.root.getName());
		fileExplorerTO.setRootTmp(this.rootTmp);
		
		OurGridPortalServerUtil.getInstance().execute(fileExplorerTO, new AsyncCallback<FileExplorerResponseTO>() {

			public void onFailure(Throwable caught) {}

			public void onSuccess(FileExplorerResponseTO result) {
				if(result.isQuotaExed()){
					OurGridPortal.notifyQuota(result.getQuotaExceeded());
					OurGridPortal.desactivateSubmission();
					OurGridPortal.desactivateQuotaOperations();
					OurGridPortal.cancelJobs(result.getJobsRequestList());
				}else{
					OurGridPortal.setFristQuotaNotification(true);
					OurGridPortal.activateSubmission();
					OurGridPortal.activateQuotaOperations();
				}
				updateFileExplorerPanel(result.getFileRoot(), result.getFileRootTmp());
				updateFileInfoBar(result.getQuotaPercentage());
				
				List<FileTO> selection = new ArrayList<FileTO>();
				selection.add(result.getFileRootTmp());
				
				fileExplorerTreePanel.getSelectionModel().deselectAll();
				fileExplorerTreePanel.getSelectionModel().setSelection(selection);
			}
			
		});
	}
	
	public void updateFileExplorerPanel(FileTO root, FileTO rootTmp){
		this.root = root;
		this.rootTmp = rootTmp;
		updateFileExplorer(this.root, this.rootTmp);
		updateLocation(this.rootTmp);
		
		this.cutButton.setEnabled(!treeGrid.getSelectionModel().getSelectedItems().isEmpty());
		this.copyButton.setEnabled(!treeGrid.getSelectionModel().getSelectedItems().isEmpty());
		this.deleteButton.setEnabled(!treeGrid.getSelectionModel().getSelectedItems().isEmpty());
		this.archiveManagerButton.setEnabled(isArchiveManagerButtonEnabled());
	}
	
	public void updateFileExplorer(FileTO fileTO, FileTO rootTmp) {
		updateStoreTreeGrid(fileTO);
		updateStoreGrid(rootTmp.getChildren(), rootTmp.getListFiles());
	}  
	
  	private void updateStoreTreeGrid(FileTO fileTO) {
  		this.storeTreePanel.removeAll();
  		this.storeTreePanel.add(fileTO, true);
  		expands(fileTO);
	}
  	
  	private void updateStoreGrid(List<ModelData> listFolders, List<FileTO> listFiles) {
  		storeGrid.removeAll();
		createStoreGrid(listFolders, listFiles);
	}
	
	public void updateFileInfoBar() {
		updateFileInfoBar(this.quota);
	}
	
	public void updateFileInfoBar(Double quotaUsedPercentage) {
		
		List<FileTO> files = new LinkedList<FileTO>();
		files.add(this.root);
		this.fileInfoPanel.setFileInfo(files);
		
		this.fileInfoPanel.setFileInfo(files);
		this.fileInfoPanel.setQuotaInfo(quotaUsedPercentage);
	}
	
	private void goHome(){
		
		submitExplorerNode(this.root, false, true);
		
		List<FileTO> selection = new ArrayList<FileTO>();
		selection.add(this.root);
		
		this.fileExplorerTreePanel.getSelectionModel().deselectAll();
		this.fileExplorerTreePanel.getSelectionModel().setSelection(selection);
		
		this.cutButton.setEnabled(!treeGrid.getSelectionModel().getSelectedItems().isEmpty());
		this.copyButton.setEnabled(!treeGrid.getSelectionModel().getSelectedItems().isEmpty());
		this.deleteButton.setEnabled(!treeGrid.getSelectionModel().getSelectedItems().isEmpty());
		this.archiveManagerButton.setEnabled(isArchiveManagerButtonEnabled());
		
	}
	
	private boolean isArchiveManagerButtonEnabled(){
		return  isCompressButtonEnabled() || isDecompressButtonEnabled();
	}
	
	private boolean isCompressButtonEnabled(){
		return  treeGrid.getSelectionModel().getSelectedItems().size() == 1 
		     && treeGrid.getSelectionModel().getSelectedItem().isFolder();
	}
	
	private boolean isDecompressButtonEnabled(){
		return  treeGrid.getSelectionModel().getSelectedItems().size() == 1 
		     && !treeGrid.getSelectionModel().getSelectedItem().isFolder() && treeGrid.getSelectionModel().getSelectedItem().getName().endsWith(".zip");
	}
	
	private void uploadFile() {
		uploadFileWindow = new UploadFileWindow(this.rootTmp,false);
		uploadFileWindow.setVisible(true);
	}
	
	private void cutFiles(List<FileTO> list, FileTO source){
		
		this.cutSource = source;
		this.cutFileList.clear();
		this.cutFileList.addAll(list);
		
		this.copyFileList.clear();
		
		if(!cutFileList.isEmpty()){
			this.pasteButton.setEnabled(true && !isQuotaExeeded);
			createContextMenuNewFolder(true);
		}
	}
	
	private void copyFiles(List<FileTO> list){
		this.copyFileList.clear();
		this.copyFileList.addAll(list);
		
		this.cutFileList.clear();
		
		if(!copyFileList.isEmpty()){
			this.pasteButton.setEnabled(true && !isQuotaExeeded);
			createContextMenuNewFolder(true);
		}
	}
	
	protected void submitPasteFile() {
		
		UserModel userModel = OurGridPortal.getUserModel();
		
		PasteFileTO pasteFileTO = FileExplorerUtil.createPasteFileTO(rootTmp, cutSource, cutFileList, copyFileList, userModel.getUserLogin());
		
		OurGridPortalServerUtil.getInstance().execute(pasteFileTO, new AsyncCallback<PasteFileResponseTO>() {

			public void onFailure(Throwable caught) {
				MessageBox.alert("Paste File Error", caught.getMessage(), null);
			}

			@Override
			public void onSuccess(PasteFileResponseTO result) {
				unmaskMainPanel();
				refresh();
				cutFileList.clear();
				cutSource = null;
				boolean isPasteAction = !copyFileList.isEmpty() || !cutFileList.isEmpty(); 
				pasteButton.setEnabled(isPasteAction && !isQuotaExeeded);
				createContextMenuNewFolder(isPasteAction);
				
			}

		});
	}
	
	private void createNewFolder(String name){
		if (validateNewFolderName(name)){
			submitNewFolder(name);
		}
	}
	
	protected boolean validateNewFolderName(String name) {
		
		String valid = name.replaceAll(VType.NOT_PUNCT.getRegex(), "");
        if (!name.equals(valid)) {
        	MessageBox.alert("New Folder Failed", "The new folder name is not valid (invalid characters \" \\ / : * ? \" < > |).", null);
        	return false;
        }
		
		return true;
	}
	
	protected void submitNewFolder(String name) {
		
		NewFolderTO newFolderTO = FileExplorerUtil.createNewFolderTO(name, this.rootTmp);
		
		OurGridPortalServerUtil.getInstance().execute(newFolderTO, new AsyncCallback<NewFolderResponseTO>() {

			public void onFailure(Throwable caught) {
				MessageBox.alert("New Folder Error", caught.getMessage(), null);
			}

			@Override
			public void onSuccess(NewFolderResponseTO result) {
				addNode(result.getFile());
			}
			
		});
	}
	
	private void addNode(FileTO file) {

		if(file.isFolder()){
			this.rootTmp.add(file);
			this.rootTmp.setHasChildren(true);
			addTreeNode(this.rootTmp, file, false);
		}else{
			
			if(!rootTmp.getListFiles().contains(file)){
				rootTmp.getListFiles().add(file);
			}
		}

		updateStoreGrid(rootTmp.getChildren(), rootTmp.getListFiles());	
	}

	private void addTreeNode(FileTO root, FileTO fileTO, boolean addChildren) {
		FileTO parent = this.storeTreePanel.findModel(root);
		if(parent != null){
			this.storeTreePanel.add(parent, fileTO, addChildren);
		}
	}

	private void renameFile(FileTO file, String name) {
		if (validateNewName(name)){
			submitRenameFile(file, name);
		}
	}
	
	private boolean validateNewName(String name) {
		
		String valid = name.replaceAll(VType.NOT_PUNCT.getRegex(), "");
        if (!name.equals(valid)) {
        	MessageBox.alert("Rename File Failed", "The new name is not valid (invalid characters \" \\ / : * ? \" < > |).", null);
        	return false;
        }
		
		return true;
	}
	
	protected void submitRenameFile(FileTO fileTO,String name) {
		
		RenameFileTO renameFileTO = FileExplorerUtil.createRenameFileTO(fileTO, name);
		
		OurGridPortalServerUtil.getInstance().execute(renameFileTO, new AsyncCallback<RenameFileResponseTO>() {

			public void onFailure(Throwable caught) {
				MessageBox.alert("Rename File Error", caught.getMessage(), null);
			}

			@Override
			public void onSuccess(RenameFileResponseTO result) {
				updateNameNode(result.getOldFileTO(), result.getNewName(), result.getNewLocation());
				updateLocationNode(rootTmp);
			}

		});
	}
	
	private void updateLocationNode(FileTO fileTO) {
		
		if(fileTO !=  null){
			for (ModelData child : fileTO.getChildren()) {
				FileTO file = this.storeTreePanel.findModel((FileTO) child);
				file.setLocation(fileTO.getLocation() + file.getName() + "\\");
				this.storeTreePanel.update(file);
				updateLocationNode(file);
			}
		}
		storeTreePanel.commitChanges();
	}

	protected void submitDeleteFile(List<FileTO> listFile) {
		
		UserModel userModel = OurGridPortal.getUserModel();
		
		DeleteFileTO deleteFileTO = FileExplorerUtil.createDeleteFileTO(listFile, rootTmp, userModel.getUserLogin());
		
		OurGridPortalServerUtil.getInstance().execute(deleteFileTO, new AsyncCallback<DeleteFileResponseTO>() {

			public void onFailure(Throwable caught) {
				MessageBox.alert("Delete File Error", caught.getMessage(), null);
			}

			@Override
			public void onSuccess(DeleteFileResponseTO result) {
				if(!result.isQuotaExed()){
					OurGridPortal.setFristQuotaNotification(true);
					OurGridPortal.activateSubmission();
					OurGridPortal.activateQuotaOperations();
				}
				removeNodes(result.getListFiles(), rootTmp);
				updateFileInfoBar(result.getQuotaPercentage());
			}

		});
	}
	
	private void removeNodes(List<FileTO> listFiles, FileTO root) {
		removeTreeNodes(listFiles);
		removeGridNodes(listFiles, root);
	}

	private void removeTreeNodes(List<FileTO> listFiles) {
		for (FileTO fileTO : listFiles) {

			FileTO fileTree = this.storeTreePanel.findModel(fileTO);
			if(fileTree != null){
				this.storeTreePanel.remove(fileTree);
			}
		}
		this.storeTreePanel.commitChanges();
	}

	private void removeGridNodes(List<FileTO> listFiles, FileTO root) {
		for (FileTO fileTO : listFiles) {

			FileTO fileGrid = this.storeGrid.findModel(fileTO);
			if(fileGrid != null){
				this.storeGrid.remove(fileGrid);
				if(fileGrid.isFolder()){
					root.remove(fileGrid);
				}else{
					root.getListFiles().remove(fileGrid);
				}
			}
		}
		this.storeGrid.commitChanges();
	}

	public void closeWindow() {
		this.uploadFileWindow.closeWindow();
	}
	
	public void activateQuotaOperations() {
		
		this.isQuotaExeeded = false;
		
		boolean isPasteAction = !copyFileList.isEmpty() || !cutFileList.isEmpty();
		
		cutButton.setEnabled(!isQuotaExeeded);
		copyButton.setEnabled(!isQuotaExeeded);
		pasteButton.setEnabled(isPasteAction && !isQuotaExeeded);
		uploadButton.setEnabled(!isQuotaExeeded);
		archiveManagerButton.setEnabled(!isQuotaExeeded && isArchiveManagerButtonEnabled());
		
		createContextMenuGeneralOperationsForFiles();
		createContextMenuSubmitAndGeneralOperations();
		createContextMenuGeneralOperationsForAnyElements();
		createContextMenuGeneralOperationsForOneFolder();
		createContextMenuNewFolder(isPasteAction);
	}

	public void desactivateQuotaOperations() {
		
		this.isQuotaExeeded = true;
		
		cutButton.setEnabled(!isQuotaExeeded);
		copyButton.setEnabled(!isQuotaExeeded);
		pasteButton.setEnabled(!isQuotaExeeded);
		uploadButton.setEnabled(!isQuotaExeeded);
		archiveManagerButton.setEnabled(!isQuotaExeeded && isArchiveManagerButtonEnabled());
		
		createContextMenuGeneralOperationsForFiles();
		createContextMenuSubmitAndGeneralOperations();
		createContextMenuGeneralOperationsForAnyElements();
		createContextMenuGeneralOperationsForOneFolder();
		createContextMenuNewFolder(false);
	}

	public void expandRoot() {
		this.fileExplorerTreePanel.setExpanded(this.root, true);
	}
	
	public void refreshRoot(){
		submitExplorerNode(root, true, false);
	}
}