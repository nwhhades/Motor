package cn.hades.net.cache;

import android.app.Application;
import android.util.Log;

import com.tencent.mmkv.MMKV;

public enum CacheUtils {
    instance;

    private static final String TAG = "CacheUtils";
    private static final String KEY_TIME_SUFFIX = "_TIME";
    private MMKV mmkv;

    CacheUtils() {
    }

    public void init(final Application app) {
        if (mmkv == null) {
            Log.d(TAG, "init: 初始化MMKV");
            String root = MMKV.initialize(app);
            Log.d(TAG, "init: MMKV缓存保存地址 - " + root);
            mmkv = MMKV.mmkvWithID(TAG);
        }
    }

    private String getTimeKey(String key) {
        return key + KEY_TIME_SUFFIX;
    }

    public synchronized String get(String key, String def) {
        String value = def;
        if (key != null) {
            String timeKey = getTimeKey(key);
            long outTime = mmkv.getLong(timeKey, 0);
            if (outTime <= 0 || outTime > System.currentTimeMillis()) {
                //没有过期 或者  过期时间小于等于0
                value = mmkv.getString(key, def);
            } else {
                //过期了，移除保存的值
                mmkv.removeValueForKey(key);
                mmkv.removeValueForKey(timeKey);
            }
        }
        Log.d(TAG, "get: 获取缓存数据 \n key => " + key + " \n value => " + value);
        return value;
    }

    public synchronized void put(String key, String value, long cacheTime) {
        Log.d(TAG, "put: 保存缓存数据 \n key => " + key + " \n value => " + value + " \n cacheTime => " + cacheTime);
        if (key != null && value != null) {
            mmkv.putString(key, value);
            mmkv.putLong(getTimeKey(key), cacheTime > 0 ? (System.currentTimeMillis() + cacheTime) : 0);
        }
    }

    public synchronized void put(String key, String value) {
        put(key, value, 0);
    }

    public synchronized void remove(String key) {
        Log.d(TAG, "remove: 删除缓存数据 \n key => " + key);
        if (key != null) {
            mmkv.removeValueForKey(key);
            mmkv.removeValueForKey(getTimeKey(key));
        }
    }

    public synchronized void clearAll() {
        Log.d(TAG, "clear: 清除缓存数据");
        mmkv.clearAll();
    }

}
