package org.glytoucan.web.controller;

import static nl.captcha.Captcha.NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.glytoucan.web.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Application.class })
@WebAppConfiguration
public class CaptchaControllerTest {

  @Autowired
  private WebApplicationContext wac;

  private MockMvc mockMvc;
  private MockHttpSession mockHttpSession;

  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
    mockHttpSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
  }

  @Test
  public void testIndex() throws Exception {
    // http://kuwalab.hatenablog.jp/entry/spring_mvc41/028
    assertThat(mockHttpSession.getAttribute(NAME), is(nullValue()));
    mockMvc.perform(get("/Captcha/image").session(mockHttpSession)).andExpect(status().isOk());
    assertThat(mockHttpSession.getAttribute(NAME), is(notNullValue()));
  }
}
