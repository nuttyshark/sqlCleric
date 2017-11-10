package org.naertui.hermes.trigger;

import java.util.ArrayList;
import java.util.HashMap;

import org.naertui.hermes.HermesCURD;

public class HermesTrigger {

	public HashMap<Integer, HashMap<HermesTriggerAction, ArrayList<HermesTriggerInst>>> hm;
	
	public HermesTrigger(){
		hm = new HashMap<Integer, HashMap<HermesTriggerAction, ArrayList<HermesTriggerInst>>>();
	}
	
	public void inject(HermesTriggerInst inst){
		HashMap<HermesTriggerAction, ArrayList<HermesTriggerInst>> trig = hm.get(inst.table);
		if(trig == null){
			trig = new HashMap<HermesTriggerAction, ArrayList<HermesTriggerInst>>();
			//trig.put(inst.action, new ArrayList<HermesTriggerInst>().Push(inst));
		}else{
			ArrayList<HermesTriggerInst> instSet = trig.get(inst.action);
			if(instSet == null){
				//trig.put(inst.action, new ArrayList<HermesTriggerInst>().Push(inst));
			}else{
				instSet.add(inst);
			}
		}
	}
	
	public void trig(Integer table, HermesTriggerAction action, HermesCURD<?> curd){
		HashMap<HermesTriggerAction, ArrayList<HermesTriggerInst>> trig = hm.get(table);
		if(trig != null){
			ArrayList<HermesTriggerInst> instSet = trig.get(action);
			if(instSet != null){
				for(HermesTriggerInst inst:instSet){
					inst.trig(null, curd);
				}
			}
		}
	}
	
}
