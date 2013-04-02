package com.veisite.vegecom.ui;

import javax.swing.SwingUtilities;

import org.apache.log4j.Level;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ResourceBundleMessageSource;


public class DesktopApp {
	
	private static Logger logger = LoggerFactory.getLogger(DesktopApp.class);
	
	private static VegecomUIInstance ui=null;
	
	/**
	 * Establece si la aplicación está ejecutandose en modo de pruebas o
	 * real. Por defecto modo de pruebas.
	 */
	private static boolean productionMode = false;
	private static final String PRODUCTIONMODE_STRING = "productionMode"; 

	public static void main(String[] args) {

		// detectar si estamos ejecutando en modo producción o pruebas
		if (args.length>0 && args[0].equals(PRODUCTIONMODE_STRING))
			DesktopApp.productionMode = true;
		logger.debug("Production mode is "+productionMode);
		
		// Configuramos logs
		configureLogLevels();
		
		// Establecemos el recurso de mensajes
		ResourceBundleMessageSource rbm = new ResourceBundleMessageSource();
		rbm.setBasename("i18n.client.messages");
		/* Inicializamos recurso de mensajes */
		setResourceBundle(rbm);
		
		/* 
		 * iniciamos la interfaz de usuario
		 */
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
				    runDesktopApp();
				} catch (Throwable t) {
					logger.error("Unexpected error",t);
					ErrorInfo err = new ErrorInfo("Error", "Error inesperado", t.getMessage(), null, t, null, null);
					JXErrorPane.showDialog(null, err);
					exitDesktopApp(1);
				}
			}
		});
		
	}
	
	private static void runDesktopApp() throws Throwable {
		logger.debug("Initializing main window");
		ui = new VegecomUIInstance("VegecomUIInstance", productionMode, getResourceBundle());
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
	private static void configureLogLevels() {
		if (isProductionMode()) {
			org.apache.log4j.Logger root = 
					org.apache.log4j.LogManager.getRootLogger();
			root.setLevel(Level.ERROR);
			org.apache.log4j.Logger appLogger = 
					org.apache.log4j.LogManager.getLogger("com.veisite.vegecom");
			appLogger.setLevel(Level.INFO);
		}
	}

	private static void exitDesktopApp(int exitCode) {
		logger.info("Closing application with exit code {}", exitCode);
		System.exit(exitCode);
	}

	/**
	 * @return the productionMode
	 */
	public static boolean isProductionMode() {
		return productionMode;
	}

	/**
	 * Metodo que devuelve false o una excepcion si la aplicación
	 * no está operativa por algún motivo
	 * True si la aplicacion está lista para su uso y tiene
	 * conexión con la base de datos.
	 * 
	 * @throws Throwable
	 */
	public static boolean isApplicationReady() throws Throwable {
		return getContext()!=null;
	}
	
}
