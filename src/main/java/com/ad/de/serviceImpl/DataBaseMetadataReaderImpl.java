package com.ad.de.serviceImpl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.ad.de.config.AppDataStore;
import com.ad.de.domain.Column;
import com.ad.de.domain.RdbmsTableMetadata;
import com.ad.de.domain.TableInfo;


@Service
public class DataBaseMetadataReaderImpl implements com.ad.de.service.DataBaseMetadataReader {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseMetadataReaderImpl.class);

	@Autowired
	private DataSource dataSource;

	@Override
	public void fetchDatabaseMetadata() {
		LOGGER.info("Started processing database Metadata");

	}

	@Override
	public void fetchDatabaseMetadataLimitedUser() {

		LOGGER.info("Started processing database Metadata");
		Connection con = DataSourceUtils.getConnection(dataSource);
		List<String> nonExistingRdbmsTables = new ArrayList();
		List<TableInfo> tableInfoList = AppDataStore.getConfigTableInfoList();
		int totalTableSize = tableInfoList.size();
		int index = 0;
		for (TableInfo tableInfo : tableInfoList) {
			String schema = tableInfo.getSourceSchema();
			String name = tableInfo.getSourceName();

			LOGGER.info("============================================================================");
			LOGGER.info("======READING SOURCE METADATA for TABLE {} : {} OF {} =============", name, ++index,
					totalTableSize);
			LOGGER.info("============================================================================");
			String query = "HELP TABLE " + schema + "." + name + ";";
			String tempQuery = "CREATE VOLATILE TABLE TEMPTABLE1 AS (SELECT top 1 * from " + schema + "." + name
					+ " ) with data;";
			String tempTableHelpQuery = "HELP TABLE TEMPTABLE1";
			String tempTableDropQuery = "DROP TABLE TEMPTABLE1;";
			Statement stmt = null;
			RdbmsTableMetadata tableMD = new RdbmsTableMetadata();
			tableMD.setSchema(schema);
			tableMD.setName(name);

			List<Column> colList = new ArrayList<>();
			try {
				stmt = con.createStatement();
				ResultSet rs1 = stmt.executeQuery(tempQuery);
				ResultSet rs = stmt.executeQuery(tempTableHelpQuery);
				while (rs.next()) {
					String columnName = rs.getString("COLUMN NAME");
					String columnType = rs.getString("TYPE").trim();
					// System.out.println("Column name :- " + columnName + " " +
					// columnType);
					String columnLength = "";
					if (columnType.equalsIgnoreCase("D"))
						columnLength = rs.getString("DECIMAL TOTAL DIGITS");
					else
						columnLength = rs.getString("MAX LENGTH");
					int precision = rs.getInt("DECIMAL FRACTIONAL DIGITS");
					Column col = new Column.ColumnBuilder().name(columnName).type(columnType).length(columnLength)
							.precision(precision).build();
					colList.add(col);
				}
				tableMD.setCols(colList);
				AppDataStore.updateRdbmsMetadataCache(tableMD);
				LOGGER.info("Successfully cached rdbms metadata.");
				ResultSet rs2 = stmt.executeQuery(tempTableDropQuery);
                stmt.close();
				// ResultSet tbls = md.getTables(null, schema, name, null);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			} 
			
		}
		try {
			
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
