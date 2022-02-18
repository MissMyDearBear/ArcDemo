package com.bear.arcdemo.ndk;

public class NLog {
    static {
        Runtime.getRuntime().loadLibrary("native-log");
    }

    public native static void nBearLog(String str);
}
