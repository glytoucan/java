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
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
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

	String[] requiredFields = {SelectSparql.PRIMARY_KEY, "email", "givenName", "familyName", "verifiedEmail"};

	/**
	 * 
	 * Newly registered user, store data into RDF.
	 * 
	 * This is an important method as the Contributor information was pre-existing and yet the authentication method was handled outside of RDF.
	 * 
	 * This has changed with rdf glytoucan.  So in order to make use of/map the previously created Contributor linkages, it's necessary to associate
	 * a newly logged in Google user to the pre-existing Contributor class.  When newly registering, all that is necessary would then be the Contributor ID. 
	 * 
	 * In order to retrieve the ID (to registered), it is assumed the user would be logged in, thus the email address should be available.
	 * The email address would then be used to reference the id by the mapping data created by this method.
	 * 
	 * mapping process is found here:
	 * org.glycoinfo.rdf.service.impl.UserProcedure line 168:
	 * 		contributorProcedure.setName(getSparqlEntity().getValue(org.glycoinfo.rdf.service.UserProcedure.givenName) + " " + getSparqlEntity().getValue(org.glycoinfo.rdf.service.UserProcedure.familyName));
	 * 
	 * The previously registered foaf name was a First name, blank space, and last name.  So if the exact same words are used in google, it will be mapped automatically.
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
		
		try {
			userProcedure.add(sparqlentity);
		} catch (SparqlException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
//        MimeMessagePreparator preparator = new MimeMessagePreparator() {
//
//        	public void prepare(MimeMessage mimeMessage) throws Exception {
//
//                mimeMessage.setRecipient(Message.RecipientType.TO,
//                        new InternetAddress(userInfo.getEmail()));
//                mimeMessage.setFrom(new InternetAddress("admin@glytoucan.org"));
//                mimeMessage.setSubject("registration:" + userInfo.getGivenName() + " " + userInfo.getEmail());
//                mimeMessage.setText(
//                        "new user info:\nFirst Name:" + userInfo.getGivenName() + "\nLast Name:"
//                            + userInfo.getFamilyName() + "\nemail:" + userInfo.getEmail() + "\nverified:" + userInfo.getVerifiedEmail());
//            }
//        };
//
//        try {
//            this.mailSender.send(preparator);
//        }
//        catch (MailException ex) {
//            logger.error(ex.getMessage());
//        }
        // pass processing back to SavedRequestAware parent.
		super.onAuthenticationSuccess(request, response, authentication);
	}
}