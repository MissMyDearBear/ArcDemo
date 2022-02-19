// Write C++ code here.
//
#include "iostream"
#include "jni.h"
#include "android/log.h"
#include "string"
#include "cstring"

#define LOGI(...) \
  ((void)__android_log_print(ANDROID_LOG_ERROR, "native-log-bear-->", __VA_ARGS__))

using namespace std;

extern "C"
void Java_com_bear_arcdemo_ndk_NLog_nBearLog(JNIEnv *env, jclass s, jstring jStr) {
    const char *nativeString = env->GetStringUTFChars(jStr, nullptr);
    LOGI("%s", nativeString);
    //使用完后要主动释放
    env->ReleaseStringUTFChars(jStr, nativeString);
}
