package com.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCpostgresconnect {
	
	   private static final String URL = "jdbc:postgresql://localhost:5432/crepto";
	    private static final String USER = "postgres";
	    private static final String PASSWORD = "202580";
	    
	    
	    private static JDBCpostgresconnect instanse;
	    
	    private Connection connection;
	    
	    
	    private  JDBCpostgresconnect() {
	    	
	    	try(Connection connection = DriverManager.getConnection(URL , USER , PASSWORD)){
	    		if(connection == null) {
	    			throw new RuntimeException("Fail to connect postgres sql");
	    			
	    		}else {
	    			System.out.println("connected succeffluy");
	    		}
	    		
	    		
	    	} catch (SQLException e) {
				e.printStackTrace();
			}
	    	
	    }
	    
	    public static JDBCpostgresconnect getinstanse() {
	    	
	    	if (instanse == null) {
	    		synchronized (JDBCpostgresconnect.class) {
	    			if (instanse == null) {
	    				instanse = new JDBCpostgresconnect();
	    			}
	    			
	    		}
	    	}
	    	
	    	return instanse;
	    }
	    
	    public Connection getconnection() {
	    	
	    	return connection;
	    }
	    
	    public static void main(String[] args) {
	    	
	    	
	    	JDBCpostgresconnect db = JDBCpostgresconnect.getinstanse();
	        Connection conn = db.getconnection();

	        if (conn != null) {
	            System.out.println(" Connection is ready to use.");
	        }
	    }
	    

}
