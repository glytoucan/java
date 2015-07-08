package org.glytoucan.ws.controller;

import java.util.ArrayList;
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
import org.glytoucan.ws.api.D3_Tree_json;
import org.glytoucan.ws.api.GlycanResponse;
import org.glytoucan.ws.api.GlycoSequence;
import org.glytoucan.ws.api.Greeting;
import org.glytoucan.ws.api.TreeSequence;
import org.glytoucan.ws.api.Tree_json;
import org.glytoucan.ws.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Api(value = "/glycosequence", description = "GlycoSequence-related interfaces")
@RequestMapping("/glycosequence")
public class D3Controller {
	protected Log logger = LogFactory.getLog(getClass());

	@Autowired
	SparqlDAO sparqlDAO;

	@Autowired
	@Qualifier(value = "d3SequenceSelectSparql")
	private SelectSparql selectD3Sparql;

	public SparqlDAO getSparqlDAO() {
		return sparqlDAO;
	}

	public void setSparqlDAO(SparqlDAO sparqlDAO) {
		this.sparqlDAO = sparqlDAO;
	}

	public void setSelectD3Sparql(SelectSparql read) {
		this.selectD3Sparql = read;
	}

	public SelectSparql getSelectD3Sparql() {
		return selectD3Sparql;
	}

	@Bean
	TripleStoreProperties getTripleStoreProperties() {
		return new TripleStoreProperties();
	}

	private final AtomicLong counter = new AtomicLong();

	@RequestMapping(value = "/retrieve", method = RequestMethod.GET)
	@ApiOperation(value = "Retrieve the sequence of an id", response = GlycoSequence.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sequence retrieved successfully") })
	public @ResponseBody D3_Tree_json retrieve(
			@ApiParam(required = true, value = "id of the sequence") @RequestParam(value = "primaryId", defaultValue = "G00030MO") String primaryId) {
		logger.debug("primaryId=" + primaryId + "<");
		// error messageを返す（形式が違うもの）
		List<SparqlEntity> list = null;
		try {
			SelectSparql ss = getSelectD3Sparql();
			SparqlEntity se = ss.getSparqlEntity();
			if (null == ss.getSparqlEntity())
				se = new SparqlEntity();
			se.setValue(Saccharide.PrimaryId, primaryId);
			ss.setSparqlEntity(se);
			logger.debug(ss.getSparql());
			list = sparqlDAO.query(ss);
		} catch (SparqlException e) {
			D3_Tree_json a = new D3_Tree_json();
			a.setName("sorry");
			return a;
		}
		SparqlEntity se = null;

		D3_Tree_json a = new D3_Tree_json();
		Tree_json b1 = new Tree_json();
		Tree_json b2 = new Tree_json();
		Tree_json b3 = new Tree_json();
		Tree_json b4 = new Tree_json();
		Tree_json b5 = new Tree_json();
		Tree_json b6 = new Tree_json();
		// TreeSequence c1 = new TreeSequence();
		TreeSequence c2 = new TreeSequence();
		TreeSequence c3 = new TreeSequence();
		TreeSequence c4 = new TreeSequence();
		TreeSequence c5 = new TreeSequence();
		TreeSequence c6 = new TreeSequence();
		ArrayList<Tree_json> b_list = new ArrayList<Tree_json>();
		ArrayList<TreeSequence> c_list1 = new ArrayList<TreeSequence>(); // MAPにする？
		ArrayList<TreeSequence> c_list2 = new ArrayList<TreeSequence>();
		ArrayList<TreeSequence> c_list3 = new ArrayList<TreeSequence>();
		ArrayList<TreeSequence> c_list4 = new ArrayList<TreeSequence>();
		ArrayList<TreeSequence> c_list5 = new ArrayList<TreeSequence>();
		ArrayList<TreeSequence> c_list6 = new ArrayList<TreeSequence>();

		try {
			// table to d3 class
			int j = 0;
			c2.setName("test_keiko");
			for (SparqlEntity i : list) {
				TreeSequence c1 = new TreeSequence();
				se = list.get(j);
				// logger.debug("list.motif" + se.getValue("motif") + "<");

				String motifName = se.getValue("motif");
				c1.setName(motifName);
				c1.setSize(10);
				if (c1.getName().equals(c2.getName())) { // c1Name.equals(c2Name)
					break;
				} else if (c1.getName().length() == 0) {
					break;
				} else {
					c_list1.add(c1);
				}
				c2.setName(c1.getName());
				j++;
			}

			j = 0;
			c2.setName("test_keiko");
			for (SparqlEntity i : list) {
				TreeSequence c1 = new TreeSequence();
				se = list.get(j);
				// logger.debug("list.motif" + se.getValue("motif") + "<");

				String isomerName = se.getValue("isomer");
				c1.setName(isomerName);
				c1.setSize(10);
				if (c1.getName().equals(c2.getName())) { // c1Name.equals(c2Name)
					break;
				} else if (c1.getName().length() == 0) {
					break;
				} else {
					c_list2.add(c1);
				}
				c2.setName(c1.getName());
				j++;
			}
			j = 0;
			c2.setName("test_keiko");
			for (SparqlEntity i : list) {
				TreeSequence c1 = new TreeSequence();
				se = list.get(j);
				logger.debug("list.superS" + se.getValue("superS") + "<");

				String superSName = se.getValue("superS");
				c1.setName(superSName);
				c1.setSize(10);
				if (c1.getName().equals(c2.getName())) { // c1Name.equals(c2Name)
					break;
				} else if (c1.getName().length() == 0) {
					break;
				} else {
					c_list3.add(c1);
				}
				c2.setName(c1.getName());
				j++;
			}
			
			j = 0;
			c2.setName("test_keiko");
			for (SparqlEntity i : list) {
				TreeSequence c1 = new TreeSequence();
				se = list.get(j);

				String subSName = se.getValue("subS");
				c1.setName(subSName);
				c1.setSize(10);
				if (c1.getName().equals(c2.getName())) { // c1Name.equals(c2Name)
					break;
				} else if (c1.getName().length() == 0) {
					break;
				} else {
					c_list4.add(c1);
				}
				c2.setName(c1.getName());
				j++;
			}
			
			j = 0;
			c2.setName("test_keiko");
			for (SparqlEntity i : list) {
				TreeSequence c1 = new TreeSequence();
				se = list.get(j);
				logger.debug("list.subsume" + se.getValue("subsume") + "<");

				String subsumeName = se.getValue("subsume");
				c1.setName(subsumeName);
				c1.setSize(10);
				logger.debug("subsumeName" + c1.getName() + "<");
				if (c1.getName().equals(c2.getName())) { // c1Name.equals(c2Name)
					break;
				} else if (c1.getName().length() == 0) {
					break;
				} else {
					logger.debug("else<");
					c_list5.add(c1);
				}
				c2.setName(c1.getName());
				j++;
			}
			
			j = 0;
			c2.setName("test_keiko");
			for (SparqlEntity i : list) {
				TreeSequence c1 = new TreeSequence();
				se = list.get(j);

				String subBName = se.getValue("subsumeB");
				c1.setName(subBName);
				c1.setSize(10);
				if (c1.getName().equals(c2.getName())) { // c1Name.equals(c2Name)
					break;
				} else if (c1.getName().length() == 0) {
					break;
				} else {
					c_list6.add(c1);
				}
				c2.setName(c1.getName());
				j++;
			}
			logger.debug("list" + list + "<");

			b1.setName("has_motif");
			b1.setChildren(c_list1);
			b2.setName("has_linkage_isomer");
			b2.setChildren(c_list2);
			b3.setName("has_superstructure");
			b3.setChildren(c_list3);
			b4.setName("has_substructure");
			b4.setChildren(c_list4);
			b5.setName("subsumes");
			b5.setChildren(c_list5);
			b6.setName("subsumed_by");
			b6.setChildren(c_list6);
			b_list.add(b1);
			b_list.add(b2);
			b_list.add(b3);
			b_list.add(b4);
			b_list.add(b5);
			b_list.add(b6);
			String GlycanName = se.getValue("id");
			a.setName(GlycanName);
			a.setChildren(b_list);

		} catch (java.lang.IndexOutOfBoundsException ie) {
			return null;
		}
		// GlycoSequence gs = new GlycoSequence(counter.incrementAndGet(),
		// primaryId);
		// gs.setSequence(se.getValue(org.glycoinfo.rdf.glycan.GlycoSequence.Sequence));

		return a;
	}

	// @RequestMapping("/execute")
	// @ApiOperation(value="Execute a sparql", response=GlycoSequence.class)
	// @ApiResponses (value ={@ApiResponse(code=200,
	// message="Sequence retrieved successfully")})
	// public GlycoSequence execute(
	// @RequestParam(value = "primaryId", defaultValue = "idRequired") String
	// sparql) {
	// logger.debug("primaryId=" + sparql + "<");
	// List<SparqlEntity> list = null;
	// try {
	// SelectSparql ss = getSelectSparql();
	// SparqlEntity se = ss.getSparqlEntity();
	// if (null==ss.getSparqlEntity())
	// se = new SparqlEntity();
	// se.setValue(Saccharide.PrimaryId, sparql);
	// ss.setSparqlEntity(se);
	// logger.debug(ss.getSparql());
	// list = sparqlDAO.query(ss);
	// } catch (SparqlException e) {
	// return new GlycoSequence(0, "sorry something bad happened." +
	// e.getMessage());
	// }
	// SparqlEntity se = null;
	// try {
	// se = list.get(0);
	// } catch (java.lang.IndexOutOfBoundsException ie) {
	// return new GlycoSequence(0, "sorry no results for " + primaryId );
	// }
	// GlycoSequence gs = new GlycoSequence(counter.incrementAndGet(),
	// primaryId);
	// gs.setSequence(se.getValue(org.glycoinfo.rdf.glycan.GlycoSequence.Sequence));
	//
	// return gs;
	// }
}