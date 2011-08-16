package com.facecook.sassy.watson.myconnect;

import java.util.HashMap;

import android.graphics.drawable.Drawable;

class ImageCache {
    private static final HashMap<String, Drawable> cache = new HashMap<String, Drawable>();
    
    public static Drawable get(String url) {
        if (url == null) {
            return null;
        }
        return cache.get(url);
    }
    
    public static void put(String url, Drawable drawable) {
        if (cache.containsKey(url)) {
            return;
        }
        cache.put(url, drawable);
    }
    
    public static boolean isCache(String url) {
        return cache.containsKey(url);
    }

}
