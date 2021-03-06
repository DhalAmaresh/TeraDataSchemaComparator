package com.ad.de.service;

public interface XmlToJavaConverter<T> {

	/**
	 * This method will read given xml files and unmarshals to java object.
	 * In case of any exception this method will throw {@link DataValidatorException}
	 */
	public T convertFromXMLToObject(String xmlfile) ;
}
