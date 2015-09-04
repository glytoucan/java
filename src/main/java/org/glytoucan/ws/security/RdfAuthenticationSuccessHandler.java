package org.glytoucan.ws.security;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.scint.SelectScint;
import org.glycoinfo.rdf.service.UserProcedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class RdfAuthenticationSuccessHandler extends
		SavedRequestAwareAuthenticationSuccessHandler {
	public static Log logger = (Log) LogFactory
			.getLog(RdfAuthenticationSuccessHandler.class);

	@Autowired
	@Qualifier(value = "userProcedure")
	UserProcedure userProcedure;
	
	@Autowired(required=false)
	JavaMailSender mailSender;

	String[] requiredFields = {SelectScint.PRIMARY_KEY, "email", "givenName", "familyName", "verifiedEmail"};

	/**
	 * 
	 * Newly registered user, store data into RDF.
	 * 
	 * @see org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {
		
		// newly registered user
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();
        
		logger.debug("userinfo:>" + userInfo);
		Map<String, String> objectAsMap;
	    try {
			objectAsMap = BeanUtils.describe(userInfo);
		} catch (IllegalAccessException | InvocationTargetException
				| NoSuchMethodException e) {
			throw new ServletException(e);
		}
		objectAsMap.remove("picture");
		objectAsMap.remove("link");

		SparqlEntity sparqlentity = new SparqlEntity(userInfo.getId());

		sparqlentity.putAll(objectAsMap);
		
		sparqlentity.remove("id");

		Set<String> columns = sparqlentity.getColumns();
		logger.debug("columns:>" + columns + "<");
		if (!columns.containsAll(Arrays.asList(requiredFields))) {
			logger.error("fail");
		}
		
		userProcedure.setSparqlEntity(sparqlentity);
		try {
			userProcedure.addUser();
		} catch (SparqlException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}

        // pass processing back to SavedRequestAware parent.
		super.onAuthenticationSuccess(request, response, authentication);
	}
}