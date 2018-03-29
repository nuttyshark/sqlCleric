package com.clearso.hermes.core;

public interface Hh {

    static HermesRetrieveBuilder retrieve(){
        return new HermesRetrieveBuilder();
    }

    static HermesCreateBuilder create(){
        return new HermesCreateBuilder();
    }

    static HermesUpdateBuilder update(){
        return new HermesUpdateBuilder();
    }

    static HermesDeleteBuilder delete(){
        return new HermesDeleteBuilder();
    }

    static HermesSqlVal V(Object v){
        return new HermesSqlVal(v);
    }

    int JOIN_LEFT = HermesRetrieveBuilder.JOIN_LEFT;
    int JOIN_RIGHT = HermesRetrieveBuilder.JOIN_RIGHT;
    int JOIN_NORMAL = HermesRetrieveBuilder.JOIN_NORMAL;
    int JOIN_FULL = HermesRetrieveBuilder.JOIN_FULL;

}
