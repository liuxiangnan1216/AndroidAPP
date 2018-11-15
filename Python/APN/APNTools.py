#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2018/10/25 15:50
# @Author  : LiuXiangNan
# @Site    : 
# @File    : SagerealAPNTools.py
import datetime
import re
import sqlite3
import sys
import tkinter
import tkinter.messagebox
import tkinter as tk
from tkinter import filedialog
import os.path
import xlrd as xlrd
import xlwt as xlwt

root = tk.Tk()
root.withdraw()
from functionapn import *
from PyQt5 import QtWidgets

global excel_path
excel_path = ''
global file_name
file_name = ''


class MainWindow(QtWidgets.QMainWindow, Ui_MainWindow):
    def __init__(self, parent=None):
        super(MainWindow, self).__init__(parent)
        self.setupUi(self)

        #################################################################################################################
        self.pushButton_about.clicked.connect(self.softAbout)
        self.toolButton_path.clicked.connect(self.selectPath)
        self.pushButton_import.clicked.connect(self.import_start)

        #################################################################################################################

    def import_start(self):
        global excel_path
        global file_name
        self.progressBar.setProperty("value", 0)
        sheet_name = self.comboBox.itemText(self.comboBox.currentIndex())
        print('sheet_name====', sheet_name)
        if excel_path:
            result = tk.messagebox.askquestion('INFO', '将导入  {0}  的数据到服务端，请确定！'.format(sheet_name))
            if result == 'yes' and file_name:
                self.excel_to_list(excel_path, sheet_name)
        else:
            tk.messagebox.showerror('ERROR', '文件错误，请选择正确的文件！')

    def excel_to_list(self, excel_name, sheet_name):
        op_excel = xlrd.open_workbook(r'%s' % (excel_name))
        sheet2 = op_excel.sheet_by_name(sheet_name)
        key_index = []
        # 以列创建 list_cols_name
        cols_list_name = []
        database_list_data = []
        database_list_in = []
        # database_tuple_buf = ()
        for i in range(0, sheet2.ncols):
            cols_list_name.append(sheet2.col_values(i))

        self.progressBar.setProperty("value", 20)

        for j in range(0, sheet2.nrows):
            for k in range(0, sheet2.ncols):
                if self.is_merge(sheet2, j, k):
                    merge = self.is_merge(sheet2, j, k)
                    cols_list_name[merge[1]][merge[0]] = merge[2]
        self.progressBar.setProperty("value", 30)
        # print('cols list name =====', cols_list_name)

        try:
        # if True:
            name_index = sheet2.col_values(0).index('NAME')
            apn_index = sheet2.col_values(0).index('APN')
            MCC_index = sheet2.col_values(0).index('MCC')
            MNC_index = sheet2.col_values(0).index('MNC')
            unam_index = sheet2.col_values(0).index('USERNAME')
            pwd_index = sheet2.col_values(0).index('PASSWORD')
            autype_index = sheet2.col_values(0).index('AUTHENTICATION TYPE')
            # hpage_index = sheet2.col_values(0).index('Homepage')
            proaddr_index = sheet2.col_values(0).index('PROXY')  # 有值则是有代理的
            proport_index = sheet2.col_values(0).index('PORT')
            prousename_index = sheet2.col_values(0).index('USERNAME')
            prousepwd = sheet2.col_values(0).index('PASSWORD')
            mvno_index = sheet2.col_values(0).index('mvno_match_data')
            if re.search(r'印度', sheet_name):
                usepro_index = sheet2.col_values(0).index('Proxy Enable')
            else:
                usepro_index = -1

            key_index = [name_index,
                         apn_index,
                         MCC_index,
                         unam_index,
                         pwd_index,
                         proaddr_index,
                         proport_index,
                         prousename_index,
                         prousepwd,
                         autype_index,
                         usepro_index,
                         mvno_index,
                         MNC_index]
            self.progressBar.setProperty("value", 50)

            r1 = u'[a-zA-Z0-9’!"#$%&\'()*+,-./:;<=>?@，。?★、…【】《》？“”‘’！[\\]^_`{|}~]+'  # 用户也可以在此进行自定义过滤字符
            data_table_name = re.sub(r1, '', sheet_name)

            if key_index:
                for m in range(1, sheet2.ncols):
                    for n in range(0, len(key_index)):
                        if key_index[n] == -1:
                            input_str = ''
                        else:
                            input_str = cols_list_name[m][key_index[n]]
                            if isinstance(input_str, str):
                                input_str = input_str.strip()
                                if input_str.endswith(' '):
                                    print('----------', input_str)

                        database_list_in.append(input_str)

                    if not re.findall(',', str(database_list_in[-1])):
                        if isinstance(database_list_in[-1], str) and database_list_in[-1]:
                            mncstr = database_list_in[-1].strip()
                            if int(float(mncstr)) == 0:
                                database_list_in[-1] = '00'
                        if isinstance(database_list_in[-1], float):
                            if database_list_in[-1] == 0.0:
                                database_list_in[-1] = '00'

                    if database_list_in[-1] and database_list_in[2]:
                        mcc = str(int(database_list_in[2]))
                        if re.findall(',', str(database_list_in[-1])):
                            mnc_list = database_list_in[-1].split(',')
                            # database_tuple_buf = ()
                            for i in range(0, len(mnc_list)):
                                mnc = (str(int(mnc_list[i])).strip()).zfill(2)
                                database_list_in[2] = mcc + mnc

                                # database_tuple_buf + tuple(database_list_in)
                                if database_list_in[5]:
                                    database_list_in[3] = ''
                                    database_list_in[4] = ''
                                else:
                                    database_list_in[7] = ''
                                    database_list_in[8] = ''

                                if not database_list_in[10]:
                                    if database_list_in[5] == '' or database_list_in[6] == '':
                                        database_list_in[10] = 'no'
                                    else:
                                        database_list_in[10] = 'yes'

                                database_list_data.append(tuple(database_list_in))
                            database_list_in = []
                        else:
                            mnc = (str(int(database_list_in[-1])).strip()).zfill(2)
                            database_list_in[2] = mcc + mnc
                            if database_list_in[5]:
                                database_list_in[3] = ''
                                database_list_in[4] = ''
                            else:
                                database_list_in[7] = ''
                                database_list_in[8] = ''

                            if not re.search(r'印度', sheet_name):
                                if not database_list_in[6]:
                                    database_list_in[6] = '0'
                                if not (database_list_in[5]) == '' and database_list_in[6] != 0:
                                    database_list_in[10] = 'yes'
                                else:
                                    database_list_in[10] = 'no'

                    if database_list_in:
                        database_list_data.append(database_list_in)
                        database_list_in = []
                # for i in range(0, len(database_list_data)):
                #     print('database_list_data============', database_list_data[i])
                self.open_database(data_table_name, sheet2, database_list_data)

            tk.messagebox.showinfo('SUCESSFUL', '导入成功！')
        except ValueError as e:
            print('error===', e)
            tk.messagebox.showerror('ERROR', '文件数据错误，请检查文件是否正确！')
        self.progressBar.setProperty("value", 0)

    def open_database(self, database_table, sheet2_ncols, list_value):
        database_table_name = database_table.strip()
        try:
            conn = sqlite3.connect('xxxxxxx/APNTEST.db')
        except:
            tk.messagebox.showerror('ERROR', '服务器链接错误！')
        self.progressBar.setProperty("value", 70)
        print(conn)
        if conn:
            # 操作游标
            cursor = conn.cursor()
            # print('database_table=====', database_table_name)
            self.progressBar.setProperty("value", 80)
            datatime = datetime.datetime.now().strftime('%Y%m%d%H%M%S')

            # cursor.executemany('INSERT INTO UPDATEDATE VALUES (?, ?)', [(database_table, datatime)])
            # 使用 execute() 方法执行 SQL，如果表存在则删除
            cursor.execute('DROP TABLE IF EXISTS {0}'.format(database_table_name))
            cursor.execute('''CREATE TABLE {0}(
                [Account_Name]    STRING,
                APN               STRING,
                MCCMNC            STRING,
                UserName          STRING,
                Password          STRING,
                [Proxy_Address]   STRING,
                [Proxy_Port]      STRING,
                [Proxy_User_Name] STRING,
                [Proxy_Password]  STRING,
                [Auty.type]       STRING,
                [Use_Proxy]       STRING,
                [Mnvo_match_data] STRING
            )'''.format(database_table_name))

            # 存入数据
            self.progressBar.setProperty("value", 90)
            for i in range(0, len(list_value)):
                input_database = list(list_value[i])
                del input_database[-1]#删除list的最后的原素
                cursor.executemany('INSERT INTO {0} VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)'.format(database_table_name), [input_database])

            # 更新时间
            cursor.execute('''UPDATE  UPDATEDATE SET DATE = {0} WHERE SHEETNAME = '{1}' '''.format(datatime, database_table_name))
            conn.commit()
            cursor.close()
            conn.close()
            self.progressBar.setProperty("value", 100)
            print('end')


    def softAbout(self):
        tkinter.messagebox.showinfo('About')

    def selectPath(self):
        global excel_path
        excel_path = filedialog.askopenfilename()
        if excel_path.endswith('.xlsm') | excel_path.endswith('.xlsx') | excel_path.endswith(
                '.xltx') | excel_path.endswith('.xltm') | excel_path.endswith('.xlsb') | excel_path.endswith(
                '.xlam') | excel_path.endswith('.xls'):
            self.lineEdit_path.setText(excel_path)
            global file_name
            file_name = os.path.basename(excel_path)
            op_excel = xlrd.open_workbook(r'{0}'.format(excel_path))
            sheet_names = op_excel.sheet_names()
            if sheet_names:
                for i in range(0, len(sheet_names)):
                    print(type(op_excel.sheet_names()[i]))
                    self.comboBox.addItem(op_excel.sheet_names()[i])
        else:
            tkinter.messagebox.showerror('文件类型错误', '请选择正确的文件类型')

    def is_merge(self, sheet2, row_index, col_index):
        merged = sheet2.merged_cells
        for (rlow, rhigh, clow, chigh) in merged:
            if (row_index >= rlow and row_index < rhigh):
                if (col_index >= clow and col_index < chigh):
                    cell_value = sheet2.cell_value(rlow, clow)
                    if cell_value:
                        return (row_index, col_index, cell_value)
                    else:
                        return None

    def set_style(self, name, height, bold=False):
        style = xlwt.XFStyle()  # 初始化样式
        font = xlwt.Font()  # 为样式创建字体
        font.name = name
        font.bold = bold
        font.color_index = 4
        font.height = height

        style.font = font
        return style

if __name__ == "__main__":
    app = QtWidgets.QApplication(sys.argv)  # 首先必须实例化QApplication类，作为GUI主程序入口
    MainWindow = MainWindow()  # 实例化MainWindow类，创建自带menu的窗体类型QMainWindow
    MainWindow.setFixedSize(700, 325)  # 禁止改变窗口大小
    MainWindow.show()  # 显示窗体
    sys.exit(app.exec_())  # 当来自操作系统的分发事件指派调用窗口时，
