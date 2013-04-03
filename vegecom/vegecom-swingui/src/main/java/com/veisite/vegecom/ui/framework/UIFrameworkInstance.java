package com.veisite.vegecom.ui.framework;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.veisite.vegecom.ui.framework.menu.UIFrameworkMenuBar;
import com.veisite.vegecom.ui.framework.module.UIFrameworkModuleManager;
import com.veisite.vegecom.ui.framework.service.UIFrameworkServiceManager;
import com.veisite.vegecom.ui.framework.statusbar.UIFrameworkStatusBar;
import com.veisite.vegecom.ui.framework.views.UIFrameworkViewArea;

/**
 * Proporciona un framework gráfico de componentes a aplicaciones
 * que les facilita la integracion en el marco común.
 * 
 * Proporciona los siguientes servicios:
 * 	Una barra de menu de aplicación donde los componentes puede registrar
 * sus elementos de menu y sus opciones de menu dentro los elementos.
 * 
 * 	Un 
 * 
 * @author josemaria
 *
 */
public class UIFrameworkInstance extends JFrame implements WindowListener {

	/**
	 * serial
	 */
	private static final long serialVersionUID = -5770455349216954841L;
	
	/**
	 * Metodo que se ejecuta cuando se cierra el framework.
	 * Puede ser establecido por los llamantes
	 */
	private Runnable callOnDispose = null;
	
	/**
	 * Cadena de texto con el identificador de la instancia
	 */
	private String id;
	
	/**
	 * Recurso para internacionalizacion
	 */
	protected MessageSource messageSource;
	
	/**
	 * Barra de menu principal
	 */
	protected UIFrameworkMenuBar menuBar;

	/**
	 * Barra de estado
	 */
	protected UIFrameworkStatusBar statusBar;

	/**
	 * Gestor de elementos de vistas, un tabbedPane
	 */
	protected UIFrameworkViewArea viewArea;

	/**
	 * Gestor de modulos 
	 */
	protected UIFrameworkModuleManager moduleManager;

	/**
	 * Gestor de servicios 
	 */
	protected UIFrameworkServiceManager serviceManager;

	/**
	 * Contexto de la aplicación
	 * 
	 * @param id
	 */
	protected ApplicationContext context = null;
	
	/**
	 * Validator por defecto
	 */
	protected Validator validator;
	
	
	public UIFrameworkInstance(String id, MessageSource messageSource) {
		super();
		this.id = id;
		this.messageSource = messageSource;
		initFramework();
	}
	
	protected void initFramework() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		addWindowListener(this);	
		// Crea los recursos gráficos.
		
		// Crear la barra de menu vacía, 
		menuBar = new UIFrameworkMenuBar("defaultMenuBar");
		setJMenuBar(menuBar);
		menuBar.addPropertyChangeListener(UIFrameworkMenuBar.CHANGED_PROPERTY, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				update();
			}
		});
		
		// Crea una barra de estado 
		statusBar = new UIFrameworkStatusBar();
		getContentPane().add(statusBar, BorderLayout.SOUTH);
		
		// Creamos un area de vistas en el centro. Es un tabbedPane 
		viewArea = new UIFrameworkViewArea("defualtViewArea");
		getContentPane().add(viewArea, BorderLayout.CENTER);
		
		// Creamos el gestor de módulos por defecto
		moduleManager = new UIFrameworkModuleManager("moduleManager");
		
		// Creamos el gestor de servicios por defecto
		serviceManager = new UIFrameworkServiceManager("serviceManager");
		
	}

	/* (non-Javadoc)
	 * @see java.awt.Window#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		if (callOnDispose!=null) callOnDispose.run();
	}
	
	
	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		dispose();
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the callOnDispose
	 */
	public Runnable getCallOnDispose() {
		return callOnDispose;
	}

	/**
	 * @param callOnDispose the callOnDispose to set
	 */
	public void setCallOnDispose(Runnable callOnDispose) {
		this.callOnDispose = callOnDispose;
	}

	/**
	 * @return the menuBar
	 */
	public UIFrameworkMenuBar getUIFrameworkMenuBar() {
		return menuBar;
	}

	/**
	 * @return the statusBar
	 */
	public UIFrameworkStatusBar getStatusBar() {
		return statusBar;
	}

	/**
	 * @return the viewArea
	 */
	public UIFrameworkViewArea getViewArea() {
		return viewArea;
	}

	/**
	 * @return the context
	 */
	public ApplicationContext getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @return the moduleManager
	 */
	public UIFrameworkModuleManager getModuleManager() {
		return moduleManager;
	}

	/**
	 * @return the serviceManager
	 */
	public UIFrameworkServiceManager getServiceManager() {
		return serviceManager;
	}

	/**
	 * Actualiza graficamente el framework
	 * La actualización la realiza siempre en el thread de eventos
	 */
	public void update() {
		if (SwingUtilities.isEventDispatchThread()) {
			pack();
			invalidate();
			repaint();
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					pack();
					invalidate();
					repaint();
				}
			});
		}
	}

	public String getMessage(String code, Object[] args, String defaultMessage) {
		return messageSource.getMessage(code, args, defaultMessage, Locale.getDefault());
	}

	/**
	 * Devuelve el validador por defecto.
	 */
	public Validator getValidator() {
		if (validator==null) {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	        validator = factory.getValidator();
		}
		return validator;
	}
	
}
