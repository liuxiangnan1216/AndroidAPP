#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2019/3/18 16:37
# @Author  : LiuXiangNan
# @Site    : 
# @File    : loginUI.py

from PyQt5 import QtCore, QtGui, QtWidgets

class Ui_Dialog(object):
    def setupUi(self, Dialog):
        Dialog.setObjectName("Dialog")
        Dialog.resize(388, 262)
        self.pushButton_login = QtWidgets.QPushButton(Dialog)
        self.pushButton_login.setGeometry(QtCore.QRect(30, 200, 130, 30))
        self.pushButton_login.setObjectName("pushButton_login")
        self.pushButton_cancel = QtWidgets.QPushButton(Dialog)
        self.pushButton_cancel.setGeometry(QtCore.QRect(220, 200, 130, 30))
        self.pushButton_cancel.setObjectName("pushButton_cancel")
        self.label_username = QtWidgets.QLabel(Dialog)
        self.label_username.setGeometry(QtCore.QRect(30, 80, 61, 30))
        font = QtGui.QFont()
        font.setFamily("Aharoni")
        font.setPointSize(12)
        font.setBold(True)
        font.setWeight(75)
        self.label_username.setFont(font)
        self.label_username.setObjectName("label_username")
        self.label_pwd = QtWidgets.QLabel(Dialog)
        self.label_pwd.setGeometry(QtCore.QRect(30, 140, 61, 30))
        font = QtGui.QFont()
        font.setFamily("Aharoni")
        font.setPointSize(12)
        font.setBold(True)
        font.setWeight(75)
        self.label_pwd.setFont(font)
        self.label_pwd.setObjectName("label_pwd")
        self.lineEdit_username = QtWidgets.QLineEdit(Dialog)
        self.lineEdit_username.setGeometry(QtCore.QRect(100, 80, 251, 30))
        self.lineEdit_username.setObjectName("lineEdit_username")
        self.lineEdit_pwd = QtWidgets.QLineEdit(Dialog)
        self.lineEdit_pwd.setGeometry(QtCore.QRect(100, 140, 251, 30))
        self.lineEdit_pwd.setInputMethodHints(QtCore.Qt.ImhHiddenText)
        self.lineEdit_pwd.setText("")
        self.lineEdit_pwd.setObjectName("lineEdit_pwd")
        self.label_title = QtWidgets.QLabel(Dialog)
        self.label_title.setGeometry(QtCore.QRect(60, 30, 271, 30))
        font = QtGui.QFont()
        font.setFamily("Aharoni")
        font.setPointSize(12)
        font.setBold(True)
        font.setWeight(75)
        self.label_title.setFont(font)
        self.label_title.setObjectName("label_title")

        self.retranslateUi(Dialog)
        QtCore.QMetaObject.connectSlotsByName(Dialog)

    def retranslateUi(self, Dialog):
        _translate = QtCore.QCoreApplication.translate
        Dialog.setWindowTitle(_translate("Dialog", "Dialog"))
        self.pushButton_login.setText(_translate("Dialog", "登录"))
        self.pushButton_cancel.setText(_translate("Dialog", "取消"))
        self.label_username.setText(_translate("Dialog", "用户名："))
        self.label_pwd.setText(_translate("Dialog", "密   码："))
        self.label_title.setText(_translate("Dialog", "Sagereal 功能机 APN CHECK 工具 "))
