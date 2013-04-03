package com.veisite.vegecom.ui;

import javax.swing.SwingUtilities;

import org.apache.log4j.Level;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;


public class DesktopApp {
	
	private static Logger logger = LoggerFactory.getLogger(DesktopApp.class);
	
	private VegecomUIInstance ui=null;
	
	private MessageSource messageSource;
	
	/**
	 * Establece si la aplicación está ejecutandose en modo de pruebas o
	 * real. Por defecto modo de pruebas.
	 */
	private boolean productionMode = false;
	private static final String PRODUCTIONMODE_STRING = "productionMode"; 

	public static void main(String[] args) {
		
		final DesktopApp app = new DesktopApp();

		// detectar si estamos ejecutando en modo producción o pruebas
		if (args.length>0 && args[0].equals(PRODUCTIONMODE_STRING))
			app.productionMode = true;
		logger.debug("Production mode is "+app.productionMode);
		
		// Configuramos logs
		app.configureLogLevels();
		
		// Establecemos el recurso de mensajes
		ResourceBundleMessageSource rbm = new ResourceBundleMessageSource();
		rbm.setBasename("i18n.client.messages");
		/* Inicializamos recurso de mensajes */
		app.setMessageSource(rbm);
		
		/* 
		 * iniciamos la interfaz de usuario
		 */
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
				    app.runDesktopApp();
				} catch (Throwable t) {
					logger.error("Unexpected error",t);
					ErrorInfo err = new ErrorInfo("Error", "Error inesperado", t.getMessage(), null, t, null, null);
					JXErrorPane.showDialog(null, err);
					app.exitDesktopApp(1);
				}
			}
		});
		
	}
	
	private void runDesktopApp() throws Throwable {
		logger.debug("Initializing main window");
		ui = new VegecomUIInstance("VegecomUIInstance", productionMode, getMessageSource());
		ui.setCallOnDispose(new Runnable() {
			@Override
			public void run() {
				exitDesktopApp(0);
			}
		});
		ui.start();
	}


	/**
	 * Configura los niveles de logs según el modo de ejecución de la 
	 * aplicación
	 */
	private void configureLogLevels() {
		if (isProductionMode()) {
			org.apache.log4j.Logger root = 
					org.apache.log4j.LogManager.getRootLogger();
			root.setLevel(Level.ERROR);
			org.apache.log4j.Logger appLogger = 
					org.apache.log4j.LogManager.getLogger("com.veisite.vegecom");
			appLogger.setLevel(Level.INFO);
		}
	}

	private void exitDesktopApp(int exitCode) {
		logger.info("Closing application with exit code {}", exitCode);
		System.exit(exitCode);
	}

	/**
	 * @return the productionMode
	 */
	public boolean isProductionMode() {
		return productionMode;
	}
	
	/**
	 * devuelve la fuente de mensajes de la aplicación.
	 */
	public MessageSource getMessageSource() {
		return messageSource;
	}
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

}
