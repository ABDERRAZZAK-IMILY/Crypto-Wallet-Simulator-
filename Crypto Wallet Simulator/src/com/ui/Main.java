package com.ui;

import java.sql.Connection;
import java.util.logging.Logger;

import com.postgres.JDBCpostgresconnect;
import com.util.AppLogger;


public class Main {

    private static final Logger logger = AppLogger.getLogger(Main.class.getName());

	public static void main(String[] args) {
		
		   logger.info("Application started");
		
		JDBCpostgresconnect db = JDBCpostgresconnect.getinstanse();
	    Connection conn = db.getconnection();

	    if (conn != null) {
	    	
	    	logger.info("Database connection succes");
	    	
	        System.out.println("Connection is ready to use.");
	    }
	    
	    
	}

}
