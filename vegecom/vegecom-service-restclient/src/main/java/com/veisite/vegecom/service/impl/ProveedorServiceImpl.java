package com.veisite.vegecom.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.model.Proveedor;
import com.veisite.vegecom.service.ProveedorService;
import com.veisite.vegecom.service.impl.dao.ProveedorDAO;

@Service
public class ProveedorServiceImpl extends TerceroServiceImpl<Proveedor> implements ProveedorService {

	@Autowired
	ProveedorDAO dao;
	
	@Override
	public Proveedor save(Proveedor proveedor) {
		boolean newItem = (proveedor.getId()==null);
		Proveedor i = dao.save(proveedor);
		if (newItem) fireItemAddedEvent(i);
		else fireItemChangedEvent(i);
		return i;
	}

	@Override
	public Proveedor remove(Long id) {
		Proveedor p = dao.remove(id);
		fireItemRemovedEvent(p);
		return p;
	}

	@Override
	public Proveedor getById(Long id) {
		return dao.getById(id);
	}

	@Override
	public List<Proveedor> getList() {
		return dao.getList();
	}
	
	@Override
	public void writeListTo(ObjectOutputFlow<Proveedor> output) throws DataIOException {
		dao.writeListTo(output);
	}

}
