package org.smm;

import java.util.HashMap;
import java.util.Map;

public class LocalCache {

	private static ThreadLocal<LocalCache> threadLocal = new ThreadLocal<LocalCache>();

	private Map<Object, Object> cache = new HashMap<Object, Object>();

	private LocalCache() {

	}

	public static LocalCache get() {
		LocalCache localCache = threadLocal.get();

		if (localCache == null) {
			localCache = new LocalCache();
			threadLocal.set(localCache);
		}

		return localCache;
	}

	public void put(Object key, Object value) {
		this.cache.put(key, value);
	}

	public Object find(Object key) {
		return this.cache.get(key);
	}

	public void invalidate() {
		this.cache.clear();
	}
}
