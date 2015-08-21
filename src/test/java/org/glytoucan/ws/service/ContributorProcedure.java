package org.glytoucan.ws.service;

import org.glycoinfo.rdf.SparqlException;

public interface ContributorProcedure {
	String addContributor() throws SparqlException;

	String getName();

	void setName(String name);
}