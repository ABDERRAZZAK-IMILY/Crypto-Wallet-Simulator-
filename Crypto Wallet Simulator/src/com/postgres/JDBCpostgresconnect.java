package com.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.ui.Main;
import com.util.AppLogger;

public class JDBCpostgresconnect {
	
    private static final Logger logger = AppLogger.getLogger(Main.class.getName());

	   private static final String URL = "jdbc:postgresql://localhost:5432/crepto";
	    private static final String USER = "postgres";
	    private static final String PASSWORD = "202580";
	    
	    
	    private static JDBCpostgresconnect instanse;
	    
	    private Connection connection;
	    
	    
	    private  JDBCpostgresconnect() {
	    	
	    	try {
	    		
	    		this.connection = DriverManager.getConnection(URL , USER , PASSWORD);
	    		
	    		logger.info("Connection successfully");
	    		
	    	} catch (SQLException e) {
				e.printStackTrace();
			}
	    	
	    }
	    
	    public void closeConnection() {
	        if (connection != null) {
	            try {
	                connection.close();
	                logger.info("Connection closed successfully");
	            } catch (SQLException e) {
	                logger.severe("Error closing connection: " + e.getMessage());
	            }
	        }
	    }
	    
	    public static JDBCpostgresconnect getinstanse() {
	    	
	    	
	    			if (instanse == null) {
	    				instanse = new JDBCpostgresconnect();
	    			}
	    			
	    		
	    	
	    	
	    	return instanse;
	    }
	    
	    public Connection getconnection() {
	    	
	    	return connection;
	    }
	    

}
