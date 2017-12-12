package com.clearso.hermes;

public abstract class HermesDbTaskBuilder {
	
	public final static String sep = "^_^";
	public static HermesDbConf _defaultConf;
	public HermesDbConf conf;
	
	public HermesDbTaskBuilder(){
		this(defaultConf());
	}

	public HermesDbTaskBuilder(HermesDbConf conf){
		this.conf = conf;
	}
	
	public static void defaultConf(HermesDbConf conf){
		_defaultConf = conf;
	}

	public static HermesDbConf defaultConf(){
		if(_defaultConf == null){
			throw new HermesException("You need to set your conf first");
		}
		return _defaultConf;
	}
	
	protected String buildPattern(HermesPattern<Integer> pattern){
		return pattern.toString(colOff -> {
			return conf.Get(colOff).ColName(colOff, true);
		});
	}
	
	public abstract String toSql();
	
	public static String SqlElem(Object rd){
		if(rd instanceof Integer ||
		   rd instanceof Long
		   ){
			return rd.toString();
		}else if(rd instanceof Boolean){
			if((Boolean)rd){
				return "TRUE";
			}else{
				return "FALSE";
			}
		}else{
			return "'" + rd.toString() + "'";
		}
	}
	
}
