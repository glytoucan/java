package org.glytoucan.web.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.isNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.isEmptyOrNullString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.glycoinfo.rdf.service.GlycanProcedure;
import org.glytoucan.web.Application;
import org.glytoucan.web.controller.WelcomeController;
import org.glytoucan.web.model.XmlSiteMap;
import org.glytoucan.web.model.XmlSiteMapSet;
import org.glytoucan.web.model.XmlUrl;
import org.glytoucan.web.model.XmlUrlSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.WebApplicationContext;
import org.junit.Assert;
import org.junit.Before;

@SpringApplicationConfiguration(classes = { Application.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackages = { "org.glytoucan.ws" })
@EnableAutoConfiguration
@WebAppConfiguration
public class WelcomeControllerTest {
  private static final Log logger = LogFactory.getLog(WelcomeControllerTest.class);

	@Autowired
	WelcomeController welcome;
	
	@Autowired
	SparqlDAO sparqlDao;

	@Autowired
  private WebApplicationContext wac;
	
  private MockMvc mockMvc;

  @Autowired
  GlycanProcedure glycanProcedure;
  
  private static int iTotal = 0;
  
  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    SparqlEntity countSE = glycanProcedure.getCount();
    String total = countSE.getValue("total");
    iTotal = Integer.parseInt(total);
  }

	@Test
	public void testWelcome() throws Exception {
		ExtendedModelMap model = new ExtendedModelMap();
		String result = welcome.welcome(model);
		assertEquals("index", result);
		logger.debug(result);
	}
	
	@Test
	public void testSitemapIndex() throws SparqlException {
  	  XmlSiteMapSet result = welcome.sitemapIndex();
	    Assert.assertNotNull(result);
	    int num = (int)Math.ceil(iTotal/50000);
	    logger.debug("NUM:>" + num);
	    logger.debug("result.getXmlSiteMaps().size():>" + result.getXmlSiteMaps().size());
	    
	    Assert.assertTrue(result.getXmlSiteMaps().size() == (num + 2));
	    		
	    logger.debug(result);
	}
	
	@Test
	public void testMockSiteMap() throws Exception {
		MvcResult result = mockMvc.perform(get("/sitemap.xml"))
		.andExpect(status().isOk())
		.andReturn()
		
		;
		
		
		String content = result.getResponse().getContentAsString();
		String first1k = content.substring(0, 1000);
		logger.debug(first1k);
		Assert.assertTrue(first1k.contains("glytoucan.org"));
//		.andExpect(view().name("sitemap"))
//		.andExpect(
//				model().attribute("listAccs", isNotNull()));
//						hasProperty("sequence", isEmptyOrNullString())));
	}
	
	@Test
	public void testMockSiteMap0() throws Exception {
		MvcResult result = mockMvc.perform(get("/sitemap0.xml"))
		.andExpect(status().isOk())
		.andReturn()
		
		;
		
		
		String content = result.getResponse().getContentAsString();
		String first1k = content.substring(0, 1000);
		logger.debug(first1k);
		Assert.assertTrue(first1k.contains("glytoucan.org"));
//		.andExpect(view().name("sitemap"))
//		.andExpect(
//				model().attribute("listAccs", isNotNull()));
//						hasProperty("sequence", isEmptyOrNullString())));
	}
	
	@Test
	public void testMockSiteMap1() throws Exception {
		MvcResult result = mockMvc.perform(get("/sitemap1.xml"))
		.andExpect(status().isOk())
		.andReturn()
		
		;
		
		
		String content = result.getResponse().getContentAsString();
		String first1k = content.substring(0, 1000);
		logger.debug(first1k);
		Assert.assertTrue(first1k.contains("glytoucan.org"));
//		.andExpect(view().name("sitemap"))
//		.andExpect(
//				model().attribute("listAccs", isNotNull()));
//						hasProperty("sequence", isEmptyOrNullString())));
	}
	
	@Test
	public void testMockSiteMapTop() throws Exception {
		MvcResult result = mockMvc.perform(get("/sitemapTop.xml"))
		.andExpect(status().isOk())
		.andReturn()
		;
		
		String content = result.getResponse().getContentAsString();
		logger.debug(content);
		Assert.assertTrue(content.contains("glytoucan.org"));
		Assert.assertTrue(content.contains("Motifs/listAll"));
		Assert.assertTrue(content.contains("Structures"));
		Assert.assertTrue(content.contains("Structures/graphical"));
		Assert.assertTrue(content.contains("Structures/structureSearch"));
		Assert.assertTrue(content.contains("Motifs/search"));
		Assert.assertTrue(content.contains("Preferences/index"));
		Assert.assertTrue(content.contains("Preferences/ja"));
		Assert.assertTrue(content.contains("Preferences/ch1"));
		Assert.assertTrue(content.contains("Preferences/ch2"));
		Assert.assertTrue(content.contains("Preferences/fr"));
		Assert.assertTrue(content.contains("Preferences/de"));
		Assert.assertTrue(content.contains("Preferences/ru"));
		
//		.andExpect(view().name("sitemap"))
//		.andExpect(
//				model().attribute("listAccs", isNotNull()));
//						hasProperty("sequence", isEmptyOrNullString())));
	}
	
	@Test
	public void testSitemap() throws SparqlException {
		ExtendedModelMap model = new ExtendedModelMap();
	  XmlUrlSet result = welcome.main(model, "0");
    Assert.assertNotNull(result);
    logger.debug(result);
    ArrayList<XmlUrl> list = (ArrayList<XmlUrl>) result.getXmlUrls();
    logger.debug(list.size());

    Assert.assertTrue(result.getXmlUrls().size() == 50000);
	}

	@Test
	public void testSitemap2() throws SparqlException {
		ExtendedModelMap model = new ExtendedModelMap();
	  XmlUrlSet result = welcome.main(model, "1");
    Assert.assertNotNull(result);
    logger.debug(result);
    Assert.assertTrue(result.getXmlUrls().size() <= 50000);

    int resultRows = iTotal - 50000;
    
    Assert.assertEquals(result.getXmlUrls().size(), resultRows);

	}

	
  @Test
  public void testSitemapHostname() throws Exception {
		ExtendedModelMap model = new ExtendedModelMap();
		  XmlSiteMapSet result = welcome.sitemapIndex();
		  Collection<XmlSiteMap> xmlsm = result.getXmlSiteMaps();
    for (Iterator iterator = xmlsm.iterator(); iterator.hasNext();) {
      XmlSiteMap xmlSM2 = (XmlSiteMap) iterator.next();
      String loc = xmlSM2.getLoc();
      logger.debug("loc:>" + loc);
      loc.contains("https://glytoucan.org");
    }
  }
  
	 @Test
	  public void testConnect() throws Exception {
	   mockMvc.perform(get("/connect?url=http://stanza.glytoucan.org/stanza/motif_list?&notation=cfg&page=motif_list"))
     .andExpect(status().isOk())
     .andExpect(content().string(containsString("/glycans/G00055MO")));
//     .andExpect(content(). string("/Structures/Glycans/G00055MO"));
	  }
	 
}