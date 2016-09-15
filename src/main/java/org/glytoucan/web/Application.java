package org.glytoucan.web;

import java.util.Arrays;

import org.eurocarbdb.application.glycanbuilder.BuilderWorkspace;
import org.eurocarbdb.application.glycanbuilder.renderutil.GlycanRendererAWT;
import org.eurocarbdb.resourcesdb.Config;
import org.eurocarbdb.resourcesdb.io.MonosaccharideConverter;
import org.glycoinfo.convert.GlyConvertConfig;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntityFactory;
import org.glycoinfo.rdf.dao.virt.SparqlDAOVirtSesameImpl;
import org.glycoinfo.rdf.dao.virt.VirtRepositoryConnectionFactory;
import org.glycoinfo.rdf.dao.virt.VirtSesameConnectionFactory;
import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionManager;
import org.glycoinfo.rdf.glycan.D3SequenceSelectSparql_has_topology;
import org.glycoinfo.rdf.glycan.D3SequenceSelectSparql_isomer;
import org.glycoinfo.rdf.glycan.D3SequenceSelectSparql_motif;
import org.glycoinfo.rdf.glycan.D3SequenceSelectSparql_topology_by;
import org.glycoinfo.rdf.scint.ClassHandler;
import org.glycoinfo.rdf.scint.InsertScint;
import org.glycoinfo.rdf.scint.SelectScint;
import org.glycoinfo.rdf.service.GlycanProcedure;
import org.glycoinfo.rdf.service.impl.ContributorProcedureConfig;
import org.glycoinfo.rdf.service.impl.GlycanProcedureConfig;
import org.glycoinfo.rdf.service.impl.MailService;
import org.glycoinfo.rdf.utils.TripleStoreProperties;
import org.glycoinfo.vision.generator.ImageGenerator;
import org.glytoucan.admin.client.config.UserClientConfig;
import org.glytoucan.client.config.ClientConfiguration;
import org.glytoucan.client.config.ContributorConfig;
import org.glytoucan.web.view.LocalizationHandlerMapping;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import virtuoso.sesame2.driver.VirtuosoRepository;

//http://stackoverflow.com/questions/31307883/springfox-dependency-breaking-my-spring-context
@SpringBootApplication(exclude=org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class)
@Import(value = { GlycanProcedureConfig.class, ContributorProcedureConfig.class, ClientConfiguration.class, GlyConvertConfig.class, UserClientConfig.class, ContributorConfig.class })
public class Application  {
//	extends SpringBootServletInitializer
	private static final String graph = "http://rdf.glytoucan.org";

	@Bean
//	@Scope(value="request", proxyMode=ScopedProxyMode.TARGET_CLASS)
	SparqlDAO getSparqlDAO() {
		return new SparqlDAOVirtSesameImpl();
	}
	
	@Bean(name = "d3SequenceSelectSparql_motif")
	SelectSparql getd3SelectSparql_motif() {
		SelectSparql select = new D3SequenceSelectSparql_motif();
//		select.setFrom("FROM <http://rdf.glycoinfo.org/glycan/browser/demo>");
		return select;
	}
	
	@Bean(name = "d3SequenceSelectSparql_isomer")
	SelectSparql getd3SelectSparql_isomer() {
		SelectSparql select = new D3SequenceSelectSparql_isomer();
//		select.setFrom("FROM <http://rdf.glycoinfo.org/glycan/browser/demo>");
		return select;
	}
	
	@Bean(name = "d3SequenceSelectSparql_topology")
	SelectSparql getd3SelectSparql_topology() {
		SelectSparql select = new D3SequenceSelectSparql_has_topology();
//		select.setFrom("FROM <http://rdf.glycoinfo.org/glycan/browser/demo>");
		return select;
	}
	
	@Bean(name = "d3SequenceSelectSparql_topologyby")
	SelectSparql getd3SelectSparql_topologyby() {
		SelectSparql select = new D3SequenceSelectSparql_topology_by();
//		select.setFrom("FROM <http://rdf.glycoinfo.org/glycan/browser/demo>");
		return select;
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
//     MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
//     ScalaObjectMapper objectMapper = new ScalaObjectMapper();
//     objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//     jsonConverter.setObjectMapper(objectMapper);
//     return jsonConverter;
    	
    	MappingJackson2HttpMessageConverter jacksonConverter = new
    			MappingJackson2HttpMessageConverter();
    			jacksonConverter.setSupportedMediaTypes(Arrays.asList(MediaType.valueOf("application/json")));
    			jacksonConverter.setObjectMapper(jacksonObjectMapper());
    			return jacksonConverter;
    }
    
    @Bean
    public ObjectMapper jacksonObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.setSerializationInclusion(Include.NON_NULL);
    return objectMapper;
    }
    
    @Bean
    public BufferedImageHttpMessageConverter bufferedImageHttpMessageConverter() {
    	return new BufferedImageHttpMessageConverter();
    }
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(Application.class);
//    }
        
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
	
	@Bean(name = "selectscintperson")
	SelectScint getSelectPersonScint() throws SparqlException {
		SelectScint select = new SelectScint("schema", "http://schema.org/", "Person");
		
//		select.setClassHandler(getPersonClassHandler());
		return select;
	}

	@Bean(name = "insertscintperson")
	InsertScint getInsertPersonScint() throws SparqlException {
		InsertScint insert = new InsertScint(graph + "/schema/users", "schema", "http://schema.org/", "Person");
		return insert;
	}

	@Bean(name = "selectscintregisteraction")
	SelectScint getSelectRegisterActionScint() throws SparqlException {
		SelectScint select = new SelectScint("schema", "http://schema.org/", "RegisterAction");
		return select;
	}

	@Bean(name = "insertscintregisteraction")
	InsertScint getInsertRegisterActionScint() throws SparqlException {
		InsertScint insert = new InsertScint(graph + "/schema/users", "schema", "http://schema.org/", "RegisterAction");
		return insert;
	}

	ClassHandler getPersonClassHandler() throws SparqlException {
		ClassHandler ch = new ClassHandler();
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
	
//    @Bean
//    ResidueTranslator residueTranslator() throws IOException {
//    	return new ResidueTranslator();
//    }

    @Bean
    ImageGenerator imageGenerator() {
    	return new ImageGenerator();
    }

    @Bean
    GlycanRendererAWT glycanRenderer() {
    	return new GlycanRendererAWT();
    }
    
    @Bean
    BuilderWorkspace glycanWorkspace() {
    	return new BuilderWorkspace(glycanRenderer());
    }
    
	@Bean
	MailService mailService() {
		return new MailService();
	}
	
	@Bean
	SparqlEntityFactory sparqlEntityFactory() {
		return new SparqlEntityFactory();
	}
	
//	@Bean
//	LogClient logClient() {
//		return new LogClient();
//	}
	

  
//  @Bean(name="gtc.version")
//  public String gtcVersion() {
//    return "test";      
//  }


//  @Value("${GTC_VERSION}")
//  String gtcVersion;
//  
//  @Value("${api.hostname")
//  private String hostname;
//  
//  @Bean
//  public String getGtcVersion() {
//    return gtcVersion;
//  }
//
//  public void setGtcVersion(String gtcVersion) {
//    this.gtcVersion = gtcVersion;
//  }
}