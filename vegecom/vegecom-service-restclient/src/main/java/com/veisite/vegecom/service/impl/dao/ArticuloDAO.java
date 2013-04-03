package com.veisite.vegecom.service.impl.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.model.Articulo;

@Repository
public class ArticuloDAO {

	public static final Logger logger = LoggerFactory.getLogger(ArticuloDAO.class);

	// TODO [implements]
	private Articulo test = createTestArticulo();
	
	private Articulo createTestArticulo() {
		Articulo a = new Articulo();
		a.setId(1L);
		a.setCodigo("TEST01");
		a.setDescripcion("Articulo Test (NoImpl)");
		a.setId(1L);
		return a;
	}
	
	public Articulo getById(Long id) {
		return test;
	}
	
	public Articulo save(Articulo articulo) {
		return articulo;
	}

	/**
	 * Devuelve la lista de articulo.
	 * 
	 * @return
	 */
	public List<Articulo> getList() {
		List<Articulo> lista = new ArrayList<Articulo>();
		lista.add(test);
		return lista;
	}

	
	/**
	 * 	Recupera la lista de articulo y la envia a un buffer de lectura/escritura
	 * @param output salida hacia la que se dirigen los objectos. 
	 * @throws DataIOException
	 */
	public void writeListTo(ObjectOutputFlow<Articulo> output) throws DataIOException {
		List<Articulo> lista = new ArrayList<Articulo>();
		lista.add(test);
		for (Articulo a : lista) output.write(a);
	}
	
	
}
