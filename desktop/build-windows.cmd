@echo off

call "C:\Program Files (x86)\Microsoft Visual Studio\2019\BuildTools\VC\Auxiliary\Build\vcvarsall.bat" x64
set BUILD_DIR=./.cxx
cmake -S %~dp0/../cxx -B %BUILD_DIR%
cmake --build %BUILD_DIR% --config Release