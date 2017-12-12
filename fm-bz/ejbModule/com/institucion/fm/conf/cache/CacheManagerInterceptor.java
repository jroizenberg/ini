package com.institucion.fm.conf.cache;

public interface CacheManagerInterceptor {
	public void store(String type, Object key, Object obj);

	public Object get(String type, Object key);

	public void clean();

	public String toString();
	
	public void remove(String type, Object key);

}
