package org.glytoucan.ws.api;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.validator.constraints.NotEmpty;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * This class is used to collect information about a glycan from the user
 * View of GlycanEntity
 */
@XmlRootElement (name="glycan-sequence-input")
@ApiModel (value="Glycan", description="Glycan representation in string format with format")
public class GlycanInput {

	String structure;
	String format;

	/**
	 * @return the structure
	 */
	@XmlJavaTypeAdapter(value=CDATAAdapter.class)
	@NotEmpty
	//@Structure
	@ApiModelProperty (value="Glycan sequence", required=true)
	public String getStructure() {
		return structure;
	}

	/**
	 * @param structure the structure to set
	 */
	public void setStructure(String structure) {
		this.structure = structure;
	}

	/**
	 * @return the format
	 */
	@XmlAttribute
	@ApiModelProperty (value="Format of the sequence", required=false)
	public String getFormat() {
		return format;
	}

	/**
	 * @param encoding the format to set
	 */
	public void setFormat(String encoding) {
		this.format = encoding;
	}
}
