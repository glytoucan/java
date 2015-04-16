package org.glytoucan.ws.api;

import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.glytoucan.ws.api.DateSerializer;
import org.glytoucan.ws.api.Motif;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Class to hold information about Glycans
 */

@XmlRootElement (name="glycan")
@JsonSerialize
public class Glycan {
	Integer glycanId;
	String accessionNumber;
    Date dateEntered;
	String structure;
	Integer structureLength;
	Double mass;
	Set<Motif> motifs;
	User contributor;
//	Set<GlycanComposition> compositions;
	
	/**
	 * @return the motifs
	 */
	@XmlElementWrapper (name="motifs")
	public Set<Motif> getMotifs() {
		return motifs;
	}

	/**
	 * @param motifs the motifs to set
	 */
	public void setMotifs(Set<Motif> motifs) {
		this.motifs = motifs;
	}

	@XmlAttribute
	public Integer getGlycanId() {
		return glycanId;
	}

	public void setGlycanId(Integer glycanId) {
		this.glycanId = glycanId;
	}

	@XmlAttribute
	public String getAccessionNumber() {
		return accessionNumber;
	}

	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	}

	@JsonSerialize(using = DateSerializer.class)
	@XmlAttribute
	public Date getDateEntered() {
		return dateEntered;
	}

	public void setDateEntered(Date dateEntered) {
		this.dateEntered = dateEntered;
	}

	@XmlElement(name="structure")
	public String getStructure() {
		return structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
	}

	@XmlAttribute
	public Integer getStructureLength() {
		return structureLength;
	}

	public void setStructureLength(Integer structureLength) {
		this.structureLength = structureLength;
	}

	/**
	 * @return the mass
	 */
	@XmlAttribute
	public Double getMass() {
		return mass;
	}

	/**
	 * @param mass the mass to set
	 */
	public void setMass(Double mass) {
		this.mass = mass;
	}
	
	public User getContributor() {
		return contributor;
	}

	public void setContributor(User contributor) {
		this.contributor = contributor;
	}


	/**
	 * @return the compositions
	 */
//	@XmlElementWrapper(name="glycan-compositions")
//	@XmlElement(name="glycan-composition")
//	public Set<GlycanComposition> getCompositions() {
//		return compositions;
//	}

	/**
	 * @param compositions the compositions to set
	 */
//	public void setCompositions(Set<GlycanComposition> compositions) {
//		this.compositions = compositions;
//	}	
}
