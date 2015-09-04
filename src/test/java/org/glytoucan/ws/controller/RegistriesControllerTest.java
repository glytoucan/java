package org.glytoucan.ws.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
public class RegistriesControllerTest {
	public static Log logger = (Log) LogFactory
			.getLog(RegistriesControllerTest.class);

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testRegistriesStart() throws Exception {
		mockMvc.perform(get("/Registries/index"))
				.andExpect(status().isOk())
				.andExpect(view().name("register/index"))
				.andExpect(
						model().attribute("sequence",
								hasProperty("sequence")));
	}

	@Test
	public void testRegisterG00031MO() throws Exception {
		String sequence = "RES\\n" + 
	"1b:a-dgal-HEX-1:5\\n" + 
				"2s:n-acetyl\\n" + 
				"3b:b-dgal-HEX-1:5\\n" + 
				"LIN\\n" + 
				"1:1d(2+1)2n\\n" + 
				"2:1o(3+1)3d";
		logger.debug("sequence:>" + sequence + "<");
		SequenceInput si = new SequenceInput();
		si.setId("G00031MO");
		si.setSequence(sequence);
		si.setResultSequence("WURCS=2.0/2,2,1/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1");
//		si.setResultSequence("WURCS%3D2.0%2F2%2C2%2C1%2F%5B22112h-1a_1-5_2*NCC%2F3%3DO%5D%5B12112h-1b_1-5%5D%2F1-2%2Fa3-b1");
		si.setImage("/glycans/G00031MO/image?style=extended&format=png&notation=cfg");

			mockMvc.perform(
					post("/Registries/confirmation").contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequence", sequence))
					.andExpect(status().isOk())
					.andExpect(view().name("register/confirmation"))
					.andExpect(model().attribute("listRegistered", contains(si)));
	}
	
	@Test
	public void testRegisterG00031MOG01132OH() throws Exception {
		String id = "G00031MO";
		String sequence = "RES\\n" + 
	"1b:a-dgal-HEX-1:5\\n" + 
				"2s:n-acetyl\\n" + 
				"3b:b-dgal-HEX-1:5\\n" + 
				"LIN\\n" + 
				"1:1d(2+1)2n\\n" + 
				"2:1o(3+1)3d";
		
		String id2 = "G01132OH";
		String sequence2 = "RES\\n"
				+ "1b:x-dgal-HEX-1:5\\n"
				+ "2s:n-acetyl\\n"
				+ "3b:b-dgal-HEX-1:5\\n"
				+ "4b:a-dgal-HEX-1:5\\n"
				+ "5s:n-acetyl\\n"
				+ "6b:b-dgal-HEX-1:5\\n"
				+ "7b:b-dglc-HEX-1:5\\n"
				+ "8s:n-acetyl\\n"
				+ "9s:sulfate\\n"
				+ "LIN\\n"
				+ "1:1d(2+1)2n\\n"
				+ "2:1o(3+1)3d\\n"
				+ "3:3o(4+1)4d\\n"
				+ "4:4d(2+1)5n\\n"
				+ "5:4o(3+1)6d\\n"
				+ "6:1o(6+1)7d\\n"
				+ "7:7d(2+1)8n\\n"
				+ "8:7o(6+1)9n";
		
		logger.debug("sequence:>" + sequence + "<");
		logger.debug("sequence2:>" + sequence2 + "<");
		SequenceInput si = new SequenceInput();
		si.setId(id);
		si.setSequence(sequence);
		si.setResultSequence("WURCS=2.0/2,2,1/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1");
		si.setImage("/glyspace/service/glycans/" + id + "/image?style=extended&format=png&notation=cfg");

//		ListRegistered=
//		SequenceInput [sequence=RES\n1b:a-dgal-HEX-1:5\n2s:n-acetyl\n3b:b-dgal-HEX-1:5\nLIN\n1:1d(2+1)2n\n2:1o(3+1)3d, resultSequence=WURCS=2.0/2,2,1/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1, image=/glyspace/service/glycans/G00031MO/image?style=extended&format=png&notation=cfg, id=G00031MO],
//		SequenceInput [sequence=RES\n1b:x-dgal-HEX-1:5\n2s:n-acetyl\n3b:b-dgal-HEX-1:5\n4b:a-dgal-HEX-1:5\n5s:n-acetyl\n6b:b-dgal-HEX-1:5\n7b:b-dglc-HEX-1:5\n8s:n-acetyl\n9s:sulfate\nLIN\n1:1d(2+1)2n\n2:1o(3+1)3d\n3:3o(4+1)4d\n4:4d(2+1)5n\n5:4o(3+1)6d\n6:1o(6+1)7d\n7:7d(2+1)8n\n8:7o(6+1)9n, resultSequence=WURCS=2.0/4,5,4/[a2112h-1x_1-5_2*NCC/3=O][a2112h-1b_1-5][a2112h-1a_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O_6*OSO/3=O/3=O]/1-2-3-2-4/a3-b1_a6-e1_b4-c1_c3-d1, image=/glyspace/service/glycans/G01132OH/image?style=extended&format=png&notation=cfg, id=G01132OH]]

		SequenceInput si2 = new SequenceInput();
		si2.setId(id2);
		si2.setSequence(sequence2);
		si2.setResultSequence("WURCS=2.0/4,5,4/[a2112h-1x_1-5_2*NCC/3=O][a2112h-1b_1-5][a2112h-1a_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O_6*OSO/3=O/3=O]/1-2-3-2-4/a3-b1_a6-e1_b4-c1_c3-d1");
		si2.setImage("/glyspace/service/glycans/" + id2 + "/image?style=extended&format=png&notation=cfg");
		
			mockMvc.perform(
					post("/Registries/confirmation").contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequence", sequence+"\n"+sequence2))
					.andExpect(status().isOk())
					.andExpect(view().name("register/confirmation"))
					.andExpect(model().attribute("listRegistered", containsInAnyOrder(equalTo(si), equalTo(si2))));
	}
	
	@Test
	public void testRegisterNewErrorAndG00031MO() throws Exception {
		String id = "G00031MO";
		String sequence = "RES\\n" + 
	"1b:a-dgal-HEX-1:5\\n" + 
				"2s:n-acetyl\\n" + 
				"3b:b-dgal-HEX-1:5\\n" + 
				"LIN\\n" + 
				"1:1d(2+1)2n\\n" + 
				"2:1o(3+1)3d";
		
		String id2 = "";
		String sequence2 = "RES\\n"
				+ "1b:x-dglc-HEX-1:5|1:a\\n"
				+ "2b:b-dgal-HEX-1:5\\n"
				+ "LIN\\n"
				+ "1:1o(4+1)2d";
		
		logger.debug("sequence:>" + sequence + "<");
		logger.debug("sequence2:>" + sequence2 + "<");

		SequenceInput si = new SequenceInput();
		si.setId(id);
		si.setSequence(sequence);
		si.setResultSequence("WURCS=2.0/2,2,1/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1");
		si.setImage("/glyspace/service/glycans/" + id + "/image?style=extended&format=png&notation=cfg");
//		sequence=RES\n1b:x-dglc-HEX-1:5\n2b:x-dman-HEX-1:5\nLIN\n1:1o(-1+1)2d, resultSequence=Failed Conversion:org.eurocarbdb.MolecularFramework.io.SugarImporterException, image=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALoAAABSCAIAAABse1lJAAAEf0lEQVR42u2aP2hbRxzHvx2SOoraGNIalSpYYKeR01eiJCr1UKihggrqBA8uZNCgBA2miEJAgwmi2KDBgw02eHBoKc7QIWCoKCqoxPCgpRDQ4EGDhwzeM8gQQQTy0P5Osp3Qxvrz1HuXXr8ffru+797n7n73TviTkJ4Bh4BQF0JdCHUh1IVQF0JdOASEuhDqQqgLoS6EuhDqwiEg1IVQF0JdCHUh1IVQFw4BoS6EuhDqQqgLoS6EunAICHUh1IVQF0JdiEYODg7+L7pUKpV8Pp9IJNDCcZxkMlkoFHZ3d42/htc52zE7OzuxWMyUMf7p4rounLMIv4nbIayO4+Fl/OTgxwmsjOHWCEZOybuRsTAyCiobPgHGgHtACagCT4AdoAh8A7xvMNvf1pVoNLq8vGzz6iIPmcvl8N5pLEbw+Nqr64+ryF3A+VM+j8VhNowCD4DmCfUcWANCBt9Tm3K5LLrYvBnJs+GzYUy+jUdXTnTluH7+CBOBbDbrmyvATeAL4OnJrhzXHnDdt2yvZH5+fmFhweZWV8YXn55Ti0dXV9rlxsQYf+axyoYvW4tHs7eqiTEG15h0Or20tGStLqVSSTUrYkCPrhytMaFQSHevoLKpZqXWsyuHa4wP2U4ik8lsbGzYqYta6sfOqE62L1falbsg3aXmbejDVifb7L/WtGbrgCwtZndDjbpIX6aOQh5caXW+4XBY3wlWZVNHoaaneq41W+fY8XjcTl1k5cTdsEddpG6cLxQKGrNhxasuUml92TpQr9eDwWCj0bBQFznyqc8qnnXJj+pb81U29VnFsy7fmdqPpqamisWihbrgDfTd5L5ca+OO4+jKpsLVBtDlF33ZOrO6uirnIxt1ETy7IvX9JeilOUD9DnMEAgELLwGGhobw2wCry/pF2TI0ZsOzAXT5VV+2rkxOTm5vb9umy/h462LIsy7fRhKJhMZs6mLIsy4/6MvWFemy5+bmbNMllUrh3qh3Xb56N5/Pa8yG+wPo8rW+bF2RM3wkErFNl62tLXz8lmddZAGoVCoas+Fzz7pozdYLw8PDe3t7VunSaDRkEuD+B150WYzIDq09G1xPujzQmq0XZCsU463SRdjc3MREoI/7xXY9uiLv0nVd7dlwvZ/7xXY99SFbV3K5nJG7Ru030rOzs7j5Tl+f/5PJpAyHDw+vsuFOX5//fcvWmWw2a+SfDNp1qdfrsnQrY3pZY9zY9PT0zMyMP98VDrMpY3pZY2p+ZutMOp1eX1+3UJf2W5F5HI/Hu/QxK2PSQsq88fN9vMjWpY8p+p+tc+NlpNf277+60ivIQ6o/Tsvp+uHlwy94bkzdK90Nyyx3HKdUKhl5AS+yqdN19egLXq11r7RiNts/kW3I1L00fJ4W0s+nUimZqa3vqggGg9FoNJPJlMtlsxP3dc72Mvv7+6FQqFqt2q8L+beMMfXT1IVQF0JdCHUh1IVQF0KoC6EuhLoQ6kKoC6EuhFAXQl0IdSHUhVAXQl0IoS6EuhDqQqgLoS6EuhBCXQh1IdSFUBfyX+IvRZf444HWjGUAAAAASUVORK5CYII=, id=could not convert]]
		SequenceInput si2 = new SequenceInput();
		si2.setId(GlycanProcedure.CouldNotConvert);
		si2.setSequence(sequence2);
		si2.setResultSequence("Failed Conversion:org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitorException: Error in GlycoCT validation:>Carbonyl acid contained in the ring. It must be use a substituent \"lactone\". :x-dglc-HEX-1:5|1:a<");
		si2.setImage(null);
		mockMvc.perform(
			post("/Registries/confirmation").contentType(
					MediaType.APPLICATION_FORM_URLENCODED).param(
					"sequence", sequence+"\\n"+sequence2))
			.andExpect(status().isOk())
			.andExpect(view().name("register/confirmation"))
			.andExpect(model().attribute("listRegistered", contains(si)))
			.andExpect(model().attribute("listErrors", contains(si2)));
	}
	
	@Test
	public void testRegisterNewAndG00031MO() throws Exception {
		String id = "G00031MO";
		String sequence = "RES\\n" + 
	"1b:a-dgal-HEX-1:5\\n" + 
				"2s:n-acetyl\\n" + 
				"3b:b-dgal-HEX-1:5\\n" + 
				"LIN\\n" + 
				"1:1d(2+1)2n\\n" + 
				"2:1o(3+1)3d";
		
		String id2 = "";
		String sequence2 = "RES\\n"
				+ "1b:x-dman-HEX-1:5\\n"
				+ "2b:x-dgal-HEX-1:5\\n"
				+ "3s:n-acetyl\\n"
				+ "LIN\\n"
				+ "1:1o(-1+1)2d\\n"
				+ "2:2d(2+1)3n";
		
		logger.debug("sequence:>" + sequence + "<");
		logger.debug("sequence2:>" + sequence2 + "<");

		SequenceInput si = new SequenceInput();
		si.setId(id);
		si.setSequence(sequence);
		si.setResultSequence("WURCS=2.0/2,2,1/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1");
		si.setImage("/glyspace/service/glycans/" + id + "/image?style=extended&format=png&notation=cfg");
		SequenceInput si2 = new SequenceInput();
//		SequenceInput [sequence=RES\n1b:x-dman-HEX-1:5\n2b:x-dgal-HEX-1:5\n3s:n-acetyl\nLIN\n1:1o(-1+1)2d\n2:2d(2+1)3n, resultSequence=, image=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALoAAABSCAIAAABse1lJAAADkUlEQVR42u3bv0sbYRjA8aeDrU3TNmArKUQMmLZRrpiWFDIU6pAhgy0OFjpkSCGDQxbhBimhKGRwUEghg4UOLh0EoVIyBBQOCoVCBgcHR/8EhwwZHNLnjLUdLJprc28av1/eWeXez7333g+lRXThhENAcCG4EFwILgQXgguHgOBCcCG4EFwILgQXDgHBheBCcCG4EFwILhwCggvBheBCcCG4EFw4BAQXggvBheBCXezo6KgPuci/yM9jUa/Xi8ViOp1u/2rLsjKZTKlU2t/f7x0ru7u7iUTClJjucjn++d6Hb1wcxxHrhkSuyZuwlGOyMSGfLfk0Lqtj8npYhgfUjc5TL6wr8Xh8ZWWlP1eX3ueiE2Dbtty7KktR+f7k7PHtsdgjMjRgcJ7a1Wo15dK3F6Me56LHXZ6HJHVLtif/aOV0fHkk44FCoWCQy8LCwuLiYn9udXufi869PLvtLh7nWmkPJ6FiDK4xuVxueXkZLga4VKtVd7OiAi5o5ecaEw6HTe1j8vn82toaXPzm4l6Gxq67O9mOrLSHPaI7XyOzpUuL2avhJeWie0b3VsiDleOdbyQSMXJ3rX92MpmEi99cdFWX+YhHLjpeDJVKJf9nq9FoBIPBZrMJF1+56O2o+1jFM5fiqKnr0dTU1NbWFlx85SJXpONN7u/jfcyyLCMTVi6X9f4ILmdz6WKerej4+FDMFQgEeAng6+oyODgoX/9idanc18uZqVM8lUrt7OzAxT8usdjxiyHPXN5F0+m0KS66y56bm4OLf1yy2ay8HfXO5dXdYrFoiovew0ejUbj4x2Vzc1Oe3vTMRRener1u8PlHKBQ6ODiAi09cms2mnqDy4YEXLktR3T20jKaXQhUPF//eGa2vr8t4oIP3i+2xPanOHMcxy8W2bSPvGi/1G+nZ2Vl5eaejx/+ZTEanqmW6QqFg5EuGS82l0WjoZcUVc5E1xklMT0/PzMwY/DrptFwuV6lU4OL313QqRteYZDJ5zj5mdUy3t3pO94KV9sbLyF6bb3VP9jE6Ae5H3Xp3vTFx8gTPSbjvleYjugJZllWtVlu9kV6GTL2X5j8Bfp2yeq+RzWZ1FXGf+YoEg8F4PJ7P52u1Wi8sKu0ODw/D4fDe3l6/caHuiTH1q+FCcCG4EFwILgQXIrgQXAguBBeCC8GFCC4EF4ILwYXgQnAhggvBheBCcCG4EFyI4EJwIbgQXOh/6geJSrubc4F5CgAAAABJRU5ErkJggg==, id=null]
		si2.setId(null);
		si2.setSequence(sequence2);
		si2.setResultSequence("WURCS=2.0/2,2,1/[a1122h-1x_1-5][a2112h-1x_1-5_2*NCC/3=O]/1-2/a?-b1");
		si2.setImage("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALoAAABSCAIAAABse1lJAAADkUlEQVR42u3bv0sbYRjA8aeDrU3TNmArKUQMmLZRrpiWFDIU6pAhgy0OFjpkSCGDQxbhBimhKGRwUEghg4UOLh0EoVIyBBQOCoVCBgcHR/8EhwwZHNLnjLUdLJprc28av1/eWeXez7333g+lRXThhENAcCG4EFwILgQXgguHgOBCcCG4EFwILgQXDgHBheBCcCG4EFwILhwCggvBheBCcCG4EFw4BAQXggvBheBCXezo6KgPuci/yM9jUa/Xi8ViOp1u/2rLsjKZTKlU2t/f7x0ru7u7iUTClJjucjn++d6Hb1wcxxHrhkSuyZuwlGOyMSGfLfk0Lqtj8npYhgfUjc5TL6wr8Xh8ZWWlP1eX3ueiE2Dbtty7KktR+f7k7PHtsdgjMjRgcJ7a1Wo15dK3F6Me56LHXZ6HJHVLtif/aOV0fHkk44FCoWCQy8LCwuLiYn9udXufi869PLvtLh7nWmkPJ6FiDK4xuVxueXkZLga4VKtVd7OiAi5o5ecaEw6HTe1j8vn82toaXPzm4l6Gxq67O9mOrLSHPaI7XyOzpUuL2avhJeWie0b3VsiDleOdbyQSMXJ3rX92MpmEi99cdFWX+YhHLjpeDJVKJf9nq9FoBIPBZrMJF1+56O2o+1jFM5fiqKnr0dTU1NbWFlx85SJXpONN7u/jfcyyLCMTVi6X9f4ILmdz6WKerej4+FDMFQgEeAng6+oyODgoX/9idanc18uZqVM8lUrt7OzAxT8usdjxiyHPXN5F0+m0KS66y56bm4OLf1yy2ay8HfXO5dXdYrFoiovew0ejUbj4x2Vzc1Oe3vTMRRener1u8PlHKBQ6ODiAi09cms2mnqDy4YEXLktR3T20jKaXQhUPF//eGa2vr8t4oIP3i+2xPanOHMcxy8W2bSPvGi/1G+nZ2Vl5eaejx/+ZTEanqmW6QqFg5EuGS82l0WjoZcUVc5E1xklMT0/PzMwY/DrptFwuV6lU4OL313QqRteYZDJ5zj5mdUy3t3pO94KV9sbLyF6bb3VP9jE6Ae5H3Xp3vTFx8gTPSbjvleYjugJZllWtVlu9kV6GTL2X5j8Bfp2yeq+RzWZ1FXGf+YoEg8F4PJ7P52u1Wi8sKu0ODw/D4fDe3l6/caHuiTH1q+FCcCG4EFwILgQXIrgQXAguBBeCC8GFCC4EF4ILwYXgQnAhggvBheBCcCG4EFyI4EJwIbgQXOh/6geJSrubc4F5CgAAAABJRU5ErkJggg==");

			mockMvc.perform(
					post("/Registries/confirmation").contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequence", sequence+"\\n"+sequence2))
					.andExpect(status().isOk())
					.andExpect(view().name("register/confirmation"))
					.andExpect(model().attribute("listRegistered", contains(si)))
					.andExpect(model().attribute("listNew", contains(si2)));
	}

	@Test
	public void testRegisterNewAndG00031MOAndError() throws Exception {
		
		/*
RES
1b:a-dgal-HEX-1:5
2s:n-acetyl
3b:b-dgal-HEX-1:5
LIN
1:1d(2+1)2n
2:1o(3+1)3d
RES
1b:x-dgro-dgal-NON-2:6|1:a|2:keto|3:d
2b:x-dglc-HEX-1:5
3s:n-acetyl
4s:n-acetyl
LIN
1:1o(-1+1)2d
2:2d(2+1)3n
3:1d(5+1)4n
RES
1b:x-dman-HEX-1:5
2b:x-dgal-HEX-1:5
3s:n-acetyl
LIN
1:1o(-1+1)2d
2:2d(2+1)3n
RES
1b:x-dglc-HEX-1:5|1:a
2b:b-dgal-HEX-1:5
LIN
1:1o(4+1)2d
		*/
		String id = "G00031MO";
		String sequence = "RES\\n" + 
	"1b:a-dgal-HEX-1:5\\n" + 
				"2s:n-acetyl\\n" + 
				"3b:b-dgal-HEX-1:5\\n" + 
				"LIN\\n" + 
				"1:1d(2+1)2n\\n" + 
				"2:1o(3+1)3d";
		
		String id2 = "";
		String sequence2 = "RES\\n"
				+ "1b:x-dman-HEX-1:5\\n"
				+ "2b:x-dgal-HEX-1:5\\n"
				+ "3s:n-acetyl\\n"
				+ "LIN\\n"
				+ "1:1o(-1+1)2d\\n"
				+ "2:2d(2+1)3n";
		
		String sequence3 = "RES\\n"
				+ "1b:x-dglc-HEX-1:5|1:a\\n"
				+ "2b:b-dgal-HEX-1:5\\n"
				+ "LIN\\n"
				+ "1:1o(4+1)2d";

		
		logger.debug("sequence:>" + sequence + "<");
		logger.debug("sequence2:>" + sequence2 + "<");
		logger.debug("sequence3:>" + sequence3 + "<");

		SequenceInput si = new SequenceInput();
		si.setId(id);
		si.setSequence(sequence);
		si.setResultSequence("WURCS=2.0/2,2,1/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1");
		si.setImage("/glycans/" + id + "/image?style=extended&format=png&notation=cfg");
		SequenceInput si2 = new SequenceInput();
		si2.setId(null);
		si2.setSequence(sequence2);
		si2.setResultSequence("WURCS=2.0/2,2,1/[a1122h-1x_1-5][a2112h-1x_1-5_2*NCC/3=O]/1-2/a?-b1");
		si2.setImage("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALoAAABSCAIAAABse1lJAAADkUlEQVR42u3bv0sbYRjA8aeDrU3TNmArKUQMmLZRrpiWFDIU6pAhgy0OFjpkSCGDQxbhBimhKGRwUEghg4UOLh0EoVIyBBQOCoVCBgcHR/8EhwwZHNLnjLUdLJprc28av1/eWeXez7333g+lRXThhENAcCG4EFwILgQXgguHgOBCcCG4EFwILgQXDgHBheBCcCG4EFwILhwCggvBheBCcCG4EFw4BAQXggvBheBCXezo6KgPuci/yM9jUa/Xi8ViOp1u/2rLsjKZTKlU2t/f7x0ru7u7iUTClJjucjn++d6Hb1wcxxHrhkSuyZuwlGOyMSGfLfk0Lqtj8npYhgfUjc5TL6wr8Xh8ZWWlP1eX3ueiE2Dbtty7KktR+f7k7PHtsdgjMjRgcJ7a1Wo15dK3F6Me56LHXZ6HJHVLtif/aOV0fHkk44FCoWCQy8LCwuLiYn9udXufi869PLvtLh7nWmkPJ6FiDK4xuVxueXkZLga4VKtVd7OiAi5o5ecaEw6HTe1j8vn82toaXPzm4l6Gxq67O9mOrLSHPaI7XyOzpUuL2avhJeWie0b3VsiDleOdbyQSMXJ3rX92MpmEi99cdFWX+YhHLjpeDJVKJf9nq9FoBIPBZrMJF1+56O2o+1jFM5fiqKnr0dTU1NbWFlx85SJXpONN7u/jfcyyLCMTVi6X9f4ILmdz6WKerej4+FDMFQgEeAng6+oyODgoX/9idanc18uZqVM8lUrt7OzAxT8usdjxiyHPXN5F0+m0KS66y56bm4OLf1yy2ay8HfXO5dXdYrFoiovew0ejUbj4x2Vzc1Oe3vTMRRener1u8PlHKBQ6ODiAi09cms2mnqDy4YEXLktR3T20jKaXQhUPF//eGa2vr8t4oIP3i+2xPanOHMcxy8W2bSPvGi/1G+nZ2Vl5eaejx/+ZTEanqmW6QqFg5EuGS82l0WjoZcUVc5E1xklMT0/PzMwY/DrptFwuV6lU4OL313QqRteYZDJ5zj5mdUy3t3pO94KV9sbLyF6bb3VP9jE6Ae5H3Xp3vTFx8gTPSbjvleYjugJZllWtVlu9kV6GTL2X5j8Bfp2yeq+RzWZ1FXGf+YoEg8F4PJ7P52u1Wi8sKu0ODw/D4fDe3l6/caHuiTH1q+FCcCG4EFwILgQXIrgQXAguBBeCC8GFCC4EF4ILwYXgQnAhggvBheBCcCG4EFyI4EJwIbgQXOh/6geJSrubc4F5CgAAAABJRU5ErkJggg==");
		SequenceInput si3 = new SequenceInput();
		si3.setId(GlycanProcedure.CouldNotConvert);
		si3.setSequence(sequence3);
		si3.setResultSequence("Failed Conversion:org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitorException: Error in GlycoCT validation:>Carbonyl acid contained in the ring. It must be use a substituent \"lactone\". :x-dglc-HEX-1:5|1:a<");
		si3.setImage(null);

			mockMvc.perform(
					post("/Registries/confirmation").contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequence", sequence+"\\n"+sequence2+"\\n"+sequence3))
					.andExpect(status().isOk())
					.andExpect(view().name("register/confirmation"))
					.andExpect(model().attribute("listRegistered", contains(si)))
					.andExpect(model().attribute("listNew", contains(si2)))
					.andExpect(model().attribute("listErrors", contains(si3)));
	}


	@Test
	public void testRegisterNew() throws Exception {
		
		/*
RES
1b:x-dgal-HEX-1:5
2b:x-dman-HEX-1:5
3s:n-acetyl
LIN
1:1o(-1+1)2d
2:1d(2+1)3n
		*/
//		String id = "G00031MO";
		String sequence = "RES\\n"
				+ "1b:x-dgal-HEX-1:5\\n"
				+ "2b:x-dman-HEX-1:5\\n"
				+ "3s:n-acetyl\\n"
				+ "LIN\\n"
				+ "1:1o(-1+1)2d\\n"
				+ "2:1d(2+1)3n";
		
		logger.debug("sequence:>" + sequence + "<");
		SequenceInput si = new SequenceInput();
		si.setId(null);
		si.setSequence(sequence);
		si.setResultSequence("WURCS=2.0/2,2,1/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1");
//		si.setImage("/glycans/" + id + "/image?style=extended&format=png&notation=cfg");
			mockMvc.perform(
					post("/Registries/confirmation").contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequence", sequence))
					.andExpect(status().isOk())
					.andExpect(view().name("register/confirmation"))
					.andExpect(model().attribute("listRegistered", contains(si)));
	}
	

	@Test
	public void testRegisterNew2() throws Exception {
		
		/*
RES
1b:x-dman-HEX-1:5
2b:x-dgal-HEX-1:5
LIN
1:1o(-1+1)2d
		*/
//		String id = "G00031MO";
		String sequence = "RES\\n"
				+ "1b:x-dman-HEX-1:5\\n"
				+ "2b:x-dgal-HEX-1:5\\n"
				+ "LIN\\n"
				+ "1:1o(-1+1)2d\\n";
		
		logger.debug("sequence:>" + sequence + "<");
//		String[] resultSequence = request.getParameterValues("resultSequence");
//		String[] origSequence = request.getParameterValues("sequence");
//		String[] image = request.getParameterValues("image");

		
		SequenceInput si = new SequenceInput();
		si.setId(null);
		si.setSequence(sequence);
		si.setResultSequence("WURCS=2.0/2,2,1/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1");
//		si.setImage("/glycans/" + id + "/image?style=extended&format=png&notation=cfg");
			mockMvc.perform(
					post("/Registries/complete").contentType(
							MediaType.APPLICATION_FORM_URLENCODED)
							.param("checked", "on")
							.param("resultSequence", "WURCS=2.0/2,2,1/[a1122h-1x_1-5][a2112h-1x_1-5]/1-2/a?-b1")
							.param("sequence", sequence)
							.param("image", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALoAAABSCAIAAABse1lJAAADkUlEQVR42u3bv0sbYRjA8aeDrU3TNmArKUQMmLZRrpiWFDIU6pAhgy0OFjpkSCGDQxbhBimhKGRwUEghg4UOLh0EoVIyBBQOCoVCBgcHR/8EhwwZHNLnjLUdLJprc28av1/eWeXez7333g+lRXThhENAcCG4EFwILgQXgguHgOBCcCG4EFwILgQXDgHBheBCcCG4EFwILhwCggvBheBCcCG4EFw4BAQXggvBheBCXezo6KgPuci/yM9jUa/Xi8ViOp1u/2rLsjKZTKlU2t/f7x0ru7u7iUTClJjucjn++d6Hb1wcxxHrhkSuyZuwlGOyMSGfLfk0Lqtj8npYhgfUjc5TL6wr8Xh8ZWWlP1eX3ueiE2Dbtty7KktR+f7k7PHtsdgjMjRgcJ7a1Wo15dK3F6Me56LHXZ6HJHVLtif/aOV0fHkk44FCoWCQy8LCwuLiYn9udXufi869PLvtLh7nWmkPJ6FiDK4xuVxueXkZLga4VKtVd7OiAi5o5ecaEw6HTe1j8vn82toaXPzm4l6Gxq67O9mOrLSHPaI7XyOzpUuL2avhJeWie0b3VsiDleOdbyQSMXJ3rX92MpmEi99cdFWX+YhHLjpeDJVKJf9nq9FoBIPBZrMJF1+56O2o+1jFM5fiqKnr0dTU1NbWFlx85SJXpONN7u/jfcyyLCMTVi6X9f4ILmdz6WKerej4+FDMFQgEeAng6+oyODgoX/9idanc18uZqVM8lUrt7OzAxT8usdjxiyHPXN5F0+m0KS66y56bm4OLf1yy2ay8HfXO5dXdYrFoiovew0ejUbj4x2Vzc1Oe3vTMRRener1u8PlHKBQ6ODiAi09cms2mnqDy4YEXLktR3T20jKaXQhUPF//eGa2vr8t4oIP3i+2xPanOHMcxy8W2bSPvGi/1G+nZ2Vl5eaejx/+ZTEanqmW6QqFg5EuGS82l0WjoZcUVc5E1xklMT0/PzMwY/DrptFwuV6lU4OL313QqRteYZDJ5zj5mdUy3t3pO94KV9sbLyF6bb3VP9jE6Ae5H3Xp3vTFx8gTPSbjvleYjugJZllWtVlu9kV6GTL2X5j8Bfp2yeq+RzWZ1FXGf+YoEg8F4PJ7P52u1Wi8sKu0ODw/D4fDe3l6/caHuiTH1q+FCcCG4EFwILgQXIrgQXAguBBeCC8GFCC4EF4ILwYXgQnAhggvBheBCcCG4EFyI4EJwIbgQXOh/6geJSrubc4F5CgAAAABJRU5ErkJggg==")
							)
					.andExpect(status().isOk())
					.andExpect(view().name("register/confirmation"))
					.andExpect(model().attribute("listNew", contains(si)));
	}


	
}