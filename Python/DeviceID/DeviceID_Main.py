#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2018/9/25 18:08
# @Author  : LiuXiangNan
# @Site    : 
# @File    : DeviceID_Main.py
import os
import re
import tkinter as tk
from tkinter import filedialog
import tkinter
import tkinter.messagebox

import unix as unix
from PyQt5 import QtCore, QtGui, QtWidgets


root = tk.Tk()
root.withdraw()

global dir_pathName
dir_pathName = '/'
windowColor = "background-color: rgb(198, 226, 255)"
color = "background-color: rgb(224, 255, 255)"
whitecolor = "background-color: rgb(255, 255, 255)"

inputFont = QtGui.QFont()
inputFont.setPointSize(16)

class Ui_DeviceID(object):
    def setupUi(self, DeviceID):
        DeviceID.setObjectName("DeviceID")
        DeviceID.resize(819, 542)
        font = QtGui.QFont()
        font.setFamily("Arial")
        font.setPointSize(12)
        DeviceID.setFont(font)
        DeviceID.setCursor(QtGui.QCursor(QtCore.Qt.ArrowCursor))
        DeviceID.setStyleSheet(windowColor)
        self.centralwidget = QtWidgets.QWidget(DeviceID)
        self.centralwidget.setObjectName("centralwidget")
        self.label_tac = QtWidgets.QLabel(self.centralwidget)
        self.label_tac.setGeometry(QtCore.QRect(50, 110, 251, 31))
        font = QtGui.QFont()
        font.setFamily("Arial Black")
        font.setPointSize(14)
        font.setBold(True)
        font.setWeight(75)
        self.label_tac.setFont(font)
        self.label_tac.setObjectName("label_tac")
        # self.label_tac.setStyleSheet(whitecolor)
        self.label_message = QtWidgets.QLabel(self.centralwidget)
        self.label_message.setGeometry(QtCore.QRect(50, 50, 251, 31))
        font = QtGui.QFont()
        font.setFamily("Arial Black")
        font.setPointSize(14)
        font.setBold(True)
        font.setWeight(75)
        self.label_message.setFont(font)
        self.label_message.setObjectName("label_message")
        self.label_sn_pro = QtWidgets.QLabel(self.centralwidget)
        self.label_sn_pro.setGeometry(QtCore.QRect(50, 170, 251, 31))
        font = QtGui.QFont()
        font.setFamily("Arial Black")
        font.setPointSize(14)
        font.setBold(True)
        font.setWeight(75)
        self.label_sn_pro.setFont(font)
        self.label_sn_pro.setObjectName("label_sn_pro")
        self.label_check = QtWidgets.QLabel(self.centralwidget)
        self.label_check.setGeometry(QtCore.QRect(50, 240, 251, 21))
        font = QtGui.QFont()
        font.setFamily("Arial Black")
        font.setPointSize(14)
        font.setBold(True)
        font.setWeight(75)
        self.label_check.setFont(font)
        self.label_check.setObjectName("label_check")
        self.label_sn_end = QtWidgets.QLabel(self.centralwidget)
        self.label_sn_end.setGeometry(QtCore.QRect(510, 170, 91, 31))
        font = QtGui.QFont()
        font.setFamily("Arial Black")
        font.setPointSize(14)
        font.setBold(True)
        font.setWeight(75)
        self.label_sn_end.setFont(font)
        self.label_sn_end.setObjectName("label_sn_end")
        self.pushButton_produce = QtWidgets.QPushButton(self.centralwidget)
        self.pushButton_produce.setGeometry(QtCore.QRect(20, 410, 771, 61))
        self.pushButton_produce.setStyleSheet(color)
        font = QtGui.QFont()
        font.setFamily("Arial Black")
        font.setPointSize(28)
        font.setBold(True)
        font.setWeight(75)
        self.pushButton_produce.setFont(font)
        self.pushButton_produce.setMouseTracking(False)
        self.pushButton_produce.setAutoFillBackground(True)
        self.pushButton_produce.setObjectName("pushButton_produce")
        self.label = QtWidgets.QLabel(self.centralwidget)
        self.label.setGeometry(QtCore.QRect(50, 10, 340, 21))
        font = QtGui.QFont()
        font.setFamily("Arial")
        font.setPointSize(14)
        self.label.setFont(font)
        self.label.setObjectName("label")
        self.pushButton_help = QtWidgets.QPushButton(self.centralwidget)
        self.pushButton_help.setGeometry(QtCore.QRect(580, 10, 75, 23))
        # self.pushButton_help.setIcon(QtGui.QIcon('./fb_red.png'))
        self.pushButton_help.setStyleSheet(color)
        font = QtGui.QFont()
        font.setPointSize(8)
        self.pushButton_help.setFont(font)
        self.pushButton_help.setObjectName("pushButton_help")
        self.pushButton_about = QtWidgets.QPushButton(self.centralwidget)
        self.pushButton_about.setGeometry(QtCore.QRect(680, 10, 75, 23))
        self.pushButton_about.setStyleSheet(color)
        font = QtGui.QFont()
        font.setPointSize(8)
        self.pushButton_about.setFont(font)
        self.pushButton_about.setObjectName("pushButton_about")
        self.lineEdit_message = QtWidgets.QLineEdit(self.centralwidget)
        self.lineEdit_message.setGeometry(QtCore.QRect(310, 50, 421, 31))
        self.lineEdit_message.setStyleSheet(whitecolor)
        self.lineEdit_message.setObjectName("lineEdit_message")
        self.lineEdit_message.setFont(inputFont)

        self.lineEdit_tac = QtWidgets.QLineEdit(self.centralwidget)
        self.lineEdit_tac.setGeometry(QtCore.QRect(310, 110, 421, 31))
        self.lineEdit_tac.setStyleSheet(whitecolor)
        self.lineEdit_tac.setFont(inputFont)
        self.lineEdit_tac.setObjectName("lineEdit_tac")
        self.lineEdit_tac.setMaxLength(8)
        self.lineEdit_tac.setValidator(QtGui.QRegExpValidator(QtCore.QRegExp('[0-9]{1,8} ')))

        self.lineEdit_sn_pro = QtWidgets.QLineEdit(self.centralwidget)
        self.lineEdit_sn_pro.setGeometry(QtCore.QRect(310, 170, 161, 31))
        self.lineEdit_sn_pro.setStyleSheet(whitecolor)
        self.lineEdit_sn_pro.setObjectName("lineEdit_sn_pro")
        self.lineEdit_sn_pro.setFont(inputFont)
        self.lineEdit_sn_pro.setMaxLength(6)
        self.lineEdit_sn_pro.setValidator(QtGui.QRegExpValidator(QtCore.QRegExp('[0-9]{1,6} ')))
        # self.lineEdit_sn_pro.setValidator(QValidator=)

        self.lineEdit_sn_end = QtWidgets.QLineEdit(self.centralwidget)
        self.lineEdit_sn_end.setGeometry(QtCore.QRect(600, 170, 131, 31))
        self.lineEdit_sn_end.setStyleSheet(whitecolor)
        self.lineEdit_sn_end.setObjectName("lineEdit_sn_end")
        self.lineEdit_sn_end.setFont(inputFont)
        self.lineEdit_sn_end.setMaxLength(6)
        self.lineEdit_sn_end.setValidator(QtGui.QRegExpValidator(QtCore.QRegExp('[0-9]{1,6} ')))

        self.checkBox = QtWidgets.QCheckBox(self.centralwidget)
        self.checkBox.setGeometry(QtCore.QRect(310, 240, 21, 21))
        font = QtGui.QFont()
        font.setFamily("Arial")
        font.setPointSize(14)
        self.checkBox.setFont(font)
        self.checkBox.setText("")
        self.checkBox.setObjectName("checkBox")
        self.label_3 = QtWidgets.QLabel(self.centralwidget)
        self.label_3.setGeometry(QtCore.QRect(740, 110, 21, 31))
        font = QtGui.QFont()
        font.setPointSize(24)
        self.label_3.setFont(font)
        self.label_3.setObjectName("label_3")

        self.label_4 = QtWidgets.QLabel(self.centralwidget)
        self.label_4.setGeometry(QtCore.QRect(480, 170, 21, 31))
        font = QtGui.QFont()
        font.setPointSize(24)
        self.label_4.setFont(font)
        self.label_4.setObjectName("label_4")

        self.label_5 = QtWidgets.QLabel(self.centralwidget)
        self.label_5.setGeometry(QtCore.QRect(740, 170, 21, 31))
        font = QtGui.QFont()
        font.setPointSize(24)
        self.label_5.setFont(font)
        self.label_5.setObjectName("label_5")

        self.lineEdit_path = QtWidgets.QLineEdit(self.centralwidget)
        self.lineEdit_path.setGeometry(QtCore.QRect(210, 350, 521, 31))
        self.lineEdit_path.setStyleSheet(whitecolor)
        self.lineEdit_path.setObjectName("lineEdit_path")
        self.lineEdit_path.setFont(inputFont)
        self.lineEdit_fileName = QtWidgets.QLineEdit(self.centralwidget)
        self.lineEdit_fileName.setGeometry(QtCore.QRect(210, 290, 521, 31))
        self.lineEdit_fileName.setStyleSheet(whitecolor)
        self.lineEdit_fileName.setObjectName("lineEdit_fileName")
        self.lineEdit_fileName.setFont(inputFont)

        self.label_fileName = QtWidgets.QLabel(self.centralwidget)
        self.label_fileName.setGeometry(QtCore.QRect(50, 290, 151, 31))
        font = QtGui.QFont()
        font.setFamily("Arial Black")
        font.setPointSize(14)
        font.setBold(True)
        font.setWeight(75)
        self.label_fileName.setFont(font)
        self.label_fileName.setObjectName("label_fileName")
        self.label_patName = QtWidgets.QLabel(self.centralwidget)
        self.label_patName.setGeometry(QtCore.QRect(50, 350, 151, 31))
        font = QtGui.QFont()
        font.setFamily("Arial Black")
        font.setPointSize(14)
        font.setBold(True)
        font.setWeight(75)
        self.label_patName.setFont(font)
        self.label_patName.setObjectName("label_patName")
        self.pushButton_path = QtWidgets.QPushButton(self.centralwidget)
        self.pushButton_path.setGeometry(QtCore.QRect(740, 350, 51, 31))
        self.pushButton_path.setStyleSheet(color)
        font = QtGui.QFont()
        font.setPointSize(10)
        self.pushButton_path.setFont(font)
        self.pushButton_path.setAutoFillBackground(True)
        self.pushButton_path.setObjectName("pushButton_path")
        self.label_check_2 = QtWidgets.QLabel(self.centralwidget)
        self.label_check_2.setGeometry(QtCore.QRect(340, 240, 221, 21))
        font = QtGui.QFont()
        font.setPointSize(8)
        self.label_check_2.setFont(font)
        self.label_check_2.setObjectName("label_check_2")
        DeviceID.setCentralWidget(self.centralwidget)
        self.menubar = QtWidgets.QMenuBar(DeviceID)
        self.menubar.setGeometry(QtCore.QRect(0, 0, 819, 23))
        self.menubar.setObjectName("menubar")
        DeviceID.setMenuBar(self.menubar)
        self.statusbar = QtWidgets.QStatusBar(DeviceID)
        self.statusbar.setObjectName("statusbar")
        DeviceID.setStatusBar(self.statusbar)

        self.retranslateUi(DeviceID)
        QtCore.QMetaObject.connectSlotsByName(DeviceID)

    def retranslateUi(self, DeviceID):
        _translate = QtCore.QCoreApplication.translate
        DeviceID.setWindowTitle(_translate("DeviceID", "DeviceID生成工具"))
        self.label_tac.setText(_translate("DeviceID", "TAC  码  (IMEI 码前 8 位) ："))
        self.label_message.setText(_translate("DeviceID", "附  加  信  息  (法 国 项 目)："))
        self.label_sn_pro.setText(_translate("DeviceID", "SN码(中间6位)- -起始码段："))
        self.label_check.setText(_translate("DeviceID", "IMEI 校 验 位(最 后 一 位)："))
        self.label_sn_end.setText(_translate("DeviceID", "结束码段："))
        self.pushButton_produce.setText(_translate("DeviceID", "生成 Device-ID文件"))
        self.label.setText(_translate("DeviceID", "项目 Device-ID 生 成 工 具"))
        self.pushButton_help.setText(_translate("DeviceID", "HELP"))
        self.pushButton_about.setText(_translate("DeviceID", "ABOUT"))
        self.label_3.setText(_translate("DeviceID", "*"))
        self.label_4.setText(_translate("DeviceID", "*"))
        self.label_5.setText(_translate("DeviceID", "*"))
        self.label_fileName.setText(_translate("DeviceID", "文   件   名   称："))
        self.label_patName.setText(_translate("DeviceID", "保   存   路   径："))
        self.pushButton_path.setText(_translate("DeviceID", "PATH..."))
        self.label_check_2.setText(_translate("DeviceID", "默认勾选生成校验位，取消则不生成校验位"))


        self.pushButton_help.clicked.connect(self.softHelp)
        self.pushButton_about.clicked.connect(self.softAbout)
        self.pushButton_path.clicked.connect(self.selectPath)
        self.pushButton_produce.clicked.connect(self.produce_DeviceID)
        self.checkBox.click()#默认选中
        r = self.checkBox.isChecked()#判断该选择框是否被选中，选中为 true 否则为 false
        print(r)

    def softHelp(self):
        tkinter.messagebox.showinfo('Help', '该工具用于生成 DeviceID 文件，输出为TXT文本，'
                                            '\n注意生成的TXT文本在 Windows 下不要编辑保存。')


    def softAbout(self):
        tkinter.messagebox.showinfo('About',' Author  :  '
                                            '\n Mail  :  '
                                            '\n Org   :  '
                                            '\n Team  :  '
                                            '\n Version :'
                                            '\n Date : 2018-09-26')

    # def get_ProjectName(self):
    #     projectName = self.lineEdit_projectName.text()
    #     if not projectName.strip():
    #         tkinter.messagebox.showerror('错误', '项目名不能为空！')
    #     else:
    #         return projectName

    def get_Msg(self):
        extraMsg = self.lineEdit_message.text()
        return extraMsg


    def get_TAC(self):
        tacCode = self.lineEdit_tac.text()
        size = tacCode.__len__()
        if not size == 8 or not tacCode.strip():
            tkinter.messagebox.showerror('错误', 'TAC码必须为 8 位数字！')
            #return None
        else:
            pattern = r'[0-9]{1,8}'
            match = re.match(pattern, tacCode)
            print(match)
            if match == None or not match.span()[1] == 8:
                tkinter.messagebox.showerror('错误', 'TAC码必须为 8 位数字！')
                #return None
            else:
                return tacCode

    def get_SN_Pro(self):
        snPro = self.lineEdit_sn_pro.text()
        size = snPro.__len__()
        if not size == 6 or not snPro.strip():
            tkinter.messagebox.showerror('错误', 'TAC码必须为 6 位数字！')
            # return None
        else:
            pattern = r'[0-9]{1,6}'
            match = re.match(pattern, snPro)
            print(match)
            if match == None or not match.span()[1] == 6:
                tkinter.messagebox.showerror('错误', 'TAC码必须为 6 位数字！')
                # return None
            else:
                return snPro

    def get_SN_End(self):
        snEnd = self.lineEdit_sn_end.text()
        size = snEnd.__len__()
        if not size == 6 or not snEnd.strip():
            tkinter.messagebox.showerror('错误', 'TAC码必须为 6 位数字！')
            # return None
        else:
            pattern = r'[0-9]{1,6}'
            match = re.match(pattern, snEnd)
            print(match)
            if match == None or not match.span()[1] == 6:
                tkinter.messagebox.showerror('错误', 'TAC码必须为 6 位数字！')
                # return None
            else:
                return snEnd

    def selectPath(self):
        global dir_pathName
        dir_path = filedialog.askdirectory()
        if not dir_path:
            tkinter.messagebox.showinfo('提示', '请先选择保存路径！')
        dir_pathName = dir_path + '/'
        print("文件路径夹：" + dir_pathName)
        self.lineEdit_path.setText(dir_pathName)

    def get_FinallySnCode(self):
        if self.get_SN_Pro() and self.get_SN_End():
            print('得到sn码')
            # global snEndCode
            # global snProCode
            snProCode = self.get_SN_Pro()
            snEndCode = self.get_SN_End()
            finallySnCode = []
            print(type(finallySnCode))
            if snEndCode < snProCode :
                tkinter.messagebox.showerror('错误', 'SN码输入有误，结束码段必须大于起始码段！')
            else:
                for i in range(int(snProCode), int(snEndCode) + 1):
                    snCode = i
                    finallySnCode.append('%06d'%snCode)
            return finallySnCode


    def get_DeviceCode(self, codeList):
        print(codeList)
        deviceCodeList = []
        for i in range(0, len(codeList)):
            strCode = codeList[i]
            print(type(strCode))
            #计算checked code
            checkedCode = self.get_CheckCode(strCode)
            print('checkedCode===', checkedCode)
            if not checkedCode == -1:
                deviceCode = '%s%s'%(codeList[i], str(checkedCode))
                deviceCodeList.append(deviceCode)
        print('deviceCodeList==',deviceCodeList)
        return deviceCodeList



    def get_CheckCode(self, strCode):
        print(strCode)
        val_sum1 = 0#奇数位之和
        val_sum2 = 0
        val_total = 0
        val_temp = 0
        sscList = []
        if len(strCode) == 14:
            for i in strCode:
                sscList.append(int(i))
            for j in range(0, 14):
                if j % 2 == 0:#奇数位求和
                    # print('奇数位==',j)
                    val_sum1 = val_sum1 + sscList[j]
                else:#偶数位
                    # print('偶数位==', j)
                    val_temp = sscList[j] * 2
                    if val_temp < 10:
                        val_sum2 = val_sum2 + val_temp
                    else:
                        val_sum2 = val_sum2 + 1 + (val_temp - 10)

            val_total = val_sum1 + val_sum2
            if (val_total % 10) == 0:
                return 0
            else:
                return (10 - (val_total % 10))
        else:
            return -1

    def enableControl(self):

        self.checkBox.setDisabled(False)
        self.pushButton_path.setDisabled(False)
        self.pushButton_produce.setDisabled(False)
        # self.pushButton_fileName.setDisabled(False)

    def disAbleControl(self):
        self.checkBox.setDisabled(True)
        self.pushButton_path.setDisabled(True)
        self.pushButton_produce.setDisabled(True)
        # self.pushButton_fileName.setDisabled(True)

    def produce_DeviceID(self):
        imeiCodeList = []
        deviceIDNameList = []
        # projectName = self.get_ProjectName()
        extraMsg = self.get_Msg()
        if True:
            tacCode = self.get_TAC()
            if tacCode:
                snCodeList = self.get_FinallySnCode()
                if snCodeList:
                    for i in range(0, len(snCodeList)):
                        snCode = snCodeList[i]
                        imeiCodeList.append('%s%s'%(tacCode, snCode))
                    isCheckBox = self.checkBox.isChecked()
                    print('isCheckBox===',isCheckBox)
                    #tkMsg = tkinter.messagebox.askquestion('提示', '正在生成文件，请稍等！',)
                    getSaveFileName = self.lineEdit_fileName.text()
                    if not getSaveFileName:
                        tkinter.messagebox.showinfo('提示', '请输入有效的文件名！')
                        return
                    global dir_pathName
                    if dir_pathName == '/':
                        tkinter.messagebox.showerror('提示', '请选择保存路径！')
                        return

                    self.disAbleControl()

                    if (isCheckBox):
                        deviceIdList = self.get_DeviceCode(imeiCodeList)
                    else:
                        deviceIdList = imeiCodeList

                    for k in range(0,len(deviceIdList)):
                        if extraMsg:
                            deviceIDName = '%s_%s'%(extraMsg, deviceIdList[k])
                        else:
                            deviceIDName = '%s'%(deviceIdList[k])
                        deviceIDNameList.append(deviceIDName)

                    #保存文件
                    fileDevice = open('%s%s.txt' % (dir_pathName, getSaveFileName), 'wb+')
                    for j in range(0, len(deviceIDNameList)):#移字节码格式写入
                        deviceBatys = bytes(deviceIDNameList[j], encoding='utf-8')
                        fileDevice.write(deviceBatys)
                        fileDevice.write(bytes('\n', encoding='utf-8'))
                    fileDevice.flush()
                        # fileDevice.write(chr(10))
                        # fileDevice.write(deviceIDNameList[j])
                        # fileDevice.write('\n\r')
                        # fileDevice.write(chr(10))
                    # fileDevice.flush()
                    # os.linesep
                    # with open('%s%s.txt'%(dir_pathName,getSaveFileName), 'wb+') as fileDevice:
                    #     for k in range(0, len(deviceIDNameList)):
                    #         write = deviceIDNameList[k]
                    #         write = write.encode("utf-8")
                    #         fileDevice.write(deviceIDNameList[k])
                    #         fileDevice.write(chr(10))
                    #     fileDevice.flush()
                        # set ff:'%s'%(getSaveFileName) = unix

                    tkinter.messagebox.showinfo('提示', '文件保存完成！')
                    self.enableControl()
