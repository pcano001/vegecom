package com.veisite.vegecom.service.impl.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.model.Municipio;
import com.veisite.vegecom.model.Provincia;
import com.veisite.vegecom.rest.client.ListResponseExtractor;
import com.veisite.vegecom.rest.client.ObjectFlowResponseExtractor;
import com.veisite.vegecom.rest.client.ObjectResponseExtractor;
import com.veisite.vegecom.service.SerializationService;
import com.veisite.vegecom.service.impl.RestClientService;

@Repository
public class MunicipioDAO {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RestClientService restService;
	
	@Autowired
	private SerializationService serializationService;
	
	@Autowired
	private DAOExceptionHandler exceptionHandler;
	
	private static final String ENTRYPOINT_URL = "/municipio";
	
	public Municipio getById(String id) {
		RestTemplate tp = restService.createRestTemplate();
		String url = restService.getBaseURL()+ENTRYPOINT_URL+"/{id}";
		RequestCallback cb = null;
		ResponseExtractor<Municipio> ex = 
				new ObjectResponseExtractor<Municipio>(serializationService, Municipio.class);
		try {
			return tp.execute(url, HttpMethod.GET, cb, ex, id);
		} catch (RestClientException rce) {
			throw exceptionHandler.getDataAccessException(rce);
		}
	}

	public Municipio save(Municipio municipio) {
		return municipio;
	}
	
	public List<Municipio> getList() {
		RestTemplate tp = restService.createRestTemplate();
		String url = restService.getBaseURL()+ENTRYPOINT_URL+"/list";
		RequestCallback cb = null;
		ResponseExtractor<List<Municipio>> ex = 
				new ListResponseExtractor<Municipio>(serializationService, Municipio.class);
		try {
			return tp.execute(url, HttpMethod.GET, cb, ex);
		} catch (RestClientException rce) {
			throw exceptionHandler.getDataAccessException(rce);
		}
	}

	public List<Municipio> getListbyProvincia(Provincia provincia) {
		RestTemplate tp = restService.createRestTemplate();
		String url = restService.getBaseURL()+ENTRYPOINT_URL+"/list?provinciaId={pid}";
		RequestCallback cb = null;
		ResponseExtractor<List<Municipio>> ex = 
				new ListResponseExtractor<Municipio>(serializationService, Municipio.class);
		try {
			return tp.execute(url, HttpMethod.GET, cb, ex, provincia.getId());
		} catch (RestClientException rce) {
			throw exceptionHandler.getDataAccessException(rce);
		}
	}
	
	public Municipio getByNombre(String nombre) {
		throw new RestClientException("Not Implemented yet");
	}

	public void getList(ObjectOutputFlow<Municipio> output, Provincia provincia) throws DataIOException {
		RestTemplate tp = restService.createRestTemplate();
		String url = restService.getBaseURL()+ENTRYPOINT_URL+"/list";
		if (provincia!=null) url += "?provinciaId={pid}";
		RequestCallback cb = null;
		ResponseExtractor<Municipio> ex = 
				new ObjectFlowResponseExtractor<Municipio>(serializationService, output, Municipio.class);
		String pId="";
		if (provincia!=null) pId=provincia.getId();
		logger.debug("Quering server for Municipio List...");
		try {
			tp.execute(url, HttpMethod.GET, cb, ex, pId);
		} catch (RestClientException rce) {
			throw exceptionHandler.getDataAccessException(rce);
		}
		logger.debug("Reading municipio has ended correctly, exiting...");
	}

}
