package org.glytoucan.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.Glycosidic_topology;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.glycoinfo.rdf.utils.TripleStoreProperties;
import org.glytoucan.web.api.GRABTreeJson;
import org.glytoucan.web.api.GRABTreeSequence;
import org.glytoucan.web.api.GRABTreeEachRelationship;
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
public class GRABController {
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
	
	/*
	@Autowired
	@Qualifier(value = "d3SequenceSelectSparql_isomer")
	private SelectSparql selectD3Sparql_isomer;

	public void setSelectD3Sparql_isomer(SelectSparql read) {
		this.selectD3Sparql_isomer = read;
	}

	public SelectSparql getSelectD3Sparql_isomer() {
		return selectD3Sparql_isomer;
	}

	@Bean
	TripleStoreProperties getTripleStoreProperties() {
		return new TripleStoreProperties();
	}
	*/
	/*
	@Autowired
	@Qualifier(value = "d3SequenceSelectSparql_sub")
	private SelectSparql selectD3Sparql_sub;

	public void setSelectD3Sparql(SelectSparql read) {
		this.selectD3Sparql_sub = read;
	}

	public SelectSparql getSelectD3Sparql_sub() {
		return selectD3Sparql_sub;
	}
	
	@Autowired
	@Qualifier(value = "d3SequenceSelectSparql_super")
	private SelectSparql selectD3Sparql_super;

	public void setSelectD3Sparql(SelectSparql read) {
		this.selectD3Sparql_super = read;
	}

	public SelectSparql getSelectD3Sparql_super() {
		return selectD3Sparql_super;
	}
	*/
	
	/*
	@Autowired
	@Qualifier(value = "GRABSequenceSelectSparql_subsumes")
	private SelectSparql selectGRABSparql_subsumes;

	public void setSelectGRABSparql_subsumes(SelectSparql read) {
		this.selectGRABSparql_subsumes = read;
	}

	public SelectSparql getSelectGRABSparql_subsumes() {
		return selectGRABSparql_subsumes;
	}
	
	@Autowired
	@Qualifier(value = "GRABSequenceSelectSparql_subsumedby")
	private SelectSparql selectGRABSparql_subsumedby;

	public void setSelectGRABSparql_subsumedby(SelectSparql read) {
		this.selectGRABSparql_subsumedby = read;
	}

	public SelectSparql getSelectGRABSparql_subsumedby() {
		return selectGRABSparql_subsumedby;
	}
	*/
	
	@Autowired
	@Qualifier(value = "d3SequenceSelectSparql_topology")
	private SelectSparql selectD3Sparql_topology;

	public void setSelectD3Sparql_topology(SelectSparql read) {
		this.selectD3Sparql_topology = read;
	}

	public SelectSparql getSelectD3Sparql_topology() {
		return selectD3Sparql_topology;
	}
	
	@Autowired
	@Qualifier(value = "d3SequenceSelectSparql_topologyby")
	private SelectSparql selectD3Sparql_topologyby;

	public void setSelectD3Sparql_topologyby(SelectSparql read) {
		this.selectD3Sparql_topologyby = read;
	}

	public SelectSparql getSelectD3Sparql_topologyby() {
		return selectD3Sparql_topologyby;
	}


	private final AtomicLong counter = new AtomicLong();

	@RequestMapping(value = "/D3retrieve", method = RequestMethod.GET)
	@ApiOperation(value = "Retrieve the sequence of json of D3 Tree ", response = GRABTreeJson.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sequence retrieved successfully") })
	public @ResponseBody GRABTreeJson D3retrieve(
			@ApiParam(required = true, value = "id of the sequence") @RequestParam(value = "primaryId", defaultValue = "G00030MO") String primaryId) {
		logger.debug("primaryId=" + primaryId + "<");
		// error messageを返す（形式が違うもの）
		/*List<SparqlEntity> list = new ArrayList<SparqlEntity>();*/
		List<SparqlEntity> list_motif = null;
		//List<SparqlEntity> list_isomer = null;
		List<SparqlEntity> list_topology = null;
		List<SparqlEntity> list_topologyby = null;
		
		/*
		 List<SparqlEntity> list_subsumes = null;
		 List<SparqlEntity> list_subsumedby = null;	
		 List<SparqlEntity> list_sub = null;
		 List<SparqlEntity> list_super = null; 
		*/
		
		try {
			/* motif*/
			SelectSparql motif_ss = getSelectD3Sparql_motif();
			SparqlEntity motif_se = motif_ss.getSparqlEntity();
			if (null == motif_ss.getSparqlEntity())
				motif_se = new SparqlEntity();
			motif_se.setValue(Saccharide.PrimaryId, primaryId);
			motif_ss.setSparqlEntity(motif_se);
			logger.debug(motif_ss.getSparql());
			list_motif = sparqlDAO.query(motif_ss);
			
//			/* isomer*/
//			SelectSparql isomer_ss = getSelectD3Sparql_isomer();
//			SparqlEntity isomer_se = isomer_ss.getSparqlEntity();
//			if (null == isomer_ss.getSparqlEntity())
//				isomer_se = new SparqlEntity();
//			isomer_se.setValue(Saccharide.PrimaryId, primaryId);
//			isomer_ss.setSparqlEntity(isomer_se);	
//			logger.debug(isomer_ss.getSparql());
//			list_isomer = sparqlDAO.query(isomer_ss);
			
			// topology
			SelectSparql topology_ss = getSelectD3Sparql_topology();
			SparqlEntity topology_se = topology_ss.getSparqlEntity();
			if (null == topology_ss.getSparqlEntity())
				topology_se = new SparqlEntity();
			topology_se.setValue(Glycosidic_topology.PrimaryId_1, primaryId);
			topology_ss.setSparqlEntity(topology_se);
			logger.debug(topology_ss.getSparql());
			list_topology = sparqlDAO.query(topology_ss);
			
			// topologyby
			SelectSparql topologyby_ss = getSelectD3Sparql_topologyby();
			SparqlEntity topologyby_se = topologyby_ss.getSparqlEntity();
			if (null == topologyby_ss.getSparqlEntity())
				topologyby_se = new SparqlEntity();
			topologyby_se.setValue(Glycosidic_topology.PrimaryId_2, primaryId);
			topologyby_ss.setSparqlEntity(topologyby_se);
			logger.debug(topologyby_ss.getSparql());
			list_topologyby = sparqlDAO.query(topologyby_ss);		
			
			/* sub
			SelectSparql sub_ss = getSelectD3Sparql_sub();
			SparqlEntity sub_se = sub_ss.getSparqlEntity();
			if (null == sub_ss.getSparqlEntity())
				sub_se = new SparqlEntity();
			sub_se.setValue(Saccharide.PrimaryId, primaryId);
			sub_ss.setSparqlEntity(sub_se);		
			logger.debug(sub_ss.getSparql());
			list_sub = sparqlDAO.query(sub_ss);
			*/
			
			/* super
			SelectSparql super_ss = getSelectD3Sparql_super();
			SparqlEntity super_se = super_ss.getSparqlEntity();
			if (null == super_ss.getSparqlEntity())
				super_se = new SparqlEntity();
			super_se.setValue(Saccharide.PrimaryId, primaryId);
			super_ss.setSparqlEntity(super_se);
			logger.debug(super_ss.getSparql());
			list_super = sparqlDAO.query(super_ss);
			*/
			
		} catch (SparqlException e) {
			GRABTreeJson a = new GRABTreeJson();
			a.setName("sorry");
			return a;
		}
		SparqlEntity motif_se = null;
		//SparqlEntity isomer_se = null;
		SparqlEntity topology_se = null;
		SparqlEntity topologyby_se = null;
		
		/*
		SparqlEntity sub_se = null;
		SparqlEntity super_se = null;
		*/

		GRABTreeJson a = new GRABTreeJson();
		GRABTreeEachRelationship b1 = new GRABTreeEachRelationship();
		GRABTreeEachRelationship b2 = new GRABTreeEachRelationship();
		GRABTreeEachRelationship b3 = new GRABTreeEachRelationship();
		GRABTreeEachRelationship b4 = new GRABTreeEachRelationship();
		GRABTreeEachRelationship b5 = new GRABTreeEachRelationship();
		GRABTreeEachRelationship b6 = new GRABTreeEachRelationship();
		GRABTreeEachRelationship b7 = new GRABTreeEachRelationship();
		GRABTreeSequence c2 = new GRABTreeSequence();
		ArrayList<GRABTreeEachRelationship> b_list = new ArrayList<GRABTreeEachRelationship>();
		ArrayList<GRABTreeSequence> c_list1 = new ArrayList<GRABTreeSequence>(); // MAPにする？
		ArrayList<GRABTreeSequence> c_list2 = new ArrayList<GRABTreeSequence>();
		ArrayList<GRABTreeSequence> c_list3 = new ArrayList<GRABTreeSequence>();
		ArrayList<GRABTreeSequence> c_list4 = new ArrayList<GRABTreeSequence>();
		ArrayList<GRABTreeSequence> c_list5 = new ArrayList<GRABTreeSequence>();
		ArrayList<GRABTreeSequence> c_list6 = new ArrayList<GRABTreeSequence>();
		ArrayList<GRABTreeSequence> c_list7 = new ArrayList<GRABTreeSequence>(); //No_reraltion_data
		int check = 0;
		
		try {
			// table to d3 class
			int j = 0;
			int k = 0;
			c2.setName("test_TreeJson");
			if (list_motif != null) {
				for (SparqlEntity i : list_motif) {
					GRABTreeSequence c1 = new GRABTreeSequence();
					motif_se = list_motif.get(j);
					// logger.debug("list.motif" + se.getValue("motif") + "<");
	
					String motifName = motif_se.getValue("motif");
					c1.setName(motifName);
					c1.setSize(1);
					if (c1.getName().length() == 0) {
						check++;
						logger.debug("check="+ check);
						c1.setName("None");
						c_list1.add(c1);
						b1.setName("has_motif");
						b1.setChildren(c_list1);
						b_list.add(b1);
						break;
					}  else {
						b1.setName("has_motif");
						c_list1.add(c1);
						k++;
						/* 昔のverのmotifの場所にだけ残しておく、他の場所は消す
						if (c1.getName().equals(c2.getName())) { // c1Name.equals(c2Name)
							break;
						} else {
							c_list1.add(c1);
							k++;
						}
						*/
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

//			j = 0;
//			k = 0;
//			c2.setName("test_TreeJson");
//			if (list_isomer != null) {
//				for (SparqlEntity i : list_isomer) {
//					GRABTreeSequence c1 = new GRABTreeSequence();
//					isomer_se = list_isomer.get(j);
//					String isomerName = isomer_se.getValue("isomer");
//					c1.setName(isomerName);
//					c1.setSize(2);
//					if (c1.getName().length() == 0) {
//						check++;
//						logger.debug("check="+ check);
//						c1.setName("None");
//						c_list2.add(c1);
//						b2.setName("has_linkage_isomer");
//						b2.setChildren(c_list2);
//						b_list.add(b2);
//						break;
//					} else {
//						b2.setName("has_linkage_isomer");
//						c_list2.add(c1);
//						k++;
//					}
//					c2.setName(c1.getName());
//					j++;
//				}
//				if (k != 0) {
//					logger.debug("clist2がblistに");
//					b2.setChildren(c_list2);
//					b_list.add(b2);
//				}
//			}

			// subsumes
			j = 0;
			k = 0;
			c2.setName("test_TreeJson");
			if (list_topology != null) {
				for (SparqlEntity i : list_topology) {
					GRABTreeSequence c1 = new GRABTreeSequence();
					topology_se = list_topology.get(j);
					String topologyName = topology_se.getValue("topology_id");
					c1.setName(topologyName);
					c1.setSize(5);
					if (c1.getName().length() == 0) {
						check++;
						logger.debug("check="+ check);
						c1.setName("None");
						c_list5.add(c1);
						b5.setName("subsumes");
						b5.setChildren(c_list5);
						b_list.add(b5);
						break;
					} else {
						b5.setName("subsumes");
						c_list5.add(c1);
						k++;
					}
					c2.setName(c1.getName());
					j++;
				}
				if (k != 0) {
					logger.debug("clist5がblistに");
					b5.setChildren(c_list5);
					b_list.add(b5);
				}
			}

			// subsumed_by
			j = 0;
			k = 0;
			c2.setName("test_TreeJson");
			if (list_topologyby != null) {
				for (SparqlEntity i : list_topologyby) {
					GRABTreeSequence c1 = new GRABTreeSequence();
					topologyby_se = list_topologyby.get(j);
					String topologyByName = topologyby_se.getValue("id");
					c1.setName(topologyByName);
					c1.setSize(6);
					if (c1.getName().length() == 0) {
						check++;
						logger.debug("check="+ check);
						c1.setName("None");
						c_list6.add(c1);
						b6.setName("subsumed_by");
						b6.setChildren(c_list6);
						b_list.add(b6);
						break;
					} else {
						b6.setName("subsumed_by");
						c_list6.add(c1);
						k++;
					}
					c2.setName(c1.getName());
					j++;
				}
				if (k != 0) {
					logger.debug("clist6がblistに");
					b6.setChildren(c_list6);
					b_list.add(b6);
				}
			}
			
//			// topology
//			j = 0;
//			k = 0;
//			c2.setName("test_TreeJson");
//			if (list_topology != null) {
//				for (SparqlEntity i : list_topology) {
//					GRABTreeSequence c1 = new GRABTreeSequence();
//					topology_se = list_topology.get(j);
//					String topologyName = topology_se.getValue("topology_id");
//					c1.setName(topologyName);
//					c1.setSize(5);
//					if (c1.getName().length() == 0) {
//						check++;
//						logger.debug("check="+ check);
//						c1.setName("None");
//						c_list5.add(c1);
//						b5.setName("has_topology");
//						b5.setChildren(c_list5);
//						b_list.add(b5);
//						break;
//					} else {
//						b5.setName("has_topology");
//						c_list5.add(c1);
//						k++;
//					}
//					c2.setName(c1.getName());
//					j++;
//				}
//				if (k != 0) {
//					logger.debug("clist5がblistに");
//					b5.setChildren(c_list5);
//					b_list.add(b5);
//				}
//			}
//
//			//topologyby
//			j = 0;
//			k = 0;
//			c2.setName("test_TreeJson");
//			if (list_topologyby != null) {
//				for (SparqlEntity i : list_topologyby) {
//					GRABTreeSequence c1 = new GRABTreeSequence();
//					topologyby_se = list_topologyby.get(j);
//					String topologyByName = topologyby_se.getValue("id");
//					c1.setName(topologyByName);
//					c1.setSize(6);
//					if (c1.getName().length() == 0) {
//						check++;
//						logger.debug("check="+ check);
//						c1.setName("None");
//						c_list6.add(c1);
//						b6.setName("topology_contained_by");
//						b6.setChildren(c_list6);
//						b_list.add(b6);
//						break;
//					} else {
//						b6.setName("topology_contained_by");
//						c_list6.add(c1);
//						k++;
//					}
//					c2.setName(c1.getName());
//					j++;
//				}
//				if (k != 0) {
//					logger.debug("clist6がblistに");
//					b6.setChildren(c_list6);
//					b_list.add(b6);
//				}
//			}
			
			/* super
			j = 0;
			k = 0;
			c2.setName("test_TreeJson");
			if (list_super != null) {
				for (SparqlEntity i : list_super) {
					TreeSequence c1 = new TreeSequence();
					super_se = list_super.get(j);
					String superSName = super_se.getValue("superS");
					c1.setName(superSName);
					c1.setSize(3);
					if (c1.getName().length() == 0) {
						break;
					} else {
						b3.setName("has_superstructure");
						c_list3.add(c1);
						k++;
					}
					c2.setName(c1.getName());
					j++;
				}
				if (k != 0) {
					logger.debug("clist3がblistに");
					b3.setChildren(c_list3);
					b_list.add(b3);
				}
			}
			*/
			
			/* sub
			j = 0;
			k = 0;
			c2.setName("test_TreeJson");
			if (list_sub != null) {
				for (SparqlEntity i : list_sub) {
					TreeSequence c1 = new TreeSequence();
					sub_se = list_sub.get(j);
	
					String subSName = sub_se.getValue("subS");
					c1.setName(subSName);
					c1.setSize(4);
					if (c1.getName().length() == 0) {
						break;
					} else {
						b4.setName("has_substructure");
						c_list4.add(c1);
						k++;
					}
					c2.setName(c1.getName());
					j++;
				}
				if (k != 0) {
					logger.debug("clist4がblistに");
					b4.setChildren(c_list4);
					b_list.add(b4);
				}
			}
			*/
			
			
			
			/*logger.debug("list" + list + "<");*/

			/*String GlycanName = motif_se.getValue("id");
			a.setName(GlycanName);*/
			a.setName(primaryId);
			a.setChildren(b_list);
			
			/* 全体で None を出したい時
			if (check != 2 (motif and isomer = 2) ){	
				logger.debug("into_normal");
				a.setChildren(b_list);
			}else{
				logger.debug("into_b7");
				b7.setName("None");
				b7.setChildren(null);
				b_list.add(b7);
				a.setChildren(b_list);
			}
			*/

		} catch (java.lang.IndexOutOfBoundsException ie) {
			return null;
		}
		// GlycoSequence gs = new GlycoSequence(counter.incrementAndGet(),
		// primaryId);
		// gs.setSequence(se.getValue(org.glycoinfo.rdf.glycan.GlycoSequence.Sequence));

		return a;
	}


	
//	@RequestMapping(value = "/D3retrieve2", method = RequestMethod.GET)
//	@ApiOperation(value = "Retrieve the sequence of json of D3 Tree ", response = GRABTreeJson.class)
//	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sequence retrieved successfully") })
//	public @ResponseBody GRABTreeJson D3retrieve2(
//			@ApiParam(required = true, value = "id of the sequence") @RequestParam(value = "primaryId", defaultValue = "G00030MO") String primaryId) {
//		logger.debug("primaryId=" + primaryId + "<");	
//		// error messageを返す（形式が違うもの）
//		List<SparqlEntity> list_motif = null;
//		List<SparqlEntity> list_isomer = null;
//
//		try {
//			/* motif*/
//			SelectSparql motif_ss = getSelectD3Sparql_motif();
//			SparqlEntity motif_se = motif_ss.getSparqlEntity();
//			if (null == motif_ss.getSparqlEntity())
//				motif_se = new SparqlEntity();
//			motif_se.setValue(Saccharide.PrimaryId, primaryId);
//			motif_ss.setSparqlEntity(motif_se);
//			logger.debug(motif_ss.getSparql());
//			list_motif = sparqlDAO.query(motif_ss);
//			
//			/* isomer*/
//			SelectSparql isomer_ss = getSelectD3Sparql_isomer();
//			SparqlEntity isomer_se = isomer_ss.getSparqlEntity();
//			if (null == isomer_ss.getSparqlEntity())
//				isomer_se = new SparqlEntity();
//			isomer_se.setValue(Saccharide.PrimaryId, primaryId);
//			isomer_ss.setSparqlEntity(isomer_se);	
//			logger.debug(isomer_ss.getSparql());
//			list_isomer = sparqlDAO.query(isomer_ss);
//		} catch (SparqlException e) {
//			GRABTreeJson a = new GRABTreeJson();
//			a.setName("sorry");
//			return a;
//		}
//		SparqlEntity motif_se = null;
//		SparqlEntity isomer_se = null;
//
//		GRABTreeJson a = new GRABTreeJson();
//		GRABTreeEachRelationship b1 = new GRABTreeEachRelationship();
//		GRABTreeEachRelationship b2 = new GRABTreeEachRelationship();
//		GRABTreeSequence c2 = new GRABTreeSequence();
//		ArrayList<GRABTreeEachRelationship> b_list = new ArrayList<GRABTreeEachRelationship>();
//		ArrayList<GRABTreeSequence> c_list1 = new ArrayList<GRABTreeSequence>(); // MAPにする？
//		ArrayList<GRABTreeSequence> c_list2 = new ArrayList<GRABTreeSequence>();
//		int check = 0;
//		
//		try {
//			// table to d3 class
//			int j = 0;
//			int k = 0;
//			c2.setName("test_TreeJson");
//			if (list_motif != null) {
//				for (SparqlEntity i : list_motif) {
//					GRABTreeSequence c1 = new GRABTreeSequence();
//					motif_se = list_motif.get(j);
//					// logger.debug("list.motif" + se.getValue("motif") + "<");
//	
//					String motifName = motif_se.getValue("motif");
//					c1.setName(motifName);
//					c1.setSize(1);
//					if (c1.getName().length() == 0) {
//						check++;
//						logger.debug("check="+ check);
//						break;
//					}  else {
//						b1.setName("has_motif");
//						c_list1.add(c1);
//						k++;
//						/* 昔のverのmotifの場所にだけ残しておく、他の場所は消す
//						if (c1.getName().equals(c2.getName())) { // c1Name.equals(c2Name)
//							break;
//						} else {
//							c_list1.add(c1);
//							k++;
//						}
//						*/
//					}
//					c2.setName(c1.getName());
//					j++;
//				}
//				if (k != 0) {
//					logger.debug("clist1がblistに");
//					b1.setChildren(c_list1);
//					b_list.add(b1);
//				}
//			}
//
//			j = 0;
//			k = 0;
//			c2.setName("test_TreeJson");
//			if (list_isomer != null) {
//				for (SparqlEntity i : list_isomer) {
//					GRABTreeSequence c1 = new GRABTreeSequence();
//					isomer_se = list_isomer.get(j);
//					String isomerName = isomer_se.getValue("isomer");
//					c1.setName(isomerName);
//					c1.setSize(2);
//					if (c1.getName().length() == 0) {
//						check++;
//						logger.debug("check="+ check);
//						break;
//					} else {
//						b2.setName("has_linkage_isomer");
//						c_list2.add(c1);
//						k++;
//					}
//					c2.setName(c1.getName());
//					j++;
//				}
//				if (k != 0) {
//					logger.debug("clist2がblistに");
//					b2.setChildren(c_list2);
//					b_list.add(b2);
//				}
//			}
//			a.setName(primaryId);
//			a.setChildren(b_list);
//
//		} catch (java.lang.IndexOutOfBoundsException ie) {
//			return null;
//		}
//		// GlycoSequence gs = new GlycoSequence(counter.incrementAndGet(),
//		// primaryId);
//		// gs.setSequence(se.getValue(org.glycoinfo.rdf.glycan.GlycoSequence.Sequence));
//
//		return a;
//	}
	
/*	@RequestMapping(value = "/D3retrieve3", method = RequestMethod.GET)
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
			c2.setName("test_TreeJson");
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
			c2.setName("test_TreeJson");
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
			
			c2.setName("test_TreeJson");
			for (SparqlEntity i : list) {
				TreeSequence c1 = new TreeSequence();
				se = list.get(j);
				String subsumeName = se.getValue("subsume");
				c1.setName(subsumeName);
				c1.setSize(5);
				if (c1.getName().length() == 0) {
					break;
				} else {
					b5.setName("topology");
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
			c2.setName("test_TreeJson");
			for (SparqlEntity i : list) {
				TreeSequence c1 = new TreeSequence();
				se = list.get(j);

				String subBName = se.getValue("subsumeB");
				c1.setName(subBName);
				c1.setSize(6);
				if (c1.getName().length() == 0) {
					break;
				} else {
					b6.setName("topology_by");
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