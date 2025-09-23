package com.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCpostgresconnect {
	
	   private static final String URL = "jdbc:postgresql://localhost:5432/crepto";
	    private static final String USER = "postgres";
	    private static final String PASSWORD = "202580";
	    
	    private void connect() {
	    	
	    	try(Connection connection = DriverManager.getConnection(URL , USER , PASSWORD)){
	    		if(connection == null) {
	    			throw new IllegalArgumentException("Fail to connect postgres sql");
	    			
	    		}else {
	    			System.out.println("connected succeffluy");
	    		}
	    		
	    		
	    	} catch (SQLException e) {
				e.printStackTrace();
			}
	    	
	    }
	    
	    public static void main(String[] args) {
	    	
	    	JDBCpostgresconnect newsql = new JDBCpostgresconnect();
	    	newsql.connect();
	    	
	    }
	    

}
