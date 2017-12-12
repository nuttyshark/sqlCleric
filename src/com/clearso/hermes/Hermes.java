package com.clearso.hermes;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.TimeZone;

public class Hermes {

	public static int exp = 0;
	
	public static void Init(String fname, HermesDbConf conf){
		try{
			FileInputStream fi;
			try {
				fi = new FileInputStream(fname);

				Properties pro = new Properties();
				pro.load(fi);
				fi.close();
				TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			HermesDbTaskBuilder.defaultConf(conf);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
