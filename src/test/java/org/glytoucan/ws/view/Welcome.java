package org.glytoucan.ws.view;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.glytoucan.ws.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class Welcome {
	@Value("${local.server.port}")
	int port;
	
	@Test
	public void testIndex() {
		ResponseEntity<String> entity = new TestRestTemplate().getForEntity(
				"http://localhost:{port}", String.class, this.port);
		assertThat(entity.getStatusCode(), is(HttpStatus.OK));
		assertThat(entity.getBody(), containsString("Tweets by @glytoucan"));
	}
}
