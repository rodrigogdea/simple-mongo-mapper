package org.smm;

import java.util.regex.Pattern;

import junit.framework.Assert;

import org.junit.Test;
import org.smm.RegExpMap;

public class RegExpMapTest {

	@Test
	public void testWithASimpleKeyValue() {

		RegExpMap<String> regExpMap = new RegExpMap<String>();

		regExpMap.put(Pattern.compile("^John$"), "Coltrane");
		regExpMap.put(Pattern.compile("^Dexter$"), "Gordon");

		Assert.assertEquals("Coltrane", regExpMap.get("John"));
		Assert.assertEquals("Gordon", regExpMap.get("Dexter"));
		Assert.assertNull(regExpMap.get("DexterX"));
	}

	@Test
	public void testWithARegexpKeyValue() {

		RegExpMap<String> regExpMap = new RegExpMap<String>();

		regExpMap.put(Pattern.compile("^some"), "HELL");

		Assert.assertEquals("HELL", regExpMap.get("somePart"));
		Assert.assertEquals("HELL", regExpMap.get("someThing"));
		Assert.assertNull(regExpMap.get("Here there is someOne who..."));
	}

}
