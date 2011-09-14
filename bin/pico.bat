@echo off

set PICO_HOME=%~dp0..
::set PICO_CLASSPATH=";"

call groovy.bat -cp "%PICO_HOME%\lib\ini4j-0.5.2-jdk14.jar;%PICO_HOME%\libexec" %PICO_HOME%\bin\pico.groovy %*
