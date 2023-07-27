package cn.hades.net.service;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GetService {

    @GET
    Observable<String> get(@Url String url);

}
