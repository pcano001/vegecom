package com.veisite.vegecom.ui.framework.component.util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filtra las pulsaciones de teclado para que se ajusten a la expresión
 * regular suministrada. Siempre permite las teclas de borrado
 *  
 * @author josemaria
 *
 */
public class RegexFilterKeyListener extends KeyAdapter {
	
	private static Logger logger = LoggerFactory.getLogger(RegexFilterKeyListener.class); 
	
	private String regex;
	
	public RegexFilterKeyListener(String regex) {
		try {
			Pattern.compile(regex);
			this.regex = regex;
		} catch (PatternSyntaxException e) {
			logger.error("Expresión regular '{}' no es correcta",e);
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
		String s = new String(new char[] {c});
		if ( !((c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE) ||
			  s.matches(regex)) ) {
			e.consume();
		}
		super.keyTyped(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		super.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		super.keyReleased(e);
	}

	/**
	 * @return the regexPattern
	 */
	public String getRegex() {
		return regex;
	}

}
