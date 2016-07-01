package org.glytoucan.web.api;

import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.GlycoSequenceSelectSparql;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SparqlConfirmTest {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger(SparqlConfirmTest.class);

	@Test
	public void jsonSerializationTest() throws Exception {
	    GlycoSequenceSelectSparql sp = new GlycoSequenceSelectSparql(); 
//	    GlycoSequence gs = new GlycoSequence(10, "G00003VQ");
//	    GlycoSequenceController gc = new GlycoSequenceController();
//	    SelectSparql selectSparql;

//	    gs.setSequence("atgcatgc");
	   
//	    gc.getSelectSparql();
//	    gs.setSequence(sequence);
//	     sp.getPrimaryId();
//	    long b = gs.getId();
//	    String a = gs.getPrimaryId();
//	    gc.retrieve(a);
//	    gc.getSelectSparql();
//	    gc.setSelectSparql(sp);
	    
	    SparqlEntity sparqlentity = new SparqlEntity();
	    sparqlentity.setValue(Saccharide.PrimaryId, "G000TEST");
	    sp.setSparqlEntity(sparqlentity);
//	    sp.getPrimaryId();
//	    sp.getWhere();
//	     sp.getSparql();
	     logger.debug(sp.getSparql());
	     
//	     ObjectMapper mapper = new ObjectMapper();
//	     // {"glycanId":0,"accessionNumber":"G001234","dateEntered":"1970-01-01T00:00:00Z","structure":"structureString","structureLength":null,"mass":123.0,"motifs":null,"contributor":{"userId":1,"userName":"aoki","loginId":"aokinobu","email":"support@glytoucan.org","active":true,"validated":false,"affiliation":"Soka University","dateRegistered":"1970-01-01T00:00:00Z","lastLoggedIn":"1970-01-01T00:00:00Z","roles":null}}
//	     logger.debug(mapper.writeValueAsString(g));
//	     assertEquals("{\"glycanId\":0,\"accessionNumber\":\"G001234\",\"dateEntered\":\"1970-01-01T00:00:00Z\",\"structure\":\"structureString\",\"structureLength\":null,\"mass\":123.0,\"motifs\":null,\"contributor\":{\"userId\":1,\"userName\":\"aoki\",\"loginId\":\"aokinobu\",\"email\":\"support@glytoucan.org\",\"active\":true,\"validated\":false,\"affiliation\":\"Soka University\",\"dateRegistered\":\"1970-01-01T00:00:00Z\",\"lastLoggedIn\":\"1970-01-01T00:00:00Z\",\"roles\":null}}", 
//	    		 mapper.writeValueAsString(g));
	}     
}
