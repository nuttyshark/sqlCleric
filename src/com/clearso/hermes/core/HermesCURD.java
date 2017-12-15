package com.clearso.hermes.core;

import java.util.ArrayList;
import java.util.Set;

import com.clearso.hermes.types.HermesArray;

public abstract class HermesCURD <T>extends HermesDbTaskBuilder {

	protected ArrayList<HermesDbUnit> srcList = new ArrayList<HermesDbUnit>();

	private ArrayList<HermesPattern<Integer>> retPattern = new ArrayList<HermesPattern<Integer>>();
	private ArrayList<String> retAlias = new ArrayList<String>();

	private ArrayList<HermesPattern<Integer>> condPattern = new ArrayList<HermesPattern<Integer>>();
	protected boolean condNeed = true;
	
	public HermesCURD(){
		this(defaultConf());
	}

	public HermesCURD(HermesDbConf conf){
		this.conf = conf;
	}
	
	public void NullCond(){
		condNeed = false;
	}

	@SuppressWarnings("unchecked")
	public T Clear(){
		retPattern.clear();
		retAlias.clear();
		condPattern.clear();
		srcList.clear();
		condNeed = true;
		_clear();
		return (T)this;
	}
	
	protected abstract void _clear();

	public T Ret(Integer col){
		return Ret("^_^", col);
	}
	
	public T Ret(String pattern, Integer... cols){
		return Ret(pattern, null, cols);
	}

	@SuppressWarnings("unchecked")
	public T Ret(String pattern, String alias, Integer... cols){
		retPattern.add(new HermesPattern<Integer>(pattern, cols));
		retAlias.add(alias);
		return (T)this;
	}

	@SuppressWarnings("unchecked")
	public T Cond(String pattern, Integer... col){
		condPattern.add(new HermesPattern<Integer>(pattern, col));
		return (T)this;
	}

	public T Conde(Object equal, Integer col){
		return Cond("^_^ = "+equal, col);
	}

	public T Condi(Set<?> equal, Integer col){
		if(equal.size() == 0){
			return Cond("false");
		}else{
			return Cond("^_^ IN "+new HermesSqlVal(equal), col);
		}
	}

	protected String buildCond(){
		StringBuilder cond = null;
		for(HermesPattern<Integer> elem: condPattern){
			if(cond == null){
				cond = new StringBuilder(" WHERE ");
			}else{
				cond.append(" AND ");
			}
			cond.append(" (").append(buildPattern(elem)).append(")");
		}
		if(cond == null){
			if(condNeed){
				throw new HermesException("Null cond is not allowed");
			}
			return " ";
		}else{
			return cond.toString();
		}
	}

	public String buildRet(){
		StringBuilder sql = null;
		for(int i=0; i<retPattern.size(); i++){
			HermesPattern<Integer> pattern = retPattern.get(i);
			if(sql == null){
				sql=new StringBuilder(" RETURNING ");
			}else{
				sql.append(",");
			}
			sql.append(buildPattern(pattern));
			if(retAlias.get(i) != null){
				sql.append(" as ").append(retAlias.get(i));
			}
		}
		return sql == null?"":sql.toString();
	}
	

	protected void checkCol(Integer[] cols){
		for(Integer col:cols){
			checkCol(col);
		}
	}
	
	protected int checkCol(int index){
		int found;
		found = HermesArray.find(srcList, elem -> {
			return elem.ValidCol(index);
		});
		if(found == -1){
			throw new HermesException("illegal col");
		}
		return found;
	}
	
}
