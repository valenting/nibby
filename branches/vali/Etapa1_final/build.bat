@echo off
del *.class
set jpath="C:\Program Files\Java\jdk1.6.0_16\bin"
if EXIST "%jpath%\javac.exe"
	goto BUILD
if NOT EXIST "%jpath%\javac.exe"
	goto NOTFOUND

:BUILD
cls
echo "Compiling files"
%jpath%\javac *.java
echo "Done."
pause
goto END

:NOTFOUND
javac *.java 
cls
echo "cannot find C:\Program Files\Java\jdk1.6.0_16\bin\javac.exe"
echo " please set the path manually "
pause
:END