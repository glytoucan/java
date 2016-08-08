package org.glytoucan.web.controller;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.convert.error.ConvertException;
import org.glycoinfo.convert.error.ConvertFormatException;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.service.GlycanProcedure;
import org.glytoucan.client.GlycoSequenceClient;
import org.glytoucan.client.config.GlycanQueryConfig;
import org.glytoucan.client.soap.GlycoSequenceDetailResponse;
import org.glytoucan.client.soap.GlycoSequenceSearchResponse;
import org.glytoucan.client.soap.ResponseMessage;
import org.glytoucan.model.spec.GlycanClientQuerySpec;
import org.glytoucan.web.model.SequenceInput;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.knappsack.swagger4springweb.annotation.ApiExclude;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Controller
@ApiExclude
@RequestMapping("/Structures")
@Import(GlycanQueryConfig.class)
public class StructuresController {
	Log logger = LogFactory.getLog(StructuresController.class);
	
//	@Autowired
//	GlycanProcedure glycanProcedure;
	
//	@Autowired
//	LogClient logClient;
	
	@Autowired
	GlycoSequenceClient glycoSequenceClient;
	
	@Autowired
	GlycanClientQuerySpec gtcClient;

	@RequestMapping()
	public String def(Model model) {
		return "structures/index";
	}
	
	@RequestMapping("/")
	public String root(Model model) {
		return "structures/index";
	}

	@RequestMapping("/index")
	public String index(Model model) {
		return "structures/index";
	}

	@RequestMapping("/graphical")
	public String graphical(Model model, HttpSession httpSession) {
		return "structures/graphical";
	}
	
	@RequestMapping(value="/structureSearch", method = RequestMethod.GET)
	public String structureSearch(Model model, @ModelAttribute("sequence") SequenceInput sequence, BindingResult result/*, @RequestParam(value="errorMessage", required=false) String errorMessage*/) {
//		logger.debug(errorMessage);
//		model.addAttribute("errorMessage", errorMessage);
		return "structures/structure_search";
	}
	
	@RequestMapping("/structure")
    @ApiOperation(value="Confirms the completion of a registration", 
	notes="Based on the previous /confirmation screen, the sequence array is processed and registered with the following logic:<br />"
			+ "1. generation of base rdf.glycoinfo.org classes such as Saccha￼ride <br />"
			+ "2. wurcsRDF generation.  create the WURCS RDF based on the wurcs input. <br />"
			+ "3. calculate the mass.<br />"
			+ "4. loop through motifs to register has_motif relationship.<br />"
			+ "5. calculate cardinality and generate Components.<br />"
			+ "6. for each monosaccharide in the glycan registered, create the monosaccharide alias.<br />")
	@ApiResponses(value ={@ApiResponse(code=200, message="Structure added successfully"),
			@ApiResponse(code=401, message="Unauthorized"),
			@ApiResponse(code=500, message="Internal Server Error")})
	public String structure(Model model, @ModelAttribute("sequence") SequenceInput sequence, BindingResult result, RedirectAttributes redirectAttrs, @RequestParam(required=false, value="errorMessage") String errorMessage)  {
		if (StringUtils.isNotBlank(errorMessage))
			model.addAttribute("errorMessage", errorMessage);
		logger.debug(sequence);
        if (StringUtils.isEmpty(sequence.getSequence())) {
        	
        	logger.debug("adding errorMessage:>input sequence<");
        	redirectAttrs.addFlashAttribute("errorMessage", "Please input a sequence");
        	return "redirect:/Structures/structureSearch";
        } else {
    		logger.debug("text1>" + sequence.getSequence() + "<");

//    		glycanProcedure.setSequence(sequence.getSequence());
    	    GlycoSequenceSearchResponse response = glycoSequenceClient.textSearchRequest(sequence.getSequence() );
    	    Assert.assertNotNull(response);
    	    
    	    logger.debug(response);
    	    logger.debug(response.getAccessionNumber());
    	    ResponseMessage rm = response.getResponseMessage();
    	    logger.debug(rm);
    	    logger.debug(rm.getErrorCode());
    	    if (BigInteger.ZERO.compareTo(rm.getErrorCode()) != 0) {
            logger.error("ResponseMessage error:>" + rm.getMessage());
            redirectAttrs.addFlashAttribute("errorMessage", rm.getMessage());
            return "redirect:/Structures/" + sequence.getFrom();
    	    }
    		String id = response.getAccessionNumber();
    		logger.debug("search found:>" + id + "<");
    		if (null != id && id.equals(GlycanProcedure.NotRegistered)) {
    		  sequence.setId(GlycanProcedure.NotRegistered);
				try {
	    			String imageSequence = sequence.getSequence().replaceAll("(?:\\r\\n|\\n)", "\\\\n");

	    			
	    	    HashMap<String, Object> data = new HashMap<String, Object>();
	    	    data.put(GlycanClientQuerySpec.IMAGE_FORMAT, "png");
	    	    data.put(GlycanClientQuerySpec.IMAGE_NOTATION, "cfg");
	    	    data.put(GlycanClientQuerySpec.IMAGE_STYLE, "extended");
	    	    data.put(GlycanClientQuerySpec.SEQUENCE, imageSequence); 
	    	    
					sequence.setImage(gtcClient.getImageBase64(data));
				} catch (KeyManagementException | NoSuchAlgorithmException
						| KeyStoreException | IOException e) {
					redirectAttrs.addFlashAttribute("errorMessage", "system error");
					logger.error(e.getMessage());
					e.printStackTrace();
					return "redirect:/Structures/"  + sequence.getFrom();
				}
    		} else {
    			sequence.setId(id);
    			sequence.setImage(response.getImage());
    		}
    		logger.debug("image:>" + sequence.getImage());
    		
    		String resultSequence = null;
			try {
//				resultSequence = URLEncoder.encode(se.getValue(GlycoSequenceToWurcsSelectSparql.Sequence), "UTF-8");
//				WURCS=2.0/2,2,1/[22112h-1a_1-5_2*NCC/3=O][12112h-1b_1-5]/1-2/a3-b1
				resultSequence = URLEncoder.encode(response.getSequence(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				resultSequence = response.getSequence();
			}
    		sequence.setResultSequence(resultSequence);
//    		sequence.setResultSequence(se.getValue(GlycoSequence.Sequence));
    		// embed wurcs, image, accNum into model
//    		model.addAttribute("id", sequence.getId());
//    		model.addAttribute("image", sequence.getImage());
//    		model.addAttribute("wurcs", sequence.getResultSequence());
    		
           return "structures/structure";
        }
    }
	
	@RequestMapping(value="/Entry/{accessionNumber}", method=RequestMethod.GET)
	public String glycans(@PathVariable String accessionNumber, Model model, RedirectAttributes redirectAttrs)  {
		try {
			if (StringUtils.isNotBlank(accessionNumber) && accessionNumber.startsWith("G")) {
//				logClient.insertDefaultLog("glycan entry page for " + accessionNumber + " requested.");
			    GlycoSequenceDetailResponse response = glycoSequenceClient.detailRequest(accessionNumber);
			    logger.debug(response.getDescription());
     			model.addAttribute("accNum", accessionNumber);
     			model.addAttribute("description", response.getDescription());
				return "structures/glycans";
			}
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttrs.addFlashAttribute("errorMessage", "Currently under maintence please try again in a few minutes");
			return "redirect:/";
		}
		redirectAttrs.addFlashAttribute("errorMessage", "This accession number does not exist");
		return "redirect:/";
    }

	@RequestMapping(value="/Accession", method=RequestMethod.POST)
	public String accession(@ModelAttribute("aNum") String aNum)  {
		
		return "redirect:/Structures/Glycans/" + aNum;
    }
	
	@RequestMapping(value="/test", method = RequestMethod.GET)
	public String test() {
		return "structures/test";
	}
	@RequestMapping(value="/testout", method = RequestMethod.GET)
	public String testout(RedirectAttributes redirectAttrs) {
		redirectAttrs.addAttribute("errorMessage", "error message");
		redirectAttrs.addFlashAttribute("errorMessage", "error message");
		return "redirect:/Structures/test";
	}

	@RequestMapping(value="/Glycans/{accessionNumber}", method=RequestMethod.GET)
	public String entry(@PathVariable String accessionNumber, Model model, RedirectAttributes redirectAttrs)  {
		try {
			if (StringUtils.isNotBlank(accessionNumber) && accessionNumber.startsWith("G")) {
//				logClient.insertDefaultLog("glycan entry page for " + accessionNumber + " requested.");
			    GlycoSequenceDetailResponse response = glycoSequenceClient.detailRequest(accessionNumber);
			    logger.debug(response.getDescription());
     			model.addAttribute("accNum", accessionNumber);
     			model.addAttribute("description", response.getDescription());
				return "structures/entry";
			}
		} catch (Exception e) {
			e.printStackTrace();
			redirectAttrs.addFlashAttribute("errorMessage", "Currently under maintence please try again in a few minutes");
			return "redirect:/";
		}
		redirectAttrs.addFlashAttribute("errorMessage", "This accession number does not exist");
		return "redirect:/";
    }
}
