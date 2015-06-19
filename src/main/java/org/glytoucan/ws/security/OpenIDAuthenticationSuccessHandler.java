package org.glytoucan.ws.security;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class OpenIDAuthenticationSuccessHandler extends
		SavedRequestAwareAuthenticationSuccessHandler {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger("org.glytoucan.ws.security.OpenIDAuthenticationSuccessHandler");

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {
		// String token = request.getParameter("token");
		//
		// logger.debug("token:>" + token + "<");

		logger.debug("name:>" + authentication.getName());
		logger.debug("authentication:>" + authentication + "<");
		
		PreAuthenticatedAuthenticationToken auth = (PreAuthenticatedAuthenticationToken)authentication;
		UserInfo user =  (UserInfo) auth.getPrincipal();
		logger.debug("user.getId()>"+ user.getId() + "<");
		logger.debug("user.getFamilyName()>"+ user.getFamilyName() +"<");
		logger.debug("user.getName()>" + user.getName() + "<");

		OAuth2AccessToken token = (OAuth2AccessToken) auth.getCredentials();
		logger.debug("token:>" + token.getValue());
				
		// try {
		// UserDetails userDetails =
		// authenticationUserDetailsService.loadUserDetails(token);
		// } catch (JanrainException e)
		// {
		// throw new BadCredentialsException(e.getMessage(), e);
		// }
		response.sendRedirect(getDefaultTargetUrl() + URLEncoder.encode(authentication.getPrincipal().toString(), "UTF-8"));
	}
}