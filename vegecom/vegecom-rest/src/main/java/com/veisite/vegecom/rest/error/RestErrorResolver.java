package com.veisite.vegecom.rest.error;

public interface RestErrorResolver {

	public RestError resolve(Exception ex);
	
}
