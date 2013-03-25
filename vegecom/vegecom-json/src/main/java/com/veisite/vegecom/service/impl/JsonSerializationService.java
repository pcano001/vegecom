package com.veisite.vegecom.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectInputFlow;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.service.SerializationService;

@Service
public class JsonSerializationService implements SerializationService {
	
	private JsonFactory factory;
	
	private ObjectMapper mapper;
	
	public JsonSerializationService() {
		this.factory = new JsonFactory();
		this.mapper = new ObjectMapper(factory);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	}

	public <T> void writeList(OutputStream output, List<T> lista) throws IOException {
		JsonGenerator jgen = factory.createGenerator(output);
		for (T o : lista) {
			mapper.writeValue(jgen, o);
		}
	}

	public <T> void writeObjectStream(OutputStream output,
			ObjectInputFlow<T> input) throws IOException, DataIOException {
		JsonGenerator jgen = factory.createGenerator(output);
		T o;
		while ((o = input.read())!=null) {
			mapper.writeValue(jgen, o);
		}
	}

	public <T> void write(OutputStream output, T object) throws IOException {
		JsonGenerator jgen = factory.createGenerator(output);
		mapper.writeValue(jgen, object);
	}

	public <T> List<T> readList(InputStream input, Class<T> type) throws IOException {
		List<T> lista = new ArrayList<T>();
		JsonParser jpar = factory.createJsonParser(input);
		while (jpar.nextToken()!=null) {
			T o = mapper.readValue(jpar, type);
			if (o!=null) lista.add(o);
		}
		return lista;
	}

	public <T> T read(InputStream input, Class<T> type) throws IOException {
		T o = null;
		JsonParser jpar = factory.createJsonParser(input);
		if (jpar.nextToken()!=null) o = mapper.readValue(jpar, type); 
		return o;
	}

	public <T> void readObjectStream(InputStream input,
			ObjectOutputFlow<T> output, Class<T> type) throws IOException, DataIOException {
		JsonParser jpar = factory.createJsonParser(input);
		while (jpar.nextToken()!=null) {
			T o = mapper.readValue(jpar, type);
			if (o!=null) output.write(o);
		}
	}

}
