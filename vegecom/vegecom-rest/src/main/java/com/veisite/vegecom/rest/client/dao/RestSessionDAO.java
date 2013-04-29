package com.veisite.vegecom.rest.client.dao;

import com.veisite.vegecom.rest.security.RestApiKeySession;

public interface RestSessionDAO extends RestDAO {

	public RestApiKeySession createSession(String user, String password);
	
}
