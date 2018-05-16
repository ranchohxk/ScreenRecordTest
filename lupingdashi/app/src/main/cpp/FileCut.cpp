/**************************************************************************
 * 作    者：	hxk
 * 版    本：	1.0
 * 生成日期：
 * 功能描述：视频文件剪辑
 * 其    他: 代码实现，不用命令行实现 // ./ffmpeg -i test.mp4 -ss 00:00:50.0 -codec copy -t 20 output.mp4
 * 历    史:
 **************************************************************************/

#include "FileCut.h"

FileCut::FileCut() {

}

FileCut::~FileCut() {

}

int initFFmpeg() {
    av_register_all();
    avformat_network_init();
}

struct Cut cut;
MessageQueue msg_queue;

int FileCut::initFileCut(char *inputPath, char *outputPath, int startTime, int endTime) {
    msg_queue_init(&msg_queue);
    cut.inputPath = inputPath;
    cut.outputPath = outputPath;
    cut.startTime = startTime;
    cut.endTime = endTime;
    cut.msg_queue = msg_queue;
    return 0;
}

int FileCut::cutFile(char *inputPath, char *outputPath, int startTime, int endTime) {
    //initFileCut(inputPath, outputPath, startTime, endTime);
    int videdoIndex = -1;
    int audioIndex = -1;
    initFFmpeg();
    AVFormatContext *pFormatCtx;
    pFormatCtx = avformat_alloc_context();
    if (avformat_open_input(&pFormatCtx, (const char *) inputPath, NULL, NULL) != 0) {
        LOGE("Couldn't open input stream.\n");
        return -1;
    }
    if (avformat_find_stream_info(pFormatCtx, NULL) < 0) {
        LOGE("Couldn't find stream information.\n");
        return -1;
    }
    for (int i = 0; i < pFormatCtx->nb_streams; i++) {
        if (pFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_AUDIO) {
            audioIndex = i;

        } else if (pFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_VIDEO) {
            videdoIndex = i;
        }
        if (videdoIndex == -1 && audioIndex == -1) {
            LOGE(" Didn't find a video and audio stream.\n");
            goto Error;
        }
    }
    //  msg_queue_put_simple1(&cut.msg_queue, CUTFILE_MSG_SUCESS);
    return 1;
    Error:
    //  msg_queue_put_simple1(&cut.msg_queue, CUTFILE_MSG_ERROR);
    //close
    return -1;
}