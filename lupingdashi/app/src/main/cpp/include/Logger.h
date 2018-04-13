/**************************************************************************
 * 作    者：	hxk
 * 版    本：	1.0
 * 生成日期：	2017-9-15
 * 功能描述：     jni 中 log打印
 *               LOGD("mBlockAlign: %u\n ",mBlockAlign);
 *               LOGE("%s", "获取视频信息失败");
 * 其    他:
 * 历    史:
 **************************************************************************/
#ifndef LOCALFFMPEGPLAYER_LOGGER_H
#define LOCALFFMPEGPLAYER_LOGGER_H


#ifdef ANDROID

#include<android/log.h>

#define TAG "LPDS"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__)
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__)

#elif defined(WIN32)
#include "stdio.h"

#define LOGD printf
#define LOGE printf

#endif


#endif //LOCALFFMPEGPLAYER_LOGGER_H
