package com.veisite.vegecom.service.impl.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.veisite.vegecom.model.FamiliaArticulo;

@Repository
public class FamiliaArticuloDAO {
	
	// TODO [implements]
	private FamiliaArticulo test = createTestFamiliaArticulo();
	
	private FamiliaArticulo createTestFamiliaArticulo() {
		FamiliaArticulo fa = new FamiliaArticulo();
		fa.setId(1L);
		fa.setNombre("Test (NoImpl)");
		return fa;
	}

	public FamiliaArticulo getById(Long id) {
	      return test;
	}

	public FamiliaArticulo save(FamiliaArticulo familiaArticulo) {
		return test;
	}
	
	public List<FamiliaArticulo> getList() {
		List<FamiliaArticulo> lfa = new ArrayList<FamiliaArticulo>();
		lfa.add(test);
		return lfa;
	}

	public FamiliaArticulo getByNombre(String nombre) {
		return test;
	}

}
