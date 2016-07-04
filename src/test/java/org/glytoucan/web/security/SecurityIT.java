package org.glytoucan.web.security;

import static com.jayway.restassured.RestAssured.given;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.springframework.security.oauth2.common.AuthenticationScheme.form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.glytoucan.web.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.github.fromi.openidconnect.security.UserInfo;
import com.jayway.restassured.RestAssured;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class SecurityIT {

	private static final Log logger = LogFactory.getLog(SecurityIT.class);
	
    @Value("${local.server.port}")
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }
    
	@Value("${google.oauth2.clientId}")
    private String clientId;

    @Value("${google.oauth2.clientSecret}")
    private String clientSecret;

    @Autowired
    private OAuth2RestOperations restTemplate;
    
    @Test
    public void welcomePageNotRedirected() {
        given().redirects().follow(false).when().get("/").then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void securedPageRedirectsToLoginPage() {
        given().redirects().follow(false).when().get("/Registries/index").then()
                .statusCode(HttpStatus.SC_MOVED_TEMPORARILY)
                .header("Location", endsWith("/login"));
    }

    @Test
    public void loginPageRedirectsToGoogle() {
        given().redirects().follow(false).when().get("/login").then()
                .statusCode(HttpStatus.SC_MOVED_TEMPORARILY)
                .header("Location", startsWith("https://accounts.google.com/o/oauth2/auth"));
    }
    
    @Test
    public void testToken() {
    	
    	
    	DefaultOAuth2AccessToken defToken = new DefaultOAuth2AccessToken("ya29.qQKucenHaUWeYJudz1MN5XpFyC4LAM0cvbqohRyVmVi7ai8uq0KW4khewbKuyaLhWO-jfcI");
    	DefaultOAuth2ClientContext defaultContext = new DefaultOAuth2ClientContext();
    	defaultContext.setAccessToken(defToken);
    	OAuth2RestOperations rest = new OAuth2RestTemplate(googleOAuth2Details(), defaultContext);
        final ResponseEntity<UserInfo> userInfoResponseEntity = rest.getForEntity("https://www.googleapis.com/oauth2/v2/userinfo", UserInfo.class);
        logger.debug("userInfo:>" + userInfoResponseEntity.toString());
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
}
