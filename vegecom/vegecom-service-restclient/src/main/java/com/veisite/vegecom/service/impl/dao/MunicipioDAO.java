package com.veisite.vegecom.service.impl.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.model.Municipio;
import com.veisite.vegecom.model.Provincia;
import com.veisite.vegecom.rest.client.ListResponseExtractor;
import com.veisite.vegecom.rest.client.ObjectFlowResponseExtractor;
import com.veisite.vegecom.rest.client.ObjectResponseExtractor;
import com.veisite.vegecom.rest.client.RestClientRequest;

@Repository
public class MunicipioDAO extends AbstractRestDAO {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public MunicipioDAO() {
		setResourcePath("/rs/municipio");
	}
	
	public Municipio getById(String id) {
		String path = getResourcePath()+"/"+id;
		RestClientRequest request = requestFactory.createRequest(path);
		RequestCallback cb = null;
		ResponseExtractor<Municipio> ex = 
				new ObjectResponseExtractor<Municipio>(serializationService, Municipio.class);
		return executeQuery(request, HttpMethod.GET, cb, ex);
	}

	public Municipio save(Municipio municipio) {
		return municipio;
	}
	
	public List<Municipio> getList() {
		String path = getResourcePath()+"/list";
		RestClientRequest request = requestFactory.createRequest(path);
		RequestCallback cb = null;
		ResponseExtractor<List<Municipio>> ex = 
				new ListResponseExtractor<Municipio>(serializationService, Municipio.class);
		return executeQuery(request, HttpMethod.GET, cb, ex);
	}

	public List<Municipio> getListbyProvincia(Provincia provincia) {
		String path = getResourcePath()+"/list";
		RestClientRequest request = requestFactory.createRequest(path);
		if (provincia!=null) request.setParameter("provinciaId", provincia.getId());
		RequestCallback cb = null;
		ResponseExtractor<List<Municipio>> ex = 
				new ListResponseExtractor<Municipio>(serializationService, Municipio.class);
		return executeQuery(request, HttpMethod.GET, cb, ex);
	}
	
	public Municipio getByNombre(String nombre) {
		throw new RestClientException("Not Implemented yet");
	}

	public void getList(ObjectOutputFlow<Municipio> output, Provincia provincia) throws DataIOException {
		String path = getResourcePath()+"/list";
		RestClientRequest request = requestFactory.createRequest(path);
		if (provincia!=null) request.setParameter("provinciaId", provincia.getId());
		RequestCallback cb = null;
		ResponseExtractor<Municipio> ex = 
				new ObjectFlowResponseExtractor<Municipio>(serializationService, output, Municipio.class);
		logger.debug("Quering server for Municipio List...");
		executeQuery(request, HttpMethod.GET, cb, ex);
		logger.debug("Reading municipio has ended correctly, exiting...");
	}

}
