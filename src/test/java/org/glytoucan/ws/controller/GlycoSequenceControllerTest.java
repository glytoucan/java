package org.glytoucan.ws.controller;

import static org.junit.Assert.*;

import org.glycoinfo.batch.search.wurcs.SubstructureSearchSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.search.SearchSparqlBean;
import org.glytoucan.ws.Application;
import org.glytoucan.ws.api.GlycoSequence;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@ComponentScan(basePackages = {"org.glytoucan.ws"})
@EnableAutoConfiguration
public class GlycoSequenceControllerTest {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger("org.glytoucan.ws.controller.GlycoSequenceControllerTest");

	@Autowired
	GlycoSequenceController glycoSequenceController;

	@Test
	public void testRetrieve() throws Exception { 
		GlycoSequence gs = glycoSequenceController.retrieve("G00030MO");
		logger.debug(gs.getSequence() + gs.getPrimaryId());
		assertEquals("G00030MO", gs.getPrimaryId());
		assertEquals("WURCS%3D2.0%2F4%2C7%2C6%2F%5Bu2122h_2*NCC%2F3%3DO%5D%5Ba2122h-1b_1-5_2*NCC%2F3%3DO%5D%5Ba1122h-1b_1-5%5D%5Ba1122h-1a_1-5%5D%2F1-2-3-4-2-4-2%2Fa4-b1_b4-c1_c3-d1_c6-f1_e1-d2%7Cd4_g1-f2%7Cf4", gs.getSequence());
	}
	
//	@Test
//	public void testRetrieveMotif() throws Exception {
//		GlycoSequence gs = glycoSequenceController.retrieveMotif();
//		logger.debug(gs.getSequence() + gs.getPrimaryId());
//	}
}