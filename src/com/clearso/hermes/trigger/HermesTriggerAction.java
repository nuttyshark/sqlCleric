package com.clearso.hermes.trigger;

public enum HermesTriggerAction {

	INSERT(0),
	UPDATE(1),
	DELETE(2);
	
	public int v;
	
	private HermesTriggerAction(int value){
		v = value;
	}
	
}
