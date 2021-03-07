@echo off

set CMAKE="C:\Program Files (x86)\Microsoft Visual Studio\2019\BuildTools\Common7\IDE\CommonExtensions\Microsoft\CMake\CMake\bin\cmake.exe"
set BUILD_DIR=%~dp0/build/cxx
echo %CMAKE%
%CMAKE% -S %~dp0 -B %BUILD_DIR%
%CMAKE% --build %BUILD_DIR%