#include <jni.h>
#include "quickjs/quickjs.h"

extern "C" JNIEXPORT jlong JNICALL Java_Quickjs_jsNewRuntime(
    JNIEnv *env)
{
  return (jlong)JS_NewRuntime();
}