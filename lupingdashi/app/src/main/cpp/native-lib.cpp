/**************************************************************************
 * 作    者：	hxk
 * 版    本：	1.0
 * 生成日期：
 * 功能描述：
 * 其    他:
 * 历    史:
 **************************************************************************/
#include <jni.h>
#include "include/Logger.h"

extern "C"
{
//封装格式
#include "include/libavformat/avformat.h"
//解码
#include "include/libavcodec/avcodec.h"
//缩放
#include "include/libswscale/swscale.h"
//重采样
#include "include/libswresample/swresample.h"
//
#include "include/libavutil/pixfmt.h"

}

#include "FileCut.h"
#include "Const.h"

extern "C" {
static void
post_event_base(JNIEnv *env, jobject weak_this, int what, int arg1, int arg2, jobject obt) {
    jclass clazz = env->FindClass("com/supper/lupingdashi/utils/AVContrl");
    if (clazz == NULL) {
        LOGE("find class AVContrl.class error!");
        return;
    }

    jmethodID id = env->GetMethodID(clazz, "postEventFromNative",
                                    "(Ljava/lang/Object;IIILjava/lang/Object;)V");
    if (id == NULL) {
        LOGE("find method methodCalledByJni error!");
        return;
    }
    jobject obj = weak_this;
    env->CallVoidMethod(obj, id, weak_this, what, arg1, arg2, obt);
    return;
}
//post_event(env, thiz, 101, 1, 1);
static void post_event(JNIEnv *env, jobject weak_this, int what, int arg1, int arg2) {
    LOGD("post_event.what:%d,arg1:%d,arg2:%d\n", what, arg1, arg2);
    post_event_base(env, weak_this, what, arg1, arg2, NULL);
    return;
}
static void post_event2(JNIEnv *env, jobject weak_this, int what, int arg1, int arg2, jobject obt) {
    LOGD("post_event2.what:%d,arg1:%d,arg2:%d\n", what, arg1, arg2);
    post_event_base(env, weak_this, what, arg1, arg2, obt);
    return;
}

static void initFFmpeg() {
    av_register_all();
    avformat_network_init();

}

FileCut *mFileCut;
/**
 * 视频剪辑函数
 * @param env
 * @param thiz
 * @param inputPath
 * @param outPath
 * @param startTime
 * @param endTime
 * @return
 */
JNIEXPORT int JNICALL Java_com_supper_lupingdashi_utils_AVContrl_montageVideo(
        JNIEnv *env, jobject thiz, jstring inputPath, jstring outPath, jint startTime,
        jint endTime) {
    if (startTime < 0 || endTime < 0) {
        LOGE("argument error!\n");
        return -1;
    }
    mFileCut = new FileCut();
    if (mFileCut == NULL) {
        LOGE("mFileCut is NULL!\n");
        post_event(env, thiz, MONTAGE_Video_FAILED, 1, 1);
    }
    int ret = mFileCut->cutFile((char *) inputPath, (char *) outPath, startTime, endTime);
    if (ret > 0) {
        LOGD("montageVideo sucess!\n");
        post_event(env, thiz, MONTAGE_Video_SUCESS, 1, 1);

    } else {
        LOGD("montageVideo falied!\n");
        post_event(env, thiz, MONTAGE_Video_FAILED, 1, 1);
    }
    return 0;
}
}


