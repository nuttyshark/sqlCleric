package com.clearso.hermes;

public class HermesOriginalBuilder extends HermesDbTaskBuilder {

	private String sql;
	
	public HermesOriginalBuilder(HermesDbHolder da){
		super(da);
	}
	
	public HermesOriginalBuilder setSql(String sql){
		this.sql = sql;
		return this;
	}
	
	@Override
	public String toSql() {
		// TODO Auto-generated method stub
		return sql;
	}

}
