package org.glytoucan.ws;

import org.glycoinfo.batch.glyconvert.GlycomeDBConvertSelectSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlDAOSesameImpl;
import org.glycoinfo.rdf.glycan.GlycoSequenceSelectSparql;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	@Bean
	SparqlDAO getSparqlDAO() {
		return new SparqlDAOSesameImpl();
	}
	
	@Bean
	SelectSparql getSelectSparql() {
		SelectSparql select = new GlycoSequenceSelectSparql();
		select.setFrom("FROM <http://rdf.glytoucan.org/sequence/wurcs>\nFROM <http://rdf.glytoucan.org>");
		return select;
	}

	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
