package com.ad.de.serviceImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.ad.de.config.AppDataStore;
import com.ad.de.domain.Column;
import com.ad.de.domain.RdbmsTableMetadata;
import com.ad.de.service.QueryWriter;


@Service
@Configuration
public class QueryWriterImpl implements QueryWriter{

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryWriter.class);
	
	final Properties dataTypeProperties = new Properties();
	
	private static final String QUERY_PREFIX ="CREATE EXTERNAL TABLE ";
	private static final String DOT = ".";
	private static final String COMMA = ",";
	private static final String NEW_LINE="\n";
	private static final String TAB = "\t";
	private static final String BR1=" ( ";
	private static final String BR2=" ) ";
	private static final String QUERY_CLOSE = ";";
	private static final String SLASH="/";
	
	@Value("${spring.teradataDatasource.system}")
	private String dbSystem;
	
	@Value("${location.prefix}")
	private String queryLocationPre;
	
	@Value("${location.suffix}")
	private String queryLocationSuff;
	
	@Value("${format}")
	private String format;
	
	@Value("${encoding}")
	private String encoding;
	
	@Value("${output.file.location}")
	private String outputLocation;
	
	@Value("${database.data.type.mapping.file.location}")
	private Resource dataTypeMappingFileLocation;
	
	@Value("${application.additional.columns}")
	private String additionalColumns;
	
	@Value("${application.additional.columns.type}")
	private String additionalColumnsType;
	
	@Value("${application.additional.columns.skip}")
	private String additionalColumnsSkip;
	
	private int additionalColumnFlag=5;
	String[] additionalColumnArray;
	String[] additionalColumnTypeArray;
	List additionalColumnSkipList;
	
	@PostConstruct
	public void initDatatypeMappings() {
		try (InputStream inputStream = dataTypeMappingFileLocation.getInputStream();) {
			dataTypeProperties.load(inputStream);
			LOGGER.info("Read database datatype properties file. Datatype info is {}.",
					dataTypeProperties);
		} catch (IOException e) {
			LOGGER.error("Error while loading database datatype properties file, root cause is {}.", e.getMessage());
			LOGGER.error(e.getMessage(), e);
		}
		additionalColumnSkipList= Arrays.asList(additionalColumnsSkip.split(","));
		additionalColumnArray = additionalColumns.split("#");
		additionalColumnTypeArray=additionalColumnsType.split("#");
		if(additionalColumnArray.length==additionalColumnTypeArray.length && additionalColumnArray.length!=0){
			LOGGER.info("Additional Column and additional column type length match");
			additionalColumnFlag=1;
		}	
		else{
			LOGGER.error("The length of additional Column and additional Column Type does not match");
			additionalColumnFlag=0;
		}
	}
	
	
	@Override
	public void writeQuery() {
		try {
		File f = new File("");
		String s = f.getAbsolutePath();
		PrintWriter writer = new PrintWriter(s+outputLocation+"outFile.sql", "UTF-8");

		List<RdbmsTableMetadata> tableData = AppDataStore.getRdbmsTableInfoList();
         
		for (RdbmsTableMetadata tableMetadata : tableData) {
			String schemaName = tableMetadata.getTargetSchema();
			String rdbmsTbleName = tableMetadata.getName();
			String targetTableName = tableMetadata.getTargetName();
			//String tableAdditionalColumns = tableMetadata.
			List<Column> cols = tableMetadata.getCols();
			StringBuilder baseString = new StringBuilder(queryLocationPre);
			String queryLocation= baseString.append(dbSystem).append(SLASH).append(schemaName).append(SLASH)
											 .append(rdbmsTbleName).append(SLASH).append(queryLocationSuff).toString();
			writer.println("/*##---------------------------------------------------------------------------");
			writer.println("##--- CREATE TABLE SCRIPT FOR " + schemaName+DOT+targetTableName+" --------------");
			writer.println("##---------------------------------------------------------------------------*/");
			writer.println(" ");
			writer.println(QUERY_PREFIX + schemaName+DOT+targetTableName+BR1+NEW_LINE);
			int size = cols.size();
			int count = 1;
			for (Column t : cols) {
				if(count<size)
					writer.println(t.getName().toUpperCase() + TAB + getMappedDatatype(t.getType(), "NA")+getDataTypeLength(t.getType(),t.getLength(),t.getPrecision())+TAB+COMMA+NEW_LINE);
				else{
					if(additionalColumnFlag==1 && !additionalColumnSkipList.contains(rdbmsTbleName))
						writer.println(t.getName().toUpperCase() + TAB + getMappedDatatype(t.getType(), "NA")+getDataTypeLength(t.getType(),t.getLength(),t.getPrecision())+TAB+COMMA+NEW_LINE);
					else
						writer.println(t.getName().toUpperCase() + TAB + getMappedDatatype(t.getType(), "NA")+getDataTypeLength(t.getType(),t.getLength(),t.getPrecision())+TAB+NEW_LINE);
				}
				count++;
			}
			if(additionalColumnFlag==1 && !additionalColumnSkipList.contains(rdbmsTbleName)){
				for(int line=0;line<additionalColumnArray.length;line++){
					if(additionalColumnArray.length-line>1)
					writer.println(additionalColumnArray[line] + TAB +additionalColumnTypeArray[line]+TAB+COMMA+NEW_LINE );
					else
					writer.println(additionalColumnArray[line] + TAB +additionalColumnTypeArray[line]+TAB+NEW_LINE );
				}
			}
			writer.println(BR2);
			writer.println("LOCATION " + queryLocation + NEW_LINE + "FORMAT " + format + NEW_LINE + "ENCODING " + encoding + QUERY_CLOSE);
			writer.println(" ");
			
		}
		writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
		
	
	private String getDataTypeLength(String type, String length, int precision) {
		if(type.trim().equalsIgnoreCase("AT")||type.trim().equalsIgnoreCase("TS") )
		{
		    String s = BR1+precision+BR2;
		    return s;
		}
		if(type.trim().equalsIgnoreCase("D"))
		{
		    String s = BR1+length+COMMA+precision+BR2;
		    return s;
		}
		else 
			return "";

	}


	public String getMappedDatatype(String key, String defaultValue) {
		return dataTypeProperties.getProperty(key.trim(), defaultValue);
	}

}
