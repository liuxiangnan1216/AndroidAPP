LoNg tool require JRE, start LoNg.bat

JRE install url: http://www.java.com/zh_CN/download/manual.jsp

v2.1709.1 2017.2.27
be compatible with MobileLog and NetLog new feature

v2.1650.1 2016.12.05
enhancement: After per pull action done, check whether the size of the file pulled out is the same as
that of the original file in the device.

v2.1642.4 2016.10.12
add: support debuggerd exception zcore
add: be compatible with new format of kernel log, when parsing kernel time
optimize: refine all kinds of modem log paths, fix some potential bug.

v2.1609.5 2016.2.26
optimize: adjust the structure of loNg to Multi-thread for higher speed
optimize: reduce processing time of /data/aee_exp/xxx.DB in User Load 

v1.1604.2 2016.1.19
add: external sdcard mtklog directory
add: Record all DB info into a file named 'DBFileMap'

v1.1549.5 2015.12.04
fix: add safe code for exceptional device time 

v1.1546.3 2015.11.10
fix: the issue of modem logger being shut down

v1.1542.5 2015.10.23
add: mdlog1 and mdlog3 concurrence case

v1.1542.3 2015.10.20
fix: fix issue of taglog can not been stoped

v1.1539.3 2015.09.07
fix: fix potential issue which may cause drop mdlog

v1.1537.1 2015.09.07
fix: fix the issue which cause system_server wtf exception
add: multi-log support

v1.1533.2 2015.08.11
add: support new mdlog file suffix

v1.1511.5 2015.03.12
fix: the NE of mdlogger which is caused by too long file name which is renamed by LoNg

v1.1436.5 2014.09.05
modify: fix mdlog folder name in /data to /data/mdlog/bootupLog

v1.1434.1 2014.08.19
modify: do not generate 0kb file
add: FileMap file to record every log file and start time in each zip package

v1.1430.5 2014.07.28
fix£ºdelete modem log file in k2 project

v1.1422.1 2014.06.10
fix£ºmodem log can't start after stop
add: pull out EE memory dump after boot up EE

v1.1402.1 2014.01.09
modify: user load, /data/* file can not delete. filter out all repeat files in /data.

v1.1349.1 2013.12.05
add: support LTE modem log.

v1.1346.1 2013.11.11
fix: device name contain "device", get sn error.

v1.1345.1 2013.11.04
fix: isException rule, skip aee temp folder .dbg file.

v1.1343.2 2013.10.23
fix: read logpath config from ini.

v1.1343.1 2013.10.21
fix: fail on pull large db files.

v1.1342.1 2013.10.16
fix: do not start mtklogger every 5 minutes. old branch, netlog will start every time.

v1.1341.1 2013.10.09
fix: do not mv /data/anr/ files.
new: start mtklogger every 5 minutes.

v1.1339.2 2013.09.25
fix: 1.5-1.6 stop log after first exception.

v1.6 2013.09.10
fix: user load, repeat dbg files.

v1.5 2013.08.29
new: user load, /data/*.dbg file can not delete. filter out repeat files.
fix: do not pull aee temp folder.
fix: Exception netlog split into two packages. Get file list again after exception.

v1.4 2013.07.24
modify: folder contain .cap file, package name add "_netlog".

v1.3 2013.06.25
fix: destroy redirect thread after kill blocking adb.
modify: delay 1s after pull one file, for MT6572.

v1.2 2013.06.20
fix: rename logic error, some file not pull.

v1.1 2013.06.08
add watchdog thread for detecting device blocking adb.