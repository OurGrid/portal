package org.ourgrid.portal.client.common.image;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

public interface JobStatusTreeIcons extends ImageBundle {

	  @Resource("ready.gif")
	  AbstractImagePrototype unstarted();
	  
	  @Resource("running.gif")
	  AbstractImagePrototype running();
	  
	  @Resource("finished.gif")
	  AbstractImagePrototype finished();
	  
	  @Resource("error.gif")
	  AbstractImagePrototype failed();
	  
	  @Resource("cancelled.gif")
	  AbstractImagePrototype cancelled();
	  
	  @Resource("aborted.gif")
	  AbstractImagePrototype aborted();
	  
	  @Resource("wait.gif")
	  AbstractImagePrototype loading();
	  
	  @Resource("download.gif")
	  AbstractImagePrototype download();
	  
	  @Resource("folder.gif")
	  AbstractImagePrototype folder();
	  
	  @Resource("file.gif")
	  AbstractImagePrototype file();
}
