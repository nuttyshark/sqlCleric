package com.clearso.hermes.types;

import java.util.Date;
import java.util.HashMap;

import com.clearso.hermes.core.HermesException;

public class HermesMap extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1446223169291807101L;
	
	public HermesMap(){
		super();
	}
	
	public HermesMap Put(String key, Object r){
		put(key, r);
		return this;
	}
	
	public static HermesMap Puts(String key, Object r){
		return new HermesMap().Put(key, r);
	}
	
	public Object get(String key, boolean need){
		Object res = get(key);
		if(res == null){
			if(need){
				throw new HermesException(key+" not found");
			}
			return null;
		}else{
			return res;
		}
	}

	public String getString(String key){
		return getString(key, false);
	}
	
	public String getString(String key, boolean need){
		return (String)get(key, need);
	}

	public int getIntValue(String key){
		return (int)getInteger(key);
	}
	
	public int getIntValue(String key, boolean need){
		return (int)getInteger(key, need);
	}

	public Integer getInteger(String key){
		return getInteger(key, false);
	}
	
	public Integer getInteger(String key, boolean need){
		return (Integer)get(key, need);
	}
	
	public long getLongValue(String key){
		return (long)getLong(key);
	}
	
	public long getLongValue(String key, boolean need){
		return (long)getLong(key, need);
	}
	
	public Long getLong(String key){
		return getLong(key, false);
	}
	
	public Long getLong(String key, boolean need){
		return (Long)get(key, need);
	}
	
	public Double getDouble(String key, boolean need){
		return (Double)get(key, need);
	}
	
	public Double getDouble(String key){
		return getDouble(key, false);
	}

	public HermesDay getHermesDay(String key){
		return getHermesDay(key, false);
	}
	
	public HermesDay getHermesDay(String key, boolean need){
		return (HermesDay)get(key, need);
	}
	
	public Date getDate(String key){
		return getDate(key, false);
	}
	
	public Date getDate(String key, boolean need){
		return (Date)get(key, need);
	}

	public<T> T[] getArray(Class<T> cls, String key){
		return getArray(cls, key, false);
	}

	public<T> T getObject(Class<T> cls, String key){
		return getObject(cls, key, false);
	}

	@SuppressWarnings("unchecked")
	public<T> T getObject(Class<T> cls, String key, boolean need){
		return (T)get(key,need);
	}
	
	@SuppressWarnings("unchecked")
	public<T> T[] getArray(Class<T> cls, String key, boolean need){
		return (T[])get(key, need);
	}

	public Boolean getBoolean(String key){
		return getBoolean(key, false);
	}
	
	public Boolean getBoolean(String key, boolean need){
		return (Boolean)get(key, need);
	}
	
	public HermesMap Merge(String prefix, HermesMap src){
		for(String key: src.keySet()){
			put(prefix+key, src.get(key));
		}
		return this;
	}
	
}
