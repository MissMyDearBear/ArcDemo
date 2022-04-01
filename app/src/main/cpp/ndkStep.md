## Android JNI开发步骤

### 创建CPP步骤

- `src`文件夹下创建`cpp`目录
- `cpp`目录下创建`CMakeLists.txt`
- 新建`native-log.cpp`

```
配置CMake


# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.1)

# Declares and names the project.

project("arcdemo")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             native-log

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             native-log.cpp
        )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )
#
## Specifies libraries CMake should link to your target library. You
## can link multiple libraries, such as libraries you define in this
## build script, prebuilt third-party libraries, or system libraries.
#


target_link_libraries( # Specifies the target library.
                       native-log

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} ) 


```
- 引用native方法
``` 
///app module build.gradle

android{
....
defaultConfig{
externalNativeBuild {
            cmake {
                cppFlags ''
            }
        }
        ndk{
            moduleName "arcdemo"
            ldLibs "log"
        }
}
 
 externalNativeBuild {
        cmake {
            path file('src/main/cpp/CMakeLists.txt')
            version '3.18.1'
        }
  }

}


///kt 中引用native方法
 static {
        Runtime.getRuntime().loadLibrary("native-log");
    }
    
    public native static void nBearLog(String str);
    
    
    ///对应native-log.cpp  
    Java_com_bear_arcdemo_ndk_NLog_nBearLog(JNIEnv *env, jclass s, jstring jStr)
```

