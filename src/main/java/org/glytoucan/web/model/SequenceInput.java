package org.glytoucan.web.model;

//import org.hibernate.validator.constraints.NotEmpty;

import com.wordnik.swagger.annotations.ApiModel;

@ApiModel (value="Sequence Input", description="Structure Sequence")
public class SequenceInput {
//	@NotEmpty
	String sequenceInput;
    
    String resultSequence;
    
    String image;
    
    String id;
    
    String from;
    
    boolean register = true;

	public boolean isRegister() {
		return register;
	}

	public void setRegister(boolean register) {
		this.register = register;
	}

	public String getSequenceInput() {
		return sequenceInput;
	}

	public void setSequenceInput(String sequence) {
		this.sequenceInput = sequence;
	}

	@Override
	public String toString() {
		return "SequenceInput [sequence=" + sequenceInput + ", resultSequence="
				+ resultSequence + ", image=" + image + ", id=" + id + "]";
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
	
		public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result
				+ ((resultSequence == null) ? 0 : resultSequence.hashCode());
		result = prime * result
				+ ((sequenceInput == null) ? 0 : sequenceInput.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SequenceInput other = (SequenceInput) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		if (resultSequence == null) {
			if (other.resultSequence != null)
				return false;
		} else if (!resultSequence.equals(other.resultSequence))
			return false;
		if (sequenceInput == null) {
			if (other.sequenceInput != null)
				return false;
		} else if (!sequenceInput.equals(other.sequenceInput))
			return false;
		return true;
	}	
}