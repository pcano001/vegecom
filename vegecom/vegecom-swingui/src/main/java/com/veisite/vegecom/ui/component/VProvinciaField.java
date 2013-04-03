package com.veisite.vegecom.ui.component;

import java.awt.Dimension;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import org.springframework.util.Assert;

import com.veisite.vegecom.model.Provincia;
import com.veisite.vegecom.service.AddressService;
import com.veisite.vegecom.ui.framework.component.VComboBox;
import com.veisite.vegecom.ui.framework.component.VTextField;

public class VProvinciaField extends VComboBox<Provincia> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Lista de provincias
	 */
	List<com.veisite.vegecom.model.Provincia> lista;
	
	public VProvinciaField(AddressService as) {
		super();
		Assert.notNull(as);
		this.lista = as.getProvincias();
		Vector<Provincia> v = new Vector<Provincia>(this.lista);
		// Insert empty value ""
		v.add(0, null);
		ComboBoxModel<Provincia> model = new DefaultComboBoxModel<Provincia>(v);
		setModel(model);
		Provincia pro = new Provincia();
		pro.setNombre("GUADALAJARA  ");
		setPrototypeDisplayValue(pro);
		// Ajustamos tamaño igual a un campo de texto.
		VTextField p = new VTextField();
		p.setText("prueba");
		setPreferredSize(new Dimension(getPreferredSize().width, p.getPreferredSize().height));
		setMaximumSize(new Dimension(Short.MAX_VALUE, p.getPreferredSize().height));
	}

	public void setProvinciaFromCode(String code) {
		// Si el codigo es la provincia actual, no hay que hacer nada
		Provincia current = (Provincia) getSelectedItem();
		if (current!=null && current.getId().equals(code)) return;
		// Validar codigo
		if (!isValidCode(code)) return;
		// Buscar valor en lista
		Provincia prov = null;
		for (Provincia p : lista) {
			if (p.getId().equals(code)) {
				prov = p;
				break;
			}
		}
		if (prov!=null) {
	    	setSelectedItem(prov);
		}
	}
	
	public void setProvinciaFromCode(int code) {
		setProvinciaFromCode(String.format("%02d", code));
	}
	
	private boolean isValidCode(String code) {
		int c=0;
		try {
			c = new Integer(NumberFormat.getIntegerInstance().parse(code).intValue());
		} catch (ParseException e) {
			return false;
		}
		if (c!=0) return isValidCode(c);
		return false;
	}
	
	private boolean isValidCode(int code) {
		if (code<1 || code>52) return false;
		return true;
	}


}
