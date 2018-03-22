package com.supper.lupingdashi.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.supper.lupingdashi.R;
import com.supper.lupingdashi.service.VideoRecordService;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.Screen;

/**
 * Created by Administrator on 2018/3/20.
 */

public class RecoredApplication extends Application {

    private static RecoredApplication application;
    private ImageView mImageView;

    private void setImageView(ImageView m) {
        this.mImageView = m;

    }

    public ImageView getImageView() {
        return mImageView;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        application = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, VideoRecordService.class));
        initFloatWindow();
    }

    public void initFloatWindow() {
        mImageView = new ImageView(getApplicationContext());
        mImageView.setImageResource(R.drawable.floatview_icon_close);
        FloatWindow
                .with(getApplicationContext())
                .setView(mImageView)
                .setWidth(50)                   //100px
                .setHeight(Screen.width, 0.1f)    //屏幕宽度的 20%
                .setX(50)                       //100px
                .setY(Screen.height, 0.1f)        //屏幕高度的 30%
                // .setMoveStyle(500, new AccelerateInterpolator())  //贴边动画时长为500ms，加速插值器
                .setDesktopShow(true)                //默认 false
                //  .setMoveType(MoveType.slide)         //可拖动，释放后自动贴边
                .build();

    }

    public static RecoredApplication getInstance() {
        return application;
    }
}