#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2019/7/15 18:15
# @Author  : LiuXiangNan
# @Site    : 
# @File    : FlashToolScript.py



import configparser
import subprocess
import time
from pathlib import Path
import threading
import serial

config = configparser.ConfigParser()
config.read('./settings.ini', encoding='utf-8')
file_path_name = config.get('CMD', 'FILEPATHNAME')
test_count = config.get('CMD', 'TESTCOUNT')
com = config.get('CMD', 'COM')
bps = config.get('CMD', 'COMBPS')
timeout = config.get('CMD', 'TIMEOUT')
disusb = config.get('CMD', 'DISUSB')
ch = config.get('CMD', 'CH')

check_CH = [0xFF]
CH1_on = [0xA0, 0x01, 0x01, 0xA2]
CH1_off = [0xA0, 0x01, 0x00, 0xA1]

CH2_on = [0xA0, 0x02, 0x01, 0xA3]
CH2_off = [0xA0, 0x02, 0x00, 0xA2]

CH3_on = [0xA0, 0x03, 0x01, 0xA4]
CH3_off = [0xA0, 0x03, 0x00, 0xA3]

CH4_on = [0xA0, 0x04, 0x01, 0xA5]
CH4_off = [0xA0, 0x04, 0x00, 0xA4]

spe_time = 0.1



def mainRun():
    print("file path name: ", file_path_name,
          "\ncom: ", com,
          "\ntest count: ", test_count,
          "\nbps : ", bps,
          "\ntime out : ", timeout,
          "\ndisconnect time:", disusb,
          "\nCH:", ch
          )

    try:
        global ser
        ser = serial.Serial(com, bps, timeout=0.5)
        print(ser)
        ser.write(check_CH)
        # time.sleep(5)
        # ser.write(b"0xA0 0x01 0x01 0xA2")

        print("test: ", ser.readline())
        if ser.readline():
            ch_reset()#复位端口，关闭
        else:
            print("端口错误！")
            return
        # ser.write(b"0x51 0xFF")
        # time.sleep(5)
        # ser.write(b"0xFF")
        # time.sleep(5)
        # ser.write(b"0xFF")
    except:
        print("COM 口设置错误请再重新设置后再开始！")
        return

    path = Path.cwd()
    print(path)

    for i in range(0, int(test_count)):
        print("正在测试第 %d 次。。。" % (i + 1))
        usbControl_off()
        thread_usb = threading.Thread(target=usbControl_on(), name='usbControl')
        thread_usb.start()
        cmd = subprocess.Popen("{0}/flashtool/flash_tool.exe -i {1}".format(path, file_path_name),
                               shell=True,
                               stdout=subprocess.PIPE,
                               stderr=subprocess.PIPE
                               )

        stdout, stderr = cmd.communicate()
        returncode = cmd.returncode
        print("cmd : ", returncode)
        if returncode == 0:
            print("第 %d 次测试，刷机成功" % (i + 1))
            break
        print("第 %d 次测试完成，将进行下次测试！" % (i + 1))
        time.sleep(10)
    print("测试结束！")

def ch_reset():
    print("reset CH")
    ser.write(CH1_off)
    time.sleep(spe_time)
    ser.write(CH2_off)
    time.sleep(spe_time)
    ser.write(CH3_off)
    time.sleep(spe_time)
    ser.write(CH4_off)
    time.sleep(spe_time)


def usbControl_on():
    time.sleep(int(timeout))
    print("usb control status on")
    ser.write(CH1_on)
    time.sleep(spe_time)
    ser.write(CH2_on)
    time.sleep(spe_time)
    ser.write(CH3_on)
    time.sleep(spe_time)
    ser.write(CH4_on)
    time.sleep(spe_time)
    # ser.write(b"A0 04 01 A5")#开
    time.sleep(int(disusb))
def usbControl_off():
    print("usb control status off")
    # ser.write(b"A0 04 00 A4")
    ser.write(CH1_off)
    time.sleep(spe_time)
    ser.write(CH2_off)
    time.sleep(spe_time)
    ser.write(CH3_off)
    time.sleep(spe_time)
    ser.write(CH4_off)
    time.sleep(spe_time)

if __name__ == '__main__':
    mainRun()
