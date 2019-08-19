% Writed by yanghui.li <yanghui.li@mediatek.com> version 00.36%
% 2013/5/25 Update check java install process for windows7, auto copy java.exe to system32%
% 2013/6/15 Update the install directory%
% 2013/7/20 Update the AutoUpdate feature%
% 2015/2/14 DO NOT show analysis result if the use is srv_eservice%
% 2016/5/01 DO NOT check JDK%

echo QAAT Check Start.

@echo off

set version=0075
if exist "c:\QAAT\AutoUpdate.jar" (
    java -jar c:\QAAT\AutoUpdate.jar -v %version%
)

if exist "c:\QAAT\aee_extract.exe" (
    if exist "%windir%\system32\aee_extract.exe" (
       del %windir%\system32\aee_extract.exe
    )
    if exist "%windir%\syswow64" (
       if exist "%windir%\syswow64\aee_extract.exe" (
           del %windir%\syswow64\aee_extract.exe
       )
    )
   echo copy aee_extract to system32 and syswow64
   copy "c:\QAAT\aee_extract.exe" "%windir%\system32\aee_extract.exe"
   if exist "%windir%\syswow64" (
        copy "c:\QAAT\aee_extract.exe" "%windir%\syswow64\aee_extract.exe"
   )
   del c:\QAAT\aee_extract.exe
   
   % only 0024->0025 version need, before it's need update the reigster %
   echo.>>temp.reg
   echo [HKEY_CLASSES_ROOT\QAAT]>>temp.reg
   echo.>>temp.reg
   echo @="URL:QAAT Protocol">>temp.reg
   echo "URL Protocol"="">>temp.reg
   echo.>>temp.reg
   echo [HKEY_CLASSES_ROOT\QAAT\DefaultIcon]>>temp.reg
   echo @="java -classpath C:\\QAAT\\QAAT-ProGuard.jar com.mediatek.qaat.html.HtmlFunctionFactory" >> temp.reg
   echo.>>temp.reg
   echo [HKEY_CLASSES_ROOT\QAAT\shell]>>temp.reg
   echo.>>temp.reg
   echo [HKEY_CLASSES_ROOT\QAAT\shell\open]>>temp.reg
   echo.>>temp.reg
   echo [HKEY_CLASSES_ROOT\QAAT\shell\open\command]>>temp.reg
   echo @="\"javaw\" \"-classpath\" \"C:\\QAAT\\QAAT-ProGuard.jar\" \"com.mediatek.qaat.html.HtmlFunctionFactory\" \"%%1\"" >> temp.reg
   c:\windows\regedit -s temp.reg
   del temp.reg
)

set qaat_path=c:\QAAT\QAAT-ProGuard.jar

if not exist "c:\QAAT\QAAT-ProGuard.jar" (
   set qaat_path=QAAT-ProGuard.jar
)

@echo on
java -jar %qaat_path% -d %1

@echo off
set special_user_prefix=srv_eservice
if exist "analysis_result.txt" (
    if /i not %username:~0,12% == %special_user_prefix% (
	start analysis_result.txt
	pause
    )
)