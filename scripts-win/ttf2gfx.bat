@echo off
set DIR="%~dp0"
set JAVA_EXEC="%DIR:"=%\bin\javaw"



pushd %DIR% & start "TTF2GFX" %JAVA_EXEC% %CDS_JVM_OPTS%  -p "%~dp0/../app" -m TTF2GFX/ttf2gfx.Ttf2GfxApp  %* & popd
