package com.veisite.utils.binding;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.LoggerFactory;

public class BindTarget<SV> {

	/**
	 * logger
	 */
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(BindTarget.class);

	/**
	 * target object to be synchronized
	 */
	private Object target;
	
	/**
	 * target object to be synchronized
	 */
	private String property;
	
	/**
	 * Variable que controla la no entrada en recursividad
	 */
	private Boolean amISetting = false;
	
	/**
	 * 
	 * @param target
	 * @param property
	 */
	private Converter<SV, ?> converter;

	
	public BindTarget(Object target, String property) {
		super();
		this.target = target;
		this.property = property;
	}

	/**
	 * @return the target
	 */
	public Object getTarget() {
		return target;
	}

	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * Estable el valor en el destino
	 * @param newValue
	 */
	public void setValue(SV newValue) {
		if (amISetting) return;
		if (target!=null) {
			Object v;
			if (converter!=null) {
				v = converter.convert(newValue);
			} else v = newValue;
			if (amISetting) return;
			synchronized (amISetting) {
				if (amISetting) return;
				amISetting = true;
				try {
					PropertyUtils.setProperty(target, property, v);
				} catch (NoSuchMethodException e) {
					logger.error(
							"Error setting value on bind target: ObjectClass: {}, property: {}",
							target.getClass().getName(), property, e);
				} catch (IllegalAccessException e) {
					logger.error(
							"Error setting value on bind target: ObjectClass: {}, property: {}",
							target.getClass().getName(), property, e);
				} catch (InvocationTargetException e) {
					logger.error(
							"Error setting value on bind target: ObjectClass: {}, property: {}",
							target.getClass().getName(), property, e);
				}
				amISetting = false;
			}
		}
	}

	/**
	 * @return the converter
	 */
	public Converter<SV, ?> getConverter() {
		return converter;
	}

	/**
	 * @param converter the converter to set
	 */
	public void setConverter(Converter<SV, ?> converter) {
		this.converter = converter;
	}

}
