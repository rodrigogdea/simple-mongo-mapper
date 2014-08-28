package org.smm;

public class EnumFieldValueConverter<T extends Enum<T>> implements FieldValueConverter {

	private Class<T> enumType;

	public EnumFieldValueConverter(Class<T> enumType) {
		this.enumType = enumType;
	}

	@Override
	public Object convertToDb(Object aFieldValue) {
		return aFieldValue.toString();
	}

	@Override
	public Object convertToEntity(Object aFieldValue) {
		return Enum.valueOf(enumType, (String) aFieldValue);
	}

}
