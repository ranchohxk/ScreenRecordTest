//
// Created by hxk on 2018/5/15.
//

#ifndef LUPINGDASHI_FILECUT_H
#define LUPINGDASHI_FILECUT_H
extern "C" {
#include <Logger.h>
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
};


class FileCut {
public:
    FileCut();

    ~FileCut();

    int cutFile(char *inputPath, char *outputPath, int startTime, int endTime);

};


#endif //LUPINGDASHI_FILECUT_H
