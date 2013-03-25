package com.veisite.vegecom.security;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecureBasicDataSource extends BasicDataSource {
	
	private static final Logger logger = LoggerFactory.getLogger(SecureBasicDataSource.class);
	
	public SecureBasicDataSource() {
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.dbcp.BasicDataSource#setPassword(java.lang.String)
	 */
	@Override
	public void setPassword(String password) {
		String p = "d5VdfGh78pcyds3BpcyBtbvgeSBwYXreNzd29yZC4dXNlcm5hreebcerGFz43vcwa";
		try {
			super.setPassword(new DesEncrypter(p).decrypt(password));
		} catch (Exception e) {
			logger.debug("Error configuring db password",e);
			throw new java.lang.UnknownError("security error");
		}
	}
	
}
