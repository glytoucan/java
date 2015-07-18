package org.glytoucan.ws.security;

import static org.springframework.http.HttpMethod.GET;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

//@Configuration
//@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final String LOGIN_URL = "/login";
//    private final String REDIRECT_URL = "/";

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new GlyLoginAuthenticationEntryPoint(LOGIN_URL);
    }

    @Bean
    public OpenIDConnectAuthenticationFilter openIdConnectAuthenticationFilter() {
    	OpenIDConnectAuthenticationFilter open = new OpenIDConnectAuthenticationFilter(LOGIN_URL);
    	open.setAuthenticationFailureHandler(new OpenIDAuthenticationFailureHandler());
    	open.setAuthenticationSuccessHandler(new OpenIDAuthenticationSuccessHandler());
    	
        return open;
    }

    @Bean
    public OAuth2ClientContextFilter oAuth2ClientContextFilter() {
        return new OAuth2ClientContextFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(oAuth2ClientContextFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .addFilterAfter(openIdConnectAuthenticationFilter(), OAuth2ClientContextFilter.class)
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and().authorizeRequests()
                .antMatchers(GET, "/").permitAll()
                .antMatchers(GET, "/statuscheck**/*").authenticated()
                .antMatchers(GET, "/statuscheck**").authenticated()
                .antMatchers(GET, "/users**/*").authenticated()
                .antMatchers(GET, "/users**").authenticated();
    }
}
