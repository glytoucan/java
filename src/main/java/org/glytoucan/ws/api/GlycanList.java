package org.glytoucan.ws.api;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModel;

@XmlRootElement(name="items")      // when XML is generated, the list of glycans will be under "items" element
@ApiModel (value="GlycanList", description="List of Glycans")
public class GlycanList {
	private List<Object> glycans;
	
	public GlycanList() {
		this.glycans = new ArrayList<Object>();
	}
	
	public GlycanList (List<Object> glycans) {
		this.glycans = glycans;
	}

	@XmlElements({ 
	    @XmlElement(name="glycan", type=Glycan.class),
	    @XmlElement(name="accessionNumber", type=String.class),
	})
	@JsonProperty(value="items")
	public List<Object> getGlycans() {
		return glycans;
	}

	public void setGlycans(Object[] array) {
		for (int i = 0; i < array.length; i++) {
			Object item = array[i];
			if (item instanceof String || item instanceof Glycan)  // only these types should be added to this list
				this.glycans.add(item);
		}
		
	}
}
