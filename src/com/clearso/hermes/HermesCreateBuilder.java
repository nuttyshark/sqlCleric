package com.clearso.hermes;

//import java.sql.SQLException;

public class HermesCreateBuilder extends HermesCURD<HermesCreateBuilder> {
	
	/*
	public HermesCreateBuilder() throws HermesException, SQLException{
		//maybe bug here
		//this(HermesDbHolder.GetInstance());
	}
	*/
	
	public HermesCreateBuilder Load(HermesDbUnit src){
		srcList.add(src);
		return this;
	}
	
	public String toSql(){
		HermesDbUnit src = srcList.get(0);
		StringBuilder sql = new StringBuilder("INSERT INTO ");
		sql.append(src.TableName());
		StringBuilder col_sql = null;
		StringBuilder val_sql = null;
		for(int i=src.Offset(); i<src.Offset()+src.ColNum(); i++){
			if(src.Var(i) != null){
				if(col_sql == null){
					col_sql = new StringBuilder("");
					val_sql = new StringBuilder("");
				}else{
					col_sql.append(",");
					val_sql.append(",");
				}
				col_sql.append(src.ColName(i));
				val_sql.append(src.Var(i));
			}
		}
		sql.append(" (").append(col_sql).append(") VALUES (").append(val_sql).append(")").append(buildRet());
		
		return sql.toString();
	}

	@Override
	protected void _clear() {
		// TODO Auto-generated method stub
		
	}
	
}
