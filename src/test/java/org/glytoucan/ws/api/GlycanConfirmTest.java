package org.glytoucan.ws.api;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Set;

import org.glycoinfo.batch.search.wurcs.SubstructureSearchSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.scint.ScintTest;
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
	     User u = new User();
	     u.setUserId(1);
	     u.setEmail("support@glytoucan.org");
	     u.setUserName("aoki");
	     u.setLoginId("aokinobu");
	     u.setAffiliation("Soka University");
	     u.setDateRegistered(d);
	     u.setLastLoggedIn(d);
	     g.setContributor(u);
	     g.setDateEntered(d);
	     g.setMass(123.0);
	     g.setStructure("structureString");
	     g.setGlycanId(0);

	     ObjectMapper mapper = new ObjectMapper();
	     // {"glycanId":0,"accessionNumber":"G001234","dateEntered":"1970-01-01T00:00:00Z","structure":"structureString","structureLength":null,"mass":123.0,"motifs":null,"contributor":{"userId":1,"userName":"aoki","loginId":"aokinobu","email":"support@glytoucan.org","active":true,"validated":false,"affiliation":"Soka University","dateRegistered":"1970-01-01T00:00:00Z","lastLoggedIn":"1970-01-01T00:00:00Z","roles":null}}
	     logger.debug(mapper.writeValueAsString(g));
	     assertEquals("{\"glycanId\":0,\"accessionNumber\":\"G001234\",\"dateEntered\":\"1970-01-01T00:00:00Z\",\"structure\":\"structureString\",\"structureLength\":null,\"mass\":123.0,\"motifs\":null,\"contributor\":{\"userId\":1,\"userName\":\"aoki\",\"loginId\":\"aokinobu\",\"email\":\"support@glytoucan.org\",\"active\":true,\"validated\":false,\"affiliation\":\"Soka University\",\"dateRegistered\":\"1970-01-01T00:00:00Z\",\"lastLoggedIn\":\"1970-01-01T00:00:00Z\",\"roles\":null}}", 
	    		 mapper.writeValueAsString(g));
	}
}
