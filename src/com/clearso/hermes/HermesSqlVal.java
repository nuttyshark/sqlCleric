package com.clearso.hermes;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

public class HermesSqlVal {
	static String reg = "(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"  
            		  + "(\\b(select|update|and|or|delete|insert|"
            		  + "trancate|char|into|substr|ascii|declare|"
            		  + "exec|count|master|into|drop|execute)\\b)"; 
	static Pattern pattern = null;
	String val;

	@SuppressWarnings("unchecked")
	public HermesSqlVal(Object r){
		if(r == null){
			this.val = "";
		}else{
			if(r instanceof Integer ||
			   r instanceof Long ||
			   r instanceof Float ||
			   r instanceof Double ||
			   r instanceof Boolean){
				this.val = r.toString();
			}else if(r instanceof String){
				String rt = (String) r;
				this.val = "'"+rt.replace("'", "''")+"'";
			}else if(r instanceof List<?>){
				this.val = "ARRAY["+(HermesArray.toString((List<?>)r, obj -> {
					return new HermesSqlVal(obj).toString();
				}))+"]";
			}else if(r instanceof Set<?>){
				this.val = null;
				StringBuilder sb = null;
				Set<Object> m = (Set<Object>)r;
				for(Object obj:m){
					if(sb == null){
						sb = new StringBuilder("(");
					}else{
						sb.append(",");
					}
					sb.append(new HermesSqlVal(obj).toString());
				}
				this.val = sb.append(")").toString();
			}else if(r instanceof Date){
				this.val = "TO_TIMESTAMP("+((Date)r).getTime()/1000+")";
			}else if(r instanceof JSONObject){
				this.val = "'"+((JSONObject)r).toJSONString()+"'::jsonb";
			}else{
				this.val = "'"+r.toString()+"'";
			}
			Safety();
		}
	}
	
	public void Safety(){
		if(pattern == null){
			pattern = Pattern.compile(reg);
		}
		if(pattern.matcher(this.val).find()){
			//throw new HermesException("SQL Injection");
		}
	}
	
	public String toString(){
		return val;
	}

}
