cmake_minimum_required(VERSION 3.7 FATAL_ERROR)
project(jniquickjs LANGUAGES CXX)

find_package(JNI REQUIRED)
include_directories(${JAVA_INCLUDE_PATH})
include_directories(${JAVA_INCLUDE_PATH2})

include("${CMAKE_CURRENT_SOURCE_DIR}/quickjs.cmake")

IF (CMAKE_SYSTEM_NAME MATCHES "Linux")
    target_compile_options(quickjs PRIVATE "-fPIC")
ENDIF ()

add_library(jniquickjs SHARED ${CXX_LIB_DIR}/jni.cpp)
target_compile_features(jniquickjs PUBLIC cxx_std_17)
target_link_libraries(jniquickjs PRIVATE quickjs)