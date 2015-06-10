package org.glytoucan.ws.controller;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.startsWith;

import org.apache.http.HttpStatus;
import org.glytoucan.ws.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.jayway.restassured.RestAssured;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class RestOpenIDTest {

    @Value("${local.server.port}")
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void welcomePageNotRedirected() {
        given().redirects().follow(false).when().get("/").then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void securedPageRedirectsToLoginPage() {
        given().redirects().follow(false).when().get("/statuscheck").then()
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
    public void jsonRequestReturnsData() {
        given().redirects().follow(false).when().get("/statuscheck.json").then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }
}