#!/usr/bin/env python
# coding=utf-8
# Created by liuxiangnan on 18-07-06.


import re
import subprocess
from com.android.monkeyrunner import MonkeyRunner,MonkeyDevice
from com.android.monkeyrunner.recorder import MonkeyRecorder as recorder


def main():
    devices = get_devices()
    if not devices:
        print('No devices found. Exit')
        return
    print(devices)
    if len(devices):
        print('录制脚本中。。。')
        device = MonkeyRunner.waitForConnection()
        recorder.start(device)


def get_devices():
    pipe = subprocess.Popen('adb devices', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)

    if not pipe.stderr:
        print('No adb found,please install adb! Abort test')
        return False
        
    devices = re.findall(r'\s(\S+)\tdevice', pipe.stdout.read().decode('utf-8'))
    return devices

if __name__== '__main__':
    main()
