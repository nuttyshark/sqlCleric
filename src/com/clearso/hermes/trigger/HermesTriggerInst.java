package org.naertui.hermes.trigger;

import org.naertui.hermes.HermesCURD;

public abstract class HermesTriggerInst {

	public final HermesTriggerAction action;
	
	public final Integer table;
	
	public final Integer[] cols;
	
	public HermesTriggerInst(HermesTriggerAction action, Integer table, Integer[] cols){
		this.action = action;
		this.cols = cols;
		this.table = table;
	}
	
	public abstract HermesTriggerInst clone();
	
	public abstract void trig(Integer[] cols, HermesCURD<?> curd);
	
}
