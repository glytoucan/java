package org.glytoucan.ws.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.glytoucan.ws.api.Confirmation;
import org.glytoucan.ws.api.Greeting;
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

@Controller
@Api(value="/statuscheck", description="Status Check request")
@RequestMapping ("/statuscheck")
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value="/greeting", method=RequestMethod.GET)
	@ApiOperation(value="Check if the user's credentials are acceptable", response=Greeting.class, notes="If the user is authorized, this does not necessarily mean that s/he is allowed to access all the resources")
    @ApiResponses (value={@ApiResponse(code=203, message="User is accepted")})
    public @ResponseBody Greeting greeting(@ApiParam(required=true, value="login name of the user") @RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
}