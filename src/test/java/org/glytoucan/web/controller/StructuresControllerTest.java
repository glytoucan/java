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
	    si.setSequence(sequence);
	    si.setResultSequence(URLEncoder.encode("WURCS=2.0/1,1,0/[a2112h-1x_1-5]/1/", "UTF-8"));
	    
	    si.setImage("/glycans/G68158BT/image?style=extended&format=png&notation=cfg");
	      mockMvc.perform(
	          post("/Structures/structure").with(csrf()).contentType(
	              MediaType.APPLICATION_FORM_URLENCODED).param(
	              "sequence", sequence))
	          .andExpect(status().isOk())
	          .andExpect(view().name("structures/structure"))
	          .andExpect(model().attribute("sequenceInput", hasProperty("sequence", is(sequence))))
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
		si.setSequence(sequence);
		si.setResultSequence("WURCS%3D2.0%2F2%2C2%2C1%2F%5Ba2112h-1a_1-5_2*NCC%2F3%3DO%5D%5Ba2112h-1b_1-5%5D%2F1-2%2Fa3-b1");
		
		si.setImage("/glycans/G00031MO/image?style=extended&format=png&notation=cfg");
			mockMvc.perform(
					post("/Structures/structure").with(csrf()).contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequence", sequence))
					.andExpect(status().isOk())
					.andExpect(view().name("structures/structure"))
					.andExpect(model().attribute("sequenceInput", hasProperty("sequence", is(sequence))))
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
		si.setSequence(sequence);
//		si.setResultSequence(URLEncoder.encode("WURCS=2.0/2,2,1/[22112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1", "UTF-8"));
//		si.setResultSequence("WURCS=2.0/6,10,11/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5][a2122h-1b_1-5_2*NCC/3=O_6*OSO/3=O/3=O][a1221m-1a_1-5][Aad21122h-2a_2-6_5*NCC/3=O][a2112h-1b_1-5_2*NCC/3=O_6*OSO/3=O/3=O]/1-2-3-4-2-5-6-4-2-5/a3-b1_a6-g1_b3-c1_c3-d1_c4-e1_e3-f2_g3-h1_g4-i1_i3-j2_c1-e3~n_g1-i3~n");
		si.setResultSequence(URLEncoder.encode("WURCS=2.0/6,10,11/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5][a2122h-1b_1-5_2*NCC/3=O_6*OSO/3=O/3=O][a1221m-1a_1-5][Aad21122h-2a_2-6_5*NCC/3=O][a2112h-1b_1-5_2*NCC/3=O_6*OSO/3=O/3=O]/1-2-3-4-2-5-6-4-2-5/a3-b1_a6-g1_b3-c1_c3-d1_c4-e1_e3-f2_g3-h1_g4-i1_i3-j2_c1-e3~n_g1-i3~n", "UTF-8"));
		si.setImage("data:image/png;base64,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");
		
			mockMvc.perform(
					post("/Structures/structure").with(csrf()).contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequence", sequence))
					.andExpect(status().isOk())
					.andExpect(view().name("structures/structure"))
					.andExpect(model().attribute("sequenceInput", hasProperty("sequence", is(sequence))))
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
		si.setSequence(sequence);
		si.setResultSequence(URLEncoder.encode("WURCS=2.0/4,5,4/[a2112h-1x_1-5_2*NCC/3=O][a2112h-1b_1-5][a2112h-1a_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O_6*OSO/3=O/3=O]/1-2-3-2-4/a3-b1_a6-e1_b4-c1_c3-d1", "UTF-8"));
//		si.setResultSequence("WURCS%3D2.0%2F2%2C2%2C1%2F%5B22112h-1a_1-5_2*NCC%2F3%3DO%5D%5B12112h-1b_1-5%5D%2F1-2%2Fa3-b1");
		si.setImage("/glycans/" + id + "/image?style=extended&format=png&notation=cfg");
			mockMvc.perform(
					post("/Structures/structure").with(csrf()).contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequence", sequence))
					.andExpect(status().isOk())
					.andExpect(view().name("structures/structure"))
					.andExpect(model().attribute("sequenceInput", hasProperty("sequence", is(sequence))))
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
		si.setSequence(sequence);
//		si.setResultSequence("WURCS=2.0/4,6,5/[u2122h_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5]/1-2-3-4-2-4/a4-b1_b4-c1_c3-d1_c6-f1_e1-d2|d4");
		si.setResultSequence("WURCS%3D2.0%2F2%2C2%2C1%2F%5Ba2112h-1a_1-5_2*NCC%2F3%3DO%5D%5Ba2112h-1b_1-5%5D%2F1-2%2Fa3-b1");
		si.setImage("/glycans/" + id + "/image?style=extended&format=png&notation=cfg");
			mockMvc.perform(
					post("/Structures/structure").with(csrf()).contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequence", sequence))
					.andExpect(status().isOk())
					.andExpect(view().name("structures/structure"))
					.andExpect(model().attribute("sequenceInput", hasProperty("sequence", is(sequence))))
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
		si.setSequence(sequence);
		si.setResultSequence("WURCS%3D2.0%2F6%2C13%2C12%2F%5Ba2122h-1x_1-5_2*NCC%2F3%3DO%5D%5Ba2122h-1b_1-5_2*NCC%2F3%3DO%5D%5Ba1122h-1b_1-5%5D%5Ba1122h-1a_1-5%5D%5Ba2112h-1b_1-5%5D%5Ba1221m-1a_1-5%5D%2F1-2-3-4-2-2-5-4-2-5-2-6-5%2Fa4-b1_b4-c1_c3-d1_c6-h1_d2-e1_d4-f1_f4-g1_h2-i1_h6-k1_i4-j1_k3-l1_k4-m1");
		si.setImage("/glycans/" + id + "/image?style=extended&format=png&notation=cfg");
			mockMvc.perform(
					post("/Structures/structure").with(csrf()).contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequence", sequence))
					.andExpect(status().isOk())
					.andExpect(view().name("structures/structure"))
					.andExpect(model().attribute("sequenceInput", hasProperty("sequence", is(sequence))))
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
    si.setSequence(sequence);
    si.setResultSequence("WURCS%3D2.0%2F6%2C13%2C12%2F%5Ba2122h-1x_1-5_2*NCC%2F3%3DO%5D%5Ba2122h-1b_1-5_2*NCC%2F3%3DO%5D%5Ba1122h-1b_1-5%5D%5Ba1122h-1a_1-5%5D%5Ba2112h-1b_1-5%5D%5Ba1221m-1a_1-5%5D%2F1-2-3-4-2-2-5-4-2-5-2-6-5%2Fa4-b1_b4-c1_c3-d1_c6-h1_d2-e1_d4-f1_f4-g1_h2-i1_h6-k1_i4-j1_k3-l1_k4-m1");
    si.setImage("/glycans/" + id + "/image?style=extended&format=png&notation=cfg");
      mockMvc.perform(
          post("/Structures/structure").with(csrf()).contentType(
              MediaType.APPLICATION_FORM_URLENCODED).param(
              "sequence", sequence))
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