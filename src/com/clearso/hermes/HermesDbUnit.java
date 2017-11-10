package com.clearso.hermes;

public abstract class HermesDbUnit {

	protected static String table_prefix_default = "";
	protected String tablePrefix;
	
	protected HermesDbHolder dc;
	
	public static void SetTablePrefixDefault(String prefix){
		table_prefix_default = prefix;
	}
	
	public static String SqlVarMap(Object r){
		if(r == null){
			return null;
		}
		if(r instanceof Integer ||
		   r instanceof Long ||
		   r instanceof Float ||
		   r instanceof Double ||
		   r instanceof Boolean){
			return r.toString();
		}else if(r instanceof String){
			return "'"+r+"'";
		}else{
			return "'"+r.toString()+"'";
		}
	}

	public HermesDbUnit(){
		this(table_prefix_default);
	}

	public HermesDbUnit(String prefix){
		tablePrefix = prefix;
	}
	
	public abstract String Tag();
	
	public String TableName(){
		return tablePrefix + Tag();
	}
	
	public boolean ValidCol(int index){
		index -= Offset();
		if(index < 0 || index >= ColNum()){
			return false;
		}
		return true;
	}
	
	public abstract int Offset();
	
	public abstract HermesDbUnit Clear();
	
	public abstract int ColNum();

	public String ColName(int index){
		return ColName(index, false);
	}
	
	public abstract String ColName(int index, boolean prefix);
	
	public abstract String Var(int index);
	
	public abstract HermesDbUnit Col(int index, Object val) throws HermesException;
	
	public abstract int HasCol(String col);
		
}
