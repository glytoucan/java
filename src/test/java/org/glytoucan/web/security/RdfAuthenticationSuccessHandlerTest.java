package org.glytoucan.web.security;

import static org.springframework.security.core.authority.AuthorityUtils.NO_AUTHORITIES;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.web.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.fromi.openidconnect.security.UserInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class , RdfAuthenticationSuccessHandlerTest.class})
public class RdfAuthenticationSuccessHandlerTest {
  private static final Log logger = LogFactory.getLog(RdfAuthenticationSuccessHandlerTest.class);

  private static String tokenValue = "ya29.CjBeA-KtKu-fPJ4aMvdqIUDT8MvBs-nSj4rPUO5iA5dDDx8yCDADqrXakGPeP0vMqpE";
  
  @Autowired
  @Qualifier("rash")
  RdfAuthenticationSuccessHandler rash;

  @Test(expected=ServletException.class)
  public void testBlankToken() throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    UserInfo userinfo = new UserInfo("123", "Administrator", "Administrator", "Toucan", "",
        "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg", null,
        "glytoucan@gmail.com", "true");
    OAuth2AccessToken token = new DefaultOAuth2AccessToken("");

    logger.debug("Token:>" + token.getValue());

    Authentication auth = new PreAuthenticatedAuthenticationToken(userinfo, token, NO_AUTHORITIES);

    rash.onAuthenticationSuccess(request, response, auth);
  }
  
  @Test(expected=ServletException.class)
  public void testInvalidToken() throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    UserInfo userinfo = new UserInfo("123", "Administrator", "Administrator", "Toucan", "",
        "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg", null,
        "glytoucan@gmail.com", "true");
    OAuth2AccessToken token = new DefaultOAuth2AccessToken("invalid");

    logger.debug("Token:>" + token.getValue());

    Authentication auth = new PreAuthenticatedAuthenticationToken(userinfo, token, NO_AUTHORITIES);

    rash.onAuthenticationSuccess(request, response, auth);
  }
  
  @Test
  public void testAdminLogin() throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    UserInfo userinfo = new UserInfo("123", "Administrator", "Administrator", "Toucan", "",
        "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg", null,
        "glytoucan@gmail.com", "true");
    OAuth2AccessToken token = new DefaultOAuth2AccessToken(tokenValue);

    logger.debug("Token:>" + token.getValue());

    Authentication auth = new PreAuthenticatedAuthenticationToken(userinfo, token, NO_AUTHORITIES);

    rash.onAuthenticationSuccess(request, response, auth);
  }
  
  @Test
  public void testUserLogin() throws ServletException, IOException {
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    UserInfo userinfo = new UserInfo("1234", "test", "test", "Toucan", "",
        "https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg", null,
        "testglytoucan@gmail.com", "true");
    OAuth2AccessToken token = new DefaultOAuth2AccessToken("ya29.CjFeA_WccJBpAUshKFY1x97oG_9OVpO7yEAuwkOW5L_88Xr8rcrZDmZGSdl-qpRKi-yM");

    logger.debug("Token:>" + token.getValue());

    Authentication auth = new PreAuthenticatedAuthenticationToken(userinfo, token, NO_AUTHORITIES);

    rash.onAuthenticationSuccess(request, response, auth);
  }
  
  @Bean(name="rash")
  RdfAuthenticationSuccessHandler RdfAuthenticationSuccessHandler() {
    return new RdfAuthenticationSuccessHandler();
  }

}
