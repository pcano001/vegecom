package com.veisite.vegecom.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectInputFlow;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.service.SerializationException;
import com.veisite.vegecom.service.SerializationGenerationException;
import com.veisite.vegecom.service.SerializationMappingException;
import com.veisite.vegecom.service.SerializationParseException;
import com.veisite.vegecom.service.SerializationService;
import com.veisite.vegecom.service.UncategorizedSerializationException;

@Service
public class JsonSerializationService implements SerializationService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private JsonFactory factory;
	
	private ObjectMapper mapper;
	
	public JsonSerializationService() {
		this.factory = new JsonFactory();
		this.mapper = new ObjectMapper(factory);
		mapper.registerModule(new Hibernate4Module());
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
	}
	
	public <T> void writeList(OutputStream output, List<T> lista) throws SerializationException {
		try {
			JsonGenerator jgen = factory.createGenerator(output);
			for (T o : lista) {
				mapper.writeValue(jgen, o);
			}
		} catch (JsonGenerationException ge) {
			logger.debug("Error writeList", ge);
			throw new SerializationGenerationException(ge);
		} catch (JsonMappingException me) {
			logger.debug("Error writeList", me);
			throw new SerializationMappingException(me);
		} catch (IOException ioe) {
			logger.debug("Error writeList", ioe);
			throw new SerializationException(ioe);
		}
	}

	public <T> void writeObjectStream(OutputStream output,
			ObjectInputFlow<T> input) throws SerializationException {
		try {
			JsonGenerator jgen = factory.createGenerator(output);
			T o;
			while ((o = input.read())!=null) {
				mapper.writeValue(jgen, o);
			}
		} catch (DataIOException dioe) {
			logger.debug("Error writeObjectStream", dioe);
			throw new UncategorizedSerializationException(dioe);
		} catch (JsonGenerationException ge) {
			logger.debug("Error writeObjectStream", ge);
			throw new SerializationGenerationException(ge);
		} catch (JsonMappingException me) {
			logger.debug("Error writeObjectStream", me);
			throw new SerializationMappingException(me);
		} catch (IOException ioe) {
			logger.debug("Error writeObjectStream", ioe);
			throw new SerializationException(ioe);
		}
	}

	public <T> void write(OutputStream output, T object) throws SerializationException {
		try {
			JsonGenerator jgen = factory.createGenerator(output);
			mapper.writeValue(jgen, object);
		} catch (JsonGenerationException ge) {
			logger.debug("Error write", ge);
			throw new SerializationGenerationException(ge);
		} catch (JsonMappingException me) {
			logger.debug("Error write", me);
			throw new SerializationMappingException(me);
		} catch (IOException ioe) {
			logger.debug("Error write", ioe);
			throw new SerializationException(ioe);
		}
	}

	public <T> List<T> readList(InputStream input, Class<T> type) throws SerializationException {
		try {
			List<T> lista = new ArrayList<T>();
			JsonParser jpar = factory.createJsonParser(input);
			while (jpar.nextToken()!=null) {
				T o = mapper.readValue(jpar, type);
				if (o!=null) lista.add(o);
			}
			return lista;
		} catch (JsonParseException pe) {
			logger.debug("Error readList", pe);
			throw new SerializationParseException(pe);
		} catch (JsonMappingException me) {
			logger.debug("Error readList", me);
			throw new SerializationMappingException(me);
		} catch (IOException ioe) {
			logger.debug("Error readList", ioe);
			throw new SerializationException(ioe);
		}
	}

	public <T> T read(InputStream input, Class<T> type) throws SerializationException {
		try {
			T o = null;
			JsonParser jpar = factory.createJsonParser(input);
			if (jpar.nextToken()!=null) o = mapper.readValue(jpar, type);
			return o;
		} catch (JsonParseException pe) {
			logger.debug("Error read", pe);
			throw new SerializationParseException(pe);
		} catch (JsonMappingException me) {
			logger.debug("Error read", me);
			throw new SerializationMappingException(me);
		} catch (IOException ioe) {
			logger.debug("Error read", ioe);
			throw new SerializationException(ioe);
		}
	}

	public <T> T read(String content, Class<T> type) throws SerializationException {
		try {
			T o = null;
			JsonParser jpar = factory.createJsonParser(content);
			if (jpar.nextToken()!=null) o = mapper.readValue(content, type);
			return o;
		} catch (JsonParseException pe) {
			logger.debug("Error read", pe);
			throw new SerializationParseException(pe);
		} catch (JsonMappingException me) {
			logger.debug("Error read", me);
			throw new SerializationMappingException(me);
		} catch (IOException ioe) {
			logger.debug("Error read", ioe);
			throw new SerializationException(ioe);
		}
	}

	public <T> void readObjectStream(InputStream input,
			ObjectOutputFlow<T> output, Class<T> type) throws SerializationException {
		try {
			JsonParser jpar = factory.createJsonParser(input);
			while (jpar.nextToken()!=null) {
				T o = mapper.readValue(jpar, type);
				if (o!=null) output.write(o);
			}
		} catch (DataIOException dioe) {
			logger.debug("Error readObjectStream", dioe);
			throw new UncategorizedSerializationException(dioe);
		} catch (JsonParseException pe) {
			logger.debug("Error readObjectStream", pe);
			throw new SerializationParseException(pe);
		} catch (JsonMappingException me) {
			logger.debug("Error readObjectStream", me);
			throw new SerializationMappingException(me);
		} catch (IOException ioe) {
			logger.debug("Error readObjectStream", ioe);
			throw new SerializationException(ioe);
		}
	}

}
