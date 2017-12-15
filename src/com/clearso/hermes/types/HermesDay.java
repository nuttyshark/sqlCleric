package com.clearso.hermes.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.clearso.hermes.core.HermesException;


public class HermesDay{
	
	public int day;
	public int month;
	public int year;
	
	public int value;
	
	public HermesDay(){
		
	}
	
	public HermesDay(String src){
		ParseDay(this, src);
	}
	
	public HermesDay(Date src){
		Calendar ca = Calendar.getInstance();
		ca.setTime(src);
		year = ca.get(Calendar.YEAR);
		month = ca.get(Calendar.MONTH) + 1;
		day = ca.get(Calendar.DAY_OF_MONTH);
		val();
	}
	
	public HermesDay(int year, int month, int day){
		this.year = year;
		this.month = month;
		this.day = day;
		val();
	}
	
	public HermesDay(int val){
		SetSr(val);
	}
	
	public int val(){
		value = year*10000+month*100+day;
		return value;
	}
	
	public int minus(HermesDay right){
		Date ths = GetDate();
		Date to = right.GetDate();
		return (int) ((ths.getTime()+1000 - to.getTime())/ (24 * 60 * 60 * 1000));
	}
	
	public Date GetDate(){
		try {
			return new SimpleDateFormat("yyyy/MM/dd/hh/mm")
					.parse(String.format("%04d/%02d/%02d/%02d/%02d", 
								year, month, day, 0, 1));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	public HermesDay SetSr(Integer sr){
		year = sr/10000;
		sr %= 10000;
		month = sr/100;
		day = sr%100;
		val();
		return this;
	}
	
	public static HermesDay GetInstance(Integer sr){
		return new HermesDay().SetSr(sr);
	}
	
	public static HermesDay ParseDay(HermesDay rt, String date){
		String[] e = date.split("-");
		if(e.length < 3){
			throw new HermesException("illegal format");
		}
		rt.day = Integer.parseInt(e[2]);
		rt.month = Integer.parseInt(e[1]);
		rt.year = Integer.parseInt(e[0]);
		rt.val();
		return rt;
	}
	
	public static String Format(HermesDay hd){
		return hd.toString();
	}
	
	public static HermesDay ParseDay(String date){
		return ParseDay(new HermesDay(), date);
	}
	
	public boolean equals(HermesDay hd){
		if(year == hd.year &&
			month == hd.month &&
			day == hd.day){
			return true;
		}else{
			return false;
		}
	}
	
	public String toString(){
		return year+"-"+month+"-"+day;
	}
	
}
