package org.glytoucan.ws.service;

import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.virt.SparqlDAOVirtSesameImpl;
import org.glytoucan.ws.rdf.ContributorInsertSparql;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserProcedureRdfTest.class)
@ComponentScan(basePackages = {"org.glytoucan.ws"})
@EnableAutoConfiguration
public class UserProcedureRdfTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	ContributorProcedure contributorProcedure;
	
	@Bean
	SparqlDAO getSparqlDAO() {
		return new SparqlDAOVirtSesameImpl();
	}
	
	@Bean
	ContributorInsertSparql getContributorInsertSparql() {
		ContributorInsertSparql c = new ContributorInsertSparql();
		c.setGraph("http://rdf.glcoinfo.org/contributors");
		return c;
	}
	
	@Bean
	public ContributorProcedure getContributorProcedure() {
		return (ContributorProcedure) new ContributorProcedureRdf();
	}
	
	@Test
	@Transactional
	public void testGetId() throws SparqlException {
		contributorProcedure.setName("test");
		String id = contributorProcedure.addContributor();
		Assert.assertNotNull(id);
	}
}