package org.glytoucan.web.controller;

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
import org.glytoucan.web.Application;
import org.glytoucan.web.api.Glycan;
import org.glytoucan.web.api.GlycanInput;
import org.glytoucan.web.controller.GlycanController;
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
    Assert.assertEquals("iVBORw0KGgoAAAANSUhEUgAAALoAAABSCAIAAABse1lJAAADuUlEQVR42u2bIXPqQBCATzIMojICwUwRiIiKCGQFAoFAVCIQiApEBaICUYGoqEAgKioqkBUVERGdQVQiKiIiEf0BiIgIxHvLY6bTgUJJIAm5930TU9W53e929y5B/QHYG0UIAF0AXQBdAF0AXQBdCAGgC6ALoAugC6ALoAshAHQBdAF0AXQBdAF0IQSALoAugC6ALoAugC6EANAF0AXQBdAFYmSxWPwvukyn036/X6vVzs9VsahM06zX64PBwPO8RJd9DFJJ2MfHx8XFRVrGJLfmyWRSrapyWfX7ynGU56nZTLmusm11c7NUR7yRWCSmy7+1R39S0UUsqVQqDw8POlcXWWSv1yuV1Hi8NfqLhRqNlGGoZGKRUV0cxxFddG5GsrZmUyqHms9/z8Hnp7Is1e120eVHbm9v7+7udB51JfeNxrJ47JkG318aE3eNyagu7Xb7/v5eW11s25ZhRQwIlQmpMYZhxDrHZFSXTqfz+Piopy7ShkxzOclGSIbMMTL5ossaUloS6NTp6CJzmRyFoiVDmlexWIzvdJ3dUdeyLD11kco5HEbPh5yuB4MBunzH9/1CoRAEgYa6yJHPdaPnQ7pYfP0oo7oIl5eXr6+vGuqSz4cecr8/opppmuiyxnA4lPORhrocmBI5H8XK4bqkRT6f1/AlQC6XC4Lo+fA8Je2M6rJJtVp9e3vTTZdyuSwpj5wPx1G1Wg1dNpETwPX1tW66tFqtp6fo+ej35emjyyae55VKJd10eXl5qdWi50OK03Q6PUSIHV8aZFoX4ezsbDabaaVLEASyCd7foyRjPFbSoUMZsGHD1j810EXatOxGrXQRnp+fLSvE+8XVM58r8WwymWyTIOzXSQnosqZy3DL1er1U3jXGvkWurq46nXDX//V6XcKxI+unqcuOf3d0ut1uKl8yxL4w3/elrYgx+9QY31eNRqPZbP54r/Bdl1DjSwKzS8K6tNvt0WikoS4rY6TGWJa1e46x7eV4K/tm2x3UV50PW1o0qy6rofCQc8BJ6/I1x8giZUaT07XnqdUNnpQT11XD4XKwNU3Ttu09e8rJNqMEvvqWNpTWe+lEx3vZFjLPt1otqSK5XE7CWigUKpVKp9NxHOfXi+39ddnnZHT0XwKsjboxSTOfzw3DcF1Xf12Ocbe2b3VJ/Rce8SHGpJaCLOqipQTZSEG2dEEUdIk+sQK6ALoAugC6AKALoAugC6ALoAugCwC6ALoAugC6ALoAugCgC6ALoAugC6ALoAsAugC6ALrA6fIXm4tQ6SrSlTEAAAAASUVORK5CYII=", result);
  }
}