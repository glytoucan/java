package org.glytoucan.web.api;

public class GRABGraphJson {
	
	private String grabGraphjson;
	private String centerGlycanId;
	private String baseString;
	private String nodeString;
	//private String edgeString;

	public String getCenterGlycanId() {
		return centerGlycanId;
	}
	public void setCenterGlycanId(String centerGlycanId) {
		this.centerGlycanId = centerGlycanId;
	}
	public String getGrabGraphjson() {
		//baseString = "elements:[{\"data\":{\"id\":\"" + centerGlycanId + "\"},\"position\":{\"x\":0,\"y\":0},\"group\":\"nodes\"},{\"data\":{\"id\":\"subsumedby_box\"},\"position\":{\"x\":100,\"y\":0},\"group\":\"nodes\"},{\"data\":{\"id\":\"subsumes_box\"},\"position\":{\"x\":-100,\"y\":0},\"group\":\"nodes\"},{\"data\":{\"id\":\"superstructure_box\"},\"position\":{\"x\":0,\"y\":-150},\"group\":\"nodes\"},{\"data\":{\"id\":\"substructure_box\"},\"position\":{\"x\":0,\"y\":150},\"group\":\"nodes\"},{\"data\":{\"id\":\"subsumedby\",\"parent\":\"subsumedby_box\"},\"position\":{\"x\":100,\"y\":0},\"group\":\"nodes\"},{\"data\":{\"id\":\"subsumes\",\"parent\":\"subsumes_box\"},\"position\":{\"x\":-100,\"y\":0},\"group\":\"nodes\"},{\"data\":{\"id\":\"superstructure\",\"parent\":\"superstructure_box\"},\"position\":{\"x\":0,\"y\":-150},\"group\":\"nodes\"},{\"data\":{\"id\":\"substructure\",\"parent\":\"substructure_box\"},\"position\":{\"x\":0,\"y\":150},\"group\":\"nodes\"},{\"data\":{\"id\":\"center_to_subsumedby\",\"source\":\"" + centerGlycanId + "\",\"target\":\"subsumedby\"},\"group\":\"edges\"},{\"data\":{\"id\":\"center_to_subsumes\",\"source\":\"" + centerGlycanId + "\",\"target\":\"subsumes\"},\"group\":\"edges\"},{\"data\":{\"id\":\"center_to_superstructure\",\"source\":\"" + centerGlycanId + "\",\"target\":\"superstructure\"},\"group\":\"edges\"},{\"data\":{\"id\":\"center_to_substructure\",\"source\":\"" +centerGlycanId + "\",\"target\":\"substructure\"},\"group\":\"edges\"}";
		baseString = "[{\"data\":{\"id\":\"" + centerGlycanId + "\"},\"position\":{\"x\":0,\"y\":0},\"group\":\"nodes\"},{\"data\":{\"id\":\"subsumedby_box\"},\"position\":{\"x\":100,\"y\":0},\"group\":\"nodes\"},{\"data\":{\"id\":\"subsumes_box\"},\"position\":{\"x\":-100,\"y\":0},\"group\":\"nodes\"},{\"data\":{\"id\":\"superstructure_box\"},\"position\":{\"x\":0,\"y\":-150},\"group\":\"nodes\"},{\"data\":{\"id\":\"substructure_box\"},\"position\":{\"x\":0,\"y\":150},\"group\":\"nodes\"},{\"data\":{\"id\":\"subsumedby\",\"parent\":\"subsumedby_box\"},\"position\":{\"x\":100,\"y\":0},\"group\":\"nodes\"},{\"data\":{\"id\":\"subsumes\",\"parent\":\"subsumes_box\"},\"position\":{\"x\":-100,\"y\":0},\"group\":\"nodes\"},{\"data\":{\"id\":\"superstructure\",\"parent\":\"superstructure_box\"},\"position\":{\"x\":0,\"y\":-150},\"group\":\"nodes\"},{\"data\":{\"id\":\"substructure\",\"parent\":\"substructure_box\"},\"position\":{\"x\":0,\"y\":150},\"group\":\"nodes\"},{\"data\":{\"id\":\"center_to_subsumedby\",\"source\":\"" + centerGlycanId + "\",\"target\":\"subsumedby\"},\"group\":\"edges\"},{\"data\":{\"id\":\"center_to_subsumes\",\"source\":\"" + centerGlycanId + "\",\"target\":\"subsumes\"},\"group\":\"edges\"},{\"data\":{\"id\":\"center_to_superstructure\",\"source\":\"" + centerGlycanId + "\",\"target\":\"superstructure\"},\"group\":\"edges\"},{\"data\":{\"id\":\"center_to_substructure\",\"source\":\"" +centerGlycanId + "\",\"target\":\"substructure\"},\"group\":\"edges\"}";
		//grabGraphjson = baseString + nodeString + edgeString + "]"; 
		grabGraphjson = baseString + nodeString +"]"; 
		return grabGraphjson;
	}
	public void setGrabGraphjson(String grabGraphjson) {
		this.grabGraphjson = grabGraphjson;
	}
	public String getNodeString() {
		return nodeString;
	}
	public void setNodeString(String nodeString) {
		this.nodeString = nodeString;
	}
	
//	public String getEdgeString() {
//		return edgeString;
//	}
//	public void setEdgeString(String edgeString) {
//		this.edgeString = edgeString;
//	}

}
