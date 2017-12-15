package com.clearso.hermes.core;

import java.util.function.Function;

public class HermesPattern <T>{

	public String pattern;
	public T[] item;
	
	public HermesPattern(String pattern, T[] elem){
		this.pattern = pattern;
		this.item = elem;
	}
	
	public String toString(){
		return toString(elem->{
			return elem.toString();}
		);
	}
	
	protected String flag(){
		return "\\^_\\^";
	}

	public String toString(Function<T, String> consumer){
		String rb = pattern;
		for(T elem: item){
			rb = rb.replaceFirst(flag(), consumer.apply(elem));
		}
		return rb;
	}
	
}
