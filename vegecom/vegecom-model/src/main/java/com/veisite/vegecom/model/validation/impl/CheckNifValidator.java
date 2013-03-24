package com.veisite.vegecom.model.validation.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.veisite.vegecom.model.validation.CheckNif;

public class CheckNifValidator implements ConstraintValidator<CheckNif, String> {
	
	/**
	 * Expresion regular de formato de Nif español válido
	 */
	private static final String pattern_es = "[0-9A-Z]{1}[0-9]{7}[0-9A-Z]{1}";
	/**
	 * Secuencia de caracteres de control para la letra del NIF de persona fisica. 
	 * a partir de DNI,NIE
	 */
	private static final String letraDni = "TRWAGMYFPDXBNJZSQVHLCKE";
	/**
	 * Secuencia de caracteres para digito de control
	 */
	private static final String letraDigitoControl = "JABCDEFGHI";


	@Override
	public void initialize(CheckNif constraintAnnotation) {
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value==null || value.isEmpty()) return true;
		String v = value.toUpperCase();
		if (!v.matches(pattern_es)) return false;
		String c1 = v.substring(0, 1); 
		if (c1.matches("[0-9XYZ]{1}")) {
			// Es un NIF de persona física a partir de DNI o NIE. Calcular letra de control
			String c8 = v.substring(0, 8);
			if (c1.matches("XYZ")) {
				c8.replace('X', '0');
				c8.replace('Y', '1');
				c8.replace('Z', '2');
			}
			char letra = letraDni.charAt(Integer.parseInt(c8) % 23);
			if (letra!=v.charAt(8)) {
	            context.disableDefaultConstraintViolation();
	            context.buildConstraintViolationWithTemplate("{com.veisite.constraints.CheckNif.IncorrectLetter}")
	            	.addConstraintViolation();
	            return false;
			} else return true;
		}
		// El cif empieza por letra distinta de XYZ, calcular digito control
		int dc = getDigitoControl(v.substring(1,8));
		if (c1.matches("[PQRSNW]{1}")) {
			// Tomar letra de control
			char letra = letraDigitoControl.charAt(dc);
			if (letra!=v.charAt(8)) {
	            context.disableDefaultConstraintViolation();
	            context.buildConstraintViolationWithTemplate("{com.veisite.constraints.CheckNif.IncorrectLetter}")
	            	.addConstraintViolation();
	            return false;
			} else return true;
		}
		char digito = Character.forDigit(dc, 10);
		if (digito!=v.charAt(8)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{com.veisite.constraints.CheckNif.IncorrectDigit}")
            	.addConstraintViolation();
            return false;
		} else return true;
	}
	
	
	/**
	 * Devuelve el digito de control para los nif que no se calculan a partir de dni o nie
	 * El digito de control está entre 0 y 9.
	 * El número debe ser de siete caracteres 
	 */
	private int getDigitoControl(String number) {
		int suma = 0;
		for (int i=0;i<=6;i+=2) {
			int d = getDigit(number, i) * 2;
			suma += (d % 10) + (d / 10);
		}
		suma += getDigit(number,1)+getDigit(number,3)+getDigit(number,5);
		int dc = 10 - (suma % 10);
		return dc % 10;
	}
	
	/**
	 * Devuelve como entero el valor del digito en la posicion indicada en l string
	 */
	public int getDigit(String number, int position) {
		if (number!=null && position>=0 && position<number.length()) {
			char c = number.charAt(position);
			if (c>='0' && c<='9') {
				return (c-'0');
			}
		}
		return 0;
	}

}
