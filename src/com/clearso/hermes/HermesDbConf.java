package org.naertui.hermes;


public abstract class HermesDbConf {

	public abstract HermesDbUnit Get(int offset);
	
	public abstract HermesDbUnit Get(String tag);
	
}
