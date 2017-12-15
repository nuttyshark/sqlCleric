package com.clearso.hermes.core;

import java.sql.SQLException;

public class HermesException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8048477223712010015L;

	public static final int LevelNorm = 0;
	public static final int LevelSql = 0;
	public static final int LevelExp = 0;
	
	Exception loader = null;
	
	public int Level;
	
	public HermesException(String msg){
		this(msg, LevelNorm);
	}
	
	public HermesException(String msg, int level){
		this(null, LevelNorm, msg);
	}

	public HermesException(SQLException e){
		this(e, "");
	}

	public HermesException(SQLException e, String msg){
		this(e, LevelSql, msg);
	}
	
	public HermesException(Exception e){
		this(e, "");
	}
	
	public HermesException(Exception e, String msg){
		this(e, LevelExp, msg);
	}
	
	private static String extractException(Exception e){
		if(e == null){
			return "";
		}else{
			return e.getClass().getName() + " - " + e.getMessage();
		}
	}
	
	public HermesException(Exception e, int level, String msg){
		super(msg + extractException(e));
		Level = level;
		loader = e;
	}

}
