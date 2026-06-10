cmake_minimum_required(VERSION 3.22.1)
project(fmsynth C)

# TinySoundFont + stb_vorbis are warning-heavy single-file libraries; don't let
# their warnings fail or clutter the build.
add_library(fmsynth SHARED tsf_jni.c)
target_compile_options(fmsynth PRIVATE -O2 -w)

find_library(log-lib log)
target_link_libraries(fmsynth ${log-lib})
