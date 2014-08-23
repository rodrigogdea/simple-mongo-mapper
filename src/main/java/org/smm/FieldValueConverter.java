package org.smm;

public interface FieldValueConverter {

	Object convertToDb(Object aFieldValue);
	
	Object convertToEntity(Object aFieldValue);

}