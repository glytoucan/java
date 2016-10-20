package org.glytoucan.web.controller;

import org.glytoucan.web.Application;
import org.glytoucan.web.controller.GRABGraphController;
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
public class GRABGraphControllerTest {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger("org.glytoucan.ws.controller.GRABGraphControllerTest");

	@Autowired
	GRABGraphController GRABGraph; // GRABGraphという名前でGRABGraphControllerを定義している

	@Test
	public void testGRABGraph() throws Exception {
		
		//String result = GRABGraph.Graphretrieve("G99981ST");
		//String result = GRABGraph.Graphretrieve("G37236TY");
		String result = GRABGraph.Graphretrieve("G63977XF");
		logger.debug("result:"+result);
//		ObjectMapper mapper = new ObjectMapper();
//		logger.debug(mapper.writeValueAsString(result));
	}

}