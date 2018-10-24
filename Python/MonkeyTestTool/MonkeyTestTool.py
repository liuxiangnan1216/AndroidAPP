#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2018/10/23 16:31
# @Author  : LiuXiangNan
# @Site    : 
# @File    : MonkeyTestTool.py


import sys
from MonkeyTestUI import *
import tkinter.messagebox
import tkinter as tk
import os
import re
from tkinter import filedialog
import tkinter
from PyQt5.QtCore import QStringListModel
from PyQt5 import QtWidgets
import subprocess
import datetime

root = tk.Tk()
root.withdraw()
black_list = []
global black_selected
isClickedBlackList = False
isClickedList = False
global isClickedGetDeviceID
isClickedGetDeviceID = False

global test_deviceID
test_deviceID = []
global devices_id

global setting_par

global black_list_path


global log_path_all
global log_path_sing

class MainWindow(QtWidgets.QMainWindow, Ui_MainWindow):
    def __init__(self, parent=None):
        super(MainWindow, self).__init__(parent)
        self.setupUi(self)

        #################################################################################################################
        self.pushButton_help.clicked.connect(self.softHelp)
        self.pushButton_about.clicked.connect(self.softAbout)
        self.monkeyHelp()
        self.pushButton_getPackName.clicked.connect(self.get_devices_pkgName)
        self.get_devices_pkgName()
        self.pushButton_add.clicked.connect(self.add_black_list)
        self.pushButton_rm.clicked.connect(self.dele_black_list)
        self.checkBox_isoutputLog_All.click()
        self.checkBox_isOutputLog_Single.click()
        self.pushButton_savepar.clicked.connect(self.save_settings)
        self.pushButton_resetpar.clicked.connect(self.reset_settings)
        self.toolButton_log_all.clicked.connect(self.select_path_all)
        self.toolButton_log_single.clicked.connect(self.select_path_single)
        self.pushButton_start_All.clicked.connect(self.start_monkey_test)
        self.pushButton_StartTest_Single.clicked.connect(self.start_single_monkey_test)
        self.pushButton_getDevicesID.clicked.connect(self.get_devicesID)
        self.pushButton_saveBlacklist.clicked.connect(self.save_black_list)
        global black_list_path
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

    def softHelp(self):
        tkinter.messagebox.showinfo('Help', ' 该工具用于封装 Android Monkey 测试命令！'
                                            '\n 简化测试人员对 Monkey 命令的使用'
                                            '\n Monkey 测试帮助请查看 Description 页面。')

    def softAbout(self):
        tkinter.messagebox.showinfo('About', ' 仅供内部使用，未经授权请勿传播!'
                                             '\n '
                                             '\n Author  :  '
                                             '\n Mail  :  '
                                             '\n Org   :   '
                                             '\n Team  :  Automation '
                                             '\n Version :  V001 '
                                             '\n Date : 2018-10-15')

    def get_devices(self):
        pipe = subprocess.Popen('adb devices', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)

        if not pipe.stderr:
            print('No adb found,please install adb! Abort test')
            return False
        devices = re.findall(r'\s(\S+)\tdevice', pipe.stdout.read().decode('utf-8'))
        return devices

    def check_devices(self):
        devices = self.get_devices
        if not devices:
            tk.messagebox.showerror('ERROR', 'No dvices found!')
            return

    def stop_monkey(self, kill_devices_id):
        print('stop monkey id====', kill_devices_id)
        if kill_devices_id:
            monkey_ps = subprocess.Popen('adb -s {0} shell procrank | findstr monkey'.format(kill_devices_id),
                                         stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
            ps_id = monkey_ps.stdout.readline()
            print('ps_id====', ps_id)
            if ps_id != b'':
                (pid, str_other) = ps_id.decode('utf-8').split('   ', 1)
                print('pid====', pid)
                kill_monkey = subprocess.Popen('adb -s {0} shell kill {1}'.format(kill_devices_id, pid),
                                               stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
                tk.messagebox.showinfo('INFO', '{0} Monkey 测试已停止！'.format(kill_devices_id))
            else:
                tk.messagebox.showinfo('INFO', 'Permission denied 请手动停止！')
        else:
            tk.messagebox.showinfo('INFO', 'Device 不存在！')

    def save_black_list(self):
        global black_list
        global black_list_path
        global test_deviceID
        black_list_path = self.lineEdit_black_list_path.text()
        print('black_list_path===', black_list_path)

        if not black_list_path:
            tk.messagebox.showerror('ERROR', '请输入 Black List 保存路径！')
        else:
            blacklistFile = open('MonkeyTestBlackList.txt', 'w')
            for i in range(0, len(black_list)):
                blacklistFile.write(black_list[i])
                blacklistFile.write('\n')
            blacklistFile.flush()

        isEx = (os.path.getsize('MonkeyTestBlackList.txt'))
        print('isEx===', isEx)
        if isEx > 0:
            if len(test_deviceID) > 0:
                get_device_path = self.lineEdit_black_list_path.text()
                for serialno in test_deviceID:
                    pushBlackList = subprocess.Popen(
                        'adb -s {0} push {1} {2} '.format(serialno, 'MonkeyTestBlackList.txt', get_device_path),
                        stdout=subprocess.PIPE,
                        stderr=subprocess.PIPE, shell=True).communicate()
                    print('pushBlackList=type==', pushBlackList)
                    print('pushBlackList=1===', pushBlackList[1])
                    if re.findall(r'Permission denied', str(pushBlackList[1])):
                        tk.messagebox.showerror('ERROR', ' Permission denied ，请手动添加进 device！并在此输入路径')
            else:
                tk.messagebox.showerror('ERROR', 'Device 未选择确认！')
        else:
            tk.messagebox.showerror('ERROR', 'Black List 文件为空请确认是否已添加！')

    def monkeyHelp(self):
        fp = open('MonkeytestHelp.txt', "r", encoding='UTF-8')
        for line in fp.readlines():
            self.textBrowser_help.append(line)

    def get_devices_pkgName(self):
        # adb shell pm list packages -f
        pkg_devices = subprocess.Popen('adb devices', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
        if not pkg_devices.stderr:
            tk.messagebox.showerror('ERROR', 'No adb found,please install adb!')
        devices_id = re.findall(r'\s(\S+)\tdevice', pkg_devices.stdout.read().decode('utf-8'))
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
            # 向listview中赋值
            slm = QStringListModel()
            self.qList = pkg_name_list
            slm.setStringList(pkg_name_list)
            self.listView_app.setModel(slm)
            # 选择
            self.listView_app.clicked.connect(self.list_clicked)

    def list_clicked(self, qModelIndex):
        global select_pkg
        select_pkg = self.qList[qModelIndex.row()]
        global isClickedList
        isClickedList = True

    def add_black_list(self):
        global select_pkg
        global black_list
        global isClickedList
        if isClickedList:
            count = black_list.count(select_pkg)
            if count == 0:
                black_list.append(select_pkg)
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
                print('black list remove===', black_list)
                isClickedBlackList = False
        else:
            tk.messagebox.showinfo('提示', '请选择要移除的包名！')
        # 向listview中赋值
        slm = QStringListModel()
        # self.addList = black_list
        slm.setStringList(black_list)
        self.listView_black.setModel(slm)

    def clicked_black_list(self, qModelIndex):
        global black_selected
        black_selected = self.addList[qModelIndex.row()]
        global isClickedBlackList
        isClickedBlackList = True

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
        print('setting_par===', setting_par)
        return setting_par

    def select_path_all(self):
        global log_path_all
        log_path_all = filedialog.askdirectory()
        self.lineEdit_log_all.setText(log_path_all)

        # androidVersion = self.comboBox_android_All.currentText()#获取android 版
        # print('android version==' + androidVersion)

    def all_log_level(self):
        log_level_all = self.comboBox_log_level_all.currentText()
        if log_level_all == 'Level_0':
            log_level_all_cmd = ''
        elif log_level_all == 'Level_1':
            log_level_all_cmd = ' -v'
        elif log_level_all == 'Level_2':
            log_level_all_cmd = ' -v-v'
        elif log_level_all == 'Level_3':
            log_level_all_cmd = ' -v-v-v'
        return log_level_all_cmd

    def all_isRush(self):
        if self.checkBox_isRushStop_All.isChecked():
            crash_all_cmd = ' --ignore-crashes'
        else:
            crash_all_cmd = ''
        return crash_all_cmd

    def all_is_anr(self):
        if self.checkBox_isAnrStop_All.isChecked():
            anr_all_cmd = ' --ignore-timeouts'
        else:
            anr_all_cmd = ''
        return anr_all_cmd

    def all_is_apperr(self):
        if self.checkBox_isAppErrStop_All.isChecked():
            apperr_all_cmd = ' --ignore-native-crashes'
        else:
            apperr_all_cmd = ''
        return apperr_all_cmd

    def all_cer_stop(self):
        if self.checkBox_isCerStop_All.isChecked():
            cer_all_cmd = ' --ignore-security-exceptions'
        else:
            cer_all_cmd = ''
        return cer_all_cmd

    def all_kill(self):
        if self.checkBox_kill_all.isChecked():
            kill_all_cmd = ' --kill-process-after-error'
        else:
            kill_all_cmd = ''
        return kill_all_cmd

    def all_black_list(self):
        # --pkg-blacklist-file PACKAGE_BLACKLIST_FILE
        black_list_path = self.lineEdit_black_list_path.text()
        black_list_all_cmd = ''
        if self.checkBox.isChecked():
            if black_list_path:
                black_list_all_cmd = ' --pkg-blacklist-file %s/MonkeyTestBlackList.txt' % (black_list_path)
            else:
                tk.messagebox.showerror('ERROR', 'Black List 路径错误,请在 "Black List" 页面配置 BlackList ！')

        return black_list_all_cmd

    def all_delay(self):
        delay_time = self.lineEdit_dely_all.text().strip()
        if delay_time:
            if delay_time == '0':
                delay_cmd_all = ' --randomize-throttle'
            else:
                delay_cmd_all = ' --throttle %s' % (delay_time)
        else:
            delay_cmd_all = ''
        return delay_cmd_all

    def run_monkey_all(self, serialno):
        # .check devices
        # check_devices(self)

        # get setting
        monkey_par = self.save_settings()
        print('monkey_par==', monkey_par)
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
        print('match==', match)
        if match:
            delay_time = self.lineEdit_dely_all.text()
            pattern = r'[0-9]{1, 5}'
            all_monkey_cmd = 'monkey{0}{1}{2}{3}{4}{5}{6}{7}{8}{9}{10}{11}{12}{13}{14}{15}{16}{17} {18}'.format(
                self.all_log_level(),
                self.all_delay(),
                self.all_isRush(),
                self.all_is_anr(),
                self.all_is_apperr(),
                self.all_cer_stop(),
                self.all_black_list(),
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
            # print('run_cmd=====', all_monkey_cmd)

            if self.checkBox_isoutputLog_All.isChecked():
                get_log_path_name = self.lineEdit_log_all.text().strip()
                nowTime = datetime.datetime.now().strftime('%Y%m%d_%H%M%S')  # 当前时间
                if get_log_path_name:
                    log_path_all_cmd = '%s/all_app_%s_%s.txt' % (get_log_path_name, serialno, nowTime)
                    run_all_monkey_cmd = all_monkey_cmd + ' > ' + log_path_all_cmd
                    print('run_all_monkey_cmd=====================================', run_all_monkey_cmd)
                    return run_all_monkey_cmd

                else:
                    tk.messagebox.showerror('ERROR', '请输入有效的 Log 输出路径和名称！')

            else:
                print('monkey cmd===', all_monkey_cmd)
                return all_monkey_cmd
                # pipe = subprocess.Popen(all_monkey_cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True).communicate()
        else:
            tk.messagebox.showerror('ERROR', '请输入正确的测试次数！')

    def end_monkey_cmd(self, serialno):
        end_run_monkey_cmd = self.run_monkey_all(serialno)
        thread_monkey = subprocess.Popen('start /b adb -s {0} shell {1} '.format(serialno, end_run_monkey_cmd),
                                         shell=True)

    def start_monkey_test(self):
        global test_deviceID
        if test_deviceID:
            result = tk.messagebox.askquestion('提示', 'Monkey 测试开始，请勿关闭该程序！')
            print('result=====', result)
            print('start_monkey_test===test_deviceID==', test_deviceID)
            if result == 'yes':
                for serialno in test_deviceID:
                    print('serialno===', serialno)
                    self.end_monkey_cmd(serialno)
                    # monkey_ps = subprocess.Popen('adb -s {0} shell ps | findstr monkey'.format(serialno), shell=True, stdout=subprocess.PIPE)
                    # print('test===','adb -s {0} shell ps | findstr monkey'.format(serialno))
                    # IsMonkeyRun = False
                    # while True:
                    #     ps_id = monkey_ps.stdout.readline()
                    #     if ps_id != b'':
                    #         IsMonkeyRun = True
                    #         #
                    #     else:
        else:
            tk.messagebox.showerror('ERROR', '请确定 Devices！')

        ############################################## 单包测试 #############################################################

    def select_path_single(self):
        global log_path_sing
        log_path_sing = filedialog.askdirectory()
        self.lineEdit_log_Single.setText(log_path_sing)

    def single_log_level(self):
        log_level_single = self.comboBox_logleve_Single.currentText()
        if log_level_single == 'Level_0':
            log_level_single_cmd = ''
        elif log_level_single == 'Level_1':
            log_level_single_cmd = ' -v'
        elif log_level_single == 'Level_2':
            log_level_single_cmd = ' -v-v'
        elif log_level_single == 'Level_3':
            log_level_single_cmd = ' -v-v-v'
        return log_level_single_cmd

    def single_isRush(self):
        if self.checkBox_isRushStop_Single.isChecked():
            crash_single_cmd = ' --ignore-crashes'
        else:
            crash_single_cmd = ''
        return crash_single_cmd

    def single_is_anr(self):
        if self.checkBox_isAnrStop_Single.isChecked():
            anr_single_cmd = ' --ignore-timeouts'
        else:
            anr_single_cmd = ''
        return anr_single_cmd

    def single_is_apperr(self):
        if self.checkBox_isAppErrStop_Single.isChecked():
            apperr_single_cmd = ' --ignore-native-crashes'
        else:
            apperr_single_cmd = ''
        return apperr_single_cmd

    def single_cer_stop(self):
        if self.checkBox_isCerStop_Single.isChecked():
            cer_single_cmd = ' --ignore-security-exceptions'
        else:
            cer_single_cmd = ''
        return cer_single_cmd

    def single_kill(self):
        if self.checkBox_kill_single.isChecked():
            kill_single_cmd = ' --kill-process-after-error'
        else:
            kill_single_cmd = ''
        return kill_single_cmd

    def single_delay(self):
        delay_time = self.lineEdit_dely_Single.text().strip()
        if delay_time:
            if delay_time == '0':
                delay_cmd_single = ' --randomize-throttle'
            else:
                delay_cmd_single = ' --throttle %s' % (delay_time)
        else:
            delay_cmd_single = ''
        return delay_cmd_single

    def run_monkey_single(self, serialno):
        # .check devices
        # check_devices(self)

        # get setting
        monkey_par = self.save_settings()
        print('monkey_par==', monkey_par)
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

        pkg_name = self.comboBox_testName_Single.currentText()
        if pkg_name:
            run_pkg_name = ' -p ' + pkg_name
            times = self.lineEdit_times_Single.text().strip()
            pattern = r'[0-9]'
            match = re.match(pattern, times)
            print('match==', match)
            if match:
                delay_time = self.lineEdit_dely_Single.text()
                pattern = r'[0-9]{1, 5}'
                single_monkey_cmd = 'monkey{0}{1}{2}{3}{4}{5}{6}{7}{8}{9}{10}{11}{12}{13}{14}{15}{16} {18}'.format(
                    run_pkg_name,
                    self.single_log_level(),
                    self.single_delay(),
                    self.single_isRush(),
                    self.single_is_anr(),
                    self.single_is_apperr(),
                    self.single_cer_stop(),
                    self.single_kill(),
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
                # print('run_cmd=====', all_monkey_cmd)

                if self.checkBox_isOutputLog_Single.isChecked():
                    get_log_path_name = self.lineEdit_log_Single.text().strip()
                    nowTime = datetime.datetime.now().strftime('%Y%m%d_%H%M%S')  # 当前时间

                    if get_log_path_name:
                        log_path_single_cmd = '%s/%s_%s_%s.txt' % (get_log_path_name, pkg_name, serialno, nowTime)
                        run_single_monkey_cmd = single_monkey_cmd + ' > ' + log_path_single_cmd
                        print('monkey cmd===', run_single_monkey_cmd)
                        return run_single_monkey_cmd
                        # pipe = subprocess.Popen(run_single_monkey_cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True).communicate()
                    else:
                        tk.messagebox.showerror('ERROR', '请输入有效的 Log 输出路径和名称！')

                else:
                    print('monkey cmd===', single_monkey_cmd)
                    return single_monkey_cmd
                    # pipe = subprocess.Popen(single_monkey_cmd, stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                    #                         shell=True).communicate()
            else:
                tk.messagebox.showerror('ERROR', '请输入正确的测试次数！')
        else:
            tk.messagebox.showerror('ERROR', '测试应用包名必须填入！')

    def end_monkey_single_cmd(self, serialno):
        end_run_monkey_cmd = self.run_monkey_single(serialno)
        thread_monkey = subprocess.Popen('start /b adb -s {0} shell {1} '.format(serialno, end_run_monkey_cmd),
                                         shell=True)

    def start_single_monkey_test(self):
        global test_deviceID
        if test_deviceID:

            print('start_monkey_test===test_deviceID==', test_deviceID)
            for serialno in test_deviceID:
                print('serialno===', serialno)
                self.end_monkey_single_cmd(serialno)
                # monkey_ps = subprocess.Popen('adb -s {0} shell ps | findstr monkey'.format(serialno), shell=True, stdout=subprocess.PIPE)
                # print('test===','adb -s {0} shell ps | findstr monkey'.format(serialno))
                # IsMonkeyRun = False
                # while True:
                #     ps_id = monkey_ps.stdout.readline()
                #     if ps_id != b'':
                #         IsMonkeyRun = True
                #         #
                #     else:
        else:
            tk.messagebox.showerror('ERROR', '请确定 Devices！')

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
        print('devicesID====', devicesID)
        print(type(devicesID))
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

            if len(devices_id) == 2 and devices_id[1]:
                self.lineEdit_device2.setText(devices_id[1])

            if len(devices_id) == 3 and devices_id[2]:
                self.lineEdit_device3.setText(devices_id[2])

            if len(devices_id) == 4 and devices_id[3]:
                self.lineEdit_device4.setText(devices_id[3])

            if len(devices_id) == 5 and devices_id[4]:
                self.lineEdit_device5.setText(devices_id[4])

            if len(devices_id) == 6 and devices_id[5]:
                self.lineEdit_device6.setText(devices_id[5])

            if len(devices_id) == 7 and devices_id[6]:
                self.lineEdit_device7.setText(devices_id[6])

            if len(devices_id) == 8 and devices_id[7]:
                self.lineEdit_device8.setText(devices_id[7])

            if len(devices_id) == 9 and devices_id[8]:
                self.lineEdit_device9.setText(devices_id[8])

            if len(devices_id) == 10 and devices_id[9]:
                self.lineEdit_device10.setText(devices_id[9])
            global isClickedGetDeviceID
            isClickedGetDeviceID = True

    def save_deviceID(self):
        global test_deviceID
        # test_deviceID = []
        global isClickedGetDeviceID
        if isClickedGetDeviceID:
            if self.checkBox_device1.isChecked() and len(devices_id) >= 1:
                test_deviceID.append(devices_id[0])

            if self.checkBox_device2.isChecked() and len(devices_id) >= 2:
                test_deviceID.append(devices_id[1])

            if self.checkBox_device3.isChecked() and len(devices_id) >= 3:
                test_deviceID.append(devices_id[2])

            if self.checkBox_device4.isChecked() and len(devices_id) >= 4:
                test_deviceID.append(devices_id[3])

            if self.checkBox_device5.isChecked() and len(devices_id) >= 5:
                test_deviceID.append(devices_id[4])

            if self.checkBox_device6.isChecked() and len(devices_id) >= 6:
                test_deviceID.append(devices_id[5])

            if self.checkBox_device7.isChecked() and len(devices_id) >= 7:
                test_deviceID.append(devices_id[6])

            if self.checkBox_device8.isChecked() and len(devices_id) >= 8:
                test_deviceID.append(devices_id[7])

            if self.checkBox_device9.isChecked() and len(devices_id) >= 9:
                test_deviceID.append(devices_id[8])

            if self.checkBox_device10.isChecked() and len(devices_id) == 10:
                test_deviceID.append(devices_id[9])

            str1 = []
            for i in range(0, len(test_deviceID)):
                str1.append(test_deviceID[i])
                str1.append(',')
            tkinter.messagebox.showinfo('对以下 Device 进行测试', str1)


if __name__ == "__main__":
    app = QtWidgets.QApplication(sys.argv)  # 首先必须实例化QApplication类，作为GUI主程序入口
    MainWindow = MainWindow()  # 实例化MainWindow类，创建自带menu的窗体类型QMainWindow
    MainWindow.setFixedSize(620, 540)  # 禁止改变窗口大小
    MainWindow.show()  # 显示窗体
    sys.exit(app.exec_())  # 当来自操作系统的分发事件指派调用窗口时，
