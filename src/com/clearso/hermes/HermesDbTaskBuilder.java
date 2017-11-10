package com.clearso.hermes;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class HermesDbTaskBuilder {
	
	protected HermesDbHolder dbHolder;
	public final static String sep = "^_^";
	public static HermesDbConf _defaultConf;
	public HermesDbConf conf;
	
	
	public HermesDbTaskBuilder(){
		//this(HermesDbHolder.GetInstance());
	}
	
	public HermesDbTaskBuilder(HermesDbHolder da){
		this(da, defaultConf());
	}

	public HermesDbTaskBuilder(HermesDbHolder da, HermesDbConf conf){
		dbHolder = da;
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
	
	public void Exec(){
		dbHolder.ExecSql(toSql());
	}

	public Object Exec(Object resultHolder, Function<HermesResultSet, Boolean> analyse){
		return Exec(Object.class, null, resultHolder, analyse);
	}
	
	public Object Exec(Function<HermesResultSet, Boolean> analyse){
		return Exec(null, analyse);
	}

	public<T> T Exec(Class<T> a, Supplier<T> consumer, Function<HermesResultSet, Boolean> analyse){
		return Exec(a, consumer, null, analyse);
	}

	
	@SuppressWarnings("unchecked")
	public<T> T Exec(Class<T> a, Supplier<T> consumer, Object resultHolder, Function<HermesResultSet, Boolean> analyse){
		HermesResultSet rs = new HermesResultSet(dbHolder.ExecSqlQ(toSql()));
		rs.res = resultHolder;
		if(consumer != null){
			rs.ret = consumer.get();
		}
		while(rs.Next()){
			if(analyse.apply(rs)){
				break;
			}
		}
		rs.Close();
		return (T)rs.ret;
	}
	
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
