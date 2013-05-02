package com.veisite.vegecom.ui.error;

import org.jdesktop.swingx.error.ErrorInfo;
import org.springframework.core.NestedCheckedException;
import org.springframework.core.NestedRuntimeException;

public class ErrorUtil {

	/**
	 * Método estático que devuelve un objeto ErrInfo de ayuda
	 * para mostrar información del error en un interfaz gráfico.
	 * 
	 */
	public static ErrorInfo getErrorInfo(Throwable exception, String title, String message) {
		if (exception instanceof NestedRuntimeException ||
				exception instanceof NestedCheckedException) 
			return getErrorInfoNested((NestedRuntimeException) exception, title, message);
		String m;
		if (message!=null && !message.isEmpty()) m = message;
		else m = exception.getLocalizedMessage();
		String dm = getDetailedMessage(exception);
		ErrorInfo err = 
			new ErrorInfo(title==null? "Error" : title, 
						m, 
						dm, 
						null, 
						exception, 
						null, 
						null);
		return err;
	}
	
	/*
	 * Metodo que recibe una exception encadenada y devuelve el objeto de error
	 * El mensaje es unicamente el mensaje de error de la primera exception.
	 */
	private static ErrorInfo getErrorInfoNested(Throwable exception, String title, String message) {
		String m = "", dm;
		Throwable cause;
		if (message!=null && !message.isEmpty()) {
			m = message;
			dm=getDetailedMessage(exception);
			cause = exception.getCause();
		} else {
			m = getFirstNestedMessage(exception);
			dm=getDetailedMessage(exception.getCause());
			cause = exception.getCause();
			if (cause!=null) cause = cause.getCause();
		}
		ErrorInfo err = 
				new ErrorInfo(title==null? "Error" : title, 
							m, 
							dm, 
							null, 
							cause, 
							null, 
							null);
		return err;
	}
	
	/*
	 * Devuelve una cadena html con una linea por cada mensaje de error de cada una de
	 * las causas de error en la cadena de excepciones.
	 */
	private static String getDetailedMessage(Throwable exception) {
		String message = "<html><body>";
		Throwable it = exception;
		while (it!=null) {
			if (exception instanceof NestedCheckedException || 
					exception instanceof NestedRuntimeException)
				message += getFirstNestedMessage(it);
			else message += it.getLocalizedMessage();
			message += "<br/>";
			it = it.getCause();
		}
		message += "</body></html>";
		return message;
	}
	
	private static String getFirstNestedMessage(Throwable exception) {
		String s = exception.getLocalizedMessage();
		if (s==null) return "";
		int i = s.indexOf("; nested exception is");
		return i<0? s : s.substring(0, i);
	}
}
