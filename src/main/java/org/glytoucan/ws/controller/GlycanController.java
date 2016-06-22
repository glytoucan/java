package org.glytoucan.ws.controller;



import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
//import org.eurocarbdb.MolecularFramework.util.similiarity.SearchEngine.SearchEngineException;
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
import org.glycoinfo.rdf.service.UserProcedure;
import org.glycoinfo.vision.generator.ImageGenerator;
import org.glycoinfo.vision.importers.GWSImporter;
import org.glytoucan.model.spec.GlycanClientQuerySpec;
import org.glytoucan.ws.api.Confirmation;
import org.glytoucan.ws.api.Glycan;
import org.glytoucan.ws.api.GlycanInput;
import org.glytoucan.ws.api.GlycanList;
import org.glytoucan.ws.api.GlycanResponse;
//import org.glytoucan.ws.utils.MassCalculator;
//import org.glytoucan.ws.view.Confirmation;
//import org.glytoucan.ws.view.GlycanErrorResponse;
//import org.glytoucan.ws.view.GlycanExhibit;
//import org.glytoucan.ws.view.GlycanInputList;
//import org.glytoucan.ws.view.GlycanList;
//import org.glytoucan.ws.view.GlycanResponseList;
//import org.glytoucan.ws.view.StructureParserValidator;
//import org.glytoucan.ws.view.User;
//import orgimport org.slf4j.LoggerFactory;
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
//import org.glycomedb.rdf.glycordf.util.GlycoRDFWriter;
//import org.glycomedb.rdf.glycordf.util.RDFGeneratorGlycanConfig;
//import org.glycomedb.residuetranslator.ResidueTranslator;
//import org.glytoucan.ws.dao.exceptions.ErrorCodes;
//import org.glytoucan.ws.dao.exceptions.GlycanNotFoundException;
//import org.glytoucan.ws.dao.exceptions.UserQuotaExceededException;
//import org.glytoucan.ws.database.GlycanComposition;
//import org.glytoucan.ws.database.GlycanEntity;
//import org.glytoucan.ws.database.MotifEntity;
//import org.glytoucan.ws.database.MotifTag;
//import org.glytoucan.ws.database.UserEntity;
//import org.glytoucan.ws.importers.GWSImporter;
//import org.glytoucan.ws.service.EmailManager;
//import org.glytoucan.ws.service.GlycanManager;
//import org.glytoucan.ws.service.UserManager;
//import org.glytoucan.ws.service.search.CombinationSearch;
//import org.glytoucan.ws.utils.GlycanStructureProvider;

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
	UserProcedure userManager;
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
	
//	@RequestMapping(value="/delete/list", method = RequestMethod.DELETE, consumes={"application/xml", "application/json"})
//    @ApiOperation(value="Deletes the glycans with given accession numbers", response=Confirmation.class, notes="This can be accessed by the Administrator user only")
//    @ApiResponses (value ={@ApiResponse(code=200, message="Glycans deleted successfully"), 
//    		@ApiResponse(code=400, message="Illegal argument - Only accession numbers should be given"),
//    		@ApiResponse(code=401, message="Unauthorized"),
//    		@ApiResponse(code=403, message="Not enough privileges to delete glycans"),
//    		@ApiResponse(code=404, message="Glycan does not exist"),
//    		@ApiResponse(code=415, message="Media type is not supported"),
//    		@ApiResponse(code=500, message="Internal Server Error")})
//	public @ResponseBody Confirmation deleteGlycans (
//			@ApiParam(required=true, value="accession numbers of the glycans to be deleted") 
//			@RequestBody(required=true) 
//			GlycanList accessionNumbers) {
//		GlycanList list = new GlycanList();
//		List<Object> objectList = accessionNumbers.getGlycans();
//		List<String> numberList = new ArrayList<>();
//		for (Iterator iterator = objectList.iterator(); iterator.hasNext();) {
//			Object item = (Object) iterator.next();
//			if (item instanceof String) {
//				numberList.add((String)item);
//			}
//			else {
//				throw new IllegalArgumentException("Only accession numbers should be given as input");
//			}
//		}
//		glycanManager.deleteGlycansByAccessionNumber(numberList);
//		return new Confirmation("Glycans deleted successfully", HttpStatus.OK.value());
//	}
	
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
	
//	@RequestMapping(value = "/{accessionNumber}/rdf", method = RequestMethod.GET, produces={"text/turtle"})
//	@ApiOperation(value="Retrieves glycan RDF by accession number", response=ResponseEntity.class)
//	@ApiResponses (value ={@ApiResponse(code=200, message="Success"),
//			@ApiResponse(code=404, message="Glycan does not exist"),
//			@ApiResponse(code=500, message="Internal Server Error")})
//	public ResponseEntity<String> getGlycanRDF (
//			@ApiParam(required=true, value="id of the glycan") 
//			@PathVariable("accessionNumber") String accessionNumber) throws IOException {
//		
//		Glycan glycanEntity = glycanManager.getGlycanByAccessionNumber(accessionNumber);
//		glycanEntity.getContributor().setEmail(""); // hide the email
//		
//		GlycanStructureProvider strProvider = new GlycanStructureProvider();
//		strProvider.setGlycan(glycanEntity);
//		
//		// generate RDF export
//        RDFGeneratorGlycanConfig t_config = createRDFConfiguration();
//        ResidueTranslator t_residueTransTranslation = new ResidueTranslator();
//        GlycoRDFWriter t_writer = new GlycoRDFWriter(strProvider, t_config, t_residueTransTranslation);
//        t_writer.setNamespace ("glytoucan");
//        t_writer.write(glycanEntity.getGlycanId());
//        StringWriter t_writerString = new StringWriter();
//        t_writer.serialze(t_writerString, "TURTLE");
//        t_writerString.flush();
//        
//        // write it out
//    	return new ResponseEntity<String>(t_writerString.toString(), HttpStatus.OK);
//	}
	
//	@RequestMapping(value="/list/accessionNumber", method = RequestMethod.POST, consumes={"application/xml", "application/json"}, produces={"application/xml", "application/json"})
//    @ApiOperation(value="Retrieves the glycans with given accession numbers", response=GlycanList.class, notes="optional payload parameter allows to get the 'full' glycan object details (default) or 'exhibit' format conforming to SIMILE Exhibit")
//    @ApiResponses (value ={@ApiResponse(code=200, message="Glycans retrieved successfully"), 
//    		@ApiResponse(code=400, message="Illegal argument - Only accession numbers should be given"),
//    		@ApiResponse(code=404, message="Glycan does not exist"),
//    		@ApiResponse(code=415, message="Media type is not supported"),
//    		@ApiResponse(code=500, message="Internal Server Error")})
//	public @ResponseBody GlycanList listGlycansByAccessionNumbers (
//			@ApiParam(required=true, value="accession numbers of the glycans to be retrieved") 
//			@RequestBody
//			GlycanList accessionNumbers,
//			@ApiParam(required=false, value="payload: full (default)") 
//			@RequestParam(required=false, value="payload", defaultValue="full")
//			String payload) {
//		
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//		String imageURL;
//		String requestURI = request.getRequestURL().toString();
//		
//		GlycanList list = new GlycanList();
//		List<Object> objectList = accessionNumbers.getGlycans();
//		List<String> numberList = new ArrayList<>();
//		for (Iterator iterator = objectList.iterator(); iterator.hasNext();) {
//			Object item = (Object) iterator.next();
//			if (item instanceof String) {
//				numberList.add((String)item);
//			}
//			else {
//				throw new IllegalArgumentException("Only accession numbers should be given");
//			}
//		}
//		List<Glycan> glycans = glycanManager.getGlycansByAccessionNumbers(numberList);
//		for (Iterator<Glycan> iterator = glycans.iterator(); iterator.hasNext();) {
//			Glycan glycanEntity = (Glycan) iterator.next();
//			glycanEntity.getContributor().setEmail(""); // hide email from any user
//		}
//		list.setGlycans(glycans.toArray());
//		return list;
//	}
	
//	@RequestMapping(value = "/list", method = RequestMethod.GET, produces={"application/xml", "application/json"})
//	@ApiOperation (value="Lists all the glycans", response=GlycanList.class, notes="payload option can be omitted to get only the glycan ids or set to 'full' to get glycan objects. 'exhibit' option allows to get glycan objects conforming to SIMILE Exhibit Json"
//			+ " format.")
//	@ApiResponses (value ={@ApiResponse(code=200, message="Success"), 
//			@ApiResponse(code=500, message="Internal Server Error")})
//	public @ResponseBody GlycanList listGlycans (
//			@ApiParam(required=false, value="payload: id (default) or full") 
//			@RequestParam(required=false, value="payload", defaultValue="id")
//			String payload, @ApiParam(required=true, value="limit: the limit to rows returned") 
//			@RequestParam(required=true, value="limit", defaultValue="100")
//			String limit, @ApiParam(required=false, value="offset: offset off of first row to retrieve") 
//			@RequestParam(required=false, value="offset", defaultValue="100")
//			String offset) throws ParseException, SparqlException {
//		GlycanList list = new GlycanList();
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//		String imageURL;
//		String requestURI = request.getRequestURL().toString();
//		List<Glycan> glycanList = new ArrayList<Glycan>();
//		List<SparqlEntity> glycans = glycanProcedure.getGlycans(offset, limit);
//		for (SparqlEntity sparqlEntity : glycans) {
//			Glycan glycan = copyGlycan(sparqlEntity);
//			logger.debug("adding:>" + glycan + "<");
//			glycanList.add(glycan);
//		}
//		if (payload != null && (payload.equalsIgnoreCase("full"))) {
//			list.setGlycans(glycanList.toArray());
//		}
//		else {
//			List<String> ids = new ArrayList<String>();
//			for (SparqlEntity sparqlEntity : glycans) {
//				ids.add(sparqlEntity.getValue(GlycoSequence.AccessionNumber));
//			}
//			list.setGlycans(ids.toArray());
//		}
//		return list;
//	}
	
	
	// sample sparql : https://bitbucket.org/issaku/virtuoso/wiki/G00030MO%20(order%20by%20manual)
//	@RequestMapping(value = "/sparql/substructure", method = RequestMethod.GET, produces={"application/json"})
//    @ApiOperation(value="Returns the select SPARQL used to find a substructure in the wurcs RDF ontology.", 
//    			response=SelectSparql.class)
//    @ApiResponses(value ={@ApiResponse(code=200, message="Found match(es)"), 
//    				@ApiResponse(code=400, message="Illegal argument - Glycan should be valid"),
//    				@ApiResponse(code=404, message="Cannot generate"),
//    				@ApiResponse(code=500, message="Internal Server Error")})
//	public @ResponseBody SelectSparql substructureSearch (
//			@RequestParam(required=true, value="sequence", defaultValue="WURCS")
//			@ApiParam(required=true, value="Glycan sequence", name="sequence") 
//			String sequence,
//			@RequestParam(required=true, value="format", defaultValue="wurcs")
//			@ApiParam(required=true, value="Glycan format - currently only wurcs via GET", name="format") 
//			String format) throws SugarImporterException, GlycoVisitorException, SparqlException {
//		logger.debug("Substructure search");
//		logger.debug("sequence:" + sequence);
//		logger.debug("format:" + format);
//		
//		// if glycoct:
//		if (format != null && format.equals("glycoct")) {
//			// if it's not valid, would have thrown an exception.
//	
//			// convert to wurcs
//			SugarImporter t_objImporterGlycoCT = new SugarImporterGlycoCTCondensed();
//	
//			Sugar sugar = t_objImporterGlycoCT.parse(sequence);
//			// GlycoCT must be validated by GlycoVisitorValidation
//			GlycoVisitorValidation validation = new GlycoVisitorValidation();
//			validation.start(sugar);
//			if ( validation.getErrors().size() > 0 ) {
//				logger.error("Errors:");
//				for ( String err : validation.getErrors() ) {
//					logger.error( err );
//				}
//				logger.warn("Warnings:");
//				for ( String warn : validation.getWarnings() ) {
//					logger.warn( warn );
//				}
//				logger.warn("\n");
//			}
////			SugarExporterWURC t_exporter3 = new SugarExporterWURCS();
////			t_exporter3.start(sugar);
////			sequence = t_exporter3.getWURCSCompress();
//		}
//		
//		// if wurcs:
//		// validate the structure
//		
//		// wurcs to sparql
//		SparqlEntity se = new SparqlEntity();
//		se.setValue(GlycoSequence.Sequence, sequence);
//		SelectSparql selectSparql = substructureSearchSparql;
//		selectSparql.setSparqlEntity(se);
//		
//		String where = selectSparql.getWhere();
//		selectSparql.setWhere(where.replace('\n', ' '));
//		String sparql = selectSparql.getSparql();
//		selectSparql.setSparql(sparql.replace('\n', ' '));
//		logger.debug("GlycanController result:>" + selectSparql.getSparql() + "<");
//
//		return selectSparql;
//	}
	
//	@RequestMapping(value = "/search/composition", method = RequestMethod.POST, consumes={"application/xml", "application/json"}, produces={"application/xml", "application/json"})
//    @ApiOperation(value="Searches for glycan structures with the given composition", 
//    			response=GlycanList.class)
//    @ApiResponses(value ={@ApiResponse(code=200, message="Found match(es)"), 
//    				@ApiResponse(code=400, message="Illegal argument - Search Criteria should be valid"),
//    				@ApiResponse(code=404, message="No matching glycan is found"),
//    				@ApiResponse(code=415, message="Media type is not supported"),
//    				@ApiResponse(code=500, message="Internal Server Error")})
//	public @ResponseBody GlycanList compositionSearch (
//			@RequestBody (required=true)
//			@ApiParam(required=true, value="Search Criteria")
//			@Valid CompositionSearchInput input,
//			@ApiParam(required=false, value="payload: id (default) or full or exhibit") 
//			@RequestParam(required=false, value="payload", defaultValue="id")
//			String payload) throws SugarImporterException, GlycoVisitorException, SearchEngineException {
//		GlycanList matches = new GlycanList();
//		matches.setGlycans((glycanManager.compositionSearch(input)).toArray());
//		if (payload != null && (payload.equalsIgnoreCase("exhibit") || payload.equalsIgnoreCase("full"))) {
//			matches = listGlycansByAccessionNumbers(matches, payload);
//		}
//		return matches;
//	}
	
//	@RequestMapping(value = "/search/user", method = RequestMethod.POST, consumes={"application/xml", "application/json"}, produces={"application/xml", "application/json"})
//    @ApiOperation(value="Searches for glycan structures submitted by the given user", notes="You can provide any of the following fields: login name, full name, affiliation and email for the user (password field will be ignored). "
//    		+ "The user matching all of the given fields (if any) will be used as the contributor",
//    			response=GlycanList.class)
//    @ApiResponses(value ={@ApiResponse(code=200, message="Found match(es)"), 
//    				@ApiResponse(code=400, message="Illegal argument - Search Criteria should be valid"),
//    				@ApiResponse(code=404, message="No matching glycan is found / No user is found matching the criteria"),
//    				@ApiResponse(code=415, message="Media type is not supported"),
//    				@ApiResponse(code=500, message="Internal Server Error")})
//	public @ResponseBody GlycanList getGlycansByContributor (
//			@RequestBody 
//			@ApiParam(required=true, value="User Criteria")
//		    UserInput user,
//		    @ApiParam(required=false, value="payload: id (default) or full or exhibit") 
//			@RequestParam(required=false, value="payload", defaultValue="id")
//			String payload) {
//		GlycanList matches = new GlycanList();
//		if (user == null || 
//			(user.getLoginId() == null && 
//			user.getAffiliation() == null && 
//			user.getLoginId() == null && 
//			user.getEmail() == null)) {
//			// invalid input
//			throw new IllegalArgumentException("Invalid input: You should at least provide one field of the user");
//		} else if ((user.getLoginId() == null || (user.getLoginId() != null && user.getLoginId().isEmpty())) && 
//				(user.getAffiliation() == null || (user.getAffiliation() != null && user.getAffiliation().isEmpty())) && 
//				(user.getEmail() == null || (user.getEmail() != null && user.getEmail().isEmpty())) &&
//				(user.getFullName() == null || (user.getFullName() != null && user.getFullName().isEmpty()) ) ) {
//			// invalid input
//			throw new IllegalArgumentException("Invalid input: You should at least provide one field of the user");		
//		}
//		matches.setGlycans(glycanManager.getGlycansByContributor(user).toArray());
//		if (payload != null && (payload.equalsIgnoreCase("exhibit") || payload.equalsIgnoreCase("full"))) {
//			matches = listGlycansByAccessionNumbers(matches, payload);
//		}
//		return matches;
//	}
	
//	@RequestMapping(value = "/search/user/pending", method = RequestMethod.POST, consumes={"application/xml", "application/json"}, produces={"application/xml", "application/json"})
//    @ApiOperation(value="Searches for all pending glycan structures submitted by the given user", notes="Administrator/Moderator use only. You can provide any of the following fields: login name, full name, affiliation and email for the user (password field will be ignored). "
//    		+ "The user matching all of the given fields (if any) will be used as the contributor",
//    			response=GlycanList.class)
//    @ApiResponses(value ={@ApiResponse(code=200, message="Found match(es)"), 
//    				@ApiResponse(code=400, message="Illegal argument - Search Criteria should be valid"),
//    				@ApiResponse(code=401, message="Unauthorized"),
//    	    		@ApiResponse(code=403, message="Not enough privileges to delete glycans"),
//    				@ApiResponse(code=404, message="No matching glycan is found / No user is found matching the criteria"),
//    				@ApiResponse(code=415, message="Media type is not supported"),
//    				@ApiResponse(code=500, message="Internal Server Error")})
//	public @ResponseBody GlycanList getPendingGlycansByContributor (
//			@RequestBody 
//			@ApiParam(required=true, value="User Criteria")
//		    UserInput user, 
//		    @ApiParam(required=false, value="payload: id (default) or full or exhibit") 
//			@RequestParam(required=false, value="payload", defaultValue="id")
//			String payload) {
//		GlycanList matches = new GlycanList();
//		if (user == null || 
//			(user.getLoginId() == null && 
//			user.getAffiliation() == null && 
//			user.getLoginId() == null && 
//			user.getEmail() == null)) {
//			// invalid input
//			throw new IllegalArgumentException("Invalid input: You should at least provide one field of the user");
//		} else if ((user.getLoginId() == null || (user.getLoginId() != null && user.getLoginId().isEmpty())) && 
//				(user.getAffiliation() == null || (user.getAffiliation() != null && user.getAffiliation().isEmpty())) && 
//				(user.getEmail() == null || (user.getEmail() != null && user.getEmail().isEmpty())) &&
//				(user.getFullName() == null || (user.getFullName() != null && user.getFullName().isEmpty()) ) ) {
//			// invalid input
//			throw new IllegalArgumentException("Invalid input: You should at least provide one field of the user");		
//		}
//		matches.setGlycans(glycanManager.getAllPendingGlycansByContributor(user).toArray());
//		if (payload != null && (payload.equalsIgnoreCase("exhibit") || payload.equalsIgnoreCase("full"))) {
//			matches = listGlycansByAccessionNumbers(matches, payload);
//		}
//		return matches;
//	}
	
//	@RequestMapping(value = "/search/motif", method = RequestMethod.POST, produces={"application/xml", "application/json"})
//    @ApiOperation(value="Searches for glycan structures containing the given motif", 
//    			response=GlycanList.class)
//    @ApiResponses(value ={@ApiResponse(code=200, message="Found match(es)"), 
//    				@ApiResponse(code=400, message="Illegal argument - Motif name should be valid"),
//    				@ApiResponse(code=404, message="No matching glycan is found / No motif is found with given name"),
//    				@ApiResponse(code=415, message="Media type is not supported"),
//    				@ApiResponse(code=500, message="Internal Server Error")})
//	public @ResponseBody GlycanList getGlycansByMotif (
//			@RequestBody 
//			@ApiParam(required=true, value="Motif Name")
//		    String motifName, 
//		    @ApiParam(required=false, value="payload: id (default) or full or exhibit") 
//			@RequestParam(required=false, value="payload", defaultValue="id")
//			String payload) throws Exception {
//		GlycanList matches = new GlycanList();
//		if (motifName == null || motifName.isEmpty())
//			// invalid input
//			throw new IllegalArgumentException("Invalid input: You have to provide the name of the motif");
//		matches.setGlycans(glycanManager.motifSearch(motifName).toArray());
//		if (payload != null && (payload.equalsIgnoreCase("exhibit") || payload.equalsIgnoreCase("full"))) {
//			matches = listGlycansByAccessionNumbers(matches, payload);
//		}
//		return matches;
//	}
	
//	@RequestMapping(value = "/search/exact", method = RequestMethod.POST, consumes={"application/xml", "application/json"}, produces={"application/xml", "application/json"})
//    @ApiOperation(value="Searches for a glycan structure having exactly the same structure as the input structure", 
//    			response=Glycan.class)
//    @ApiResponses(value ={@ApiResponse(code=200, message="Found a match"), 
//    				@ApiResponse(code=400, message="Illegal argument - Glycan should be valid"),
//    				@ApiResponse(code=404, message="No matching glycan is found"),
//    				@ApiResponse(code=415, message="Media type is not supported"),
//    				@ApiResponse(code=500, message="Internal Server Error")})
//	public @ResponseBody Glycan exactStructureSearch (
//			@RequestBody (required=true)
//			@ApiParam(required=true, value="Glycan") 
//			@Valid
//			GlycanInput glycan) throws Exception {
//		
////		String encoding = glycan.getEncoding();
////		Sugar sugarStructure = importParseValidate(glycan);
////		if (sugarStructure == null) {
////			throw new IllegalArgumentException("Structure cannot be imported");
////		}
//		String exportedStructure = null;
////		
////		// export into GlycoCT to make sure we have a uniform structure content in the DB
////		try {
////			exportedStructure = StructureParserValidator.exportStructure(sugarStructure);
////		} catch (Exception e) {
////			throw new IllegalArgumentException("Cannot export into common encoding: " + e.getMessage());
////		}
//			
//		Glycan glycanEntity = glycanManager.getByStructure(exportedStructure);
//		glycanEntity.getContributor().setEmail(""); // hide the email
//		return glycanEntity;
//	}
	
//	@RequestMapping (value="/search/complex", method=RequestMethod.POST, consumes={"application/xml", "application/json"}, produces={"application/xml", "application/json"})
//    @ApiOperation(value="Searches for glycan structures using a combination of other searches with union/intersection/difference",
//        response=GlycanList.class)
//            @ApiResponses(value ={@ApiResponse(code=200, message="Found match(es)"),
//            @ApiResponse(code=400, message="Illegal argument - Search input should be valid"),
//            @ApiResponse(code=415, message="Media type is not supported"),
//            @ApiResponse(code=500, message="Internal Server Error")})
//    public @ResponseBody GlycanList complexSearch (
//            @ApiParam (required=true, value="search input")
//            @RequestBody
//            CombinationSearch search, 
//            @ApiParam(required=false, value="payload: id (default) or full or exhibit") 
//			@RequestParam(required=false, value="payload", defaultValue="id")
//			String payload) throws Exception {
//        GlycanList matches = new GlycanList();
//        matches.setGlycans(search.search(glycanManager).toArray());
//        if (payload != null && (payload.equalsIgnoreCase("exhibit") || payload.equalsIgnoreCase("full"))) {
//			matches = listGlycansByAccessionNumbers(matches, payload);
//		}
//        return matches;
//    }

//	@RequestMapping(value="/add/list", method=RequestMethod.POST, consumes={"application/xml", "application/json"}, produces={"application/xml", "application/json"}) 
//	@ApiOperation(value="Add multiple glycan structures submitted as a list", response=GlycanResponseList.class)
//	@ApiResponses(value ={@ApiResponse(code=201, message="Glycans added successfully"), 
//			@ApiResponse(code=400, message="Illegal argument - Glycan List should be valid"),
//			@ApiResponse(code=401, message="Unauthorized. Only the logged in user can submit structures"),
//			@ApiResponse(code=415, message="Media type is not supported"),
//			@ApiResponse(code=500, message="Internal Server Error")})
//	public @ResponseBody GlycanResponseList glycanListAdd (
//			@ApiParam(name="glycans", required=true)
//			@RequestBody GlycanInputList glycans, Principal p) {
//		if (glycans == null || glycans.getGlycans().isEmpty()) {
//			throw new IllegalArgumentException("Invalid Input: The list is empty - No glycans to add");
//		}
//		List<GlycanResponse> responseList = new ArrayList<>();
//		List<GlycanErrorResponse> errorList = new ArrayList<>();
//		for (Iterator<?> iterator = glycans.getGlycans().iterator(); iterator.hasNext();) {
//			GlycanInput glycan = (GlycanInput) iterator.next();
//			try {
//				ResponseEntity<GlycanResponse> resp = submitStructure(glycan, p);
//				GlycanResponse gRes = resp.getBody();
//				gRes.setStructure(glycan.getSequence());
//				responseList.add(gRes);
//			} catch (UserQuotaExceededException e) {
//				throw e;
//			} catch (IllegalArgumentException e) {
//				GlycanErrorResponse error = new GlycanErrorResponse();
//				error.setErrorMessage(e.getMessage());
//				error.setStructure(glycan.getSequence());
//				error.setStatusCode(HttpStatus.BAD_REQUEST.value());
//				error.setErrorCode(ErrorCodes.INVALID_INPUT);
//				errorList.add(error);
//				logger.warn("Failed to add a glycan from the list. Reason: {}"+ e.getMessage());
//			} catch (SugarImporterException e) {
//				GlycanErrorResponse error = new GlycanErrorResponse();
//				error.setErrorMessage("Failed to import the structure from encoding " + glycan.getFormat() + ". Reason: " + e.getErrorText());
//				error.setStructure(glycan.getSequence());
//				error.setStatusCode(HttpStatus.BAD_REQUEST.value());
//				error.setErrorCode(ErrorCodes.PARSE_ERROR);
//				errorList.add(error);
//				logger.warn("Failed to import the structure from encoding " + glycan.getFormat() + ". Reason: " + e.getErrorText());
//			} catch (GlycoVisitorException e) {
//				GlycanErrorResponse error = new GlycanErrorResponse();
//				error.setErrorMessage("Failed to validate the structure. Reason: " + e.getMessage());
//				error.setStructure(glycan.getSequence());
//				error.setStatusCode(HttpStatus.BAD_REQUEST.value());
//				error.setErrorCode(ErrorCodes.INVALID_STRUCTURE);
//				errorList.add(error);
//				logger.warn("Failed to validate the structure. Reason: " + e.getMessage());
//			} catch (Exception e) {
//				GlycanErrorResponse error = new GlycanErrorResponse();
//				error.setErrorMessage("Failed to add the structure. Reason: " + e.getMessage());
//				error.setStructure(glycan.getSequence());
//				error.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//				error.setErrorCode(ErrorCodes.INTERNAL_ERROR);
//				errorList.add(error);
//				logger.error("Failed to add the structure. Reason: " + e.getMessage());
//			}
//		}
//		
//		return new GlycanResponseList(responseList, errorList);
//	}
	
//	@RequestMapping(value="/check/list", method=RequestMethod.POST, consumes={"application/xml", "application/json"}, produces={"application/xml", "application/json"}) 
//	@ApiOperation(value="Check multiple glycan structures to see if they already exist in the registry and whether they are valid to be added", response=GlycanResponseList.class)
//	@ApiResponses(value ={@ApiResponse(code=200, message="Glycans checked successfully"), 
//			@ApiResponse(code=400, message="Illegal argument - Glycan List should be valid"),
//			@ApiResponse(code=415, message="Media type is not supported"),
//			@ApiResponse(code=500, message="Internal Server Error")})
//	public @ResponseBody GlycanResponseList glycanListCheck (
//			@ApiParam(name="glycans", required=true)
//			@RequestBody GlycanInputList glycans) {
//		if (glycans == null || glycans.getGlycans().isEmpty()) {
//			throw new IllegalArgumentException("Invalid Input: The list is empty - No glycans to check");
//		}
//		List<GlycanResponse> responseList = new ArrayList<>();
//		List<GlycanErrorResponse> errorList = new ArrayList<>();
//		for (Iterator<?> iterator = glycans.getGlycans().iterator(); iterator.hasNext();) {
//			GlycanInput glycan = (GlycanInput) iterator.next();
//			GlycanResponse gRes = new GlycanResponse();
//			gRes.setStructure(glycan.getSequence());
//			try {
//				Glycan glycanEntity = exactStructureSearch(glycan);
//				if (glycanEntity != null) {
//					gRes.setExisting(true);
//					gRes.setPending(glycanManager.isPending(glycanEntity));
//					gRes.setAccessionNumber(glycanEntity.getAccessionNumber());
//				}
//				else
//					gRes.setExisting(false);
//				responseList.add(gRes);
//			} catch (Exception e) {
//				if (e instanceof GlycanNotFoundException) {
//					// new glycan
//					gRes.setExisting(false);
//					responseList.add(gRes);
//				}
//				else {
//					GlycanErrorResponse error = new GlycanErrorResponse();
//					if (e instanceof SugarImporterException) {
//						error.setErrorMessage("Failed to import the structure. Reason: " + ((SugarImporterException)e).getErrorText());
//						error.setErrorCode(ErrorCodes.PARSE_ERROR);
//						logger.info("Failed to import the structure. Reason: {}"+ ((SugarImporterException)e).getErrorText());
//					} else if (e instanceof UserQuotaExceededException) {
//						error.setErrorMessage("User quota exceeded");
//						error.setErrorCode (ErrorCodes.NOT_ALLOWED);
//						logger.error("User quota exceeded");
//					}
//					else {
//						error.setErrorMessage("Failed to validate the structure. Reason: " + e.getMessage());
//						error.setErrorCode(ErrorCodes.INVALID_STRUCTURE);
//						logger.info("Failed to validate the structure. Reason: {}"+ e.getMessage());
//					}
//					error.setStructure(glycan.getSequence());
//					error.setStatusCode(HttpStatus.BAD_REQUEST.value());
//					errorList.add(error);
//				}
//			}
//		}
//		return new GlycanResponseList(responseList, errorList);
//	}
	
	/**
     * Accept a file containing glycan structures
	 * @throws IOException 
     */
//    @RequestMapping(value = "/add/batchFile", method = RequestMethod.POST)
//    @ApiOperation(value="Add glycan structures submitted in a file.", response=Confirmation.class)
//    @ApiResponses(value ={@ApiResponse(code=201, message="Glycans added successfully"), 
//			@ApiResponse(code=400, message="Illegal argument - File should contain valid content"),
//			@ApiResponse(code=401, message="Unauthorized. Only the logged in user can submit structures"),
//			@ApiResponse(code=415, message="Media type is not supported"),
//			@ApiResponse(code=500, message="Internal Server Error")})
//    public @ResponseBody GlycanResponseList batchGlycanSubmit( 
//    		@ApiParam (name="encoding", defaultValue="glycoct")
//    		@RequestParam(value="encoding", defaultValue="glycoct") String encoding,
//    		@ApiParam(required=true, name="file")
//            @RequestParam("file") MultipartFile glycanFile, Principal p) throws IOException {
// 
//        if (glycanFile != null && !glycanFile.isEmpty()) {
//        	byte[] bytes = glycanFile.getBytes();
//        	InputStream stream = glycanFile.getInputStream();
//        	BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//        	
//        	List<GlycanInput> list = new ArrayList<GlycanInput>();
//        	if (encoding.equalsIgnoreCase("glycoct_xml") || encoding.equalsIgnoreCase("cabosML") || encoding.equalsIgnoreCase("glyde")) {
//        		// xml - parse xml, call appropriate importer for each children
//        		throw new IllegalArgumentException("The encoding is not supported yet!");
//        	} else if (encoding.equalsIgnoreCase("carbbank")) {
//        		// separator is "empty line" ?
//        		throw new IllegalArgumentException("The encoding is not supported yet!");
//        	} else if (encoding.equalsIgnoreCase("glycoct") || encoding.equalsIgnoreCase("kcf")) {
//        		// separator is ///
//        		String line = null;
//        		String structure ="";
//        		while ((line = reader.readLine()) != null) {
//        			if (line.equalsIgnoreCase("///")) {
//        				// add the structure
//        				if (!structure.isEmpty()) {
//        					// submit each structure
//        	        		GlycanInput glycan = new GlycanInput();
//        	        		glycan.setFormat(encoding);
//        	        		glycan.setSequence(structure.trim());
//        	        		list.add(glycan);
//        				}
//        				//new structure
//        				structure= "";
//        			}
//        			else {
//        				structure += line + "\n";
//        			}
//        		}
//        		// EOF check
//				if (!structure.isEmpty()) {
//					// submit each structure
//	        		GlycanInput glycan = new GlycanInput();
//	        		glycan.setFormat(encoding);
//	        		glycan.setSequence(structure.trim());
//	        		list.add(glycan);
//				}
//        	}
//        	else { // each line corresponds to a glycan structure
//	        	String structure = null;
//	        	while ((structure = reader.readLine()) != null) {
//	        		logger.debug("Line: " + structure);
//	        		// submit each structure
//	        		GlycanInput glycan = new GlycanInput();
//	        		glycan.setFormat(encoding);
//	        		glycan.setSequence(structure);
//	        		list.add(glycan);
//	        	}
//        	}
//        	GlycanInputList inputList = new GlycanInputList();
//        	inputList.setGlycans(list);
//        	return glycanListAdd (inputList, p);
//        } else {
//        	throw new IllegalArgumentException("Invalid input: The file is empty - No glycans to add");
//        }
//    }
    
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
      
//		SparqlEntity se = sparqlEntityFactory.create();
//    	SparqlEntity se = new SparqlEntity();
//		se.setValue(Saccharide.PrimaryId, accessionNumber);
//		
//		SparqlEntityFactory.set(se);
////		sparqlEntityFactory.set(se);
////		logger.debug("sparqlEntityFactory:>" + sparqlEntityFactory + "<");
//		logger.debug("SparqlEntity:>" + se + "<");
//    	SparqlEntity glycanEntity = glycanProcedure.searchByAccessionNumber(accessionNumber);
//    	String sequence = glycanEntity.getValue("GlycoCTSequence");
//    	
//    	logger.debug("image for " + accessionNumber + " sequence:>" + sequence + "<");
//    	
//    	byte[] bytes = imageGenerator.getGlycoCTImage(sequence, format, notation, style);

				
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