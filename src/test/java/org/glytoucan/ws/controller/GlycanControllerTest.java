package org.glytoucan.ws.controller;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import javax.xml.bind.DatatypeConverter;

/*
import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
 */
import org.apache.http.HttpStatus;
import org.glycoinfo.batch.search.wurcs.SubstructureSearchSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.glycan.wurcs.GlycoSequenceResourceEntryContributorSelectSparql;
import org.glytoucan.ws.Application;
import org.glytoucan.ws.api.Glycan;
import org.glytoucan.ws.api.GlycanInput;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.jayway.restassured.RestAssured;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class GlycanControllerTest {

    @Value("${local.server.port}")
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }
    
    public static Logger logger = (Logger) LoggerFactory
			.getLogger("org.glytoucan.ws.controller.GlycanControllerTest");

    @Autowired
	GlycanController gc;
    

	
	@Test
	public void testG00031MO() throws Exception {
		Glycan result = gc.getGlycan("G00031MO");
		logger.debug("RESULT>" + result.getStructure() +"<");
	}
	
	@Test
	public void testG00026MOProtocolScope() throws Exception {
		Glycan result = gc.getGlycan("G00026MO");
		logger.debug("RESULT>" + result.getStructure() +"<");
		result = gc.getGlycan("G00031MO");
		logger.debug("RESULT>" + result.getStructure() +"<");
	}
	
    @Test
    public void getG00031MO() {
        given().redirects().follow(false).when().get("/glycans/G00031MO").then().statusCode(HttpStatus.SC_OK);
    }
    
	@Bean
	SelectSparql glycoSequenceSelectSparql() {
		GlycoSequenceResourceEntryContributorSelectSparql sb = new GlycoSequenceResourceEntryContributorSelectSparql();
		sb.setFrom("FROM <http://rdf.glytoucan.org>\nFROM <http://rdf.glytoucan.org/sequence/wurcs>");
		return sb;
	}

  @Test
  public void getImage() throws Exception {
    ResponseEntity<byte[]> re = gc.getGlycanImage("G00031MO", "png", "cfg", "extended");
    byte[] body = re.getBody();
    String result = DatatypeConverter.printBase64Binary(body);
    
    logger.debug("strResult:>" + result);
    logger.debug("<html><img src=\"data:image/png;base64," + result + "\"></html>");
    Assert.assertEquals("iVBORw0KGgoAAAANSUhEUgAAAIYAAABSCAIAAAAetDv/AAAC10lEQVR42u3asYvaUBwH8G83B/+AQA8q1EFoBuGEujdQoVIcHB1CyVRCpwxSMjg4OHjDgctRKN7ucEMKdsrQMYODg8MN3W9QOOEEHa4/Ynu0BalGzUtz3y9vD3mf/N7vvae4ZxIWcApIwpCEJAxJSMKQhCQMSRiSkIQhCUkYkpCEIQlJGJIwJCEJQxKSMCQhCUMShiQkYUhCkhRmtVo9FpIgCFzXNQwDYXRdr1Qq7XZ7Mpkkx2M0GhWLRVUq8ZH4vg+8BJ4DHwEPGAPXwAi4Aj4AT8VG5iIJ9VEoFLrdbpqrRF7ScRzgGXAJLDeMO+Ac0BTOxTrD4VBI0rxwybsBb4HXwM1mj4fxHTi1bVshSbPZbLVaaW7vMr/Am7AIltuNqagorBXTNDudTmpJPM8Lm8d0a4+ftaJpWhL6StpIwiXrRdi9l7uPc+n2ambkz6SKRPpkuMVaRhp3Jycn8e+M/2JQonLE51mWBZxFJZFhynlFoYcqlSM+TLaS4bEjMsmnmNeuTVOfHhLgye6N/ffxRc72JDn4OrDcY3xDjHkUJJlMBrjdg+SrLH2skkMmn8+HF1mRST4bhsH2fsg0Gg3gYg+S967rchN8yAwGA+BVZBIpsiAIeFQ8ZBaLRS6XA/xIJJflclntxYYsm/JVpe2Oq9/vA6e73Dmux41Y+r6vlsRxHCX3j0cvzHq9Drzb6SpFTogyHcqv/2zbVnJLf3SS+XwuS1Cosk2tTKvVaq1WU/gL0kNM0+z1eikkWatIrZRKpX/1lStp6fJtJsFj3QjV7C9ie5L0FXnJ8I8QF+F55fbXD1Yj4EwqSdd1z/PukxFZsuQbUvJoxPzpyR5GzitSDeHZHtlsVo7olmUNh8MkFMc6s9lM07TxeJx+kv8ooqLq0SRJXEhCEoYkJGFIQhKGJCRhSMKQhCQMSUjCkIQkDElIwpCEIQlJGJKQhCEJSRiSMCQhCbNTfgBhmNNUvEd3pQAAAABJRU5ErkJggg==", result);
  }
}