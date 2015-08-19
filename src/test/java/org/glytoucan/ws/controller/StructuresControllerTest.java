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
}