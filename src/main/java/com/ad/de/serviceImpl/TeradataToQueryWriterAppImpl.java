package com.ad.de.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ad.de.service.DataBaseMetadataConfigFileReader;
import com.ad.de.service.DataBaseMetadataReader;
import com.ad.de.service.QueryWriter;
import com.ad.de.service.SchemaToQueryWriterApp;

@Service
public class TeradataToQueryWriterAppImpl implements SchemaToQueryWriterApp{

	private static final Logger LOGGER = LoggerFactory.getLogger(TeradataToQueryWriterAppImpl.class);
	
	@Autowired
	private DataBaseMetadataConfigFileReader configReader;
	
	@Autowired
	private DataBaseMetadataReader metadataReader;
	
	@Autowired
	private QueryWriter queryWriter;
	
	@Value("${application.target.autocreate}")
	private String targetAutoCreateFlag; 
	
	@Value("${spring.teradataDataSource.usertype}")
	String dataSourceUserType;
	
	@Override
	public void startQueryWriteProcess() {
		LOGGER.info("Processing Database Config File");
		configReader.processDatabaseMetadataConfigFile();
		LOGGER.info("Building Source Database Metadata Cache");
		if(dataSourceUserType.equals("limited"))
			metadataReader.fetchDatabaseMetadataLimitedUser();
		else
			//WIP
			metadataReader.fetchDatabaseMetadata();
		LOGGER.info("Writing DDL Scripts");
		queryWriter.writeQuery();
		if(targetAutoCreateFlag.equalsIgnoreCase("TRUE")){
			LOGGER.info("Auto Creating Target Tables");
		}
		else {
			LOGGER.info("Skipping Auto Creating of Target Tables");
		}
		
		LOGGER.info("Application Close");
		
	}

}
