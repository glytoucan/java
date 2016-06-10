package org.glytoucan.ws.controller;

import org.glytoucan.ws.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

@SpringApplicationConfiguration(classes = { Application.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackages = { "org.glytoucan.ws" })
@EnableAutoConfiguration
public class UserControllerTest {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger("org.glytoucan.ws.controller.D3ControllerTest");

	@Autowired
	UserController userC;

	@Test
	public void testUser() throws Exception {
		ExtendedModelMap map = new ExtendedModelMap();
		RedirectAttributesModelMap redMap = new RedirectAttributesModelMap();
		String results = userC.profile(map, redMap);
		logger.debug(results);
	}
	
	@Test
	@Transactional
	public void testGenerateHash() throws Exception {
		ExtendedModelMap map = new ExtendedModelMap();
		RedirectAttributesModelMap redMap = new RedirectAttributesModelMap();
		String results = userC.profile(map, redMap);
		logger.debug(results);
	}
}