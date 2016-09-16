package org.glytoucan.web.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.security.core.authority.AuthorityUtils.NO_AUTHORITIES;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.glytoucan.admin.model.User;
import org.glytoucan.web.Application;
import org.glytoucan.web.controller.UserController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.github.fromi.openidconnect.security.UserInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
@WebAppConfiguration
public class UserControllerTest {
  public static Logger logger = (Logger) LoggerFactory.getLogger("org.glytoucan.ws.controller.D3ControllerTest");

  @Autowired
  UserController userC;
  
  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;
  
  String tokenValue = "ya29.CjFgA9_SMiK9tAy2uoM4AD6d8b5YKACXZm8juN0PvygvSbN__oqsFGFMMVZugeyKiJ2P";

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
  }

  @Test
  public void testUser() throws Exception {
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken("test", "testpw"));
    
    ExtendedModelMap map = new ExtendedModelMap();
    RedirectAttributesModelMap redMap = new RedirectAttributesModelMap();
    String results = userC.profile(map, redMap);
    logger.debug(results);
  }

  @Test
  @Transactional
  public void testGenerateHash() throws Exception {
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken("test", "testpw"));
    
    ExtendedModelMap map = new ExtendedModelMap();
    RedirectAttributesModelMap redMap = new RedirectAttributesModelMap();

    String results = userC.profile(map, redMap);
    logger.debug(results);
  }
   
  @Test
  public void testUserProfileStartNoToken() throws Exception {
    UserInfo userinfo = new UserInfo("123", "Administrator", "Administrator", "Toucan", "", "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg", null, "glytoucan@gmail.com", "true");
        OAuth2AccessToken token = new DefaultOAuth2AccessToken("");
    
        logger.debug("Token:>" + token.getValue());
      
      Authentication auth = new  PreAuthenticatedAuthenticationToken(userinfo, token, NO_AUTHORITIES);

        
    mockMvc.perform(get("/Users/profile").with(csrf()).with(user(userinfo)).with(authentication(auth)))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/signout"));
  }
 
  @Test
  public void testUserProfile() throws Exception {
    UserInfo userinfo = new UserInfo("123", "Administrator", "Administrator", "Toucan", "", "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg", null, "aokinobu@gmail.com", "false");
        OAuth2AccessToken token = new DefaultOAuth2AccessToken(tokenValue);
    
        logger.debug("Token:>" + token.getValue());
      
      Authentication auth = new  PreAuthenticatedAuthenticationToken(userinfo, token, NO_AUTHORITIES);

      User user = new User();
      user.setEmail(userinfo.getEmail());
      user.setFamilyName(userinfo.getFamilyName());
      user.setGivenName(userinfo.getGivenName());
        
    mockMvc.perform(get("/Users/profile").with(csrf()).with(user(userinfo)).with(authentication(auth)))
        .andExpect(status().isOk())
        .andExpect(view().name("users/profile"));
//        .andExpect(request().attribute("userProfile", contains(user)));
  }
  
}