package cn.hades.net.simple;

import com.google.gson.reflect.TypeToken;

import cn.hades.net.OnNetListener;

public interface OnStringNetListener extends OnNetListener<String> {

    default TypeToken<String> getTypeToken() {
        return new TypeToken<String>() {
        };
    }

    default String decodeDataStr(String dataStr) {
        return dataStr;
    }

    default boolean checkData(String data) {
        return data != null;
    }

}
