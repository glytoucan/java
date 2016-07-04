package org.glytoucan.web.view;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.glytoucan.web.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest("server.port=0")
public class HtmlTests {

	
	@Autowired
	InternalResourceViewResolver internalResourceViewResolver;

	@Value("${local.server.port}")
	int port;

	@Test
	public void testInternalResourceViewResolver() {
		assertThat(internalResourceViewResolver, is(not(nullValue())));
	}

	@Test
	public void testIndex() {
		ResponseEntity<String> entity = new TestRestTemplate().getForEntity(
				"http://localhost:{port}", String.class, this.port);
		assertThat(entity.getStatusCode(), is(HttpStatus.OK));
		assertThat(entity.getBody(), containsString("THE GLYCAN REPOSITORY"));
	}
	
	@Test
	public void testD3() {
		ResponseEntity<String> entity = new TestRestTemplate().getForEntity(
				"http://localhost:{port}/D3_dndTree/G00030MO", String.class, this.port);
		assertThat(entity.getStatusCode(), is(HttpStatus.OK));
		assertThat(entity.getBody(), containsString("ID: G00030MO"));
	}
}
