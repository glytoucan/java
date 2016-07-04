package org.glytoucan.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.fromi.openidconnect.security.OpenIDConnectAuthenticationFilter;

public class GlytoucanConnectAuthenticationFilter extends OpenIDConnectAuthenticationFilter {

	static final Logger logger = LoggerFactory.getLogger(GlytoucanConnectAuthenticationFilter.class);
	
    protected GlytoucanConnectAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }
 
}
