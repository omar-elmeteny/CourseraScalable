package com.guctechie.classes;

import java.io.InputStream;
import java.util.Properties;

public class ProjectProperties {
    private static final Properties properties = new Properties();

    static {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = loader.getResourceAsStream("application.properties");
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

//    public static void main(String[] args) {
//        System.out.println(ProjectProperties.getProperty("eurekaServerHost"));
//    }

}
