package com.veisite.vegecom.ui.framework.service;

import java.awt.Window;

import javax.validation.Validator;

import com.veisite.vegecom.ui.framework.UIFrameworkObject;

public interface UIFrameworkService extends UIFrameworkObject {

	public void initService() throws Throwable;
	
	public void disposeService();
	
	public String getMessage(String code, Object[] args, String defaultMessage);

	public Validator getValidator();
	
	public Window getParentWindow();
	
}
