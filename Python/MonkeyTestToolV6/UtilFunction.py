#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2019/1/17 9:30
# @Author  : LiuXiangNan
# @Site    : 
# @File    : UtilFunction.py
import re
import sqlite3
import subprocess
import tkinter.messagebox
import tkinter as tk
root = tk.Tk()
root.withdraw()


def check_env():
    try:
        print('check test environment...')
        conn = sqlite3.connect('//192.168.3.88/sql//ToolCheckEnv.db')
        print('env ok!')
        conn.close()
        return True
    except:
        tk.messagebox.showerror('ERROR', '无法运行该工具，该工具仅限内部使用,\n请确定已经正确连接内部服务器！')
        return False


def softHelp():
    tkinter.messagebox.showinfo('Help', ' 该工具用于封装 Android Monkey 测试命令！'
                                        '\n 简化测试人员对 Monkey 命令的使用'
                                        '\n Monkey 测试帮助请查看 Description 页面。')


def softAbout():
    tkinter.messagebox.showinfo('About', ' 仅供内部使用，未经授权请勿传播!'
                                         '\n '
                                         '\n Author  :  '
                                         '\n Mail  :  '
                                         '\n Org   :   '
                                         '\n Team  :   '
                                         '\n Version : '
                                         '\n Date : 2019-01-17')


def get_devices():
    pipe = subprocess.Popen('adb devices', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
    if not pipe.stderr:
        print('No adb found,please install adb! Abort test')
        return False
    devices = re.findall(r'\s(\S+)\tdevice', pipe.stdout.read().decode('utf-8'))
    if not devices:
        tk.messagebox.showerror('ERROR', 'No dvices found!')
    return devices
