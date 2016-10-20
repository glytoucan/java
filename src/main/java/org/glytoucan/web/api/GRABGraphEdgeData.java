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

import scala.collection.immutable.Set;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class GRABGraphEdgeData {
	
	private GRABGraphEdgeIdSequence data;
	private String group = "edges";
	
	public GRABGraphEdgeIdSequence getdata() {
		return data;
	}
	public void setdata(GRABGraphEdgeIdSequence data) {
		this.data = data;
	}
	
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	
	
}
