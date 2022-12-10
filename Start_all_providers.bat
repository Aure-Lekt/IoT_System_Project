@ECHO OFF

SET version="1.0"
SET parent_path=%~dp0
cd %parent_path%

SET time_to_sleep=10

echo Starting Providers ... may not work, for obsucre reason beyond human comprehension

cd .\thermometer-provider\target
START "Thermometer" /B "cmd /c javaw -jar thermometer-provider-%version%.jar > sout_pv1.log 2>&1"
echo Thermometer started
timeout /t %time_to_sleep% /nobreak > NUL

cd ..\..
cd .\detectorone-provider\target
START "DetectorOne" /B "cmd /c javaw -jar detectorone-provider-%version%.jar > sout_pv2.log 2>&1"
echo Detector One started
timeout /t %time_to_sleep% /nobreak > NUL

cd ..\..
cd .\detectortwo-provider\target
START "DetectorTwo" /B "cmd /c javaw -jar detectortwo-provider-%version%.jar > sout_pv3.log 2>&1"
echo Detector Two started
timeout /t %time_to_sleep% /nobreak > NUL

cd ..\..
cd .\roboticarm-provider\target
START "DetectorTwo" /B "cmd /c javaw -jar roboticarm-provider-%version%.jar > sout_pv4.log 2>&1"
echo Robotic Arm started
timeout /t %time_to_sleep% /nobreak > NUL

cd %parent_path%

::Kill self
title=arrowheadSecureStarter
FOR /F "tokens=2" %%p in ('"tasklist /v /NH /FI "windowtitle eq arrowheadSecureStarter""') DO taskkill /pid %%p > NUL 2>&1
