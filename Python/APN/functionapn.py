# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'functionapn.ui'
#
# Created by: PyQt5 UI code generator 5.11.2
#
# WARNING! All changes made in this file will be lost!

from PyQt5 import QtCore, QtGui, QtWidgets

class Ui_MainWindow(object):
    def setupUi(self, MainWindow):
        MainWindow.setObjectName("MainWindow")
        MainWindow.resize(707, 358)
        self.centralwidget = QtWidgets.QWidget(MainWindow)
        self.centralwidget.setObjectName("centralwidget")
        self.label = QtWidgets.QLabel(self.centralwidget)
        self.label.setGeometry(QtCore.QRect(20, -30, 561, 111))
        font = QtGui.QFont()
        font.setPointSize(20)
        self.label.setFont(font)
        self.label.setObjectName("label")
        self.pushButton_about = QtWidgets.QPushButton(self.centralwidget)
        self.pushButton_about.setGeometry(QtCore.QRect(600, 15, 75, 23))
        font = QtGui.QFont()
        font.setPointSize(10)
        self.pushButton_about.setFont(font)
        self.pushButton_about.setObjectName("pushButton_about")
        self.label_path = QtWidgets.QLabel(self.centralwidget)
        self.label_path.setGeometry(QtCore.QRect(40, 85, 111, 41))
        font = QtGui.QFont()
        font.setPointSize(12)
        self.label_path.setFont(font)
        self.label_path.setObjectName("label_path")
        self.lineEdit_path = QtWidgets.QLineEdit(self.centralwidget)
        self.lineEdit_path.setGeometry(QtCore.QRect(160, 90, 461, 30))
        self.lineEdit_path.setObjectName("lineEdit_path")
        self.toolButton_path = QtWidgets.QToolButton(self.centralwidget)
        self.toolButton_path.setGeometry(QtCore.QRect(630, 90, 51, 30))
        self.toolButton_path.setObjectName("toolButton_path")
        self.pushButton_import = QtWidgets.QPushButton(self.centralwidget)
        self.pushButton_import.setGeometry(QtCore.QRect(20, 200, 661, 61))
        font = QtGui.QFont()
        font.setPointSize(26)
        self.pushButton_import.setFont(font)
        self.pushButton_import.setObjectName("pushButton_import")
        self.label_sheet = QtWidgets.QLabel(self.centralwidget)
        self.label_sheet.setGeometry(QtCore.QRect(50, 145, 101, 21))
        font = QtGui.QFont()
        font.setPointSize(16)
        self.label_sheet.setFont(font)
        self.label_sheet.setObjectName("label_sheet")
        self.comboBox = QtWidgets.QComboBox(self.centralwidget)
        self.comboBox.setEnabled(True)
        self.comboBox.setGeometry(QtCore.QRect(160, 140, 461, 30))
        self.comboBox.setEditable(False)
        self.comboBox.setObjectName("comboBox")
        self.progressBar = QtWidgets.QProgressBar(self.centralwidget)
        self.progressBar.setGeometry(QtCore.QRect(20, 280, 671, 23))
        self.progressBar.setProperty("value", 0)
        self.progressBar.setObjectName("progressBar")
        MainWindow.setCentralWidget(self.centralwidget)
        self.menubar = QtWidgets.QMenuBar(MainWindow)
        self.menubar.setGeometry(QtCore.QRect(0, 0, 707, 23))
        self.menubar.setObjectName("menubar")
        MainWindow.setMenuBar(self.menubar)
        self.statusbar = QtWidgets.QStatusBar(MainWindow)
        self.statusbar.setObjectName("statusbar")
        MainWindow.setStatusBar(self.statusbar)

        self.retranslateUi(MainWindow)
        QtCore.QMetaObject.connectSlotsByName(MainWindow)

    def retranslateUi(self, MainWindow):
        _translate = QtCore.QCoreApplication.translate
        MainWindow.setWindowTitle(_translate("MainWindow", "FunctionMobileAPNImport"))
        self.label.setText(_translate("MainWindow", "功能机 APN 参数 EXCEL 表格导入"))
        self.pushButton_about.setText(_translate("MainWindow", "About"))
        self.label_path.setText(_translate("MainWindow", "EXCEL表格路径："))
        self.toolButton_path.setText(_translate("MainWindow", "..."))
        self.pushButton_import.setText(_translate("MainWindow", "导入数据至服务端"))
        self.label_sheet.setText(_translate("MainWindow", "Sheet 名："))

