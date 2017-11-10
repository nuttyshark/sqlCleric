package com.clearso.hermes;

import java.util.ArrayList;

public class HermesRetrieveBuilder extends HermesCURD<HermesRetrieveBuilder> {
	
	public final int JOIN_NORMAL = 0;
	public final int JOIN_LEFT = 1;
	public final int JOIN_RIGHT = 2;
	public final int JOIN_FULL = 3;
	public final int JOIN_UNION = 4;
	
	private ArrayList<joinLink> srcLink = new ArrayList<joinLink>();

	private ArrayList<String> colAlias = new ArrayList<String>();
	private ArrayList<HermesPattern<Integer>> colPattern = new ArrayList<HermesPattern<Integer>>();
	
	private HermesPattern<Integer> groupPattern = null;
	
	private ArrayList<HermesPattern<Integer>> orderPattern = new ArrayList<HermesPattern<Integer>>();
	private ArrayList<Boolean> orderDesc = new ArrayList<Boolean>();
	
	private ArrayList<HermesRetrieveBuilder> uniList = new ArrayList<HermesRetrieveBuilder>();
	
	private int limit = -1;
	
	private int skip = -1;
	
	class joinLink{
		public int joinType;
		public HermesPattern<Integer> pattern = null;
		
		public static final String DefaultLink = " "+sep+" = "+sep+" ";
		
		public joinLink(int joinType, String pattern, Integer... col){
			this.pattern = new HermesPattern<Integer>(pattern, col);
			this.joinType = joinType;
		}
		
	}
	/*
	public HermesRetrieveBuilder(){
		super();
	}
	*/

	public HermesRetrieveBuilder(HermesDbHolder da){
		super(da);
	}
	
	public HermesRetrieveBuilder(HermesDbHolder da, HermesDbConf conf){
		super(da, conf);
		Clear();
	}
	
	public HermesRetrieveBuilder Clear(){
		
		srcList.clear();
		srcLink.clear();
		
		colAlias.clear();
		colPattern.clear();
		
		orderPattern.clear();
		orderDesc.clear();
		
		uniList.clear();
		
		groupPattern = null;
		limit = -1;
		skip = -1;
		return super.Clear();
	}
	
	public HermesRetrieveBuilder Uni(HermesRetrieveBuilder uni){
		uniList.add(uni);
		return this;
	}

	public HermesRetrieveBuilder Src(int src){
		return Src(conf.Get(src), JOIN_NORMAL, null, 0);
	}
	
	public HermesRetrieveBuilder Src(int dst, int srcCol, int dstCol, int type){
		return Src(conf.Get(dst), type,  joinLink.DefaultLink, srcCol, dstCol);
	}
	
	public HermesRetrieveBuilder Src(int dst, int type, String pattern, Integer... col){
		return Src(conf.Get(dst), type,  pattern, col);
	}
	
	public HermesRetrieveBuilder Src(HermesDbUnit dst, int type, String pattern, Integer... col){
		if(conf == null){
			throw new HermesException("You need to set your conf first");
		}
		//remember no rpt join allowed
		if(HermesArray.find(srcList, elem -> {
			return elem.Offset() == dst.Offset();
		})!= -1){
			return this;
		}
		srcList.add(dst);
		joinLink jl;
		switch(type){
		case JOIN_LEFT:
		case JOIN_RIGHT:
		case JOIN_FULL:
			if(dst == null){
				throw new HermesException("no ref for join selected");
			}
			jl = new joinLink(type, pattern, col);
			break;
		case JOIN_NORMAL:
			jl = new joinLink(type, null, 0);
			break;
		default:
			jl = null;
			break;
		}
		srcLink.add(jl);
		return this;
	}
	
	public HermesRetrieveBuilder ExCol(int col){
	
		int rm_fnd = HermesArray.find(colPattern, elem->{
			if(elem.item.length == 1){
				if(elem.item[0] == col){
					if(elem.pattern.equals("^_^")){
						return true;
					}
				}
			}
			return false;
		});
		if(rm_fnd == -1){
			throw new HermesException("col not found");
		}
		colPattern.remove(rm_fnd);
		colAlias.remove(rm_fnd);
		return this;
	}
	
	public HermesRetrieveBuilder Col(int col){
		return Col(col, null);
	}
	
	public HermesRetrieveBuilder Col(int col, String alias){
		return Col(alias, sep, col);
	}
	
	public HermesRetrieveBuilder Col(String pattern, Integer ...col){
		return Col(null, pattern, col);
	}
	
	public HermesRetrieveBuilder Col(String alias, String pattern, Integer... col){
		for(Integer x:col){
			checkCol(x);
		}
		if(alias != null){
			if(HermesArray.find(colAlias, elem -> {
				if(elem != null){
					return elem.equals(alias);
				}else{
					return false;
				}
			}) != -1){
				throw new HermesException("alias repeat");
			}
		}
		colPattern.add(new HermesPattern<Integer>(pattern, col));
		colAlias.add(alias);
		return this;
	}
	
	public HermesRetrieveBuilder Ord(Integer col){
		return Ord(col, false); 
	}
	
	public HermesRetrieveBuilder Ord(Integer col, boolean desc){
		return Ord(desc, sep, col);
	}
	
	public HermesRetrieveBuilder Ord(String pattern, Integer... col){
		return Ord(false, pattern, col);
	}
	
	public HermesRetrieveBuilder Ord(boolean desc, String pattern, Integer... col){
		orderPattern.add(new HermesPattern<Integer>(pattern, col));
		orderDesc.add(desc);
		return this;
	}
	
	public HermesRetrieveBuilder Grp(Integer col){
		return Grp("^_^", col);
	}

	public HermesRetrieveBuilder Grp(String pattern, Integer... col){
		groupPattern = new HermesPattern<Integer>(pattern, col);
		return this;
	}
	
	public HermesRetrieveBuilder L(int i){
		limit = i;
		return this;
	}
	
	public HermesRetrieveBuilder S(int skp){
		skip = skp;
		return this;
	}
	
	private String buildCols(){
		StringBuilder col = null;
		for(int i = 0; i<colPattern.size(); i++){
			if(col == null){
				col = new StringBuilder(" ");
			}else{
				col.append(", ");
			}
			col.append(buildPattern(colPattern.get(i)));
			if(colAlias.get(i) != null){
				col.append(" as ").append(colAlias.get(i));
			}
			col.append(" ");
		}
		return col.toString();
	}
	
	private String buildOrder(){
		StringBuilder order = null;
		for(int i=0; i<orderPattern.size(); i++){
			if(order == null){
				order = new StringBuilder(" ORDER BY ");
			}else{
				order.append(" , ");
			}
			order.append(buildPattern(orderPattern.get(i)));
			if(orderDesc.get(i)){
				order.append(" DESC ");
			}
		}
		return order == null?"":order.toString();
	}
	
	private String buildGroup(){
		return groupPattern == null?"":" GROUP BY " + buildPattern(groupPattern);
	}
	
	
	public String toSql(){

		StringBuilder sql = new StringBuilder("SELECT ");
		sql.append(buildCols())
			.append(" FROM ")
			.append(srcList.get(0).TableName());
		for(int i=1; i<srcList.size(); i++){
			boolean join = false;
			if(srcLink.get(i).joinType == JOIN_LEFT){
				join = true;
				sql.append(" LEFT JOIN ");
			}else if(srcLink.get(i).joinType == JOIN_RIGHT){
				join = true;
				sql.append(" RIGHT JOIN ");
			}else if(srcLink.get(i).joinType == JOIN_FULL){
				join = true;
				sql.append(" FULL JOIN ");
			}
			sql.append(srcList.get(i).TableName());
			if(join){
				sql.append(" ON ")
					.append(buildPattern(srcLink.get(i).pattern));
			}
		}
		sql.append(buildCond()).append(buildGroup()).append(buildOrder());
		if(limit != -1){
			sql.append(" LIMIT ").append(limit);
		}
		if(skip != -1){
			sql.append(" OFFSET ").append(skip);
		}
		//String subSql =
		for(HermesRetrieveBuilder uni:uniList){
			sql.append(" UNION ALL ").append(uni.toSql());
		}
		return sql.toString();
	}


	public ArrayList<String> GetCols() {
		// TODO Auto-generated method stub
		return colAlias;
	}
	
	@Override
	protected void _clear() {
		// TODO Auto-generated method stub
		
	}
}
