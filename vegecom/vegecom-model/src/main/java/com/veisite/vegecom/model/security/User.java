package com.veisite.vegecom.model.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.veisite.vegecom.model.ModelObject;

@Entity
@Table(name="users")
public class User extends ModelObject {

	/**
	 * serial
	 */
	private static final long serialVersionUID = -7854963605402459676L;

	@Id
    @Column(name = "id", length=50)
    private String id;

    @Column @NotNull
    private String name;
    
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
