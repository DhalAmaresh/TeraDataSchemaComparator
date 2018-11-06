package com.ad.de.domain;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.util.Assert;


public class Column {
	

	private String name;
	private String type;
	private String length;
	private int precision;
	
	 public Column(){
		 
	 }
	 
	 public Column(String name,String type, String length, int precision){
		 super();
		 this.name = name;
		 this.type = type;
		 this.length=length;
		 this.precision=precision;
	 }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (obj.getClass() != this.getClass())) {
			return false;
		}
		// object must be Test at this point
		Column col = (Column) obj;
		return this.name.equalsIgnoreCase(col.name) && this.type.equalsIgnoreCase(col.type);
	}
	 
	@Override
	public int hashCode() {
		int hash = 3;
		hash = (7 * hash) + this.name.toLowerCase().hashCode();
		hash = (7 * hash) + this.type.toLowerCase().hashCode();
		return hash;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public static class ColumnBuilder {

		private String name;
		private String type;
		private String length;
		private int precision;

		public ColumnBuilder name(String name) {
			this.name = name;
			return this;
		}

		public ColumnBuilder type(String type) {
			this.type = type;
			return this;
		}
		
		public ColumnBuilder length(String length) {
			this.length = length;
			return this;
		}
		
		public ColumnBuilder precision(int precision) {
			this.precision = precision;
			return this;
		}

		public Column build() {
			Assert.notNull(name);
			Assert.notNull(type);
			Assert.notNull(length);
			Assert.notNull(precision);
			return new Column(name, type, length, precision);
		}
	}
}
