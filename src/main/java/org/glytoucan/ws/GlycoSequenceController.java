package org.glytoucan.ws;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.GlycoSequenceSelectSparql;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.glycoinfo.rdf.utils.TripleStoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
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

	@RequestMapping("/retrieve")
	public GlycoSequence retrieve(
			@RequestParam(value = "primaryId", defaultValue = "idRequired") String primaryId) {
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
	
	@RequestMapping("/execute")
	public GlycoSequence execute(
			@RequestParam(value = "primaryId", defaultValue = "idRequired") String primaryId) {
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
}