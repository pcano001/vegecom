package com.veisite.vegecom.ui.tercero;

import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.validation.Validator;

import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.veisite.utils.binding.BindTarget;
import com.veisite.vegecom.model.Municipio;
import com.veisite.vegecom.model.Provincia;
import com.veisite.vegecom.model.TerceroComercial;
import com.veisite.vegecom.service.AddressService;
import com.veisite.vegecom.ui.component.VCodigoPostalField;
import com.veisite.vegecom.ui.component.VCuentaBancariaField;
import com.veisite.vegecom.ui.component.VMunicipioField;
import com.veisite.vegecom.ui.component.VNifField;
import com.veisite.vegecom.ui.component.VProvinciaField;
import com.veisite.vegecom.ui.framework.component.VTextArea;
import com.veisite.vegecom.ui.framework.component.VTextField;
import com.veisite.vegecom.ui.framework.util.UIResources;
import com.veisite.vegecom.ui.service.TerceroUIService;

public abstract class TerceroEditComponent<T extends TerceroComercial> extends JPanel {

	/**
	 * serial
	 */
	private static final long serialVersionUID = -5863438742043219413L;

	/**
	 * tercero a editar
	 */
	private T tercero;
	
	/**
	 * Componentes
	 */
	protected VNifField nifField;
	protected VTextField nameField;
	protected VTextField domicilioField;
	protected VCodigoPostalField cpField;
	protected VProvinciaField provinciaField;
	protected VMunicipioField municipioField;
	protected VTextField localidadField;
	protected VTextField telefonoField;
	protected VTextField emailField;
	protected VCuentaBancariaField cccField;
	protected VTextArea observacionesField;
	
	/**
	 * ui Instance. Recursos graficos
	 */
	private TerceroUIService<T> uiService;
	
	/**
	 * Servicio de direcciones
	 */
	private AddressService addressService;
	
	
	/**
	 * Constructor
	 * @param tercero
	 */
	public TerceroEditComponent(T tercero, TerceroUIService<T> uiService) {
		Assert.notNull(tercero);
		Assert.notNull(uiService);
		this.tercero = tercero;
		this.uiService = uiService;
		this.addressService = uiService.getContext().getBean(AddressService.class);
		Assert.notNull(this.addressService);
		createComponents();
		composePanel();
		bindComponents();
	}

	/**
	 * @return the tercero
	 */
	public T getTercero() {
		return tercero;
	}
	
	protected void createComponents() {
		String s;
		Validator v = uiService.getValidator();
		s =	uiService.getMessage("ui.tercero.TerceroEditPanel.nifPrompt", null, "Nif");
		nifField = new VNifField(s, 9);
		nifField.configureValidation(v, tercero, "nif");
		s =	uiService.getMessage("ui.tercero.TerceroEditPanel.namePrompt", null, "Name");
		nameField = new VTextField(s);
		nameField.setColumns(35);
		nameField.configureValidation(v, tercero, "nombre");
		s =	uiService.getMessage("ui.tercero.TerceroEditPanel.telefonoPrompt", null, "Telephone");
		telefonoField = new VTextField(s);
		telefonoField.setColumns(20);
		s =	uiService.getMessage("ui.tercero.TerceroEditPanel.emailPrompt", null, "E-mail");
		emailField = new VTextField(s);
		emailField.setColumns(25);
		emailField.configureValidation(v, tercero, "email");
		s =	uiService.getMessage("ui.tercero.TerceroEditPanel.domicilioPrompt", null, "Address");
		domicilioField = new VTextField(s);
		domicilioField.setColumns(35);
		s =	uiService.getMessage("ui.tercero.TerceroEditPanel.codigoPostalPrompt", null, "C.P.");
		cpField = new VCodigoPostalField(s);
		cpField.setColumns(4);
		provinciaField = new VProvinciaField(addressService);
		municipioField = new VMunicipioField(provinciaField,addressService);
		s =	uiService.getMessage("ui.tercero.TerceroEditPanel.localidadPrompt", null, "Village/Town");
		localidadField = new VTextField(s);
		localidadField.setColumns(25);
	}
	
	protected void composePanel() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JPanel vBox = new JPanel();
	    vBox.setLayout(new BoxLayout(vBox,BoxLayout.Y_AXIS));
	    
	    JPanel row;
		String s;
	    
	    row = new JPanel(new FlowLayout(FlowLayout.LEFT));
		s =	uiService.getMessage("ui.tercero.TerceroEditPanel.nifLabel", null, "Nif:");
	    row.add(UIResources.getLabeledComponent(s, nifField));
		s =	uiService.getMessage("ui.tercero.TerceroEditPanel.nameLabel", null, "Name:");
	    row.add(UIResources.getLabeledComponent(s, nameField));
	    vBox.add(row);
	    
	    row = new JPanel(new FlowLayout(FlowLayout.LEFT));
		s =	uiService.getMessage("ui.tercero.TerceroEditPanel.telefonoLabel", null, "Telephone:");
	    row.add(UIResources.getLabeledComponent(s, telefonoField));
		s =	uiService.getMessage("ui.tercero.TerceroEditPanel.emailLabel", null, "E-mail:");
	    row.add(UIResources.getLabeledComponent(s, emailField));
	    vBox.add(row);
	    
	    row = new JPanel(new FlowLayout(FlowLayout.LEFT));
		s =	uiService.getMessage("ui.tercero.TerceroEditPanel.domicilioLabel", null, "Address:");
	    row.add(UIResources.getLabeledComponent(s, domicilioField));
		s =	uiService.getMessage("ui.tercero.TerceroEditPanel.codigoPostalLabel", null, "C.P.:");
	    row.add(UIResources.getLabeledComponent(s, cpField));
		s =	uiService.getMessage("ui.tercero.TerceroEditPanel.provinciaLabel", null, "Province:");
	    row.add(UIResources.getLabeledComponent(s, provinciaField));
	    vBox.add(row);
	    
	    row = new JPanel(new FlowLayout(FlowLayout.LEFT));
		s =	uiService.getMessage("ui.tercero.TerceroEditPanel.municipioLabel", null, "City:");
	    row.add(UIResources.getLabeledComponent(s, municipioField));
		s =	uiService.getMessage("ui.tercero.TerceroEditPanel.localidadLabel", null, "Village/Town:");
	    row.add(UIResources.getLabeledComponent(s, localidadField));
	    vBox.add(row);
	    
	    add(vBox);
	}
	
	protected void bindComponents() {
		nifField.setText(tercero.getNif());
		nifField.addBindTo(new BindTarget<String>(tercero, "nif"));
		nameField.setText(tercero.getNombre());
		nameField.addBindTo(new BindTarget<String>(tercero, "nombre"));
		telefonoField.setText(tercero.getTelefono());
		telefonoField.addBindTo(new BindTarget<String>(tercero, "telefono"));
		emailField.setText(tercero.getEmail());
		emailField.addBindTo(new BindTarget<String>(tercero, "email"));
		domicilioField.setText(tercero.getDomicilio());
		domicilioField.addBindTo(new BindTarget<String>(tercero, "domicilio"));
		cpField.setText(tercero.getCodigoPostal());
		cpField.addBindTo(new BindTarget<String>(tercero, "codigoPostal"));
		provinciaField.setSelectedItem(tercero.getProvincia());
		provinciaField.addBindTo(new BindTarget<Provincia>(tercero, "provincia"));
		municipioField.setSelectedItem(tercero.getMunicipio());
		municipioField.addBindTo(new BindTarget<Municipio>(tercero, "municipio"));
		localidadField.setText(tercero.getLocalidad());
		localidadField.addBindTo(new BindTarget<String>(tercero, "localidad"));
		
		// Listener para cambiar provincia en cambio de cp
		cpField.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				changeProvinciaFromCP();
			}
			@Override
			public void focusGained(FocusEvent e) {
			}
		});

	}
	
	
	/**
	 * Si el codigo postal es correto y distinto de la provincia actual
	 * entonces cambia la provincia y pregunta antes.
	 */
	private void changeProvinciaFromCP() {
		String sc = cpField.getText();
		if (sc==null || sc.isEmpty()) return;
		int pc=0;
		try {
			pc = Integer.parseInt(sc) / 1000;
		} catch (NumberFormatException nfe) {
			LoggerFactory.getLogger(TerceroEditComponent.class).error("Error en cp",nfe);
		}
		provinciaField.setProvinciaFromCode(pc);
	}
	

}
