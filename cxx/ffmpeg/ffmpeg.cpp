#include <jni.h>

extern "C"
{
#include "libavcodec/jni.h"
#include "libavformat/avformat.h"

  JNIEXPORT
  jint JNI_OnLoad(JavaVM *vm, void *res)
  {
    av_jni_set_java_vm(vm, 0);
    return JNI_VERSION_1_4;
  }

  JNIEXPORT jlong JNICALL Java_Ffmpeg_avformatAllocContext(
      JNIEnv *env,
      jobject thiz)
  {
    return (jlong)avformat_alloc_context();
  }
}