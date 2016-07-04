package org.glytoucan.web.api;

import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@XmlRootElement(name="motif")
public class Motif {

	Integer motifId;
	String name;
	String sequence;
	
	Set<Glycan> glycans;
	
	/**
	 * @return the motifId
	 */
	@XmlAttribute
	public Integer getMotifId() {
		return motifId;
	}
	/**
	 * @param motifId the motifId to set
	 */
	public void setMotifId(Integer motifId) {
		this.motifId = motifId;
	}
	/**
	 * @return the name
	 */
	@XmlAttribute
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the sequence
	 */
	@XmlAttribute
	public String getSequence() {
		return sequence;
	}
	
	/**
	 * @param sequences the sequences to set
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	/**
	 * @return the glycans
	 */
	@XmlTransient  // so that from the motif we should not go back to glycans - prevent cycles
	@JsonIgnore
	public Set<Glycan> getGlycans() {
		return glycans;
	}
	/**
	 * @param glycans the glycans to set
	 */
	public void setGlycans(Set<Glycan> glycans) {
		this.glycans = glycans;
	}	
}