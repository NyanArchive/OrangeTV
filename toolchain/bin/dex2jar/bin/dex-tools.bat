@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  dex-tools startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and DEX_TOOLS_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\dex-tools-2.1-SNAPSHOT.jar;%APP_HOME%\lib\asm-9.4-mctle.jar;%APP_HOME%\lib\dx-30.0.3.jar;%APP_HOME%\lib\d8-33.0.0.jar;%APP_HOME%\lib\dex-translator-2.1-SNAPSHOT.jar;%APP_HOME%\lib\d2j-smali-2.1-SNAPSHOT.jar;%APP_HOME%\lib\d2j-jasmin-2.1-SNAPSHOT.jar;%APP_HOME%\lib\dex-writer-2.1-SNAPSHOT.jar;%APP_HOME%\lib\dex-ir-2.1-SNAPSHOT.jar;%APP_HOME%\lib\dex-reader-2.1-SNAPSHOT.jar;%APP_HOME%\lib\dex-reader-api-2.1-SNAPSHOT.jar;%APP_HOME%\lib\d2j-base-cmd-2.1-SNAPSHOT.jar;%APP_HOME%\lib\asm-util-9.4.jar;%APP_HOME%\lib\asm-analysis-9.4.jar;%APP_HOME%\lib\asm-commons-9.4.jar;%APP_HOME%\lib\asm-tree-9.4.jar;%APP_HOME%\lib\antlr4-4.9.3.jar;%APP_HOME%\lib\antlr4-runtime-4.9.3.jar;%APP_HOME%\lib\antlr-3.5.3.jar;%APP_HOME%\lib\ST4-4.3.1.jar;%APP_HOME%\lib\antlr-runtime-3.5.3.jar;%APP_HOME%\lib\org.abego.treelayout.core-1.0.3.jar;%APP_HOME%\lib\javax.json-1.0.4.jar;%APP_HOME%\lib\icu4j-69.1.jar


@rem Execute dex-tools
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %DEX_TOOLS_OPTS%  -classpath "%CLASSPATH%"  %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable DEX_TOOLS_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%DEX_TOOLS_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
