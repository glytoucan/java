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
public class GRABTreeEachRelationship {

	String name;
//	TreeSequence children;
	List<GRABTreeSequence> children;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
		public List<GRABTreeSequence> getChildren() {
			return children;
		}

	public void setChildren(List<GRABTreeSequence> children) {
		this.children = children;
	}

}