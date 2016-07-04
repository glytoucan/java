package org.glytoucan.web.api;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.glytoucan.web.api.D3_Tree_json;
import org.glytoucan.web.api.TreeSequence;
import org.glytoucan.web.api.Tree_json;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class D3TreeConfirmTest {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger(D3TreeConfirmTest.class);

	@Test
	public void jsonSerializationTest() throws Exception {
		D3_Tree_json a = new D3_Tree_json();
		Tree_json b1 = new Tree_json();
		Tree_json b2 = new Tree_json();
		TreeSequence c1 = new TreeSequence();
		TreeSequence c2 = new TreeSequence();
		TreeSequence c3 = new TreeSequence();
		ArrayList<Tree_json> b_list = new ArrayList<Tree_json>();
		ArrayList<TreeSequence> c_list = new ArrayList<TreeSequence>();

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
		ArrayList<TreeSequence> c_list2 = new ArrayList<TreeSequence>();		
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
