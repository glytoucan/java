package org.glytoucan.ws.controller;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

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
    public void postImage() {
    	String sequence = "RES\\n"
				+ "1b:x-dglc-HEX-x:x\\n"
				+ "2b:b-dgal-HEX-1:5\\n"
				+ "3b:a-dgal-HEX-1:5\\n"
				+ "4b:b-dgal-HEX-1:5\\n"
				+ "5s:n-acetyl\\n"
				+ "LIN\\n"
				+ "1:1o(4+1)2d\\n"
				+ "2:2o(3+1)3d\\n"
				+ "3:3o(3+1)4d\\n"
				+ "4:4d(2+1)5n";
		String output2 = "{\"encoding\":\"glycoct\",\"structure\":\"" + sequence + "\"}"; 
//        given().redirects().follow(false).when().post("/glycans/image/glycan?format=png&notation=cfg&style=extended", "{\"encoding\":\"glycoct\",\"structure\":\"" + sequence + "\"}").then().statusCode(HttpStatus.SC_OK);
        
		String image = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAASIAAABSCAIAAAB/iU6UAAAFWElEQVR42u2cMWgjRxhGv1xljIorBTHEEBeCqDCcICoNEUQQE1y4dCGMChNEuEKFCSpcqHDhAx24cIgv2ODShQsdyGBQ4eIKFy5UqEih4rorLIjgBHaRjKQkGEda7a60s2vpPabcO3/Mzpt/ZjSS/gKAgBFdAIBmAGgGAGgGgGYAaAYAaAaAZgCAZgBoBoBmAIBmAGgGgGYAgGYAaAYAaAaAZgBoBgBoBoBmAGgGAGgGgGYAgGYAaAaAZgCAZgBoBmCVh4eHGdRM08BmX9zc3JRKpUwmM/jTyWQym82Wy+Vmsxn6ECHbhNze3q6uroZlWrCa9f9//82aZvV6PZ3WyopKJdVqajbVaqnRULWq16+1tCQzbsx7CuUNRTyb9K30tfSLVJUa0h/SrXQh/Sx9GWK2J3UskUgcHBzMZjWLvmbmBRSLxeVlnZ2NjPHwoMNDxeOy/J6in036SjqV7ke0z9JbKR7i+B5Qq9WMZjO7aIy4ZqbfNzZMNVC7PT7Mx49KpVQoFKyN4yhnk36Uvpc+jXbsv9aSXlnLNpTd3d29vb3ZPAKJvmbm3a+v9wqCyzydTm8025mbI55N+qFfrO7dtTtjWog1LZfL7e/vo1kImlWrVbPhMaPTUyRTN+LxeND7jYhn62/G7lw79k9Ns5BtFPl8/ujoCM1sa2aWPclk7xTBRyqzFzI7+0CXZFHOJn3TP+G4997eBprNAVPKwl21zqlmZk+cTvtMZUba0tJScKfVEc/WP1q899U+B5rNOXYqlUIz25qZVUSl4j/Y9rbK5fJ8ZpPe+NXMtFxw2RzodDqxWKzb7aKZVc0SiUSj4T/Yu3cBrs0inq3/sZhvzX4La924trZ2cXGBZlY1e/HC8wHD43Z52bvrMIfZpC+8H348bu+Dy+ZMpVLJ5XJoNlyz4Jgk2IcP85ttAsdMu1Z4LC4uctnKajVbWFjodv0Hu7qSWT7NZzbpzwk0uwwu21jS6fTV1RWa2dNsZWWl2fQf7PRUmUxmPrP1Ly761uz34LKNpVwu7+zsoJk9zba2to6P/QcrFFQqlWY729DON9mkXyfQ7Kfg+m0szWZzeXkZzaapmfO3ac7PzzMZ/8HMpH5zczOT2R4nHJpN+s63ZlPJNgkvX75stVpo5qSZ+++h/f8fPnmg2+2aie362k+qszOZVf60qoSFbF6/vzd4bOjDg2xS3Zdmp5P021QwS1YzU6DZGM2c51pP65+Tk5NUysPd3EFrt2XGWb1edx7K7uPZyea+3x4/PPQBk0165eXe8KB9GprNMsViMZQ7xPOrmWFzczOf93aVKZvNmlfl/Ccim20qmg2ySduerlmNymaZQqEQyjdinqtmnrZAox7odDpmGWNGs5u60elofX19Y2Nj6GcvXjULJZsbx9z8TsQgW980NzXtziGbZXK53OHhIZqN18xHKXMezWZuTqVSznuharV3tGDmwlFjxV81s5ltWkuAJ9nG7dMunLPZZLCxDOUM5llWs2ktzB7vN8wLMPvj4+Pej20MPh02JaLRUKXSO1RIJpPVatXlWiuy2SbfNI7K1j/lb/z7yfVd/97jGzfZbGKWi2Hd039Ov2zlfriMPc0bOtWdn59vbW2Z2bd/10GxWCyRSOTz+Vqt5mYyfnIEMuqP2s/mQzP3J5OT95sd2u12PB5vNBqzplkw5dHtcAnrV+i8TkBR67cZxpgW2ht/jppFUx76DWZEMwYK/YZm9hY/QL+hGQCgGQCaAaAZAKAZAJoBoBkAoBkAmgEAmgGgGQCaAQCaAaAZAJoBAJoBoBkAoBkAmgGgGQCgGQCaAaAZAKAZAJoBwFP+BvUCvvHguHARAAAAAElFTkSuQmCC";
        given().body(output2).with().contentType(JSON).then().statusCode(HttpStatus.SC_OK).and().expect().body(equalTo(image)).when().post("/glycans/image/glycan?format=png&notation=cfg&style=extended");
    }
    
    @Test
    public void testImage() throws Exception {
    	String sequence = "RES\\n"
				+ "1b:x-dglc-HEX-x:x\\n"
				+ "2b:b-dgal-HEX-1:5\\n"
				+ "3b:a-dgal-HEX-1:5\\n"
				+ "4b:b-dgal-HEX-1:5\\n"
				+ "5s:n-acetyl\\n"
				+ "LIN\\n"
				+ "1:1o(4+1)2d\\n"
				+ "2:2o(3+1)3d\\n"
				+ "3:3o(3+1)4d\\n"
				+ "4:4d(2+1)5n";
		String output2 = "{\"encoding\":\"glycoct\",\"structure\":\"" + sequence + "\"}"; 
        
		String image = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAASIAAABSCAIAAAB/iU6UAAAFWElEQVR42u2cMWgjRxhGv1xljIorBTHEEBeCqDCcICoNEUQQE1y4dCGMChNEuEKFCSpcqHDhAx24cIgv2ODShQsdyGBQ4eIKFy5UqEih4rorLIjgBHaRjKQkGEda7a60s2vpPabcO3/Mzpt/ZjSS/gKAgBFdAIBmAGgGAGgGgGYAaAYAaAaAZgCAZgBoBoBmAIBmAGgGgGYAgGYAaAYAaAaAZgBoBgBoBoBmAGgGAGgGgGYAgGYAaAaAZgCAZgBoBmCVh4eHGdRM08BmX9zc3JRKpUwmM/jTyWQym82Wy+Vmsxn6ECHbhNze3q6uroZlWrCa9f9//82aZvV6PZ3WyopKJdVqajbVaqnRULWq16+1tCQzbsx7CuUNRTyb9K30tfSLVJUa0h/SrXQh/Sx9GWK2J3UskUgcHBzMZjWLvmbmBRSLxeVlnZ2NjPHwoMNDxeOy/J6in036SjqV7ke0z9JbKR7i+B5Qq9WMZjO7aIy4ZqbfNzZMNVC7PT7Mx49KpVQoFKyN4yhnk36Uvpc+jXbsv9aSXlnLNpTd3d29vb3ZPAKJvmbm3a+v9wqCyzydTm8025mbI55N+qFfrO7dtTtjWog1LZfL7e/vo1kImlWrVbPhMaPTUyRTN+LxeND7jYhn62/G7lw79k9Ns5BtFPl8/ujoCM1sa2aWPclk7xTBRyqzFzI7+0CXZFHOJn3TP+G4997eBprNAVPKwl21zqlmZk+cTvtMZUba0tJScKfVEc/WP1q899U+B5rNOXYqlUIz25qZVUSl4j/Y9rbK5fJ8ZpPe+NXMtFxw2RzodDqxWKzb7aKZVc0SiUSj4T/Yu3cBrs0inq3/sZhvzX4La924trZ2cXGBZlY1e/HC8wHD43Z52bvrMIfZpC+8H348bu+Dy+ZMpVLJ5XJoNlyz4Jgk2IcP85ttAsdMu1Z4LC4uctnKajVbWFjodv0Hu7qSWT7NZzbpzwk0uwwu21jS6fTV1RWa2dNsZWWl2fQf7PRUmUxmPrP1Ly761uz34LKNpVwu7+zsoJk9zba2to6P/QcrFFQqlWY729DON9mkXyfQ7Kfg+m0szWZzeXkZzaapmfO3ac7PzzMZ/8HMpH5zczOT2R4nHJpN+s63ZlPJNgkvX75stVpo5qSZ+++h/f8fPnmg2+2aie362k+qszOZVf60qoSFbF6/vzd4bOjDg2xS3Zdmp5P021QwS1YzU6DZGM2c51pP65+Tk5NUysPd3EFrt2XGWb1edx7K7uPZyea+3x4/PPQBk0165eXe8KB9GprNMsViMZQ7xPOrmWFzczOf93aVKZvNmlfl/Ccim20qmg2ySduerlmNymaZQqEQyjdinqtmnrZAox7odDpmGWNGs5u60elofX19Y2Nj6GcvXjULJZsbx9z8TsQgW980NzXtziGbZXK53OHhIZqN18xHKXMezWZuTqVSznuharV3tGDmwlFjxV81s5ltWkuAJ9nG7dMunLPZZLCxDOUM5llWs2ktzB7vN8wLMPvj4+Pej20MPh02JaLRUKXSO1RIJpPVatXlWiuy2SbfNI7K1j/lb/z7yfVd/97jGzfZbGKWi2Hd039Ov2zlfriMPc0bOtWdn59vbW2Z2bd/10GxWCyRSOTz+Vqt5mYyfnIEMuqP2s/mQzP3J5OT95sd2u12PB5vNBqzplkw5dHtcAnrV+i8TkBR67cZxpgW2ht/jppFUx76DWZEMwYK/YZm9hY/QL+hGQCgGQCaAaAZAKAZAJoBoBkAoBkAmgEAmgGgGQCaAQCaAaAZAJoBAJoBoBkAoBkAmgGgGQCgGQCaAaAZAKAZAJoBwFP+BvUCvvHguHARAAAAAElFTkSuQmCC";
//        given().body(output2).with().contentType(JSON).then().statusCode(HttpStatus.SC_OK).and().expect().body(equalTo(image)).when().post("/glycans/image/glycan?format=png&notation=cfg&style=extended");
		
		GlycanInput glycan = new GlycanInput();
		glycan.setFormat("glycoct");
		glycan.setSequence(sequence);
		gc.getGlycanImageByStructure(glycan, null, "cfg", "extended");
    }
}