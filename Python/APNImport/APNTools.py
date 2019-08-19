#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2018/10/25 15:50
# @Author  : LiuXiangNan
# @Site    : 
# @File    : .py
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
from PyQt5.QtCore import QStringListModel
from PyQt5.QtWidgets import QDialog, QLineEdit
import xml.dom.minidom

from loginUI import Ui_Dialog

root = tk.Tk()
root.withdraw()
from functionapn import *
from PyQt5 import QtWidgets

global excel_path
excel_path = ''
global file_name
file_name = ''
xml_path =''
file_name_xml = ''


class MainWindow(QtWidgets.QMainWindow, Ui_MainWindow):
    def __init__(self, parent=None):
        super(MainWindow, self).__init__(parent)
        self.setupUi(self)

        #################################################################################################################
        self.pushButton_about.clicked.connect(self.softAbout)
        self.toolButton_path.clicked.connect(self.selectPath)
        self.pushButton_import.clicked.connect(self.import_start)
        self.checkBox_xml.setChecked(True)
        self.checkBox_excel.clicked.connect(self.switch_excel)
        self.checkBox_xml.clicked.connect(self.switch_xml)

        #################################################################################################################


    def switch_xml(self):
        if self.checkBox_xml.isChecked():
            self.checkBox_excel.setChecked(False)
            self.comboBox.setEnabled(False)
        elif not self.checkBox_xml.isChecked():
            self.checkBox_excel.setChecked(True)
            self.comboBox.setEnabled(True)
        self.lineEdit_path.clear()
        self.comboBox.clear()

    def switch_excel(self):
        if self.checkBox_excel.isChecked():
            self.checkBox_xml.setChecked(False)
            self.comboBox.setEnabled(True)

        elif not self.checkBox_excel.isChecked():
            self.checkBox_xml.setChecked(True)
            self.comboBox.setEnabled(False)
        self.lineEdit_path.clear()
        self.comboBox.clear()

    def import_start(self):
        if not self.lineEdit_path.text():
            tk.messagebox.showerror('ERROR', '文件名不能为空，请选择正确的文件！')
            return
        if self.checkBox_excel.isChecked():
            self.import_start_excel()
        if self.checkBox_xml.isChecked():
            self.import_start_xml()

    def import_start_xml(self):
        print('import_start_xml')
        self.progressBar.setProperty("value", 0)
        self.parse_xml()


    def parse_xml(self):
        # 打开数据库
        try:
            conn_xml = sqlite3.connect('./APNTEST.db')
        except:
            tk.messagebox.showerror('ERROR', '服务器链接错误！')

        datatime = datetime.datetime.now().strftime('%Y%m%d%H%M%S')
        r1 = u'[a-zA-Z0-9’!"#$%&\'()*+,-./:;<=>?@，。?★、…【】《》？“”‘’！[\\]^_`{|}~]+'  # 用户也可以在此进行自定义过滤字符
        update_name = re.sub('-', '_', file_name_xml)


        # 操作游标
        cursor_xml = conn_xml.cursor()
        cursor_xml.execute('DROP TABLE IF EXISTS {0}'.format('DORO_APN_XML'))
        cursor_xml.execute('''CREATE TABLE DORO_APN_XML(
                                    [Account_Name]    STRING,
                                    MCCMNC            STRING,
                                    APN               STRING,
                                    UserName          STRING,
                                    Password          STRING,
                                    [Auth.type]       STRING,
                                    HomePage          STRING,
                                    Connection_Type   STRING,
                                    [Use_Proxy]       STRING,
                                    [Proxy_Address]   STRING,
                                    [Proxy_Port]      STRING,
                                    [Proxy_User_Name] STRING,
                                    [Proxy_Password]  STRING,
                                    Type              STRING,
                                    [Mnvo_match_data] STRING
                                     )'''.format('DORO_APN_XML'))

        # # # 操作游标
        # cursor_xml = conn_xml.cursor()
        self.progressBar.setProperty("value", 40)
        global xml_path
        input_database = []
        fp = xml.dom.minidom.parse(xml_path)
        for node in fp.getElementsByTagName("apn"):
            carrier = node.getAttribute("carrier")
            input_database.append(carrier)#account name

            mcc = node.getAttribute("mcc")
            mnc = node.getAttribute("mnc")
            input_database.append(mcc + mnc)#mccmnc

            apn = node.getAttribute("apn")
            input_database.append(apn)  # apn

            username = node.getAttribute("username")
            user = node.getAttribute("user")
            if user:
                input_database.append(user)
            elif username:
                input_database.append(username)
            elif not user and not username:
                input_database.append('')

            password = node.getAttribute("password")
            input_database.append(password)#password

            auth_type = node.getAttribute("authtype")
            if auth_type == '1':
                auth_type = 'secure'
            else:
                auth_type = 'normal'
            input_database.append(auth_type)#auth.type

            homepage = node.getAttribute("mmsc")
            input_database.append(homepage) # home page

            connection_type = node.getAttribute("type")
            input_database.append('Factory default')# 存在问题 TODO

            use_proxy = node.getAttribute("")
            input_database.append(use_proxy) #TODO

            proxy_address = node.getAttribute("mmsproxy")
            input_database.append(proxy_address) # proxy address

            proxy_port = node.getAttribute("port")
            if not proxy_port:
                proxy_port = node.getAttribute("mmsport")
            input_database.append(proxy_port)

            if proxy_port and proxy_address:
                use_proxy = 'yes'
            else:
                use_proxy = 'no'
            input_database[8] = use_proxy

            proxy_user_name = node.getAttribute("username")
            proxy_usre_user = node.getAttribute("user")
            proxy_password = node.getAttribute("password")
            if proxy_address and proxy_port:
                input_database[3] = ''
                input_database[4] = ''
            else:
                proxy_user_name = ''
                proxy_password = ''
                proxy_usre_user = ''

            if proxy_user_name:
                input_database.append(proxy_user_name)
            elif proxy_usre_user:
                input_database.append(proxy_usre_user)
            elif not proxy_usre_user and not proxy_user_name:
                input_database.append('')

            input_database.append(proxy_password)

            type = node.getAttribute("type")
            input_database.append(type)

            mvno_match_data = node.getAttribute("mvno_match_data")
            input_database.append(mvno_match_data)

            input_tuple = tuple(input_database)
            # print(input_tuple)

            #插入数据库
            cursor_xml.executemany(
                'INSERT INTO DORO_APN_XML VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)', [input_database])
            # print('input_database=======', input_database)
            conn_xml.commit()
            input_database = []

        self.progressBar.setProperty("value", 80)

        # 更新时间
        cursor_xml.execute('''UPDATE  UPDATEDATE SET DATE = '{0}',ExcelName = '{1}' WHERE SHEETNAME = '{2}' '''.format(datatime, update_name, 'DORO项目'))
        conn_xml.commit()

        cursor_xml.execute('SELECT * from {0}'.format('DORO_APN_XML'))
        data = cursor_xml.fetchall()
        output = []

        for k in range(0, len(data)):
            output_str = '%-5s  :(Account_Name:  %-10s)    |    (MCCMNC: %-10s)    |    (APN: %-10s)    ' \
                         '|    (Username: %-10s)    |    (Password: %-10s)    |    (Auth.type: %-10s)    ' \
                         '|    (Homepage: %-10s)    |    (Connection type: %-10s)    |    (Use proxy: %-10s)    ' \
                         '|    (Proxy address: %-10s)    |    (Proxy port: %-10s)    |    (Proxy user name: %-10s)    ' \
                         '|    (Proxy password: %-10s)    |    (Type: %-10s)    |    (Mvno match data: %-10s)'\
                         %(k + 1, str(data[k][0]), str(data[k][1]), str(data[k][2]), str(data[k][3]), str(data[k][4]),
                           str(data[k][5]), str(data[k][6]), str(data[k][7]), str(data[k][8]), str(data[k][9]),
                           str(data[k][10]), str(data[k][11]), str(data[k][12]), str(data[k][13]), str(data[k][14]))

            output.append(output_str)
        # 向listview中赋值
        slm = QStringListModel()
        self.qList = output
        slm.setStringList(output)
        self.listView.setModel(slm)

        self.progressBar.setProperty("value", 90)


        cursor_xml.close()
        conn_xml.close()
        self.progressBar.setProperty("value", 100)
        print('input end')


    def import_start_excel(self):
        # print('import_start_excel')
        # return
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
                        if n == 9:
                            input_str = str(cols_list_name[m][key_index[n]])
                            if (input_str.strip()).upper() == 'CHAP':
                                input_str = 'security'
                            else:
                                input_str = 'normal'
                        else:
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
            conn = sqlite3.connect('//192.168.3.88/sql/APNCHECKTEST/APNTEST.db')
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
            cursor.execute('''UPDATE  UPDATEDATE SET DATE = {0}, ExcelName = '{1}' WHERE SHEETNAME = '{2}' '''.format(datatime, file_name, database_table_name))

            cursor.execute('SELECT * from {0}'.format(database_table_name))
            data = cursor.fetchall()
            output = []
            for k in range(0, len(data)):
                output_str = '%-5s  :(Account_Name : %-10s )    |   (APN : %-10s)    |   (MCCMNC : %-10s)\
                |   (UserName : %-10s)    |   (Password : %-10s)    |   (Proxy_Address : %-10s )\
                |   (Proxy_Port : %-10s)    |   (Proxy_User_Name : %-10s)    |   (Proxy_Password : %-10s)\
                |   (Auty.type : %-10s)    |   (Use_Proxy : %-10s)    |   (Mnvo_match_data: %-10s)'\
                 %(k + 1, str(data[k][0]), str(data[k][1]), str(data[k][2]), str(data[k][3]), str(data[k][4]), \
                   str(data[k][5]), str(data[k][6]), str(data[k][7]), str(data[k][8]), str(data[k][9]), \
                   str(data[k][10]), str(data[k][11]))

                output.append(output_str)
            # 向listview中赋值
            slm = QStringListModel()
            self.qList = output
            slm.setStringList(output)
            self.listView.setModel(slm)

            conn.commit()
            cursor.close()
            conn.close()
            self.progressBar.setProperty("value", 100)
            print('end')



    def softAbout(self):
        tkinter.messagebox.showinfo('About', ' 仅供内部使用，未经授权请勿传播!'
                                             '\n Author  :  '
                                             '\n Mail  :  '
                                             '\n Org   :    '
                                             '\n Team  :   '
                                             '\n Version :   '
                                             '\n Date : 2019-03-18')

    def selectPath(self):
        if self.checkBox_xml.isChecked():
            self.selectPath_xml()
        elif self.checkBox_excel.isChecked():
            self.selectPath_excel()

    def selectPath_xml(self):
        print('select xml')
        global file_name_xml
        global xml_path
        xml_path = filedialog.askopenfilename()
        if xml_path.endswith('xml'):
            self.lineEdit_path.setText(xml_path)
            file_name_xml = os.path.basename(xml_path)
        else:
            tkinter.messagebox.showerror('文件类型错误', '请选择正确的文件类型')


    def selectPath_excel(self):
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


class logindialog(QDialog, Ui_Dialog):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.setupUi(self)
        self.setWindowTitle('登录')
        self.resize(388, 262)
        self.setFixedSize(self.width(), self.height())

        self.lineEdit_pwd.setEchoMode(QLineEdit.Password)#以密码形式显示

        #############################################
        self.pushButton_login.clicked.connect(self.login)
        self.pushButton_cancel.clicked.connect(self.app_exit)
        ###############################################


        # self.setWindowFlags(Qt.WindowCloseButtonHint)

    def login(self):
        # self.lineEdit_pwd.setEchoMode(QLineEdit.Password)
        user_name = self.lineEdit_username.text()
        pwd = self.lineEdit_pwd.text()
        print('pwd====', pwd)

        if not user_name:
            tkinter.messagebox.showerror('错误', '请输入用户名！')
            return
        if not pwd:
            tkinter.messagebox.showerror('错误', '密码不能为空！')
            return
        try:
            conn = sqlite3.connect('//192.168.3.88/sql//APNCHECKTEST/APNTEST.db')
            cursor = conn.cursor()
            sql_str = '''SELECT * from Login where User_Name=\'{0}\''''.format(user_name)
            cursor.execute(sql_str)
            result = cursor.fetchall()
            if result:

                db_pwd = result[0][1]
                print('db pwd ===', db_pwd)
                if str(pwd) == str(db_pwd):
                    self.accept()
                else:
                    tkinter.messagebox.showerror('错误', '密码错误！')
                    return
            else:
                tkinter.messagebox.showerror('错误', '用户名错误！')
                return
        except:
            tkinter.messagebox.showerror('错误', '服务器连接错误！')
            return

    def app_exit(self):
        sys.exit(app.exec_())



if __name__ == "__main__":
    app = QtWidgets.QApplication(sys.argv)  # 首先必须实例化QApplication类，作为GUI主程序入口
    dialog = logindialog()
    # if dialog.exec() == QDialog.Accepted:
    MainWindow = MainWindow()  # 实例化MainWindow类，创建自带menu的窗体类型QMainWindow
    MainWindow.setFixedSize(707, 690)  # 禁止改变窗口大小
    MainWindow.show()  # 显示窗体
    sys.exit(app.exec_())  # 当来自操作系统的分发事件指派调用窗口时，
