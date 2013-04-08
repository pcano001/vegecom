package com.veisite.vegecom.service.impl.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.veisite.vegecom.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
}
