package com.veisite.vegecom.model;

import java.io.Serializable;

import com.veisite.utils.bean.Bean;

public abstract class ModelObject extends Bean implements Serializable {
	
	/**
	 * serial
	 */
	private static final long serialVersionUID = -1997473386822676685L;

	public abstract Object getId();
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof ModelObject))
			return false;
		return getId().equals(((ModelObject) obj).getId());
	}
	
}
