### What is "J2SE for Android"

1. Android is based on Java, NOT J2SE, so there is no Swing and AWT classes in Android.
2. "J2SE for Android" provides Swing and AWT classes, so you can run J2SE application on Android with this library.
3. "J2SE for Android" implements most functions, NOT all.

***
### License

1. before using files in src directory, please read and agree the license file "license.txt".

***
### How to use

1. this library is based on library of HomeCenter, please download it from https://github.com/javalovercn/homecenter
2. this is NOT an Android Project, and there is no Activity in it, you should create new Android project and include this library.
3. an instance of Activity and res are required by this library, please invoke "hc.android.J2SEInitor.init(Object[] paras)" in construction and pass them in. For more detail, see method "init".
4. the files in res directory should be copied to your res directory of Android project.

***
### Important

1. you can't dex if there is class in package "java" or "javax" without option "--core-library".
2. the file "dx.jar" is removed the check code for "--core-library", so it is not required to set option "--core-library" in Eclipse to debug your project.
3. copy a backup of original dx.jar of your ADT, replace [adt-home]/sdk/build-tools/android-X.X/lib/dx.jar with this dx.jar.
4. the file "dx.jar" is belong to Android Open Source Project, and is licensed under the Apache License, Version 2.0.
