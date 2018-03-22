package com.supper.lupingdashi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.hjm.bottomtabbar.BottomTabBar;
import com.supper.lupingdashi.activity.FloatWindowListener;
import com.supper.lupingdashi.fragment.FindFragment;
import com.supper.lupingdashi.fragment.MainFragment;
import com.supper.lupingdashi.fragment.OtherFragment;
import com.supper.lupingdashi.fragment.VideoFragment;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.Screen;

/**

 */
public class MainActivity extends AppCompatActivity implements VideoFragment.OnFragmentInteractionListener, FindFragment.OnFragmentInteractionListener, OtherFragment.OnFragmentInteractionListener {
    private static final int STORAGE_REQUEST_CODE = 102;
    private static final int AUDIO_REQUEST_CODE = 11;


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public BottomTabBar mBottomTabBar;
    private String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  initWindowBar();
        initToolBar();
        permissionCheck();

    }



    public void permissionCheck() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_REQUEST_CODE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CAMERA}, 1);
        }


    }

    public void initWindowBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
    }

    //bottomtabbar
    //https://link.jianshu.com/?t=https://github.com/hujinmeng/MyApplication
    public void initToolBar() {
        mBottomTabBar = (BottomTabBar) findViewById(R.id.bottom_tab_bar);
        mBottomTabBar.init(getSupportFragmentManager())
                .setImgSize(90, 90)
                .setFontSize(12)
                .setTabPadding(4, 6, 10)
                .setChangeColor(Color.BLACK, Color.BLUE)
                .addTabItem(getResources().getString(R.string.main_page), R.mipmap.main_screenrecord_blue, R.mipmap.main_screenrecord_gray, MainFragment.class)
                .addTabItem(getResources().getString(R.string.video_page), R.mipmap.main_videomanager_blue, R.mipmap.main_videomanager_gray, VideoFragment.class)
                .addTabItem(getResources().getString(R.string.find_page), R.mipmap.main_discover_blue, R.mipmap.main_discover_gray, FindFragment.class)
                //   .addTabItem(getResources().getString(R.string.other_page), R.mipmap.ic_launcher, R.mipmap.ic_logo, OtherFragment.class)
                //  .setTabBarBackgroundResource(R.mipmap.ic_launcher)
                .isShowDivider(false)
                .setCurrentTab(0)
                .setOnTabChangeListener(new BottomTabBar.OnTabChangeListener() {
                    @Override
                    public void onTabChange(int position, String name, View view) {

                        switch (position) {
                            case 0:
                                Log.d(TAG, "mainpage!\n");

                                break;
                            case 1:
                                Log.d(TAG, "videopage!\n");

                                break;
                            case 2:
                                Log.d(TAG, "findpage!\n");
                                break;
                            case 3:
                                Log.d(TAG, "otherpage!\n");
                                break;
                            default:
                                break;
                        }
                    }
                });
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
