package com.veisite.vegecom.ui.framework.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UIResources {
	
	private static Logger logger = LoggerFactory.getLogger(UIResources.class);
	
	private static String iconsPath = "images/icons/";  

	/**
	 * Devuelve un panel compuesto por una etiqueta con el texto pasado
	 *  y con el componente al lado o debajo segun indique el parametro
	 *  vertical. El teto de la etiqueta se pone en negrita s se indica
	 * @param label
	 * 		texto de la etieuta que acompaña al componente
	 * @param c
	 * 		componente 
	 * @param vertical
	 * 		si la disposición de la etiqueta y el componente es en 
	 * 		vertical u horizontal (false)
	 * @param negrita
	 * 		si true la fuente de la etiqueta se establece en negrita.
	 * @return
	 */
	public static JPanel getLabeledComponent(String label, JComponent c,
			boolean vertical, boolean negrita) {
		JPanel p = new JPanel();
		BoxLayout bl = new BoxLayout(p, 
				vertical ? BoxLayout.PAGE_AXIS : BoxLayout.LINE_AXIS);
		p.setLayout(bl);
		JLabel jlabel = new JLabel(label+" ");
		Font f = jlabel.getFont();
		int fs = f.getSize();
		if (negrita)
			jlabel.setFont(new Font(f.getName(), Font.BOLD, fs));
		jlabel.setAlignmentY(0.95F);
		jlabel.setAlignmentX(0.0F);
		p.add(jlabel);
		c.setAlignmentY(0.85F);
		c.setAlignmentX(0.0F);
		p.add(c);
		int w = p.getPreferredSize().width;
		int h = p.getPreferredSize().height;
		p.setMinimumSize(new Dimension(w,h));
		p.setMaximumSize(new Dimension(w,h));
		return p;
	}
	

	public static JPanel getLabeledComponent(String label, JComponent c) {
		return getLabeledComponent(label, c, true, false);
	}
	
	public static JPanel getLabeledComponent(String label, JComponent c,
			boolean vertical) {
		return getLabeledComponent(label, c, vertical, false);
	}
	
	public static void centerWindow(java.awt.Window window) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		window.setLocation((screenWidth-window.getWidth()) / 2, (screenHeight-window.getHeight()) / 2);
	}
	
	private static ImageIcon getIcon(String name, String size) {
		String path = iconsPath + name + size + ".png";
		URL url = path.getClass().getResource(path);
		if (url != null) return new ImageIcon(url);
		url = path.getClass().getResource("/"+path);
		if (url != null) return new ImageIcon(url);
		ClassLoader loader = UIResources.class.getClassLoader();
		if(loader != null) url = loader.getResource(path);
		if(url == null) url = loader.getResource("/"+path);
		if(url != null) return new ImageIcon(url);
		logger.error("El icono solicitado no se encuentra: {}",path);
		return null;
	}
	
	public static ImageIcon getIcon16(String name) {
		return getIcon(name,"16");
	}
	
	public static ImageIcon getIcon24(String name) {
		return getIcon(name,"24");
	}

	public static ImageIcon getIcon32(String name) {
		return getIcon(name,"32");
	}
	
	public static ImageIcon getIcon48(String name) {
		return getIcon(name,"48");
	}
	
	public static ImageIcon getIcon64(String name) {
		return getIcon(name,"64");
	}
	
	public static ImageIcon getIcon128(String name) {
		return getIcon(name,"128");
	}
	
	/**
	 * Metodo que devuelve un panel con un icono y mensaje de error
	 * Se utiliza cuando se detecta un error en la inicialización de algun
	 * componente grafico.
	 * 
	 * @return
	 */
	public static JPanel getErrorPanel(String mensaje) {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    p.add(new JLabel(UIResources.getIcon48("face-sick")));
	    JLabel label = new JLabel((mensaje==null ? "Ha ocurrido un error en la aplicación." : mensaje));
	    Font f = new Font(label.getFont().getName(),Font.BOLD,label.getFont().getSize()+3);
	    label.setFont(f);
	    p.add(label);
		return p;
	}
	
	
	/**
	 * Metodo que toma una excepción y la muestra en un ventana.
	 * 
	 */
	public static void showException(Component owner, Throwable t) {
		String dm = t.getCause()!=null ? t.getCause().getMessage() : t.getMessage();
		ErrorInfo err = new ErrorInfo("Error", t.getMessage(), dm, null, t, null, null);
		JXErrorPane.showDialog(owner, err);
	}
	
	/**
	 * Metodo que toma una excepción y la muestra en un ventana.
	 * 
	 */
	public static void showException(Component owner, Throwable t, String message) {
		ErrorInfo err = new ErrorInfo("Error", message, t.getMessage(), null, t, null, null);
		JXErrorPane.showDialog(owner, err);
	}
	
}
