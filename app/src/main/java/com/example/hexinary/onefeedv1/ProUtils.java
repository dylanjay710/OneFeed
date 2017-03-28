package com.example.hexinary.onefeedv1;

/**
 * Created by hexinary on 17-3-28.
 */

/* DD */
public class ProUtils {

    private static ProUtils _instance;

    private ProUtils() {

    }

    public static ProUtils getInstance() {
        if (_instance == null) {
            _instance = new ProUtils();
        }
        return _instance;
    }
//    private  ProUtils getInstance() {
//        return this.proUtilsInstance;
//    }

    public void log(String msg) {
        System.out.println(msg);
    }

    public void logArgs(String... args) {
        for (String s : args)
            System.out.println(s);
    }


}
