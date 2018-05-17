/**************************************************************************
 * 作    者：	hxk
 * 版    本：	1.0
 * 生成日期：
 * 功能描述：视频文件剪辑
 * 其    他: 代码实现，不用命令行实现 // ./ffmpeg -i test.mp4 -ss 00:00:50.0 -codec copy -t 20 output.mp4
 * 历    史:
 **************************************************************************/

#include "FileCut.h"
#include <memory>

FileCut::FileCut() {

}

FileCut::~FileCut() {

}

AVFormatContext *inputContext = NULL;
AVFormatContext *outputContext = NULL;
int64_t lastReadPacktTime;

int initFFmpeg() {
    av_register_all();
    int ret = avformat_network_init();
    av_log_set_level(AV_LOG_DEBUG);
    return ret;
}

int interrupt_cb(void *ctx) {
    int timeout = 3;
    if (av_gettime() - lastReadPacktTime > timeout * 1000 * 1000) {
        return -1;
    }
    return 0;
}

int OpenInput(char *inputUrl) {
    inputContext = avformat_alloc_context();
    lastReadPacktTime = av_gettime();
    inputContext->interrupt_callback.callback = interrupt_cb;
    int ret = avformat_open_input(&inputContext, inputUrl, nullptr, nullptr);
    if (ret < 0) {
        LOGE("Input file open input failed!\n");
        return ret;
    }
    ret = avformat_find_stream_info(inputContext, nullptr);
    if (ret < 0) {
        LOGE("Find input file stream inform failed\n");
    } else {
        LOGD("Open input file  success\n");
    }
    return ret;
}

int OpenOutput(char *outUrl, const char *format_name) {
    int ret = avformat_alloc_output_context2(&outputContext, nullptr, format_name, outUrl);
    if (ret < 0) {
        LOGE("open output context failed\n");
        goto Error;
    }
    ret = avio_open2(&outputContext->pb, outUrl, AVIO_FLAG_WRITE, nullptr, nullptr);
    if (ret < 0) {
        LOGE("open avio failed");
        goto Error;
    }

    for (int i = 0; i < inputContext->nb_streams; i++) {
        AVStream *stream = avformat_new_stream(outputContext,
                                               inputContext->streams[i]->codec->codec);
        ret = avcodec_copy_context(stream->codec, inputContext->streams[i]->codec);
        if (ret < 0) {
            LOGE("copy coddec context failed");
            goto Error;
        }
    }
    ret = avformat_write_header(outputContext, nullptr);
    if (ret < 0) {
        LOGE("format write header failed");
        goto Error;
    }
    LOGI(" Open output file success %s\n", outUrl);
    return ret;
    Error:
    if (outputContext) {
        for (int i = 0; i < outputContext->nb_streams; i++) {
            avcodec_close(outputContext->streams[i]->codec);
        }
        avformat_close_input(&outputContext);
    }
    return ret;
}

void CloseInput() {
    if (inputContext != nullptr) {
        avformat_close_input(&inputContext);
    }
}

void CloseOutput() {
    if (outputContext != nullptr) {
        for (int i = 0; i < outputContext->nb_streams; i++) {
            AVCodecContext *codecContext = outputContext->streams[i]->codec;
            avcodec_close(codecContext);
        }
        avformat_close_input(&outputContext);
    }
}

int WritePacket(AVPacket *packet) {
    auto inputStream = inputContext->streams[packet->stream_index];
    auto outputStream = outputContext->streams[packet->stream_index];
    return av_interleaved_write_frame(outputContext, packet);
}

AVPacket *ReadPacketFromSource() {
    AVPacket *packet = (AVPacket *) av_malloc(sizeof(AVPacket));
    av_init_packet(packet);
    lastReadPacktTime = av_gettime();
    int ret = av_read_frame(inputContext, packet);
    if (ret >= 0) {
        return packet;
    } else {
        return nullptr;
    }
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
    //第20S开始，去掉8S
    int startPacketNum = 500;
    int discardtPacketNum = 200;
    int packetCount = 0;
    int64_t lastPacketPts = AV_NOPTS_VALUE;
    int64_t lastPts = AV_NOPTS_VALUE;
    initFFmpeg();
    int ret = OpenInput(inputPath);
    if (ret >= 0) {
        LOGI("inputFile format name:%s", inputContext->iformat->name);
        //文件原本什么格式，这里剪裁后保持原格式
        ret = OpenOutput(outputPath, inputContext->iformat->name);
    }
    if (ret < 0) {
        LOGE("check file exist or not file read permission?\n");
        goto Error;
    }
    while (1) {
        auto packet = ReadPacketFromSource();
        if (packet != nullptr) {
            packetCount++;
            LOGD("packetCount:%d\n", packetCount);
            if (packetCount <= 800) {

                ret = WritePacket(packet);
            }
//            if (packetCount <= 200 || packetCount >= 700) {
//                if (packetCount >= 700) {
//                    if (packet->pts - lastPacketPts > 120) {
//                        lastPts = lastPacketPts;
//                    } else {
//                        auto diff = packet->pts - lastPacketPts;
//                        lastPts += diff;
//                    }
//                }
//                lastPacketPts = packet->pts;
//                if (lastPts != AV_NOPTS_VALUE) {
//                    packet->pts = packet->dts = lastPts;
//                }
//                ret = WritePacket(packet);
//            }
        } else {
            break;
        }
    }
    LOGI("cut_fiel sucess!\n");
    //  msg_queue_put_simple1(&cut.msg_queue, CUTFILE_MSG_SUCESS);
    return 1;
    Error:
    //  msg_queue_put_simple1(&cut.msg_queue, CUTFILE_MSG_ERROR);
    CloseInput();
    CloseOutput();
    return -1;
}