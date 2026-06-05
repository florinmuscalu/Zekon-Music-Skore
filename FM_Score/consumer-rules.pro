# Keep the native synth class and its native methods un-renamed so the JNI symbols
# (Java_tech_zekon_FM_1Score_FM_1Synth_*) still resolve after R8 minification in release builds.
-keepclasseswithmembernames,includedescriptorclasses class tech.zekon.FM_Score.FM_Synth {
    native <methods>;
}
