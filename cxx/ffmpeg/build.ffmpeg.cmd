@echo off

set MSYS2_PATH_TYPE=inherit

set ANDROID_NDK_HOME=C:/Users/ekibun/AppData/Local/Android/Sdk/ndk/21.4.7075529
"C:\msys64\usr\bin\bash.exe" --login -x %~dp0ffmpeg.config.sh android arm
"C:\msys64\usr\bin\bash.exe" --login -x %~dp0ffmpeg.config.sh android arm64
"C:\msys64\usr\bin\bash.exe" --login -x %~dp0ffmpeg.config.sh android x86
"C:\msys64\usr\bin\bash.exe" --login -x %~dp0ffmpeg.config.sh android x86_64

call "C:\Program Files (x86)\Microsoft Visual Studio\2019\BuildTools\VC\Auxiliary\Build\vcvarsall.bat" x64
"C:\msys64\usr\bin\bash.exe" --login -x %~dp0ffmpeg.config.sh win32 x86_64
call "C:\Program Files (x86)\Microsoft Visual Studio\2019\BuildTools\VC\Auxiliary\Build\vcvarsall.bat" x86
"C:\msys64\usr\bin\bash.exe" --login -x %~dp0ffmpeg.config.sh win32 x86