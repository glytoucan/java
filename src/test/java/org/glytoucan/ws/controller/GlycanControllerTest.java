package org.glytoucan.ws.controller;

import static com.jayway.restassured.RestAssured.given;

import org.apache.http.HttpStatus;
import org.glycoinfo.batch.search.wurcs.SubstructureSearchSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.glycan.wurcs.GlycoSequenceResourceEntryContributorSelectSparql;
import org.glytoucan.ws.Application;
import org.glytoucan.ws.api.Glycan;
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
@SpringApplicationConfiguration(classes = Application.class)
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

//	@Test
	public void testG00032MOGlycoct() throws Exception {

		// glycoct conversion to wurcs
		SelectSparql compare = new SubstructureSearchSparql();
		String G00032MO = "RES\n" + "1b:b-dglc-HEX-1:5\n" + "2s:methyl\n"
				+ "3s:sulfate\n" + "4b:b-dgro-HEX-1:5|2:d|3:d|4:d|6:a\n"
				+ "5b:a-dglc-HEX-1:5\n" + "6b:o-dman-HEX-0:0|1:aldi\n"
				+ "7b:x-dgro-dgal-NON-2:6|2:keto|1:a|3:d\n" + "8s:n-acetyl\n"
				+ "9b:b-dara-HEX-2:5|2:keto\n" + "10b:o-dgro-TRI-0:0|1:aldi\n"
				+ "11b:b-dxyl-HEX-1:5|1:keto|4:keto|6:d\n"
				+ "12b:b-drib-HEX-1:5|2:d|6:d\n" + "13b:b-dglc-HEX-1:5\n"
				+ "14s:n-acetyl\n" + "15s:phospho-ethanolamine\n"
				+ "16b:b-HEX-x:x\n" + "17b:b-SUG-x:x\n" + "18b:o-SUG-0:0\n"
				+ "19b:a-dery-HEX-1:5|2,3:enx\n"
				+ "20b:a-dman-OCT-x:x|1:a|2:keto|3:d\n" + "LIN\n"
				+ "1:1o(2+1)2n\n" + "2:1o(3+1)3n\n" + "3:1o(4+1)4d\n"
				+ "4:4o(6+1)5d\n" + "5:5o(4+1)6d\n" + "6:6o(6+2)7d\n"
				+ "7:7d(5+1)8n\n" + "8:7o(8+2)9d\n" + "9:9o(4+1)10d\n"
				+ "10:10o(3+1)11d\n" + "11:11o(3+1)12d\n" + "12:12o(4+1)13d\n"
				+ "13:13d(2+1)14n\n" + "14:13o(6+1)15n\n" + "15:15n(1+1)16o\n"
				+ "16:16o(4+1)17d\n" + "17:17o(-1+1)18d\n"
				+ "18:18o(-1+1)19d\n" + "19:19o(4+2)20d\n";
		SelectSparql result = gc.substructureSearch(G00032MO, "glycoct");
		logger.debug(result.getSparql());
	}

	@Test
	public void testG00003VQGlycoct() throws Exception {

		// glycoct conversion to wurcs
		SelectSparql compare = new SubstructureSearchSparql();
		String G00003VQ = "WURCS=2.0/4,5,5/[12112h-1b_1-5_2*NCC/3=O][12122a-1b_1-5][22112h-1a_1-5][12122h-1b_1-5]/1-2-1-3-4/a3-b1_b4-c1_c4-d1_d3-e1_a1-e4~n";
		SelectSparql result = gc.substructureSearch(G00003VQ, "wurcs");
		logger.debug("RESULT>" + result.getSparql() +"<");
	}

	@Test
	public void testG00012MOGlycoct() throws Exception {

		// glycoct conversion to wurcs
		SelectSparql compare = new SubstructureSearchSparql();
		String G00003VQ = "WURCS=2.0/4,4,3/[u2122h][12112h-1b_1-5][22112h-1a_1-5][12112h-1b_1-5_2*NCC/3=O]/1-2-3-4/a4-b1_b3-c1_c3-d1";
		SelectSparql result = gc.substructureSearch(G00003VQ, "wurcs");
		logger.debug("RESULT>" + result.getSparql() +"<");
	}

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
}