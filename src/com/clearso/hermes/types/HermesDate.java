package com.clearso.hermes.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.clearso.hermes.HermesException;

public class HermesDate {
	
	public static final int SEC = 0;
	public static final int MIN = 1;
	public static final int DAY = 2;
	
	Date rec;
	
	public HermesDate(){
		rec = new Date();
	}
	
	public HermesDate(String src){
		this();
		rec = ParseDate(src);
	}
	
	public HermesDate(Date src){
		this();
		rec = src;
	}
	
	public static Date ParseDate(String date){
		try{
			SimpleDateFormat dateFormat;
			if(date == null){
				return null;
			}
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = date.replaceAll("\"", "");
			if(date.contains("+")){
				String[] r = date.split("\\+");
				dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+"+r[1]));
			}else{
				dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
			}
			Date rt = dateFormat.parse(date.split("\\.")[0]);
			return rt;
		}catch(ParseException e){
			throw new HermesException(e);
		}
	}
	
	public static Date After(Date src, int unit, long after){
		long msp = 0L;
		switch(unit){
		case SEC:
			msp = after * 1000L;
			break;
		case MIN:
			msp = after * 60000L;
			break;
		case DAY:
			msp = after * 1440000L;
			break;
		default:
			throw new HermesException("illegal date unit");
		}
		if(src == null){
			src = new Date();
		}
		return new Date(src.getTime() + msp);
	}
	
	public static String Format(Date date){
		SimpleDateFormat dateFormat;
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}
	
	public String toString(){
		return Format(rec);
	}
	
}
