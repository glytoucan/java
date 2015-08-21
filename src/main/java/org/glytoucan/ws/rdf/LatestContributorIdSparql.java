package org.glytoucan.ws.rdf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparqlBean;

public class LatestContributorIdSparql extends SelectSparqlBean {

	public static Log logger = (Log) LogFactory.getLog(LatestContributorIdSparql.class);

	/**
	 * PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>
SELECT xsd:integer(MAX(?id)) + xsd:integer(1) as ?id
FROM <http://rdf.glytoucan.org>
where {
?o a foaf:Person .
BIND (STRAFTER(STR(?o), "http://rdf.glytoucan.org/contributor/userId/") AS ?id)
}
	 * 
	 */
	public LatestContributorIdSparql() {
		this.prefix = "PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n";
		this.select = "(xsd:integer(MAX(?id)) + xsd:integer(1)) as ?id";
		this.from = "FROM <http://rdf.glytoucan.org>\n";
		this.where 
			= "?o a foaf:Person .\n"
			+ "BIND (STRAFTER(STR(?o), \"http://rdf.glytoucan.org/contributor/userId/\") AS ?id) .\n";
	}
}