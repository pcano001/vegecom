package com.veisite.vegecom.ui.framework.module;

import java.awt.Window;

import javax.validation.Validator;

import com.veisite.vegecom.ui.framework.UIFrameworkObject;

/**
 * Interfaz que debe implementar un modulo para poder 
 * integrarse en el framework grafico
 * 
 * @author josemaria
 *
 */
public interface UIFrameworkModule extends UIFrameworkObject {
	
	public void initModule() throws Throwable;
	
	public void disposeModule() throws Throwable;
	
	public String getMessage(String code, Object[] args, String defaultMessage);

	public Validator getValidator();

	public Window getParentWindow();
	
}
