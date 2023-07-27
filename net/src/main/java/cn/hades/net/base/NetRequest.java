package cn.hades.net.base;

import androidx.annotation.NonNull;

import java.util.Map;

public class NetRequest {

    private String key;
    private String url1;
    private String url2;
    private Map<String, Object> args;
    private NetCacheType cacheType = NetCacheType.NO_CACHE;
    private long cacheTime = 0;

    public boolean checkFail() {
        return key == null || url1 == null || cacheType == null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }

    public NetCacheType getCacheType() {
        return cacheType;
    }

    public void setCacheType(NetCacheType cacheType) {
        this.cacheType = cacheType;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
    }

    @NonNull
    @Override
    public String toString() {
        return "NetRequest{" +
                "key='" + key + '\'' +
                ", url1='" + url1 + '\'' +
                ", url2='" + url2 + '\'' +
                ", args=" + args +
                ", cacheType=" + cacheType +
                ", cacheTime=" + cacheTime +
                '}';
    }

}
