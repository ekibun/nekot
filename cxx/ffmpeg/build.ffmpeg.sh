#!/bin/bash
set -e

ANDROID_NDK_HOME=/home/ekibun/android-ndk-r21e

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

abi="$1_$2"

CONFIG_ARGS=(
  --enable-pic
  --enable-static
  --disable-shared
  --enable-gpl
  --enable-nonfree
  --disable-programs
  --disable-encoders
  --disable-muxers
  --disable-network
  --disable-postproc
  --disable-avdevice
  --disable-protocols
  --disable-doc
  --disable-filters
  --disable-avfilter
  --enable-cross-compile
  --prefix=$DIR/build/$abi/
)

exitUnsupport() {
  echo "unsupport abi $abi"
  exit 1
}

case $1 in
  "win32")
    case $2 in
      "x86")
        CROSS_PREFIX=i686-w64-mingw32
        ;;
      "x86_64")
        CROSS_PREFIX=x86_64-w64-mingw32
        ;;
      *)
        exitUnsupport
    esac
    CONFIG_ARGS+=(
      --arch=$2
      --target-os=mingw32
      --cross-prefix=$CROSS_PREFIX-
    )
    ;;
  "android")
    MIN_API=21
    ARCH_ROOT="$ANDROID_NDK_HOME/platforms/android-$MIN_API/arch-$2"
    TOOLCHAIN="$ANDROID_NDK_HOME/toolchains/llvm/prebuilt/linux-x86_64"
    case $2 in
      "arm")
        CC_PREFIX="$TOOLCHAIN/bin/armv7a-linux-androideabi$MIN_API"
        HOST=arm-linux-androideabi
        ;;
      "arm64")
        CC_PREFIX="$TOOLCHAIN/bin/aarch64-linux-android$MIN_API"
        HOST=aarch64-linux-android
        ;;
      "x86")
        CC_PREFIX="$TOOLCHAIN/bin/i686-linux-android$MIN_API"
        HOST=i686-linux-android
        CONFIG_ARGS+=(--disable-asm) # TODO https://trac.ffmpeg.org/ticket/7796
        ;;
      "x86_64")
        CC_PREFIX="$TOOLCHAIN/bin/x86_64-linux-android$MIN_API"
        HOST=x86_64-linux-android
        CONFIG_ARGS+=(--disable-asm) # TODO https://stackoverflow.com/a/57707863
        ;;
      *)
        exitUnsupport
    esac
    CONFIG_ARGS+=(
      --arch=$2
      --target-os=$1
      --cc=$CC_PREFIX-clang
      --cxx=$CC_PREFIX-clang++
      --cross-prefix=$TOOLCHAIN/bin/$HOST-
      --extra-ldflags="-Wl,-rpath-link=$ARCH_ROOT/usr/lib"
    )
    ;;
  *)
    exitUnsupport
esac

echo "build ffmpeg for $abi"

echo CONFIG_ARGS="${CONFIG_ARGS[@]}"

builddir=$DIR/build/.make/$abi

if [ -d $builddir ]; then
  rm -r $builddir
fi
mkdir -p $builddir

cd $builddir

$DIR/ffmpeg/configure "${CONFIG_ARGS[@]}"

if [ $1 == "android" ]; then
  sed -i "s/#define HAVE_INET_ATON 0/#define HAVE_INET_ATON 1/" config.h
  sed -i "s/#define getenv(x) NULL/\\/\\/ #define getenv(x) NULL/" config.h
fi

make -j8
make install
