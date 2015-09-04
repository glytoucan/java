package org.glytoucan.ws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ExtendedModelMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest("server.port=0")
public class ApplicationTests {

	Log logger = LogFactory.getLog(ApplicationTests.class);

	@Value("${local.server.port}")
	int port;

	@Test
	public void testI18n() {
//		File file = new File("classpath:/1.json");
		Resource resource = new ClassPathResource("1.json");
		
		ObjectMapper mapper = new ObjectMapper();
		// build a JSON object
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readTree(resource.getFile());
		} catch (IOException e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
	    logger.debug(rootNode);
	    
	    JsonNode result = rootNode.get("result");
	    logger.debug(result);
	    JsonNode article = result.get("article");
	    logger.debug(article);
	    JsonNode Motifs = article.get("Motifs");
	    logger.debug(Motifs);
	    ExtendedModelMap emm = new ExtendedModelMap();
	    Map motifsMap = null;
		try {
			motifsMap = mapper.treeToValue(Motifs, Map.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			Assert.fail();
		}
	    
	    logger.debug(motifsMap);
	    emm.addAllAttributes(motifsMap);
	    JsonNode common = rootNode.get("result").get("common");
	    logger.debug(common);

	}
	
	@Test
	public void testDownloadI18N() throws IOException {
		JsonNode rootNode = null;
		File file = null;
		ObjectMapper mapper = new ObjectMapper();
		File dir = new File(System.getProperty("java.io.tmpdir") + "/glytoucan.localization");
		if (!dir.exists()) {
			dir.mkdir();
		}
		if (dir.exists()) {
			file = new File(dir.getAbsolutePath() + "/" + "1" + ".json");
		}
		if (null != file && !file.exists()) {
			//download
			URL localizationfile = new URL("http://local.glytoucan.org/localizations/get_json/" + "1" +".json");
			ReadableByteChannel rbc = Channels.newChannel(localizationfile.openStream());
			FileOutputStream fos = new FileOutputStream(file);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		}
		logger.debug("size of file:>" + file.getTotalSpace());
		rootNode = mapper.readTree(file);
	}
	
	
}
