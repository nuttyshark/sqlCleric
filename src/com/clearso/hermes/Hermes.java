package org.naertui.hermes;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.TimeZone;

import org.naertui.hermes.HermesDbHolder;

public class Hermes {

	public static int exp = 0;

	protected HermesDbHolder dh = null;
	
	public static void Init(String fname, HermesDbConf conf){
		try{
			FileInputStream fi;
			try {
				fi = new FileInputStream(fname);

				Properties pro = new Properties();
				pro.load(fi);
				fi.close();
				HermesDbHolder.set_db(pro);
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
	
	protected Hermes(){
		this(HermesDbHolder.GetInstance());
	}
	
	public Hermes(HermesDbHolder dh){
		setDh(dh);
	}

	protected void setDh(HermesDbHolder dh){
		this.dh = dh;
	}
	
	public void die() throws SQLException{
		dh.die();
	}
	
}
