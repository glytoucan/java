package org.glytoucan.web.controller;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eurocarbdb.MolecularFramework.io.CarbohydrateSequenceEncoding;
import org.eurocarbdb.MolecularFramework.io.SugarImporter;
import org.eurocarbdb.MolecularFramework.io.SugarImporterException;
import org.eurocarbdb.MolecularFramework.io.SugarImporterFactory;
import org.eurocarbdb.MolecularFramework.io.GlycoCT.SugarImporterGlycoCTCondensed;
import org.eurocarbdb.MolecularFramework.sugar.Sugar;
import org.eurocarbdb.MolecularFramework.util.validation.GlycoVisitorValidation;
import org.eurocarbdb.MolecularFramework.util.validation.StructureParserValidator;
import org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitorException;
import org.eurocarbdb.resourcesdb.io.MonosaccharideConverter;
import org.glycoinfo.batch.search.wurcs.SubstructureSearchSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.SparqlEntityFactory;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.glycoinfo.rdf.service.GlycanProcedure;
import org.glycoinfo.vision.generator.ImageGenerator;
import org.glycoinfo.vision.importers.GWSImporter;
import org.glytoucan.model.spec.GlycanClientQuerySpec;
import org.glytoucan.web.api.Confirmation;
import org.glytoucan.web.api.Glycan;
import org.glytoucan.web.api.GlycanInput;
import org.glytoucan.web.api.GlycanList;
import org.glytoucan.web.api.GlycanResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
/**
 * @author aoki
 *

{"apiVersion":"v1","swaggerVersion":"1.2","apis":[{"path":"/doc/Tree","description":"D3 Tree format","position":0},{"path":"/doc/glycans","description":"Structure Management","position":0},{"path":"/doc/Registries","description":null,"position":0},{"path":"/doc/Preferences","description":null,"position":0},{"path":"/doc/Structures","description":null,"position":0},{"path":"/doc/org.glytoucan.ws.controller.WelcomeController","description":null,"position":0},{"path":"/doc/org.glytoucan.ws.controller.ViewController","description":null,"position":0},{"path":"/doc/Users","description":null,"position":0},{"path":"/doc/Motifs","description":null,"position":0},{"path":"/doc/glycosequence","description":"GlycoSequence-related interfaces","position":0}],"authorizations":null,"info":null}

 * This work is licensed under the Creative Commons Attribution 4.0 International License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by/4.0/.
 *
 */
@Controller
@Api(value="/glycans", description="Structure Management")
@RequestMapping ("/glycans")
public class GlycanController {
	
	private static final Log logger = LogFactory
			.getLog(GlycanController.class);

	@Autowired
	SubstructureSearchSparql substructureSearchSparql;
	
	@Autowired
	GlycanProcedure glycanProcedure;
//	
//	@Autowired
//	EmailManager emailManager;
//	
//	@Autowired
//	UserProcedure userManager;
//	
	@Autowired
	ImageGenerator imageGenerator;
//	
//	@Autowired
//	MassCalculator massCalculator;
	
//	@Autowired
//	MonosaccharideConversion residueTranslator;
	
	@Autowired
	MonosaccharideConverter monosaccharideConverter;
	
//	@Value("${documentation.services.basePath}")
	String serverBasePath;
	
	 @Autowired
	  GlycanClientQuerySpec gtcClient;
	
//	public void setGlycanManager(GlycanManager glycanManager) {
//		this.glycanManager = glycanManager;
//	}
	
//	private RDFGeneratorGlycanConfig createRDFConfiguration() {
//		RDFGeneratorGlycanConfig t_config = new RDFGeneratorGlycanConfig();
//        t_config.setReferencedCompound(false);
//        t_config.setImages(true);
//        t_config.setRemoteEntries(true);
//        t_config.setSequenceGlycoCt(true);
//        t_config.setSequenceGlydeII(false);
//        t_config.setSequenceKCF(false);
//        t_config.setSequenceLinucs(false);
//        t_config.setComposition(true);
//        t_config.setSequenceCarbBank(false);
//        t_config.setMotif(true);
//        t_config.setFlatReferencedCompound(false);
//        t_config.setFlatSequence(false);
//        return t_config;
//	}
	

	public boolean validateSequence (String sequence, String format) throws SugarImporterException, GlycoVisitorException {
		logger.debug("Input structure: {" + sequence + "}");
		// assume GlycoCT encoding
		Sugar sugarStructure = StructureParserValidator.parse(sequence);
		
		
		if (StructureParserValidator.isValid(sugarStructure)) {		
			return true;
		} else {
			throw new IllegalArgumentException("Validation error, please submit a valid structure");
		}
}
	
	public Sugar importParseValidate (GlycanInput glycan) throws GlycoVisitorException, SugarImporterException {
		String encoding = glycan.getFormat();
		logger.debug("Input structure: {" + glycan.getSequence() + "}");
		Sugar sugarStructure = null;
		if (encoding != null && !encoding.isEmpty() && !(encoding.equalsIgnoreCase("glycoct") || encoding.equalsIgnoreCase("glycoct_condensed"))  && !encoding.equalsIgnoreCase("gws")) {
			logger.debug("Converting from {" + encoding +"}");
			ArrayList<CarbohydrateSequenceEncoding> supported = SugarImporterFactory.getSupportedEncodings();
			for (Iterator<CarbohydrateSequenceEncoding> iterator = supported.iterator(); iterator.hasNext();) {
				CarbohydrateSequenceEncoding carbohydrateSequenceEncoding = (CarbohydrateSequenceEncoding) iterator
						.next();
				if (encoding.equalsIgnoreCase(carbohydrateSequenceEncoding.getId())) {	
					try {
						if (encoding.equalsIgnoreCase("kcf")) {
//							sugarStructure = SugarImporterFactory.importSugar(glycan.getSequence(), carbohydrateSequenceEncoding, residueTranslator);
						}
						else {
							sugarStructure = SugarImporterFactory.importSugar(glycan.getSequence(), carbohydrateSequenceEncoding, monosaccharideConverter);
						}
					} catch (Exception e) {
						// import failed
						String message=e.getMessage();
						//e.printStackTrace();
						if (e instanceof SugarImporterException) {
							message = ((SugarImporterException)e).getErrorText() + ": " + ((SugarImporterException)e).getPosition();
						}
						throw new IllegalArgumentException("Structure cannot be imported: " + message);
					}
					break;
				}
			}
			if (sugarStructure == null && !encoding.equalsIgnoreCase("gws")) {
				//encoding is not supported
				throw new IllegalArgumentException("Encoding " + encoding + " is not supported");
			}
		} 
		else {
			String structure;
			if (encoding != null && encoding.equalsIgnoreCase("gws")) { // glycoworkbench encoding
				structure = new GWSImporter().parse(glycan.getSequence());
				//logger.debug("converted from gws:  {}", structure);
			} else {
				// assume GlycoCT encoding
				structure = glycan.getSequence();
			}
			sugarStructure = StructureParserValidator.parse(structure);
		}
		
		
		if (StructureParserValidator.isValid(sugarStructure)) {		
			return sugarStructure;
		} else {
			throw new IllegalArgumentException("Validation error, please submit a valid structure");
		}
	}
//	
//	private void getCompositions (GlycanExhibit glycan, Set<GlycanComposition> compositions) {
//		for (Iterator iterator = compositions.iterator(); iterator.hasNext();) {
//			GlycanComposition glycanComposition = (GlycanComposition) iterator
//					.next();
//			String compName = glycanComposition.getComposition().getName();
//			int count = glycanComposition.getCount();
//			switch (compName) {
//				case "Fuc": glycan.setNumberOfFuc(count); 
//							break;
//				case "Gal": glycan.setNumberOfGal(count);
//							break;
//				case "GalA" : glycan.setNumberOfGalA(count);
//							break;
//				case "GalN" : glycan.setNumberOfGalN(count);
//							break;
//				case "GalNAc" : glycan.setNumberOfGalNAc(count);
//							break;
//				case "Glc" : glycan.setNumberOfGlc(count);
//							break;
//				case "GlcA" : glycan.setNumberOfGlcA(count);
//							break;
//				case "GlcN" : glycan.setNumberOfGlcN(count);
//							break;
//				case "GlcNAc" : glycan.setNumberOfGlcNAc(count);
//							break;
//				case "Kdn" : glycan.setNumberOfKdn(count);
//							break;
//				case "Man" : glycan.setNumberOfMan(count);
//							break;
//				case "ManA" : glycan.setNumberOfManA(count);
//							break;
//				case "ManN" : glycan.setNumberOfManN(count);
//							break;
//				case "ManNAc" : glycan.setNumberOfManNAc(count);
//							break;
//				case "NeuAc" : glycan.setNumberOfNeuAc(count);
//							break;
//				case "NeuGc" : glycan.setNumberOfNeuGc(count);
//							break;
//				case "Xly" : glycan.setNumberOfXyl(count);
//							break;
//				case "IdoA" : glycan.setNumberOfIdoA(count);
//							break;
//			}
//		}
//	}
	
	@RequestMapping(value = "/add/sequence", method = RequestMethod.POST, consumes={"application/xml", "application/json"}, produces={"application/json"})
    @ApiOperation(value="Adds a glycan structure to the system, returns the assigned glycan identifier", 
    			response=GlycanResponse.class, notes="Only currently logged in user can submit a structure, the returned object contains the accession number assigned or the already existing one.  Currently only wurcs or glycoct are supported.  After some simple validation of the string, the mass is calculated.  Substructure search of Motifs are then executed.  Finally the sequence is converted into wurcs RDF format and added into the ontology.")
    @ApiResponses(value ={@ApiResponse(code=201, message="Structure added successfully"),
    		@ApiResponse(code=400, message="Illegal argument - Glycan should be valid"),
    		@ApiResponse(code=401, message="Unauthorized"),
    		@ApiResponse(code=415, message="Media type is not supported"),
    		@ApiResponse(code=500, message="Internal Server Error")})
	public ResponseEntity<GlycanResponse> submitStructure (
		    @ApiParam(required=true, value="Glycan") 
		    @RequestBody (required=true)
		    @Valid GlycanInput glycan,
		    Principal p) throws Exception {

		String userName = p.getName();
		logger.debug("begin import ParseValidate");
		if (glycan.getFormat().equals("glycoct")) {
			Sugar sugarStructure = importParseValidate(glycan);
		}
		logger.debug("end import ParseValidate");
		
		String exportedStructure = null;
		Double mass=null;
		boolean massError = false;
		Exception massException=null;
		
//		if (sugarStructure == null) {
//			throw new IllegalArgumentException("Structure cannot be imported");
//		}
		
		// export into GlycoCT to make sure we have a uniform structure content in the DB
		try {
			logger.debug("begin exportStructure");
//			exportedStructure = StructureParserValidator.exportStructure(sugarStructure);
//			logger.debug("exported Structure: {}", exportedStructure);
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot export into common encoding: " + e.getMessage());
		}
//		try {
//			mass = massCalculator.calculateMass(sugarStructure);
//			if (mass != null && mass <= 0) {
//				// could not calculate mass
//				logger.info("Could not calculate mass for the structure!");
//				mass = null;
//			}
//		} catch (Exception e) {
//			// failed to calculate mass
//			massError = true;
//			massException = e;
//			mass = null;
//		}
		
		GlycanResponse response = null;
//		try {

//			response = glycanManager.register(exportedStructure, userName);
			if (response.getExisting()) {
				logger.debug("GLYCAN_EXISTS:" + response.getAccessionNumber() + userName);
			} else {
//				if (response.getQuotaExceeded()) {
//					// send email to the moderator
//					List<User> moderators = userManager.getModerators();
//					emailManager.sendUserQuotaAlert(moderators, userName);
//					throw new UserQuotaExceededException("Cannot add the glycan, user's quota exceeded. Please contact the Administrator");
//				}
				logger.info ("GLYCAN_ADD,{},{}" + response.getAccessionNumber() + userName);
			}
//		} catch (DataIntegrityViolationException e) {
//			// failed to add, need a new accession number
//			boolean exception = true;
//			do {
//				logger.info("Duplicate accession number retrying." +  e.getMessage());
//				try {
////					response = glycanManager.assignNewAccessionNumber(exportedStructure, userName);
//					exception = false;
//				} catch (DataIntegrityViolationException ex) {
//					exception = true;
//				}
//			} while (exception);
//		} 
//		if (massError) {
//			logger.info("Mass calculation exception occured for glycan {}. Reason: {}" + response.getAccessionNumber() + massException);
//		}

		return new ResponseEntity<GlycanResponse> (response, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/{accessionNumber}/delete", method = RequestMethod.DELETE)
    @ApiOperation(value="Deletes the glycan with given accession number", response=Confirmation.class, notes="This can be accessed by the Administrator user only")
    @ApiResponses (value ={@ApiResponse(code=200, message="Glycan deleted successfully"), 
    		@ApiResponse(code=401, message="Unauthorized"),
    		@ApiResponse(code=403, message="Not enough privileges to delete glycans"),
    		@ApiResponse(code=404, message="Glycan does not exist"),
    		@ApiResponse(code=500, message="Internal Server Error")})
	public @ResponseBody Confirmation deleteGlycan (@ApiParam(required=true, value="accession number of the glycan to be deleted") @PathVariable("accessionNumber") String accessionNumber) {
//		glycanManager.deleteByAccessionNumber(accessionNumber);
		return new Confirmation("Glycan deleted successfully", HttpStatus.OK.value());
	}

	@RequestMapping(value = "/{accessionNumber}", method = RequestMethod.GET, produces={"application/xml", "application/json"})
	@ApiOperation(value="Retrieves glycan by accession number", response=Glycan.class)
	@ApiResponses (value ={@ApiResponse(code=200, message="Success"),
			@ApiResponse(code=404, message="Glycan does not exist"),
			@ApiResponse(code=500, message="Internal Server Error")})
	public @ResponseBody Glycan getGlycan (
			@ApiParam(required=true, value="id of the glycan") @PathVariable("accessionNumber") String accessionNumber) throws SparqlException, ParseException {
		logger.debug("Get glycan");
    	SparqlEntity se = new SparqlEntity();

		se.setValue(Saccharide.PrimaryId, accessionNumber);

		SparqlEntity sparqlEntity = glycanProcedure.searchByAccessionNumber(accessionNumber);
		Glycan glycan = new Glycan();
		glycan.setAccessionNumber(accessionNumber);
		glycan.setContributor(sparqlEntity.getValue("Contributor"));
		logger.debug(sparqlEntity.getValue("DateRegistered"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.ENGLISH);
		Date date = sdf.parse(sparqlEntity.getValue("DateRegistered"));
		glycan.setDateEntered(date);
		glycan.setMass(Double.valueOf(sparqlEntity.getValue("Mass")));
		glycan.setStructure(sparqlEntity.getValue("GlycoCTSequence") + "\n");
		glycan.setStructureLength(glycan.getStructure().length());

		return glycan;
	}
	
    @RequestMapping(value="/{accessionNumber}/image", method=RequestMethod.GET, produces={MediaType.IMAGE_PNG_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.IMAGE_JPEG_VALUE})
    @ApiOperation(value="Retrieves glycan image by accession number", response=Byte[].class)
	@ApiResponses (value ={@ApiResponse(code=200, message="Success"),
			@ApiResponse(code=400, message="Illegal argument"),
			@ApiResponse(code=404, message="Glycan does not exist"),
			@ApiResponse(code=500, message="Internal Server Error")})
    public @ResponseBody ResponseEntity<byte[]> getGlycanImage (
    		@ApiParam(required=true, value="id of the glycan") 
    		@PathVariable("accessionNumber") 
    		String accessionNumber,
    		@ApiParam(required=false, value="format of the the glycan image", defaultValue="png") 
    		@RequestParam("format") 
    		String format,
    		@ApiParam(required=false, value="notation to use to generate the image", defaultValue="cfg") 
    		@RequestParam("notation") 
    		String notation,
    		@ApiParam(required=false, value="style of the image", defaultValue="compact") 
    		@RequestParam("style") 
    		String style
    		) throws Exception {
      
      
      HashMap<String, Object> data = new HashMap<String, Object>();
      data.put(GlycanClientQuerySpec.IMAGE_FORMAT, format);
      data.put(GlycanClientQuerySpec.IMAGE_NOTATION, notation);
      data.put(GlycanClientQuerySpec.IMAGE_STYLE, style);
      data.put(GlycanClientQuerySpec.ID, accessionNumber);
      byte[] bytes = gtcClient.getImage(data);
      
		HttpHeaders headers = new HttpHeaders();
    	if (format == null || format.equalsIgnoreCase("png")) {    
		    headers.setContentType(MediaType.IMAGE_PNG);
    	} else if (format.equalsIgnoreCase("svg")) {
		    headers.setContentType(MediaType.APPLICATION_XML);
    	} else if (format.equalsIgnoreCase("jpg") || format.equalsIgnoreCase("jpeg")) {
    		headers.setContentType(MediaType.IMAGE_JPEG);
    	}
    	
      int noOfDays = 3650; // 10 years
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
      Date date = calendar.getTime();
      headers.setExpires(date.getTime());
      logger.debug("expires on :>" + date.getTime() + "<");
    	
      return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
    }
    
    @RequestMapping(value="/image/glycan", method=RequestMethod.POST, consumes={"application/xml", "application/json"}, produces={MediaType.IMAGE_PNG_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.IMAGE_JPEG_VALUE})
    @ApiOperation(value="Retrieves glycan image by accession number", response=Byte[].class)
	@ApiResponses (value ={@ApiResponse(code=200, message="Success"),
			@ApiResponse(code=400, message="Illegal argument"),
			@ApiResponse(code=404, message="Glycan does not exist"),
			@ApiResponse(code=415, message="Media type is not supported"),
			@ApiResponse(code=500, message="Internal Server Error")})
    public @ResponseBody ResponseEntity<byte[]> getGlycanImageByStructure (
    		@RequestBody (required=true)
			@ApiParam(required=true, value="Glycan") 
			@Valid
			GlycanInput glycan,
    		@ApiParam(required=false, value="format of the the glycan image", defaultValue="png") 
    		@RequestParam("format") 
    		String format,
    		@ApiParam(required=false, value="notation to use to generate the image", defaultValue="cfg") 
    		@RequestParam("notation") 
    		String notation,
    		@ApiParam(required=false, value="style of the image", defaultValue="compact") 
    		@RequestParam("style") 
    		String style
    		) throws Exception {
    	
    	Sugar sugarStructure = importParseValidate(glycan);
    	if (sugarStructure == null) {
			throw new IllegalArgumentException("Structure cannot be imported");
		}
		String exportedStructure;
		
		// export into GlycoCT
		try {
			exportedStructure = StructureParserValidator.exportStructure(sugarStructure);
		} catch (Exception e) {
			throw new IllegalArgumentException("Cannot export into common encoding: " + e.getMessage());
		}
    	
		logger.debug(exportedStructure);
    	byte[] bytes = imageGenerator.getImage(exportedStructure, format, notation, style);
		
		HttpHeaders headers = new HttpHeaders();
    	if (format == null || format.equalsIgnoreCase("png")) {    
		    headers.setContentType(MediaType.IMAGE_PNG);
    	} else if (format.equalsIgnoreCase("svg")) {
		    headers.setContentType(MediaType.APPLICATION_XML);
    	} else if (format.equalsIgnoreCase("jpg") || format.equalsIgnoreCase("jpeg")) {
    		headers.setContentType(MediaType.IMAGE_JPEG);
    	}
		return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
    }
    
    public Glycan copyGlycan(SparqlEntity se) throws ParseException {
		Glycan glycan = new Glycan();
		glycan.setAccessionNumber(se.getValue(GlycoSequence.AccessionNumber));
		glycan.setContributor(se.getValue("Contributor"));

		logger.debug(se.getValue("DateRegistered"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.ENGLISH);
		Date date = sdf.parse(se.getValue("DateRegistered"));
		glycan.setDateEntered(date);
		if (StringUtils.isNotBlank(se.getValue("Mass")))
			glycan.setMass(Double.valueOf(se.getValue("Mass")));
		if (StringUtils.isNotBlank(se.getValue("GlycoCTSequence")))
			glycan.setStructure(se.getValue("GlycoCTSequence") + "\n");
		glycan.setStructureLength(glycan.getStructure().length());
		return glycan;
    }
}
