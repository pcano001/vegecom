package com.veisite.vegecom.ui.service.impl;

import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.util.List;

import javax.swing.JOptionPane;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import com.veisite.vegecom.model.Cliente;
import com.veisite.vegecom.model.exception.VegecomException;
import com.veisite.vegecom.service.ClienteService;
import com.veisite.vegecom.service.DataChangeListener;
import com.veisite.vegecom.ui.framework.module.UIFrameworkModule;
import com.veisite.vegecom.ui.framework.service.UIFrameworkAbstractService;
import com.veisite.vegecom.ui.service.ClienteUIService;
import com.veisite.vegecom.ui.tercero.TerceroListTableModel;
import com.veisite.vegecom.ui.tercero.cliente.ClienteEditDialog;
import com.veisite.vegecom.ui.tercero.cliente.ClienteListProvider;

public class ClienteUIServiceImpl extends UIFrameworkAbstractService 
				implements ClienteUIService, DataChangeListener<Cliente> {

	/**
	 * logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * id 
	 */
	public static final String SERVICE_ID = "clienteUIService";
	
	/**
	 * Servicio de acceso a datos de clientes
	 */
	private ClienteService dataService;
	
	
	/**
	 * Contexto de aplicación
	 */
	protected ApplicationContext context;
	
	/**
	 * Modelo de datos de lista de clientes
	 */
	protected TerceroListTableModel<Cliente> listModel = null;
	
	
	public ClienteUIServiceImpl(UIFrameworkModule uiModule, ApplicationContext context) {
		super(uiModule);
		Assert.notNull(context);
		this.context = context;
	}
	
	@Override
	public String getId() {
		return SERVICE_ID;
	}

	@Override
	public void initService() throws Throwable {
		dataService = this.context.getBean(ClienteService.class);
		if (dataService==null)
			throw new VegecomException("ClienteService is not available");
		dataService.addDataChangeListener(this);

	}
	

	@Override
	public void disposeService() {
	}
	
	/**
	 * Metodos de servicio
	 * 
	 */
	
	/**
	 * Alta de un nuevo cliente
	 * 
	 * Devuelve el nuevo cliente que se va a editar.
	 * 
	 */
	public Cliente newCliente(Component parent) {
		return editCliente(null, parent);
	}
	
	/**
	 * modificar un cliente
	 * 
	 * Devuelve el cliente que se va a modificar 
	 * 
	 *  Si el cliente pasado es null, intenta crear un nuevo cliente
	 * 
	 */
	public Cliente editCliente(Cliente cliente, Component parent) {
		Assert.notNull(dataService);
		Cliente eCliente = cliente;
		if (eCliente==null) {
			eCliente = new Cliente();
			String s = getMessage("ui.ClienteUIService.DefaultName", 
					null, "New customer");
			eCliente.setNombre(s);
		}
		boolean error=false;
		Throwable excep=null;
		if (eCliente.getId()!=null) {
			try {
				eCliente = dataService.getById(eCliente.getId());
			} catch (DataAccessException e) {
				excep = e;
				error = true;
			} catch (Throwable t) {
				excep = t;
				error = true;
			}
			if (error) {
				logger.error("Error retrieving customer from persistence service", excep);
				String t = getMessage("ui.ClienteUIService.ErrorLoadClienteTitle", 
						null, "Error retrieving customer");
				String m = getMessage("ui.ClienteUIService.ErrorLoadClienteMessage", 
						null, "Error retrieving customer data");
				ErrorInfo err = new ErrorInfo(t, m,	excep.getMessage(), null, excep, null, null);
				JXErrorPane.showDialog(parent==null ? getParentWindow() : parent, err);
				return null;
			}
			// Si el cliente es null, no se ha encontrado
			// Podría haber sido borrado
			if (eCliente==null) {
				logger.error("Error retrieving customer from persistence service. Not found.");
				String t = getMessage("ui.ClienteUIService.ErrorNotExistClienteTitle", 
						null, "Error retrieving customer");
				String m = getMessage("ui.ClienteUIService.ErrorNotExistClienteMessage", 
						null, "Customer not found. Please, refresh data");
				ErrorInfo err = new ErrorInfo(t, m,	m, null, null, null, null);
				JXErrorPane.showDialog(getParentWindow(), err);
				return null;
			}
		}
		ClienteEditDialog dialog = new ClienteEditDialog(parent==null ? getParentWindow() : parent, eCliente, this);
		dialog.setDataService(dataService);
		dialog.setModalityType(ModalityType.MODELESS);
		dialog.pack();
		dialog.setLocationRelativeTo(dialog.getOwner());
		dialog.setVisible(true);
		return eCliente;
	}
	
	/**
	 * Elimina un cliente
	 * 
	 * Devuelve el cliente eliminado o null si no se elimino
	 * 
	 *  Pide confirmación si así se requiere
	 * 
	 */
	public Cliente removeCliente(Cliente cliente, boolean askConfirmation, Component parent) {
		Assert.notNull(dataService);
		if (askConfirmation) {
			String ti = getMessage("ui.ClienteUIService.ConfirmDeleteTitle", 
					null, "Remove Customer");
			String me =  getMessage("ui.ClienteUIService.ConfirmDeleteQuestion", 
					new String[] {cliente.getNombre()}, "Do you want to remove customer {0}?");
			int code = JOptionPane.showConfirmDialog(parent==null ? getParentWindow() : parent, me, ti, JOptionPane.YES_NO_OPTION);
			if (code!=JOptionPane.YES_OPTION) return null;
		}
		boolean error=false;
		Throwable excep=null;
		try {
			dataService.remove(cliente);
		} catch (DataAccessException e) {
			excep = e;
			error = true;
		} catch (Throwable t) {
			excep = t;
			error = true;
		}
		if (error) {
			logger.error("Error deleting customer in persistence service", excep);
			String t = getMessage("ui.ClienteUIService.ErrorDeleteClienteTitle", 
					null, "Error deleting customer");
			String m = getMessage("ui.ClienteUIService.ErrorDeleteClienteMessage", 
					null, "Error trying to delete customer data");
			ErrorInfo err = new ErrorInfo(t, m,	excep.getMessage(), null, excep, null, null);
			JXErrorPane.showDialog(parent==null ? getParentWindow() : parent, err);
			return null;
		}
		return cliente;
	}
	

	@Override
	public TerceroListTableModel<Cliente> getListTableModel() {
		Assert.notNull(dataService);
		if (listModel == null) {
			ClienteListProvider dataProvider = new ClienteListProvider(dataService);
			listModel = new TerceroLisTableModel<Cliente>(dataProvider, this);
			listModel.refreshData();
		}
		return this.listModel;
	}
	
	
	/**
	 * Un nuevo tercero ha sido añadido
	 * Incluir en la lista
	 */
	@Override
	public void itemAdded(Cliente item) {
		if (listModel!=null) listModel.addItem(item);
	}


	/**
	 * Un tercero ha sido modificado, notificar la tabla
	 */
	@Override
	public void itemChanged(Cliente item) {
		int index = getModelIndexForItem(item);
		if (index>=0) listModel.setItemAt(index, item);
	}


	/* (non-Javadoc)
	 * @see com.veisite.vegecom.service.DataChangeListener#itemRemoved(java.lang.Object)
	 */
	@Override
	public void itemRemoved(Cliente item) {
		int index = getModelIndexForItem(item);
		if (index>=0) listModel.delItemAt(index);
	}
	
	/**
	 * Busca un elemento en la table y devuelve el indice en el modelo
	 * Devuelve -1 si no se encuetra.
	 * @param table
	 * @param item
	 * @return
	 */
	protected int getModelIndexForItem(Cliente item) {
		if (listModel!=null) {
			List<Cliente> lista = listModel.getDataList();
			for (int i=0; i<lista.size(); i++)
				if (lista.get(i).getId().equals(item.getId()))
					return i;
		}
		return -1;
	}
	

}
