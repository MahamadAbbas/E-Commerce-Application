package com.retail.e_com.cache;

import java.time.Duration;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class CacheStore<T> {

	private Cache<String, T> cache;

	public CacheStore(int maxAge) {
		this.cache = CacheBuilder.newBuilder()
		.expireAfterWrite(Duration.ofMinutes(maxAge))
		.concurrencyLevel(Runtime.getRuntime().availableProcessors())
		.build();
	}
	
	public void add(String key, T value) {
		cache.put(key, value);
	}
	
	public T getData(String key) {
		return cache.getIfPresent(key);
	}
	
	public void removeData(String key) {
		
	}
}
