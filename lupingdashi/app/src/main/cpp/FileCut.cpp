/**
 *视频文件剪辑
 * Created by hxk on 2018/5/15.
 * use code not // ./ffmpeg -i test.mp4 -ss 00:00:50.0 -codec copy -t 20 output.mp4
*/

#include "FileCut.h"

FileCut::FileCut() {

}

FileCut::~FileCut() {

}

int initFFmpeg() {
    av_register_all();
    avformat_network_init();
}

int FileCut::cutFile(char *inputPath, char *outputPath, int startTime, int endTime) {
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
            return -1;
        }
    }
    return 1;
}