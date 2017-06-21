package demo;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LogUtil {  
    private static final Logger logger;  
    private static final String INI_FILE_NAME = "/Users/jonathanma/Documents/workspace/APNDemo/log4j.properties";  
    private static final String TAG = "APNDemo";  
  
    static {  
//        System.out.println("ProjectHomePath=" + URLUtil.getProjectHomePath());  
//        System.setProperty("WORKDIR", URLUtil.getProjectHomePath());  
        String path = INI_FILE_NAME;  
        PropertyConfigurator.configure(path);  
        logger = Logger.getLogger(TAG);  
    }  

    public static void debug(Object message) {  
        logger.info(message);  
    }  
  
    public static void debug(String message, Throwable e) {  
        logger.info(message, e);  
    }  
  
    public static void info(String message) {  
        logger.info(message);  
    }  
  
    public static void info(String message, Throwable e) {  
        logger.info(message, e);  
    }  
  
    public static void error(String message) {  
        logger.error(message);  
    }  
  
    public static void error(Object message, Throwable e) {  
        logger.error(message, e);  
    }  
  
    public static void error(Throwable e) {  
        logger.error("", e);  
    }  
}  
