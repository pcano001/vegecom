package com.veisite.vegecom.service.impl.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.veisite.vegecom.model.Municipio;
import com.veisite.vegecom.model.Provincia;

public interface MunicipioRepository extends JpaRepository<Municipio, String> {
	
	public List<Municipio> findByProvincia(Provincia provincia, Sort sort);

	public Municipio findByNombre(String nombre);

}
