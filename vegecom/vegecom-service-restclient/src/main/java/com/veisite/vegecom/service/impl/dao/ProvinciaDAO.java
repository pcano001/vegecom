package com.veisite.vegecom.service.impl.dao;

import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;

import com.veisite.vegecom.model.Provincia;
import com.veisite.vegecom.rest.client.ListResponseExtractor;
import com.veisite.vegecom.rest.client.ObjectResponseExtractor;
import com.veisite.vegecom.rest.client.RestClientRequest;

@Repository
public class ProvinciaDAO extends AbstractRestDAO {

	public ProvinciaDAO() {
		setResourcePath("/rs/provincia");
	}
	
	public Provincia getById(String id) {
		String path = getResourcePath()+"/"+id;
		RestClientRequest request = requestFactory.createRequest(path);
		RequestCallback cb = null;
		ResponseExtractor<Provincia> ex = 
				new ObjectResponseExtractor<Provincia>(serializationService, Provincia.class);
		return executeQuery(request, HttpMethod.GET, cb, ex);
	}

	public Provincia save(Provincia provincia) {
		return provincia;
	}
	
	public List<Provincia> getList() {
		String path = getResourcePath()+"/list";
		RestClientRequest request = requestFactory.createRequest(path);
		RequestCallback cb = null;
		ResponseExtractor<List<Provincia>> ex = 
				new ListResponseExtractor<Provincia>(serializationService, Provincia.class);
		return executeQuery(request, HttpMethod.GET, cb, ex);
	}

}
