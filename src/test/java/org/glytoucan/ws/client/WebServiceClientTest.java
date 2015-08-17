package org.glytoucan.ws.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import com.sun.research.ws.wadl.Application;

public class WebServiceClientTest {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger(WebServiceClientTest.class);

	@Test
	public void image() throws Exception {
		GlyspaceClient gsClient = new GlyspaceClient();
		String image = gsClient.getImage("https://test.glytoucan.org", "RES\\n"
				+ "1b:x-dglc-HEX-x:x\\n"
				+ "2b:b-dgal-HEX-1:5\\n"
				+ "3b:a-dgal-HEX-1:5\\n"
				+ "4b:b-dgal-HEX-1:5\\n"
				+ "5s:n-acetyl\\n"
				+ "LIN\\n"
				+ "1:1o(4+1)2d\\n"
				+ "2:2o(3+1)3d\\n"
				+ "3:3o(3+1)4d\\n"
				+ "4:4d(2+1)5n");
		logger.debug(image);
	}
	
	@Test
	public void testRead() throws IOException {
	    /* Test image to string and string to image start */
        BufferedImage img = ImageIO.read(new File("/tmp/download.png"));
        BufferedImage newImg;
        String imgstr;
		GlyspaceClient gsClient = new GlyspaceClient();
        imgstr = gsClient.encodeToString(img, "png");
        System.out.println(imgstr);
        newImg = gsClient.decodeToImage(imgstr);
        ImageIO.write(newImg, "png", new File("/tmp/CopyOfTestImage.png"));
        /* Test image to string and string to image finish */
	}
}
