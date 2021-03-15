cmake_minimum_required(VERSION 3.7 FATAL_ERROR)
project(nekojni)

set(CMAKE_BUILD_TYPE "Release")

if (ANDROID)
  find_library(log-lib log)
  set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -fPIC")
else()
  find_package(JNI REQUIRED)
  include_directories(${JAVA_INCLUDE_PATH})
  include_directories(${JAVA_INCLUDE_PATH2})
endif ()

set(common-lib ${log-lib})

include("${CMAKE_CURRENT_SOURCE_DIR}/quickjs/CMakeLists.txt")
include("${CMAKE_CURRENT_SOURCE_DIR}/ffmpeg/CMakeLists.txt")
