package com.ad.de.app;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.ad.de.service.SchemaToQueryWriterApp;

@SpringBootApplication
@ComponentScan(basePackages="com.ad.de")
public class TeraDataSchemaComparatorApplication {

	
	
	@Autowired
	private SchemaToQueryWriterApp writeQueryApp;
	
		
	@PostConstruct
	public void initApp(){
		//testApplicationProperties.testPropRead();
		//testTeradataConnection.processDatabaseMetadata();
		writeQueryApp.startQueryWriteProcess();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(TeraDataSchemaComparatorApplication.class, args);

		
		
	}
}
