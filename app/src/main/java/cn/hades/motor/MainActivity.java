package cn.hades.motor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import cn.hades.motor.databinding.ActivityMainBinding;
import cn.hades.net.NetUtils;
import cn.hades.net.base.NetCacheType;
import cn.hades.net.base.NetRequest;
import cn.hades.net.cache.CacheUtils;
import cn.hades.net.simple.OnStringNetListener;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    protected ActivityMainBinding binding;

    private static final String def_url1 = "http://192.168.50.246:8080";
    private static final String def_url2 = "http://www.baidu.com";
    private static final String get_args = "?id=3&name=张三&msg=这是get请求";

    private final Map<String, Object> post_args = new HashMap<>();

    protected NetCacheType cacheType = NetCacheType.NO_CACHE;

    protected long cacheTime = 7000;

    protected Disposable d;

    protected OnStringNetListener netListener = new OnStringNetListener() {
        @Override
        public void onStart() {
            Log.d(TAG, "onStart: 请求开始");
            addMsg("请求开始");
        }

        @Override
        public void onCache(String data) {
            addMsg("读取缓存成功" + data);
        }

        @Override
        public void onNetStart(Disposable disposable) {
            addMsg("网络请求开始");
            d = disposable;
        }

        @Override
        public void onNetWin(String data) {
            addMsg("网络请求成功" + data);
        }

        @Override
        public void onNetFail(Throwable throwable) {
            addMsg("网络请求失败" + throwable.getMessage());
        }

        @Override
        public void onNetEnd() {
            addMsg("网络请求结束");
        }

        @Override
        public void onEnd() {
            Log.d(TAG, "onStart: 请求结束");
            addMsg("请求结束");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        init();
    }

    private void init() {
        NetUtils.init(getApplication());
        CacheUtils.instance.clearAll();
        post_args.put("id", 4);
        post_args.put("name", "李四");
        post_args.put("msg", "这是post请求");
        binding.etUrl.setText(def_url1);
        binding.etCacheTime.setText(String.valueOf(cacheTime));
    }

    private void initView() {
        binding.tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        binding.btn1.setOnClickListener(this);
        binding.btn2.setOnClickListener(this);
        binding.rgCache.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.mrb1) {
                cacheType = NetCacheType.NO_CACHE;
            } else if (checkedId == R.id.mrb2) {
                cacheType = NetCacheType.ONLY_CACHE;
            } else {
                cacheType = NetCacheType.FIRST_CACHE;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn1) {
            get();
        } else {
            post();
        }
    }

    private String getUrl() {
        return binding.etUrl.getText() + "";
    }

    private void getCacheTime() {
        try {
            cacheTime = Long.parseLong(binding.etCacheTime.getText() + "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getCacheTime: " + cacheTime);
    }

    private NetRequest getRequest(boolean isGet) {
        getCacheTime();
        NetRequest request = new NetRequest();
        request.setKey(isGet ? "get" : "post");
        //添加参数
        if (isGet) {
            request.setUrl1(getUrl() + "/get" + get_args);
        } else {
            request.setUrl1(getUrl() + "/post");
            request.setArgs(post_args);
        }
        request.setUrl2(def_url2);
        request.setCacheType(cacheType);
        request.setCacheTime(cacheTime);
        return request;
    }

    private void get() {
        clearMsg();
        NetUtils.instance.get(getRequest(true), netListener);
    }

    private void post() {
        clearMsg();
        NetUtils.instance.post(getRequest(false), netListener);
    }

    private void clearMsg() {
        binding.tvContent.setText("");
        if (d != null && !d.isDisposed()) {
            d.dispose();
        }
    }

    private void addMsg(String msg) {
        String txt = binding.tvContent.getText() + "";
        txt = txt + "\n\n\n" + msg;
        binding.tvContent.setText(txt);
    }

}