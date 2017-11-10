package org.naertui.hermes;

import java.io.FileInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.naertui.hermes.trigger.HermesTrigger;
import org.naertui.hermes.trigger.HermesTriggerAction;
//import org.naertui.hermes.trigger.HermesTriggerInst;

public class HermesDbHolder {
	
	public int DbType = 0;
	
	protected Connection _conn = null;
	protected Statement _st = null;
	
	public static DataSource datasource = null;
	private int transLevel = 0;
	
	public HermesTrigger trigger = null;
	
	public static int act = 0;
	public static int hact = 0;
	public static int conerr = 0;

	public String lstSql = "";
	
    private static interface Singleton {
        final HermesDbHolder INSTANCE = new HermesDbHolder();
    }
	
	private HermesDbHolder(){
		if(datasource == null){
			try{
				Class.forName("org.postgresql.Driver");
				FileInputStream fi = new FileInputStream("../conf/pgdb.conf");
				Properties pro = new Properties();
				pro.load(fi);
				fi.close();
				HermesDbHolder.set_db(pro);
			}catch(Exception e){
				System.out.println("db init failed");
			}
		}
	}
	
	public static void set_db(Properties pro){
		PoolProperties p = new PoolProperties();
		String url = "jdbc:postgresql://"+pro.getProperty("db_host")+":"+pro.getProperty("db_port")+"/"+pro.getProperty("db_name");
        p.setUrl(url);
        p.setDriverClassName("org.postgresql.Driver");
        p.setUsername(pro.getProperty("db_user"));
        p.setPassword(pro.getProperty("db_passwd"));
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(190);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(false);
        p.setRemoveAbandoned(true);
        
        p.setJdbcInterceptors(
          "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
          "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        datasource = new DataSource();
        datasource.setPoolProperties(p);
	}
	
	public static HermesDbHolder GetInstance(){
		hact ++;
		return new HermesDbHolder();
	}
	
	public static Connection GetConn() throws SQLException{
		return Singleton.INSTANCE.conn();
	}

	public Connection conn() throws SQLException{
		if(_conn != null){
			if(!_conn.isClosed()){
				return _conn;
			}
		}	
		try{
			_st = null;
			_conn = datasource.getConnection();
			act ++;
			return _conn;
		}catch(SQLException e){
			throw e;
		}
	}
	
	private Statement st(){
		try{
			if(this._st == null){
				this._st = conn().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
												  ResultSet.CONCUR_READ_ONLY);
			}
			return this._st;
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public void die(){
		try{
			TransactionRollBack();
			if(this._st != null && !this._st.isClosed()){
				this._st.close();
				this._st = null;
			}
			if(this._conn != null && !this._conn.isClosed()){
				hact --;
				this._conn.close();
				act --;
				this._conn = null;
			}
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	private static final int MODE_NORM = 0;
	private static final int MODE_TRANSACTION = 1;
	
	private int running_mode = MODE_NORM;
	
	public void TransactionStart(){
		if(transLevel == 0){
			try{
				conn().setAutoCommit(false);
				running_mode = MODE_TRANSACTION;
			}catch(SQLException e){
				throw new HermesException(e);
			}
		}
		transLevel ++;
	}
	
	public void TransactionEnd(){
		transLevel --;
		if(transLevel == 0){
			try{
				conn().commit();
				conn().setAutoCommit(true);
				running_mode = MODE_NORM;
			}catch(SQLException e){
				throw new HermesException(e);
			}
		}else if(transLevel < 0){
			transLevel = 0;
		}
	}
	
	public void TransactionRollBack(){
		try{
			if(this.running_mode == MODE_TRANSACTION){
				transLevel = 0;
				conerr ++;
				conn().rollback();
			}
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}
			
	public ResultSet ExecSqlQ(String sql){
		lstSql = sql;
		try{
			if(this.running_mode == MODE_NORM){
				return st().executeQuery(sql);
			}else if(this.running_mode == MODE_TRANSACTION){
				PreparedStatement pst = null;
				pst = conn().prepareStatement(sql);
				return pst.executeQuery();
			}else{
				throw new HermesException("SQL Mode Invalid, only Trans and Norm is allowed");
			}
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public int ExecSql(String sql){
		lstSql = sql;
		try{
			if(this.running_mode == MODE_NORM){
				return st().executeUpdate(sql);
			}else if(this.running_mode == MODE_TRANSACTION){
				PreparedStatement pst = null;
				pst = conn().prepareStatement(sql);
				return pst.executeUpdate();
			}else{
				throw new HermesException("SQL Mode Invalid, only Trans and Norm is allowed");
			}
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public CallableStatement ProcCall(String proc){
		try{
			TransactionStart();
			return conn().prepareCall(proc);
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public void ProcDone(CallableStatement proc){
		try{
			TransactionEnd();
			proc.close();
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}

	public static int ResultSetSize(ResultSet rs){
		try{
	        if(! rs.next()){
	            return 0;
	        }
	        rs.last();
	        int setCount = rs.getRow();
	        rs.beforeFirst();
	        return setCount;
		}catch(SQLException e){
			throw new HermesException(e);
		}
	}
	
	public void Trig(int col, HermesTriggerAction action, HermesCURD<?> curd){
		if(trigger == null){
			return;
		}
		//HermesTriggerInst inst = trigger.hm.get(col).get(action);
		//if()
	}
	
}
