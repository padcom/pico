@echo off

set PICO_HOME=%~dp0..

call groovy.bat -cp %PICO_HOME%\libexec %PICO_HOME%\bin\pico.groovy %*
