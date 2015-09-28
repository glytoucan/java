package org.glytoucan.ws.client;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GlyspaceClient {
	protected Log logger = LogFactory.getLog(getClass());

	public String getImage(String hostname, String sequence) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
		String url = hostname + "/glyspace/service/glycans/image/glycan?format=png&notation=cfg&style=extended";
	
		SSLContextBuilder builder = new SSLContextBuilder();
	    builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
	    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
	            builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	    CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(
	            sslsf).build();
		HttpPost post = new HttpPost(url);
		logger.debug("url:>" + url);

		// add header
		post.setHeader("Content-Type", "application/json");

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("structure", sequence);
		data.put("encoding", "glycoct");
		ObjectMapper mapper = new ObjectMapper();
		String output = mapper.writeValueAsString(data);
		String output2 = "{\"structure\":\"" + sequence + "\", \"encoding\":\"glycoct\"}"; 
		logger.debug("output:"+output);
		logger.debug("output:"+output2);
		
		post.setEntity(new StringEntity(output2));

		HttpResponse response = client.execute(post);
		int code = response.getStatusLine().getStatusCode();
		logger.debug("status code:"+code);
		if (code != 200) {
			
			
			
			return null;
		}

		BufferedReader rd = new BufferedReader(
		        new InputStreamReader(response.getEntity().getContent()));

//		StringBuilder result = new StringBuilder();
//		String line = "";
//		while ((line = rd.readLine()) != null) {
//			logger.debug("line:>" + line);
//			result.append(line);
//		}
        BufferedImage bufimage = ImageIO.read(response.getEntity().getContent());

		String image = encodeToString(bufimage, "png"); 
		
		return "data:image/png;base64," + image;
	}
	
	String getRegisteredImage(String hostname) throws IOException {
		String url = hostname + "/glycans/image/glycan?format=png&notation=cfg&style=extended'";
	
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		logger.debug("status code:"+code);
		if (code != 200) {
			return null;
		}
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));

		StringBuilder result = new StringBuilder("data:image/png;base64,");
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(DatatypeConverter.printBase64Binary(line.getBytes()));
		}
		return result.toString();
	}
	
    /**
     * Decode string to image
     * @param imageString The string to decode
     * @return decoded image
     */
    public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            imageByte = Base64.decodeBase64(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Encode image to string
     * @param image The image to encode
     * @param type jpeg, bmp, ...
     * @return encoded string
     */
    public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            imageString = Base64.encodeBase64String(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }
    
    

}
