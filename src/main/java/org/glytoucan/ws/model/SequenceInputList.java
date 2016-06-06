package org.glytoucan.ws.model;

import java.util.List;

//import org.hibernate.validator.constraints.NotEmpty;

import com.wordnik.swagger.annotations.ApiModel;

@ApiModel (value="Sequence Input", description="Structure Sequence")
public class SequenceInputList {
//	@NotEmpty
	List<String> sequence;
    
    List<String> resultSequence;
    
    List<String> image;
    
    List<String> id;
    
    List<Boolean> register;

	public List<String> getSequence() {
		return sequence;
	}

	public void setSequence(List<String> sequence) {
		this.sequence = sequence;
	}

	public List<String> getResultSequence() {
		return resultSequence;
	}

	public void setResultSequence(List<String> resultSequence) {
		this.resultSequence = resultSequence;
	}

	public List<String> getImage() {
		return image;
	}

	public void setImage(List<String> image) {
		this.image = image;
	}

	public List<String> getId() {
		return id;
	}

	public void setId(List<String> id) {
		this.id = id;
	}

	public List<Boolean> getRegister() {
		return register;
	}

	public void setRegister(List<Boolean> register) {
		this.register = register;
	}	
}