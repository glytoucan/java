package org.glytoucan.web.controller;

import org.glytoucan.web.Application;
import org.glytoucan.web.api.GRABTreeJson;
import org.glytoucan.web.controller.GRABController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringApplicationConfiguration(classes = { Application.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackages = { "org.glytoucan.web" })
@EnableAutoConfiguration
public class GRABControllerTest {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger("org.glytoucan.ws.controller.D3ControllerTest");

	@Autowired
	GRABController t_GRAB; // D3という名前でD3Controllerを定義している

	@Test
	public void testD3() throws Exception {

//		D3_Tree_json result = D3.D3retrieve("GxxxxxxB");
		GRABTreeJson result = t_GRAB.D3retrieve("G99981ST");
		ObjectMapper mapper = new ObjectMapper();
		logger.debug(mapper.writeValueAsString(result));
	}
	
//	@Test
//	public void testnullD3() throws Exception {
//
//		D3_Tree_json result = D3.retrieve("");
//		ObjectMapper mapper = new ObjectMapper();
//		logger.debug(mapper.writeValueAsString(result));
//	}

}