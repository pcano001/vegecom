package com.veisite.vegecom.ui.tercero.cliente;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.veisite.vegecom.model.exception.VegecomException;
import com.veisite.vegecom.service.ClienteService;
import com.veisite.vegecom.ui.VegecomUIMenu;
import com.veisite.vegecom.ui.framework.UIFrameworkInstance;
import com.veisite.vegecom.ui.framework.menu.UIFrameworkMenu;
import com.veisite.vegecom.ui.framework.menu.UIFrameworkMenuBar;
import com.veisite.vegecom.ui.framework.menu.UIFrameworkMenuItem;
import com.veisite.vegecom.ui.framework.module.UIFrameworkAbstractModule;
import com.veisite.vegecom.ui.framework.util.UIResources;
import com.veisite.vegecom.ui.framework.views.UIFrameworkView;
import com.veisite.vegecom.ui.service.ClienteUIService;
import com.veisite.vegecom.ui.service.impl.ClienteUIServiceImpl;

public class ClienteUIModule extends UIFrameworkAbstractModule {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * module id
	 */
	public static String MODULE_ID = "clientesModule";
	
	/**
	 * menu
	 */
	public static String MENU_CUSTOMERLIST_ID = "customerListMenu";
	public static String MENU_NEWCUSTOMER_ID = "newCustomerMenu";
	
	/**
	 * Servicio de acceso a datos de cliente 
	 */
	private ClienteService clienteService=null;
	
	/**
	 * Recursos graficos del modulo.
	 */
	private UIFrameworkView clienteListView = null;

	/**
	 * Servicios graficos para clientes.
	 */
	private ClienteUIService clienteUIService = null;
	
	public ClienteUIModule(UIFrameworkInstance uiInstance,
			ApplicationContext context) {
		super(uiInstance, context);
	}

	@Override
	public String getId() {
		return MODULE_ID;
	}

	@Override
	public void initModule() throws Throwable {
		try {
			clienteService = this.context.getBean(ClienteService.class);
		} catch (Throwable t) {
			logger.error("Cannot get Customer Service",t);
			throw t;
		}
		if (clienteService==null) 
			throw new VegecomException("Cannot get Customer Service. Module cannot init");
		// Registra el servicio grafico para clientes
		clienteUIService = new ClienteUIServiceImpl(this, context);
		uiInstance.getServiceManager().registerService(clienteUIService);
		configureUI();
	}

	@Override
	public void disposeModule() throws Throwable {
	}

	/**
	 * Configura los elementos graficos que se integran en el framework
	 */
	private void configureUI() {
		UIFrameworkMenuBar menuBar = uiInstance.getUIFrameworkMenuBar();
		UIFrameworkMenu menu = menuBar.getMenu(VegecomUIMenu.MENU_SELL_ID);
		if (menu==null) {
			String m = uiInstance.getMessage("ui.ApplicationFrame.Menu.Sells", null, "Sells");
			menu = menuBar.addMenu(VegecomUIMenu.MENU_SELL_ID, m);
		}
		String m = uiInstance.getMessage("ui.ClientesModule.Menu.ClientesList", null, "Customers");
		UIFrameworkMenuItem mi = new UIFrameworkMenuItem(MENU_CUSTOMERLIST_ID,new ClienteListAction(m));
		menu.add(mi);
		
		menu = menuBar.getMenu(VegecomUIMenu.MENU_FILE_ID);
		if (menu!=null) {
			// Toomar menu nuevo
			UIFrameworkMenu newMenu = 
					menu.getMenu(VegecomUIMenu.MENUGROUP_FILE_NEWOPEN_ID, VegecomUIMenu.MENU_FILE_NEW_ID);
			if (newMenu==null) {
				m = uiInstance.getMessage("ui.components.menu.New", null, "New");
				newMenu = new UIFrameworkMenu(VegecomUIMenu.MENU_FILE_NEW_ID, m);
				menu.addGroup(VegecomUIMenu.MENUGROUP_FILE_NEWOPEN_ID, 0);
				menu.add(VegecomUIMenu.MENUGROUP_FILE_NEWOPEN_ID, newMenu);
			}
			m = uiInstance.getMessage("ui.ClientesModule.Menu.NewCliente", null, "Customer");
			mi = new UIFrameworkMenuItem(MENU_NEWCUSTOMER_ID, new NewClienteAction(m));
			newMenu.add(mi);
		}
	}
	
	
	private class ClienteListAction extends AbstractAction {
		/**
		 * serial
		 */
		private static final long serialVersionUID = 2006976827174012189L;

		public ClienteListAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (clienteListView==null) {
				try {
					ClienteListProvider lp = new ClienteListProvider(clienteService);
					clienteListView = new ClienteListPanel(clienteUIService,lp);
				} catch (VegecomException exception) {
					logger.error("Cannot make a clienteListPanel",exception);
					UIResources.showException(uiInstance, exception);
					return;
				}
			}
			uiInstance.getViewArea().addView(clienteListView);
		}
	}
	
	private class NewClienteAction extends AbstractAction {
		/**
		 * serial
		 */
		private static final long serialVersionUID = 6313251397994803529L;

		public NewClienteAction(String name) {
			super(name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			clienteUIService.newCliente(uiInstance);
		}
	}
	
}
