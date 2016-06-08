package org.glytoucan.ws.api;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GlycanConfirmTest {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger(GlycanConfirmTest.class);

	@Test
	public void jsonSerializationTest() throws Exception {
	     Date d = new Date(0);
	     Glycan g = new Glycan();
	     g.setAccessionNumber("G001234");
	     g.setContributor("aoki");
	     g.setDateEntered(d);
	     g.setMass(123.0);
	     g.setStructure("structureString");
	     g.setGlycanId(0);

	     ObjectMapper mapper = new ObjectMapper();
	    // {"glycanId":0,"accessionNumber":"G001234","dateEntered":"1970-01-01T00:00:00Z","structure":"structureString","structureLength":null,"mass":123.0,"motifs":null,"contributor":{"userId":1,"userName":"aoki","loginId":"aokinobu","email":"support@glytoucan.org","active":true,"validated":false,"affiliation":"Soka University","dateRegistered":"1970-01-01T00:00:00Z","lastLoggedIn":"1970-01-01T00:00:00Z","roles":null}}
//       {"glycanId":0,"accessionNumber":"G001234","dateEntered":"1970-01-01T00:00:00Z","structure":"structureString","structureLength":null,"mass":123.0,"motifs":null,"contributor":"aoki"}
	     logger.debug(mapper.writeValueAsString(g));
	     assertEquals("{\"glycanId\":0,\"accessionNumber\":\"G001234\",\"dateEntered\":\"1970-01-01T00:00:00Z\",\"structure\":\"structureString\",\"structureLength\":null,\"mass\":123.0,\"motifs\":null,\"contributor\":{\"userId\":1,\"userName\":\"aoki\",\"loginId\":\"aokinobu\",\"email\":\"support@glytoucan.org\",\"active\":true,\"validated\":false,\"affiliation\":\"Soka University\",\"dateRegistered\":\"1970-01-01T00:00:00Z\",\"lastLoggedIn\":\"1970-01-01T00:00:00Z\",\"roles\":null\",\"contributor\":\"aoki\"}}", 
	    		 mapper.writeValueAsString(g));
	}
}
