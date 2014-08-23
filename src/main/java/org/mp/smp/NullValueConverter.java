package org.mp.smp;

public class NullValueConverter implements FieldValueConverter {

	@Override
	public Object convertToDb(Object aFieldValue) {
		return aFieldValue;
	}
	
	@Override
	public Object convertToEntity(Object aFieldValue) {
		return aFieldValue;
	}
	
}
