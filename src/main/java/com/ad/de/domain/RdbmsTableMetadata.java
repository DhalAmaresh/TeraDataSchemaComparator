package com.ad.de.domain;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class RdbmsTableMetadata extends TableMetadata {

	/**
	 *
	 */
	public RdbmsTableMetadata() {
	}

	private String schema;	
	private String targetName;
	// It is possible to have composite primary key
	private List<String> pkList;
	private String targetSchema;


	public String getTargetSchema() {
		return targetSchema;
	}

	public void setTargetSchema(String targetSchema) {
		this.targetSchema = targetSchema;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public List<String> getPkList() {
		return pkList;
	}

	public void setPkList(List<String> pkList) {
		this.pkList = pkList;
	}

}

