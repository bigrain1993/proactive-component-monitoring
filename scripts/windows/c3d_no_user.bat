@echo off
echo. 
echo --- C3D ---------------------------------------------

goto doit

:usage
echo. 
goto end


:doit
SETLOCAL
call init.bat
start %JAVA_CMD% org.objectweb.proactive.rmi.StartNode //localhost/dispatcher
start %JAVA_CMD% org.objectweb.proactive.rmi.StartNode //localhost/Renderer1
start %JAVA_CMD% org.objectweb.proactive.rmi.StartNode //localhost/Renderer2
start %JAVA_CMD% org.objectweb.proactive.rmi.StartNode //localhost/Renderer3
start %JAVA_CMD% org.objectweb.proactive.rmi.StartNode //localhost/Renderer4
pause
start %JAVA_CMD% org.objectweb.proactive.examples.c3d.C3DDispatcher //localhost/dispatcher c3d.hosts
ENDLOCAL

:end
echo. 
echo ---------------------------------------------------------
