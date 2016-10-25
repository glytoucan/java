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
import org.glycoinfo.rdf.utils.TripleStoreProperties;
import org.glytoucan.web.api.GRABGraphEdgeData;
import org.glytoucan.web.api.GRABGraphEdgeIdSequence;
import org.glytoucan.web.api.GRABGraphNodeData;
import org.glytoucan.web.api.GRABGraphNodeIdSequence;
import org.glytoucan.web.api.GRABGraphNodePositionSequence;
import org.glytoucan.web.api.GRAB_Graph_json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Api(value = "/Graph", description = "GRAB Graph format")
@RequestMapping("/Graph")
public class GRABGraphController {
	protected Log logger = LogFactory.getLog(getClass());

	@Autowired
	SparqlDAO sparqlDAO;

	@Bean
	TripleStoreProperties getTripleStoreProperties() {
		return new TripleStoreProperties();
	}
	
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
	

	private final AtomicLong counter = new AtomicLong();

	@RequestMapping(value = "/Graphretrieve", method = RequestMethod.GET)
	@ApiOperation(value = "Retrieve the sequence of json of GRAB Graph ")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Sequence retrieved successfully") })
	public @ResponseBody String Graphretrieve(
			@ApiParam(required = true, value = "id of the sequence") @RequestParam(value = "primaryId", defaultValue = "G00030MO") String primaryId) {
		logger.debug("primaryId=" + primaryId + "<");
		// error messageを返す（形式が違うもの）
		/*List<SparqlEntity> list = new ArrayList<SparqlEntity>();*/
		List<SparqlEntity> list_subsumes = null;
		List<SparqlEntity> list_subsumedby = null;	
		/*
		 List<SparqlEntity> list_sub = null;
		 List<SparqlEntity> list_super = null; 
		*/
		
		try {
			// subsumes
			SelectSparql subsumes_ss = getSelectGRABSparql_subsumes();
			SparqlEntity subsumes_se = subsumes_ss.getSparqlEntity();
			if (null == subsumes_ss.getSparqlEntity())
				subsumes_se = new SparqlEntity();
			subsumes_se.setValue(Glycosidic_topology.PrimaryId_1, primaryId);
			subsumes_ss.setSparqlEntity(subsumes_se);
			logger.debug(subsumes_ss.getSparql());
			list_subsumes = sparqlDAO.query(subsumes_ss);
			
			// subsumedby
			SelectSparql subsumedby_ss = getSelectGRABSparql_subsumedby();
			SparqlEntity subsumedby_se = subsumedby_ss.getSparqlEntity();
			if (null == subsumedby_ss.getSparqlEntity())
				subsumedby_se = new SparqlEntity();
			subsumedby_se.setValue(Glycosidic_topology.PrimaryId_2, primaryId);
			subsumedby_ss.setSparqlEntity(subsumedby_se);
			logger.debug(subsumedby_ss.getSparql());
			list_subsumedby = sparqlDAO.query(subsumedby_ss);		
			
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
			GRAB_Graph_json a = new GRAB_Graph_json();
			a.setGrabGraphjson("sorry");
			return a.getGrabGraphjson();
		}
		SparqlEntity subsumes_se = null;
		SparqlEntity subsumedby_se = null;
		/*
		SparqlEntity sub_se = null;
		SparqlEntity super_se = null;
		*/

		GRAB_Graph_json a = new GRAB_Graph_json();
		ArrayList<GRABGraphNodeData> nodeData_list = new ArrayList<GRABGraphNodeData>();
		//ArrayList<GRABGraphEdgeData> edgeData_list = new ArrayList<GRABGraphEdgeData>();
		
		try {
			// subsumes
			int j = 0;
			int sparqlCount = 0;
			int count = 1;
			int x = 0;
			int y = 0;	
			if (list_subsumes != null) {
				for (SparqlEntity k : list_subsumes) {
					subsumes_se = list_subsumes.get(j);
					sparqlCount++;
				}
				for (SparqlEntity i : list_subsumes) {
					subsumes_se = list_subsumes.get(j);
					String subsumesName = subsumes_se.getValue("subsumes_id");
					GRABGraphNodeData nodeData = new GRABGraphNodeData(); 
					//GRABGraphEdgeData edgeData = new GRABGraphEdgeData();
					GRABGraphNodeIdSequence nodeIdSeq = new GRABGraphNodeIdSequence();
					GRABGraphNodePositionSequence nodePosSeq = new GRABGraphNodePositionSequence();
					//GRABGraphEdgeIdSequence edgeIdSeq = new GRABGraphEdgeIdSequence();
					GRABGraphNodeIdSequence nodeCheckSeq = new GRABGraphNodeIdSequence();
					nodeCheckSeq.setId(subsumesName);
					
					if(nodeCheckSeq.getId().length() == 0){
						break;
					}
					
					nodeIdSeq.setId(subsumesName);
					nodeIdSeq.setParent("subsumes_box");
					
					x = -170 + (j /5 * -70); 
					y = j % 5;
					int yy[] = {-100,-50,0,50,100};
					if (sparqlCount < 5){
						yy[0] = 0;
						yy[1] = 50;
						yy[2] = -50;
						yy[3] = 100;
						yy[4] = -100;
					} 
					nodePosSeq.setX(x); //計算
					nodePosSeq.setY(yy[y]);
					nodeData.setdata(nodeIdSeq);
					nodeData.setposition(nodePosSeq);
					nodeData_list.add(nodeData);
					
					/* Edgeは必要なくなった
					edgeIdSeq.setId("subsumes"+ count);
					edgeIdSeq.setSource("subsumes");
					edgeIdSeq.setTarget(subsumesName);
					edgeData.setdata(edgeIdSeq);
					edgeData_list.add(edgeData);
					 */	
					count++;
					j++;
				}
				logger.debug("count:"+count);
			}

			//subsumedby
			j = 0;
			sparqlCount = 0;
			count = 1;
			x = 0;
			y = 0;	
			if (list_subsumedby != null) {
				for (SparqlEntity k : list_subsumedby) {
					subsumedby_se = list_subsumedby.get(j);
					sparqlCount++;
				}
				logger.debug("subsumedby:"+sparqlCount);
				for (SparqlEntity i : list_subsumedby) {
					subsumedby_se = list_subsumedby.get(j);
					String subsumedByName = subsumedby_se.getValue("id");
					GRABGraphNodeData nodeData = new GRABGraphNodeData(); 
					//GRABGraphEdgeData edgeData = new GRABGraphEdgeData();
					GRABGraphNodeIdSequence nodeIdSeq = new GRABGraphNodeIdSequence();
					GRABGraphNodePositionSequence nodePosSeq = new GRABGraphNodePositionSequence();
					//GRABGraphEdgeIdSequence edgeIdSeq = new GRABGraphEdgeIdSequence();
					GRABGraphNodeIdSequence nodeCheckSeq = new GRABGraphNodeIdSequence();
					nodeCheckSeq.setId(subsumedByName);
					
					if(nodeCheckSeq.getId().length() == 0){
						break;
					}
					
					nodeIdSeq.setId(subsumedByName);
					nodeIdSeq.setParent("subsumedby_box");
					
					x = 170 + (j / 5 * 70); 
					y = j % 5;
					int yy[] = {-100,-50,0,50,100};
					if (sparqlCount < 5){
						yy[0] = 0;
						yy[1] = 50;
						yy[2] = -50;
						yy[3] = 100;
						yy[4] = -100;
					} 
					nodePosSeq.setX(x); //計算
					nodePosSeq.setY(yy[y]);					
					nodeData.setdata(nodeIdSeq);
					nodeData.setposition(nodePosSeq);
					nodeData_list.add(nodeData);
					
					/* Edgeは必要なくなった
					edgeIdSeq.setId("subsumedby"+ count);
					edgeIdSeq.setSource("subsumedby");
					edgeIdSeq.setTarget(subsumedByName);
					edgeData.setdata(edgeIdSeq);
					edgeData_list.add(edgeData);
					 */	
					
					count++;
					j++;
				}
				logger.debug("count:"+count);
				logger.debug("j:"+j);
			}
			
			/* super
			j = 0;
			count = 1;
			sparqlCount = 0;
			x = 0;
			y = 0;	
			if (list_super != null) {
				for (SparqlEntity k : list_super) {
					super_se = list_super.get(j);
					sparqlCount++;
				}
				for (SparqlEntity i : list_super) {
					super_se = list_super.get(j);
					String superName = super_se.getValue("superS"); //sparqlのカラム名に変更する
					GRABGraphNodeData nodeData = new GRABGraphNodeData(); 
					GRABGraphEdgeData edgeData = new GRABGraphEdgeData();
					GRABGraphNodeIdSequence nodeIdSeq = new GRABGraphNodeIdSequence();
					GRABGraphNodePositionSequence nodePosSeq = new GRABGraphNodePositionSequence();
					GRABGraphEdgeIdSequence edgeIdSeq = new GRABGraphEdgeIdSequence();
					GRABGraphNodeIdSequence nodeCheckSeq = new GRABGraphNodeIdSequence();
					nodeCheckSeq.setId(superName);
					
					if(nodeCheckSeq.getId().length() == 0){
						break;
					}
					
					nodeIdSeq.setId(superName);
					nodeIdSeq.setParent("superstructure_box");
					
					x = j % 10;
					int xx[] = {-140, -70, 0, 70, 140};
					if (sparqlCount < 5){
						xx[0] = 0;
						xx[1] = 70;
						xx[2] = -70;
						xx[3] = 140;
						xx[4] = -140;
					} 
					y = -200 + (j / 5 * -50); 		
					nodePosSeq.setX(xx[x]); //計算
					nodePosSeq.setY(y);
					
					nodeData.setdata(nodeIdSeq);
					nodeData.setposition(nodePosSeq);
					nodeData_list.add(nodeData);
					
					/* Edgeは必要なくなった
					edgeIdSeq.setId("superstructure"+ count);
					edgeIdSeq.setSource("superstructure");
					edgeIdSeq.setTarget(superName);
					edgeData.setdata(edgeIdSeq);
					edgeData_list.add(edgeData);
					
					count++;
					j++;
				}
			}
			*/
			
			/* sub
			j = 0;
			sparqlCount = 0;
			count = 1;
			x = 0;
			y = 0;	
			if (list_sub != null) {
				for (SparqlEntity k : list_sub) {
					sub_se = list_sub.get(j);
					sparqlCount++;
				}
				for (SparqlEntity i : list_sub) {
					sub_se = list_sub.get(j);
					String subName = sub_se.getValue("subS"); //sparqlのカラム名に変更する
					GRABGraphNodeData nodeData = new GRABGraphNodeData(); 
					GRABGraphEdgeData edgeData = new GRABGraphEdgeData();
					GRABGraphNodeIdSequence nodeIdSeq = new GRABGraphNodeIdSequence();
					GRABGraphNodePositionSequence nodePosSeq = new GRABGraphNodePositionSequence();
					GRABGraphEdgeIdSequence edgeIdSeq = new GRABGraphEdgeIdSequence();
					GRABGraphNodeIdSequence nodeCheckSeq = new GRABGraphNodeIdSequence();
					nodeCheckSeq.setId(subName);
					
					if(nodeCheckSeq.getId().length() == 0){
						break;
					}
					
					nodeIdSeq.setId(subName);
					nodeIdSeq.setParent("substructure_box");
					
					x = j % 10;
					int xx[] = {-140, -70, 0, 70, 140};
					if (sparqlCount < 5){
						xx[0] = 0;
						xx[1] = 70;
						xx[2] = -70;
						xx[3] = 140;
						xx[4] = -140;
					} 
					y = 200 + (j /5 * 50); 		
					nodePosSeq.setX(xx[x]); //計算
					nodePosSeq.setY(y);
					
					nodeData.setdata(nodeIdSeq);
					nodeData.setposition(nodePosSeq);
					nodeData_list.add(nodeData);
					
					/* Edgeは必要なくなった
					edgeIdSeq.setId("substructure"+ count);
					edgeIdSeq.setSource("substructure");
					edgeIdSeq.setTarget(subName);
					edgeData.setdata(edgeIdSeq);
					edgeData_list.add(edgeData);
					
					count++;
					j++;
				}
			}
			*/
			
			ObjectMapper mapper = new ObjectMapper();
			String nodes_result = mapper.writeValueAsString(nodeData_list);
			//String edges_result = mapper.writeValueAsString(edgeData_list);
			String change1 = "\\[";
			String change2 = "\\]";
			String nodes_result1 = nodes_result.replaceAll(change1, ",");
			//String edges_result1 = edges_result.replaceAll(change1, ",");
			String nodes_string = nodes_result1.replaceAll(change2, "");
			//String edges_string = edges_result1.replaceAll(change2, "");
			
			logger.debug("nodeData" + nodes_string);
			
			a.setCenterGlycanId(primaryId);
			a.setNodeString(nodes_string);
			//a.setEdgeString(edges_string);

		} catch (java.lang.IndexOutOfBoundsException ie) {
			return null;
		} catch (JsonProcessingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return a.getGrabGraphjson();
	}
}