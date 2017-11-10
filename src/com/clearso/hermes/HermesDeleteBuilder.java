package org.naertui.hermes;

//import java.sql.SQLException;

public class HermesDeleteBuilder extends HermesCURD<HermesDeleteBuilder> {
	
	/*
	public HermesDeleteBuilder() throws HermesException, SQLException{
		//maybe bug here
		//this(HermesDbHolder.GetInstance());
	}
	*/
	
	public HermesDeleteBuilder(HermesDbHolder da){
		super(da);
	}
	
	public HermesDeleteBuilder Src(int src){
		srcList.add(conf.Get(src));
		return this;
	}

	@Override
	public String toSql() {
		// TODO Auto-generated method stub
		HermesDbUnit src = srcList.remove(srcList.size()-1);
		StringBuilder sql = new StringBuilder("DELETE FROM ")
								.append(src.TableName());
		sql.append(buildCond()).append(buildRet());
		return sql.toString();
	}

	@Override
	protected void _clear() {
		// TODO Auto-generated method stub
		
	}

}
