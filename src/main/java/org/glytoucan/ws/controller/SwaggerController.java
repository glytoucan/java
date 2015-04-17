package org.glytoucan.ws.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.knappsack.swagger4springweb.controller.ApiDocumentationController;

@Controller
public class SwaggerController extends ApiDocumentationController {

	public static Logger logger=(Logger) LoggerFactory.getLogger("org.glytoucan.ws.controller.SwaggerController");

    public SwaggerController() {
        setBaseControllerPackage("org.glytoucan.ws.controller");
        setBaseModelPackage("org.glytoucan.ws.api");
        setApiVersion("v1");
    }

   @RequestMapping(value = "/documentation", method = RequestMethod.GET)
   public String documentation() {
   		logger.debug("documentation");
        return "documentation";
    }
}