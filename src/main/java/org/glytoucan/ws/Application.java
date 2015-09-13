package org.glytoucan.ws;

import java.io.IOException;

import org.eurocarbdb.application.glycanbuilder.GlycanRendererAWT;
import org.eurocarbdb.application.glycoworkbench.GlycanWorkspace;
import org.eurocarbdb.resourcesdb.Config;
import org.eurocarbdb.resourcesdb.io.MonosaccharideConverter;
import org.glycoinfo.batch.search.wurcs.SubstructureSearchSparql;
import org.glycoinfo.mass.MassInsertSparql;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.virt.SparqlDAOVirtSesameImpl;
import org.glycoinfo.rdf.dao.virt.VirtRepositoryConnectionFactory;
import org.glycoinfo.rdf.dao.virt.VirtSesameConnectionFactory;
import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionManager;
import org.glycoinfo.rdf.glycan.ContributorInsertSparql;
import org.glycoinfo.rdf.glycan.ContributorNameSelectSparql;
import org.glycoinfo.rdf.glycan.GlycoSequenceInsertSparql;
import org.glycoinfo.rdf.glycan.GlycoSequenceSelectSparql;
import org.glycoinfo.rdf.glycan.ResourceEntryInsertSparql;
import org.glycoinfo.rdf.glycan.SaccharideInsertSparql;
import org.glycoinfo.rdf.glycan.SaccharideSelectSparql;
import org.glycoinfo.rdf.glycan.wurcs.GlycoSequenceResourceEntryContributorSelectSparql;
import org.glycoinfo.rdf.glycan.wurcs.MotifSequenceSelectSparql;
import org.glycoinfo.rdf.glycan.wurcs.WurcsRDFInsertSparql;
import org.glycoinfo.rdf.glycan.wurcs.WurcsRDFMSInsertSparql;
import org.glycoinfo.rdf.scint.ClassHandler;
import org.glycoinfo.rdf.scint.InsertScint;
import org.glycoinfo.rdf.scint.SelectScint;
import org.glycoinfo.rdf.service.ContributorProcedure;
import org.glycoinfo.rdf.service.GlycanProcedure;
import org.glycoinfo.rdf.service.UserProcedure;
import org.glycoinfo.rdf.service.impl.ContributorProcedureRdf;
import org.glycoinfo.rdf.utils.TripleStoreProperties;
import org.glycomedb.residuetranslator.ResidueTranslator;
import org.glyspace.registry.utils.ImageGenerator;
import org.glytoucan.ws.api.D3SequenceSelectSparql;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import virtuoso.sesame2.driver.VirtuosoRepository;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.knappsack.swagger4springweb.util.ScalaObjectMapper;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {
	
	private static final String graph = "http://rdf.glytoucan.org";

	@Bean
	SparqlDAO getSparqlDAO() {
		return new SparqlDAOVirtSesameImpl();
	}
	
	@Bean(name = "glycoSequenceSelectSparql")
	SelectSparql getSelectSparql() {
		SelectSparql select = new GlycoSequenceSelectSparql();
		select.setFrom("FROM <http://rdf.glytoucan.org/sequence/wurcs>\nFROM <http://rdf.glytoucan.org>");
		return select;
	}

	@Bean(name = "d3SequenceSelectSparql")
	SelectSparql getd3SelectSparql() {
		SelectSparql select = new D3SequenceSelectSparql();
//		select.setFrom("FROM <http://rdf.glycoinfo.org/glycan/browser/demo>");
		return select;
	}
	
	@Bean(name = "motifSequenceSelectSparql")
	SelectSparql getmotifSequenceSelectSparql() {
		return new MotifSequenceSelectSparql();
	}
	
	@Bean
	MonosaccharideConverter monosaccharideConverter() {
		Config config = new Config();
		MonosaccharideConverter mc = new MonosaccharideConverter();
		mc.setConfig(config);
		return mc;
	}

//    @Bean
//    public HttpMessageConverters customConverters() {
//        HttpMessageConverter<?> additional = ...
//        HttpMessageConverter<?> another = ...
//        return new HttpMessageConverters(additional, another);
//    }	

//	<mvc:message-converters> 
//    <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
//    <property name="objectMapper">
//        <bean class="com.knappsack.swagger4springweb.util.ScalaObjectMapper"/>
//    </property>
//</bean>
//<bean class="org.springframework.http.converter.BufferedImageHttpMessageConverter"/>

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
     MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
     ScalaObjectMapper objectMapper = new ScalaObjectMapper();
     objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
     jsonConverter.setObjectMapper(objectMapper);
     return jsonConverter;
    }
    
    @Bean
    public BufferedImageHttpMessageConverter bufferedImageHttpMessageConverter() {
    	return new BufferedImageHttpMessageConverter();
    }
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
        
//    @Bean
//    public ServerProperties getServerProperties() {
//        return new ServerCustomization();
//    }
//    
	@Bean
	VirtSesameConnectionFactory getSesameConnectionFactory() {
		return new VirtRepositoryConnectionFactory(getRepository());
	}
	
	@Bean
	public Repository getRepository() {
		return new VirtuosoRepository(
				getTripleStoreProperties().getUrl(), 
				getTripleStoreProperties().getUsername(),
				getTripleStoreProperties().getPassword());
	}

	@Bean
	TripleStoreProperties getTripleStoreProperties() {
		return new TripleStoreProperties();
	}
	
	@Bean
	VirtSesameTransactionManager transactionManager() throws RepositoryException {
		return new VirtSesameTransactionManager(getSesameConnectionFactory());
	}
	
//	@Bean
//	public FilterRegistrationBean sitemeshFilter() {
//		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//		filterRegistrationBean.setFilter(new MySiteMeshFilter());
//		filterRegistrationBean.addUrlPatterns("/*");
//		return filterRegistrationBean;
//	}
	
	
	
//    <bean id="handlerMapping"
//            class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping">
//        <property name="interceptors">
//            <list>
//                <ref bean="officeHoursInterceptor"/>
//            </list>
//        </property>
//    </bean>
//	public RequestMappingHandlerMapping handlerMapping() {
//		RequestMappingHandlerMapping r = new RequestMappingHandlerMapping();
//		ArrayList<Object[]> list = new ArrayList();
//		list.add(new LocalizationHandlerMapping());
//		r.setInterceptors(list);
//	}

	@Bean
	public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
		return new WebConfig();
	}
	
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
	    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
	    CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
	    characterEncodingFilter.setEncoding("UTF-8");
	    registrationBean.setFilter(characterEncodingFilter);
//	    registrationBean.setOrder();
	    return registrationBean;
	}
	
	@Bean(name = "userProcedure")
	UserProcedure getUserProcedure() throws SparqlException {
		UserProcedure user = new org.glycoinfo.rdf.service.impl.UserProcedure();
		return user;
	}
    
	@Bean(name = "selectscintperson")
	SelectScint getSelectPersonScint() throws SparqlException {
		SelectScint select = new SelectScint();
		
		select.setClassHandler(getPersonClassHandler());
		return select;
	}

	@Bean(name = "insertscintperson")
	InsertScint getInsertPersonScint() throws SparqlException {
		InsertScint insert = new InsertScint("http://rdf.glytoucan.org/users");
		insert.setClassHandler(getPersonClassHandler());
		return insert;
	}

	@Bean(name = "selectscintregisteraction")
	SelectScint getSelectRegisterActionScint() throws SparqlException {
		SelectScint select = new SelectScint();
		select.setClassHandler(getRegisterActionClassHandler());
		return select;
	}

	@Bean(name = "insertscintregisteraction")
	InsertScint getInsertRegisterActionScint() throws SparqlException {
		InsertScint insert = new InsertScint("http://rdf.glytoucan.org/users");
		insert.setClassHandler(getRegisterActionClassHandler());
		return insert;
	}

	ClassHandler getPersonClassHandler() throws SparqlException {
		ClassHandler ch = new ClassHandler("schema", "http://schema.org/", "Person");
		ch.setSparqlDAO(getSparqlDAO());
		return ch; 
	}
	
	ClassHandler getRegisterActionClassHandler() throws SparqlException {
		ClassHandler ch = new ClassHandler("schema", "http://schema.org/", "RegisterAction");
		ch.setSparqlDAO(getSparqlDAO());
		return ch; 
	}
	
	ClassHandler getDateTimeClassHandler() throws SparqlException {
		ClassHandler ch = new ClassHandler("schema", "http://schema.org/", "DateTime");
		ch.setSparqlDAO(getSparqlDAO());
		return ch; 
	}
	
	@Bean(name = "glycanProcedure")
	GlycanProcedure getGlycanProcedure() throws SparqlException {
		GlycanProcedure glycan = new org.glycoinfo.rdf.service.impl.GlycanProcedure();
		return glycan;
	}
	
	@Bean
	SaccharideInsertSparql getSaccharideInsertSparql() {
		SaccharideInsertSparql sis = new SaccharideInsertSparql();
		sis.setGraph(graph);
		return sis;
	}
	
	@Bean(name = "contributorProcedure")
	ContributorProcedure getContributorProcedure() throws SparqlException {
		ContributorProcedure cp = new ContributorProcedureRdf();
		return cp;
	}

	@Bean
	ContributorInsertSparql getContributorInsertSparql() {
		ContributorInsertSparql c = new ContributorInsertSparql();
		c.setGraph(graph);
		return c;
	}
	
	@Bean
	ContributorNameSelectSparql getContributorNameSelectSparql() {
		ContributorNameSelectSparql selectbyNameContributor = new ContributorNameSelectSparql();
		selectbyNameContributor.setFrom("FROM <" + graph + ">");
		return selectbyNameContributor;
	}

	@Bean
	ResourceEntryInsertSparql getResourceEntryInsertSparql() {
		ResourceEntryInsertSparql resourceEntryInsertSparql = new ResourceEntryInsertSparql();
		SparqlEntity se = new SparqlEntity();
		se.setValue(ResourceEntryInsertSparql.Database, "glytoucan");
		resourceEntryInsertSparql.setSparqlEntity(se);
		resourceEntryInsertSparql.setGraph(graph);
		return resourceEntryInsertSparql;
	}

	@Bean
	SelectSparql glycoSequenceContributorSelectSparql() {
		GlycoSequenceResourceEntryContributorSelectSparql sb = new GlycoSequenceResourceEntryContributorSelectSparql();
		sb.setFrom("FROM <http://rdf.glytoucan.org>\nFROM <http://rdf.glytoucan.org/sequence/wurcs>");
		return sb;
	}
	
	@Bean
	WurcsRDFInsertSparql wurcsRDFInsertSparql() {
		WurcsRDFInsertSparql wrdf = new WurcsRDFInsertSparql();
		wrdf.setSparqlEntity(new SparqlEntity());
		wrdf.setGraph("http://rdf.glytoucan.org/sequence/wurcs");
		return wrdf;
	}
	
	@Bean
	InsertSparql glycoSequenceInsert() {
		GlycoSequenceInsertSparql gsis = new GlycoSequenceInsertSparql();
		gsis.setSparqlEntity(new SparqlEntity());
		gsis.setGraph("http://rdf.glytoucan.org");
		return gsis;
	}
	
    @Bean
    SubstructureSearchSparql substructureSearchSparql() {
    	SubstructureSearchSparql select = new SubstructureSearchSparql();
//    	select.setFrom("");
    	return select;
    }
    
    @Bean
    ResidueTranslator residueTranslator() throws IOException {
    	return new ResidueTranslator();
    }

    @Bean
    ImageGenerator imageGenerator() {
    	return new ImageGenerator();
    }

    @Bean
    GlycanRendererAWT glycanRenderer() {
    	return new GlycanRendererAWT();
    }
    
    @Bean
    GlycanWorkspace glycanWorkspace() {
    	return new GlycanWorkspace(glycanRenderer());
    }
    
	@Bean
	MassInsertSparql massInsertSparql() {
		MassInsertSparql mass = new MassInsertSparql();
		mass.setGraphBase(graph);
		return mass;
	}
	
	@Bean
	SelectSparql listAllGlycoSequenceContributorSelectSparql() {
		GlycoSequenceResourceEntryContributorSelectSparql sb = new GlycoSequenceResourceEntryContributorSelectSparql();
		sb.setFrom("FROM <http://rdf.glytoucan.org>\nFROM <http://rdf.glytoucan.org/sequence/wurcs>\nFROM <http://rdf.glytoucan.org/mass>");
		return sb;
	}
	
	@Bean
	WurcsRDFMSInsertSparql wurcsRDFMSInsertSparql() {
		WurcsRDFMSInsertSparql wrdf = new WurcsRDFMSInsertSparql();
		wrdf.setGraph("http://rdf.glytoucan.org/wurcs/ms");
		return wrdf;
	}
	
	@Bean
	SaccharideSelectSparql saccharideSelectSparql() {
		SaccharideSelectSparql select = new SaccharideSelectSparql();
		select.setFrom("FROM <http://rdf.glytoucan.org>\n");
		return select;
	}
}