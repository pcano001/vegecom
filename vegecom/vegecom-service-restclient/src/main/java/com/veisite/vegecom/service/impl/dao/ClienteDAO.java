package com.veisite.vegecom.service.impl.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.model.Cliente;
import com.veisite.vegecom.rest.client.ListResponseExtractor;
import com.veisite.vegecom.rest.client.ObjectFlowResponseExtractor;
import com.veisite.vegecom.rest.client.ObjectRequestFiller;
import com.veisite.vegecom.rest.client.ObjectResponseExtractor;
import com.veisite.vegecom.rest.client.RestClientRequest;

@Repository
public class ClienteDAO extends AbstractRestDAO {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public ClienteDAO() {
		setResourcePath("/rs/cliente");
	}
	
	public Cliente getById(Long id) {
		String path = getResourcePath()+"/"+Long.toString(id);
		RestClientRequest request = requestFactory.createRequest(path);
		RequestCallback cb = null;
		ResponseExtractor<Cliente> ex = 
				new ObjectResponseExtractor<Cliente>(serializationService, Cliente.class);
		return executeQuery(request, HttpMethod.GET, cb, ex);
	}
	
	public Cliente save(Cliente cliente) {
		if (cliente.getId()==null) {
			cliente = create(cliente);
		} else {
			cliente = update(cliente);
		}
		return cliente;
	}

	public Cliente create(Cliente cliente) {
		String path = getResourcePath();
		RestClientRequest request = requestFactory.createRequest(path);
		RequestCallback cb = 
			new ObjectRequestFiller<Cliente>(serializationService, cliente);
		ResponseExtractor<Cliente> ex = 
				new ObjectResponseExtractor<Cliente>(serializationService, Cliente.class);
		return executeQuery(request, HttpMethod.POST, cb, ex);
	}

	public Cliente update(Cliente cliente) {
		String path = getResourcePath()+"/"+Long.toString(cliente.getId());
		RestClientRequest request = requestFactory.createRequest(path);
		RequestCallback cb = 
			new ObjectRequestFiller<Cliente>(serializationService, cliente);
		ResponseExtractor<Cliente> ex = 
			new ObjectResponseExtractor<Cliente>(serializationService, Cliente.class);
		return executeQuery(request, HttpMethod.PUT, cb, ex);
	}

	public Cliente remove(Long id) {
		String path = getResourcePath()+"/"+Long.toString(id);
		RestClientRequest request = requestFactory.createRequest(path);
		RequestCallback cb = null;
		ResponseExtractor<Cliente> ex = 
			new ObjectResponseExtractor<Cliente>(serializationService, Cliente.class);
		return executeQuery(request, HttpMethod.DELETE, cb, ex);
	}
	
	/**
	 * Devuelve la lista de cliente.
	 * 
	 * @return
	 */
	public List<Cliente> getList() {
		String path = getResourcePath()+"/list";
		RestClientRequest request = requestFactory.createRequest(path);
		RequestCallback cb = null;
		ResponseExtractor<List<Cliente>> ex = 
				new ListResponseExtractor<Cliente>(serializationService, Cliente.class);
		return executeQuery(request, HttpMethod.GET, cb, ex);
	}

	
	/**
	 * 	Recupera la lista de cliente y la envia a un buffer de lectura/escritura
	 * @param output
	 * @throws DataIOException
	 */
	public void writeListTo(ObjectOutputFlow<Cliente> output) throws DataIOException {
		String path = getResourcePath()+"/list";
		RestClientRequest request = requestFactory.createRequest(path);
		RequestCallback cb = null;
		ResponseExtractor<Cliente> ex = 
				new ObjectFlowResponseExtractor<Cliente>(serializationService, output, Cliente.class);
		logger.debug("Quering server for Cliente List...");
		executeQuery(request, HttpMethod.GET, cb, ex);
		logger.debug("Reading cliente has ended correctly, exiting...");
	}

}
