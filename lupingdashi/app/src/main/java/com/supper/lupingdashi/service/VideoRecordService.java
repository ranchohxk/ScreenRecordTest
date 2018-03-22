package com.supper.lupingdashi.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.os.Binder;
import android.os.Environment;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VideoRecordService extends Service {
    private final String TAG = "VideoRecordService";
    private MediaProjection mediaProjection;
    private MediaRecorder mediaRecorder;
    private VirtualDisplay virtualDisplay;

    private boolean mRnning;
    private int mWidth = 720;
    private int mHeight = 1280;
    //private int width = 1280;
//    private int height = 720;
    private int mDpi = 1;
    private int mFrameRate = 30;
    private boolean mVideoSd;//true  30fps标清  false 高清 60fps

    public VideoRecordService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind!");
        return new VideoRecordBinder();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand!");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate!\n");
        super.onCreate();
        HandlerThread serviceThread = new HandlerThread("service_thread",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        serviceThread.start();
        mRnning = false;
        mediaRecorder = new MediaRecorder();
    }

    public void onDestroy() {
        Log.d(TAG, "onDestory!");
        super.onDestroy();
        stopRecord();

    }

    public void setMediaProject(MediaProjection project) {
        mediaProjection = project;
    }

    public boolean isRunning() {
        return mRnning;
    }

    public void setConfig(int width, int height, int dpi, int framerate) {
        Log.d(TAG, "width:" + width + "height:" + height + "dpi:" + dpi);
        this.mWidth = width;
        this.mHeight = height;
        this.mDpi = dpi;
        this.mFrameRate = framerate;
    }

    public void setVideoSd(boolean videosd) {
        Log.d(TAG, "mVideoSd:" + videosd);
        this.mVideoSd = videosd;


    }

    public boolean stopRecord() {
        Log.d(TAG, "stopRecord!\n");
        if (!mRnning) {
            return false;
        }
        mRnning = false;
        if (mediaRecorder != null) {
            try {
                mediaRecorder.setOnErrorListener(null);
                mediaRecorder.setOnInfoListener(null);
                mediaRecorder.setPreviewDisplay(null);
                mediaRecorder.stop();
            } catch (IllegalStateException e) {
                Log.i("Exception", Log.getStackTraceString(e));
            } catch (RuntimeException e) {
                Log.i("Exception", Log.getStackTraceString(e));
            } catch (Exception e) {
                Log.i("Exception", Log.getStackTraceString(e));
            }
            mediaRecorder.reset();
        }
        if (virtualDisplay != null) {
            virtualDisplay.release();
            virtualDisplay = null;
        }
        if (mediaProjection != null) {
            mediaProjection.stop();
            mediaProjection = null;
        }
        return true;
    }

    private void createVirtualDisplay() {
        Log.d(TAG, "createVirtualDisplay!\n");
        virtualDisplay = mediaProjection.createVirtualDisplay("MainScreen", mWidth, mHeight, mDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder.getSurface(), null, null);
    }

    private String curTimeMate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date curDate = new Date(System.currentTimeMillis());
        String curTime = formatter.format(curDate).replace(" ", "");

        return curTime;
    }


    private void initRecorder() {
        Log.d(TAG, "initRecorder!\n");


        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        // mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(getsaveDirectory() + curTimeMate() + ".mp4");
        //  mediaRecorder.setOutputFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/lupingzhuanjia/" + curTimeMate() + ".mp4");
        mediaRecorder.setVideoSize(mWidth, mHeight);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024);
        if (mVideoSd) {
            mediaRecorder.setVideoFrameRate(30);
        } else {
            mediaRecorder.setVideoFrameRate(60);

        }
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getsaveDirectory() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "LPScreenRecord" + "/";
            File file = new File(rootDir);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return null;
                }
            }
            Toast.makeText(getApplicationContext(), rootDir, Toast.LENGTH_SHORT).show();
            return rootDir;
        } else {
            return null;
        }
    }

    public boolean startRecord() {
        if (mediaProjection == null || mRnning) {
            return false;
        }
        Log.d(TAG, "startRecord!\n");
        initRecorder();
        createVirtualDisplay();//mediaRecorder.prepare()必须在创建VirtualDisplay的实例之前调用，否则不能正常获取到 Surface的实例
        mediaRecorder.start();
        mRnning = true;
        return true;
    }


    public class VideoRecordBinder extends Binder {
        public VideoRecordService getRecordService() {
            return VideoRecordService.this;
        }
    }
}
