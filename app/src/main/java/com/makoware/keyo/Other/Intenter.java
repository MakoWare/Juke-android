package com.makoware.keyo.Other;

import android.content.Intent;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by doman412 on 4/15/14.
 */
public class Intenter {
    private static Intenter ourInstance = new Intenter();

    public static Intenter getInstance() {
        return ourInstance;
    }

    private HashMap<String, Object> intents;

    private Intenter() {
        this.intents = new HashMap<String, Object>();
    }

    private HashMap<String, Object> getIntents(){
        return this.intents;
    }

    public static void putObjectForKey(String key, Object obj) {
        ourInstance.intents.put(key, obj);
    }

    public static Object getObjectForKey(String key){
        Object obj = ourInstance.intents.get(key);
        return obj;
    }

}
