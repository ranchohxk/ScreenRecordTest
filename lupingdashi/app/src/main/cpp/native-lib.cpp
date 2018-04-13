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

//
JNIEXPORT int JNICALL Java_com_supper_lupingdashi_utils_AVContrl_montageVideo(
        JNIEnv *env, jobject thiz, jstring inputPath, jstring outPath, jint startTime,
        jint endTime) {
    post_event(env, thiz, 1011, 1, 1);
    av_register_all();
    return 0;


}
}


