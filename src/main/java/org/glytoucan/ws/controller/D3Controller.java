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
import org.glytoucan.ws.api.TreeSequence;
import org.glytoucan.ws.api.Tree_json;
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
@Api(value = "/Tree", description = "D3 Tree format")
@RequestMapping("/Tree")
public class D3Controller {
	protected Log logger = LogFactory.getLog(getClass());

	@Autowired
	SparqlDAO sparqlDAO;

	@Autowired
	@Qualifier(value = "d3SequenceSelectSparql_motif")
	private SelectSparql selectD3Sparql_motif;

	public SparqlDAO getSparqlDAO() {
		return sparqlDAO;
	}

	public void setSparqlDAO(SparqlDAO sparqlDAO) {
		this.sparqlDAO = sparqlDAO;
	}

	public void setSelectD3Sparql_motif(SelectSparql read) {
		this.selectD3Sparql_motif = read;
	}

	public SelectSparql getSelectD3Sparql_motif() {
		return selectD3Sparql_motif;
	}
	
	@Autowired
	@Qualifier(value = "d3SequenceSelectSparql_isomer")
	private SelectSparql selectD3Sparql_isomer;

	public void setSelectD3Sparql(SelectSparql read) {
		this.selectD3Sparql_isomer = read;
	}

	public SelectSparql getSelectD3Sparql_isomer() {
		return selectD3Sparql_isomer;
	}

	@Bean
	TripleStoreProperties getTripleStoreProperties() {
		return new TripleStoreProperties();
	}

	private final AtomicLong counter = new AtomicLong();

	@RequestMapping(value = "/D3retrieve", method = RequestMethod.GET)
	@ApiOperation(value = "Retrieve the sequence of json of D3 Tree ", response = D3_Tree_json.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sequence retrieved successfully") })
	public @ResponseBody D3_Tree_json D3retrieve(
			@ApiParam(required = true, value = "id of the sequence") @RequestParam(value = "primaryId", defaultValue = "G00030MO") String primaryId) {
		logger.debug("primaryId=" + primaryId + "<");
		// error messageを返す（形式が違うもの）
		/*List<SparqlEntity> list = new ArrayList<SparqlEntity>();*/
		List<SparqlEntity> list_motif = null;
		List<SparqlEntity> list_isomer = null;
		try {
			/* motif*/
			SelectSparql motif_ss = getSelectD3Sparql_motif();
			SparqlEntity motif_se = motif_ss.getSparqlEntity();
			if (null == motif_ss.getSparqlEntity())
				motif_se = new SparqlEntity();
			motif_se.setValue(Saccharide.PrimaryId, primaryId);
			motif_ss.setSparqlEntity(motif_se);
			/* list.addAll(sparqlDAO.query(motif_ss));*/
			list_motif = sparqlDAO.query(motif_ss);
			
			/* isomer*/
			SelectSparql isomer_ss = getSelectD3Sparql_isomer();
			SparqlEntity isomer_se = isomer_ss.getSparqlEntity();
			if (null == isomer_ss.getSparqlEntity())
				isomer_se = new SparqlEntity();
			isomer_se.setValue(Saccharide.PrimaryId, primaryId);
			isomer_ss.setSparqlEntity(isomer_se);
			
			logger.debug(isomer_ss.getSparql());
			/* list.addAll(sparqlDAO.query(isomer_ss));*/
			list_isomer = sparqlDAO.query(isomer_ss);
			
		} catch (SparqlException e) {
			D3_Tree_json a = new D3_Tree_json();
			a.setName("sorry");
			return a;
		}
		SparqlEntity motif_se = null;
		SparqlEntity isomer_se = null;

		D3_Tree_json a = new D3_Tree_json();
		Tree_json b1 = new Tree_json();
		Tree_json b2 = new Tree_json();
		Tree_json b3 = new Tree_json();
		Tree_json b4 = new Tree_json();
		Tree_json b5 = new Tree_json();
		Tree_json b6 = new Tree_json();
		TreeSequence c2 = new TreeSequence();
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
			int k = 0;
			c2.setName("test_keiko");
			if (list_motif != null) {
				for (SparqlEntity i : list_motif) {
					TreeSequence c1 = new TreeSequence();
					motif_se = list_motif.get(j);
					// logger.debug("list.motif" + se.getValue("motif") + "<");
	
					String motifName = motif_se.getValue("motif");
					c1.setName(motifName);
					c1.setSize(1);
					if (c1.getName().length() == 0) {
						break;
					}  else {
						b1.setName("has_motif");
						if (c1.getName().equals(c2.getName())) { // c1Name.equals(c2Name)
							break;
						} else {
							c_list1.add(c1);
							k++;
						}
	
					}
					c2.setName(c1.getName());
					j++;
				}
				if (k != 0) {
					logger.debug("clist1がblistに");
					b1.setChildren(c_list1);
					b_list.add(b1);
				}
			}

			j = 0;
			k = 0;
			c2.setName("test_keiko");
			if (list_isomer != null) {
				for (SparqlEntity i : list_isomer) {
					TreeSequence c1 = new TreeSequence();
					isomer_se = list_isomer.get(j);
					String isomerName = isomer_se.getValue("isomer");
					c1.setName(isomerName);
					c1.setSize(2);
					if (c1.getName().length() == 0) {
						break;
					} else {
						b2.setName("has_linkage_isomer");
						if (c1.getName().equals(c2.getName())) { // c1Name.equals(c2Name)
							break;
						} else {
							c_list2.add(c1);
							k++;
						}
	
					}
					c2.setName(c1.getName());
					j++;
				}
				if (k != 0) {
					logger.debug("clist2がblistに");
					b2.setChildren(c_list2);
					b_list.add(b2);
				}
			}
			/*j = 0;
			k = 0;
			c2.setName("test_keiko");
			for (SparqlEntity i : list) {
				TreeSequence c1 = new TreeSequence();
				se = list.get(j);
				String superSName = se.getValue("superS");
				c1.setName(superSName);
				c1.setSize(3);
				if (c1.getName().length() == 0) {
					break;
				} else {
					b3.setName("has_superstructure");
					if (c1.getName().equals(c2.getName())) { // c1Name.equals(c2Name)
						break;
					} else {
						c_list3.add(c1);
						k++;
					}

				}
				c2.setName(c1.getName());
				j++;
			}
			if (k != 0) {
				logger.debug("clist3がblistに");
				b3.setChildren(c_list3);
				b_list.add(b3);
			}

			j = 0;
			k = 0;
			c2.setName("test_keiko");
			for (SparqlEntity i : list) {
				TreeSequence c1 = new TreeSequence();
				se = list.get(j);

				String subSName = se.getValue("subS");
				c1.setName(subSName);
				c1.setSize(4);
				if (c1.getName().length() == 0) {
					break;
				} else {
					b4.setName("has_substructure");
					if (c1.getName().equals(c2.getName())) { // c1Name.equals(c2Name)
						break;
					} else {
						c_list4.add(c1);
						k++;
					}

				}
				c2.setName(c1.getName());
				j++;
			}
			if (k != 0) {
				logger.debug("clist4がblistに");
				b4.setChildren(c_list4);
				b_list.add(b4);
			}

			j = 0;
			k = 0;
			c2.setName("test_keiko");
			for (SparqlEntity i : list) {
				TreeSequence c1 = new TreeSequence();
				se = list.get(j);
				String subsumeName = se.getValue("subsume");
				c1.setName(subsumeName);
				c1.setSize(5);
				if (c1.getName().length() == 0) {
					break;
				} else {
					b5.setName("subsumes");
					if (c1.getName().equals(c2.getName())) { // c1Name.equals(c2Name)
						break;
					} else {
						c_list5.add(c1);
						k++;
					}

				}
				c2.setName(c1.getName());
				j++;
			}
			if (k != 0) {
				logger.debug("clist5がblistに");
				b5.setChildren(c_list5);
				b_list.add(b5);
			}
			
			j = 0;
			k = 0;
			c2.setName("test_keiko");
			for (SparqlEntity i : list) {
				TreeSequence c1 = new TreeSequence();
				se = list.get(j);

				String subBName = se.getValue("subsumeB");
				c1.setName(subBName);
				c1.setSize(6);
				if (c1.getName().length() == 0) {
					break;
				} else {
					b6.setName("subsumed_by");
					if (c1.getName().equals(c2.getName())) { // c1Name.equals(c2Name)
						break;
					} else {
						c_list6.add(c1);
						k++;
					}

				}
				c2.setName(c1.getName());
				j++;
			}
			if (k != 0) {
				logger.debug("clist6がblistに");
				b6.setChildren(c_list6);
				b_list.add(b6);
			}
			
			logger.debug("list" + list + "<");*/

			String GlycanName = motif_se.getValue("id");
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


	
/*	@RequestMapping(value = "/D3retrieve2", method = RequestMethod.GET)
	@ApiOperation(value = "Retrieve the sequence of json of D3 Tree ", response = D3_Tree_json.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sequence retrieved successfully") })
	public @ResponseBody D3_Tree_json D3retrieve2(
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
		TreeSequence c2 = new TreeSequence();
		ArrayList<Tree_json> b_list = new ArrayList<Tree_json>();
		ArrayList<TreeSequence> c_list1 = new ArrayList<TreeSequence>(); // MAPにする？
		ArrayList<TreeSequence> c_list2 = new ArrayList<TreeSequence>();

		try {
			// table to d3 class
			int j = 0;
			int k = 0;
			c2.setName("test_keiko");
			for (SparqlEntity i : list) {
				TreeSequence c1 = new TreeSequence();
				se = list.get(j);
				// logger.debug("list.motif" + se.getValue("motif") + "<");

				String motifName = se.getValue("motif");
				c1.setName(motifName);
				c1.setSize(1);
				if (c1.getName().length() == 0) {
					break;
				} else {
					b1.setName("has_motif");
					if (c1.getName().equals(c2.getName())) { // c1Name.equals(c2Name)
						break;
					} else {
						c_list1.add(c1);
						k++;
					}

				}
				c2.setName(c1.getName());
				j++;
			}
			if (k != 0) {
				logger.debug("clist1がblistに");
				b1.setChildren(c_list1);
				b_list.add(b1);
			}

			j = 0;
			k = 0;
			c2.setName("test_keiko");
			for (SparqlEntity i : list) {
				TreeSequence c1 = new TreeSequence();
				se = list.get(j);
				String isomerName = se.getValue("isomer");
				c1.setName(isomerName);
				c1.setSize(2);
				if (c1.getName().length() == 0) {
					break;
				} else {
					b2.setName("has_linkage_isomer");
					if (c1.getName().equals(c2.getName())) { // c1Name.equals(c2Name)
						break;
					} else {
						c_list2.add(c1);
						k++;
					}

				}
				c2.setName(c1.getName());
				j++;
			}
			if (k != 0) {
				logger.debug("clist2がblistに");
				b2.setChildren(c_list2);
				b_list.add(b2);
			}
			
			logger.debug("list" + list + "<");

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
	
	@RequestMapping(value = "/D3retrieve3", method = RequestMethod.GET)
	@ApiOperation(value = "Retrieve the sequence of json of D3 Tree ", response = D3_Tree_json.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sequence retrieved successfully") })
	public @ResponseBody D3_Tree_json D3retrieve3(
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
		Tree_json b3 = new Tree_json();
		Tree_json b4 = new Tree_json();
		TreeSequence c2 = new TreeSequence();
		ArrayList<Tree_json> b_list = new ArrayList<Tree_json>();
		ArrayList<TreeSequence> c_list3 = new ArrayList<TreeSequence>();
		ArrayList<TreeSequence> c_list4 = new ArrayList<TreeSequence>();

		try {
			// table to d3 class
			int j = 0;
			int k = 0;
			c2.setName("test_keiko");
			for (SparqlEntity i : list) {
				TreeSequence c1 = new TreeSequence();
				se = list.get(j);
				String superSName = se.getValue("superS");
				c1.setName(superSName);
				c1.setSize(3);
				if (c1.getName().length() == 0) {
					break;
				} else {
					b3.setName("has_superstructure");
					if (c1.getName().equals(c2.getName())) { // c1Name.equals(c2Name)
						break;
					} else {
						c_list3.add(c1);
						k++;
					}

				}
				c2.setName(c1.getName());
				j++;
			}
			if (k != 0) {
				logger.debug("clist3がblistに");
				b3.setChildren(c_list3);
				b_list.add(b3);
			}

			j = 0;
			k = 0;
			c2.setName("test_keiko");
			for (SparqlEntity i : list) {
				TreeSequence c1 = new TreeSequence();
				se = list.get(j);

				String subSName = se.getValue("subS");
				c1.setName(subSName);
				c1.setSize(4);
				if (c1.getName().length() == 0) {
					break;
				} else {
					b4.setName("has_substructure");
					if (c1.getName().equals(c2.getName())) { // c1Name.equals(c2Name)
						break;
					} else {
						c_list4.add(c1);
						k++;
					}

				}
				c2.setName(c1.getName());
				j++;
			}
			if (k != 0) {
				logger.debug("clist4がblistに");
				b4.setChildren(c_list4);
				b_list.add(b4);
			}

			
			logger.debug("list" + list + "<");

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
	
	@RequestMapping(value = "/D3retrieve4", method = RequestMethod.GET)
	@ApiOperation(value = "Retrieve the sequence of json of D3 Tree ", response = D3_Tree_json.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sequence retrieved successfully") })
	public @ResponseBody D3_Tree_json D3retrieve4(
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
		Tree_json b5 = new Tree_json();
		Tree_json b6 = new Tree_json();
		TreeSequence c2 = new TreeSequence();
		ArrayList<Tree_json> b_list = new ArrayList<Tree_json>();
		ArrayList<TreeSequence> c_list5 = new ArrayList<TreeSequence>();
		ArrayList<TreeSequence> c_list6 = new ArrayList<TreeSequence>();

		try {
			// table to d3 class
			int j = 0;
			int k = 0;
			
			c2.setName("test_keiko");
			for (SparqlEntity i : list) {
				TreeSequence c1 = new TreeSequence();
				se = list.get(j);
				String subsumeName = se.getValue("subsume");
				c1.setName(subsumeName);
				c1.setSize(5);
				if (c1.getName().length() == 0) {
					break;
				} else {
					b5.setName("subsumes");
					if (c1.getName().equals(c2.getName())) { // c1Name.equals(c2Name)
						break;
					} else {
						c_list5.add(c1);
						k++;
					}

				}
				c2.setName(c1.getName());
				j++;
			}
			if (k != 0) {
				logger.debug("clist5がblistに");
				b5.setChildren(c_list5);
				b_list.add(b5);
			}
			
			j = 0;
			k = 0;
			c2.setName("test_keiko");
			for (SparqlEntity i : list) {
				TreeSequence c1 = new TreeSequence();
				se = list.get(j);

				String subBName = se.getValue("subsumeB");
				c1.setName(subBName);
				c1.setSize(6);
				if (c1.getName().length() == 0) {
					break;
				} else {
					b6.setName("subsumed_by");
					if (c1.getName().equals(c2.getName())) { // c1Name.equals(c2Name)
						break;
					} else {
						c_list6.add(c1);
						k++;
					}

				}
				c2.setName(c1.getName());
				j++;
			}
			if (k != 0) {
				logger.debug("clist6がblistに");
				b6.setChildren(c_list6);
				b_list.add(b6);
			}
			
			logger.debug("list" + list + "<");

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
	}*/
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