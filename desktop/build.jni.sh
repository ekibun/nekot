#!/bin/bash
set -e

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

if [ $( which wslpath ) ]; then
  COMPILER_PREFIX="x86_64-w64-mingw32"
  CMAKE_FLAG=(
    -DCMAKE_SYSTEM_NAME=Windows
    -DCOMPILER_PREFIX=${COMPILER_PREFIX}-windres
    -DCMAKE_C_COMPILER=${COMPILER_PREFIX}-gcc
    -DCMAKE_CXX_COMPILER=${COMPILER_PREFIX}-g++
    -DCMAKE_FIND_ROOT_PATH=/usr/${COMPILER_PREFIX}
  )
  export JAVA_HOME=$( wslpath -u "$1" )
else
  export JAVA_HOME=$1
fi

BUILD_DIR="$DIR/.cxx"
cmake "${CMAKE_FLAG[@]}" -DLIBRARY_OUTPUT_PATH=bin -B "$BUILD_DIR" -S "$DIR/../cxx"
cmake --build "$BUILD_DIR" --config Release --verbose