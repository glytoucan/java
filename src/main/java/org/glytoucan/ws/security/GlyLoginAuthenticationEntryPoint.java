package org.glytoucan.ws.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.glytoucan.ws.ErrorCodes;
import org.glytoucan.ws.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class GlyLoginAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
	
	public static Logger logger=(Logger) LoggerFactory.getLogger("org.glyspace.registry.security.MyBasicAuthenticationEntryPoint");
	
    private final String REDIRECT_URL = "/";

    public GlyLoginAuthenticationEntryPoint() {
    }

    public GlyLoginAuthenticationEntryPoint(String loginFormUrl) {
    	super(loginFormUrl);
    }
	
	@Override
	public void commence (HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
			throws IOException, ServletException {
		
		if (request.getRequestURI().endsWith("json")) {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		
		Throwable mostSpecificCause = authEx.getCause();
	    ErrorMessage errorMessage;
	    if (mostSpecificCause != null) {
	    	//String exceptionName = mostSpecificCause.getClass().getName();
	    	String message = mostSpecificCause.getMessage();
	        errorMessage = new ErrorMessage(message);
	    } else {
	    	errorMessage = new ErrorMessage(authEx.getMessage());
	    }
	    logger.warn("Unauthorized: {}", errorMessage);
	    errorMessage.setStatus(HttpStatus.UNAUTHORIZED.value());
	    errorMessage.setErrorCode(ErrorCodes.UNAUTHORIZED);
		String acceptString = request.getHeader("Accept");
		if (null == acceptString)
			acceptString="json";
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
		} else {
			super.commence (request, response, authEx);
		}
	}
	
	@Override
    public void afterPropertiesSet() throws Exception {
    }
}