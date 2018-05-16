/**************************************************************************
 * 作    者：	hxk
 * 版    本：	1.0
 * 生成日期：
 * 功能描述： 底层与上层消息机制
 * 其    他:
 * 历    史:
 **************************************************************************/

#ifndef LUPINGDASHI_CONST_H
#define LUPINGDASHI_CONST_H

#include <jni.h>

/*****************************notification filed****************************************/
//视频剪辑消息
enum video_cut_info_type {
    MONTAGE_Video_SUCESS = 101,
    MONTAGE_Video_FAILED = 102,
};


/*****************************notification method****************************************/
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

/**
 * just use like post_event(env, thiz, 101, 1, 1);
 */

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

#endif //LUPINGDASHI_CONST_H
