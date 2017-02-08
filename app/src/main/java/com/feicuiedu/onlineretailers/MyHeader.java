package com.feicuiedu.onlineretailers;

import android.content.Context;
import android.view.View;

import com.andview.refreshview.callback.*;

/**
 * Created by zqc on 2017/1/11.
 */

public class MyHeader extends View  implements com.andview.refreshview.callback.IHeaderCallBack {

    public MyHeader(Context context) {
        super(context);
    }

    @Override
    public void onStateNormal() {

    }

    @Override
    public void onStateReady() {

    }

    @Override
    public void onStateRefreshing() {

    }

    @Override
    public void onStateFinish(boolean success) {

    }

    @Override
    public void onHeaderMove(double headerMovePercent, int offsetY, int deltaY) {

    }

    @Override
    public void setRefreshTime(long lastRefreshTime) {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    @Override
    public int getHeaderHeight() {
        return 0;
    }
}
