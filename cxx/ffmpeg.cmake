cmake_minimum_required(VERSION 3.7 FATAL_ERROR)

project(ffmpeg LANGUAGES CXX)
add_library(ffmpeg SHARED
  ${CMAKE_CURRENT_LIST_DIR}/ffmpeg.cpp
)

IF (ANDROID)
  set(FFMPEG_PATH "${CMAKE_CURRENT_LIST_DIR}/build/android_${CMAKE_ANDROID_ARCH}")
  set(ffmpeg-lib
    z
  )
ENDIF ()

IF (WIN32)
  IF (CMAKE_VS_PLATFORM_NAME MATCHES "x64")
    set(FFMPEG_PATH "${CMAKE_CURRENT_LIST_DIR}/build/win32_x86_64")
  ELSE()
    set(FFMPEG_PATH "${CMAKE_CURRENT_LIST_DIR}/build/win32_x86")
  ENDIF ()

  set(ffmpeg-lib
    WindowsApp.lib
  )
ENDIF ()

target_include_directories(ffmpeg PRIVATE "${FFMPEG_PATH}/include")

target_link_libraries(ffmpeg PRIVATE
  ${common-lib}
  ${ffmpeg-lib}
  "${FFMPEG_PATH}/lib/libavformat.a"
  "${FFMPEG_PATH}/lib/libavcodec.a"
  "${FFMPEG_PATH}/lib/libavutil.a"
  "${FFMPEG_PATH}/lib/libswresample.a"
  "${FFMPEG_PATH}/lib/libswscale.a"
)