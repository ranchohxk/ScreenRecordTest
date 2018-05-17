package com.supper.lupingdashi.fragment;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.supper.lupingdashi.MainActivity;
import com.supper.lupingdashi.R;
import com.supper.lupingdashi.activity.RecoredApplication;
import com.supper.lupingdashi.service.VideoRecordService;
import com.supper.lupingdashi.utils.AVContrl;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.Screen;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.BIND_AUTO_CREATE;
import static android.content.Context.MEDIA_PROJECTION_SERVICE;


public class MainFragment extends Fragment {


    private String TAG = "MainFragment";
    private Button hengpBt, shupBt;
    private MediaProjectionManager mProjectionManager;
    private MediaProjection mMediaProjection;
    private static final int RECORD_REQUEST_CODE = 101;
    private VideoRecordService mRecordService;
    private boolean misBound = false;
    int mVideoFrameRate = 30;
    ImageView mImageView;


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            VideoRecordService.VideoRecordBinder binder = (VideoRecordService.VideoRecordBinder) service;
            mRecordService = binder.getRecordService();
            //  mRecordService.setConfig(metrics.widthPixels, metrics.heightPixels, metrics.densityDpi);
            mRecordService.setVideoSd(true);//true  标清   false  高清
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            if (mRecordService != null) {
                mRecordService = null;
            }
        }
    };

    /**
     * 模拟HOME键返回桌面的功能
     */
    private void simulateHome() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(intent);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        mProjectionManager = (MediaProjectionManager) getActivity().getSystemService(MEDIA_PROJECTION_SERVICE);
        hengpBt = getActivity().findViewById(R.id.hengping);
        shupBt = getActivity().findViewById(R.id.shuping);
        mImageView = RecoredApplication.getInstance().getImageView();
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRecordService == null) {
                    Intent intent = new Intent(getActivity(), VideoRecordService.class);
                    misBound = getActivity().bindService(intent, connection, BIND_AUTO_CREATE);
                    Intent captureIntent = mProjectionManager.createScreenCaptureIntent();
                    startActivityForResult(captureIntent, RECORD_REQUEST_CODE);
                    mImageView.setImageDrawable(getActivity().getDrawable(R.drawable.floatview_icon));
                    Log.d(TAG, "开始录屏");
                }
                if (mRecordService != null && mRecordService.isRunning()) {
                    Log.d(TAG, "关闭录屏");
                    mRecordService.stopRecord();
                    mImageView.setImageDrawable(getActivity().getDrawable(R.drawable.floatview_icon_close));
                    stopScreenRecording();
                    mRecordService = null;

                }
            }
        });
        hengpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "开始录屏!\n");
                //test 拉通native与上层消息机制;文件裁剪测试
//                AVContrl av = new AVContrl();
//                av.initAVContrl();
//                av.montageVideo("/mnt/sdcard/test.flv", "/mnt/sdcard/out.flv", 1, 1);
                Intent intent = new Intent(getActivity(), VideoRecordService.class);
                misBound = getActivity().bindService(intent, connection, BIND_AUTO_CREATE);
                Intent captureIntent = mProjectionManager.createScreenCaptureIntent();
                startActivityForResult(captureIntent, RECORD_REQUEST_CODE);
            }
        });
        shupBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecordService != null) {
                    mRecordService.stopRecord();
                    Log.d(TAG, "shup onClick!\n");
                }

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RECORD_REQUEST_CODE && resultCode == RESULT_OK) {
            mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
            mRecordService.setMediaProject(mMediaProjection);
            simulateHome();//退出当前界面，模拟home
            mRecordService.startRecord();//开始录屏
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    /**
     * 关闭屏幕录制，即停止录制Service
     */
    private void stopScreenRecording() {

        if (getActivity() != null) {
            if (misBound && connection != null) {
                getActivity().unbindService(connection);
            }
        }
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestory!\n");
        super.onDestroy();
        stopScreenRecording();
        FloatWindow.destroy();
    }


}
