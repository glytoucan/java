package org.glytoucan.ws.controller;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.client.GlyspaceClient;
import org.glycoinfo.conversion.util.DetectFormat;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.service.GlycanProcedure;
import org.glytoucan.ws.model.SequenceInput;
import org.glytoucan.ws.security.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.knappsack.swagger4springweb.annotation.ApiExclude;
import com.wordnik.swagger.annotations.ApiOperation;

@Controller
@ApiExclude
@RequestMapping("/Registries")
public class RegistriesController {
	Log logger = LogFactory.getLog(RegistriesController.class);

	@Autowired
	GlycanProcedure glycanProcedure;
	
	@RequestMapping("/graphical")
	public String graphical(Model model, RedirectAttributes redirectAttrs) {
    	return "register/graphical";
	}

	@RequestMapping(value="/index", method = RequestMethod.GET)
	public String root(Model model, @ModelAttribute("sequence") SequenceInput sequence, BindingResult result/*, @RequestParam(value="errorMessage", required=false) String errorMessage*/) {
		return "register/index";
	}

	@RequestMapping("/confirmation")
    public String confirmation(Model model, @ModelAttribute("sequence") SequenceInput sequence, BindingResult result, RedirectAttributes redirectAttrs) throws SparqlException  {
		logger.debug(sequence);
		List<SequenceInput> list = new ArrayList<SequenceInput>();
		List<SequenceInput> listRegistered = new ArrayList<SequenceInput>();
		List<SequenceInput> listErrors = new ArrayList<SequenceInput>();
        if (StringUtils.isEmpty(sequence.getSequence())) {
        	
        	logger.debug("adding errorMessage:>input sequence<");
        	redirectAttrs.addFlashAttribute("errorMessage", "Please input a sequence");
        	return "redirect:/Registries/index";
        } else {
    		logger.debug("text1>" + sequence.getSequence() + "<");

    		List<String> inputs = DetectFormat.split(sequence.getSequence());
    		logger.debug("split input:>" + inputs + "<");
    		SparqlEntity se;
    		List<SparqlEntity> searchResults;
    		try {
    			searchResults = glycanProcedure.search(inputs);
			} catch (SparqlException e) {
				redirectAttrs.addAttribute("errorMessage", "an error occurred");
				return "redirect:/Registries/index";
			}
    		
    		logger.debug("search results:>" + searchResults + "<");
    		for (Iterator iterator = searchResults.iterator(); iterator
					.hasNext();) {
				SparqlEntity sparqlEntity = (SparqlEntity) iterator.next();

	    		SequenceInput si = new SequenceInput();
	    		
    			si.setSequence(sparqlEntity.getValue(GlycanProcedure.FromSequence));
    			String imageSequence = si.getSequence().replaceAll("(?:\\r\\n|\\n)", "\\\\n");
    			si.setSequence(imageSequence);

	    		String resultSequence = null;
//				try {
//					resultSequence = URLEncoder.encode(sparqlEntity.getValue(GlycoSequenceToWurcsSelectSparql.Sequence), "UTF-8");
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
					resultSequence = sparqlEntity.getValue(GlycanProcedure.ResultSequence);
					
//				}
				if (StringUtils.isBlank(resultSequence))
					resultSequence = sparqlEntity.getValue(GlycanProcedure.Sequence);
				si.setResultSequence(resultSequence);

				String id = sparqlEntity.getValue(GlycanProcedure.AccessionNumber);
	    		
	    		if (StringUtils.isNotEmpty(resultSequence) && resultSequence.startsWith(GlycanProcedure.CouldNotConvertHeader)) {
	    			si.setId(GlycanProcedure.CouldNotConvert);
//	    			String imageSequence = si.getSequence().replaceAll("(?:\\r\\n|\\n)", "\\\\n");
	    			
	    			logger.debug("imageSequence:>" + imageSequence + "<");
					GlyspaceClient gsClient = new GlyspaceClient();
					try {
						si.setImage(gsClient.getImage("https://test.glytoucan.org", imageSequence));
					} catch (KeyManagementException | NoSuchAlgorithmException
							| KeyStoreException | IOException e) {
						redirectAttrs.addAttribute("errorMessage", "system error");
						logger.error(e.getMessage());
						e.printStackTrace();
						return "redirect:/Registries/index";
					}
					logger.debug("adding to error:>" + si);
					listErrors.add(si);
	    		} else if (StringUtils.isNotEmpty(id) && id.equals(GlycanProcedure.NotRegistered)) {
//	    			String imageSequence = si.getSequence().replaceAll("(?:\\r\\n|\\n)", "\\\\n");
	    			
	    			logger.debug("imageSequence:>" + imageSequence + "<");

					GlyspaceClient gsClient = new GlyspaceClient();
					try {
						logger.debug("glycoct to image:>" + si.getSequence() + "<");
						si.setImage(gsClient.getImage("https://test.glytoucan.org", imageSequence));
					} catch (KeyManagementException | NoSuchAlgorithmException
							| KeyStoreException | IOException e) {
						redirectAttrs.addFlashAttribute("errorMessage", "system error");
						logger.error(e.getMessage());
						e.printStackTrace();
						return "redirect:/Registries/index";
					}
					logger.debug("adding to new:>" + si);
					list.add(si);
	    		} else {
					si.setResultSequence(resultSequence);
	    			si.setId(id);
	    			si.setImage(sparqlEntity.getValue(GlycanProcedure.Image));
					logger.debug("adding to old:>" + si);
					listRegistered.add(si);
	    		}
	    		
	    		logger.debug(si);
			}

    		model.addAttribute("listRegistered", listRegistered);
    		model.addAttribute("listErrors", listErrors);
    		model.addAttribute("listNew", list);
            return "register/confirmation";
        }
    }
	
	@RequestMapping(value="/complete", method = RequestMethod.POST)
    @ApiOperation(value="Confirms the completion of a registration", 
	notes="Based on the previous /confirmation screen, the sequence array is processed and registered with the following logic:<br />"
			+ "1. generation of base rdf.glycoinfo.org classes such as Saccha￼ride <br />"
			+ "2. wurcsRDF generation.  create the WURCS RDF based on the wurcs input. <br />"
			+ "3. calculate the mass.<br />"
			+ "4. loop through motifs to register has_motif relationship.<br />"
			+ "5. calculate cardinality and generate Components.<br />"
			+ "6. for each monosaccharide in the glycan registered, create the monosaccharide alias.<br />"
			+ "RDF:<br />"
			+ "@PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> ."
			+ "@PREFIX glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#> ."
			+ "@PREFIX xsd:   <http://www.w3.org/2001/XMLSchema#> ."
			+ "<http://rdf.glycoinfo.org/glycan/G00054MO>"
			+ " a	glycan:saccharide ;"
			+ "	glycan:has_resource_entry"
			+ "		<http://www.glytoucan.org/Structures/Glycans/G00054MO> ."
			+ "		 * <http://rdf.glycoinfo.org/Structures/Glycans/e06b141de8d13adfa0c3ad180b9eae06>"
			+ "        a                          glycan:resource_entry ;"
			+ "        glycan:in_glycan_database  glytoucan:database_glytoucan ;"
			+ "        glytoucan:contributor      <http://rdf.glytoucan/contributor/userId/1> ;"
			+ "        glytoucan:date_registered  \"2014-10-20 06:47:31.204\"^^xsd:dateTimeStamp ."
			)
    public String complete(HttpServletRequest request) throws SparqlException  {
		
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (null != userInfo && userInfo.getVerifiedEmail()!=null && userInfo.getVerifiedEmail().equals("true") && StringUtils.isNotBlank(userInfo.getGivenName())) {
        	logger.debug("user is verified:>" + userInfo.getGivenName());
        	glycanProcedure.setContributor(userInfo.getGivenName());
        } else {
        	return "redirect:/signin?errorMessage=Please sign in with a verified email address.  Check Profile for details.";
        }
		
		String[] checked = request.getParameterValues("checked");
		logger.debug(Arrays.asList(checked));
		
//		[RES\n1b:x-dgro-dgal-NON-2:6|1:a|2:keto|3:d\n2b:x-dglc-HEX-1:5\n3s:n-acetyl\n4s:n-acetyl\nLIN\n1:1o(-1+1)2d\n2:2d(2+1)3n\n3:1d(5+1)4n, 
//		RES\n1b:x-dman-HEX-1:5\n2b:x-dgal-HEX-1:5\n3s:n-acetyl\nLIN\n1:1o(-1+1)2d\n2:2d(2+1)3n]
//		[on,on] 
		String[] resultSequence = request.getParameterValues("resultSequence");
		String[] origSequence = request.getParameterValues("sequence");
		String[] image = request.getParameterValues("image");
		ArrayList<String> registerList = new ArrayList<String>();
		ArrayList<String> origList = new ArrayList<String>();
		ArrayList<String> imageList = new ArrayList<String>();
		ArrayList<String> resultList = new ArrayList<String>();
		for (int i = 0; i < checked.length; i++) {
			String check = checked[i];
			if (StringUtils.isNotBlank(check) && check.equals("on")) {
				logger.debug("registering:" + resultSequence[i] + "<");
				String id = glycanProcedure.register(origSequence[i], resultSequence[i]);
				registerList.add(resultSequence[i]);
				origList.add(origSequence[i]);
				imageList.add(image[i]);
				resultList.add(id);
			}
		}
//		Map<String, String> results = null;
//		List<String> resultList = new ArrayList<String>();
//		if (!registerList.isEmpty()) {
//			 results = glycanProcedure.register(registerList);
//			 searchBySequence
//			 
////			 glycanProcedure.addGlycoSequence(origList);
//		}
//		for (String sequence: registerList) {
//			for (Iterator iterator = results.keySet().iterator(); iterator.hasNext();) {
//				String key = (String) iterator.next();
//				String registeredSequence = results.get(key);
//				if (registeredSequence.equals(sequence))
//					resultList.add(key);
//			}
//		}
				 
		request.setAttribute("registeredList", registerList);
		request.setAttribute("origList", origList);
		request.setAttribute("imageList", imageList);
		request.setAttribute("resultList", resultList);
		return "register/complete";
	}


	@RequestMapping("/upload")
	public String upload(Model model) {
		return "register/index";
	}
}