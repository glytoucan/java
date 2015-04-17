package org.glytoucan.ws.controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.glycoinfo.rdf.utils.TripleStoreProperties;
import org.glytoucan.ws.api.GlycanResponse;
import org.glytoucan.ws.api.GlycoSequence;
import org.glytoucan.ws.api.Greeting;
import org.glytoucan.ws.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Api(value="/glycosequence", description="GlycoSequence-related interfaces")
@RequestMapping ("/glycosequence")
public class GlycoSequenceController {
	protected Log logger = LogFactory.getLog(getClass());

	@Autowired
	SparqlDAO sparqlDAO;

	@Autowired
	private SelectSparql selectSparql;
	
	public SparqlDAO getSparqlDAO() {
		return sparqlDAO;
	}

	public void setSparqlDAO(SparqlDAO sparqlDAO) {
		this.sparqlDAO = sparqlDAO;
	}
		
	public void setSelectSparql(SelectSparql read) {
		this.selectSparql = read;
	}

	public SelectSparql getSelectSparql() {
		return selectSparql;
	}

	@Bean
	TripleStoreProperties getTripleStoreProperties() {
		return new TripleStoreProperties();
	}

	private final AtomicLong counter = new AtomicLong();

	@RequestMapping(value="/retrieve", method=RequestMethod.GET)
    @ApiOperation(value="Retrieve the sequence of an id", response=GlycoSequence.class)
	@ApiResponses (value ={@ApiResponse(code=200, message="Sequence retrieved successfully")})
	public @ResponseBody GlycoSequence retrieve(
			@ApiParam(required=true, value="id of the sequence")
			@RequestParam(value = "primaryId", defaultValue = "G00030MO") String primaryId) {
		logger.debug("primaryId=" + primaryId + "<");
		List<SparqlEntity> list = null;
		try {
			SelectSparql ss = getSelectSparql();
			SparqlEntity se = ss.getSparqlEntity();
			if (null==ss.getSparqlEntity())
				se = new SparqlEntity();
			se.setValue(Saccharide.PrimaryId, primaryId);
			ss.setSparqlEntity(se);
			logger.debug(ss.getSparql());
			list = sparqlDAO.query(ss);
		} catch (SparqlException e) {
			return new GlycoSequence(0, "sorry something bad happened." + e.getMessage());
		}
		SparqlEntity se = null;
		try {
		se = list.get(0);
		} catch (java.lang.IndexOutOfBoundsException ie) {
			return new GlycoSequence(0, "sorry no results for " + primaryId );
		}
		GlycoSequence gs = new GlycoSequence(counter.incrementAndGet(), primaryId);
		gs.setSequence(se.getValue(org.glycoinfo.rdf.glycan.GlycoSequence.Sequence));

		return gs;
	}
	
//	@RequestMapping("/execute")
//    @ApiOperation(value="Execute a sparql", response=GlycoSequence.class)
//	@ApiResponses (value ={@ApiResponse(code=200, message="Sequence retrieved successfully")})
//	public GlycoSequence execute(
//			@RequestParam(value = "primaryId", defaultValue = "idRequired") String sparql) {
//		logger.debug("primaryId=" + sparql + "<");
//		List<SparqlEntity> list = null;
//		try {
//			SelectSparql ss = getSelectSparql();
//			SparqlEntity se = ss.getSparqlEntity();
//			if (null==ss.getSparqlEntity())
//				se = new SparqlEntity();
//			se.setValue(Saccharide.PrimaryId, sparql);
//			ss.setSparqlEntity(se);
//			logger.debug(ss.getSparql());
//			list = sparqlDAO.query(ss);
//		} catch (SparqlException e) {
//			return new GlycoSequence(0, "sorry something bad happened." + e.getMessage());
//		}
//		SparqlEntity se = null;
//		try {
//		se = list.get(0);
//		} catch (java.lang.IndexOutOfBoundsException ie) {
//			return new GlycoSequence(0, "sorry no results for " + primaryId );
//		}
//		GlycoSequence gs = new GlycoSequence(counter.incrementAndGet(), primaryId);
//		gs.setSequence(se.getValue(org.glycoinfo.rdf.glycan.GlycoSequence.Sequence));
//
//		return gs;
//	}
}