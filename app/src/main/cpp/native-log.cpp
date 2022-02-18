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
//    const jclass stringClass = env->GetObjectClass(jStr);
//    const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
//    const jbyteArray stringJBytes = (jbyteArray) env->CallObjectMethod(jStr, getBytes,
//                                                                       env->NewStringUTF("UTF-8"));
//    size_t length = (size_t) env->GetArrayLength(stringJBytes);
//    jbyte *pBytes = env->GetByteArrayElements(stringJBytes, NULL);
//
//    string ret = string((char *) pBytes, length);
//    env->ReleaseByteArrayElements(stringJBytes, pBytes, JNI_ABORT);
//    int n = ret.length();
//    char char_array[n + 1];
//    strcpy(char_array, ret.c_str());
//    LOGI("%s", char_array);

    const char *nativeString = env->GetStringUTFChars(jStr, NULL);
    env->ReleaseStringUTFChars(jStr, nativeString);
    LOGI("%s", nativeString);
    nativeString = NULL;
}
