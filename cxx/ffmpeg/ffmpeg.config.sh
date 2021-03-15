#!/bin/bash

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

pushd $DIR

abi="$1_$2"

if [ -d build/$abi ]; then
  rm -r build/$abi
fi
mkdir -p build/$abi

cd build/$abi

COMMON_CONFIG="\
  --disable-programs \
  --disable-encoders \
  --disable-muxers \
  --disable-avdevice \
  --disable-protocols \
  --disable-doc \
  --disable-filters \
  --disable-avfilter \
  --enable-static \
  --enable-cross-compile \
  --prefix=./ \
"

case $1 in
  "win32")
    INCLUDE="$JAVA_HOME\include;$INCLUDE"
    LIB="$JAVA_HOME\lib;$LIB"
    ../../ffmpeg/configure \
      $COMMON_CONFIG \
      --arch=$2 \
      --target-os=$1 \
      --toolchain=msvc \
      --disable-d3d11va \
      --disable-dxva2 \
      --extra-cflags="-MD -DWINAPI_FAMILY=WINAPI_FAMILY_APP -D_WIN32_WINNT=0x0A00" \
      --extra-ldflags="-APPCONTAINER WindowsApp.lib"
    ;;
  "android")
    MIN_PLATFORM="$ANDROID_NDK_HOME/platforms/android-21"
    TOOLCHAIN="$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/windows-x86_64"
    case $2 in
      "arm")
        CC_PREFIX="$TOOLCHAIN/bin/armv7a-linux-androideabi21"
        CROSS_PREFIX="$TOOLCHAIN/bin/arm-linux-androideabi-"
        ;;
      "arm64")
        CC_PREFIX="$TOOLCHAIN/bin/aarch64-linux-android21"
        CROSS_PREFIX="$TOOLCHAIN/bin/aarch64-linux-android-"
        ;;
      "x86")
        CC_PREFIX="$TOOLCHAIN/bin/i686-linux-android21"
        CROSS_PREFIX="$TOOLCHAIN/bin/i686-linux-android-"
        ;;
      "x86_64")
        CC_PREFIX="$TOOLCHAIN/bin/x86_64-linux-android21"
        CROSS_PREFIX="$TOOLCHAIN/bin/x86_64-linux-android-"
        ;;
      *)
        exit 1
    esac
    ../../ffmpeg/configure \
      $COMMON_CONFIG \
      --arch=$2 \
      --target-os=$1 \
      --cc=$CC_PREFIX-clang \
      --cxx=$CC_PREFIX-clang++ \
      --cross-prefix=$CROSS_PREFIX \
      --enable-jni \
      --disable-asm \
      --extra-cflags="-Os -fpic -DANDROID" \
      --extra-ldflags="-Wl,-rpath-link=$MIN_PLATFORM/arch-arm/usr/lib -nostdlib -fPIC"
    sed -i "s/#define HAVE_INET_ATON 0/#define HAVE_INET_ATON 1/" config.h
    sed -i "s/#define getenv(x) NULL/\\/\\/ #define getenv(x) NULL/" config.h
    ;;
  *)
    exit 1
esac
if [ "$1" == "win32" ]; then
  toolchain='msvc'
  extracflags="-MD -DWINAPI_FAMILY=WINAPI_FAMILY_APP -D_WIN32_WINNT=0x0A00"
  extraldflags="-APPCONTAINER WindowsApp.lib"
fi

make -j8
make install

popd