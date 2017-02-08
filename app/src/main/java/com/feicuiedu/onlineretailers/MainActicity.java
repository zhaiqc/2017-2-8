package com.feicuiedu.onlineretailers;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.id.list;
import static com.feicuiedu.onlineretailers.R.id.image;


/**
 * Created by zqc on 2017/1/5.
 */

public class MainActicity extends AppCompatActivity implements IntnetOkhttp.intnetListener {
    private RequestQueue mQueue;
    private int autoCurrIndex = 0;
    private Timer timer = new Timer();
    private Context context;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ImageView[] imageViews = new ImageView[5];
    private Toolbar toolBar;
    private static final String TAG = "MainActicity";
    private List<String> SmallImages = new ArrayList<>();
    private List<String> salesImage = new ArrayList<>();
    private IntnetOkhttp myOKhttp;
    private ImageView Image;
    private LinearLayout llMain;
    private Message message = new Message();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11, img12, img13, img14, img15, img16, img17, img18, img19, img20, img21, img22, img23, img25, img24;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            List<String> list;
            if (msg.what == 1) {
                list = (List<String>) msg.obj;
                for (String url : list) {
                    notifyViewPager(url);
                    viewPagerAdapter.notifyDataSetChanged();
                }

            }
            if (msg.what == 2) {
                viewPager.setCurrentItem(msg.arg1, false);
            }
            if (msg.what == 3) {
                Log.d(TAG, "handleMessage: " + msg.obj);
                for (Object url : (List<String>) msg.obj) {
                    Log.d(TAG, "handleMessage: " + url);
                    dyncCreateImage(url.toString());
                    intnetVolly();
                }
            }
            if (msg.what == 4) {
                Log.d(TAG, "handleMessage:obj" + msg.obj);
                Log.d(TAG, "handleMessage:111 " + msg.obj.toString());
                intnetImage((JSONObject) msg.obj);
            }
            super.handleMessage(msg);
        }

    };

    //底部圆点选择
    private ViewPager.OnPageChangeListener voc = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            autoCurrIndex = position;
            switch (position) {
                case 0:
                    imageViews[0].setImageResource(R.drawable.adware_style_selected);
                    imageViews[1].setImageResource(R.drawable.adware_style_default);
                    imageViews[2].setImageResource(R.drawable.adware_style_default);
                    imageViews[3].setImageResource(R.drawable.adware_style_default);
                    imageViews[4].setImageResource(R.drawable.adware_style_default);
                    break;
                case 1:
                    imageViews[1].setImageResource(R.drawable.adware_style_selected);
                    imageViews[0].setImageResource(R.drawable.adware_style_default);
                    imageViews[2].setImageResource(R.drawable.adware_style_default);
                    imageViews[3].setImageResource(R.drawable.adware_style_default);
                    imageViews[4].setImageResource(R.drawable.adware_style_default);
                    break;
                case 2:
                    imageViews[2].setImageResource(R.drawable.adware_style_selected);
                    imageViews[0].setImageResource(R.drawable.adware_style_default);
                    imageViews[1].setImageResource(R.drawable.adware_style_default);
                    imageViews[3].setImageResource(R.drawable.adware_style_default);
                    imageViews[4].setImageResource(R.drawable.adware_style_default);
                    break;
                case 3:
                    imageViews[3].setImageResource(R.drawable.adware_style_selected);
                    imageViews[1].setImageResource(R.drawable.adware_style_default);
                    imageViews[2].setImageResource(R.drawable.adware_style_default);
                    imageViews[0].setImageResource(R.drawable.adware_style_default);
                    imageViews[4].setImageResource(R.drawable.adware_style_default);
                    break;
                case 4:
                    imageViews[4].setImageResource(R.drawable.adware_style_selected);
                    imageViews[3].setImageResource(R.drawable.adware_style_default);
                    imageViews[2].setImageResource(R.drawable.adware_style_default);
                    imageViews[1].setImageResource(R.drawable.adware_style_default);
                    imageViews[0].setImageResource(R.drawable.adware_style_default);
                    break;
            }

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    };
    //下拉刷新
    private SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            myOKhttp.intnetOkhttp();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        context = this;
        inntToolBar();
        myOKhttp = new IntnetOkhttp(this, context);
        myOKhttp.intnetOkhttp();
        inntViewPager();
        SmallImages = getdata();
        startAutoScroll();
        initLeadIcon();
        inntIamge();
        inntswipeRefreshLayout();

    }

    //初始化ToolBar
    private void inntToolBar() {
        toolBar = (Toolbar) findViewById(R.id.tool);
        toolBar.setTitle("");
        setSupportActionBar(toolBar);
    }

    //初始化图片列表
    private void inntIamge() {
        img1 = (ImageView) findViewById(R.id.iv_list_1);
        img2 = (ImageView) findViewById(R.id.iv_list_2);
        img3 = (ImageView) findViewById(R.id.iv_list_3);
        img4 = (ImageView) findViewById(R.id.iv_list_4);
        img5 = (ImageView) findViewById(R.id.iv_list_5);
        img6 = (ImageView) findViewById(R.id.iv_list_6);
        img7 = (ImageView) findViewById(R.id.iv_list_7);
        img8 = (ImageView) findViewById(R.id.iv_list_8);
        img9 = (ImageView) findViewById(R.id.iv_list_9);
        img10 = (ImageView) findViewById(R.id.iv_list_10);
        img11 = (ImageView) findViewById(R.id.iv_list_11);
        img12 = (ImageView) findViewById(R.id.iv_list_12);
        img13 = (ImageView) findViewById(R.id.iv_list_13);
        img14 = (ImageView) findViewById(R.id.iv_list_14);
        img15 = (ImageView) findViewById(R.id.iv_list_15);
        img16 = (ImageView) findViewById(R.id.iv_list_16);
        img17 = (ImageView) findViewById(R.id.iv_list_17);
        img18 = (ImageView) findViewById(R.id.iv_list_18);
        img19 = (ImageView) findViewById(R.id.iv_list_19);
        img20 = (ImageView) findViewById(R.id.iv_list_20);
        img21 = (ImageView) findViewById(R.id.iv_list_21);
        img22 = (ImageView) findViewById(R.id.iv_list_22);
        img23 = (ImageView) findViewById(R.id.iv_list_23);
        img24 = (ImageView) findViewById(R.id.iv_list_24);
        img25 = (ImageView) findViewById(R.id.iv_list_25);
    }

    //初始化ViewPager
    private void inntViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(voc);

    }

    private List<String> getdata() {
        List<MyJson.DataBean.PlayerBean> myList = new ArrayList<>();
        List<String> listImages = new ArrayList<>();
        for (MyJson.DataBean.PlayerBean myJson : myList) {
            String imgUrl = myJson.getPhoto().getSmall();
            listImages.add(imgUrl);
        }
        return listImages;
    }

    //初始化圆点图标
    private void initLeadIcon() {
        imageViews[0] = (ImageView) findViewById(R.id.img_1);
        imageViews[1] = (ImageView) findViewById(R.id.img_2);
        imageViews[2] = (ImageView) findViewById(R.id.img_3);
        imageViews[3] = (ImageView) findViewById(R.id.img_4);
        imageViews[4] = (ImageView) findViewById(R.id.img_5);
        imageViews[0].setImageResource(R.drawable.adware_style_selected);
    }

    //重写intnetOkhttp
    @Override
    public void intnetOkhttp(List<MyJson.DataBean.PlayerBean> list) {
        message = handler.obtainMessage();
        message.what = 3;
        message.obj = list;
        handler.sendMessage(message);
        message = handler.obtainMessage();
        for (MyJson.DataBean.PlayerBean myJson : list) {
            SmallImages.add(myJson.getPhoto().getUrl());
        }
        message.obj = SmallImages;
        message.what = 1;
        handler.sendMessage(message);

    }

    //网络加载ViewPager图片
    private void notifyViewPager(String imgUrl) {
        View view = getLayoutInflater().inflate(R.layout.view_list, null);
        Image = (ImageView) view.findViewById(image);
        Picasso.with(context)
                .load(imgUrl)
                .resize(1200, 500)
                .centerCrop()
                .into(Image);
        viewPagerAdapter.addViewToAdapter(view);
        viewPagerAdapter.notifyDataSetChanged();
    }

    // 设置自动轮播图片，2s后执行，周期是2s
    private void startAutoScroll() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (autoCurrIndex == SmallImages.size() - 1) {
                    autoCurrIndex = -1;
                }
                autoCurrIndex++;
                Message message = new Message();
                message.what = 2;
                message.arg1 = autoCurrIndex;
                handler.sendMessage(message);
            }

        }, 2000, 2000);
    }

    //动态生成促销单品图片
    private void dyncCreateImage(String imgUrl) {
        ImageView img = new ImageView(this);
        llMain = (LinearLayout) findViewById(R.id.ll_main);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        Picasso.with(this).load(imgUrl).resize(265, 265).transform(new CircleTransform()).into(img);
        img.setLayoutParams(lp);
        llMain.addView(img);

    }

    //重写intnetOkhttp
    @Override
    public void intnetOkhttp(String body) {
        Log.d(TAG, "intnetOkhttp:body" + body);
        Gson gson = new Gson();
        MyJson myJson = gson.fromJson(body, MyJson.class);
        List<MyJson.DataBean.PromoteGoodsBean> Promote_goods = myJson.getData().getPromote_goods();
        for (MyJson.DataBean.PromoteGoodsBean list : Promote_goods) {
            Log.d(TAG, "intnetOkhttp: " + list.getImg().getSmall());
            salesImage.add(list.getImg().getThumb());
        }


        message = handler.obtainMessage();
        message.what = 3;
        message.obj = salesImage;
        handler.sendMessage(message);

    }

    //Volly访问网络
    private void intnetVolly() {
        mQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://106.14.32.204/eshop/emobile/?url=/home/category", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        message = handler.obtainMessage();
                        message.what = 4;
                        message.obj = response;
                        handler.sendMessage(message);
                        Log.d(TAG, "onResponse:123 " + message);
                        Log.d(TAG, "onResponse: " + response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue.add(jsonObjectRequest);
    }

    //从网络下载图片与imageview绑定
    private void intnetImage(JSONObject response) {
        List<String> imgUrlList = new ArrayList<>();
        Gson gson = new Gson();
        SecondJson secondJson = gson.fromJson(response.toString(), SecondJson.class);
        List<SecondJson.DataBean> listData = secondJson.getData();
        for (SecondJson.DataBean dataBen : listData) {
            for (SecondJson.DataBean.GoodsBean goodBen : dataBen.getGoods()) {
                Log.d(TAG, "intnetImage111: " + goodBen.getImg().getUrl());
                imgUrlList.add(goodBen.getImg().getThumb());

            }
        }
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        String imgUrl1;
        ImageLoader.ImageListener listener;
        imgUrl1 = imgUrlList.get(0);
        listener = ImageLoader.getImageListener(img1,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(1);
        listener = ImageLoader.getImageListener(img2,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(2);
        listener = ImageLoader.getImageListener(img3,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);


        imgUrl1 = imgUrlList.get(3);
        listener = ImageLoader.getImageListener(img4,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(4);
        listener = ImageLoader.getImageListener(img5,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(5);
        listener = ImageLoader.getImageListener(img6,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener, 250, 250);

        imgUrl1 = imgUrlList.get(6);
        listener = ImageLoader.getImageListener(img7,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(7);
        listener = ImageLoader.getImageListener(img8,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(8);
        listener = ImageLoader.getImageListener(img9,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(9);
        listener = ImageLoader.getImageListener(img10,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(10);
        listener = ImageLoader.getImageListener(img11,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(11);
        listener = ImageLoader.getImageListener(img12,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(12);
        listener = ImageLoader.getImageListener(img13,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(13);
        listener = ImageLoader.getImageListener(img14,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(14);
        listener = ImageLoader.getImageListener(img15,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(15);
        listener = ImageLoader.getImageListener(img16,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(16);
        listener = ImageLoader.getImageListener(img17,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(17);
        listener = ImageLoader.getImageListener(img18,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(18);
        listener = ImageLoader.getImageListener(img19,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(19);
        listener = ImageLoader.getImageListener(img20,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(20);
        listener = ImageLoader.getImageListener(img21,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(21);
        listener = ImageLoader.getImageListener(img22,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(22);
        listener = ImageLoader.getImageListener(img23,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(23);
        listener = ImageLoader.getImageListener(img24,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);

        imgUrl1 = imgUrlList.get(24);
        listener = ImageLoader.getImageListener(img25,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imgUrl1, listener);
//        xRefreshView.setLoadComplete(true);
//        xRefreshView.stopLoadMore();
        swipeRefreshLayout.setRefreshing(false);


    }

    //初始化swipeRefreshLayout
    private void inntswipeRefreshLayout() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl);
        swipeRefreshLayout.setOnRefreshListener(listener);
        // 这句话是为了，第一次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
//        MyHeader myHeader = new MyHeader(this);
//        xRefreshView = (XRefreshView) findViewById(R.id.xrv);
//        xRefreshView.setXRefreshViewListener(listener);

//        xRefreshView.setCustomHeaderView(myHeader);
    }
}


