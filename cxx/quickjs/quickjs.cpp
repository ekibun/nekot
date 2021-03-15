#include <jni.h>
#include "quickjs/quickjs.h"

extern "C"
{
  JNIEXPORT jlong JNICALL Java_QuickjsJni_getStringPtr(
      JNIEnv *env,
      jobject thiz,
      jstring input)
  {
    return (jlong)env->GetStringUTFChars(input, 0);
  }

  JNIEXPORT jlong JNICALL Java_QuickjsJni_jsParseInit(
      JNIEnv *env,
      jobject thiz,
      jlong *ctx,
      jlong input,
      jint input_len)
  {
    return (jlong)JS_ParseInit(
        (JSContext *)ctx,
        (const char *)input,
        input_len);
  }

  JNIEXPORT void JNICALL Java_QuickjsJni_jsParseEnd(
      JNIEnv *env,
      jobject thiz,
      jlong *ctx,
      jlong s)
  {
    JS_ParseEnd((JSContext *)ctx, (JSParseState *)s);
  }

  JNIEXPORT jint JNICALL Java_QuickjsJni_jsParseNextToken(
      JNIEnv *env,
      jobject thiz,
      jlong *ctx,
      jlong *s)
  {
    JSRuntime *rt = JS_GetRuntime((JSContext *)ctx);
    uint8_t *stack_top = JS_SetStackTop(rt, 0);
    jint ret = (jint)JS_ParseNextToken((JSParseState *)s);
    JS_SetStackTop(rt, stack_top);
    return ret;
  }

  JNIEXPORT jint JNICALL Java_QuickjsJni_jsParseTemplatePart(
      JNIEnv *env,
      jobject thiz,
      jlong *s)
  {
    return (jint)JS_ParseTemplatePart((JSParseState *)s);
  }

  JNIEXPORT jlong JNICALL Java_QuickjsJni_jsGetParseStateTokenPtr(
      JNIEnv *env,
      jobject thiz,
      jlong *s)
  {
    return (jlong)JS_GetParseStateTokenPtr((JSParseState *)s);
  }

  JNIEXPORT jlong JNICALL Java_QuickjsJni_jsGetParseStateBufPtr(
      JNIEnv *env,
      jobject thiz,
      jlong *s)
  {
    return (jlong)JS_GetParseStateBufPtr((JSParseState *)s);
  }

  JNIEXPORT void JNICALL Java_QuickjsJni_jsSetParseStateBufPtr(
      JNIEnv *env,
      jobject thiz,
      jlong *s,
      jlong ptr)
  {
    JS_SetParseStateBufPtr((JSParseState *)s, (const uint8_t *)ptr);
  }

  JNIEXPORT jlong JNICALL Java_QuickjsJni_jsNewRuntime(
      JNIEnv *env,
      jobject thiz)
  {
    return (jlong)JS_NewRuntime();
  }

  JNIEXPORT jlong JNICALL Java_QuickjsJni_jsNewContext(
      JNIEnv *env,
      jobject thiz,
      jlong rt)
  {
    return (jlong)JS_NewContext((JSRuntime *)rt);
  }

  JNIEXPORT jlong JNICALL Java_QuickjsJni_jsEval(
      JNIEnv *env,
      jobject thiz,
      jlong *ctx,
      jstring input,
      jstring filename,
      jint eval_flags)
  {
    JSRuntime *rt = JS_GetRuntime((JSContext *)ctx);
    uint8_t *stack_top = JS_SetStackTop(rt, 0);
    JSValue *ret = new JSValue(JS_Eval(
        (JSContext *)ctx,
        env->GetStringUTFChars(input, 0),
        env->GetStringLength(input),
        env->GetStringUTFChars(filename, 0),
        eval_flags));
    JS_SetStackTop(rt, stack_top);
    return (jlong)ret;
  }

  JNIEXPORT jstring JNICALL Java_QuickjsJni_jsToString(
      JNIEnv *env,
      jobject thiz,
      jlong ctx,
      jlong v)
  {
    auto str = JS_ToCString((JSContext *)ctx, *(JSValue *)v);
    auto jstr = env->NewStringUTF(str);
    JS_FreeCString((JSContext *)ctx, str);
    return jstr;
  }

  JNIEXPORT void JNICALL Java_QuickjsJni_jsFreeValue(
      JNIEnv *env,
      jobject thiz,
      jlong ctx,
      jlong v,
      jboolean free)
  {
    JS_FreeValue((JSContext *)ctx, *(JSValue *)v);
    if (free)
      delete (JSValue *)v;
  }

  JNIEXPORT void JNICALL Java_QuickjsJni_jsFreeContext(
      JNIEnv *env,
      jobject thiz,
      jlong ctx)
  {
    JS_FreeContext((JSContext *)ctx);
  }

  JNIEXPORT void JNICALL Java_QuickjsJni_jsFreeRuntime(
      JNIEnv *env,
      jobject thiz,
      jlong rt)
  {
    JS_FreeRuntime((JSRuntime *)rt);
  }
}
