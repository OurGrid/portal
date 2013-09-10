package org.ourgrid.portal.client.common.gui;

import org.ourgrid.portal.client.common.to.model.AbstractTreeNodeTO;

import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;

public class JobTreePanel extends TreePanel<AbstractTreeNodeTO> {

	public JobTreePanel(TreeStore<AbstractTreeNodeTO> store) {
		super(store);
	}

	protected void refresh(AbstractTreeNodeTO model) {
		super.refresh(model);
	}
	
}
