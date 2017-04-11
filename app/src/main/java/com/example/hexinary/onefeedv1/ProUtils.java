package com.example.hexinary.onefeedv1;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hexinary on 17-3-28.
 */

/* DD */
public class ProUtils {

    private static ProUtils _instance;

    private ProUtils() {}

    public static ProUtils getInstance() {
        if (_instance == null) {
            _instance = new ProUtils();
        }
        return _instance;
    }

    public void logArgs(List<String> args) {
        for (String s : args)
            System.out.println(s);
    }

    public void logUserFacebookFeed(LinkedHashMap<String, List<String>> userFeed) {
        for (String key : userFeed.keySet())
            for (String elem : userFeed.get(key))
                log(elem + "," + key);
    }

    public void log(String msg) {
        System.out.println(msg);
    }

}
