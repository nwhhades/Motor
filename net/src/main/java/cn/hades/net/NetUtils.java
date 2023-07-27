package cn.hades.net;

import android.app.Application;

import androidx.annotation.NonNull;

import java.lang.reflect.Type;

import cn.hades.net.base.NetCacheType;
import cn.hades.net.base.NetRequest;
import cn.hades.net.cache.CacheUtils;
import cn.hades.net.utils.GsonUtils;
import cn.hades.net.utils.ServiceUtils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public enum NetUtils {
    instance;

    public static void init(final Application app) {
        CacheUtils.instance.init(app);
    }

    public <T> void get(@NonNull NetRequest request, @NonNull OnNetListener<T> onNetListener) {
        onRequest(true, request, onNetListener);
    }

    public <T> void post(@NonNull NetRequest request, @NonNull OnNetListener<T> onNetListener) {
        onRequest(false, request, onNetListener);
    }

    private static <T> void onRequest(boolean isGet, @NonNull NetRequest request, @NonNull OnNetListener<T> onNetListener) {
        onNetListener.onStart();
        //检查请求
        if (request.checkFail()) {
            onNetListener.onEnd();
            return;
        }
        //检查缓存
        final Type dataType = onNetListener.getTypeToken().getType();
        final NetCacheType cacheType = request.getCacheType();
        final boolean saveCache;
        boolean stopRequest = false;
        T cacheData = null;
        if (cacheType == NetCacheType.NO_CACHE) {
            saveCache = false;
        } else {
            saveCache = true;
            if (cacheType == NetCacheType.ONLY_CACHE) {
                stopRequest = true;
            }
            cacheData = readCacheData(request.getKey(), dataType);
        }
        //发送缓存
        if (cacheData != null) {
            onNetListener.onCache(cacheData);
            if (stopRequest) {
                onNetListener.onEnd();
                return;
            }
        }
        //开始网络请求
        getObservable(request, isGet)
                .doOnDispose(() -> {
                    onNetListener.onNetFail(new Exception("请求被主动终止"));
                    onNetListener.onEnd();
                }).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        onNetListener.onNetStart(d);
                    }

                    @Override
                    public void onNext(String s) {
                        //解密数据
                        s = onNetListener.decodeDataStr(s);
                        //解析数据
                        T data = parseDataStr(s, dataType);
                        if (onNetListener.checkData(data)) {
                            //保存数据
                            if (saveCache) {
                                writeCacheData(request.getKey(), s, request.getCacheTime());
                            }
                            onNetListener.onNetWin(data);
                            onNetListener.onNetEnd();
                            onNetListener.onEnd();
                        } else {
                            onNetListener.onNetFail(new Exception("数据格式检查不通过"));
                            onNetListener.onEnd();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        onNetListener.onNetFail(e);
                        onNetListener.onEnd();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private static Observable<String> getObservable(@NonNull NetRequest request, boolean isGet) {
        Observable<String> observable;
        if (isGet) {
            Observable<String> observable2 = ServiceUtils.instance.getGetService()
                    .get(request.getUrl2())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onTerminateDetach();
            observable = ServiceUtils.instance.getGetService()
                    .get(request.getUrl1())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(observable2)
                    .onTerminateDetach();
        } else {
            Observable<String> observable2 = ServiceUtils.instance.getPostService()
                    .post(request.getUrl2(), request.getArgs())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onTerminateDetach();
            observable = ServiceUtils.instance.getPostService()
                    .post(request.getUrl1(), request.getArgs())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .onErrorResumeNext(observable2)
                    .onTerminateDetach();
        }
        return observable;
    }

    private static <T> T readCacheData(@NonNull String key, @NonNull Type dataType) {
        String data = CacheUtils.instance.get(key, null);
        return parseDataStr(data, dataType);
    }

    private static void writeCacheData(@NonNull String key, @NonNull String dataStr, long cacheTime) {
        CacheUtils.instance.put(key, dataStr, cacheTime);
    }

    @SuppressWarnings("unchecked")
    private static <T> T parseDataStr(@NonNull final String dataStr, @NonNull final Type dataType) {
        try {
            if (dataType == String.class) {
                return (T) dataStr;
            } else {
                return GsonUtils.instance.fromJson(dataStr, dataType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
