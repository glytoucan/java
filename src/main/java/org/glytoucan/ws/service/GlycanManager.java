package org.glytoucan.ws.service;

import java.util.List;

import org.glytoucan.ws.api.Glycan;
import org.glytoucan.ws.api.GlycanResponse;
import org.glytoucan.ws.api.UserInput;

public interface GlycanManager {
	
	public Glycan getGlycanById (int glycanId);
	public Glycan getGlycanByAccessionNumber (String accession);
	public Glycan getGlycanByStructure (String structure);
	
	public void deleteGlycanByAccessionNumber (String accession);
	
	// http://docs.openlinksw.com/virtuoso/rdfgraphsecurity.html#rdfgraphsecurityintconfsec
	public List<String> getGlycansByContributor (UserInput user);
	public GlycanResponse addStructure (String structure, String userName, Double mass) throws Exception;
	public List<Glycan> getGlycans();
	
	public List<Glycan> getGlycansByAccessionNumbers(List<String> accessionNumbers);
	
	public List<String> getGlycanIds();
	public List<String> subStructureSearch(String structure) throws Exception;
	public List<String> motifSearch (String motifName) throws Exception;
	GlycanResponse assignNewAccessionNumber(String structure, String userName);
//	List<String> compositionSearch(CompositionSearchInput input)
//			throws SugarImporterException, GlycoVisitorException,
//			SearchEngineException;
	List<String> getAllPendingGlycansByContributor(UserInput user);
	void deleteGlycansByAccessionNumber (List<String> accessionNumbers);
	
	public boolean isPending(Glycan glycan);
}
