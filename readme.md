### What is "J2SE for Android"

1. Android is based on Java, NOT J2SE, so there is no Swing and AWT classes in Android.
2. "J2SE for Android" provides Swing and AWT classes, and you can run J2SE application on Android with this library.
3. "J2SE for Android" implements most functions, NOT all.
4. this project applies successfully to [HomeCenter](https://github.com/javalovercn/hc_server_dist/).

***
### Snapshot
Tip : some functions are for J2SE only, for example, skin for UI and designer.

| Android | J2SE |
| :------: | :------: |
| ![Menu](http://homecenter.mobi/images/server_cap/menu_android.png) | ![Menu](http://homecenter.mobi/images/server_cap/menu_j2se.png) |
| ![Menu](http://homecenter.mobi/images/server_cap/pwd_android.png) | ![Menu](http://homecenter.mobi/images/server_cap/pwd_j2se.png) |
| ![Menu](http://homecenter.mobi/images/server_cap/proj_android.png) | ![Menu](http://homecenter.mobi/images/server_cap/proj_j2se.png) |
| ![Menu](http://homecenter.mobi/images/server_cap/opt_sec_android.png) | ![Menu](http://homecenter.mobi/images/server_cap/opt_sec_j2se.png) |
| ![Menu](http://homecenter.mobi/images/server_cap/opt_lib_android.png) | ![Menu](http://homecenter.mobi/images/server_cap/opt_lib_j2se.png) |
| ![Menu](http://homecenter.mobi/images/server_cap/opt_oth_android.png) | ![Menu](http://homecenter.mobi/images/server_cap/opt_oth_j2se.png) |
| ![Menu](http://homecenter.mobi/images/server_cap/opt_dev_android.png) | ![Menu](http://homecenter.mobi/images/server_cap/opt_dev_j2se.png) |

***
### License

1. before using files in src directory, please read and agree the license file "license.txt".
2. the file "dx.jar" is belong to Android Open Source Project, and is licensed under the Apache License, Version 2.0.

***
### How to use

1. this library is based on library of [HomeCenter](https://github.com/javalovercn/homecenter),
2. there is no Activity in it, you should create new Android project and include this library.
3. an instance of Activity and res are required by this library, please invoke "hc.android.J2SEInitor.init(Object[] paras)".
4. the files in res directory should be copied to your res directory of Android project.

***
### Important

1. you can't dex if there is a class in package "java" or "javax" without option "--core-library".
2. the "dx.jar" in current library is removed the checking codes for "--core-library", and it is not required to set option "--core-library" to debug.
3. backup the original dx.jar of your ADT, replace [adt-home]/sdk/build-tools/android-X.X/lib/dx.jar with this "dx.jar".

***
### Mirrors

1. https://github.com/javalovercn/j2se_for_android
2. https://gitee.com/mirrors/j2se-for-android
