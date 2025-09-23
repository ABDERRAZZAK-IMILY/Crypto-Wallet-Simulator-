package com.util;

import java.io.IOException;
import java.util.logging.*;

public class AppLogger {

    private static Logger logger;

    public static Logger getLogger(String className) {
        if (logger == null) {
            logger = Logger.getLogger(className);

            try {
                // Create FileHandler: "app.log", append mode = true
                FileHandler fileHandler = new FileHandler("logs/myapp.log", true);

                // Simple formatter (or you can use XMLFormatter)
                fileHandler.setFormatter(new SimpleFormatter());

                // Add handler to logger
                logger.addHandler(fileHandler);

                // Optional: disable console logging if you want only file output
                Logger rootLogger = Logger.getLogger("");
                for (Handler h : rootLogger.getHandlers()) {
                    rootLogger.removeHandler(h);
                }

                // Set log level
                logger.setLevel(Level.ALL);

            } catch (IOException e) {
                System.err.println("Failed to initialize logger: " + e.getMessage());
            }
        }
        return logger;
    }
}
