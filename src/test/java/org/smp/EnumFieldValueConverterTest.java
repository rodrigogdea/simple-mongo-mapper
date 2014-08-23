package org.smp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.smm.EnumFieldValueConverter;

public class EnumFieldValueConverterTest {

	public enum EnumTest {
		LUNES, MARTES, MIERCOLES
	}

	@Test
	public void test() {

		EnumFieldValueConverter<EnumTest> converter = new EnumFieldValueConverter<>(EnumTest.class);

		assertEquals("LUNES", converter.convertToDb(EnumTest.LUNES));
		assertEquals(EnumTest.LUNES, converter.convertToEntity("LUNES"));
	}

}
