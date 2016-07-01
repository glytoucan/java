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
    Assert.assertEquals("iVBORw0KGgoAAAANSUhEUgAAALoAAABSCAIAAABse1lJAAADwElEQVR42u2bIW/qUBSAb6bIgphEIBAIRMVEBXKiAoFATCLIgphATCAmEBOIiQkEYmJLtgQ5MVHRJSSIiQnEREUl4v0ARAUC8d5hJMsCg0FHW3rf96VmarnnfPecc2+L+guwMYoQALoAugC6ALoAugC6EAJAF0AXQBdAF0AXQBdCAOgC6ALoAugC6ALoQggAXQBdAF0AXQBdAF0IAaALoAugC6ALhMh0Ov1fdBkOh61Wy7Is9YFhGKVSqd1ue54X6bJ3QSwJe39/Pz4+jsuY6NY8GAyKRZXPq1ZLOY7yPDUaKddVtq0uLlQ2q8QbiUVkunysPfgTiy5iSaFQuLm50bm6yCKbzWYup3q9ldGfTlW3qzIZFU0sEqqL4ziii87NSNZWqUjlUOPxzzn480eZpmo0GujyLZeXl1dXVzqPupL7cnlWPDZMg+/PjAm7xiRUl1qtdn19ra0utm3LsCIGbJUJqTGZTCbUOSahutTr9dvbWz11kTZkGLNJNkAyZI6RyRddFpDSEkGnjkcXmcvkKBQsGdK8stlseKfr5I66pmnqqYtUzk4neD7OzlS73UaXr/i+n06nJ5OJhrrIkc91g+fj/j7EfpRQXYSTk5Pn52cNdTk42HrI/fq8vMzufNFlgU6nI+cjDXX5ZUre3lSo/F6XuDg8PNTwJUAqlZpMguej31fSzqguyxSLxX6/r5su+Xze84Ln4/FRWZaFLsvICeD8/Fw3XarV6t1d8Hw0GqrVaqHLMp7n5XI53XR5enqyrOD5kOI0HA5/I8SaLw0SrYtwdHQ0Go200mUymcgmeH0NkoxeT0mH3sqAJRtW/qmBLtKmZTdqpYvw8PBgmlu8X5w/47ESzwaDwSoJtv06KQJdFlQOW6ZmsxnLu8bQt8jp6Wm9vt31f6lUknCsyfp+6rLm3+2cRqMRy5cMoS/M931pK2LMJjXG91W5XK5UKt/eK3zVZavxJYLZJWJdarVat9vVUJe5MVJjTNNcP8fY9my8lX2z6g7qs85vW1o0qy7zofA354C91uVzjpFFyowmp2vPU/MbPCknrqs6ndlgaxiGbdsb9pS9bUYRfPUtbSiu99KRjveyLWSer1arUkVSqZSENZ1OFwqFer3uOM6PF9ub67LJyWjnvwRYGHVDkmY8HmcyGdd19ddlF3drm1aX2H/hER5iTGwpSKIuWkqQjBQkSxdEQZfgEyugC6ALoAugCwC6ALoAugC6ALoAugCgC6ALoAugC6ALoAsAugC6ALoAugC6ALoAoAugC6AL7C//AEH3TyE/br3UAAAAAElFTkSuQmCC", result);
  }
}