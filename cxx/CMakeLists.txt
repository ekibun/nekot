cmake_minimum_required(VERSION 3.7 FATAL_ERROR)
project(nekojni)

set(CMAKE_BUILD_TYPE "Release")
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -fPIC")

if (WIN32)
  include_directories("$ENV{JAVA_HOME}/include")
  include_directories("$ENV{JAVA_HOME}/include/win32")
endif ()

if (ANDROID)
  find_library(log-lib log)
endif ()

set(common-lib ${common-lib} ${log-lib})

include("${CMAKE_CURRENT_SOURCE_DIR}/quickjs/CMakeLists.txt")
include("${CMAKE_CURRENT_SOURCE_DIR}/ffmpeg/CMakeLists.txt")
