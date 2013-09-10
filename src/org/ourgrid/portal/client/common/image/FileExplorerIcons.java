package org.ourgrid.portal.client.common.image;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

public interface FileExplorerIcons extends ImageBundle {

	@Resource("simpleFile.png")
	AbstractImagePrototype simpleFile();
	
	@Resource("emptyFolder.png")
	AbstractImagePrototype emptyFolder();
	
	@Resource("notEmptyFolder.png")
	AbstractImagePrototype notEmptyFolder();
}
