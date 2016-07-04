package org.glytoucan.web.security;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import com.github.fromi.openidconnect.security.OpenIDConnectAuthenticationFilter;
import com.github.fromi.openidconnect.security.SecurityConfiguration;

@Configuration
@EnableWebSecurity
@Order(90)
@ComponentScan(basePackages="com.github.fromi.openidconnect.security")
public class GlytoucanSecurityConfiguration extends SecurityConfiguration {

    private final String LOGIN_URL = "/login";

    @Bean
    public AuthenticationSuccessHandler successHandler() {
    	return new RdfAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
    	SimpleUrlAuthenticationFailureHandler suafh = new SimpleUrlAuthenticationFailureHandler();
    	suafh.setDefaultFailureUrl("/");
    	return suafh;
    }

    @Bean
    public OpenIDConnectAuthenticationFilter openIdConnectAuthenticationFilter() {
    	OpenIDConnectAuthenticationFilter open = new GlytoucanConnectAuthenticationFilter(LOGIN_URL);
    	open.setAuthenticationSuccessHandler(successHandler());
    	open.setAuthenticationFailureHandler(failureHandler());
        return open;
    }

    private CsrfTokenRepository csrfTokenRepository() 
    { 
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository(); 
        repository.setSessionAttributeName("_csrf");
        return repository; 
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//    	http.csrf().disable();
    	http.csrf().ignoringAntMatchers("/img/**").ignoringAntMatchers("/glycans/**").ignoringAntMatchers("/Structures/Accession**");
        http.addFilterAfter(oAuth2ClientContextFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .addFilterAfter(openIdConnectAuthenticationFilter(), OAuth2ClientContextFilter.class)
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and().authorizeRequests()
                .antMatchers(GET, "/").permitAll()
                .antMatchers(GET, "/sitemap.xml").permitAll()
                .antMatchers(GET, "/error").permitAll()
                .antMatchers(GET, "/Structures/**").permitAll()
                .antMatchers(POST, "/Structures/**").permitAll()
                .antMatchers(POST, "/glycans/**").permitAll()
                .antMatchers(GET, "/Users/**").authenticated()
                .antMatchers(GET, "/Registries/**").authenticated()
                .antMatchers(POST, "/Registries/**").authenticated();
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/sitemap.xml").antMatchers("/img/**").antMatchers("/glycans/**").antMatchers("/Structures/Accession**");
    }
    
//    @Value("${api.contributor.id}")
//    private String username;
//
//    @Value("${api.key}")
//    private String hash;

    @Bean(name="api.contributor.id")
    public String apiContributorId() {
      return "test";      
    }

    @Bean(name="api.key")
    public String apiKey() {
      return "testKey";
    }
}