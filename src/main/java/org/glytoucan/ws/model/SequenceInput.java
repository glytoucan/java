package org.glytoucan.ws.model;

import org.hibernate.validator.constraints.NotEmpty;

import com.wordnik.swagger.annotations.ApiModel;

@ApiModel (value="Sequence Input", description="Structure Sequence")
public class SequenceInput {
	@NotEmpty
	String sequence;
    
    String resultSequence;
    
    String image;
    
    String id;

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		return "Sequence [sequence=" + sequence + "]";
	}

	public String getResultSequence() {
		return resultSequence;
	}

	public void setResultSequence(String resultSequence) {
		this.resultSequence = resultSequence;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	
}