@echo off
del *.class
:BUILD
cls
echo "Compiling files"
javac *.java
echo "Done."
pause
