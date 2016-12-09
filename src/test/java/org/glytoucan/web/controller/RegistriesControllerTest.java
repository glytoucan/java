package org.glytoucan.web.controller;

import static java.util.Arrays.asList;
import static nl.captcha.Captcha.NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.UUID;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.service.GlycanProcedure;
import org.glytoucan.web.Application;
import org.glytoucan.web.model.SequenceInput;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.github.fromi.openidconnect.security.UserInfo;

import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;

import static org.springframework.security.core.authority.AuthorityUtils.NO_AUTHORITIES;
import static org.springframework.security.oauth2.common.AuthenticationScheme.form;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
@WebAppConfiguration
public class RegistriesControllerTest {
	public static Log logger = (Log) LogFactory
			.getLog(RegistriesControllerTest.class);

  private static String tokenValue = "ya29.GlytA-haQ7TYmVZlu8Rn3Ee7m34hw2SzE2GeAavXLyUfpsa1TPZlOkMB3qhqMtVxlOXzHRSNREr5xjcVwwYPtMA__zNopXmtbCwQuMq97sN9lLwjaINCbZ3oUlvsnQ";

	
	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;
  private MockHttpSession mockHttpSession;
  
  @Resource
  private OAuth2RestOperations restTemplate;
  
@Value("${google.oauth2.clientId}")
  private String clientId;

  @Value("${google.oauth2.clientSecret}")
  private String clientSecret;
  
  @Autowired
  RegistriesController registriesController;
  

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
    mockHttpSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
	}

	@Test
	public void testRegistriesStart() throws Exception {
		mockMvc.perform(get("/Registries/index").with(csrf()).with(user("test")))
				.andExpect(status().isOk())
				.andExpect(view().name("register/index"))
				.andExpect(
						model().attribute("sequenceInput",
								hasProperty("sequenceInput")));
	}

	@Test
  @Transactional
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
		si.setSequenceInput(sequence);
		si.setResultSequence("WURCS=2.0/2,2,1/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1");
//		si.setResultSequence("WURCS%3D2.0%2F2%2C2%2C1%2F%5B22112h-1a_1-5_2*NCC%2F3%3DO%5D%5B12112h-1b_1-5%5D%2F1-2%2Fa3-b1");
		si.setImage("/glycans/G00031MO/image?style=extended&format=png&notation=cfg");

			mockMvc.perform(
					post("/Registries/confirmation").with(csrf()).with(user("test")).contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequenceInput", sequence))
					.andExpect(status().isOk())
					.andExpect(view().name("register/confirmation"))
					.andExpect(request().attribute("listRegistered", contains(si)));
	}
	
//	@Test
//  @Transactional
	public void testRegisterG00031MOG01132OH() throws Exception {
		String id = "G00031MO";
		String sequence = "RES\n" + 
	"1b:a-dgal-HEX-1:5\n" + 
				"2s:n-acetyl\n" + 
				"3b:b-dgal-HEX-1:5\n" + 
				"LIN\n" + 
				"1:1d(2+1)2n\n" + 
				"2:1o(3+1)3d";
		
		String id2 = "G01132OH";
		String sequence2 = "RES\n"
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
		logger.debug("sequence2:>" + sequence2 + "<");
		
    String sequenceResult = sequence.replaceAll("(?:\r\n|\n)", "\\\\n").trim();
    String sequence2Result = sequence2.replaceAll("(?:\r\n|\n)", "\\\\n").trim();

		SequenceInput si = new SequenceInput();
		si.setId(id);
		si.setSequenceInput(sequenceResult);
		si.setResultSequence("WURCS=2.0/2,2,1/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1");
		si.setImage("/glycans/" + id + "/image?style=extended&format=png&notation=cfg");

//		ListRegistered=
//		SequenceInput [sequence=RES\n1b:a-dgal-HEX-1:5\n2s:n-acetyl\n3b:b-dgal-HEX-1:5\nLIN\n1:1d(2+1)2n\n2:1o(3+1)3d, resultSequence=WURCS=2.0/2,2,1/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1, image=/glyspace/service/glycans/G00031MO/image?style=extended&format=png&notation=cfg, id=G00031MO],
//		SequenceInput [sequence=RES\n1b:x-dgal-HEX-1:5\n2s:n-acetyl\n3b:b-dgal-HEX-1:5\n4b:a-dgal-HEX-1:5\n5s:n-acetyl\n6b:b-dgal-HEX-1:5\n7b:b-dglc-HEX-1:5\n8s:n-acetyl\n9s:sulfate\nLIN\n1:1d(2+1)2n\n2:1o(3+1)3d\n3:3o(4+1)4d\n4:4d(2+1)5n\n5:4o(3+1)6d\n6:1o(6+1)7d\n7:7d(2+1)8n\n8:7o(6+1)9n, resultSequence=WURCS=2.0/4,5,4/[a2112h-1x_1-5_2*NCC/3=O][a2112h-1b_1-5][a2112h-1a_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O_6*OSO/3=O/3=O]/1-2-3-2-4/a3-b1_a6-e1_b4-c1_c3-d1, image=/glyspace/service/glycans/G01132OH/image?style=extended&format=png&notation=cfg, id=G01132OH]]

		SequenceInput si2 = new SequenceInput();
		si2.setId(id2);
		si2.setSequenceInput(sequence2Result);
		si2.setResultSequence("WURCS=2.0/4,5,4/[a2112h-1x_1-5_2*NCC/3=O][a2112h-1b_1-5][a2112h-1a_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O_6*OSO/3=O/3=O]/1-2-3-2-4/a3-b1_a6-e1_b4-c1_c3-d1");
		si2.setImage("/glycans/" + id2 + "/image?style=extended&format=png&notation=cfg");
		
			mockMvc.perform(
					post("/Registries/confirmation").with(csrf()).with(user("test")).contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequence", sequence+"\n"+sequence2))
					.andExpect(status().isOk())
					.andExpect(view().name("register/confirmation"))
					.andExpect(request().attribute("listRegistered", containsInAnyOrder(equalTo(si), equalTo(si2))));
	}
	
//	@Test
//  @Transactional
	public void testRegisterNewErrorAndG00031MO() throws Exception {
		String id = "G00031MO";
		String sequence = "RES\r\n" + 
	"1b:a-dgal-HEX-1:5\r\n" + 
				"2s:n-acetyl\r\n" + 
				"3b:b-dgal-HEX-1:5\r\n" + 
				"LIN\r\n" + 
				"1:1d(2+1)2n\r\n" + 
				"2:1o(3+1)3d\r\n";
		
		String id2 = "";
		String sequence2 = "RES\r\n"
				+ "1b:x-dglc-HEX-1:5|1:a\r\n"
				+ "2b:b-dgal-HEX-1:5\r\n"
				+ "LIN\r\n"
				+ "1:1o(4+1)2d\r\n";
		
		logger.debug("sequence:>" + sequence + "<");
		logger.debug("sequence2:>" + sequence2 + "<");
		
    String sequenceResult = sequence.replaceAll("(?:\r\n|\n)", "\\\\n").trim();
    String sequence2Result = sequence2.replaceAll("(?:\r\n|\n)", "\\\\n").trim();

		SequenceInput si = new SequenceInput();
		si.setId(id);
		si.setSequenceInput(sequenceResult + "\n");
		si.setResultSequence("WURCS=2.0/2,2,1/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1");
		si.setImage("/glycans/" + id + "/image?style=extended&format=png&notation=cfg");
//		sequence=RES\n1b:x-dglc-HEX-1:5\n2b:x-dman-HEX-1:5\nLIN\n1:1o(-1+1)2d, resultSequence=Failed Conversion:org.eurocarbdb.MolecularFramework.io.SugarImporterException, image=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALoAAABSCAIAAABse1lJAAAEf0lEQVR42u2aP2hbRxzHvx2SOoraGNIalSpYYKeR01eiJCr1UKihggrqBA8uZNCgBA2miEJAgwmi2KDBgw02eHBoKc7QIWCoKCqoxPCgpRDQ4EGDhwzeM8gQQQTy0P5Osp3Qxvrz1HuXXr8ffru+797n7n73TviTkJ4Bh4BQF0JdCHUh1IVQF0JdOASEuhDqQqgLoS6EuhDqwiEg1IVQF0JdCHUh1IVQFw4BoS6EuhDqQqgLoS6EunAICHUh1IVQF0JdiEYODg7+L7pUKpV8Pp9IJNDCcZxkMlkoFHZ3d42/htc52zE7OzuxWMyUMf7p4rounLMIv4nbIayO4+Fl/OTgxwmsjOHWCEZOybuRsTAyCiobPgHGgHtACagCT4AdoAh8A7xvMNvf1pVoNLq8vGzz6iIPmcvl8N5pLEbw+Nqr64+ryF3A+VM+j8VhNowCD4DmCfUcWANCBt9Tm3K5LLrYvBnJs+GzYUy+jUdXTnTluH7+CBOBbDbrmyvATeAL4OnJrhzXHnDdt2yvZH5+fmFhweZWV8YXn55Ti0dXV9rlxsQYf+axyoYvW4tHs7eqiTEG15h0Or20tGStLqVSSTUrYkCPrhytMaFQSHevoLKpZqXWsyuHa4wP2U4ik8lsbGzYqYta6sfOqE62L1falbsg3aXmbejDVifb7L/WtGbrgCwtZndDjbpIX6aOQh5caXW+4XBY3wlWZVNHoaaneq41W+fY8XjcTl1k5cTdsEddpG6cLxQKGrNhxasuUml92TpQr9eDwWCj0bBQFznyqc8qnnXJj+pb81U29VnFsy7fmdqPpqamisWihbrgDfTd5L5ca+OO4+jKpsLVBtDlF33ZOrO6uirnIxt1ETy7IvX9JeilOUD9DnMEAgELLwGGhobw2wCry/pF2TI0ZsOzAXT5VV+2rkxOTm5vb9umy/h462LIsy7fRhKJhMZs6mLIsy4/6MvWFemy5+bmbNMllUrh3qh3Xb56N5/Pa8yG+wPo8rW+bF2RM3wkErFNl62tLXz8lmddZAGoVCoas+Fzz7pozdYLw8PDe3t7VunSaDRkEuD+B150WYzIDq09G1xPujzQmq0XZCsU463SRdjc3MREoI/7xXY9uiLv0nVd7dlwvZ/7xXY99SFbV3K5nJG7Ru030rOzs7j5Tl+f/5PJpAyHDw+vsuFOX5//fcvWmWw2a+SfDNp1qdfrsnQrY3pZY9zY9PT0zMyMP98VDrMpY3pZY2p+ZutMOp1eX1+3UJf2W5F5HI/Hu/QxK2PSQsq88fN9vMjWpY8p+p+tc+NlpNf277+60ivIQ6o/Tsvp+uHlwy94bkzdK90Nyyx3HKdUKhl5AS+yqdN19egLXq11r7RiNts/kW3I1L00fJ4W0s+nUimZqa3vqggGg9FoNJPJlMtlsxP3dc72Mvv7+6FQqFqt2q8L+beMMfXT1IVQF0JdCHUh1IVQF0KoC6EuhLoQ6kKoC6EuhFAXQl0IdSHUhVAXQl0IoS6EuhDqQqgLoS6EuhBCXQh1IdSFUBfyX+IvRZf444HWjGUAAAAASUVORK5CYII=, id=could not convert]]
		SequenceInput si2 = new SequenceInput();
		si2.setId(GlycanProcedure.CouldNotConvert);
		si2.setSequenceInput(sequence2Result);
		si2.setResultSequence("Failed Conversion:org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitorException: Error in GlycoCT validation:>Carbonyl acid contained in the ring. It must be use a substituent \"lactone\". :x-dglc-HEX-1:5|1:a<");
		si2.setImage(null);

		mockMvc.perform(
			post("/Registries/confirmation").with(csrf()).with(user("test")).contentType(
					MediaType.APPLICATION_FORM_URLENCODED).param(
					"sequence", sequence+"\\n"+sequence2))
			.andExpect(status().isOk())
			.andExpect(view().name("register/confirmation"))
			.andExpect(request().attribute("listRegistered", contains(si)))
			.andExpect(request().attribute("listErrors", contains(si2)));
	}
	
//	@Test
//  @Transactional
	public void testRegisterNewAndG00031MO() throws Exception {
	  // expected to fail as have erroneous glycoct with <cr> in end.
		String id = "G00031MO";
		String sequence = "RES\r\n" + 
	"1b:a-dgal-HEX-1:5\r\n" + 
				"2s:n-acetyl\r\n" + 
				"3b:b-dgal-HEX-1:5\r\n" + 
				"LIN\r\n" + 
				"1:1d(2+1)2n\r\n" + 
				"2:1o(3+1)3d";
		
		String id2 = "";
		String sequence2 = "RES\r\n"
				+ "1b:x-dman-HEX-1:5\r\n"
				+ "2b:x-dgal-HEX-1:5\r\n"
				+ "3s:n-acetyl\r\n"
				+ "LIN\r\n"
				+ "1:1o(-1+1)2d\r\n"
				+ "2:2d(2+1)3n";
		
		logger.debug("sequence:>" + sequence + "<");
		logger.debug("sequence2:>" + sequence2 + "<");
		// convert to how they will be stored
		String sequenceResult = sequence.replaceAll("(?:\r\n|\n)", "\\\\n").trim();
		String sequence2Result = sequence2.replaceAll("(?:\r\n|\n)", "\\\\n").trim();

		SequenceInput si = new SequenceInput();
		si.setId(id);
		si.setSequenceInput(sequenceResult);
		si.setResultSequence("WURCS=2.0/2,2,1/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1");
		si.setImage("/glycans/" + id + "/image?style=extended&format=png&notation=cfg");
		SequenceInput si2 = new SequenceInput();
//		SequenceInput [sequence=RES\n1b:x-dman-HEX-1:5\n2b:x-dgal-HEX-1:5\n3s:n-acetyl\nLIN\n1:1o(-1+1)2d\n2:2d(2+1)3n, resultSequence=, image=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALoAAABSCAIAAABse1lJAAADkUlEQVR42u3bv0sbYRjA8aeDrU3TNmArKUQMmLZRrpiWFDIU6pAhgy0OFjpkSCGDQxbhBimhKGRwUEghg4UOLh0EoVIyBBQOCoVCBgcHR/8EhwwZHNLnjLUdLJprc28av1/eWeXez7333g+lRXThhENAcCG4EFwILgQXgguHgOBCcCG4EFwILgQXDgHBheBCcCG4EFwILhwCggvBheBCcCG4EFw4BAQXggvBheBCXezo6KgPuci/yM9jUa/Xi8ViOp1u/2rLsjKZTKlU2t/f7x0ru7u7iUTClJjucjn++d6Hb1wcxxHrhkSuyZuwlGOyMSGfLfk0Lqtj8npYhgfUjc5TL6wr8Xh8ZWWlP1eX3ueiE2Dbtty7KktR+f7k7PHtsdgjMjRgcJ7a1Wo15dK3F6Me56LHXZ6HJHVLtif/aOV0fHkk44FCoWCQy8LCwuLiYn9udXufi869PLvtLh7nWmkPJ6FiDK4xuVxueXkZLga4VKtVd7OiAi5o5ecaEw6HTe1j8vn82toaXPzm4l6Gxq67O9mOrLSHPaI7XyOzpUuL2avhJeWie0b3VsiDleOdbyQSMXJ3rX92MpmEi99cdFWX+YhHLjpeDJVKJf9nq9FoBIPBZrMJF1+56O2o+1jFM5fiqKnr0dTU1NbWFlx85SJXpONN7u/jfcyyLCMTVi6X9f4ILmdz6WKerej4+FDMFQgEeAng6+oyODgoX/9idanc18uZqVM8lUrt7OzAxT8usdjxiyHPXN5F0+m0KS66y56bm4OLf1yy2ay8HfXO5dXdYrFoiovew0ejUbj4x2Vzc1Oe3vTMRRener1u8PlHKBQ6ODiAi09cms2mnqDy4YEXLktR3T20jKaXQhUPF//eGa2vr8t4oIP3i+2xPanOHMcxy8W2bSPvGi/1G+nZ2Vl5eaejx/+ZTEanqmW6QqFg5EuGS82l0WjoZcUVc5E1xklMT0/PzMwY/DrptFwuV6lU4OL313QqRteYZDJ5zj5mdUy3t3pO94KV9sbLyF6bb3VP9jE6Ae5H3Xp3vTFx8gTPSbjvleYjugJZllWtVlu9kV6GTL2X5j8Bfp2yeq+RzWZ1FXGf+YoEg8F4PJ7P52u1Wi8sKu0ODw/D4fDe3l6/caHuiTH1q+FCcCG4EFwILgQXIrgQXAguBBeCC8GFCC4EF4ILwYXgQnAhggvBheBCcCG4EFyI4EJwIbgQXOh/6geJSrubc4F5CgAAAABJRU5ErkJggg==, id=null]
		si2.setId(null);
		si2.setSequenceInput(sequence2Result);
		si2.setResultSequence("WURCS=2.0/2,2,1/[a1122h-1x_1-5][a2112h-1x_1-5_2*NCC/3=O]/1-2/a?-b1");
		si2.setImage("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALoAAABSCAIAAABse1lJAAADkUlEQVR42u3bv0sbYRjA8aeDrU3TNmArKUQMmLZRrpiWFDIU6pAhgy0OFjpkSCGDQxbhBimhKGRwUEghg4UOLh0EoVIyBBQOCoVCBgcHR/8EhwwZHNLnjLUdLJprc28av1/eWeXez7333g+lRXThhENAcCG4EFwILgQXgguHgOBCcCG4EFwILgQXDgHBheBCcCG4EFwILhwCggvBheBCcCG4EFw4BAQXggvBheBCXezo6KgPuci/yM9jUa/Xi8ViOp1u/2rLsjKZTKlU2t/f7x0ru7u7iUTClJjucjn++d6Hb1wcxxHrhkSuyZuwlGOyMSGfLfk0Lqtj8npYhgfUjc5TL6wr8Xh8ZWWlP1eX3ueiE2Dbtty7KktR+f7k7PHtsdgjMjRgcJ7a1Wo15dK3F6Me56LHXZ6HJHVLtif/aOV0fHkk44FCoWCQy8LCwuLiYn9udXufi869PLvtLh7nWmkPJ6FiDK4xuVxueXkZLga4VKtVd7OiAi5o5ecaEw6HTe1j8vn82toaXPzm4l6Gxq67O9mOrLSHPaI7XyOzpUuL2avhJeWie0b3VsiDleOdbyQSMXJ3rX92MpmEi99cdFWX+YhHLjpeDJVKJf9nq9FoBIPBZrMJF1+56O2o+1jFM5fiqKnr0dTU1NbWFlx85SJXpONN7u/jfcyyLCMTVi6X9f4ILmdz6WKerej4+FDMFQgEeAng6+oyODgoX/9idanc18uZqVM8lUrt7OzAxT8usdjxiyHPXN5F0+m0KS66y56bm4OLf1yy2ay8HfXO5dXdYrFoiovew0ejUbj4x2Vzc1Oe3vTMRRener1u8PlHKBQ6ODiAi09cms2mnqDy4YEXLktR3T20jKaXQhUPF//eGa2vr8t4oIP3i+2xPanOHMcxy8W2bSPvGi/1G+nZ2Vl5eaejx/+ZTEanqmW6QqFg5EuGS82l0WjoZcUVc5E1xklMT0/PzMwY/DrptFwuV6lU4OL313QqRteYZDJ5zj5mdUy3t3pO94KV9sbLyF6bb3VP9jE6Ae5H3Xp3vTFx8gTPSbjvleYjugJZllWtVlu9kV6GTL2X5j8Bfp2yeq+RzWZ1FXGf+YoEg8F4PJ7P52u1Wi8sKu0ODw/D4fDe3l6/caHuiTH1q+FCcCG4EFwILgQXIrgQXAguBBeCC8GFCC4EF4ILwYXgQnAhggvBheBCcCG4EFyI4EJwIbgQXOh/6geJSrubc4F5CgAAAABJRU5ErkJggg==");

			mockMvc.perform(
					post("/Registries/confirmation").with(csrf()).with(user("test")).contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequence", sequence).param("sequence-2", sequence2))
					.andExpect(status().isOk())
					.andExpect(view().name("register/confirmation"))
					.andExpect(request().attribute("listRegistered", contains(si)))
					.andExpect(request().attribute("listNew", contains(si2)));
	}

//	@Test
//	@Transactional
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
		String sequence = "RES\r\n" + 
	"1b:a-dgal-HEX-1:5\r\n" + 
				"2s:n-acetyl\r\n" + 
				"3b:b-dgal-HEX-1:5\r\n" + 
				"LIN\r\n" + 
				"1:1d(2+1)2n\r\n" + 
				"2:1o(3+1)3d";
		
		String id2 = "";
		String sequence2 = "RES\r\n"
				+ "1b:x-dman-HEX-1:5\r\n"
				+ "2b:x-dgal-HEX-1:5\r\n"
				+ "3s:n-acetyl\r\n"
				+ "LIN\r\n"
				+ "1:1o(-1+1)2d\r\n"
				+ "2:2d(2+1)3n";
		
		String sequence3 = "RES\r\n"
				+ "1b:x-dglc-HEX-1:5|1:a\r\n"
				+ "2b:b-dgal-HEX-1:5\r\n"
				+ "LIN\r\n"
				+ "1:1o(4+1)2d";

		
		logger.debug("sequence:>" + sequence + "<");
		logger.debug("sequence2:>" + sequence2 + "<");
		logger.debug("sequence3:>" + sequence3 + "<");
		
    String sequenceResult = sequence.replaceAll("(?:\r\n|\n)", "\\\\n").trim();
    String sequence2Result = sequence2.replaceAll("(?:\r\n|\n)", "\\\\n").trim();
    String sequence3Result = sequence3.replaceAll("(?:\r\n|\n)", "\\\\n").trim();

    logger.debug("sequenceResult:>" + sequenceResult + "<");
    logger.debug("sequence2Result:>" + sequence2Result + "<");
    logger.debug("sequence3Result:>" + sequence3Result + "<");
    

		SequenceInput si = new SequenceInput();
		si.setId(id);
		si.setSequenceInput(sequenceResult);
		si.setResultSequence("WURCS=2.0/2,2,1/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1");
		si.setImage("/glycans/" + id + "/image?style=extended&format=png&notation=cfg");
		SequenceInput si2 = new SequenceInput();
		si2.setId(null);
		si2.setSequenceInput(sequence2Result);
		si2.setResultSequence("WURCS=2.0/2,2,1/[a1122h-1x_1-5][a2112h-1x_1-5_2*NCC/3=O]/1-2/a?-b1");
		si2.setImage("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALoAAABSCAIAAABse1lJAAADkUlEQVR42u3bv0sbYRjA8aeDrU3TNmArKUQMmLZRrpiWFDIU6pAhgy0OFjpkSCGDQxbhBimhKGRwUEghg4UOLh0EoVIyBBQOCoVCBgcHR/8EhwwZHNLnjLUdLJprc28av1/eWeXez7333g+lRXThhENAcCG4EFwILgQXgguHgOBCcCG4EFwILgQXDgHBheBCcCG4EFwILhwCggvBheBCcCG4EFw4BAQXggvBheBCXezo6KgPuci/yM9jUa/Xi8ViOp1u/2rLsjKZTKlU2t/f7x0ru7u7iUTClJjucjn++d6Hb1wcxxHrhkSuyZuwlGOyMSGfLfk0Lqtj8npYhgfUjc5TL6wr8Xh8ZWWlP1eX3ueiE2Dbtty7KktR+f7k7PHtsdgjMjRgcJ7a1Wo15dK3F6Me56LHXZ6HJHVLtif/aOV0fHkk44FCoWCQy8LCwuLiYn9udXufi869PLvtLh7nWmkPJ6FiDK4xuVxueXkZLga4VKtVd7OiAi5o5ecaEw6HTe1j8vn82toaXPzm4l6Gxq67O9mOrLSHPaI7XyOzpUuL2avhJeWie0b3VsiDleOdbyQSMXJ3rX92MpmEi99cdFWX+YhHLjpeDJVKJf9nq9FoBIPBZrMJF1+56O2o+1jFM5fiqKnr0dTU1NbWFlx85SJXpONN7u/jfcyyLCMTVi6X9f4ILmdz6WKerej4+FDMFQgEeAng6+oyODgoX/9idanc18uZqVM8lUrt7OzAxT8usdjxiyHPXN5F0+m0KS66y56bm4OLf1yy2ay8HfXO5dXdYrFoiovew0ejUbj4x2Vzc1Oe3vTMRRener1u8PlHKBQ6ODiAi09cms2mnqDy4YEXLktR3T20jKaXQhUPF//eGa2vr8t4oIP3i+2xPanOHMcxy8W2bSPvGi/1G+nZ2Vl5eaejx/+ZTEanqmW6QqFg5EuGS82l0WjoZcUVc5E1xklMT0/PzMwY/DrptFwuV6lU4OL313QqRteYZDJ5zj5mdUy3t3pO94KV9sbLyF6bb3VP9jE6Ae5H3Xp3vTFx8gTPSbjvleYjugJZllWtVlu9kV6GTL2X5j8Bfp2yeq+RzWZ1FXGf+YoEg8F4PJ7P52u1Wi8sKu0ODw/D4fDe3l6/caHuiTH1q+FCcCG4EFwILgQXIrgQXAguBBeCC8GFCC4EF4ILwYXgQnAhggvBheBCcCG4EFyI4EJwIbgQXOh/6geJSrubc4F5CgAAAABJRU5ErkJggg==");
		SequenceInput si3 = new SequenceInput();
		si3.setId(GlycanProcedure.CouldNotConvert);
		si3.setSequenceInput(sequence3Result);
		si3.setResultSequence("Failed Conversion:org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitorException: Error in GlycoCT validation:>Carbonyl acid contained in the ring. It must be use a substituent \"lactone\". :x-dglc-HEX-1:5|1:a<");
		si3.setImage(null);

			mockMvc.perform(
					post("/Registries/confirmation").with(csrf()).with(user("test")).contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequence", sequence+"\\n"+sequence2+"\\n"+sequence3))
					.andExpect(status().isOk())
					.andExpect(view().name("register/confirmation"))
					.andExpect(request().attribute("listRegistered", contains(si)))
					.andExpect(request().attribute("listNew", contains(si2)))
					.andExpect(request().attribute("listErrors", contains(si3)));
	}

//	@Test
//  @Transactional
	public void testRegisterNew() throws Exception {
	String sequence = "RES\r\n"
				+ "1b:x-dgal-HEX-1:5\r\n"
				+ "2b:x-dman-HEX-1:5\r\n"
				+ "3s:n-acetyl\r\n"
				+ "LIN\r\n"
				+ "1:1o(-1+1)2d\r\n"
				+ "2:1d(2+1)3n\r\n";
	  String sequenceResult = sequence.replaceAll("(?:\r\n|\n)", "\\\\n").trim();

		logger.debug("sequence:>" + sequence + "<");
		SequenceInput si = new SequenceInput();
		si.setId(null);
		si.setSequenceInput(sequenceResult);
		si.setImage("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALoAAABSCAIAAABse1lJAAADaklEQVR42u3bv2sTYRjA8cehWmv9Aa3lhJQGGjUtJ41SIYPgkiFDlQ4VHDJEyJilcEORQ1rI0KEFhSyCQxeHQkGQDIEOB4IgZOjQoX9Ghw4dOuhziRaR2jbXvnfc3ffLO7mEe/vJe+97OeUn0bkTpoDgQnAhuBBcCC4EF6aA4EJwIbgQXAguBBemgOBCcCG4EFwILgQXpoDgQnAhuBBcCC4EF6aA4EJwIbgQXMhgR0dHaeHS6XRc1y2VStLNtu1yudxoNPb29kK97Msokj/Yzs5OoVCISkx41+x5ntg3JHNN3ljyPieb0/LFls9Tsj4pr8dkbEDd6FyExqV77cFHJFxUST6fX1tbS/LqohfpOI7cuyorWfnx5OTx/bE44zIyEM5cxJRLu91WLkm+Gem1yfM7Urwl2zP/tXI8vj6SqaF6vQ6XE1taWlpeXk7yVlf/9vLstr94nGmlN7yCijG9xsSUS7VaXV1dTSyXVqvlb1ZUwDmt/FljLMsyuo+JKZckH6T929DkdX8n25eV3nDGdecLl9MPdIniovsy/ygUwEp355vJZMydruPI5R8ikYgx+Hm1Wk0WMwG56Hgx0mg04HIKjvDFGPwwPfL5j1UCc3EnzN2P4silr3+PHxe5In1vcv8eH3K2bcMlNVy0wFZ0fHooJrs4l9BKBZfBwUH5doHVpXlfb2esLmlZXXK57g9Dgbm8y5ZKJbikZatbqVTk7URwLq/uuq4Ll7QcpLe2tuTpzcBcdHHqdDpwSctjusPDw2w2Kx8fBOGyki0Wi5c1uYnhcpzepvXbmCgu2sbGhkwN9fH7Ym9sz6gzz/MM7xxjzMVxnEh+azR+zQsLC/JytK/H/+VyWafD/EEjxlzq9XokbzIYv+aDgwO9rfhizrPGeIW5ubn5+XnTbwDFnUu1Wm02mwnk0hOja8zs7OwZ+5j1Sd3e6vcmhLfFYs2ltyk0dw6ImMvxPkYv0n+pW0/Xm9O/n+B5Bf93pcWMrkC2bbdarRCfesWVi96G9LsXyUdLyF8L3c9XKhVdRfxnviLDw8P5fL5Wq7Xb7TBfQY3v/wTY39+3LGt3dzf5XOiyxET10XAhuBBcCC4EF4ILEVwILgQXggvBheBCBBeCC8GF4EJwIbgQwYXgQnAhuBBcCC5EcCG4EFwILhSnfgERYn4/0mT9nQAAAABJRU5ErkJggg==");
		si.setResultSequence("WURCS=2.0/2,2,1/[a2112h-1x_1-5_2*NCC/3=O][a1122h-1x_1-5]/1-2/a?-b1");
		mockMvc.perform(
					post("/Registries/confirmation").with(csrf()).with(user("test")).contentType(
							MediaType.APPLICATION_FORM_URLENCODED).param(
							"sequence", sequence))
					.andExpect(status().isOk())
					.andExpect(view().name("register/confirmation"))
					.andExpect(request().attribute("listNew", contains(si)));
	}
	

//	@Test
//	@Transactional
	public void testRegisterNew2() throws Exception {
		String sequence = "RES\\n" + 
		    "1b:x-dgal-HEX-1:5\\n" + 
		    "2b:x-dman-HEX-1:5\\n" + 
		    "3b:x-dgal-HEX-1:5\\n" + 
		    "4b:x-dglc-HEX-1:5\\n" + 
		    "5b:x-dgal-HEX-1:5\\n" + 
		    "6s:n-acetyl\\n" + 
		    "7s:n-acetyl\\n" + 
		    "8b:x-lgal-HEX-1:5|6:d\\n" + 
		    "9b:x-llyx-PEN-1:5\\n" + 
		    "10b:x-dgal-HEX-1:5\\n" + 
		    "11b:x-llyx-PEN-1:5\\n" + 
		    "12b:x-dgro-dgal-NON-2:6|1:a|2:keto|3:d\\n" + 
		    "13b:x-dgro-dgal-NON-2:6|1:a|2:keto|3:d\\n" + 
		    "14s:n-acetyl\\n" + 
		    "15s:n-glycolyl\\n" + 
		    "16s:n-acetyl\\n" + 
		    "LIN\\n" + 
		    "1:1o(-1+1)2d\\n" + 
		    "2:2o(-1+1)3d\\n" + 
		    "3:3o(-1+1)4d\\n" + 
		    "4:4o(-1+1)5d\\n" + 
		    "5:5d(2+1)6n\\n" + 
		    "6:4d(2+1)7n\\n" + 
		    "7:3o(-1+1)8d\\n" + 
		    "8:8o(-1+1)9d\\n" + 
		    "9:9o(-1+1)10d\\n" + 
		    "10:10o(-1+1)11d\\n" + 
		    "11:8o(-1+2)12d\\n" + 
		    "12:12o(-1+2)13d\\n" + 
		    "13:13d(5+1)14n\\n" + 
		    "14:12d(5+1)15n\\n" + 
		    "15:1d(2+1)16n";

		logger.debug("sequence:>" + sequence + "<");
		SequenceInput si = new SequenceInput();
		UserInfo userinfo = new UserInfo("testid", "testname", "Administrator", "", "", "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg", null, "glytoucan@gmail.com", "true");
		String resultSequence = "WURCS=2.0/8,11,10/[a2112h-1x_1-5_2*NCC/3=O][a1122h-1x_1-5][a2112h-1x_1-5][a1221m-1x_1-5][a221h-1x_1-5][Aad21122h-2x_2-6_5*NCCO/3=O][Aad21122h-2x_2-6_5*NCC/3=O][a2122h-1x_1-5_2*NCC/3=O]/1-2-3-4-5-3-5-6-7-8-1/a?-b1_b?-c1_c?-d1_c?-j1_d?-e1_d?-h2_e?-f1_f?-g1_h?-i2_j?-k1";
		String image = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALoAAABSCAIAAABse1lJAAADkUlEQVR42u3bv0sbYRjA8aeDrU3TNmArKUQMmLZRrpiWFDIU6pAhgy0OFjpkSCGDQxbhBimhKGRwUEghg4UOLh0EoVIyBBQOCoVCBgcHR/8EhwwZHNLnjLUdLJprc28av1/eWeXez7333g+lRXThhENAcCG4EFwILgQXgguHgOBCcCG4EFwILgQXDgHBheBCcCG4EFwILhwCggvBheBCcCG4EFw4BAQXggvBheBCXezo6KgPuci/yM9jUa/Xi8ViOp1u/2rLsjKZTKlU2t/f7x0ru7u7iUTClJjucjn++d6Hb1wcxxHrhkSuyZuwlGOyMSGfLfk0Lqtj8npYhgfUjc5TL6wr8Xh8ZWWlP1eX3ueiE2Dbtty7KktR+f7k7PHtsdgjMjRgcJ7a1Wo15dK3F6Me56LHXZ6HJHVLtif/aOV0fHkk44FCoWCQy8LCwuLiYn9udXufi869PLvtLh7nWmkPJ6FiDK4xuVxueXkZLga4VKtVd7OiAi5o5ecaEw6HTe1j8vn82toaXPzm4l6Gxq67O9mOrLSHPaI7XyOzpUuL2avhJeWie0b3VsiDleOdbyQSMXJ3rX92MpmEi99cdFWX+YhHLjpeDJVKJf9nq9FoBIPBZrMJF1+56O2o+1jFM5fiqKnr0dTU1NbWFlx85SJXpONN7u/jfcyyLCMTVi6X9f4ILmdz6WKerej4+FDMFQgEeAng6+oyODgoX/9idanc18uZqVM8lUrt7OzAxT8usdjxiyHPXN5F0+m0KS66y56bm4OLf1yy2ay8HfXO5dXdYrFoiovew0ejUbj4x2Vzc1Oe3vTMRRener1u8PlHKBQ6ODiAi09cms2mnqDy4YEXLktR3T20jKaXQhUPF//eGa2vr8t4oIP3i+2xPanOHMcxy8W2bSPvGi/1G+nZ2Vl5eaejx/+ZTEanqmW6QqFg5EuGS82l0WjoZcUVc5E1xklMT0/PzMwY/DrptFwuV6lU4OL313QqRteYZDJ5zj5mdUy3t3pO94KV9sbLyF6bb3VP9jE6Ae5H3Xp3vTFx8gTPSbjvleYjugJZllWtVlu9kV6GTL2X5j8Bfp2yeq+RzWZ1FXGf+YoEg8F4PJ7P52u1Wi8sKu0ODw/D4fDe3l6/caHuiTH1q+FCcCG4EFwILgQXIrgQXAguBBeCC8GFCC4EF4ILwYXgQnAhggvBheBCcCG4EFyI4EJwIbgQXOh/6geJSrubc4F5CgAAAABJRU5ErkJggg==";
		
		mockMvc.perform(
					post("/Registries/complete").with(csrf()).with(user(userinfo)).contentType(
							MediaType.APPLICATION_FORM_URLENCODED)
							.param("checked", "on")
							.param("resultSequence", "WURCS=2.0/8,11,10/[a2112h-1x_1-5_2*NCC/3=O][a1122h-1x_1-5][a2112h-1x_1-5][a1221m-1x_1-5][a221h-1x_1-5][Aad21122h-2x_2-6_5*NCCO/3=O][Aad21122h-2x_2-6_5*NCC/3=O][a2122h-1x_1-5_2*NCC/3=O]/1-2-3-4-5-3-5-6-7-8-1/a?-b1_b?-c1_c?-d1_c?-j1_d?-e1_d?-h2_e?-f1_f?-g1_h?-i2_j?-k1")
							.param("sequence", sequence)
							.param("image", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALoAAABSCAIAAABse1lJAAADkUlEQVR42u3bv0sbYRjA8aeDrU3TNmArKUQMmLZRrpiWFDIU6pAhgy0OFjpkSCGDQxbhBimhKGRwUEghg4UOLh0EoVIyBBQOCoVCBgcHR/8EhwwZHNLnjLUdLJprc28av1/eWeXez7333g+lRXThhENAcCG4EFwILgQXgguHgOBCcCG4EFwILgQXDgHBheBCcCG4EFwILhwCggvBheBCcCG4EFw4BAQXggvBheBCXezo6KgPuci/yM9jUa/Xi8ViOp1u/2rLsjKZTKlU2t/f7x0ru7u7iUTClJjucjn++d6Hb1wcxxHrhkSuyZuwlGOyMSGfLfk0Lqtj8npYhgfUjc5TL6wr8Xh8ZWWlP1eX3ueiE2Dbtty7KktR+f7k7PHtsdgjMjRgcJ7a1Wo15dK3F6Me56LHXZ6HJHVLtif/aOV0fHkk44FCoWCQy8LCwuLiYn9udXufi869PLvtLh7nWmkPJ6FiDK4xuVxueXkZLga4VKtVd7OiAi5o5ecaEw6HTe1j8vn82toaXPzm4l6Gxq67O9mOrLSHPaI7XyOzpUuL2avhJeWie0b3VsiDleOdbyQSMXJ3rX92MpmEi99cdFWX+YhHLjpeDJVKJf9nq9FoBIPBZrMJF1+56O2o+1jFM5fiqKnr0dTU1NbWFlx85SJXpONN7u/jfcyyLCMTVi6X9f4ILmdz6WKerej4+FDMFQgEeAng6+oyODgoX/9idanc18uZqVM8lUrt7OzAxT8usdjxiyHPXN5F0+m0KS66y56bm4OLf1yy2ay8HfXO5dXdYrFoiovew0ejUbj4x2Vzc1Oe3vTMRRener1u8PlHKBQ6ODiAi09cms2mnqDy4YEXLktR3T20jKaXQhUPF//eGa2vr8t4oIP3i+2xPanOHMcxy8W2bSPvGi/1G+nZ2Vl5eaejx/+ZTEanqmW6QqFg5EuGS82l0WjoZcUVc5E1xklMT0/PzMwY/DrptFwuV6lU4OL313QqRteYZDJ5zj5mdUy3t3pO94KV9sbLyF6bb3VP9jE6Ae5H3Xp3vTFx8gTPSbjvleYjugJZllWtVlu9kV6GTL2X5j8Bfp2yeq+RzWZ1FXGf+YoEg8F4PJ7P52u1Wi8sKu0ODw/D4fDe3l6/caHuiTH1q+FCcCG4EFwILgQXIrgQXAguBBeCC8GFCC4EF4ILwYXgQnAhggvBheBCcCG4EFyI4EJwIbgQXOh/6geJSrubc4F5CgAAAABJRU5ErkJggg==")
							)
					.andExpect(status().isOk())
					.andExpect(view().name("register/complete"))
					.andExpect(request().attribute("origList", contains(sequence)))
					.andExpect(request().attribute("registeredList", contains(resultSequence)))
          .andExpect(request().attribute("imageList", contains(image)))
          .andExpect(request().attribute("resultList", notNullValue()))
					;
	}
	
	 @Test
	  @Transactional
	  public void testRegisterNewUser() throws Exception {
	    String sequence = "RES\\n" + 
	        "1b:x-dgal-HEX-1:5\\n" + 
	        "2b:x-dman-HEX-1:5\\n" + 
	        "3b:x-dgal-HEX-1:5\\n" + 
	        "4b:x-dglc-HEX-1:5\\n" + 
	        "5b:x-dgal-HEX-1:5\\n" + 
	        "6s:n-acetyl\\n" + 
	        "7s:n-acetyl\\n" + 
	        "8b:x-lgal-HEX-1:5|6:d\\n" + 
	        "9b:x-llyx-PEN-1:5\\n" + 
	        "10b:x-dgal-HEX-1:5\\n" + 
	        "11b:x-llyx-PEN-1:5\\n" + 
	        "12b:x-dgro-dgal-NON-2:6|1:a|2:keto|3:d\\n" + 
	        "13b:x-dgro-dgal-NON-2:6|1:a|2:keto|3:d\\n" + 
	        "14s:n-acetyl\\n" + 
	        "15s:n-glycolyl\\n" + 
	        "16s:n-acetyl\\n" + 
	        "LIN\\n" + 
	        "1:1o(-1+1)2d\\n" + 
	        "2:2o(-1+1)3d\\n" + 
	        "3:3o(-1+1)4d\\n" + 
	        "4:4o(-1+1)5d\\n" + 
	        "5:5d(2+1)6n\\n" + 
	        "6:4d(2+1)7n\\n" + 
	        "7:3o(-1+1)8d\\n" + 
	        "8:8o(-1+1)9d\\n" + 
	        "9:9o(-1+1)10d\\n" + 
	        "10:10o(-1+1)11d\\n" + 
	        "11:8o(-1+2)12d\\n" + 
	        "12:12o(-1+2)13d\\n" + 
	        "13:13d(5+1)14n\\n" + 
	        "14:12d(5+1)15n\\n" + 
	        "15:1d(2+1)16n";

	    logger.debug("sequence:>" + sequence + "<");
	    SequenceInput si = new SequenceInput();
	    UserInfo userinfo = new UserInfo("testid", "testname", "Johnny", "", "", "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg", null, "aokinobu@gmail.com", "true");
	    String resultSequence = "WURCS=2.0/8,11,10/[a2112h-1x_1-5_2*NCC/3=O][a1122h-1x_1-5][a2112h-1x_1-5][a1221m-1x_1-5][a221h-1x_1-5][Aad21122h-2x_2-6_5*NCCO/3=O][Aad21122h-2x_2-6_5*NCC/3=O][a2122h-1x_1-5_2*NCC/3=O]/1-2-3-4-5-3-5-6-7-8-1/a?-b1_b?-c1_c?-d1_c?-j1_d?-e1_d?-h2_e?-f1_f?-g1_h?-i2_j?-k1";
	    String image = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALoAAABSCAIAAABse1lJAAADkUlEQVR42u3bv0sbYRjA8aeDrU3TNmArKUQMmLZRrpiWFDIU6pAhgy0OFjpkSCGDQxbhBimhKGRwUEghg4UOLh0EoVIyBBQOCoVCBgcHR/8EhwwZHNLnjLUdLJprc28av1/eWeXez7333g+lRXThhENAcCG4EFwILgQXgguHgOBCcCG4EFwILgQXDgHBheBCcCG4EFwILhwCggvBheBCcCG4EFw4BAQXggvBheBCXezo6KgPuci/yM9jUa/Xi8ViOp1u/2rLsjKZTKlU2t/f7x0ru7u7iUTClJjucjn++d6Hb1wcxxHrhkSuyZuwlGOyMSGfLfk0Lqtj8npYhgfUjc5TL6wr8Xh8ZWWlP1eX3ueiE2Dbtty7KktR+f7k7PHtsdgjMjRgcJ7a1Wo15dK3F6Me56LHXZ6HJHVLtif/aOV0fHkk44FCoWCQy8LCwuLiYn9udXufi869PLvtLh7nWmkPJ6FiDK4xuVxueXkZLga4VKtVd7OiAi5o5ecaEw6HTe1j8vn82toaXPzm4l6Gxq67O9mOrLSHPaI7XyOzpUuL2avhJeWie0b3VsiDleOdbyQSMXJ3rX92MpmEi99cdFWX+YhHLjpeDJVKJf9nq9FoBIPBZrMJF1+56O2o+1jFM5fiqKnr0dTU1NbWFlx85SJXpONN7u/jfcyyLCMTVi6X9f4ILmdz6WKerej4+FDMFQgEeAng6+oyODgoX/9idanc18uZqVM8lUrt7OzAxT8usdjxiyHPXN5F0+m0KS66y56bm4OLf1yy2ay8HfXO5dXdYrFoiovew0ejUbj4x2Vzc1Oe3vTMRRener1u8PlHKBQ6ODiAi09cms2mnqDy4YEXLktR3T20jKaXQhUPF//eGa2vr8t4oIP3i+2xPanOHMcxy8W2bSPvGi/1G+nZ2Vl5eaejx/+ZTEanqmW6QqFg5EuGS82l0WjoZcUVc5E1xklMT0/PzMwY/DrptFwuV6lU4OL313QqRteYZDJ5zj5mdUy3t3pO94KV9sbLyF6bb3VP9jE6Ae5H3Xp3vTFx8gTPSbjvleYjugJZllWtVlu9kV6GTL2X5j8Bfp2yeq+RzWZ1FXGf+YoEg8F4PJ7P52u1Wi8sKu0ODw/D4fDe3l6/caHuiTH1q+FCcCG4EFwILgQXIrgQXAguBBeCC8GFCC4EF4ILwYXgQnAhggvBheBCcCG4EFyI4EJwIbgQXOh/6geJSrubc4F5CgAAAABJRU5ErkJggg==";
      
	    
//	    final ResponseEntity<UserInfo> userInfoResponseEntity = restTemplate.getForEntity("https://www.googleapis.com/oauth2/v2/userinfo", UserInfo.class);
//      
	    OAuth2AccessToken token = new DefaultOAuth2AccessToken(tokenValue);

	    
      Authentication auth = new PreAuthenticatedAuthenticationToken(userinfo, token, NO_AUTHORITIES);
      
      SecurityContextHolder.getContext().setAuthentication(auth);
      
//      OAuth2AccessToken token = (OAuth2AccessToken)SecurityContextHolder.getContext().getAuthentication().getCredentials();
      
//      logger.debug("token:>" + token + "<");
      
	    mockMvc.perform(
	          post("/Registries/complete").with(csrf()).with(user(userinfo)).with(authentication(auth)).contentType(
	              MediaType.APPLICATION_FORM_URLENCODED)
	              .param("checked", "on")
	              .param("resultSequence", "WURCS=2.0/8,11,10/[a2112h-1x_1-5_2*NCC/3=O][a1122h-1x_1-5][a2112h-1x_1-5][a1221m-1x_1-5][a221h-1x_1-5][Aad21122h-2x_2-6_5*NCCO/3=O][Aad21122h-2x_2-6_5*NCC/3=O][a2122h-1x_1-5_2*NCC/3=O]/1-2-3-4-5-3-5-6-7-8-1/a?-b1_b?-c1_c?-d1_c?-j1_d?-e1_d?-h2_e?-f1_f?-g1_h?-i2_j?-k1")
	              .param("sequenceInput", sequence)
	              .param("image", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALoAAABSCAIAAABse1lJAAADkUlEQVR42u3bv0sbYRjA8aeDrU3TNmArKUQMmLZRrpiWFDIU6pAhgy0OFjpkSCGDQxbhBimhKGRwUEghg4UOLh0EoVIyBBQOCoVCBgcHR/8EhwwZHNLnjLUdLJprc28av1/eWeXez7333g+lRXThhENAcCG4EFwILgQXgguHgOBCcCG4EFwILgQXDgHBheBCcCG4EFwILhwCggvBheBCcCG4EFw4BAQXggvBheBCXezo6KgPuci/yM9jUa/Xi8ViOp1u/2rLsjKZTKlU2t/f7x0ru7u7iUTClJjucjn++d6Hb1wcxxHrhkSuyZuwlGOyMSGfLfk0Lqtj8npYhgfUjc5TL6wr8Xh8ZWWlP1eX3ueiE2Dbtty7KktR+f7k7PHtsdgjMjRgcJ7a1Wo15dK3F6Me56LHXZ6HJHVLtif/aOV0fHkk44FCoWCQy8LCwuLiYn9udXufi869PLvtLh7nWmkPJ6FiDK4xuVxueXkZLga4VKtVd7OiAi5o5ecaEw6HTe1j8vn82toaXPzm4l6Gxq67O9mOrLSHPaI7XyOzpUuL2avhJeWie0b3VsiDleOdbyQSMXJ3rX92MpmEi99cdFWX+YhHLjpeDJVKJf9nq9FoBIPBZrMJF1+56O2o+1jFM5fiqKnr0dTU1NbWFlx85SJXpONN7u/jfcyyLCMTVi6X9f4ILmdz6WKerej4+FDMFQgEeAng6+oyODgoX/9idanc18uZqVM8lUrt7OzAxT8usdjxiyHPXN5F0+m0KS66y56bm4OLf1yy2ay8HfXO5dXdYrFoiovew0ejUbj4x2Vzc1Oe3vTMRRener1u8PlHKBQ6ODiAi09cms2mnqDy4YEXLktR3T20jKaXQhUPF//eGa2vr8t4oIP3i+2xPanOHMcxy8W2bSPvGi/1G+nZ2Vl5eaejx/+ZTEanqmW6QqFg5EuGS82l0WjoZcUVc5E1xklMT0/PzMwY/DrptFwuV6lU4OL313QqRteYZDJ5zj5mdUy3t3pO94KV9sbLyF6bb3VP9jE6Ae5H3Xp3vTFx8gTPSbjvleYjugJZllWtVlu9kV6GTL2X5j8Bfp2yeq+RzWZ1FXGf+YoEg8F4PJ7P52u1Wi8sKu0ODw/D4fDe3l6/caHuiTH1q+FCcCG4EFwILgQXIrgQXAguBBeCC8GFCC4EF4ILwYXgQnAhggvBheBCcCG4EFyI4EJwIbgQXOh/6geJSrubc4F5CgAAAABJRU5ErkJggg==")
	              )
	          .andExpect(status().isOk())
	          .andExpect(view().name("register/complete"))
	          .andExpect(request().attribute("origList", contains(sequence)))
	          .andExpect(request().attribute("registeredList", contains(resultSequence)))
	          .andExpect(request().attribute("imageList", contains(image)))
	          .andExpect(request().attribute("resultList", notNullValue()))
	          ;
	  }
   
   public OAuth2ProtectedResourceDetails googleOAuth2Details() {
     AuthorizationCodeResourceDetails googleOAuth2Details = new AuthorizationCodeResourceDetails();
     googleOAuth2Details.setAuthenticationScheme(form);
     googleOAuth2Details.setClientAuthenticationScheme(form);
     googleOAuth2Details.setClientId(clientId);
     googleOAuth2Details.setClientSecret(clientSecret);
     googleOAuth2Details.setUserAuthorizationUri("https://accounts.google.com/o/oauth2/auth");
     googleOAuth2Details.setAccessTokenUri("https://www.googleapis.com/oauth2/v3/token");
     googleOAuth2Details.setScope(asList("email"));
     return googleOAuth2Details;
 }
	 
	 @Test
   public void testRegistriesSupplementLitStart() throws Exception {
     mockMvc.perform(get("/Registries/supplement/G22768VO").with(csrf()).with(user("test")))
         .andExpect(status().isOk())
         .andExpect(view().name("register/literature/entry"));
   }
   
	  @Test
	  @Transactional
	  public void testRegisterSupplementG00031MO() throws Exception {
	      mockMvc.perform(
	          post("/Registries/supplement/G00031MO/confirmation").with(csrf()).with(user("test")).contentType(
	              MediaType.APPLICATION_FORM_URLENCODED)
	          .param("accessionNumber", "G00031MO")
            .param("literatureId", "7503987"))
	      .andExpect(status().isOk())
	      .andExpect(view().name("register/literature/confirmation"))
	      .andExpect(request().attribute("accNum", is("G00031MO")))
	      .andExpect(model().attribute("literatureTitle", is("Binding of the O-antigen of Shigella dysenteriae type 1 and 26 related synthetic fragments to a monoclonal IgM antibody."
)))
	      .andExpect(model().attribute("literatureId", is("7503987")));
	  }
	  
	   @Test
	    @Transactional
	    public void testRegisterSupplementEmpty() throws Exception {
	        mockMvc.perform(
	            post("/Registries/supplement/G00031MO/confirmation").with(csrf()).with(user("test")).contentType(
	                MediaType.APPLICATION_FORM_URLENCODED)
	            .param("accessionNumber", "G00031MO")
	            .param("literatureId", "1234a"))
	        .andExpect(status().isOk())
	        .andExpect(view().name("register/literature/entry"))
	        .andExpect(request().attribute("accNum", is("G00031MO")));
	    }
	  
	   @Test
	    @Transactional
	    public void testRegisterSupplementG00031MOComplete() throws Exception {
	     
	     Captcha captcha = new Captcha.Builder(200, 50)
	          .addText()
	          .addBackground(new GradiatedBackgroundProducer())
	            .gimp()
	            .addNoise()
	            .addBorder()
	            .build();
	     
	     mockHttpSession.putValue(NAME, captcha);
	     assertThat(mockHttpSession.getAttribute(NAME), is(notNullValue()));

	        mockMvc.perform(
	            post("/Registries/supplement/G00031MO/complete").session(mockHttpSession).with(csrf()).with(user("test")).contentType(
	                MediaType.APPLICATION_FORM_URLENCODED)
              .param("captcha", captcha.getAnswer())
	            .param("accessionNumber", "G00031MO")
	            .param("literatureId", "7503987")
	            .param("literatureTitle", "Binding of the O-antigen of Shigella dysenteriae type 1 and 26 related synthetic fragments to a monoclonal IgM antibody."))
	        .andExpect(status().isOk())
	        .andExpect(view().name("register/literature/entry"));
	    }

	   @Test
	    @Transactional
	    public void testRegisterNewDirect() {
	     MockHttpServletRequest request = new MockHttpServletRequest();
	     MockHttpServletResponse response = new MockHttpServletResponse();
	     UserInfo userinfo = new UserInfo("123", "Administrator", "Administrator", "Toucan", "",
	         "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg", null,
	         "aokinobu@gmail.com", "true");
	     OAuth2AccessToken token = new DefaultOAuth2AccessToken(tokenValue);

	     logger.debug("Token:>" + token.getValue());

	     Authentication auth = new PreAuthenticatedAuthenticationToken(userinfo, token, NO_AUTHORITIES);
	     
	     SecurityContextHolder.getContext().setAuthentication(auth);;
	     
	     String[] values = { "on", "on " };
	     request.setParameter("checked", values);
	     logger.debug(Arrays.asList(values));
       String[] resultSequences = { "", "on " };
	     request.setParameter("resultSequence", resultSequences);
       String[] sequences = { "", "on " };
	     request.setParameter("sequenceInput", sequences);
       String[] images = { "", "on " };
	     request.setParameter("image", images);
	     
	     RedirectAttributes redirectAttrs = new RedirectAttributesModelMap();
	     
	     
	     registriesController.complete(request, redirectAttrs);
	     

	   }
	   
}

