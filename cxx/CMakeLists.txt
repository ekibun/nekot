cmake_minimum_required(VERSION 3.7 FATAL_ERROR)
set(CMAKE_BUILD_TYPE "Release")
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -fPIC")

IF (ANDROID)
  find_library(log-lib log)
ELSE()
  find_package(JNI REQUIRED)
  include_directories(${JAVA_INCLUDE_PATH})
  include_directories(${JAVA_INCLUDE_PATH2})
ENDIF ()

set(common-lib ${log-lib})

include("${CMAKE_CURRENT_SOURCE_DIR}/quickjs.cmake")
include("${CMAKE_CURRENT_SOURCE_DIR}/ffmpeg.cmake")
