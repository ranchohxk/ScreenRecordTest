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
#include "FileCut.h"
#include "ConMsg.h"
#include <pthread.h>

extern "C" {

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
    return ret;
}
}


