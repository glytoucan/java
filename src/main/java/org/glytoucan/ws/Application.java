package org.glytoucan.ws;

import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlDAOSesameImpl;
import org.glycoinfo.rdf.glycan.GlycoSequenceSelectSparql;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.knappsack.swagger4springweb.util.ScalaObjectMapper;


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
}
