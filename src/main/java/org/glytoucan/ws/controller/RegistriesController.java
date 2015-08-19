package org.glytoucan.ws.controller;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.batch.glyconvert.wurcs.sparql.GlycoSequenceToWurcsSelectSparql;
import org.glycoinfo.conversion.util.DetectFormat;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.service.GlycanProcedure;
import org.glytoucan.ws.client.GlyspaceClient;
import org.glytoucan.ws.model.SequenceInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
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
    		SparqlEntity se;
    		List<SparqlEntity> searchResults;
    		try {
    			searchResults = glycanProcedure.search(inputs);
			} catch (SparqlException e) {
				redirectAttrs.addAttribute("errorMessage", "an error occurred");
				return "redirect:/Registries/index";
			}
    		
    		for (Iterator iterator = searchResults.iterator(); iterator
					.hasNext();) {
				SparqlEntity sparqlEntity = (SparqlEntity) iterator.next();

	    		SequenceInput si = new SequenceInput();
	    		
    			si.setSequence(sparqlEntity.getValue(GlycoSequenceToWurcsSelectSparql.FromSequence));

	    		String resultSequence = null;
//				try {
//					resultSequence = URLEncoder.encode(sparqlEntity.getValue(GlycoSequenceToWurcsSelectSparql.Sequence), "UTF-8");
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
					resultSequence = sparqlEntity.getValue(GlycoSequenceToWurcsSelectSparql.Sequence);
//				}
				si.setResultSequence(resultSequence);

				String id = sparqlEntity.getValue(GlycoSequenceToWurcsSelectSparql.AccessionNumber);
	    		
	    		if (StringUtils.isNotEmpty(resultSequence) && resultSequence.startsWith(GlycanProcedure.CouldNotConvertHeader)) {
	    			si.setId(GlycanProcedure.CouldNotConvert);
					GlyspaceClient gsClient = new GlyspaceClient();
					try {
						si.setImage(gsClient.getImage("https://test.glytoucan.org", si.getSequence()));
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

					GlyspaceClient gsClient = new GlyspaceClient();
					try {
						si.setImage(gsClient.getImage("https://test.glytoucan.org", si.getSequence()));
					} catch (KeyManagementException | NoSuchAlgorithmException
							| KeyStoreException | IOException e) {
						redirectAttrs.addAttribute("errorMessage", "system error");
						logger.error(e.getMessage());
						e.printStackTrace();
						return "redirect:/Registries/index";
					}
					list.add(si);
	    		} else {
	    			si.setId(id);
	    			si.setImage(sparqlEntity.getValue(GlycanProcedure.Image));
					listRegistered.add(si);
	    		}
			}
    		
    		model.addAttribute("listRegistered", listRegistered);
    		model.addAttribute("listErrors", listErrors);
    		model.addAttribute("listNew", list);
            return "register/confirmation";
        }
    }
	
	@RequestMapping("/complete")
    public String complete(Model model, @ModelAttribute("listNew") List<SequenceInput> listNew, BindingResult result, RedirectAttributes redirectAttrs) throws SparqlException  {
		logger.debug(listNew);
		logger.debug(model.asMap());
		return "register/complete";
	}


	@RequestMapping("/upload")
	public String upload(Model model) {
		return "register/index";
	}
}