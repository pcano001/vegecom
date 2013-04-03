package com.veisite.vegecom.service.impl.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import com.veisite.vegecom.model.Provincia;
import com.veisite.vegecom.rest.client.ListResponseExtractor;
import com.veisite.vegecom.rest.client.ObjectResponseExtractor;
import com.veisite.vegecom.service.SerializationService;
import com.veisite.vegecom.service.impl.RestClientService;

@Repository
public class ProvinciaDAO {

	@Autowired
	private RestClientService restService;
	
	@Autowired
	private SerializationService serializationService;
	
	private static final String ENTRYPOINT_URL = "/provincia";
	
	public Provincia getById(String id) {
		RestTemplate tp = restService.createRestTemplate();
		String url = restService.getBaseURL()+ENTRYPOINT_URL+"/{id}";
		RequestCallback cb = null;
		ResponseExtractor<Provincia> ex = 
				new ObjectResponseExtractor<Provincia>(serializationService, Provincia.class);
		return tp.execute(url, HttpMethod.GET, cb, ex, id);
	}

	public Provincia save(Provincia provincia) {
		return provincia;
	}
	
	public List<Provincia> getList() {
		RestTemplate tp = restService.createRestTemplate();
		String url = restService.getBaseURL()+ENTRYPOINT_URL+"/list";
		RequestCallback cb = null;
		ResponseExtractor<List<Provincia>> ex = 
				new ListResponseExtractor<Provincia>(serializationService, Provincia.class);
		return tp.execute(url, HttpMethod.GET, cb, ex);
	}

}
