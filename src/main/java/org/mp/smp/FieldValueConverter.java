package org.mp.smp;

public interface FieldValueConverter {

	Object convertToDb(Object aFieldValue);
	
	Object convertToEntity(Object aFieldValue);

}