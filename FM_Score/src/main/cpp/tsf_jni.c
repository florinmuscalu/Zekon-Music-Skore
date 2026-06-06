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

// Soft-knee limiter applied to the float mix before 16-bit conversion.
// Below the knee T it is the identity (bit-for-bit the same as before), so normal playing is
// unchanged; above T it saturates smoothly toward full scale and never exceeds it. This replaces
// TSF's hard clip: under the sustain pedal many voices ring at once and their sum runs past
// 0 dBFS, and that hard clip is audible as buzzy distortion. The knee is C1-continuous (slope 1
// at T), so there's no kink. Mono path only — the synth always renders TSF_MONO.
static inline float fm_soft_limit(float x) {
    const float T = 0.8f;
    float a = x < 0.0f ? -x : x;
    if (a <= T) return x;
    float sign = x < 0.0f ? -1.0f : 1.0f;
    float over = (a - T) / (1.0f - T);            // >= 0
    return sign * (T + (1.0f - T) * (over / (1.0f + over)));
}

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
    tsf_set_max_voices(f, 64);   // with sustain holding voices, the tsf.h patch steals the oldest past this
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
    tsf_set_max_voices(c, 64);
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

// Marks which MIDI notes (0..127) the GM `program` preset has a sample region for, so the caller
// can map them to keyboard keys and disable/grey the ones the instrument can't play.
JNIEXPORT jbooleanArray JNICALL
Java_tech_zekon_FM_1Score_FM_1Synth_nativeCoveredNotes(JNIEnv* env, jobject thiz,
                                                       jlong handle, jint program) {
    jboolean out[128];
    int i, r, note;
    tsf* f = TSF_HANDLE(handle);
    for (i = 0; i < 128; i++) out[i] = JNI_FALSE;
    if (f != NULL) {
        int pi = tsf_get_presetindex(f, 0, program);
        if (pi >= 0 && pi < f->presetNum) {
            struct tsf_preset* p = &f->presets[pi];
            for (r = 0; r < p->regionNum; r++) {
                for (note = p->regions[r].lokey; note <= p->regions[r].hikey; note++) {
                    if (note >= 0 && note < 128) out[note] = JNI_TRUE;
                }
            }
        }
    }
    jbooleanArray arr = (*env)->NewBooleanArray(env, 128);
    if (arr != NULL) (*env)->SetBooleanArrayRegion(env, arr, 0, 128, out);
    return arr;
}

JNIEXPORT void JNICALL
Java_tech_zekon_FM_1Score_FM_1Synth_nativeAllNotesOff(JNIEnv* env, jobject thiz, jlong handle) {
    tsf* f = TSF_HANDLE(handle);
    if (f != NULL) tsf_note_off_all(f);
}

// Sustain pedal: while on, key-offs are held; turning it off releases the held notes (TSF built-in).
JNIEXPORT void JNICALL
Java_tech_zekon_FM_1Score_FM_1Synth_nativeSetSustain(JNIEnv* env, jobject thiz, jlong handle, jint on) {
    tsf* f = TSF_HANDLE(handle);
    if (f != NULL) tsf_channel_set_sustain(f, 0, on);
}

JNIEXPORT jint JNICALL
Java_tech_zekon_FM_1Score_FM_1Synth_nativeActiveVoiceCount(JNIEnv* env, jobject thiz, jlong handle) {
    tsf* f = TSF_HANDLE(handle);
    return f == NULL ? 0 : tsf_active_voice_count(f);
}

JNIEXPORT void JNICALL
Java_tech_zekon_FM_1Score_FM_1Synth_nativeRender(JNIEnv* env, jobject thiz,
                                                 jlong handle, jshortArray out, jint frames) {
    tsf* f = TSF_HANDLE(handle);
    if (f == NULL) return;
    jshort* buf = (*env)->GetShortArrayElements(env, out, NULL);
    if (buf == NULL) return;

    // Render to float and soft-limit before converting to 16-bit, instead of tsf_render_short
    // (which hard-clips). Chunked through a small stack buffer so it stays reentrant — the live
    // render thread and the offline-export render run on different handles concurrently.
    float fbuf[1024];
    int total = (int) frames, done = 0;
    while (done < total) {
        int n = total - done;
        if (n > 1024) n = 1024;
        tsf_render_float(f, fbuf, n, 0);
        for (int i = 0; i < n; i++) {
            int vi = (int) (fm_soft_limit(fbuf[i]) * 32767.5f);
            buf[done + i] = (jshort) (vi < -32768 ? -32768 : (vi > 32767 ? 32767 : vi));
        }
        done += n;
    }

    (*env)->ReleaseShortArrayElements(env, out, buf, 0);
}

JNIEXPORT void JNICALL
Java_tech_zekon_FM_1Score_FM_1Synth_nativeFree(JNIEnv* env, jobject thiz, jlong handle) {
    tsf* f = TSF_HANDLE(handle);
    if (f != NULL) tsf_close(f);
}
