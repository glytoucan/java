package org.glytoucan.web.api;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.Saccharide;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class GRABGraphNodeData {
	
	private GRABGraphNodeIdSequence data;
	private GRABGraphNodePositionSequence position;
	private String group = "nodes";
	
	public GRABGraphNodeIdSequence getdata() {
		return data;
	}
	public void setdata(GRABGraphNodeIdSequence data) {
		this.data = data;
	}
	public GRABGraphNodePositionSequence getposition() {
		return position;
	}
	public void setposition(GRABGraphNodePositionSequence position) {
		this.position = position;
	}
	
	
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	
	
}
