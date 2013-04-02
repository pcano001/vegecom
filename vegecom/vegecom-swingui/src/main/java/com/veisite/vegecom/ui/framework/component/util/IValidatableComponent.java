package com.veisite.vegecom.ui.framework.component.util;

import javax.validation.Validator;

public interface IValidatableComponent {
	
	/**
	 * Configura la configuración de la validación del component a partir de la api de 
	 * validacion javax.validation.
	 * 
	 * @param validator
	 * 	Objeto validator para hacer las validacines
	 * @param target
	 * 	Objeto sobre el que se hace la validación
	 * @param property
	 * 	Propiedad sobre la que se hace la validación
	 */
	public void configureValidation(Validator validator, Object target, String property);
	
	/**
	 * Activa las validaciones en el objeto
	 */
	public void enableValidation(boolean validationEnabled);
	
	/**
	 * Devuelve true si las validaciones están activas, false en otro caso.
	 */
	public boolean isValidationEnabled();
	
}
