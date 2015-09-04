package org.glytoucan.ws.controller;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.service.GlycanProcedure;
import org.glytoucan.ws.Application;
import org.glytoucan.ws.model.SequenceInput;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
@WebAppConfiguration
public class StructuresControllerTest {
	public static Log logger = (Log) LogFactory
			.getLog(StructuresControllerTest.class);

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testStructureSearchStart() throws Exception {
		mockMvc.perform(get("/Structures/structureSearch"))
				.andExpect(status().isOk())
				.andExpect(view().name("structures/structure_search"))
				.andExpect(
						model().attribute("sequence",
								hasProperty("sequence", isEmptyOrNullString())));
	}

	@Test
	public void testRegisteredGlycoCTG00031MO() throws UnsupportedEncodingException {
		String sequence = "RES\n" + 
	"1b:a-dgal-HEX-1:5\n" + 
				"2s:n-acetyl\n" + 
				"3b:b-dgal-HEX-1:5\n" + 
				"LIN\n" + 
				"1:1d(2+1)2n\n" + 
				"2:1o(3+1)3d";
		logger.debug("sequence:>" + sequence + "<");
		SequenceInput si = new SequenceInput();
		si.setId("G00031MO");
		si.setSequence(sequence);
//		si.setResultSequence(URLEncoder.encode("WURCS=2.0/2,2,1/[22112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1", "UTF-8"));
		si.setResultSequence("WURCS%3D2.0%2F2%2C2%2C1%2F%5B22112h-1a_1-5_2*NCC%2F3%3DO%5D%5B12112h-1b_1-5%5D%2F1-2%2Fa3-b1");
		si.setImage("/glyspace/service/glycans/G00031MO/image?style=extended&format=png&notation=cfg");
		try {
			mockMvc.perform(
					post("/Structures/structure").contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequence", sequence))
					.andExpect(status().isOk())
					.andExpect(view().name("structures/structure"))
					.andExpect(model().attribute("sequence", hasProperty("sequence", is(sequence))))
			        .andExpect(model().attribute("sequence", hasProperty("id", is("G00031MO"))))
			        .andExpect(model().attribute("sequence", hasProperty("resultSequence", is(si.getResultSequence()))))
			        .andExpect(model().attribute("sequence", hasProperty("image", is(si.getImage()))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUnRegisteredGlycoCTOnHelpText() throws UnsupportedEncodingException {
		
		/**
		 * 
		 * RES
1b:a-dgal-HEX-1:5
2s:n-acetyl
3b:b-dgal-HEX-1:5
4r:r1
5b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d
6s:n-acetyl
7r:r2
8b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d
9s:n-acetyl
LIN
1:1d(2+1)2n
2:1o(3+1)3d
3:3o(3+1)4n
4:4n(3+2)5d
5:5d(5+1)6n
6:1o(6+1)7n
7:7n(3+2)8d
8:8d(5+1)9n
REP
REP1:13o(3+1)10d=-1--1
RES
10b:b-dglc-HEX-1:5
11s:n-acetyl
12b:a-lgal-HEX-1:5|6:d
13b:b-dgal-HEX-1:5
14s:sulfate
LIN
9:10d(2+1)11n
10:10o(3+1)12d
11:10o(4+1)13d
12:10o(6+1)14n
REP2:18o(3+1)15d=-1--1
RES
15b:b-dgal-HEX-1:5
16s:n-acetyl
17b:a-lgal-HEX-1:5|6:d
18b:b-dgal-HEX-1:5
19s:sulfate
LIN
13:15d(2+1)16n
14:15o(3+1)17d
15:15o(4+1)18d
16:15o(6+1)19n
		 * 
		 */
		String sequence = "RES\\n"
				+ "1b:a-dgal-HEX-1:5\\n"
				+ "2s:n-acetyl\\n"
				+ "3b:b-dgal-HEX-1:5\\n"
				+ "4r:r1\\n"
				+ "5b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d\\n"
				+ "6s:n-acetyl\\n"
				+ "7r:r2\\n"
				+ "8b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d\\n"
				+ "9s:n-acetyl\\n"
				+ "LIN\\n"
				+ "1:1d(2+1)2n\\n"
				+ "2:1o(3+1)3d\\n"
				+ "3:3o(3+1)4n\\n"
				+ "4:4n(3+2)5d\\n"
				+ "5:5d(5+1)6n\\n"
				+ "6:1o(6+1)7n\\n"
				+ "7:7n(3+2)8d\\n"
				+ "8:8d(5+1)9n\\n"
				+ "REP\\n"
				+ "REP1:13o(3+1)10d=-1--1\\n"
				+ "RES\\n"
				+ "10b:b-dglc-HEX-1:5\\n"
				+ "11s:n-acetyl\\n"
				+ "12b:a-lgal-HEX-1:5|6:d\\n"
				+ "13b:b-dgal-HEX-1:5\\n"
				+ "14s:sulfate\\n"
				+ "LIN\\n"
				+ "9:10d(2+1)11n\\n"
				+ "10:10o(3+1)12d\\n"
				+ "11:10o(4+1)13d\\n"
				+ "12:10o(6+1)14n\\n"
				+ "REP2:18o(3+1)15d=-1--1\\n"
				+ "RES\\n"
				+ "15b:b-dgal-HEX-1:5\\n"
				+ "16s:n-acetyl\\n"
				+ "17b:a-lgal-HEX-1:5|6:d\\n"
				+ "18b:b-dgal-HEX-1:5\\n"
				+ "19s:sulfate\\n"
				+ "LIN\\n"
				+ "13:15d(2+1)16n\\n"
				+ "14:15o(3+1)17d\\n"
				+ "15:15o(4+1)18d\\n"
				+ "16:15o(6+1)19n";
		logger.debug("sequence:>" + sequence + "<");
		SequenceInput si = new SequenceInput();
		si.setId(GlycanProcedure.NotRegistered);
		si.setSequence(sequence);
//		si.setResultSequence(URLEncoder.encode("WURCS=2.0/2,2,1/[22112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1", "UTF-8"));
		si.setResultSequence("WURCS=2.0/6,10,11/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5][a2122h-1b_1-5_2*NCC/3=O_6*OSO/3=O/3=O][a1221m-1a_1-5][Aad21122h-2a_2-6_5*NCC/3=O][a2112h-1b_1-5_2*NCC/3=O_6*OSO/3=O/3=O]/1-2-3-4-2-5-6-4-2-5/a3-b1_a6-g1_b3-c1_c3-d1_c4-e1_e3-f2_g3-h1_g4-i1_i3-j2_c1-e3~n_g1-i3~n");
		si.setImage("data:image/png;base64,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");
		
		try {
			mockMvc.perform(
					post("/Structures/structure").contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequence", sequence))
					.andExpect(status().isOk())
					.andExpect(view().name("structures/structure"))
					.andExpect(model().attribute("sequence", hasProperty("sequence", is(sequence))))
			        .andExpect(model().attribute("sequence", hasProperty("id", is(GlycanProcedure.NotRegistered))))
			        .andExpect(model().attribute("sequence", hasProperty("resultSequence", is(si.getResultSequence()))))
			        .andExpect(model().attribute("sequence", hasProperty("image", is(si.getImage()))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Test
	public void testRegisteredGlycoCTG01132OH() throws UnsupportedEncodingException {
		String id = "G01132OH";
		String sequence = "RES\n"
				+ "1b:x-dgal-HEX-1:5\n"
				+ "2s:n-acetyl\n"
				+ "3b:b-dgal-HEX-1:5\n"
				+ "4b:a-dgal-HEX-1:5\n"
				+ "5s:n-acetyl\n"
				+ "6b:b-dgal-HEX-1:5\n"
				+ "7b:b-dglc-HEX-1:5\n"
				+ "8s:n-acetyl\n"
				+ "9s:sulfate\n"
				+ "LIN\n"
				+ "1:1d(2+1)2n\n"
				+ "2:1o(3+1)3d\n"
				+ "3:3o(4+1)4d\n"
				+ "4:4d(2+1)5n\n"
				+ "5:4o(3+1)6d\n"
				+ "6:1o(6+1)7d\n"
				+ "7:7d(2+1)8n\n"
				+ "8:7o(6+1)9n";
		logger.debug("sequence:>" + sequence + "<");
		SequenceInput si = new SequenceInput();
		si.setId(id);
		si.setSequence(sequence);
//		si.setResultSequence(URLEncoder.encode("WURCS=2.0/4,5,4/[a2112h-1x_1-5_2*NCC/3=O][a2112h-1b_1-5][a2112h-1a_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O_6*OSO/3=O/3=O]/1-2-3-2-4/a3-b1_a6-e1_b4-c1_c3-d1", "UTF-8"));
		si.setResultSequence("WURCS%3D2.0%2F2%2C2%2C1%2F%5B22112h-1a_1-5_2*NCC%2F3%3DO%5D%5B12112h-1b_1-5%5D%2F1-2%2Fa3-b1");
		si.setImage("/glyspace/service/glycans/" + id + "/image?style=extended&format=png&notation=cfg");
		try {
			mockMvc.perform(
					post("/Structures/structure").contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequence", sequence))
					.andExpect(status().isOk())
					.andExpect(view().name("structures/structure"))
					.andExpect(model().attribute("sequence", hasProperty("sequence", is(sequence))))
			        .andExpect(model().attribute("sequence", hasProperty("id", is(id))))
			        .andExpect(model().attribute("sequence", hasProperty("resultSequence", is(si.getResultSequence()))))
			        .andExpect(model().attribute("sequence", hasProperty("image", is(si.getImage()))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testRegisteredGlycoCTG00029MOHybrid() throws UnsupportedEncodingException {
		String id = "G00029MO";
		String sequence = "RES\\n"
				+ "1b:x-dglc-HEX-x:x\\n"
				+ "2s:n-acetyl\\n"
				+ "3b:b-dglc-HEX-1:5\\n"
				+ "4s:n-acetyl\\n"
				+ "5b:b-dman-HEX-1:5\\n"
				+ "6b:a-dman-HEX-1:5\\n"
				+ "7b:b-dglc-HEX-1:5\\n"
				+ "8s:n-acetyl\\n"
				+ "9b:a-dman-HEX-1:5\\n"
				+ "LIN\\n"
				+ "1:1d(2+1)2n\\n"
				+ "2:1o(4+1)3d\\n"
				+ "3:3d(2+1)4n\\n"
				+ "4:3o(4+1)5d\\n"
				+ "5:5o(3+1)6d\\n"
				+ "6:6o(2|4+1)7d\\n"
				+ "7:7d(2+1)8n\\n"
				+ "8:5o(6+1)9d\\n";
		logger.debug("sequence:>" + sequence + "<");

		SequenceInput si = new SequenceInput();
		si.setId(id);
		si.setSequence(sequence);
		si.setResultSequence("WURCS=2.0/4,6,5/[u2122h_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5]/1-2-3-4-2-4/a4-b1_b4-c1_c3-d1_c6-f1_e1-d2|d4");
		si.setImage("/glycans/" + id + "/image?style=extended&format=png&notation=cfg");
		try {
			mockMvc.perform(
					post("/Structures/structure").contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequence", sequence))
					.andExpect(status().isOk())
					.andExpect(view().name("structures/structure"))
					.andExpect(model().attribute("sequence", hasProperty("sequence", is(sequence))))
			        .andExpect(model().attribute("sequence", hasProperty("id", is(id))))
			        .andExpect(model().attribute("sequence", hasProperty("resultSequence", is(si.getResultSequence()))))
			        .andExpect(model().attribute("sequence", hasProperty("image", is(si.getImage()))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
}