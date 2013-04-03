package com.veisite.vegecom.ui.component;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

import org.springframework.util.Assert;

import com.veisite.vegecom.model.Municipio;
import com.veisite.vegecom.model.Provincia;
import com.veisite.vegecom.service.AddressService;
import com.veisite.vegecom.ui.framework.component.VComboBox;
import com.veisite.vegecom.ui.framework.component.VTextField;

public class VMunicipioField extends VComboBox<Municipio> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Servicio para recuperación de provincias.
	 */
	private AddressService as;
	
	/**
	 * Campo provincia al que se vincula el municipio. Se activa un listener
	 * y se responde a los cambios de provincia refrescando la lista de municipios.
	 * @param parent
	 */
	private VProvinciaField parent;
	
	/**
	 * Guarda la provincia que tenemos cargada
	 * @param parent
	 */
	private Provincia currentProvincia;
	
	
	public VMunicipioField(VProvinciaField parent, AddressService as) {
		super();
		Assert.notNull(as);
		this.parent = parent;
		this.as = as;
		bindWithParent();
		if (parent==null) setModelForParent();
		Municipio pro = new Municipio();
		pro.setNombre("SELECCIONE MUNICIPIO DE LA LISTA");
		setPrototypeDisplayValue(pro);
		// Ajustamos tamaño igual a un campo de texto.
		VTextField p = new VTextField();
		p.setText("prueba");
		setPreferredSize(new Dimension(getPreferredSize().width, p.getPreferredSize().height));
		setMaximumSize(new Dimension(Short.MAX_VALUE, p.getPreferredSize().height));
	}
	
	/**
	 * Pone el modelo con la lista de municipios correspondientes a la provincia o 
	 * todos los municipios si no hay provincia.
	 */
	private void setModelForParent() {
		Vector<Municipio> v;
		if (parent==null) {
			v = new Vector<Municipio>(as.getAllMunicipios());
			this.currentProvincia = null;
		} else {
			Provincia p = (Provincia) parent.getSelectedItem();
			if (p==null) {
				v = new Vector<Municipio>();
			} else {
				if (p.equals(currentProvincia)) return;
				// Cargamos un nuevo modelo
				v = new Vector<Municipio>(as.getMunicipiosByProvincia(p));
			}
		}
		v.add(0, null);
		setModel(new DefaultComboBoxModel<Municipio>(v));
		setSelectedIndex(0);
	}
	
	/**
	 * Conectar un listener para escuchar los cambios en la provincia.
	 */
	private void bindWithParent() {
		if (parent==null) return;
		parent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setModelForParent();
			}
		});
	}
	
}
