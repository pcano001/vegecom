package com.veisite.vegecom.ui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.DataAccessResourceFailureException;

import com.veisite.utils.tasks.ProgressEvent;
import com.veisite.utils.tasks.ProgressEventListener;
import com.veisite.utils.tasks.TaskException;
import com.veisite.vegecom.model.audit.AuditAction;
import com.veisite.vegecom.model.exception.VegecomException;
import com.veisite.vegecom.rest.security.RestLoginFailedException;
import com.veisite.vegecom.service.audit.AuditService;
import com.veisite.vegecom.service.security.SecurityService;
import com.veisite.vegecom.service.security.SessionExpirationListener;
import com.veisite.vegecom.ui.auth.LoginPanel;
import com.veisite.vegecom.ui.auth.LoginProcessListener;
import com.veisite.vegecom.ui.auth.LoginProcessManager;
import com.veisite.vegecom.ui.context.SpringContextLoader;
import com.veisite.vegecom.ui.error.ErrorUtil;
import com.veisite.vegecom.ui.framework.UIFrameworkInstance;
import com.veisite.vegecom.ui.framework.menu.UIFrameworkMenuBar;
import com.veisite.vegecom.ui.framework.menu.UIFrameworkMenuItem;
import com.veisite.vegecom.ui.framework.util.UIResources;
import com.veisite.vegecom.ui.framework.util.WindowUtilities;
import com.veisite.vegecom.ui.framework.views.UIFrameworkView;
import com.veisite.vegecom.ui.tercero.cliente.ClienteUIModule;

public class VegecomUIInstance extends UIFrameworkInstance {

	/**
	 * serial
	 */
	private static final long serialVersionUID = -7505820006968013851L;
	
	/**
	 * logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final Dimension DIMENSION_MAXIMA_JFRAME_PRINCIPAL = new Dimension(940, 680);
	
	/**
	 * 
	 * @param id
	 */
	private Boolean started = new Boolean(false);
	
	/**
	 * Componentes graficos para la autenticación y 
	 * arranque inicial
	 */
	private UIFrameworkView loginTabPanel = null;
	private LoginPanel loginPanel = null;
	private Cursor defaultCursor = null;
	
	/**
	 * Servicios necesarios
	 */
	private SecurityService ss = null;
	private AuditService as = null;
	
	/**
	 * instancia
	 */
	private UIFrameworkInstance thisFrame = this;
	
	/**
	 * Modo de ejecución. Por defecto test
	 */
	private boolean productionMode = false;

	/**
	 * 
	 * @param id
	 * 		the id of the instance
	 * @param resourceBundleMessageSource 
	 */
	public VegecomUIInstance(String id, boolean productionMode,	MessageSource messageSource) {
		super(id, messageSource);
		this.productionMode = productionMode;
		String title = getMessage("ui.ApplicationFrame.Title", null, "Vegecom Application");
		if (this.productionMode) title += 
				getMessage("ui.ApplicationFrame.TitleTestMode", null, " (Runnig in test mode)");
		setTitle(title);
		getViewArea().setPreferredSize(DIMENSION_MAXIMA_JFRAME_PRINCIPAL);
	}
	
	
	public void start() throws VegecomException {
		synchronized (started) {
			if (started) return;
			started = true;
		}
		// Iniciamos la aplicación. 
		// Primero añadimos menu minimalista con opción de salir
		createInitialMenuBar();
		initLoginPhase();
		pack();
		WindowUtilities.centerWindowsOnDesktop(this);
		setVisible(true);
		loginPanel.acquireFocus();
	}
	
	private void initLoginPhase() throws VegecomException {
		String lt = getMessage("ui.ApplicationFrame.LoginTabTitle", null, "Login");
		loginTabPanel = new UIFrameworkView("loginPanel",lt); 
		loginTabPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		String lp = getMessage("ui.ApplicationFrame.LoginPrompt", null, null);
		String lm = getMessage("ui.ApplicationFrame.ValidatingUser", null, null);
		loginPanel = new LoginPanel(lp,lm) {
			private static final long serialVersionUID = -2593647553937268205L;
			@Override
			public void okAction() {
				loginPanel.enableComponents(false);
				loginPanel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				Thread th = new Thread(new Runnable() {
					@Override
					public void run() {
						tryAuthentication();
					}
				});
				th.start();
			}
			@Override
			public void cancelAction() {
				cancelAuthentication();
			}
		};
		defaultCursor = loginPanel.getCursor();
		loginPanel.showLoginProgress(true);
		loginTabPanel.setLayout(new GridBagLayout());
		loginTabPanel.add(loginPanel, new GridBagConstraints());
		//loginTabPanel.setPreferredSize(DIMENSION_MAXIMA_JFRAME_PRINCIPAL);
		
		// Lanzamos configuracion de contexto.
		final String[] configSpringPaths = 
			{ "META-INF/spring/applicationContext.xml" };
		final SpringContextLoader cl = new SpringContextLoader(configSpringPaths);
		cl.addEventListener(new ProgressEventListener() {
			@Override
			public void taskReport(final ProgressEvent evt) {
				switch (evt.getType()) {
				case ProgressEvent.JOB_INIT:
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							String m = 
								getMessage("ui.ApplicationFrame.ConfigurationLoading",
										null, "Loading configuration...");
							loginPanel.setLoginMessages(m);
						}
					});
					break;
				case ProgressEvent.JOB_ERROR:
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							errorOnContextLoad(evt.getException());
						}
					});
					break;
				case ProgressEvent.JOB_END:
					if (cl.getContext()==null) {
						logger.error("Context has not been correctly loaded, exiting...");
						dispose();
					}
					setContext(cl.getContext());
					/* Inicializamos recurso de mensajes */
					setMessageSource((MessageSource) cl.getContext().getBean("messageSource"));
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							loginPanel.showLoginProgress(false);
						}
					});
				}
			}
		});
		getViewArea().addView(loginTabPanel);
		getRootPane().setDefaultButton(loginPanel.getOkButton());
		try {
			cl.start();
		} catch (TaskException te) {
			logger.error("Context has not been correctly loaded, exiting...",te);
			dispose();
		}
	}
	
	private void createInitialMenuBar() {
		UIFrameworkMenuBar menuBar = getUIFrameworkMenuBar();
		String fm = getMessage("ui.ApplicationFrame.Menu.File", null, "File");
		menuBar.addMenu(VegecomUIMenu.MENU_FILE_ID, fm);
		ExitAction ea = 
				new ExitAction(getMessage("ui.ApplicationFrame.Menu.File.Exit", null, "Exit"));
		UIFrameworkMenuItem mi = new UIFrameworkMenuItem("exitAction", ea);
		menuBar.addMenuItem(new String[] {VegecomUIMenu.MENU_FILE_ID}, "fileExitGroup", mi);
		menuBar.getMenu(VegecomUIMenu.MENU_FILE_ID).getGroup("fileExitGroup").setPriority(1024);
	}


	private void initApplicationPhase() throws VegecomException {
		// Eliminar panel de login.
		getViewArea().removeView(loginTabPanel);
		getRootPane().setDefaultButton(null);
		
		// Añadir modulo de clientes
		ClienteUIModule cm = new ClienteUIModule(this,getContext());
		try {
			moduleManager.installModule(cm);
		} catch (Throwable t) {
			logger.error("Error installing clientes module",t);
		}
		
		update();
	}


	private void tryAuthentication() {
		// Comprobamos que usuario y contraseña no son vacios
		String user = loginPanel.getUser();
		String passwd = loginPanel.getPassword();
		if (user==null || user.isEmpty() || passwd==null || passwd.isEmpty()) {
			String m = getMessage("ui.ApplicationFrame.IncorrentUserPassword", 
					null, "User and password must be not empty");
			loginFailed(this, user, m, null);
			return;
		}
		// Hay que esperar a que se carge la configuración completamente para continuar o cancelar
		try {
			if (!isApplicationReady()) waitForApplicationReady();
		} catch (final Throwable t) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					errorOnContextLoad(t);
				}
			});
			return;
		}
		
		// Recuperar servicios de seguridad
		if (ss==null)
			ss = getContext().getBean(SecurityService.class);
		try {
			if (as==null)
				as = getContext().getBean(AuditService.class);
		} catch (Throwable t) {
			logger.error("Cannot get Audit Service",t);
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				loginPanel.setLoginMessages(getMessage("ui.ApplicationFrame.ValidatingUser", 
						null, "Validating user..."));
				loginPanel.showLoginProgress(true);
			}
		});
		
		// Iniciar proceso de autenticacion
		LoginProcessManager loginProcess = new LoginProcessManager(ss);
		loginProcess.addConfigurationListener(new LoginProcessListener() {
			@Override
			public void init() {
			}
			@Override
			public void error(Throwable exception, String user) {
				loginFailed(thisFrame, user, exception);
				return;
			}
			@Override
			public void authenticated() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						loginPanel.showLoginProgress(false);
						loginPanel.enableComponents(true);
						authenticationSuccess();
					}
				});
			}
		});
		loginProcess.doBackgroundAuthentication(user,passwd);
	}

	
	/**
	 * Se llama cuando el usuario se ha autenticado correctamente 
	 */
	private void authenticationSuccess() {
		// Autenticación correcta.
		// Quedamos a la escucha de que la sesión de usuario expire
		SessionExpirationListener sel = new SessionExpirationListener() {
			@Override
			public void sessionExpired() {
				sessionExpirated();
			};
		};
		if (ss!=null) ss.addSessionExpirationListener(sel);
		// Auditemos el acceso a la aplicacion.
		if (as!=null) {
			String message = "Login ";
			InetAddress localHost;
			try {
				localHost = InetAddress.getLocalHost();
				if (localHost!=null) {
					message += "from "+localHost.getHostName()+
							", ip: "+localHost.getHostAddress()+" ";
					}
			} catch (UnknownHostException e) {
				message += " (unknown host)";
			}
			as.auditAction(AuditAction.LOGIN, getClass().getSimpleName(), null, message);
		}
		else logger.error("Cannot get AuditService");
		try {
			initApplicationPhase();
		} catch (VegecomException ge) {
			UIResources.showException(rootPane, ge);
			dispose();
		}
	}
	
	private void cancelAuthentication() {
		dispose();
	}
	
	private void errorOnContextLoad(Throwable exception) {
		String m = getMessage("ui.ApplicationFrame.ContextLoadError", null, 
				"Error initiating application");
		ErrorInfo err = new ErrorInfo("Error", m, exception.getMessage(), 
				null, exception, null, null);
		JXErrorPane.showDialog(this, err);
		dispose();
	}
	

	/**
	 * Metodo que hace una espera hasta que la aplicacion está lista,
	 * cuando el metodo isApplicatinReady devuelve true
	 * 
	 * Hace una espera máxima aproximada de 40 segundos. Si despues de 
	 * ese tiempo no está lista lanza una excepción
	 * 
	 * @throws Throwable
	 */
	private static long WAIT_TIMEOUT=40000L;
	private void waitForApplicationReady() throws Throwable {
		Object monitor = new Object();
		long timeout = WAIT_TIMEOUT;
		long itime = System.currentTimeMillis();
		synchronized (monitor) {
			do {
				if (isApplicationReady()) return;
				monitor.wait(100L);
			} while (System.currentTimeMillis()-itime < timeout);
		}
		String m = getMessage("ui.ApplicationFrame.ContextLoadTimeout", null, 
				"Too much time to init");
		throw new VegecomException(m);
	}
	
	private boolean isApplicationReady() {
		return getContext()!=null;
	}
	
	/**
	 * Se llama cuando no se valida al usuario y contraseña. Puede llamarse 
	 * desde distintos thread
	 */
	private void loginFailed(final Component parent, String user, final Throwable exception) {
		// Ver el tipo de excepcion. Si es de conexion al servidor, avisar, si no mensaje
		// generico
		String m = getMessage("ui.ApplicationFrame.LoginProcessError", null, 
				"Login process cuold not be successfully completed.");
		if (exception instanceof RestLoginFailedException) {
			logger.error("Authentication error for user '{}'",loginPanel.getUser(),exception);
			m = getMessage("ui.ApplicationFrame.InvalidCredential", null, 
					"Invalid credentials, not authorized or authentication server unavailable.");
		}
		if (exception instanceof DataAccessResourceFailureException) {
			logger.error("Error trying to validate user '{}'.",loginPanel.getUser(),exception);
			m = getMessage("ui.ApplicationFrame.LoginResourceAccessError", null, 
					"Login failed. Error connecting to server.");
		}
		loginFailed(parent, user, m, exception);
		
	}

	private void loginFailed(final Component parent, String user, final String message, Throwable exception) {
		// Registramos el intento fallido de acceso
		if (as!=null) {
			String am = "Login error. ";
			am += "user: "+user;
			InetAddress localHost;
			try {
				localHost = InetAddress.getLocalHost();
				if (localHost!=null) {
					am += ", pc: "+localHost.getHostName()+
							", ip: "+localHost.getHostAddress()+" ";
					}
			} catch (UnknownHostException e) {
				am += " (unknown host) ";
			}
			am += "-- "+message;
			as.auditAction(AuditAction.LOGIN_FAIL, this.getClass().getSimpleName(), null, am);
		}
		else logger.error("Cannot get AuditService");
		Runnable toRun;
		final String title = getMessage("ui.ApplicationFrame.LoginErrorTitle", null, 
				"Login error");
		if (exception==null) {
			toRun = new Runnable() {
				@Override
				public void run() {
					JOptionPane.showMessageDialog(parent, message,
							 title, 
							 JOptionPane.ERROR_MESSAGE);
					loginPanel.setCursor(defaultCursor);
					loginPanel.showLoginProgress(false);
					loginPanel.enableComponents(true);
					loginPanel.acquireFocus();
				}
			};
		} else {
			if (exception instanceof NestedRuntimeException)
				exception = ((NestedRuntimeException)exception).getMostSpecificCause();
			final ErrorInfo err = ErrorUtil.getErrorInfo(exception, title, message);
			toRun = new Runnable() {
				@Override
				public void run() {
					JXErrorPane.showDialog(parent, err);
					loginPanel.setCursor(defaultCursor);
					loginPanel.showLoginProgress(false);
					loginPanel.enableComponents(true);
					loginPanel.acquireFocus();
				}
			};
		}
		SwingUtilities.invokeLater(toRun);
	}
	

	/**
	 * Metodo llamado cuando caduca la sesion. Puede ser llamado desde distintos
	 * threads.
	 */
	private void sessionExpirated() { 
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				String m = getMessage("ui.ApplicationFrame.SessionCaducatedMessage", null, 
						"Session has caducated. Application will be closed");
				String t = getMessage("ui.ApplicationFrame.SessionCaducatedTitle", null, 
						"Session caducated");
				JOptionPane.showMessageDialog(thisFrame, m, t, JOptionPane.WARNING_MESSAGE);
				dispose();
			}
		});
	}
	
	
	
	
	private class ExitAction extends AbstractAction {
		/**
		 * serial 
		 */
		private static final long serialVersionUID = -6026620838396435310L;

		ExitAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
			
		}
	}
	
}
