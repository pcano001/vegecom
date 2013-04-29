package com.veisite.vegecom.rest.client;

import java.util.Collection;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.veisite.vegecom.rest.security.RestApiKeySession;
import com.veisite.vegecom.rest.security.RestSecurity;

public class RestShiroSecurityManager extends DefaultSecurityManager {

	public RestShiroSecurityManager() {
	}

	public RestShiroSecurityManager(Realm singleRealm) {
		super(singleRealm);
	}

	public RestShiroSecurityManager(Collection<Realm> realms) {
		super(realms);
	}

	/* (non-Javadoc)
	 * @see org.apache.shiro.mgt.DefaultSecurityManager#onSuccessfulLogin(org.apache.shiro.authc.AuthenticationToken, org.apache.shiro.authc.AuthenticationInfo, org.apache.shiro.subject.Subject)
	 */
	@Override
	protected void onSuccessfulLogin(AuthenticationToken token,
			AuthenticationInfo info, Subject subject) {
		super.onSuccessfulLogin(token, info, subject);
		RestApiKeySession apiKey = (RestApiKeySession) info.getCredentials();
		Session session = subject.getSession();
		session.setAttribute(RestSecurity.APIKEY_KEY, apiKey.getApiKey());
		session.setAttribute(RestSecurity.SECRETSESSION_KEY, apiKey.getSecret());
	}

}
