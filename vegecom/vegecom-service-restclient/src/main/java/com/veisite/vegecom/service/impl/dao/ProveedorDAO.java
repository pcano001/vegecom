package com.veisite.vegecom.service.impl.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.model.Proveedor;

@Repository
public class ProveedorDAO {

	public static final Logger logger = LoggerFactory.getLogger(ProveedorDAO.class);

	public Proveedor getById(Long id) {
		return new Proveedor();
	}
	
	public Proveedor save(Proveedor proveedor) {
		return proveedor;
	}

	public Proveedor remove(Long id) {
		return null;
	}
	
	/**
	 * Devuelve la lista de Proveedor.
	 * 
	 * @return
	 */
	public List<Proveedor> getList() {
		List<Proveedor> lista = new ArrayList<Proveedor>();
		lista.add(new Proveedor());
		return lista;
	}

	
	/**
	 * 	Recupera la lista de Proveedor y la envia a un buffer de lectura/escritura
	 * @param output
	 * @throws DataIOException
	 */
	public void writeListTo(ObjectOutputFlow<Proveedor> output) throws DataIOException {
		List<Proveedor> lista = new ArrayList<Proveedor>();
		lista.add(new Proveedor());
		for (Proveedor p : lista) output.write(p);
	}

	
}
