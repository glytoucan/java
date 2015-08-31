package org.glytoucan.ws.model;

import org.hibernate.validator.constraints.NotEmpty;

import com.wordnik.swagger.annotations.ApiModel;

@ApiModel (value="Sequence Input", description="Structure Sequence")
public class SequenceInput {
	@NotEmpty
	String sequence = "RES\\r\\n"
			+ "1b:a-dgal-HEX-1:5\\r\\n"
			+ "2s:n-acetyl\\r\\n"
			+ "3b:b-dgal-HEX-1:5\\r\\n"
			+ "LIN\\r\\n"
			+ "1:1d(2+1)2n\\r\\n"
			+ "2:1o(3+1)3d\\r\\n"
			+ "RES\\r\\n"
			+ "1b:x-dman-HEX-1:5\\r\\n"
			+ "2b:x-dgal-HEX-1:5\\r\\n"
			+ "3s:n-acetyl\\r\\n"
			+ "LIN\\r\\n"
			+ "1:1o(-1+1)2d\\r\\n"
			+ "2:2d(2+1)3n\\r\\n"
			+ "RES\\r\\n"
			+ "1b:x-dglc-HEX-1:5|1:a\\r\\n"
			+ "2b:b-dgal-HEX-1:5\\r\\n"
			+ "LIN\n1:1o(4+1)2d";
    
    String resultSequence;
    
    String image;
    
    String id;
    
    boolean register = true;

	public boolean isRegister() {
		return register;
	}

	public void setRegister(boolean register) {
		this.register = register;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		return "SequenceInput [sequence=" + sequence + ", resultSequence="
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result
				+ ((resultSequence == null) ? 0 : resultSequence.hashCode());
		result = prime * result
				+ ((sequence == null) ? 0 : sequence.hashCode());
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
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if (!sequence.equals(other.sequence))
			return false;
		return true;
	}	
}