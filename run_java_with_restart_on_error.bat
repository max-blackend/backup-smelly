@echo off
:restart
java -jar -Xms4g .\target\smelly-1.0-shaded.jar
if %errorlevel% neq 0 goto restart
