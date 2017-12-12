package com.clearso.hermes.types;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HermesRange<T>{
	public T st;
	public T ed;
	public final int CLOSED = 0;
	public final int OPENED = 1;
	public int lc;
	public int rc;
	HermesFilter<T> filter = null;

	public HermesRange(){
		st = null;
		ed = null;
		lc = CLOSED;
		rc = OPENED;
	}
	
	public HermesRange(T start, T end){
		st = start;
		ed = end;
		lc = CLOSED;
		rc = OPENED;
	}

	@SuppressWarnings("rawtypes")
	public HermesRange<T> Parse(Class a, String src){
		if(filter == null){
			filter = new HermesFilter<T>(a);
		}
		Pattern pattern = Pattern.compile("^\\s*([\\(\\[])\\s*([^,\\s]*)\\s*,\\s*([^\\)\\]\\s]*)\\s*([\\)\\]])\\s*$");
		Matcher matcher = pattern.matcher(src);
		if(matcher.find()){
			//String[] x = matcher.gro
			lc = matcher.group(1).equals("(")?OPENED:CLOSED;
			st=filter.Load(matcher.group(2));
			ed=filter.Load(matcher.group(3));
			rc = matcher.group(4).equals(")")?OPENED:CLOSED;
		}
		return this;
	}
	
	public String toString(){
		String rb = "";
		if(lc == OPENED){
			rb = rb + "(";
		}else{
			rb = rb + "[";
		}
		rb = rb + st.toString() + "," + ed.toString();
		if(rc == OPENED){
			rb = rb + ")";
		}else{
			rb = rb + "]";
		}
		return rb;
	}

}
