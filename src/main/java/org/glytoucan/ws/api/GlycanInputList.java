package org.glytoucan.ws.api;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

//@JsonSerialize
@XmlRootElement(name="glycan-structures")
public class GlycanInputList {

	List<GlycanInput> glycans;

	/**
	 * @return the glycans
	 */
	
	@XmlElement(name="glycan-structure")
	@JsonProperty(value="glycan-structure")
	public List<GlycanInput> getGlycans() {
		return glycans;
	}

	/**
	 * @param glycans the glycans to set
	 */
	public void setGlycans(List<GlycanInput> glycans) {
		this.glycans = glycans;
	}
	
	
}
