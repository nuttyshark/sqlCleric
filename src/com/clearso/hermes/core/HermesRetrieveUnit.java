package com.clearso.hermes.core;

import java.util.ArrayList;

import com.clearso.hermes.types.HermesArray;

public class HermesRetrieveUnit extends HermesDbUnit {
	
	String tag;
	public ArrayList<String> cols;
	
	public HermesRetrieveUnit(HermesRetrieveBuilder hr,
							  String alias){
		super("");
		tag = alias;
		cols = hr.GetCols();
		for(String col:cols){
			if(col == null){
				throw new HermesException("Null alias is not allowed in sub select");
			}
		}
	}
	
	public int ColFind(String colName){
		int index = HermesArray.find(cols, r -> {
			return r.equals(colName);
		});
		if(index == -1){
			throw new HermesException("col "+colName+" not found");
		}
		return index+Offset();
	}

	@Override
	public String Tag() {
		
		return tag;
	}

	@Override
	public int Offset() {
		
		return 1000000;
	}

	@Override
	public HermesDbUnit Clear() {
		
		return null;
	}

	@Override
	public int ColNum() {
		
		return cols.size();
	}

	@Override
	public String ColName(int index, boolean prefix) {
		
		if(ValidCol(index)){
			index -= Offset();
			if(prefix){
				return TableName()+"."+cols.get(index);
			}else{
				return cols.get(index);
			}
		}else{
			return null;
		}
	}

	@Override
	public String Var(int index) {
		
		if(ValidCol(index)){
			index -= Offset();
			return cols.get(index);
		}else{
			throw new HermesException("Invalid col index");
		}
	}

	@Override
	public HermesDbUnit Col(int index, Object val) throws HermesException {
		
		return null;
	}

	@Override
	public int HasCol(String col) {
		
		int index = HermesArray.find(cols, r -> {
			return r.equals(col);
		});
		return index;
	}

}
