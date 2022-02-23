package com.bear.arcdemo.asm;

/**
 * javac Sample.java   // 生成Sample.class，也就是Java字节码
 * javap -v Sample     // 查看Sample类的Java字节码
 *
 * //通过Java字节码，生成Dalvik字节码
 * dx --dex --output=Sample.dex Sample.class
 *
 * dexdump -d Sample.dex   // 查看Sample.dex的Dalvik的字节码
 */
public class Sample {
    public void test(){
        System.out.print("I'm a test sample!");
    }
}
