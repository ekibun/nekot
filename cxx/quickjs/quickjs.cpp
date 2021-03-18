#include <jni.h>
#include "quickjs/quickjs.h"

extern "C"
{
  #include "quickjs/libregexp.h"

  JNIEXPORT jboolean JNICALL Java_QuickjsJni_jsIsIdentNext(
      JNIEnv *env,
      jobject thiz,
      jint c)
  {
    return (jboolean)lre_js_is_ident_next(c);
  }

  JNIEXPORT jboolean JNICALL Java_QuickjsJni_jsIsIdentFirst(
      JNIEnv *env,
      jobject thiz,
      jint c)
  {
    return (jboolean)lre_js_is_ident_first(c);
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
