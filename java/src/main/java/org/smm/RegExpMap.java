package org.smm;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegExpMap<T> {

	private Map<Pattern, T> valueMap = new HashMap<Pattern, T>();

	public void put(Pattern keyPattern, T value) {
		this.valueMap.put(keyPattern, value);
	}

	public T get(String key) {

		for (Map.Entry<Pattern, T> entry : this.valueMap.entrySet()) {
			if (entry.getKey().matcher(key).find()) {
				return entry.getValue();
			}
		}

		return null;
	}

}
