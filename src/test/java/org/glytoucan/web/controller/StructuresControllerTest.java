package org.glytoucan.web.controller;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.service.GlycanProcedure;
import org.glycoinfo.rdf.service.impl.GlycanProcedureConfig;
import org.glytoucan.web.Application;
import org.glytoucan.web.model.SequenceInput;
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
@SpringApplicationConfiguration(classes = { Application.class, GlycanProcedureConfig.class })
@WebAppConfiguration
public class StructuresControllerTest {
	public static Log logger = (Log) LogFactory
			.getLog(StructuresControllerTest.class);

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
  }

	@Test
	public void testStructureSearchStart() throws Exception {
		mockMvc.perform(get("/Structures/structureSearch"))
				.andExpect(status().isOk())
				.andExpect(view().name("structures/structure_search"));
	}

	
	 @Test
	  public void testGal() throws Exception {
	    String sequence = "Gal";
	    logger.debug("sequence:>" + sequence + "<");
	    SequenceInput si = new SequenceInput();
	    si.setId("G68158BT");
	    si.setSequenceInput(sequence);
	    si.setResultSequence(URLEncoder.encode("WURCS=2.0/1,1,0/[a2112h-1x_1-5]/1/", "UTF-8"));
	    
	    si.setImage("/glycans/G68158BT/image?style=extended&format=png&notation=cfg");
	      mockMvc.perform(
	          post("/Structures/structure").with(csrf()).contentType(
	              MediaType.APPLICATION_FORM_URLENCODED).param(
	              "sequenceInput", sequence))
	          .andExpect(status().isOk())
	          .andExpect(view().name("structures/structure"))
	          .andExpect(model().attribute("sequenceInput", hasProperty("sequenceInput", is(sequence))))
	              .andExpect(model().attribute("sequenceInput", hasProperty("id", is("G68158BT"))))
	              .andExpect(model().attribute("sequenceInput", hasProperty("resultSequence", is(si.getResultSequence()))))
	              .andExpect(model().attribute("sequenceInput", hasProperty("image", is(si.getImage()))));
	  }
	 	
	@Test
	public void testRegisteredGlycoCTG00031MO() throws Exception {
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
		si.setSequenceInput(sequence);
		si.setResultSequence("WURCS%3D2.0%2F2%2C2%2C1%2F%5Ba2112h-1a_1-5_2*NCC%2F3%3DO%5D%5Ba2112h-1b_1-5%5D%2F1-2%2Fa3-b1");
		
		si.setImage("/glycans/G00031MO/image?style=extended&format=png&notation=cfg");
			mockMvc.perform(
					post("/Structures/structure").with(csrf()).contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequenceInput", sequence))
					.andExpect(status().isOk())
					.andExpect(view().name("structures/structure"))
					.andExpect(model().attribute("sequenceInput", hasProperty("sequenceInput", is(sequence))))
			        .andExpect(model().attribute("sequenceInput", hasProperty("id", is("G00031MO"))))
			        .andExpect(model().attribute("sequenceInput", hasProperty("resultSequence", is(si.getResultSequence()))))
			        .andExpect(model().attribute("sequenceInput", hasProperty("image", is(si.getImage()))));
	}
	
	@Test
	public void testUnRegisteredGlycoCTOnHelpText() throws Exception {
		
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
		si.setSequenceInput(sequence);
//		si.setResultSequence(URLEncoder.encode("WURCS=2.0/2,2,1/[22112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1", "UTF-8"));
//		si.setResultSequence("WURCS=2.0/6,10,11/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5][a2122h-1b_1-5_2*NCC/3=O_6*OSO/3=O/3=O][a1221m-1a_1-5][Aad21122h-2a_2-6_5*NCC/3=O][a2112h-1b_1-5_2*NCC/3=O_6*OSO/3=O/3=O]/1-2-3-4-2-5-6-4-2-5/a3-b1_a6-g1_b3-c1_c3-d1_c4-e1_e3-f2_g3-h1_g4-i1_i3-j2_c1-e3~n_g1-i3~n");
		si.setResultSequence(URLEncoder.encode("WURCS=2.0/6,10,11/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5][a2122h-1b_1-5_2*NCC/3=O_6*OSO/3=O/3=O][a1221m-1a_1-5][Aad21122h-2a_2-6_5*NCC/3=O][a2112h-1b_1-5_2*NCC/3=O_6*OSO/3=O/3=O]/1-2-3-4-2-5-6-4-2-5/a3-b1_a6-g1_b3-c1_c3-d1_c4-e1_e3-f2_g3-h1_g4-i1_i3-j2_c1-e3~n_g1-i3~n", "UTF-8"));
		si.setImage("data:image/png;base64,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");
		
			mockMvc.perform(
					post("/Structures/structure").with(csrf()).contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequenceInput", sequence))
					.andExpect(status().isOk())
					.andExpect(view().name("structures/structure"))
					.andExpect(model().attribute("sequenceInput", hasProperty("sequenceInput", is(sequence))))
			        .andExpect(model().attribute("sequenceInput", hasProperty("id", is(GlycanProcedure.NotRegistered))))
			        .andExpect(model().attribute("sequenceInput", hasProperty("resultSequence", is(si.getResultSequence()))))
			        .andExpect(model().attribute("sequenceInput", hasProperty("image", is(si.getImage()))));
	}

	@Test
	public void testRegisteredGlycoCTG01132OH() throws Exception {
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
		si.setSequenceInput(sequence);
		si.setResultSequence(URLEncoder.encode("WURCS=2.0/4,5,4/[a2112h-1x_1-5_2*NCC/3=O][a2112h-1b_1-5][a2112h-1a_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O_6*OSO/3=O/3=O]/1-2-3-2-4/a3-b1_a6-e1_b4-c1_c3-d1", "UTF-8"));
//		si.setResultSequence("WURCS%3D2.0%2F2%2C2%2C1%2F%5B22112h-1a_1-5_2*NCC%2F3%3DO%5D%5B12112h-1b_1-5%5D%2F1-2%2Fa3-b1");
		si.setImage("/glycans/" + id + "/image?style=extended&format=png&notation=cfg");
			mockMvc.perform(
					post("/Structures/structure").with(csrf()).contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequenceInput", sequence))
					.andExpect(status().isOk())
					.andExpect(view().name("structures/structure"))
					.andExpect(model().attribute("sequenceInput", hasProperty("sequenceInput", is(sequence))))
			        .andExpect(model().attribute("sequenceInput", hasProperty("id", is(id))))
			        .andExpect(model().attribute("sequenceInput", hasProperty("resultSequence", is(si.getResultSequence()))))
			        .andExpect(model().attribute("sequenceInput", hasProperty("image", is(si.getImage()))));
	}

	@Test
	public void testRegisteredGlycoCTG00031MOHybrid() throws Exception {
		String id = "G00031MO";
		String sequence = "RES\\n" +
				"1b:a-dgal-HEX-1:5\\n" +
				"2s:n-acetyl\\n" +
				"3b:b-dgal-HEX-1:5\\n" +
				"LIN\\n" +
				"1:1d(2+1)2n\\n" +
				"2:1o(3+1)3d";
		logger.debug("sequence:>" + sequence + "<");

		SequenceInput si = new SequenceInput();
		si.setId(id);
		si.setSequenceInput(sequence);
//		si.setResultSequence("WURCS=2.0/4,6,5/[u2122h_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5]/1-2-3-4-2-4/a4-b1_b4-c1_c3-d1_c6-f1_e1-d2|d4");
		si.setResultSequence("WURCS%3D2.0%2F2%2C2%2C1%2F%5Ba2112h-1a_1-5_2*NCC%2F3%3DO%5D%5Ba2112h-1b_1-5%5D%2F1-2%2Fa3-b1");
		si.setImage("/glycans/" + id + "/image?style=extended&format=png&notation=cfg");
			mockMvc.perform(
					post("/Structures/structure").with(csrf()).contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequenceInput", sequence))
					.andExpect(status().isOk())
					.andExpect(view().name("structures/structure"))
					.andExpect(model().attribute("sequenceInput", hasProperty("sequenceInput", is(sequence))))
			        .andExpect(model().attribute("sequenceInput", hasProperty("id", is(id))))
			        .andExpect(model().attribute("sequenceInput", hasProperty("resultSequence", is(si.getResultSequence()))))
			        .andExpect(model().attribute("sequenceInput", hasProperty("image", is(si.getImage()))));
	}
	
	@Test
	public void testLinearCode() throws Exception {
		String id = GlycanProcedure.NotRegistered;
		String sequence = "GNb2(Ab4GNb4)Ma3(Ab4GNb2(Fa3(Ab4)GNb6)Ma6)Mb4GNb4GN";
		logger.debug("sequence:>" + sequence + "<");

		SequenceInput si = new SequenceInput();
		si.setId(id);
		si.setSequenceInput(sequence);
		si.setResultSequence("WURCS%3D2.0%2F6%2C13%2C12%2F%5Ba2122h-1x_1-5_2*NCC%2F3%3DO%5D%5Ba2122h-1b_1-5_2*NCC%2F3%3DO%5D%5Ba1122h-1b_1-5%5D%5Ba1122h-1a_1-5%5D%5Ba2112h-1b_1-5%5D%5Ba1221m-1a_1-5%5D%2F1-2-3-4-2-2-5-4-2-5-2-6-5%2Fa4-b1_b4-c1_c3-d1_c6-h1_d2-e1_d4-f1_f4-g1_h2-i1_h6-k1_i4-j1_k3-l1_k4-m1");
		si.setImage("data:image/png;base64,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");
			mockMvc.perform(
					post("/Structures/structure").with(csrf()).contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequenceInput", sequence))
					.andExpect(status().isOk())
					.andExpect(view().name("structures/structure"))
					.andExpect(model().attribute("sequenceInput", hasProperty("sequenceInput", is(sequence))))
			        .andExpect(model().attribute("sequenceInput", hasProperty("id", is(id))))
			        .andExpect(model().attribute("sequenceInput", hasProperty("resultSequence", is(si.getResultSequence()))))
			        .andExpect(model().attribute("sequenceInput", hasProperty("image", is(si.getImage()))));
	}
	

  
  @Test
  public void testInvalidSearch() throws Exception {
    String id = GlycanProcedure.NotRegistered;
    String sequence = "SomethingWeird";
    logger.debug("sequence:>" + sequence + "<");

    SequenceInput si = new SequenceInput();
    si.setId(id);
    si.setSequenceInput(sequence);
    si.setResultSequence("WURCS%3D2.0%2F6%2C13%2C12%2F%5Ba2122h-1x_1-5_2*NCC%2F3%3DO%5D%5Ba2122h-1b_1-5_2*NCC%2F3%3DO%5D%5Ba1122h-1b_1-5%5D%5Ba1122h-1a_1-5%5D%5Ba2112h-1b_1-5%5D%5Ba1221m-1a_1-5%5D%2F1-2-3-4-2-2-5-4-2-5-2-6-5%2Fa4-b1_b4-c1_c3-d1_c6-h1_d2-e1_d4-f1_f4-g1_h2-i1_h6-k1_i4-j1_k3-l1_k4-m1");
    si.setImage("/glycans/" + id + "/image?style=extended&format=png&notation=cfg");
      mockMvc.perform(
          post("/Structures/structure").with(csrf()).contentType(
              MediaType.APPLICATION_FORM_URLENCODED).param(
              "sequenceInput", sequence))
          .andExpect(status().is3xxRedirection())
//          .andExpect(view().name("redirect:/Structures/structureSearch"))
          .andExpect(redirectedUrl("/Structures/structureSearch"));
  }
	
	 @Test
	  public void testStructureEntryPage() throws Exception {
	    mockMvc.perform(get("/Structures/Glycans/G00051MO"))
	        .andExpect(status().isOk())
	        .andExpect(view().name("structures/entry"))
	        .andExpect(
	            model().attribute("description",
	                containsString("G00051MO")));
	  }
	
	 
	 @Test
	  public void testInvalidEntryPage() throws Exception {
	    mockMvc.perform(get("/Structures/Glycans/GTESTING"))
	        .andExpect(status().is3xxRedirection());
	  }
}