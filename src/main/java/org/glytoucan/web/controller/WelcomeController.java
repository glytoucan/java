package org.glytoucan.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.client.GlycoSequenceClient;
import org.glytoucan.client.config.GlycanQueryConfig;
import org.glytoucan.client.soap.GlycoSequenceCountResponse;
import org.glytoucan.model.GlycanList;
import org.glytoucan.model.spec.GlycanClientQuerySpec;
import org.glytoucan.model.spec.GlycanClientRegisterSpec;
import org.glytoucan.web.model.XmlSiteMap;
import org.glytoucan.web.model.XmlSiteMapSet;
import org.glytoucan.web.model.XmlUrl;
import org.glytoucan.web.model.XmlUrlSet;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.knappsack.swagger4springweb.annotation.ApiExclude;

/**
 * @author developer
 *
 */
@Controller
@ApiExclude
@Import(value = { GlycanQueryConfig.class })
public class WelcomeController {
	Log logger = LogFactory.getLog(WelcomeController.class);

	@Value("${sitemapHostname:https://glytoucan.org}")
	String serverBasePath;

	@Autowired
	GlycanClientQuerySpec glycanQueryRest;

	@Autowired
	GlycoSequenceClient glycoSequenceClient;

	@RequestMapping("/")
	public String welcome(Model model) {
		return "index";
	}

	@RequestMapping("/init")
	public String init(Model model) {
		return "init";
	}

	@RequestMapping("/signout")
	public String signout(Model model, HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@RequestMapping("/signin")
	public String signin(Model model, HttpSession session) {
		session.invalidate();
		logger.debug("signin");
		return "redirect:/?errorMessage=something happened with signin";
	}

	@RequestMapping(value = "/sitemap{sitemapNumber}.xml", method = RequestMethod.GET, produces = "application/xml")
	@ResponseBody
	public XmlUrlSet main(Model model, @PathVariable String sitemapNumber) {
		
		if (StringUtils.isBlank(sitemapNumber)) {
			sitemapNumber="0";
		}
		
		int iSitemapNum = Integer.parseInt(sitemapNumber);
		
		int offset = 50000 * iSitemapNum;
 
		Map<String, Object> map = new HashMap<String, Object>();
		String limit = "10000";
		map.put(GlycanClientQuerySpec.OFFSET, "" + offset);
		map.put(GlycanClientQuerySpec.LIMIT, limit);
		Map<String, Object> results = glycanQueryRest.getListStructures(map);

		GlycanList list = (GlycanList) results.get(GlycanClientRegisterSpec.MESSAGE);

		ArrayList<String> accs = new ArrayList<String>();

		// http://${hostname}/
		XmlUrlSet xmlUrlSet = new XmlUrlSet();
		
		int count = 0;

		while (list.getGlycans() != null && list.getGlycans().size() > 0 && count < 5) {

			logger.debug(list.getGlycans());
			for (Object gly : list.getGlycans()) {
				String acc = (String) gly;
				create(xmlUrlSet, "/Structures/Glycans/" + acc, XmlUrl.Priority.MEDIUM);

				accs.add(acc);

				logger.debug("acc:>" + acc);
			}

			offset += 10000;
			map.put(GlycanClientQuerySpec.OFFSET, offset + "");
			results = glycanQueryRest.getListStructures(map);
			list = (GlycanList) results.get(GlycanClientRegisterSpec.MESSAGE);
			
			count++;
		}

		return xmlUrlSet;
//		model.addAttribute("listAccs", xmlUrlSet);
//		return "sitemap";
	}

	@RequestMapping(value = "/connect", method = RequestMethod.GET, produces = "text/html")
	@ResponseBody
	public String connect(HttpServletRequest request) throws IOException {
		String recv = "";
		StringBuffer recvbuff = new StringBuffer();
		String url = request.getParameter("url");
		url = url.replaceAll("\\s+", "+");

		URL datapage = new URL(url);

		logger.debug(request.getParameter("url"));
		URLConnection urlcon = datapage.openConnection();
		BufferedReader buffread = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));

		while ((recv = buffread.readLine()) != null) {
			recvbuff.append(recv).append("\n");
		}
		buffread.close();

		return recvbuff.toString();
	}

	private void create(XmlUrlSet xmlUrlSet, String link, XmlUrl.Priority priority) {
		xmlUrlSet.addUrl(new XmlUrl(serverBasePath + link, priority));
	}

	/**
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <sitemapindex xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
	 * <sitemap> <loc>http://www.example.com/sitemap1.xml.gz</loc>
	 * <lastmod>2004-10-01T18:23:17+00:00</lastmod> </sitemap> <sitemap>
	 * <loc>http://www.example.com/sitemap2.xml.gz</loc>
	 * <lastmod>2005-01-01</lastmod> </sitemap> </sitemapindex>
	 * 
	 * @return
	 */
	@RequestMapping(value = "/sitemapIndex.xml", method = RequestMethod.GET, produces = "application/xml")
	@ResponseBody
	public XmlSiteMapSet sitemapIndex() {

		GlycoSequenceCountResponse gscr = glycoSequenceClient.countRequest();
		String count = gscr.getCount();
		int iCount = Integer.parseInt(count);

		// 56k
		int num = (int) Math.ceil(iCount / 50000);
		XmlSiteMapSet xmlSiteMapSet = new XmlSiteMapSet();
		
		logger.debug("number of pages:" + num);
		
		xmlSiteMapSet.addSiteMap(new XmlSiteMap(serverBasePath + "/sitemapTop.xml"));
		
		for (int i=0; i <= num;i++) {
			// create sitemap index for
			xmlSiteMapSet.addSiteMap(new XmlSiteMap(serverBasePath + "/sitemap" + i + ".xml"));
		}

		return xmlSiteMapSet;
	}
	
	@RequestMapping(value = "/sitemapTop.xml", method = RequestMethod.GET, produces = "application/xml")
	@ResponseBody
	public XmlUrlSet mainTop() {
		XmlUrlSet xmlUrlSet = new XmlUrlSet();
		create(xmlUrlSet, "", XmlUrl.Priority.HIGH);
		create(xmlUrlSet, "/Structures", XmlUrl.Priority.HIGH);
		create(xmlUrlSet, "/Structures/graphical", XmlUrl.Priority.HIGH);
		create(xmlUrlSet, "/Structures/structureSearch", XmlUrl.Priority.HIGH);
		create(xmlUrlSet, "/Motifs/listAll", XmlUrl.Priority.HIGH);
		create(xmlUrlSet, "/Motifs/search", XmlUrl.Priority.HIGH);
		create(xmlUrlSet, "/Preferences/index", XmlUrl.Priority.HIGH);
		create(xmlUrlSet, "/Preferences/ja", XmlUrl.Priority.HIGH);
		create(xmlUrlSet, "/Preferences/ch1", XmlUrl.Priority.HIGH);
		create(xmlUrlSet, "/Preferences/ch2", XmlUrl.Priority.HIGH);
		create(xmlUrlSet, "/Preferences/fr", XmlUrl.Priority.HIGH);
		create(xmlUrlSet, "/Preferences/de", XmlUrl.Priority.HIGH);
		create(xmlUrlSet, "/Preferences/ru", XmlUrl.Priority.HIGH);

		return xmlUrlSet;
	}
}
