package org.glytoucan.ws.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glytoucan.client.config.GlycanQueryConfig;
import org.glytoucan.model.GlycanList;
import org.glytoucan.model.spec.GlycanQuerySpec;
import org.glytoucan.model.spec.GlycanSpec;
import org.glytoucan.ws.model.XmlUrl;
import org.glytoucan.ws.model.XmlUrlSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.knappsack.swagger4springweb.annotation.ApiExclude;

@Controller
@ApiExclude
@Import(value = { GlycanQueryConfig.class })
public class WelcomeController {
	Log logger = LogFactory.getLog(WelcomeController.class);

	@Autowired
	GlycanQuerySpec glycanQueryRest;

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

	@RequestMapping(value = "/sitemap.xml", method = RequestMethod.GET, produces="application/xml")
	@ResponseBody
	public XmlUrlSet main() {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> results = glycanQueryRest.getListStructures(map);
		String limit = "10000";
		map.put(GlycanQuerySpec.LIMIT, limit);

		GlycanList list = (GlycanList) results.get(GlycanSpec.MESSAGE);

		int offset = 0;

		ArrayList<String> accs = new ArrayList<String>();

		// http://${hostname}/
		XmlUrlSet xmlUrlSet = new XmlUrlSet();
		create(xmlUrlSet, "", XmlUrl.Priority.HIGH);

		while (list.getGlycans() != null && list.getGlycans().size() > 0) {

			logger.debug(list.getGlycans());
			for (Object gly : list.getGlycans()) {
				String acc = (String) gly;
				create(xmlUrlSet, "/Structures/Glycans/" + acc, XmlUrl.Priority.MEDIUM);

				accs.add(acc);

				logger.debug("acc:>" + acc);
			}

			offset += 10000;
			map.put(GlycanQuerySpec.OFFSET, offset + "");
			results = glycanQueryRest.getListStructures(map);
			list = (GlycanList) results.get(GlycanSpec.MESSAGE);
		}

		return xmlUrlSet;
	}

	private void create(XmlUrlSet xmlUrlSet, String link, XmlUrl.Priority priority) {
		xmlUrlSet.addUrl(new XmlUrl("https://glytoucan.org" + link, priority));
	}
}
