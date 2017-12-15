package com.clearso.hermes.core;

import java.util.ArrayList;

//import java.sql.SQLException;

public class HermesUpdateBuilder extends HermesCURD<HermesUpdateBuilder>{

	ArrayList<HermesPattern<Integer>> colPattern = new ArrayList<HermesPattern<Integer>>();
	ArrayList<Integer> colOff = new ArrayList<Integer>();
	
	public HermesUpdateBuilder(){
		
	}

	@Override
	protected void _clear() {
		// TODO Auto-generated method stub
		colPattern.clear();
		colOff.clear();
	}

	public HermesUpdateBuilder Src(HermesDbUnit src){
		srcList.add(src);
		return this;
	}
	
	public HermesUpdateBuilder Src(int src){
		srcList.add(conf.Get(src));
		return this;
	}
	
	public HermesUpdateBuilder Col(int col, String pattern, Integer...cols){
		colOff.add(col);
		colPattern.add(new HermesPattern<Integer>(pattern, cols));
		return this;
	}
	
	@Override
	public String toSql() {
		// TODO Auto-generated method stub
		HermesDbUnit src = srcList.remove(0);
		StringBuilder sql = new StringBuilder("UPDATE ").append(src.TableName());
		StringBuilder set_sql = null;
		for(int i=src.Offset(); i<src.Offset()+src.ColNum(); i++){
			if(src.Var(i) != null){
				if(set_sql == null){
					set_sql = new StringBuilder(" SET ");
				}else{
					set_sql.append(",");
				}
				set_sql.append(src.ColName(i))
					.append(" = ")
					.append(src.Var(i));
			}
		}
		for(int i=0; i<colOff.size(); i++){
			if(set_sql == null){
				set_sql = new StringBuilder(" SET ");
			}else{
				set_sql.append(",");
			}
			set_sql.append(src.ColName(colOff.get(i)))
				.append(" = ")
				.append(buildPattern(colPattern.get(i)));
		}
		if(set_sql == null){
			throw new HermesException("no variable set");
		}
		sql.append(set_sql);
		boolean firstRef = true;
		do{
			if(srcList.size()>0){
				src = srcList.get(srcList.size()-1);
			}else{
				break;
			}
			if(firstRef){
				sql.append(" FROM ");
				firstRef = false;
			}else{
				sql.append(", ");
			}
			sql.append(src.TableName()).append(" ");
		}while(true);
		sql.append(buildCond()).append(buildRet());
		return sql.toString();
	}

}
