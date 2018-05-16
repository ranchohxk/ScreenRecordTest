package com.supper.lupingdashi.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by hxk on 2018/3/20.
 */

public class AVContrl {
    private static String TAG = "AVContrl";
    private EventHandler mEventHandler;
    /*******************local-filed**********************************/
    private static final int MONTAGE_Video_SUCESS = 101;
    private static final int MONTAGE_Video_FAILED = 102;

    public void initAVContrl() {
        System.loadLibrary("native-lib");
        Looper looper;
        if ((looper = Looper.myLooper()) != null) {
            mEventHandler = new EventHandler(this, looper);
        } else if ((looper = Looper.getMainLooper()) != null) {
            mEventHandler = new EventHandler(this, looper);
        } else {
            mEventHandler = null;
        }
    }

    /*******************call-by-native**********************************/
    public void postEventFromNative(Object weakThiz, int what,
                                    int arg1, int arg2, Object obj) {
        if (weakThiz == null)
            return;
        //    AVContrl cu = (AVContrl) ((WeakReference) weakThiz).get();//后面在c层处理弱引用问题
        AVContrl cu = (AVContrl) weakThiz;
        if (cu == null) {
            return;
        }
        if (cu.mEventHandler == null) {
            return;
        }
        cu.mEventHandler.obtainMessage(what, arg1, arg2, obj);
        cu.mEventHandler.sendEmptyMessage(what);
    }

    /*******************event-handler**********************************/
    private static class EventHandler extends Handler {
        private final WeakReference<AVContrl> mWeakAVControl;

        public EventHandler(AVContrl cu, Looper looper) {
            super(looper);
            mWeakAVControl = new WeakReference<AVContrl>(cu);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MONTAGE_Video_SUCESS:
                    LOG.debug(TAG, "MONTAGE_Video_SUCESS");
                    break;
                case MONTAGE_Video_FAILED:
                    LOG.error(TAG, "MONTAGE_Video_FAILED");
                    break;
                default:
                    LOG.error(TAG, "Unknow message type" + msg.what);
                    break;
            }
        }
    }

    /*******************native-method**********************************/

    /**
     * 剪辑视频
     *
     * @param inputfilepath  输入文件路径
     * @param outputfilepath 输出文件路径
     * @param startTime      秒
     * @param endTime        秒
     */
    public native int montageVideo(String inputfilepath, String outputfilepath, int startTime, int endTime);
}
