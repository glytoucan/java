package org.glytoucan.ws.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.glytoucan.ws.api.ErrorCodes;
import org.glytoucan.ws.api.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenIDAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler
{

	public static Logger logger=(Logger) LoggerFactory.getLogger("org.glytoucan.ws.security.OpenIDAuthenticationFailureHandler");
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authEx)
			throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		
		Throwable mostSpecificCause = authEx.getCause();
	    ErrorMessage errorMessage;
	    if (mostSpecificCause != null) {
	    	//String exceptionName = mostSpecificCause.getClass().getName();
	    	String message = mostSpecificCause.getMessage();
	        errorMessage = new ErrorMessage();
	        List<String> errors = new ArrayList<>();
	        errors.add(message);
//	        if (mostSpecificCause instanceof ) {
//	        	errors.addAll(((JanrainException)mostSpecificCause).fieldErrors);
//	        	
//	        } else {
//	        	Throwable cause = mostSpecificCause.getCause();
//	        	if (cause instanceof JanrainException) {
//	        		errors.addAll(((JanrainException)cause).fieldErrors);
//	        	}
//	        }
	        errorMessage.setErrors(errors);
	    } else {
	    	errorMessage = new ErrorMessage(authEx.getMessage());
	    }
	    logger.warn("Unauthorized: {}" + errorMessage);
	    errorMessage.setStatus(HttpStatus.UNAUTHORIZED.value());
	    errorMessage.setErrorCode(ErrorCodes.UNAUTHORIZED);
	    
	    response.sendRedirect("http://test.glytoucan.org/?message=Authorization.Failed." + errorMessage.toString());
/*	    
		String acceptString = request.getHeader("Accept");
		if (acceptString.contains("xml")) {
			response.setContentType("application/xml;charset=UTF-8");
			response.setStatus(HttpStatus.UNAUTHORIZED.value());           
			PrintWriter out = response.getWriter();    
			try {
				JAXBContext errorContext = JAXBContext.newInstance(ErrorMessage.class);
				Marshaller errorMarshaller = errorContext.createMarshaller();
				errorMarshaller.marshal(errorMessage, out);  
			} catch (JAXBException jex) {
				logger.error("Cannot generate error message in xml: {}", jex.getStackTrace());
			}
 		} else if (acceptString.contains("json")) {
			ObjectMapper jsonMapper = new ObjectMapper();          
			response.setContentType("application/json;charset=UTF-8");         
			response.setStatus(HttpStatus.UNAUTHORIZED.value());           
			PrintWriter out = response.getWriter();
			out.print(jsonMapper.writeValueAsString(errorMessage));       
		} else {
			response.sendError (HttpStatus.UNAUTHORIZED.value(), request.getUserPrincipal() + " is not allowed to access " + request.getRequestURI() + ": " + authEx.getMessage());
		}
		*/
	}

}
