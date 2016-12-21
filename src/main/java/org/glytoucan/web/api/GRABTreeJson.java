package org.glytoucan.web.api;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.Saccharide;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class GRABTreeJson {

	String name;
//	Tree_json children;
	List<GRABTreeEachRelationship> children;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<GRABTreeEachRelationship> getChildren() {
		return children;
	}

	public void setChildren(List<GRABTreeEachRelationship> children) {
		this.children = children;
	}

}