package org.glytoucan.ws.controller;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.batch.glyconvert.wurcs.sparql.GlycoSequenceToWurcsSelectSparql;
import org.glycoinfo.conversion.error.ConvertException;
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

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Controller
@Api(value="/Structures", description="Structure Management")
@RequestMapping("/Structures")
public class StructuresController {
	Log logger = LogFactory.getLog(StructuresController.class);
	
	@Autowired
	GlycanProcedure glycanProcedure;

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
	public String structure(Model model, @ModelAttribute("sequence") SequenceInput sequence, BindingResult result, RedirectAttributes redirectAttrs)  {
		logger.debug(sequence);
        if (StringUtils.isEmpty(sequence.getSequence())) {
        	
        	logger.debug("adding errorMessage:>input sequence<");
        	redirectAttrs.addFlashAttribute("errorMessage", "Please input a sequence");
        	return "redirect:/Structures/structureSearch";
        } else {
    		logger.debug("text1>" + sequence.getSequence() + "<");

    		glycanProcedure.setSequence(sequence.getSequence());
    		SparqlEntity se;
    		try {
    			se = glycanProcedure.search();
    			
			} catch (SparqlException e) {
				redirectAttrs.addAttribute("errorMessage", "an error occurred");
				return "redirect:/Structures/structureSearch";
			} catch (ConvertException e) {
				redirectAttrs.addAttribute("errorMessage", "cannot convert this sequence");
				return "redirect:/Structures/structureSearch";
			}
    		
    		String id = se.getValue(GlycoSequenceToWurcsSelectSparql.AccessionNumber);
    		if (null != id && id.equals("not registered")) {
				sequence.setId("not registered");
				GlyspaceClient gsClient = new GlyspaceClient();
				try {
					sequence.setImage(gsClient.getImage("https://test.glytoucan.org", sequence.getSequence()));
				} catch (KeyManagementException | NoSuchAlgorithmException
						| KeyStoreException | IOException e) {
					redirectAttrs.addAttribute("errorMessage", "system error");
					logger.error(e.getMessage());
					e.printStackTrace();
					return "redirect:/Structures/structureSearch";
				}
    		} else {
    			sequence.setId(id);
    			sequence.setImage(se.getValue(GlycanProcedure.Image));
    		}
    		
    		String resultSequence = null;
			try {
//				resultSequence = URLEncoder.encode(se.getValue(GlycoSequenceToWurcsSelectSparql.Sequence), "UTF-8");
//				WURCS=2.0/2,2,1/[22112h-1a_1-5_2*NCC/3=O][12112h-1b_1-5]/1-2/a3-b1
				resultSequence = URLEncoder.encode("WURCS=2.0/2,2,1/[22112h-1a_1-5_2*NCC/3=O][12112h-1b_1-5]/1-2/a3-b1", "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				resultSequence = se.getValue(GlycoSequenceToWurcsSelectSparql.Sequence);
			}
    		sequence.setResultSequence(resultSequence);
    		// embed wurcs, image, accNum into model
//    		model.addAttribute("id", sequence.getId());
//    		model.addAttribute("image", sequence.getImage());
//    		model.addAttribute("wurcs", sequence.getResultSequence());
            return "structures/structure";
        }
    }
	
	@RequestMapping("/test")
    public String test(RedirectAttributes redirectAttrs) {
		redirectAttrs.addFlashAttribute("test", "why not");
		return "redirect:/Structures/testout";
	}
	
	@RequestMapping(value="/testout", method = RequestMethod.GET)
	public String testout() {
		return "structures/test";
	}
}