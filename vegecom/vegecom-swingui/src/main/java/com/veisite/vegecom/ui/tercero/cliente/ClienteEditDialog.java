package com.veisite.vegecom.ui.tercero.cliente;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.veisite.vegecom.model.Cliente;
import com.veisite.vegecom.service.ClienteService;
import com.veisite.vegecom.ui.framework.component.dialogs.AbstractEditDialog;
import com.veisite.vegecom.ui.framework.component.panels.ValidationMessagesPanel;
import com.veisite.vegecom.ui.service.ClienteUIService;

public class ClienteEditDialog extends AbstractEditDialog {

	/**
	 * serial
	 */
	private static final long serialVersionUID = -7349392482605593043L;
	
	/**
	 * logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * El cliente que se va a editar
	 */
	private Cliente cliente;
	
	/**
	 * El Servicio de datos para persistir los cambios
	 */
	private ClienteService dataService = null;
	
	/**
	 * El Servicio grafico
	 */
	private ClienteUIService uiService = null;
	
	
	public ClienteEditDialog(Dialog owner, Cliente cliente, ClienteUIService uiService) {
		super(owner, uiService);
		this.cliente = cliente;
		this.uiService = uiService;
		initializeDialog();
	}

	public ClienteEditDialog(Frame owner, Cliente cliente, ClienteUIService uiService) {
		super(owner);
		this.cliente = cliente;
		this.uiService = uiService;
		initializeDialog();
	}

	public ClienteEditDialog(Window owner, Cliente cliente, ClienteUIService uiService) {
		super(owner);
		this.cliente = cliente;
		this.uiService = uiService;
		initializeDialog();
	}

	public ClienteEditDialog(Component parent, Cliente cliente, ClienteUIService uiService) {
		super(parent);
		this.cliente = cliente;
		this.uiService = uiService;
		initializeDialog();
	}
	
	private void initializeDialog() {
		setTitle(cliente.getNombre());
		// Poner escucha a los cambios en el cliente
		cliente.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				setContentDirty(true);
			}
		});
		// Poner el componente de edición de cliente.
		ClienteEditComponent c = new ClienteEditComponent(cliente, uiService);
		setContentComponent(c);
	}

	@Override
	protected boolean canClose() {
		// Hacemos las validaciones para poder cerrar el dialogo.
		Validator validator = uiService.getValidator();
		if (validator!=null) {
			Set<ConstraintViolation<Cliente>> constraintViolations =
					validator.validate(cliente);
			if (constraintViolations.size()>0) {
				String s = 
						uiService.getMessage("ui.ClienteEditDialog.ValidationErrorMessage", 
						null, "Customer data has errors");
				ValidationMessagesPanel<Cliente> p = 
						new ValidationMessagesPanel<Cliente>(s,constraintViolations);
				JOptionPane.showMessageDialog(this, p, s, JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		// Ahora intentamos persistir los datos si tenemos servicio de persistencia
		if (dataService!=null) {
			Throwable excep=null;
			boolean error=false;
			try {
				dataService.save(cliente);
			} catch (DataAccessException e) {
				excep = e;
				error = true;
			} catch (Throwable t) {
				excep = t;
				error = true;
			}
			if (error) {
				logger.error("Error saving customer in persistence service", excep);
				String t = uiService.getMessage("ui.ClienteEditDialog.ErrorSaveClienteTitle", 
						null, "Error retrieving customer");
				String m = uiService.getMessage("ui.ClienteEditDialog.ErrorSaveClienteMessage", 
						null, "Error retrieving customer data");
				ErrorInfo err = new ErrorInfo(t, m,	excep.getMessage(), null, excep, null, null);
				JXErrorPane.showDialog(getParent(), err);
				return false;
			}
		}
		return true;
	}

	@Override
	protected boolean canCancel() {
		return true;
	}

	@Override
	protected void onClose() {
	}

	/**
	 * @return the dataService
	 */
	public ClienteService getDataService() {
		return dataService;
	}

	/**
	 * @param dataService the dataService to set
	 */
	public void setDataService(ClienteService dataService) {
		this.dataService = dataService;
	}

	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * @return the uiService
	 */
	public ClienteUIService getUiService() {
		return uiService;
	}
	
}
