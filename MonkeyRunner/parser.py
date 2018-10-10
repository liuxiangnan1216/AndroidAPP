#!/usr/bin/env python
# coding=utf-8

#Created by liuxiangnan on 18-07-06.


#
#解析录取的 monkeyRunner 录制的 action 文件添加等待时间
# 命令 ./parser.py 源文件名  新的文件名（可以和源文件名重复将会覆盖源文件）
#
import sys
import re

def main():

    file = sys.argv[1]
    newFileName = sys.argv[2]
    addinfo = "WAIT|{'seconds':1.0,}"
    pattern = r'TOUCH\b'
    
    fp = open(file, "r")
    oldfile = fp.readlines()
    fp.close

    
    for i in range(0, len(oldfile)):
        print(oldfile[i])
        match = re.match(pattern, oldfile[i])
        if match == None:
            pass
        else:
            oldfile[i] = oldfile[i] + addinfo + '\n'
            
            
    filenew = open(newFileName,'wb')
    for item in oldfile:
        filenew.write(item)
    filenew.close()
    


if __name__ == "__main__":  
    main()
