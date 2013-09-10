package org.ourgrid.portal.client.common.gui;

import org.ourgrid.portal.client.common.to.model.FileTO;

import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;

public class FileExplorerTreePanel extends TreePanel<FileTO> {

	public FileExplorerTreePanel(TreeStore<FileTO> store) {
		super(store);
	}

	protected void refresh(FileTO model) {
		super.refresh(model);
	}
	
}