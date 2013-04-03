package com.veisite.vegecom.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.model.Cliente;
import com.veisite.vegecom.service.ClienteService;
import com.veisite.vegecom.service.impl.dao.ClienteDAO;

@Service
public class ClienteServiceImpl extends TerceroServiceImpl<Cliente> implements ClienteService {

	@Autowired
	ClienteDAO dao;
	
	@Override
	public Cliente save(Cliente cliente) {
		boolean newItem = (cliente.getId()==null);
		Cliente i = dao.save(cliente);
		if (newItem) fireItemAddedEvent(i);
		else fireItemChangedEvent(i);
		return i;
	}

	@Override
	public Cliente remove(Long id) {
		Cliente c = dao.remove(id);
		fireItemRemovedEvent(c);
		return c;
	}

	@Override
	public Cliente getById(Long id) {
		return dao.getById(id);
	}

	@Override
	public List<Cliente> getList() {
		return dao.getList();
	}
	
	@Override
	public void writeListTo(ObjectOutputFlow<Cliente> output) throws DataIOException {
		dao.writeListTo(output);
	}

}
