package cn.hades.net;

import com.google.gson.reflect.TypeToken;

import io.reactivex.disposables.Disposable;

public interface OnNetListener<T> {

    TypeToken<T> getTypeToken();

    String decodeDataStr(String dataStr);

    boolean checkData(T data);

    void onStart();

    void onCache(T data);

    void onNetStart(Disposable disposable);

    void onNetWin(T data);

    void onNetFail(Throwable throwable);

    void onNetEnd();

    void onEnd();

}
