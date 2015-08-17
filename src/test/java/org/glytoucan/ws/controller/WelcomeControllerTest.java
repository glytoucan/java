package org.glytoucan.ws.controller;

import static org.junit.Assert.assertEquals;

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
import org.springframework.ui.ExtendedModelMap;

@SpringApplicationConfiguration(classes = { Application.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackages = { "org.glytoucan.ws" })
@EnableAutoConfiguration
public class WelcomeControllerTest {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger("org.glytoucan.ws.controller.D3ControllerTest");

	@Autowired
	WelcomeController welcome; // D3という名前でD3Controllerを定義している

	@Test
	public void testWelcome() throws Exception {
		ExtendedModelMap model = new ExtendedModelMap();
		String result = welcome.welcome(model);
		assertEquals("index", result);
		logger.debug(result);
	}
	
	
	
//	@Test
//	public void testnullD3() throws Exception {
//
//		D3_Tree_json result = D3.retrieve("");
//		ObjectMapper mapper = new ObjectMapper();
//		logger.debug(mapper.writeValueAsString(result));
//	}

}