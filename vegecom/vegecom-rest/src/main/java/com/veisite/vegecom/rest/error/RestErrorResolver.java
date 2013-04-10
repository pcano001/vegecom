package com.veisite.vegecom.rest.error;

import java.util.Locale;

public interface RestErrorResolver {

	public RestError resolve(Exception ex, Locale locale);
	
}
