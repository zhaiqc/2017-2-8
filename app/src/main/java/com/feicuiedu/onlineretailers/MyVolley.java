package com.feicuiedu.onlineretailers;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by zqc on 2017/1/10.
 */

public class MyVolley extends Volley {
    private static final String TAG = "MyVolley";
    private String url = "http://106.14.32.204/eshop/emobile/?url=/home/category";
    private Context context;
    public MyVolley(Context context) {
        this.context = context;
    }

    RequestQueue mQueue = Volley.newRequestQueue(context);
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "onResponse: "+response.toString());
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("TAG", error.getMessage(), error);
        }

    });

    public void intnet(){
        mQueue.add(jsonObjectRequest);
    }
}
