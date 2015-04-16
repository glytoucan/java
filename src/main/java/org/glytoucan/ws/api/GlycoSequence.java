package org.glytoucan.ws.api;

import java.util.List;

import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.Saccharide;

public class GlycoSequence {

	private final long id;
	private final String primaryId;
	private String sequence;

	public GlycoSequence(long id, String primaryId) {
		this.id = id;
		this.primaryId = primaryId;
	}

	public long getId() {
		return id;
	}

	public String getPrimaryId() {
		return primaryId;
	}
	
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getSequence() {
		return sequence;
	}
}