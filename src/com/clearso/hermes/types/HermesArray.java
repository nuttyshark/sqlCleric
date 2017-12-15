package com.clearso.hermes.types;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clearso.hermes.core.HermesSqlVal;

public interface HermesArray{
	
	public static<T> ArrayList<T> parse(Class<T> a, String src){
		HermesFilter<T> fl = new HermesFilter<T>(a);
		ArrayList<T> array = new ArrayList<T>();
		Pattern pattern = Pattern.compile("^\\s*\\{([^\\}]*)\\}\\s*$");
		Matcher matcher = pattern.matcher(src);
		if(matcher.find()){
			String body = matcher.group(1);
			String[] vals = body.split(",");
			for(String val:vals){
				array.add(fl.Load(val));
			}
		}
		return array;
	}
	
	public static String sqlSet(List<?> p){
		StringBuilder rt = null;
		for(Object t: p){
			if(rt == null){
				rt = new StringBuilder("(");
			}else{
				rt.append(",");
			}
			rt.append(new HermesSqlVal(t).toString());
		}
		if(rt != null){
			rt.append(")");
		}
		return rt.toString();
	}
	
	public static String toSql(Object[] t, Function<Object, String> cvt){
		StringBuilder rt = null;
		for(Object elem:t){
			if(rt == null){
				rt=new StringBuilder("(");
			}else{
				rt.append(",");
			}
			rt.append(cvt.apply(elem));
		}
		if(rt != null){
			rt.append(")");
		}
		return rt.toString();
	}
		
	public static String toString(List<?> p, Function<Object, String> consumer){
		StringBuilder rt = null;
		for(Object t:p){
			if(rt == null){
				rt = new StringBuilder("");
			}else{
				rt.append(",");
			}
			rt.append(consumer.apply(t));
		}
		return rt.toString();
	}
	
	public static<T> int find(List<T> p, Function<T, Boolean> consumer){
		int i=0;
		for(T t:p){
			if(consumer.apply(t)){
				return i;
			}
			i++;
		}
		return -1;
	}

}
