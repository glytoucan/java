package org.glytoucan.web.api;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.glytoucan.web.api.GRABTreeJson;
import org.glytoucan.web.api.GRABTreeSequence;
import org.glytoucan.web.api.GRABTreeEachRelationship;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GRABTreeConfirmTest {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger(GRABTreeConfirmTest.class);

	@Test
	public void jsonSerializationTest() throws Exception {
		GRABTreeJson a = new GRABTreeJson();
		GRABTreeEachRelationship b1 = new GRABTreeEachRelationship();
		GRABTreeEachRelationship b2 = new GRABTreeEachRelationship();
		GRABTreeSequence c1 = new GRABTreeSequence();
		GRABTreeSequence c2 = new GRABTreeSequence();
		GRABTreeSequence c3 = new GRABTreeSequence();
		ArrayList<GRABTreeEachRelationship> b_list = new ArrayList<GRABTreeEachRelationship>();
		ArrayList<GRABTreeSequence> c_list = new ArrayList<GRABTreeSequence>();

		a.setName("G99981ST");
		// c.setName("Goo");
		// c.setSize(10);
		c1.setName("G00065MO");
		c1.setSize(10);
		c2.setName("G00030MO");
		c2.setSize(10);
		c3.setName("G00029MO");
		c3.setSize(10);
		b1.setName("has_motif");

		c_list.add(c1);
		c_list.add(c2);
	

		b1.setChildren(c_list);
		b2.setName("has_sumption");
		ArrayList<GRABTreeSequence> c_list2 = new ArrayList<GRABTreeSequence>();		
		c_list2.add(c3);
		b2.setChildren(c_list2);
		
		b_list.add(b1);
		b_list.add(b2);
		a.setChildren(b_list);

		ObjectMapper mapper = new ObjectMapper();
		// {"glycanId":0,"accessionNumber":"G001234","dateEntered":"1970-01-01T00:00:00Z","structure":"structureString","structureLength":null,"mass":123.0,"motifs":null,"contributor":{"userId":1,"userName":"aoki","loginId":"aokinobu","email":"support@glytoucan.org","active":true,"validated":false,"affiliation":"Soka University","dateRegistered":"1970-01-01T00:00:00Z","lastLoggedIn":"1970-01-01T00:00:00Z","roles":null}}
		logger.debug(mapper.writeValueAsString(a));
		// assertEquals("{\"name\":\"G001234\",\"children\":{\"name\":\"has_motif\",\"children\":{\"name\":\"G00065MO\",\"size\":10}}}",
		// mapper.writeValueAsString(a));
		//System.out.println(mapper.writeValueAsString(a));
	}
}
