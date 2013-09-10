package org.ourgrid.portal.client.common.util;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;

public class VTypeValidator implements Validator {

	private VType type;
	  
	public VTypeValidator(VType type){
		this.type = type;
	}
	
	@Override
	public String validate(Field<?> field, String value) {
	
		String res = null;
	    if (!value.matches(type.regex)) {
	      res = value + "isn't a valid " + type.name;
	    }
	    return res;
	}
}
