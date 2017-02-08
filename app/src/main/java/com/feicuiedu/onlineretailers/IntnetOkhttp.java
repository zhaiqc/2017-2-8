package com.feicuiedu.onlineretailers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by zqc on 2017/1/6.
 */

public class IntnetOkhttp {
    private static final String TAG = "IntnetOkhttp";
    List<MyJson.DataBean.PlayerBean> list = new ArrayList<>();
    String body;
    MyJson myJson;
    private intnetListener mListener;
    private final String FirstUrl = "http://106.14.32.204/eshop/emobile/?url=/home/data";
   // private final String SecondUrl = "http://106.14.32.204/eshop/emobile/?url=/home/category";
    public IntnetOkhttp(intnetListener mListener, Context context) {
        this.mListener = mListener;
        this.context = context;
    }

    private Context context;
    private List<String> SmallImages = new ArrayList<>();

    public interface intnetListener {
        void intnetOkhttp(List<MyJson.DataBean.PlayerBean> list);

        void intnetOkhttp(String body);
    }

    public void intnetOkhttp() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(FirstUrl)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + "失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    body = responseBody.string();
                    Gson gson = new Gson();
                    myJson = gson.fromJson(body, MyJson.class);
                    list = myJson.getData().getPlayer();
                    mListener.intnetOkhttp(list);//发送数据
                    mListener.intnetOkhttp(body);//发送数据
                }
            }
        });

    }

}
