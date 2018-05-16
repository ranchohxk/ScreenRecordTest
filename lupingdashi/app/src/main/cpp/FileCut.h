//
// Created by hxk on 2018/5/15.
//

#ifndef LUPINGDASHI_FILECUT_H
#define LUPINGDASHI_FILECUT_H
extern "C" {
#include <Logger.h>
#include "ConMsg.h"
#include "msgqueue.h"
//封装格式
#include "include/libavformat/avformat.h"
//解码
#include "include/libavcodec/avcodec.h"
//缩放
#include "include/libswscale/swscale.h"
//重采样
#include "include/libswresample/swresample.h"
//格式
#include "include/libavutil/pixfmt.h"
//common use .h file
#include "include/libavutil/opt.h"
#include "include/libavutil/channel_layout.h"
#include "include/libavutil/common.h"
#include "include/libavutil/imgutils.h"
#include "include/libavutil/mathematics.h"
#include "include/libavutil/samplefmt.h"
#include "include/libavutil/time.h"
#include "include/libavutil/fifo.h"
#include "include/libavcodec/avcodec.h"
#include "include/libavformat/avformat.h"
#include "include/libavformat/avio.h"
#include "include/libavfilter/avfiltergraph.h"
#include "include/libavfilter/avfilter.h"
#include "include/libavfilter/buffersink.h"
#include "include/libavfilter/buffersrc.h"
#include "include/libswscale/swscale.h"
#include "include/libswresample/swresample.h"
};

typedef struct Cut {
    char *inputPath;
    char *outputPath;
    int startTime;
    int endTime;
    MessageQueue msg_queue;
};

class FileCut {
public:


    FileCut();

    ~FileCut();

    int initFileCut(char *inputPath, char *outputPath, int startTime, int endTime);

    int cutFile(char *inputPath, char *outputPath, int startTime, int endTime);

};


#endif //LUPINGDASHI_FILECUT_H
