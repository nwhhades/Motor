package cn.hades.net.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public enum RetrofitUtils {
    instance;

    private static final String baseUrl = "https://www.baidu.com/";

    private final Retrofit gsonRetrofit;

    private final Retrofit stringRetrofit;

    RetrofitUtils() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(3, TimeUnit.SECONDS);
        builder.writeTimeout(3, TimeUnit.SECONDS);
        builder.readTimeout(3, TimeUnit.SECONDS);
        //日志
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);
        builder.retryOnConnectionFailure(true);
        //初始化
        this.gsonRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
        this.stringRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    public <T> T createGsonService(Class<T> tClass) {
        return gsonRetrofit.create(tClass);
    }

    public <T> T createStringService(Class<T> tClass) {
        return stringRetrofit.create(tClass);
    }

}
