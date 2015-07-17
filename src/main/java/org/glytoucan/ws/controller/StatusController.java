package org.glytoucan.ws.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.ws.api.Confirmation;
import org.glytoucan.ws.api.Greeting;
import org.glytoucan.ws.security.OAuth2Client;
import org.glytoucan.ws.security.UserInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Api(value="/status", description="Status Management")
@RequestMapping ("/status")
public class StatusController {
	Log logger = LogFactory.getLog(StatusController.class);

    private static final String template = "Status, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value="/check", method=RequestMethod.GET)
	@ApiOperation(value="Check if the user's credentials are acceptable", response=Greeting.class, notes="If the user is authorized, this does not necessarily mean that s/he is allowed to access all the resources")
    @ApiResponses (value={@ApiResponse(code=203, message="User is accepted")})
    public @ResponseBody Greeting check(@ApiParam(required=true, value="login name of the user") @RequestParam(value="name", defaultValue="Green") String name) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	logger.debug("userinfo:>" + userInfo + "<");

        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name) + "\nemail:>" + userInfo.getEmail() + "<\nverified:>" + userInfo.getVerifiedEmail() + "<");
    }
    
    @RequestMapping("/test")
    public String test() {
    	logger.debug("start /test");
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	logger.debug("userinfo:>" + userInfo + "<");
        return "Welcome, " + userInfo.getName() 
        		+ ".  Your email is >" + userInfo.getEmail()
        		+ "<.\n And verified email is:>" + userInfo.getVerifiedEmail() + "<";
    } 

}