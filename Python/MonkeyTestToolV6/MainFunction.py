#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2019/1/17 9:28
# @Author  : LiuXiangNan
# @Site    : 
# @File    : MainFunction.py
import datetime
import os
import re
import subprocess
import sys
import threading
import tkinter.messagebox
import tkinter as tk
from multiprocessing.pool import Pool
from pathlib import Path
from time import sleep
from PyQt5.QtCore import QStringListModel, QTimer, QTime
root = tk.Tk()
root.withdraw()

from PyQt5 import QtWidgets

from MonkeyTestUI import Ui_MainWindow
from UtilFunction import check_env, softHelp, softAbout


black_list = []
isClickedBlackList = False
isClickedListBlack = False


white_list = []
isClickedWhiteList = False
isClickedListWhite = False
isManualStop = False

global test_deviceID
test_deviceID = []
man_stop = []

start_test_time = datetime.datetime.now()





class MainWindow(QtWidgets.QMainWindow, Ui_MainWindow):
    def __init__(self, parent=None):
        super(MainWindow, self).__init__(parent)
        self.setupUi(self)



        ###############################################################################################################
        self.isTimeStart = True
        # 创建定时器对象和时间对象
        self.timer = QTimer()  #
        self.timeClock = QTime()
        self.timer.timeout.connect(self.addtime)

        self.timestart()
        self.monkeyHelp()

        # self.lcdNumber.setStyleSheet("border: 2px solid green; color: red; background: silver;")
        # self.lcdNumber.setSegmentStyle(QLCDNumber.Flat)

        # self.lineEdit_time.


        self.pushButton_help.clicked.connect(softHelp)
        self.pushButton_about.clicked.connect(softAbout)
        # self.get_def_blacklist()
        self.lineEdit_black_list_path.setText('/sdcard/')
        self.lineEdit_whitelistpath.setText('/sdcard/')

        self.pushButton_getPackNamebacklist.clicked.connect(self.get_black_pkgname)
        self.pushButton_addbacklist.clicked.connect(self.add_black_list)
        self.pushButton_rmbacklist.clicked.connect(self.dele_black_list)
        self.pushButton_saveBlacklist.clicked.connect(self.save_blacklist)

        self.pushButton_whitelistget.clicked.connect(self.get_whilte_pkgname)
        self.pushButton_whitelistadd.clicked.connect(self.add_white_list)
        self.pushButton_whitelistrm.clicked.connect(self.dele_white_list)
        self.pushButton_whitelistsave.clicked.connect(self.save_whitelist)

        ##### 获取设备号页面
        self.pushButton_getDevicesID.clicked.connect(self.get_devicesID)
        self.pushButton_getDevicesID_2.clicked.connect(self.save_deviceID)

        self.pushButton_stop1.clicked.connect(lambda: self.stop_monkey(self.lineEdit_device1.text()))
        self.pushButton_stop2.clicked.connect(lambda: self.stop_monkey(self.lineEdit_device2.text()))
        self.pushButton_stop3.clicked.connect(lambda: self.stop_monkey(self.lineEdit_device3.text()))
        self.pushButton_stop4.clicked.connect(lambda: self.stop_monkey(self.lineEdit_device4.text()))
        self.pushButton_stop5.clicked.connect(lambda: self.stop_monkey(self.lineEdit_device5.text()))
        self.pushButton_stop6.clicked.connect(lambda: self.stop_monkey(self.lineEdit_device5.text()))
        self.pushButton_stop7.clicked.connect(lambda: self.stop_monkey(self.lineEdit_device7.text()))
        self.pushButton_stop8.clicked.connect(lambda: self.stop_monkey(self.lineEdit_device8.text()))
        self.pushButton_stop9.clicked.connect(lambda: self.stop_monkey(self.lineEdit_device9.text()))
        self.pushButton_stop10.clicked.connect(lambda: self.stop_monkey(self.lineEdit_device10.text()))


        self.checkBox.clicked.connect(self.switch_white)
        self.checkBox_2.clicked.connect(self.switch_black)

        #### 参数页面
        self.pushButton_savepar.clicked.connect(self.save_settings)
        self.pushButton_resetpar.clicked.connect(self.reset_settings)

        self.checkBox_isAnrStop_All.setChecked(True)
        self.checkBox_isAppErrStop_All.setChecked(True)
        self.checkBox_isCerStop_All.setChecked(True)
        self.checkBox_isRushStop_All.setChecked(True)
        self.checkBox_kill_all.setChecked(True)

        ###############################################################################################################
        self.pushButton_start_All.clicked.connect(self.start_monkey_test)

        ###############################################################################################################

    def stop_monkey(self, kill_devices_id):
        print('stop monkey id====', kill_devices_id)
        if kill_devices_id:#adb -s {0} shell ps | findstr monkey
            monkey_ps = subprocess.Popen('adb -s {0} shell ps | findstr monkey'.format(kill_devices_id),
                                         stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
            ps_id = monkey_ps.stdout.readline()
            print('ps_id====', ps_id)
            if ps_id != b'':
                (shell, str_other) = ps_id.decode('utf-8').split('   ', 1)
                print('str_other====', str_other)
                (pid, str1) = str_other.strip().split(' ', 1)
                print('pid===', pid)

                kill_monkey = subprocess.Popen('adb -s {0} shell kill {1}'.format(kill_devices_id, pid),
                                               stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
                # self.kill_monkey(kill_devices_id)
                if kill_devices_id not in man_stop:
                    man_stop.append(kill_devices_id)
                print('kill monkey man stop :', man_stop)
                tk.messagebox.showinfo('INFO', '{0} Monkey 测试即将停止，请查看设备状态！'.format(kill_devices_id))
            else:
                tk.messagebox.showinfo('INFO', 'Permission denied 或 Monkey测试已经停止，请查看设备状态！')
        else:
            tk.messagebox.showinfo('INFO', 'Device 不存在！')



    def switch_white(self):
        if self.checkBox.isChecked():
            self.checkBox_2.setChecked(False)

    def switch_black(self):
        if self.checkBox_2.isChecked():
            self.checkBox.setChecked(False)

    def monkeyHelp(self):
        fp = open('MonkeytestHelp.txt', "r", encoding='UTF-8')
        for line in fp.readlines():
            self.textBrowser_help.append(line)

    """
    获取devices 所有的包名
    """
    def get_devices_pkgName(self):
        pkg_devices = subprocess.Popen('adb devices', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
        print('pkg_devices ===', pkg_devices)
        if not pkg_devices.stderr:
            tk.messagebox.showerror('ERROR', 'No adb found,please install adb!')
        devices_id = re.findall(r'\s(\S+)\tdevice', pkg_devices.stdout.read().decode('utf-8'))
        print('devices id====', devices_id)
        pkg_name_list = []
        if devices_id:
            pipe = subprocess.Popen('adb -s {0} shell pm list packages -f'.format(devices_id[0]),
                                    stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True).communicate()
            if re.findall(r'error', str(pipe[1])):
                pkg_name_list = ['获取 Device 应用包名错误', '点击“GET”重新获取']
            str_pipe = str(pipe[0], encoding='utf-8')
            pkg_line = re.split(r'\r\n', str_pipe)
            pattern = r'apk='
            for i in range(0, len(pkg_line)):
                if re.findall(r'apk=', pkg_line[i]):
                    (app, pkg_name) = pkg_line[i].split('apk=')
                    pkg_name_list.append(pkg_name)
            return pkg_name_list


    """
    添加黑名单
    """
    def get_def_blacklist(self):
        global black_list
        fp = open('Configuration/DefaultBlackList.txt', "r", encoding='UTF-8')
        for line in fp.readlines():
            print('line====', line.strip())

            black_list.append(line.strip())
        # 向listview中赋值
        slm = QStringListModel()
        self.qList = black_list
        slm.setStringList(black_list)
        self.listView_black.setModel(slm)


    def get_black_pkgname(self):
        global black_list
        black_list = []
        self.get_def_blacklist()
        pkg_name = self.get_devices_pkgName()
        print('pkg name===', pkg_name)
        if pkg_name:
            # 向listview中赋值
            slm = QStringListModel()
            self.qList = pkg_name
            slm.setStringList(pkg_name)
            self.listView_appbacklist.setModel(slm)
            # 选择
            self.listView_appbacklist.clicked.connect(self.list_black_clicked)

    def list_black_clicked(self, qModelIndex):
        global select_black_pkg
        select_black_pkg = self.qList[qModelIndex.row()]
        global isClickedListBlack
        isClickedListBlack = True

    def add_black_list(self):
        global select_black_pkg
        global black_list
        global isClickedListBlack
        if isClickedListBlack:
            count = black_list.count(select_black_pkg)
            if count == 0:
                black_list.append(select_black_pkg)
                # 向listview中赋值
                slm = QStringListModel()
                self.addList = black_list
                slm.setStringList(black_list)
                self.listView_black.setModel(slm)

                self.listView_black.clicked.connect(self.clicked_black_list)
        else:
            tk.messagebox.showinfo('提示', '请选择要加入的包名！')

    def dele_black_list(self):
        global black_selected
        global black_list
        global isClickedBlackList

        if isClickedBlackList:
            count = black_list.count(black_selected)
            if count == 1:
                black_list.remove(black_selected)
                # print('black list remove===', black_list)
                isClickedBlackList = False
        else:
            tk.messagebox.showinfo('提示', '请选择要移除的包名！')
        slm = QStringListModel()
        slm.setStringList(black_list)
        self.listView_black.setModel(slm)

    def clicked_black_list(self, qModelIndex):
        global black_selected
        black_selected = self.addList[qModelIndex.row()]
        global isClickedBlackList
        isClickedBlackList = True

    def save_blacklist(self):
        global black_list
        global black_list_path
        global test_deviceID
        # black_list_path = self.lineEdit_black_list_path.text()
        # print('black_list_path:', black_list_path)
        #
        # if not black_list_path:
        #     tk.messagebox.showerror('ERROR', '请输入 Black List 保存路径！')
        # else:
        blacklistFile = open('Configuration/MonkeyBlackList.txt', 'w')
        for i in range(0, len(black_list)):
            blacklistFile.write(black_list[i])
            blacklistFile.write('\n')
        blacklistFile.flush()
        isExBlackList = os.path.getsize('Configuration/MonkeyBlackList.txt')
        if isExBlackList > 0:
            if len(test_deviceID) > 0:
                get_black_device_path = '/sdcard/' #self.lineEdit_black_list_path.text()

                for serialno in test_deviceID:
                    pushBlackList = subprocess.Popen(
                        'adb -s {0} push {1} {2} '.format(serialno, 'Configuration/MonkeyBlackList.txt', get_black_device_path),
                        stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True).communicate()
                    print('pushBlackList====', pushBlackList)
                    if re.findall(r'Permission denied', str(pushBlackList[1])):
                        tk.messagebox.showerror('ERROR', ' Permission denied ，请手动添加进 device！并在此输入路径')
            else:
                tk.messagebox.showerror('ERROR', 'Device 未选择确认！')
        else:
            tk.messagebox.showerror('ERROR', 'Black List 文件为空请确认是否已添加！')

#####################################################################################################################
    """
    添加白名单
    """
    def get_def_whitelist(self):
        global white_list
        fp = open('Configuration/DefaultBlackList.txt', "r", encoding='UTF-8')
        for line in fp.readlines():
            print('line====', line.strip())

            white_list.append(line.strip())
        # 向listview中赋值
        slm = QStringListModel()
        self.qList = white_list
        slm.setStringList(white_list)
        self.listView_whitelist.setModel(slm)

    def get_whilte_pkgname(self):
        global white_list
        white_list = []
        self.get_def_whitelist()
        pkg_name = self.get_devices_pkgName()
        print('pkg name ==', pkg_name)
        if pkg_name:
            # 向listview中赋值
            slm = QStringListModel()
            self.qList = pkg_name
            slm.setStringList(pkg_name)
            print(pkg_name)
            self.listView_whitelistapp.setModel(slm)
            # 选择
            self.listView_whitelistapp.clicked.connect(self.list_white_clicked)

    def list_white_clicked(self, qModelIndex):
        global select_white_pkg
        select_white_pkg = self.qList[qModelIndex.row()]
        global isClickedListWhite
        isClickedListWhite = True


    def add_white_list(self):
        print('add_white_list')
        global select_white_pkg
        global white_list
        global isClickedListWhite
        if isClickedListWhite:
            count = white_list.count(select_white_pkg)
            if count == 0:
                white_list.append(select_white_pkg)
                # 向listview中赋值
                slm = QStringListModel()
                self.addList = white_list
                slm.setStringList(white_list)
                self.listView_whitelist.setModel(slm)

                self.listView_whitelist.clicked.connect(self.clicked_white_list)
        else:
            tk.messagebox.showinfo('提示', '请选择要加入的包名！')


    def dele_white_list(self):
        global white_selected
        global white_list
        global isClickedWhiteList
        if isClickedWhiteList:
            count = white_list.count(white_selected)
            if count == 1:
                white_list.remove(white_selected)
                isClickedWhiteList = False
        else:
            tk.messagebox.showinfo('提示', '请选择要移除的包名！')
        slm = QStringListModel()
        slm.setStringList(white_list)
        self.listView_whitelist.setModel(slm)


    def clicked_white_list(self, qModelIndex):
        global white_selected
        white_selected = self.addList[qModelIndex.row()]
        global isClickedWhiteList
        isClickedWhiteList = True

    def save_whitelist(self):
        global white_list
        global white_list_path
        global test_deviceID
        # white_list_path = self.lineEdit_whitelistpath.text()
        # if not white_list_path:
        #     tk.messagebox.showerror('ERROR', '请输入 Black List 保存路径！')
        # else:
        whitelistFile = open('Configuration/MonkeyWhiteList.txt', 'w')
        for i in range(0, len(white_list)):
            whitelistFile.write(white_list[i])
            whitelistFile.write('\n')
        whitelistFile.flush()

        isExWhiteListFile = os.path.getsize('Configuration/MonkeyWhiteList.txt')
        if isExWhiteListFile > 0:
            if len(test_deviceID) > 0:
                get_white_device_path = '/sdcard/'#self.lineEdit_whitelistpath.text()
                for serialno in test_deviceID:
                    pushWhiteList = subprocess.Popen(
                        'adb -s {0} push {1} {2} '.format(serialno, 'Configuration/MonkeyWhiteList.txt', get_white_device_path),
                        stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True).communicate()
                    print('pushWhiteList===', pushWhiteList)
                    if re.findall(r'Permission denied', str(pushWhiteList[1])):
                        tk.messagebox.showerror('ERROR', ' Permission denied ，请手动添加进 device！并在此输入路径')
            else:
                tk.messagebox.showerror('ERROR', 'Device 未选择确认！')
        else:
            tk.messagebox.showerror('ERROR', 'Black List 文件为空请确认是否已添加！')




########################################################################################################################
    def reset_settings(self):
        self.spinBox_seed.setValue(0)
        self.spinBox_flip.setValue(0)
        self.spinBox_touch.setValue(0)
        self.spinBox_motion.setValue(0)
        self.spinBox_trackball.setValue(0)
        self.spinBox_nav.setValue(0)
        self.spinBox_majornav.setValue(0)
        self.spinBox_syskeys.setValue(0)
        self.spinBox_appswitch.setValue(0)
        self.spinBox_anyevent.setValue(0)

    def save_settings(self):
        setting_par = []
        setting_par.append(self.spinBox_seed.text())
        setting_par.append(self.spinBox_flip.text())
        setting_par.append(self.spinBox_touch.text())
        setting_par.append(self.spinBox_motion.text())
        setting_par.append(self.spinBox_trackball.text())
        setting_par.append(self.spinBox_nav.text())
        setting_par.append(self.spinBox_majornav.text())
        setting_par.append(self.spinBox_syskeys.text())
        setting_par.append(self.spinBox_appswitch.text())
        setting_par.append(self.spinBox_anyevent.text())
        # print('setting_par===', setting_par)
        return setting_par

    def get_devicesID(self):
        # init
        self.lineEdit_device1.setText('')
        self.lineEdit_device2.setText('')
        self.lineEdit_device3.setText('')
        self.lineEdit_device4.setText('')
        self.lineEdit_device5.setText('')
        self.lineEdit_device6.setText('')
        self.lineEdit_device7.setText('')
        self.lineEdit_device8.setText('')
        self.lineEdit_device9.setText('')
        self.lineEdit_device10.setText('')

        devicesID = subprocess.Popen('adb devices', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
        print('devicesID:', devicesID)
        global devices_id
        if not devicesID.stderr:
            tk.messagebox.showerror('ERROR', 'No adb found,please install adb!')
        devices_id = re.findall(r'\s(\S+)\tdevice', devicesID.stdout.read().decode('utf-8'))
        print('devices_id===', devices_id)
        if len(devices_id) > 10 or len(devices_id) == 0:
            tk.messagebox.showerror('ERROR', '请确定 Device 个数！')
        else:
            if devices_id[0]:
                self.lineEdit_device1.setText(devices_id[0])
                self.checkBox_device1.setChecked(True)

            if len(devices_id) >= 2 and devices_id[1]:
                self.lineEdit_device2.setText(devices_id[1])
                self.checkBox_device2.setChecked(True)

            if len(devices_id) >= 3 and devices_id[2]:
                self.lineEdit_device3.setText(devices_id[2])
                self.checkBox_device3.setChecked(True)

            if len(devices_id) >= 4 and devices_id[3]:
                self.lineEdit_device4.setText(devices_id[3])
                self.checkBox_device4.setChecked(True)

            if len(devices_id) >= 5 and devices_id[4]:
                self.lineEdit_device5.setText(devices_id[4])
                self.checkBox_device5.setChecked(True)

            if len(devices_id) >= 6 and devices_id[5]:
                self.lineEdit_device6.setText(devices_id[5])
                self.checkBox_device6.setChecked(True)

            if len(devices_id) >= 7 and devices_id[6]:
                self.lineEdit_device7.setText(devices_id[6])
                self.checkBox_device7.setChecked(True)

            if len(devices_id) >= 8 and devices_id[7]:
                self.lineEdit_device8.setText(devices_id[7])
                self.checkBox_device8.setChecked(True)

            if len(devices_id) >= 9 and devices_id[8]:
                self.lineEdit_device9.setText(devices_id[8])
                self.checkBox_device9.setChecked(True)

            if len(devices_id) >= 10 and devices_id[9]:
                self.lineEdit_device10.setText(devices_id[9])
                self.checkBox_device10.setChecked(True)
            global isClickedGetDeviceID
            isClickedGetDeviceID = True

    def save_deviceID(self):
        global test_deviceID
        global isClickedGetDeviceID
        if isClickedGetDeviceID:
            if self.checkBox_device1.isChecked() and len(devices_id) >= 1:
                if devices_id[0] not in test_deviceID:
                    test_deviceID.append(devices_id[0])

            if self.checkBox_device2.isChecked() and len(devices_id) >= 2:
                if devices_id[1] not in test_deviceID:
                    test_deviceID.append(devices_id[1])

            if self.checkBox_device3.isChecked() and len(devices_id) >= 3:
                if devices_id[2] not in test_deviceID:
                    test_deviceID.append(devices_id[2])

            if self.checkBox_device4.isChecked() and len(devices_id) >= 4:
                if devices_id[3] not in test_deviceID:
                    test_deviceID.append(devices_id[3])

            if self.checkBox_device5.isChecked() and len(devices_id) >= 5:
                if devices_id[4] not in test_deviceID:
                    test_deviceID.append(devices_id[4])

            if self.checkBox_device6.isChecked() and len(devices_id) >= 6:
                if devices_id[5] not in test_deviceID:
                    test_deviceID.append(devices_id[5])

            if self.checkBox_device7.isChecked() and len(devices_id) >= 7:
                if devices_id[6] not in test_deviceID:
                    test_deviceID.append(devices_id[6])

            if self.checkBox_device8.isChecked() and len(devices_id) >= 8:
                if devices_id[7] not in test_deviceID:
                    test_deviceID.append(devices_id[7])

            if self.checkBox_device9.isChecked() and len(devices_id) >= 9:
                if devices_id[8] not in test_deviceID:
                    test_deviceID.append(devices_id[8])

            if self.checkBox_device10.isChecked() and len(devices_id) == 10:
                if devices_id[9] not in test_deviceID:
                    test_deviceID.append(devices_id[9])

            str1 = []
            for i in range(0, len(test_deviceID)):
                str1.append(test_deviceID[i])
                str1.append(',')
            tkinter.messagebox.showinfo('对以下 Device 进行测试', str1)


    def log_level(self):
        return ' -v-v-v'

    def isRush(self):
        if self.checkBox_isRushStop_All.isChecked():
            crash_cmd = ' --ignore-crashes'
        else:
            crash_cmd = ''
        return crash_cmd

    def isAnr(self):
        if self.checkBox_isRushStop_All.isChecked():
            anr_cmd = ' --ignore-timeouts'
        else:
            anr_cmd = ''
        return anr_cmd

    def isApper(self):
        if self.checkBox_isAppErrStop_All.isChecked():
            apperr_cmd = ' --ignore-native-crashes'
        else:
            apperr_cmd = ''
        return apperr_cmd

    def isCerStop(self):
        if self.checkBox_isCerStop_All.isChecked():
            cer_cmd = ' --ignore-security-exceptions'
        else:
            cer_cmd = ''
        return cer_cmd

    def all_kill(self):
        if self.checkBox_kill_all.isChecked():
            kill_cmd = ' --kill-process-after-error'
        else:
            kill_cmd = ''
        return kill_cmd

    def black_or_white_list(self):
        white_or_black_cmd = ''
        if self.checkBox.isChecked():#Black list
            white_or_black_cmd = ' --pkg-blacklist-file /sdcard/MonkeyBlackList.txt'
        if self.checkBox_2.isChecked():# white list
            white_or_black_cmd = ' --pkg-whitelist-file /sdcard/MonkeyWhiteList.txt'
        return white_or_black_cmd


    def monkeyDelay(self):
        delay_time = self.lineEdit_dely_all.text().strip()
        if delay_time:
            if delay_time == '0':
                delay_cmd = ' --randomize-throttle'
            else:
                delay_cmd = ' --throttle %s' % (delay_time)
        else:
            delay_cmd = ''
        return delay_cmd

    def compose_cmd(self):
        monkey_par = self.save_settings()
        print('monkey_par:', monkey_par)
        seed = monkey_par[0]
        if seed.strip() == '0' or not seed.strip():
            seed_par = ''
        else:
            seed_par = ' -s %s' % (seed)

        flip = monkey_par[1]
        if flip.strip() == '0' or not flip.strip():
            flip_par = ''
        else:
            flip_par = ' --pct-flip %s' % (flip)

        touch = monkey_par[2]
        if touch.strip() == '0' or not touch.strip():
            touch_par = ''
        else:
            touch_par = ' --pct-touch %s' % (touch)

        motion = monkey_par[3]
        if motion.strip() == '0' or not motion.strip():
            motion_par = ''
        else:
            motion_par = ' --pct-motion %s' % (motion)

        trabal = monkey_par[4]
        if trabal.strip() == '0' or not trabal.strip():
            trabal_par = ''
        else:
            trabal_par = ' --pct-trackball %s' % (trabal)

        nav = monkey_par[5]
        if nav.strip() == '0' or not nav.strip():
            nav_par = ''
        else:
            nav_par = ' --pct-nav %s' % (nav)

        maj = monkey_par[6]
        if maj.strip() == '0' or not maj.strip():
            maj_par = ''
        else:
            maj_par = ' --pct-majornav %s' % (maj)

        keys = monkey_par[7]
        if keys.strip() == '0' or not keys.strip():
            keys_par = ''
        else:
            keys_par = ' --pct-syskeys %s' % (keys)

        app = monkey_par[8]
        if app.strip() == '0' or not app.strip():
            app_par = ''
        else:
            app_par = ' --pct-appswitch %s' % (app)

        anyevent = monkey_par[9]
        if anyevent.strip() == '0' or not anyevent.strip():
            anyevent_par = ''
        else:
            anyevent_par = ' --pct-anyevent %s' % (anyevent)

        # .get times
        times = self.lineEdit_time_all.text().strip()
        pattern = r'[0-9]'
        match = re.match(pattern, times)
        # print('match:', match)
        if match:
            delay_time = self.lineEdit_dely_all.text()
            pattern = r'[0-9]{1, 5}'
            monkey_cmd = 'monkey{0}{1}{2}{3}{4}{5}{6}{7}{8}{9}{10}{11}{12}{13}{14}{15}{16}{17} {18}'.format(
                self.log_level(),
                self.monkeyDelay(),
                self.isRush(),
                self.isAnr(),
                self.isApper(),
                self.isCerStop(),
                self.black_or_white_list(),
                self.all_kill(),
                seed_par,
                flip_par,
                touch_par,
                motion_par,
                trabal_par,
                nav_par,
                maj_par,
                keys_par,
                app_par,
                anyevent_par,
                times
            )
            return monkey_cmd
        else:
            tk.messagebox.showerror('ERROR', '请输入正确的测试次数！')
            return


    def check_monkey_is_running(self):
        unTestList = []
        for test_id in test_deviceID:
            monkey_ps = subprocess.Popen('adb -s {0} shell ps | findstr monkey'.format(test_id),
                                             shell=True, stdout=subprocess.PIPE)
            ps_id = monkey_ps.stdout.readline()
            if ps_id == b'':
                unTestList.append(test_id)
        return unTestList


    # 点击开始调用的方法
    def start_monkey_test(self):
        print('start_monkey_test')

        canDevicesID = self.check_monkey_is_running()
        if canDevicesID:
            result = tk.messagebox.askquestion('提示', 'Monkey 测试开始，请勿关闭该程序！')
            if result == 'yes':
                monkey_cmd = self.compose_cmd()
                # print('start_monkey_test:', monkey_cmd)
                nowTime = datetime.datetime.now().strftime('%Y%m%d_%H%M%S')  # 当前时间
                global start_test_time
                start_test_time = datetime.datetime.now()
                print('MonkeyRunThread')
                # print('cmd : {0} '.format(monkey_cmd))
                for serialno in canDevicesID:
                    ter_log = './TERLOG/%s_%s.txt' % (serialno, nowTime)
                    end_run_monkey_cmd = monkey_cmd + ' >' + ter_log
                    threadName = 'threadName_%d' % test_deviceID.index(serialno)
                    threadName = MonkeyRunThread(serialno, serialno, end_run_monkey_cmd)  # 调用开始运行 monkey 测试
                    threadName.start()
                    print('thread name===', threadName)

                    sleep(1)
                    # self.analysis_log(serialno, ter_log)
                    while not os.path.exists(ter_log):
                        sleep(1)
                    thread_analy_log = '%s_%s' % ('thread_analy_log', serialno)
                    thread_analy_log = AnalysisLogThread(serialno, ter_log, end_run_monkey_cmd, monkey_cmd)
                    thread_analy_log.start()
                print('Pull log thread!')
                thread_pull_log1 = threading.Thread(target=self.pull_mtk_log, name='pull_mtk_log')
                thread_pull_log1.start()
        else:
            tk.messagebox.showerror('ERROR', '请确定 Devices！或者查看设备是否已经在测试中！')



        # global test_deviceID
        # threadList = []
        # if test_deviceID:
        #     result = tk.messagebox.askquestion('提示', 'Monkey 测试开始，请勿关闭该程序！')
        #     if result == 'yes':
        #         monkey_cmd = self.compose_cmd()
        #         # print('start_monkey_test:', monkey_cmd)
        #         nowTime = datetime.datetime.now().strftime('%Y%m%d_%H%M%S')  # 当前时间
        #         global start_test_time
        #         start_test_time = datetime.datetime.now()
        #         print('MonkeyRunThread')
        #         # print('cmd : {0} '.format(monkey_cmd))
        #         for serialno in test_deviceID:
        #             ter_log = './TERLOG/%s_%s.txt' % (serialno, nowTime)
        #             end_run_monkey_cmd = monkey_cmd + ' >' + ter_log
        #             threadName = 'threadName_%d' % test_deviceID.index(serialno)
        #             threadName = MonkeyRunThread(serialno, serialno, end_run_monkey_cmd)  # 调用开始运行 monkey 测试
        #             threadName.start()
        #             print('thread name===', threadName)
        #             threadList.append(threadName)
        #
        #             sleep(1)
        #             # self.analysis_log(serialno, ter_log)
        #             while not os.path.exists(ter_log):
        #                 sleep(1)
        #             thread_analy_log = '%s_%s' % ('thread_analy_log', serialno)
        #             thread_analy_log = AnalysisLogThread(serialno, ter_log, end_run_monkey_cmd, monkey_cmd)
        #             thread_analy_log.start()
        #         print('Pull log thread!')
        #         thread_pull_log1 = threading.Thread(target=self.pull_mtk_log, name='pull_mtk_log')
        #         thread_pull_log1.start()
        #
        #     else:
        #         tk.messagebox.showerror('ERROR', '请确定 Devices！')


    def pull_mtk_log(self):
        logNg = 'LoNg.jar'
        createTime = datetime.datetime.now().strftime('%Y%m%d_%H%M')  # 当前时间
        logpath = Path.cwd()
        print('log path ==', logpath)
        cc = subprocess.Popen('java -jar {0} start'.format(logNg), shell=True, stdout=subprocess.PIPE,
                              stderr=subprocess.PIPE, close_fds=True).communicate()


    def timestart(self):
        if self.isTimeStart:
            self.timeClock.setHMS(0, 0, 0, 0)
            self.timer.start(1000)

    def addtime(self):
        self.timeClock = self.timeClock.addMSecs(1000)  # 时间增加一秒
        currentTime = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        self.lineEdit_time.setText(currentTime)#self.timeClock.toString("hh:mm:ss"))  # 标签显示时间

    def closeEvent(self, event):  # 关闭窗口触发以下事件
        reply = tk.messagebox.askquestion('WARNING ！！！', '请确定 Monkey 测试已经测试完成，或没有设备正在测试！')
        if reply =='yes':
            event.accept() #接受关闭事件
        else:
            event.ignore() #忽略关闭事件

class AnalysisLogThread(threading.Thread):
    def __init__(self, serialno, logpathName, cmd, monkey_cmd):
        super(AnalysisLogThread, self).__init__()
        self.serialno = serialno
        self.logpathName = logpathName
        self.buffSize = -1
        self.cmd = cmd
        self.monkey_cmd = monkey_cmd

    def run(self):
        isSize = (os.path.getsize(self.logpathName))
        isMonkeyComplete = False
        startTime = datetime.datetime.now().strftime('%Y%m%d%H%M%S')  # 当前时间
        fp = open(self.logpathName, 'r')
        isShowInfo = True
        print('\n\n')
        print(' %s testing start!' % (self.serialno))

        while not isMonkeyComplete:#isSize != self.buffSize:
            sleep(5)
            monkey_ps = subprocess.Popen('adb -s {0} shell ps | findstr monkey'.format(self.serialno), shell=True,
                                             stdout=subprocess.PIPE)
            ps_id = monkey_ps.stdout.readline()
            current_time = datetime.datetime.now()  # 当前时间
            duration = ((current_time) - (start_test_time)).seconds
            h = int(duration / 3600)
            m = int((duration - (h * 3600)) / 60)
            s = duration % 60
            monkey_ps.terminate()

            print('%s Monkey test duration--%d H: %d M: %d S --monkey process info:' % (self.serialno, h, m, s), ps_id)
            if ps_id == b'':
                try:
                    readLine = fp.readlines()
                except:
                    print('文件错误！')

                ExceptionTime = datetime.datetime.now()

                for i in range((len(readLine) - 5), len(readLine)):
                    if len(readLine) > 5:
                        if re.search(r'Monkey finished', readLine[i], re.I):
                            print('%s complete' % (self.serialno))
                            fp.close()
                            isMonkeyComplete = True
                            print('%s is closed' % (self.logpathName))
                            print('Monkey test has been completed! ')
                            break
                if not isMonkeyComplete:
                    fp.close()

                    while True:
                        pipe = subprocess.Popen('adb devices', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True, close_fds=True)
                        devices = re.findall(r'\s(\S+)\tdevice', pipe.stdout.read().decode('utf-8'))
                        pipe.terminate()

                        if self.serialno in devices:
                            if self.serialno in man_stop:
                                print('%s Monkey test has been manually stopped' % self.serialno)
                                man_stop.remove(self.serialno)
                                isMonkeyComplete = True
                                break
                            else:
                                print('Restart monkey in the devices {}'.format(self.serialno))
                                print('The device is already ok! Monkey test Restarting... ')
                                nowTime = datetime.datetime.now().strftime('%Y%m%d_%H%M%S')  # 当前时间
                                ter_log = './TERLOG/%s_%s.txt' % (self.serialno, nowTime)
                                end_run_monkey_cmd = self.monkey_cmd + ' >' + ter_log
                                # print('Restarting cmd : start /b adb -s {0} shell {1}'.format(self.serialno, end_run_monkey_cmd))
                                thread_monkey = subprocess.Popen('start /b adb -s {0} shell {1} '.format(self.serialno, end_run_monkey_cmd),
                                                                 shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE, close_fds=True)
                                sleep(5)
                                fp = open(ter_log, 'r')
                                print('Restarting ok!\n{0} testing...'.format(self.serialno))
                                thread_monkey.terminate()
                                break
                        else:

                            pauseTime = datetime.datetime.now()
                            duration = ((pauseTime) - (ExceptionTime)).seconds
                            h = int(duration / 3600)
                            m = int((duration - (h * 3600)) / 60)
                            s = duration % 60
                            print('{0} USB disconnected or mobile offline, waiting devices ...{1}H:{2}M:{3}S'
                                  .format(self.serialno, h, m, s))
                            # print('{0} the device is disconnected for a long time and will stop testing.'.format(self.serialno))
                            sleep(10)



class MonkeyRunThread(threading.Thread):
    def __init__(self, threadName, serialno, cmd):
        super(MonkeyRunThread, self).__init__()
        self.threadName = threadName
        self.serialno = serialno
        self.cmd = cmd

    def run(self):
        print('MonkeyRunThread')
        thread_monkey = subprocess.Popen('start /b adb -s {0} shell {1} '.format(self.serialno, self.cmd), shell=True,
                                     stdout=subprocess.PIPE, stderr=subprocess.PIPE, close_fds=True).communicate()
        thread_monkey.terminate()

if __name__ == "__main__":
    app = QtWidgets.QApplication(sys.argv)  # 首先必须实例化QApplication类，作为GUI主程序入口
    if check_env():
        print('Launch application')
        MainWindow = MainWindow()  # 实例化MainWindow类，创建自带menu的窗体类型QMainWindow
        MainWindow.setFixedSize(648, 530)  # 禁止改变窗口大小
        MainWindow.show()  # 显示窗体
        sys.exit(app.exec_())  # 当来自操作系统的分发事件指派调用窗口时，
    else:
        sys.exit(app.exec_())