package org.ourgrid.portal.client.common.to.model;

import java.io.Serializable;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;

public class AbstractTreeNodeTO extends BaseTreeModel implements Serializable  {

	private static final long serialVersionUID = -8472440531517374465L;
	
	public static final String name = "name";
	public static final String description = "description";
	public static final String type = "type";
	
	public static final String id = "id";
	
	public static final String expanded = "expanded";
	
	public AbstractTreeNodeTO() {
		super();
		set(AbstractTreeNodeTO.expanded, false);
	}
	
	public void setName(String name) {
		set(AbstractTreeNodeTO.name, name);
	}
	
	public void setDescription(String description) {
		set(AbstractTreeNodeTO.description, description);
	}
	
	public void setType(String type) {
		set(AbstractTreeNodeTO.type, type);
	}
	
	public void setId(Object id) {
		set(AbstractTreeNodeTO.id, id);
	}
	
	public void setExpanded(boolean expanded) {
		set(AbstractTreeNodeTO.expanded, expanded);
	}

	public boolean isExpanded() {
		return get(AbstractTreeNodeTO.expanded);
	}
	
	public String getType() {
		return get(AbstractTreeNodeTO.type);
	}
	
	public AbstractTreeNodeTO getChild(Object childId) {
		for (ModelData child : getChildren()) {
			AbstractTreeNodeTO nodeChild = (AbstractTreeNodeTO) child;
			if (nodeChild.get(id).equals(childId)) {
				return nodeChild;
			}
		}
		return null;
	}

}