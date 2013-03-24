package com.veisite.utils;

public class StringUtil {
	
	public static String quitaAcentos(String input) {
	    // Cadena de caracteres original a sustituir.
	    String original = "áéíóúÁÉÍÓÚ";
	    // Cadena de caracteres ASCII que reemplazarán los originales.
	    String ascii = "aeiouAEIOU";
	    String output = input;
	    for (int i=0; i<original.length(); i++) {
	        // Reemplazamos los acentos.
	        output = output.replace(original.charAt(i), ascii.charAt(i));
	    }
	    return output;
	}

}
