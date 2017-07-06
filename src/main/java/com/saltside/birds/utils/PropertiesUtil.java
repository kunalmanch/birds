package com.saltside.birds.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by kunal on 7/6/2017.
 */
public class PropertiesUtil {

    public static Properties get(String path) throws IOException {
        Properties props = new Properties();
        InputStream is = PropertiesUtil.class.getClassLoader().getResourceAsStream(path);
        props.load(is);
        return props;
    }
}
