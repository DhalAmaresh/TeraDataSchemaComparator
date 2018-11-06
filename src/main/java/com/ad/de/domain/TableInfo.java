package com.ad.de.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@XmlRootElement(name = "table", namespace="tables")
@XmlType(propOrder = { "sourceName","sourceSchema"})
@Service
public class TableInfo {


    public TableInfo() {
    }

    private String sourceName;
    private String sourceSchema;



    @XmlElement(name = "sourceName")
    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String name) {
        this.sourceName = name;
    }

    @XmlElement(name = "sourceSchema")
    public String getSourceSchema() {
        return sourceSchema;
    }

    public void setSourceSchema(String schema) {
        this.sourceSchema = schema;
    }




    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }



}

