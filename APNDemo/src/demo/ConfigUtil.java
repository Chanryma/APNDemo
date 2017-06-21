package demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class ConfigUtil {  
    private static final String INI_FILE_NAME = "/EnnNotificationPushProxy.ini";  
    private static HashMap<String, String> propertyMap = null;  
  
    static {  
        propertyMap = new HashMap<String, String>();  
        String path = URLUtil.getClassPath(ConfigUtil.class) + INI_FILE_NAME;  
        LogUtil.debug("EnnNotificationPushProxy.ini path=" + path);  
        File file = new File(path);  
  
        FileInputStream inStream = null;  
        try {  
            inStream = new FileInputStream(file);  
        } catch (FileNotFoundException e) {  
            LogUtil.error(e);  
        }  
  
        Properties properties = new Properties();  
        try {  
            properties.load(inStream);  
        } catch (IOException e) {  
            LogUtil.error(e);  
        }  
  
        @SuppressWarnings("unchecked")  
        Enumeration<String> keys = (Enumeration<String>) properties.propertyNames();  
  
        while (keys.hasMoreElements()) {  
            String key = keys.nextElement();  
            propertyMap.put(key.toLowerCase(), properties.getProperty(key));  
        }  
    }  
  
    public static String get(String key) {  
        return propertyMap.get(key.toLowerCase());  
    }  
  
    public static int getInt(String key) {  
        String valueStr = propertyMap.get(key.toLowerCase());  
        int value = 0;  
        try {  
            value = Integer.valueOf(valueStr);  
        } catch (Exception e) {  
            LogUtil.error(e);  
            value = 0;  
        }  
  
        return value;  
    }  
  
    public static long getLong(String key) {  
        String valueStr = propertyMap.get(key.toLowerCase());  
        long value = 0;  
        try {  
            value = Long.valueOf(valueStr);  
        } catch (Exception e) {  
            LogUtil.error(e);  
            value = 0;  
        }  
  
        return value;  
    }  
  
    public static void set(String key, String value) {  
        if (get(key) == null) {  
            propertyMap.put(key, value);  
        }  
    }  
} 
