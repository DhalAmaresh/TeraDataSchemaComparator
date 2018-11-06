package com.ad.de.config;

import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import com.ad.de.domain.RdbmsTableMetadata;
import com.ad.de.domain.TableInfo;
import com.ad.de.domain.TableMetadata;


public class AppDataStore {
	private static final Logger LOGGER = LoggerFactory.getLogger(AppDataStore.class);
	
	public AppDataStore(){
		
	}
	
	// ***************************************************************************************************************
	// This cache is used to store xml configured rdbms metadata information
	private static final Map<SimpleEntry<String,String>,TableInfo> CONFIG_METADATA_CACHE = new ConcurrentHashMap<>();
	
	public static void updateConfigMetadataCache(List<TableInfo> tableInfoList){
		Assert.notNull(tableInfoList,"Configured table information must not null");
		LOGGER.info("CONFIG METADATA CACHE details before cache update {}", CONFIG_METADATA_CACHE);
		CONFIG_METADATA_CACHE.clear();
		for (TableInfo tableInfo : tableInfoList) {
			updateConfigMetadataCache(tableInfo);
		}
		LOGGER.info("CONFIG METADATA CACHE details after cache update {}", CONFIG_METADATA_CACHE);
	}
	
	public static void updateConfigMetadataCache(TableInfo tableInfo) {
		Assert.notNull(tableInfo, "Configured table infomration must not null");
		LOGGER.debug("CONFIG METADATA CACHE details before cache update {}", CONFIG_METADATA_CACHE);
		CONFIG_METADATA_CACHE.put(new SimpleEntry<>(tableInfo.getSourceSchema(), tableInfo.getSourceName()), tableInfo);
		LOGGER.debug("CONFIG METADATA CACHE details after cache update {}", CONFIG_METADATA_CACHE);
	}
	
	public static List<TableInfo> getConfigTableInfoList() {
		return Collections.unmodifiableList(new ArrayList<TableInfo>(CONFIG_METADATA_CACHE.values()));
	}

	public static TableInfo getConfigTableInfo(String schema, String tableName) {
		return getConfigTableInfo(new SimpleEntry<>(schema, tableName));
	}

	public static TableInfo getConfigTableInfo(SimpleEntry<String, String> tableEntry) {
		return CONFIG_METADATA_CACHE.get(tableEntry);
	}

	// ***************************************************************************************************************
	// This cache is used to store xml configured rdbms metadata information
	private static final Map<SimpleEntry<String, String>, RdbmsTableMetadata> RDBMS_METADATA_CACHE = new ConcurrentHashMap<>();

	public static void updateRdbmsMetadataCache(List<RdbmsTableMetadata> tableList) {
		Assert.notNull(tableList, "Table information must not null");
		LOGGER.info("RDBMS METADATA CACHE details before cache update {}", RDBMS_METADATA_CACHE);
		RDBMS_METADATA_CACHE.clear();
		for (RdbmsTableMetadata table : tableList) {
			updateRdbmsMetadataCache(table);
		}
		LOGGER.info("RDBMS METADATA CACHE details after cache update {}", RDBMS_METADATA_CACHE);
	}

	public static void updateRdbmsMetadataCache(RdbmsTableMetadata table) {
		Assert.notNull(table, "Table information must not null");
		LOGGER.debug("RDBMS METADATA CACHE details before cache update {}", RDBMS_METADATA_CACHE);
		RDBMS_METADATA_CACHE.put(new SimpleEntry<>(table.getSchema(), table.getName()), table);
		LOGGER.debug("RDBMS METADATA CACHE details after cache update {}", RDBMS_METADATA_CACHE);
	}

	public static List<RdbmsTableMetadata> getRdbmsTableInfoList() {
		return Collections.unmodifiableList(new ArrayList<RdbmsTableMetadata>(RDBMS_METADATA_CACHE.values()));
	}

	public static List<TableMetadata> getRdbmsTableInfoListWithoutSchema() {
		ArrayList<RdbmsTableMetadata> list = new ArrayList<RdbmsTableMetadata>(RDBMS_METADATA_CACHE.values());
		List<TableMetadata> mdList = new ArrayList<>();
		for (RdbmsTableMetadata rdbmsTableMetadata : list) {
			TableMetadata tmd = new TableMetadata();
			tmd.setName(rdbmsTableMetadata.getName());
			tmd.setCols(new ArrayList<>(rdbmsTableMetadata.getCols()));
			mdList.add(tmd);
		}
		return mdList;
	}

	public static RdbmsTableMetadata getRdbmsTableInfo(String schema, String tableName) {
		return getRdbmsTableInfo(new SimpleEntry<>(schema, tableName));
	}

	public static RdbmsTableMetadata getRdbmsTableInfo(SimpleEntry<String, String> tableEntry) {
		return RDBMS_METADATA_CACHE.get(tableEntry);
	}



	// ***************************************************************************************************************
	private static final List<String> NON_EXISTING_RDBMS_TBLS = new ArrayList<>();

	public static void updateNonExistingRdbmsTbls(List<String> nonExistingRdbmsTbls) {
		LOGGER.info("List of NON_EXISTING_RDBMS_TBLS before update is {}", NON_EXISTING_RDBMS_TBLS);
		if ((null != nonExistingRdbmsTbls) && (nonExistingRdbmsTbls.size() > 0)) {
			NON_EXISTING_RDBMS_TBLS.clear();
			NON_EXISTING_RDBMS_TBLS.addAll(nonExistingRdbmsTbls);
		}
		LOGGER.info("List of NON_EXISTING_RDBMS_TBLS after update is {}", NON_EXISTING_RDBMS_TBLS);
	}

	public static List<String> getNonExistingRdbmsTbls() {
		return Collections.unmodifiableList(NON_EXISTING_RDBMS_TBLS);
	}

	// ***************************************************************************************************************
	private static String reportFolder;

	public static String getReportFolder() {
		return reportFolder;
	}

	public static void setReportFolder(String reportFolder) {
		AppDataStore.reportFolder = reportFolder;
	}
	
}
