package com.clearso.hermes.toolbox;

import java.util.Random;

public class HermesRand extends Random{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 4608413957124471394L;
	Random random;
	 
	 public HermesRand(){
		 super(System.nanoTime());
	 }
	 
	public String getRandomNum(int length){  
		return getRandomRaw(length, "0123456789");
	}
	    
	public String getRandomString(int length){ 
		return getRandomRaw(length, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
	}

	public String getRandomRaw(int length, String range){
        StringBuffer sb = new StringBuffer();  
        for(int i = 0 ; i < length; ++i){  
            int number = nextInt(range.length());//[0,62)  
            sb.append(range.charAt(number));  
        }  
        return sb.toString();  
	}  
}
