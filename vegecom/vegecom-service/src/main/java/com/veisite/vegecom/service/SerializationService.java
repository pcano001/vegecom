package com.veisite.vegecom.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectInputFlow;
import com.veisite.utils.dataio.ObjectOutputFlow;

public interface SerializationService {
	
	public <T extends Object> void writeList(OutputStream output, List<T> lista)  throws IOException;
	
	public <T extends Object> void writeObjectStream(OutputStream output, ObjectInputFlow<T> input) 
			throws IOException, DataIOException;

	public <T extends Object> void write(OutputStream output, T object) throws IOException;

	public <T extends Object> List<T> readList(InputStream input, Class<T> type) throws IOException;
	
	public <T extends Object> void readObjectStream(InputStream input, ObjectOutputFlow<T> output, Class<T> type) 
			throws IOException, DataIOException;

	public <T extends Object> T read(InputStream input, Class<T> type) throws IOException;

}
