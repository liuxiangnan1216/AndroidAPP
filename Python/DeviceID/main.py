#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2018/9/4 10:47
# @Author  : LiuXiangNan
# @Site    :
# @File    : main.py


from tkinter import *
from DeviceID_Main import *


def show_MainWindow():
    app = QtWidgets.QApplication(sys.argv)  # 首先必须实例化QApplication类，作为GUI主程序入口
    MainWindow = QtWidgets.QMainWindow()  # 实例化QtWidgets.QMainWindow类，创建自带menu的窗体类型QMainWindow
    MainWindow.setFixedSize(820, 500)#禁止改变窗口大小
    ui = Ui_DeviceID()  # 实例UI类
    ui.setupUi(MainWindow)  # 设置窗体UI
    MainWindow.show()  # 显示窗体
    sys.exit(app.exec_())  # 当来自操作系统的分发事件指派调用窗口时，
    # 应用程序开启主循环（mainloop）过程，
    # 当窗口创建完成，需要结束主循环过程，
    # 这时候呼叫sys.exit（）方法来，结束主循环过程退出，
    # 并且释放内存。为什么用app.exec_()而不是app.exec()？
    # 因为exec是python系统默认关键字，为了以示区别，所以写成exec_

if __name__ == "__main__":
    show_MainWindow()