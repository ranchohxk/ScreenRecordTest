#include <jni.h>
#include <string>
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
extern "C"
JNIEXPORT jstring

JNICALL
Java_com_supper_lupingdashi_utils_CppUtils_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    av_register_all();
    return env->NewStringUTF(hello.c_str());


}


