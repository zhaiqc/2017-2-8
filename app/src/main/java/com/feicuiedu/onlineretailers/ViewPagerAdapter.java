package com.feicuiedu.onlineretailers;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zqc on 2017/1/6.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private List<View> imgList = new ArrayList();
    private Context context;
    public ViewPagerAdapter( ) {
    }
    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public boolean isViewFromObject(View img, Object object) {
        return img == object;
    }

    public void addViewToAdapter(View img) {
        imgList.add(img);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View img = imgList.get(position);
        container.removeView(img);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View img = imgList.get(position);
        container.addView(img);
        return img;
    }


}
