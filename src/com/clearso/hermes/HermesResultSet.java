package org.naertui.hermes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Function;

import org.naertui.hermes.types.HermesDate;
import org.naertui.hermes.types.HermesDay;
import org.naertui.hermes.types.HermesRange;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class HermesResultSet{

	public ResultSet rs;
	public Object ret = null;
	public Object res = null;
	public int length;
	public int iterRunner = 0;
	
	public HermesResultSet(ResultSet rs){
		try{
			this.rs = rs;
			if (rs.getType() != ResultSet.TYPE_FORWARD_ONLY){
				this.length = HermesDbHolder.ResultSetSize(rs);
			}else{
				this.length = -1;
			}
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public int GetInt(){
		iterRunner++;
		return GetInt(iterRunner);
	}
		
	public int GetInt(int label){
		try{
			return rs.getInt(label);
		}catch(SQLException e){
			throw new HermesException(e);
		}		
	}

	public int GetInt(String label){
		try{
			return rs.getInt(label);
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public Integer GetInteger(){
		iterRunner++;
		return GetInteger(iterRunner);
	}
		
	public Integer GetInteger(int label){
		try{
			return rs.getInt(label);
		}catch(SQLException e){
			throw new HermesException(e);
		}		
	}

	public Integer GetInteger(String label){
		try{
			return rs.getInt(label);
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public String GetString(){
		iterRunner++;
		return GetString(iterRunner);
	}
		
	public String GetString(String label){
		try{
			return rs.getString(label);
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public String GetString(int label){
		try{
			return rs.getString(label);
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public Double GetDouble(){
		iterRunner++;
		return GetDouble(iterRunner);
	}

	public Double GetDouble(int label){
		try{
			return rs.getDouble(label);
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public Double GetDouble(String label){
		try{
			return rs.getDouble(label);
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public long GetLong(){
		iterRunner++;
		return GetLong(iterRunner);
	}
	
	public long GetLong(String label){
		try{
			return rs.getLong(label);
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public long GetLong(int label){
		try{
			return rs.getLong(label);
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public Object GetObject(){
		iterRunner++;
		return GetObject(iterRunner);
	}
	
	public Object GetObject(String label){
		try{
			return rs.getObject(label);
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public Object GetObject(int label){
		try{
			return rs.getObject(label);
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public boolean GetBoolean(){
		iterRunner++;
		return GetBoolean(iterRunner);
	}
	
	public boolean GetBoolean(String label){
		try{
			return rs.getBoolean(label);
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public boolean GetBoolean(int label){
		try{
			return rs.getBoolean(label);
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public JSONObject GetJSONObject(){
		iterRunner++;
		return GetJSONObject(iterRunner);
	}
	
	public JSONObject GetJSONObject(String label){
		try{
			return JSON.parseObject(rs.getString(label));
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public JSONObject GetJSONObject(int label){
		try{
			return JSON.parseObject(rs.getString(label));
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public JSONArray GetJSONArray(){
		iterRunner++;
		return GetJSONArray(iterRunner);
	}
	
	public JSONArray GetJSONArray(String label){
		try{
			return JSON.parseArray(rs.getString(label));
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public JSONArray GetJSONArray(int label){
		try{
			return JSON.parseArray(rs.getString(label));
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public<T> ArrayList<T> GetArray(Class<T> t){
		iterRunner++;
		return GetArray(iterRunner, t);
	}
	
	public<T> ArrayList<T> GetArray(int label, Class<T> t){
		try{
			return HermesArray.parse(t, rs.getString(label));
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}
	

	public<T> ArrayList<T> GetArray(String label, Class<T> t){
		try{
			return HermesArray.parse(t, rs.getString(label));
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}
	
	public HermesDay GetHermesDay(){
		iterRunner++;
		return GetHermesDay(iterRunner);
	}
	
	public HermesDay GetHermesDay(int label){
		return HermesDay.ParseDay(GetString(label));
	}
	
	public HermesDay GetHermesDay(String label){
		return HermesDay.ParseDay(GetString(label));
	}
	
	public<T> HermesRange<T> GetHermesRange(Class<T> a){
		iterRunner++;
		return GetHermesRange(a, iterRunner);
	}

	public<T> HermesRange<T> GetHermesRange(Class<T> a, int label){
		return new HermesRange<T>().Parse(a, GetString(label));
	}
	
	public<T> HermesRange<T> GetHermesRange(Class<T> a, String label){
		return new HermesRange<T>().Parse(a, GetString(label));
	}
	
	
	public Date GetDate(){
		iterRunner++;
		return GetDate(iterRunner);
	}

	public Date GetDate(int label){
		return HermesDate.ParseDate(GetString(label));
	}

	public Date GetDate(String label){
		return HermesDate.ParseDate(GetString(label));
	}
	
	@SuppressWarnings("unchecked")
	public<T> T ret(Class<T> a,Function<Object, Object> consumer) {
		if(ret == null){
			ret = consumer.apply(null);
		}
		return (T)ret;
	}

	public boolean Next(){
		try{
			iterRunner = 0;
			return rs.next();
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}
	
	public void Close(){
		try{
			rs.close();
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}
	
}
