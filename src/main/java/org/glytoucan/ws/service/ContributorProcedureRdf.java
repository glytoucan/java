package org.glytoucan.ws.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glytoucan.ws.rdf.ContributorInsertSparql;
import org.glytoucan.ws.rdf.LatestContributorIdSparql;
import org.springframework.beans.factory.annotation.Autowired;

public class ContributorProcedureRdf implements ContributorProcedure  {

	private static final Log logger = LogFactory
			.getLog(ContributorProcedureRdf.class);
	
	@Autowired
	SparqlDAO sparqlDAO;
	
	@Autowired
	ContributorInsertSparql contributorSparql;

	
	String name;
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * adds a Contributor class (foaf:Person).
	 * 
     * <http://rdf.glycoinfo.org/glytoucan/contributor/1>
     * a    foaf:Person ;
     * dcterms:identifier    "1" ;
     * foaf:name "Administrator" .
     * 
	 * @throws SparqlException
	 */
	public String addContributor() throws SparqlException {
		
		if (StringUtils.isBlank(getName()))
			throw new SparqlException("name cannot be blank");

		// retrieve the latest contributor id
		SelectSparql selectLatestContributorId = new LatestContributorIdSparql();
		List<SparqlEntity> personUIDResult = sparqlDAO.query(selectLatestContributorId);
		
		SparqlEntity idSE = personUIDResult.iterator().next();
		
		String id = idSE.getValue("id");
		
		// insert the above data.
		SparqlEntity sparqlEntityPerson = new SparqlEntity(id);
		sparqlEntityPerson.setValue(ContributorInsertSparql.ContributorName, getName());
		sparqlEntityPerson.setValue(ContributorInsertSparql.UserId, id);
		contributorSparql.setSparqlEntity(sparqlEntityPerson);
		
		sparqlDAO.insert(contributorSparql);
		
//		throw new SparqlException("wtf");
		return id;
	}

	
}
