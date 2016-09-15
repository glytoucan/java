package org.glytoucan.web.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.convert.util.DetectFormat;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.Contributor;
import org.glycoinfo.rdf.service.ContributorProcedure;
import org.glycoinfo.rdf.service.GlycanProcedure;
import org.glycoinfo.rdf.service.exception.ContributorException;
import org.glycoinfo.vision.generator.ImageGenerator;
import org.glytoucan.admin.client.UserClient;
import org.glytoucan.admin.model.Authentication;
import org.glytoucan.admin.model.User;
import org.glytoucan.admin.model.UserDetailsRequest;
import org.glytoucan.admin.model.UserDetailsResponse;
import org.glytoucan.model.spec.GlycanClientQuerySpec;
import org.glytoucan.web.model.SequenceInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.fromi.openidconnect.security.UserInfo;
import com.knappsack.swagger4springweb.annotation.ApiExclude;
import com.wordnik.swagger.annotations.ApiOperation;

@Controller
@ApiExclude
@RequestMapping("/Registries")
public class RegistriesController {
	Log logger = LogFactory.getLog(RegistriesController.class);

	@Autowired
	GlycanProcedure glycanProcedure;

	@Autowired
	ImageGenerator imageGenerator;

	@Autowired
	ContributorProcedure contributorProcedure;
	
	@Autowired
	GlycanClientQuerySpec gtcClient;
	
	@Autowired
	UserClient userClient;

	@RequestMapping("/graphical")
	public String graphical(Model model, RedirectAttributes redirectAttrs) {
		return "register/graphical";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String root(Model model,
			@ModelAttribute("sequence") SequenceInput sequence,
			BindingResult result/*
								 * , @RequestParam(value="errorMessage",
								 * required=false) String errorMessage
								 */) {
		return "register/index";
	}

	@RequestMapping("/confirmation")
  public String confirmation(HttpServletRequest request, RedirectAttributes redirectAttrs) throws SparqlException {
    String sequence1 = request.getParameter("sequence");
    
    if (StringUtils.isEmpty(sequence1)) {
      logger.debug("no input sequence");
      redirectAttrs.addFlashAttribute("errorMessage",
          "Please input a sequence");
      return "redirect:/Registries/index";
    }
    
    logger.debug("sequence1>"+sequence1);

    String[] sequence2 = request.getParameterValues("sequence-2");

    
    SequenceInput sequenceInput = new SequenceInput();
    String sequence=sequence1.trim();

    if (null != sequence2) {
      for (int i = 0; i < sequence2.length; i++) {
        logger.debug("sequence2>"+sequence2[i]);
        sequence += "\r\n";
        sequence += sequence2[i].trim();
      }
    }
    logger.debug("input sequences:>"+sequence);
    
    sequenceInput.setFrom(sequence);
    sequenceInput.setSequence(sequence);

    List<SequenceInput> list = new ArrayList<SequenceInput>();
    List<SequenceInput> listRegistered = new ArrayList<SequenceInput>();
    List<SequenceInput> listErrors = new ArrayList<SequenceInput>();

    
    String result = processMultiple(sequenceInput, list, listRegistered,
        listErrors, "/Registries/index", "register/confirmation",
        redirectAttrs);

    request.setAttribute("listRegistered", listRegistered);
    request.setAttribute("listErrors", listErrors);
    request.setAttribute("listNew", list);

    return result;
  }

	private String processMultiple(SequenceInput sequence,
			List<SequenceInput> list, List<SequenceInput> listRegistered,
			List<SequenceInput> listErrors, String error, String complete,
			RedirectAttributes redirectAttrs) {
		if (StringUtils.isEmpty(sequence.getSequence())) {

			logger.debug("adding errorMessage:>input sequence<");
			redirectAttrs.addFlashAttribute("errorMessage",
					"Please input a sequence");
			return "redirect:" + error;
		} else {
			logger.debug("text1>" + sequence.getSequence() + "<");
			try {
				logger.debug("text1encoded>"
						+ URLEncoder.encode(sequence.getSequence(), "UTF-8")
						+ "<");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			List<String> inputs = DetectFormat.split(sequence.getSequence());
			logger.debug("split input:>" + inputs + "<");
			SparqlEntity se;
			List<SparqlEntity> searchResults;
			try {
				searchResults = glycanProcedure.search(inputs);
			} catch (SparqlException e) {
				redirectAttrs.addFlashAttribute("errorMessage", e.getMessage());
				return "redirect:" + error;
			}

			logger.debug("search results:>" + searchResults + "<");
//      String imageSequence = null, imageString=null;
      String resultSequence = null;

			for (Iterator<SparqlEntity> iterator = searchResults.iterator(); iterator
					.hasNext();) {
				SparqlEntity sparqlEntity = (SparqlEntity) iterator.next();

				sparqlEntity.getValue(GlycanProcedure.ResultSequence);
				
				// time to fill in this result sequence input
				SequenceInput si = new SequenceInput();

        resultSequence = sparqlEntity
            .getValue(GlycanProcedure.ResultSequence);
        if (StringUtils.isBlank(resultSequence) || resultSequence
            .startsWith(GlycanProcedure.CouldNotConvertHeader))
          resultSequence = sparqlEntity
              .getValue(GlycanProcedure.Sequence);
        si.setResultSequence(resultSequence);
        // if the result is blank, or most likely could not be converted, still try to get an image using the original...
        if (StringUtils.isNotBlank(resultSequence) && !resultSequence
            .startsWith(GlycanProcedure.CouldNotConvertHeader)) {
          logger.debug("imageSequence:>" + resultSequence + "<");
          // generate an image, wurcs.  If this fails, blow up back to user showing the weird sequence
          try {
            si.setImage(convertSequenceToImage(resultSequence));
          } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | IOException e1) {
            redirectAttrs.addFlashAttribute("errorMessage",
                "system error:" + resultSequence);
            logger.error(e1.getMessage());
            return "redirect:" + error;
          }
        } else
          si.setImage("");
				
        String fromSequence = sparqlEntity
            .getValue(GlycanProcedure.FromSequence);
        si.setSequence(fromSequence);
        
				String id = sparqlEntity
						.getValue(GlycanProcedure.AccessionNumber);

				if (StringUtils.isNotEmpty(resultSequence)
						&& resultSequence
								.startsWith(GlycanProcedure.CouldNotConvertHeader)) {
					si.setId(GlycanProcedure.CouldNotConvert);
					logger.debug("adding to error:>" + si);
					listErrors.add(si);
				} else if (StringUtils.isNotEmpty(id)
						&& id.equals(GlycanProcedure.NotRegistered)) {
					logger.debug("adding to new:>" + si);
					list.add(si);
				} else {
				  // registered
					si.setId(id);
					si.setImage(sparqlEntity.getValue(GlycanProcedure.Image));
					logger.debug("adding to old:>" + si);
					listRegistered.add(si);
				}

				logger.debug(si);
			}

			return "register/confirmation";
		}
	}

	private String convertSequenceToImage(String imageSequence)
			throws KeyManagementException, NoSuchAlgorithmException,
			KeyStoreException, IOException {
	  HashMap<String, Object> data = new HashMap<String, Object>();
	  data.put(GlycanClientQuerySpec.IMAGE_FORMAT, "png");
	  data.put(GlycanClientQuerySpec.IMAGE_NOTATION, "cfg");
	  data.put(GlycanClientQuerySpec.IMAGE_STYLE, "extended");
	  data.put(GlycanClientQuerySpec.SEQUENCE, imageSequence); 
	  
		return gtcClient.getImageBase64(data);
	}

	@RequestMapping(value = "/complete", method = RequestMethod.POST)
	@ApiOperation(value = "Confirms the completion of a registration", notes = "Based on the previous /confirmation screen, the sequence array is processed and registered with the following logic:<br />"
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
			+ "        glytoucan:date_registered  \"2014-10-20 06:47:31.204\"^^xsd:dateTimeStamp .")
	public String complete(HttpServletRequest request, RedirectAttributes redirectAttrs) {

		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String userId = null;
		if (null != userInfo && userInfo.getVerifiedEmail() != null
				&& userInfo.getVerifiedEmail().equals("true")
				&& StringUtils.isNotBlank(userInfo.getGivenName())) {
			logger.debug("user is verified:>" + userInfo.getGivenName());
		  SparqlEntity seUserId;
		try {
			seUserId = contributorProcedure.searchContributor(userInfo.getGivenName());
		} catch (ContributorException e) {
			return "redirect:/signin?errorMessage=Please sign in with a verified email address.";
		}
		  if (null != seUserId)
		    userId = seUserId.getValue(Contributor.ID);
		  if (null == userId) {
		    
		    
		    // there is a chance the user modified the given name so contributor id could not be retrieved.
//		    try {
//				userId = userProcedure.getIdByEmail(userInfo.getEmail());
//			} catch (UserException e) {
//				return "redirect:/signin?errorMessage=Please sign in with a verified email address.";
//			}
		    
		     OAuth2AccessToken token = (OAuth2AccessToken)SecurityContextHolder.getContext().getAuthentication().getCredentials();
		      logger.debug("token:>" + token.getValue());
		      
//		      SparqlEntity userData = null;
//		      try {
		        UserDetailsRequest req = new UserDetailsRequest();
		        Authentication auth = new Authentication();
		        auth.setId(userInfo.getEmail());
		        auth.setId(token.getValue());
		        req.setAuthentication(auth);
		        UserDetailsResponse response =  userClient.userDetailsRequest(req);
//		        userData = userProcedure.getIdByEmail(userInfo.getEmail());
		        if (null == response || null == response.getUser()) {
		          redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
		          return "redirect:/signout";
		        }
		        User user = response.getUser();
//		      };
		      if (null == user.getGivenName()) {
		        redirectAttrs.addAttribute("warningMessage", "Could not retrieve user information.  Please Login");
		        return "redirect:/signout";
		      }
		  }
		} else {
			return "redirect:/signin?errorMessage=Please sign in with a verified email address or check our Given Name on Google Accounts.  Please refer to Profile page for details.";
		}

		String[] checked = request.getParameterValues("checked");
		logger.debug(Arrays.asList(checked));

		// [RES\n1b:x-dgro-dgal-NON-2:6|1:a|2:keto|3:d\n2b:x-dglc-HEX-1:5\n3s:n-acetyl\n4s:n-acetyl\nLIN\n1:1o(-1+1)2d\n2:2d(2+1)3n\n3:1d(5+1)4n,
		// RES\n1b:x-dman-HEX-1:5\n2b:x-dgal-HEX-1:5\n3s:n-acetyl\nLIN\n1:1o(-1+1)2d\n2:2d(2+1)3n]
		// [on,on]
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
				String id;
				try {
					id = glycanProcedure.register(origSequence[i], userId);
				} catch (SparqlException e) {
					return "redirect:/signin?errorMessage=There was a problem with a structure.  Details:" + e.getMessage();
				}
				registerList.add(resultSequence[i]);
				origList.add(origSequence[i]);
				imageList.add(image[i]);
				resultList.add(id);
			}
		}
		// Map<String, String> results = null;
		// List<String> resultList = new ArrayList<String>();
		// if (!registerList.isEmpty()) {
		// results = glycanProcedure.register(registerList);
		// searchBySequence
		//
		// // glycanProcedure.addGlycoSequence(origList);
		// }
		// for (String sequence: registerList) {
		// for (Iterator iterator = results.keySet().iterator();
		// iterator.hasNext();) {
		// String key = (String) iterator.next();
		// String registeredSequence = results.get(key);
		// if (registeredSequence.equals(sequence))
		// resultList.add(key);
		// }
		// }

		request.setAttribute("registeredList", registerList);
		request.setAttribute("origList", origList);
		request.setAttribute("imageList", imageList);
		request.setAttribute("resultList", resultList);
		return "register/complete";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String upload(Model model) {
		return "register/upload";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String handleFileUpload(Model model,
			@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttrs) {
		if (!file.isEmpty()) {
			try {
				File newFile = new File(file.getName());
				byte[] bytes = file.getBytes();
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(newFile));
				stream.write(bytes);
				stream.close();

				String sequenceFile = new String(Files.readAllBytes(Paths
						.get(newFile.getPath())));
				SequenceInput sequence = new SequenceInput();
				sequence.setSequence(sequenceFile);

				List<SequenceInput> list = new ArrayList<SequenceInput>();
				List<SequenceInput> listRegistered = new ArrayList<SequenceInput>();
				List<SequenceInput> listErrors = new ArrayList<SequenceInput>();

				String result = processMultiple(sequence, list, listRegistered,
						listErrors, "/Registries/upload",
						"register/confirmation", redirectAttrs);

				model.addAttribute("listRegistered", listRegistered);
				model.addAttribute("listErrors", listErrors);
				model.addAttribute("listNew", list);

				return result;
			} catch (Exception e) {
				redirectAttrs.addFlashAttribute("errorMessage", "system error");
				logger.error(e.getMessage());
				e.printStackTrace();
				return "redirect:/Registries/upload";
			}
		} else {
			redirectAttrs.addFlashAttribute("errorMessage",
					"You failed to upload " + file.getName()
							+ " because the file was empty.");
			return "redirect:/Registries/upload";
		}
	}

	/**
	 * Encode image to string
	 * 
	 * @param image
	 *            The image to encode
	 * @param type
	 *            jpeg, bmp, ...
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

	@RequestMapping("/download")
	public void download(HttpServletRequest request,
			HttpServletResponse response) throws SparqlException {
		String[] listRegisteredId = request.getParameterValues("registeredId");
		String[] listRegisteredSequence = request.getParameterValues("registeredSequence");
		String[] listRegisteredResult = request.getParameterValues("registeredResultSequence");
		String[] listSequence = request.getParameterValues("sequence");
		String[] listResultSequence = request.getParameterValues("resultSequence");
		String[] listErrorSequence = request.getParameterValues("errorSequence");
		String[] listErrorResultSequence = request.getParameterValues("errorResultSequence");
		response.setContentType("text/csv");

		String csvFileName = "glytoucanDownload.csv";
		// creates mock data
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"",
				csvFileName);
		response.setHeader(headerKey, headerValue);

		try {
			boolean idExists = false;
			if (null != listRegisteredId) {
				idExists = true;
				response.getWriter().append("id,");
			}
			if ((null != listRegisteredSequence) || (null != listSequence) || (null != listErrorSequence)) {
				response.getWriter().append("sequence");
			}
			if ((null != listRegisteredResult) || (null != listResultSequence) || (null != listErrorResultSequence)) {
				response.getWriter().append(",result sequence\r\n");
			}
			if (null != listRegisteredId) {
				for (int i = 0; i < listRegisteredId.length; i++) {
					response.getWriter().append("\"" + listRegisteredId[i] + "\",");
					response.getWriter().append("\"" + listRegisteredSequence[i] + "\",");
					response.getWriter().append("\"" + listRegisteredResult[i] + "\"\r\n");
				}
			}
			if (null != listSequence) {
				for (int i = 0; i < listSequence.length; i++) {
					if (idExists)
						response.getWriter().append("\"\",");
					response.getWriter().append("\"" + listSequence[i] + "\",");
					response.getWriter().append("\"" + listResultSequence[i] + "\"\r\n");
				}
			}
			if (null != listErrorSequence) {
				for (int i = 0; i < listErrorSequence.length; i++) {
					if (idExists)
						response.getWriter().append("\"\",");
					response.getWriter().append("\"" + listErrorSequence[i] + "\",");
					response.getWriter().append("\"" + listErrorResultSequence[i] + "\"\r\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}
}