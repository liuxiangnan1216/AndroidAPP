#!/usr/bin/env monkeyrunner
# coding=utf-8

#Created by liuxiangnan on 18-07-06.

import sys
import os
import datetime
import re
import subprocess

from time import sleep
from com.android.monkeyrunner import MonkeyRunner

import logging


logging.basicConfig(level=logging.DEBUG,format="%(asctime)s-%(levelname)s==:  %(message)s")
TAG = 'liuxiangnan=:'


CMD_MAP = {  
    "TOUCH": lambda dev, arg: dev.touch(**arg),  
    "DRAG": lambda dev, arg: dev.drag(**arg),  
    "PRESS": lambda dev, arg: dev.press(**arg),  
    "TYPE": lambda dev, arg: dev.type(**arg),  
    "WAIT": lambda dev, arg: MonkeyRunner.sleep(**arg)  
    }  
  
  
file = sys.argv[1]#获取录制的mr文件名
countStr = sys.argv[2]#获取测试次数



#Process a single file for the specified device.
def process_file(fileProcess, numberTimes):
    
    for line in fileProcess:
        (cmd, rest) = line.split("|")  
        try:  
            rest = eval(rest)  
        except:  
            print ("unable to parse options")  
            continue  
  
        if cmd not in CMD_MAP:  
            print ("unknown command: " + cmd) 
            continue  
  
        CMD_MAP[cmd](device, rest)
        #sleep(1)
        screenShotSave(shotPath, device, numberTimes)
        
        
def screenShotSave(pathName, deviceName, numberTimes):#保存截图
    
    result = deviceName.takeSnapshot()
    nowTime = datetime.datetime.now().strftime('%Y%m%d_%H%M%S')#当前时间
    
    savePath = './%s/%s'%(pathName,str(numberTimes))
    saveName = '%s_%s'%(numberTimes,nowTime)

    result.writeToFile('./%s/%s.png'%(savePath,saveName),'png')



def mkdir(fileName, count):
    print('创建目录，开始')
    
    pattern = r'\.\w+'
    match = re.search(pattern, fileName)

    if match == None:
        foldName = fileName
    else:
        (foldName, exteName) = fileName.split('.',1)
    
    nowTime = datetime.datetime.now().strftime('%Y%m%d_%H%M')#当前时间
    pathFather = './%s_%s'%(foldName,nowTime)
    

    folder = os.path.exists(pathFather)
    
    if not folder:
        os.makedirs(pathFather)
    else:
        print('文件夹已经存在，测试程序退出，请重新确认！')
        sys.exit(0)
        
    if count == 1:
        print('创建目录，结束')
        return pathFather
    else:
        print('创建子目录中。。。')
        for i in range(1,count):
            pathChild = '%s/%s'%(pathFather,str(i))
            print(pathChild)
            folderChild = os.path.exists(pathChild)
            if not folderChild:
                os.makedirs(pathChild)
            else:
                print('子目录异常，程序退出！')
                sys.exit(0)
        print('创建目录，结束')
        return pathFather


def get_devices():
    pipe = subprocess.Popen('adb devices', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)

    if not pipe.stderr:
        print('No adb found,please install adb! Abort test')
        return False
    # print(pipe.stdout.read().decode('utf-8'))
    devices = re.findall(r'\s(\S+)\tdevice', pipe.stdout.read().decode('utf-8'))
    return devices
    
    
def parameter_Check():

    if len(sys.argv) == 3:
        pass
    else:
        print('输入的参数个数有误!')
        return
        
    if int(countStr) > 0:
        totalCount = int(countStr) + 1
    else:
        print('输入的参数有误！')
        return

def main():
    print len(sys.argv)
    parameter_Check()
    
    devices = get_devices()
    if not devices:
        print('No devices found. Exit')
        return
        
    totalCount = int(countStr) + 1
    fp = open(file, "r") 
    fileName = fp.name
    logging.debug(TAG+fileName)
    fp.close() 
    
    global shotPath
    shotPath = mkdir(fileName, totalCount)

    global device 
    device = MonkeyRunner.waitForConnection() 
    print(device)

    print('测试开始。。。')
    for i in range(1,totalCount):
     
        #os.system('clear')
        print('请勿关闭，正在进行第  %d  次测试。。。'%i)
        fp = open(file, "r") 
        process_file(fp, i) 
        fp.close()
    print('测试结束！共计测试 %d 次'%i)
    #sys.exit(0)
    #fp.close();  
     
  
if __name__ == "__main__":  
    main()
