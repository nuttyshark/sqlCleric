package com.clearso.hermes.types;

import java.util.Date;

import com.clearso.hermes.HermesException;

public class HermesFilter<T> {
	public static final int INT4 = 0;
	public static final int INT8 = 1;
	public static final int DATE = 2;
	public static final int DAY = 3;
	public static final int STRING = 4;
	
	int type;
	
	@SuppressWarnings("rawtypes")
	public HermesFilter(Class a){
		if(a == Integer.class){
			type = INT4;
		}else if(a == Long.class){
			type = INT8;
		}else if(a == Date.class){
			type = DATE;
		}else if(a == HermesDay.class){
			type = DAY;
		}else if(a == String.class){
			type = STRING;
		}else{
			throw new HermesException("unknow filter type");
		}
	}
	
	@SuppressWarnings("unchecked")
	public T Load(String src){
		Object obj = null;
		switch(type){
		case INT4:
			obj = Integer.parseInt(src);
			break;
		case INT8:
			obj = Long.parseLong(src);
			break;
		case DATE:
			obj = HermesDate.ParseDate(src);
			break;
		case DAY:
			obj = HermesDay.ParseDay(src);
			break;
		case STRING:
			obj = src;
			break;
		default:
			break;
		}
		return (T)obj;
	}

}
