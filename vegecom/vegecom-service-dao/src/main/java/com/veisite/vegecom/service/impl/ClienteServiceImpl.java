package com.veisite.vegecom.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.model.Cliente;
import com.veisite.vegecom.service.ClienteService;
import com.veisite.vegecom.service.impl.dao.ClienteRepository;

@Service
@Transactional(readOnly=true)
public class ClienteServiceImpl extends TerceroServiceImpl<Cliente> implements ClienteService {

	@Autowired
	ClienteRepository dao;
	
	@Override
	@Transactional
	public Cliente save(Cliente cliente) {
		boolean newItem = (cliente.getId()==null);
		Cliente i = dao.save(cliente);
		if (newItem) fireItemAddedEvent(i);
		else fireItemChangedEvent(i);
		return i;
	}

	@Override
	@Transactional
	public Cliente remove(Long id) {
		Cliente c = getById(id);
		dao.delete(id);
		if (c==null)
			throw new DataRetrievalFailureException("Cliente "+id+" not found");
		fireItemRemovedEvent(c);
		return c;
	}

	@Override
	public Cliente getById(Long id) {
		return dao.findOne(id);
	}

	@Override
	public List<Cliente> getList() {
		return dao.findAll(sortByNombreAsc());
	}
	
	@Override
	public void writeListTo(ObjectOutputFlow<Cliente> output) throws DataIOException {
		for (Cliente o : getList()) output.write(o);
	}

    private Sort sortByNombreAsc() {
        return new Sort(Sort.Direction.ASC, "nombre");
    }
	
}
