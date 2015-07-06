package org.glytoucan.ws.api;

import org.glycoinfo.rdf.SelectSparqlBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.conversion.GlyConvert;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 
 * SelectSparql for retrieving the Wurcs of The filter removes any existing
 * sequences in the getTo() of the GlyConvert.
 * 
 * For instance: Retrieving of original glycoct by using
 * org.glycoinfo.conversion.wurcs.GlycoctToWurcsConverter.
 * 
 * @author aoki
 *
 */

@Component
public class D3SequenceSelectSparql extends SelectSparqlBean {

	// public static final String SaccharideURI = Saccharide.URI;
	// public static final String id = "id";
	// public static final String GlycanSequenceURI = "GlycanSequenceURI";
	// public static final String AccessionNumber = Saccharide.PrimaryId;

	public static final String id = "id";

	// public static final String has_motif = Saccharide.URI;
	// public static final String has_linkage_isomer = "GlycanSequenceURI";
	// public static final String has_superstructure = Saccharide.PrimaryId;
	// public static final String has_substructure = Saccharide.PrimaryId;
	// public static final String hassumes = Saccharide.PrimaryId;
	// public static final String hassumes_by = Saccharide.PrimaryId;

	public D3SequenceSelectSparql(String sparql) {
		super(sparql);
	}

	public D3SequenceSelectSparql() {
		super();
		this.prefix = "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>";
		this.select = "DISTINCT ?id\n" + "?has_motif ?motif\n"
				+ "?has_linkage_isomer ?isomer\n"
				+ "?has_superstructure ?superS\n" + "?has_substructure ?subS\n"
				+ "?subsumes ?subsume\n" + "?subsumed_by ?subsumeB\n";
		this.from = "FROM <http://rdf.glycoinfo.org/glycan/browser/demo>";
	}

	public String getPrimaryId() {
		return "\"" + getSparqlEntity().getValue(Saccharide.PrimaryId) + "\"";
	}

	@Override
	public String getWhere() throws SparqlException {
		this.where = "VALUES ?id {" + getPrimaryId() + "}\n"
				+ "VALUES ?has_motif { glycan:has_motif }\n"
				+ "VALUES ?has_linkage_isomer { glytoucan:has_linkage_isomer }\n"
				+ "VALUES ?has_superstructure { glytoucan:has_superstructure }\n"
				+ "VALUES ?has_substructure { glytoucan:has_substructure }\n"
				+ "VALUES ?subsumes { glytoucan:subsumes }\n"
				+ "VALUES ?subsumed_by { glytoucan:subsumed_by }\n"

				+ "?s glytoucan:has_primary_id ?id .\n"

				+ "OPTIONAL {\n" + "?s ?has_motif ?hm .\n"
				+ "?hm glytoucan:has_primary_id ?motif .\n" + "}\n"

				+ "OPTIONAL {\n" + "?s ?has_linkage_isomer ?hli .\n"
				+ "?hli glytoucan:has_primary_id ?isomer .\n" + "}\n"

				+ "OPTIONAL {\n" + "?s ?has_superstructure ?hsuper .\n"
				+ "?hsuper glytoucan:has_primary_id ?superS .\n" + "}\n"

				+ "OPTIONAL {\n" + "?s ?has_substructure ?hsub .\n"
				+ "?hsub glytoucan:has_primary_id ?subS .\n" + "}\n"

				+ "OPTIONAL {\n" + "?s ?subsumes ?subsumes .\n"
				+ "?subsumes glytoucan:has_primary_id ?subsume .\n" + "}\n"

				+ "OPTIONAL {\n" + "?s ?subsumed_by ?sb .\n"
				+ "?sb glytoucan:has_primary_id ?subsumeB .\n" + "}\n"

		;

		return where;
	}

	protected Log logger = LogFactory.getLog(getClass());

	String glycanUri;

	/**
	 * 
	 * the filter removes any sequences that already have a sequence in the
	 * GlyConvert.getTo() format.
	 * 
	 * @return
	 */
	// public String getFilter() {
	// return "FILTER NOT EXISTS {\n" + "?" + SaccharideURI
	// + " glytoucan:has_derivatized_mass ?existingmass .\n}";
	// }

	// @Override
	// public void afterPropertiesSet() throws Exception {
	// Assert.state(getPrefix() != null, "A ident is required");
	// Assert.state(getSelect() != null, "A select is required");
	// }

}
