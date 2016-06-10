package org.glytoucan.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fromi.openidconnect.security.UserInfo;

public class LocalizationHandlerMapping extends HandlerInterceptorAdapter {
	public static final String LANGUAGE = "language";
	public static final String HOSTNAME = "hostname";
	public static final String IMAGENOTATION = "imageNotation";
	Log logger = LogFactory.getLog(LocalizationHandlerMapping.class);

	private static String env = null;

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws IOException {
		if (modelAndView != null
				&& (modelAndView.getViewName() != null && !modelAndView
						.getViewName().startsWith("redirect:"))) {
			HttpSession session = request.getSession();

			String hostname = (String) session.getAttribute(HOSTNAME);

			if (null == hostname) {
				hostname = System.getProperty("hostname");
				if (null == hostname)
					hostname = "test.glytoucan.org";
				logger.debug("hostname:>" + hostname + "<");
				
				session.setAttribute(HOSTNAME, hostname);
			}
			modelAndView.addObject("hostname", hostname);

			String language = (String) session.getAttribute(LANGUAGE);
			logger.debug("language:>" + language + "<");
			Enumeration<String> headers = request.getHeaderNames();
			// accept-encoding
			// 2015-08-03 09:31:15 DEBUG LocalizationHandlerMapping:43 -
			// accept-language

			// logger.debug(headers);
			// String acceptEncoding = request.getHeader("accept-encoding");
			// String acceptLanguage = request.getHeader("accept-language");
			// // logger.debug(acceptEncoding);
			// logger.debug(acceptLanguage);
			// gzip, deflate, sdch
			// 2015-08-03 10:27:02 DEBUG LocalizationHandlerMapping:85 -
			// en-US,en;q=0.8,ja;q=0.6
			// if (null == language) {
			// StringTokenizer tok = new StringTokenizer(acceptLanguage, ";");
			// while(tok.hasMoreTokens()) {
			// String token = tok.nextToken();
			// switch (token) {
			// case "en":
			// language = "1";
			// break;
			// case "ja":
			// language = "2";
			// break;
			// case "zh-cn":
			// language = "3";
			// break;
			// case "zh-hk":
			// language = "3";
			// break;
			// case "zh-tw":
			// language = "4";
			// break;
			// case "fr":
			// language = "5";
			// break;
			// case "de":
			// language = "6";
			// break;
			// case "ro":
			// language = "7";
			// break;
			// default:
			// break;
			// }
			// }
			if (null == language)
				language = "1";
			// }

			// while (headers.hasMoreElements()) {
			// String header = (String) headers.nextElement();
			// logger.debug(header);
			// }
			String requestUri = request.getRequestURI();
			logger.debug("requestUri:>" + requestUri + "<");
			String controller = null, page = null;
			if (requestUri != null && requestUri.trim().equals("/")) {
				controller = "Stanzas";
				page = "index";
			} else {
				int splitIndex = requestUri.indexOf('/', 1);

				logger.debug("splitIndex:>" + splitIndex + "<");
				if (splitIndex < 0) {
					controller = requestUri.substring(1);
					page = "index";
				} else {
					controller = requestUri.substring(1, splitIndex);
					page = requestUri.substring(splitIndex + 1);
				}
			}

			logger.debug("controller:>" + controller + "<");
			logger.debug("page:>" + page + "<");

			if (controller != null && controller.equals("error")) {
				controller = "Stanzas";
			}

			JsonNode rootNode = null;
			File file = null;
			ObjectMapper mapper = new ObjectMapper();
			try {
				File dir = new File(System.getProperty("java.io.tmpdir")
						+ "/glytoucan.localization");
				if (!dir.exists()) {
					dir.mkdir();
				}
				if (dir.exists()) {
					file = new File(dir.getAbsolutePath() + "/" + language
							+ ".json");
				}
				if (null != file && !file.exists()) {
					// download
					URL localizationfile = new URL(
							"http://local.glytoucan.org/localizations/get_json/"
									+ language + ".json");
					logger.debug("downloading file from:"
							+ localizationfile.getFile());
					ReadableByteChannel rbc = Channels
							.newChannel(localizationfile.openStream());
					FileOutputStream fos = new FileOutputStream(file);
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				} else {
					logger.debug("file exists at:" + file.getAbsolutePath());
				}
				logger.info("size of file:>" + file.getTotalSpace());
				rootNode = mapper.readTree(file);

			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			String imageNotation = (String) session.getAttribute(IMAGENOTATION);
			if (StringUtils.isBlank(imageNotation))
				imageNotation = "cfg";
			logger.debug("imageNotation:>" + imageNotation + "<");

			modelAndView.addObject("imageNotation", imageNotation);
			modelAndView.addObject("language", language);
			/** http://freemarker.org/docs/pgui_quickstart_createconfiguration.html */
			modelAndView.addObject("imagepath", "https://glytoucan.org/glycans/");

			ClassPathResource resource;
			if (null == rootNode) {
				resource = new ClassPathResource("1.json");
				rootNode = mapper.readTree(resource.getFile());
			}

			// logger.debug(rootNode);

			JsonNode result = rootNode.get("result");
			// logger.debug(result);
			JsonNode article = result.get("article");
			// logger.debug(article);

			// get common
			JsonNode common = rootNode.get("result").get("common");
			// logger.debug(common);
			Map commonMap = null;
			try {
				commonMap = mapper.treeToValue(common, Map.class);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			try {
				modelAndView.addAllObjects(commonMap);
			} catch (Exception e) {
				// language reading fails but shouldn't die.
				logger.debug(e.getMessage());
			}

			if (controller != null && (controller.contains("D3") || controller.contains("init") || controller.contains("sitemap"))) {
				logger.debug(modelAndView.getModel().keySet());

				return;
			}

			
			try {
			JsonNode pageNode = article.get(controller).get(page);
			Map contextMap = null;
			try {
				contextMap = mapper.treeToValue(pageNode, Map.class);
			} catch (NullPointerException e) {
				page = "index";
				pageNode = article.get(controller).get(page);
				contextMap = mapper.treeToValue(pageNode, Map.class);
			}
			logger.debug(pageNode);
			modelAndView.addAllObjects(contextMap);
			} catch (java.lang.NullPointerException npe) {
			  logger.debug("this page does not exist in localization:" + controller + "/" + page);
			}

			logger.debug(modelAndView.getModel().keySet());
			try {
				if (SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal() instanceof UserInfo){
					UserInfo userInfo = (UserInfo) SecurityContextHolder
							.getContext().getAuthentication().getPrincipal();
					if (null != userInfo && userInfo.getVerifiedEmail() != null
							&& userInfo.getVerifiedEmail().equals("true")) {
						logger.debug("user is verified");
						modelAndView.addObject("user", true);
					}
				}
			} catch (NullPointerException e) {
				logger.debug("user is not verified");
			}
		}
	}
}