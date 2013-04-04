package com.veisite.vegecom.service.impl.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.model.Cliente;
import com.veisite.vegecom.rest.client.ListResponseExtractor;
import com.veisite.vegecom.rest.client.ObjectFlowResponseExtractor;
import com.veisite.vegecom.rest.client.ObjectRequestFiller;
import com.veisite.vegecom.rest.client.ObjectResponseExtractor;
import com.veisite.vegecom.service.SerializationService;
import com.veisite.vegecom.service.impl.RestClientService;

@Repository
public class ClienteDAO {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RestClientService restService;
	
	@Autowired
	private SerializationService serializationService;
	
	private static final String ENTRYPOINT_URL = "/cliente";
	
	public Cliente getById(Long id) {
		RestTemplate tp = restService.createRestTemplate();
		String url = restService.getBaseURL()+ENTRYPOINT_URL+"/{id}";
		RequestCallback cb = null;
		ResponseExtractor<Cliente> ex = 
				new ObjectResponseExtractor<Cliente>(serializationService, Cliente.class);
		return tp.execute(url, HttpMethod.GET, cb, ex, id);
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
		RestTemplate tp = restService.createRestTemplate();
		String url = restService.getBaseURL()+ENTRYPOINT_URL;
		RequestCallback cb = 
			new ObjectRequestFiller<Cliente>(serializationService, cliente);
		ResponseExtractor<Cliente> ex = 
				new ObjectResponseExtractor<Cliente>(serializationService, Cliente.class);
		return tp.execute(url, HttpMethod.POST, cb, ex);
	}

	public Cliente update(Cliente cliente) {
		RestTemplate tp = restService.createRestTemplate();
		String url = restService.getBaseURL()+ENTRYPOINT_URL+"/{id}";
		RequestCallback cb = 
			new ObjectRequestFiller<Cliente>(serializationService, cliente);
		ResponseExtractor<Cliente> ex = 
			new ObjectResponseExtractor<Cliente>(serializationService, Cliente.class);
		return tp.execute(url, HttpMethod.PUT, cb, ex, cliente.getId());
	}

	public Cliente remove(Long id) {
		RestTemplate tp = restService.createRestTemplate();
		String url = restService.getBaseURL()+ENTRYPOINT_URL+"/{id}";
		RequestCallback cb = null;
		ResponseExtractor<Cliente> ex = 
			new ObjectResponseExtractor<Cliente>(serializationService, Cliente.class);
		return tp.execute(url, HttpMethod.DELETE, cb, ex, id);
	}
	
	/**
	 * Devuelve la lista de cliente.
	 * 
	 * @return
	 */
	public List<Cliente> getList() {
		RestTemplate tp = restService.createRestTemplate();
		String url = restService.getBaseURL()+ENTRYPOINT_URL+"/list";
		RequestCallback cb = null;
		ResponseExtractor<List<Cliente>> ex = 
				new ListResponseExtractor<Cliente>(serializationService, Cliente.class);
		return tp.execute(url, HttpMethod.GET, cb, ex);
	}

	
	/**
	 * 	Recupera la lista de cliente y la envia a un buffer de lectura/escritura
	 * @param output
	 * @throws DataIOException
	 */
	public void writeListTo(ObjectOutputFlow<Cliente> output) throws DataIOException {
		RestTemplate tp = restService.createRestTemplate();
		String url = restService.getBaseURL()+ENTRYPOINT_URL+"/list";
		RequestCallback cb = null;
		ResponseExtractor<Cliente> ex = 
				new ObjectFlowResponseExtractor<Cliente>(serializationService, output, Cliente.class);
		logger.debug("Quering server for Cliente List...");
		tp.execute(url, HttpMethod.GET, cb, ex);
		logger.debug("Reading cliente has ended correctly, exiting...");
	}

	
}
