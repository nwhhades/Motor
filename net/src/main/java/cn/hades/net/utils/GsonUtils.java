package cn.hades.net.utils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public enum GsonUtils {
    instance;

    private final Gson gson;

    GsonUtils() {
        gson = new Gson();
    }

    public <T> T fromJson(final String json, @NonNull final Type type) {
        try {
            return gson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toJson(Object o) {
        return gson.toJson(o);
    }

}
