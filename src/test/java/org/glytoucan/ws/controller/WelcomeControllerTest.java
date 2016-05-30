package org.glytoucan.ws.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.glytoucan.ws.Application;
import org.glytoucan.ws.model.XmlUrl;
import org.glytoucan.ws.model.XmlUrlSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ExtendedModelMap;

import org.junit.Assert;

@SpringApplicationConfiguration(classes = { Application.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackages = { "org.glytoucan.ws" })
@EnableAutoConfiguration
public class WelcomeControllerTest {
  private static final Log logger = LogFactory.getLog(WelcomeControllerTest.class);

	@Autowired
	WelcomeController welcome;
	
	@Autowired
	SparqlDAO sparqlDao;

	@Test
	public void testWelcome() throws Exception {
		ExtendedModelMap model = new ExtendedModelMap();
		String result = welcome.welcome(model);
		assertEquals("index", result);
		logger.debug(result);
	}
	
	@Test
	public void testSitemap() throws SparqlException {
//    ExtendedModelMap model = new ExtendedModelMap();
	  XmlUrlSet result = welcome.main();
    Assert.assertNotNull(result);
    logger.debug(result);
    ArrayList<XmlUrl> list = (ArrayList<XmlUrl>) result.getXmlUrls();
    logger.debug(list.size());

    SelectSparql ss = new SelectSparqlBean();
    ss.setPrefix("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n" +
    "PREFIX glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#>\n");
    try {
      ss.setSelect("count( distinct ?" + Saccharide.PrimaryId + ") as ?count");
    } catch (SparqlException e) {
    }
    ss.setFrom("FROM <http://rdf.glytoucan.org>\nFROM <http://rdf.glytoucan.org/core>");
    ss.setWhere("?s a glycan:saccharide .\n" + 
    "?s glytoucan:has_primary_id ?" + Saccharide.PrimaryId + " .\n");
    
    List<SparqlEntity> countSE = sparqlDao.query(ss);
    SparqlEntity se = countSE.iterator().next();
    String count = se.getValue("count");
    
    Assert.assertEquals(count, result.getXmlUrls().size());
	}
}