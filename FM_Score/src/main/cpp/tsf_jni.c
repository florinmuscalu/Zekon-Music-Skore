// JNI bridge to TinySoundFont for tech.zekon.FM_Score.FM_Synth.
//
// Plays General MIDI instruments from a SoundFont. stb_vorbis is included BEFORE
// tsf.h so TSF compiles in its Ogg-Vorbis path and can read SF3 (compressed)
// SoundFonts. All entry points take an opaque handle (the tsf* as a jlong);
// thread-safety (render vs. note on/off) is handled by the Java caller.

#include <jni.h>
#include <stdlib.h>
#include <stdint.h>

// stb_vorbis first: defines STB_VORBIS_INCLUDE_STB_VORBIS_H, which TSF keys off
// to enable SF3 decoding. Drop file I/O — we only ever decode from memory.
#define STB_VORBIS_NO_STDIO
#define STB_VORBIS_NO_PUSHDATA_API
#include "stb_vorbis.c"

#define TSF_IMPLEMENTATION
#include "tsf.h"

#define TSF_HANDLE(h) ((tsf*) (intptr_t) (h))

JNIEXPORT jlong JNICALL
Java_tech_zekon_FM_1Score_FM_1Synth_nativeLoad(JNIEnv* env, jobject thiz,
                                               jbyteArray data, jint sampleRate) {
    jsize len = (*env)->GetArrayLength(env, data);
    jbyte* bytes = (*env)->GetByteArrayElements(env, data, NULL);
    if (bytes == NULL) return 0;

    // tsf_load_memory copies/decodes everything it needs, so the input array can
    // be released immediately afterwards.
    tsf* f = tsf_load_memory(bytes, (int) len);
    (*env)->ReleaseByteArrayElements(env, data, bytes, JNI_ABORT);
    if (f == NULL) return 0;

    tsf_set_output(f, TSF_MONO, (int) sampleRate, 0.0f);
    tsf_set_max_voices(f, 48);
    return (jlong) (intptr_t) f;
}

// Independent synth instance that shares the loaded (read-only) sample/preset data by
// refcount but has its own voice/channel state — used for offline export so it can render
// concurrently with live playback without locking. Free it with nativeFree.
JNIEXPORT jlong JNICALL
Java_tech_zekon_FM_1Score_FM_1Synth_nativeCopy(JNIEnv* env, jobject thiz,
                                               jlong handle, jint sampleRate) {
    tsf* f = TSF_HANDLE(handle);
    if (f == NULL) return 0;
    tsf* c = tsf_copy(f);
    if (c == NULL) return 0;
    tsf_set_output(c, TSF_MONO, (int) sampleRate, 0.0f);
    tsf_set_max_voices(c, 48);
    return (jlong) (intptr_t) c;
}

JNIEXPORT jint JNICALL
Java_tech_zekon_FM_1Score_FM_1Synth_nativeSetProgram(JNIEnv* env, jobject thiz,
                                                     jlong handle, jint channel, jint program) {
    tsf* f = TSF_HANDLE(handle);
    if (f == NULL) return 0;
    return tsf_channel_set_presetnumber(f, channel, program, 0);
}

JNIEXPORT void JNICALL
Java_tech_zekon_FM_1Score_FM_1Synth_nativeNoteOn(JNIEnv* env, jobject thiz,
                                                 jlong handle, jint channel, jint key, jfloat velocity) {
    tsf* f = TSF_HANDLE(handle);
    if (f != NULL) tsf_channel_note_on(f, channel, key, velocity);
}

JNIEXPORT void JNICALL
Java_tech_zekon_FM_1Score_FM_1Synth_nativeNoteOff(JNIEnv* env, jobject thiz,
                                                  jlong handle, jint channel, jint key) {
    tsf* f = TSF_HANDLE(handle);
    if (f != NULL) tsf_channel_note_off(f, channel, key);
}

JNIEXPORT void JNICALL
Java_tech_zekon_FM_1Score_FM_1Synth_nativeAllNotesOff(JNIEnv* env, jobject thiz, jlong handle) {
    tsf* f = TSF_HANDLE(handle);
    if (f != NULL) tsf_note_off_all(f);
}

JNIEXPORT void JNICALL
Java_tech_zekon_FM_1Score_FM_1Synth_nativeRender(JNIEnv* env, jobject thiz,
                                                 jlong handle, jshortArray out, jint frames) {
    tsf* f = TSF_HANDLE(handle);
    if (f == NULL) return;
    jshort* buf = (*env)->GetShortArrayElements(env, out, NULL);
    if (buf == NULL) return;
    tsf_render_short(f, buf, (int) frames, 0);
    (*env)->ReleaseShortArrayElements(env, out, buf, 0);
}

JNIEXPORT void JNICALL
Java_tech_zekon_FM_1Score_FM_1Synth_nativeReset(JNIEnv* env, jobject thiz, jlong handle) {
    tsf* f = TSF_HANDLE(handle);
    if (f != NULL) tsf_reset(f);
}

JNIEXPORT void JNICALL
Java_tech_zekon_FM_1Score_FM_1Synth_nativeFree(JNIEnv* env, jobject thiz, jlong handle) {
    tsf* f = TSF_HANDLE(handle);
    if (f != NULL) tsf_close(f);
}
