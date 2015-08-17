package org.glytoucan.ws.security;

import static java.util.Arrays.asList;
import static org.springframework.security.oauth2.common.AuthenticationScheme.form;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableOAuth2Client
public class OAuth2Client {
	static final Logger logger = LoggerFactory.getLogger(OAuth2Client.class);
	
	@Value("${google.oauth2.clientId}")
    private String clientId;

    @Value("${google.oauth2.clientSecret}")
    private String clientSecret;

    @Bean
    // TODO retrieve from https://accounts.google.com/.well-known/openid-configuration ?
    public OAuth2ProtectedResourceDetails googleOAuth2Details() {
   	 	logger.debug("clientId:>" + clientId + "<");
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

    @SuppressWarnings("SpringJavaAutowiringInspection") // Provided by Spring Boot
    @Resource
    private OAuth2ClientContext oAuth2ClientContext;

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
    public OAuth2RestOperations googleOAuth2RestTemplate() {
        return new OAuth2RestTemplate(googleOAuth2Details(), oAuth2ClientContext);
    }
    
//    @Bean
//    @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
//    public OAuth2RestOperations googleOAuth2RestTemplate() {
//    	OAuth2RestTemplate temp = new OAuth2RestTemplate(googleOAuth2Details(), oAuth2ClientContext);
//    	AccessTokenProviderChain accessTokenProvider = new AccessTokenProviderChain(Arrays.<AccessTokenProvider> asList(
//    			new AuthorizationCodeAccessTokenProvider(), new ImplicitAccessTokenProvider(),
//    			new ResourceOwnerPasswordAccessTokenProvider(), new ClientCredentialsAccessTokenProvider()));
////    	accessTokenProvider.setClientTokenServices(clientTokenServices());
//    	temp.setAccessTokenProvider(accessTokenProvider);
//        return temp; 
//    }
    
//	@Bean
//	public ClientTokenServices clientTokenServices() {
//
//		return new JdbcClientTokenServices(null);
//	}
}