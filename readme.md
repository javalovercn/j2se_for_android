### What is "J2SE for Android"

1. Android is based on Java, but NOT J2SE, so there is no Swing and AWT classes in Android.
2. "J2SE for Android" provides Swing and AWT classes, so you can run J2SE application on Android with this library.
3. "J2SE for Android" is implements most functions, NOT all.

***
### License

1. before using files in src directory, please read and agree the license file "license.txt".

***
### How to use

1. this library is based on core library of HomeCenter, please download it from https://github.com/javalovercn/homecenter
2. this is NOT an Android Project, and there is no Activity in it, you should create new Android project and include this library.
3. a instance of Activity and res are required by this library, please invoke "hc.android.J2SEInitor.init(Object[] paras)" in construction and pass them in. For more detail, see method "init".
4. the files in res directory should be copied to your res directory of Android project.