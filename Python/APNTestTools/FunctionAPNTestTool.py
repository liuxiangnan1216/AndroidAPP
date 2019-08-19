#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2019/3/18 16:43
# @Author  : LiuXiangNan
# @Site    : 
# @File    : FunctionAPNTestTool.py

import datetime
import os
import re
import sqlite3
import sys
import tkinter
import tkinter.messagebox
import tkinter as tk
from time import sleep
from tkinter import filedialog
import xlsxwriter
from UI_functionAPNTest import *
root = tk.Tk()
root.withdraw()

global conn
global cursor
global mcc
global country_name
global origin_file_name
global update_date


class MainWindow(QtWidgets.QMainWindow, Ui_MainWindow):
    def __init__(self, parent=None):
        super(MainWindow, self).__init__(parent)
        self.setupUi(self)


        ################################################################################################################

        self.connect_database()
        self.pushButton_about.clicked.connect(self.softAbout)

        self.pushButton_getlist.clicked.connect(self.get_table_all)
        self.pushButton_getlist_2.clicked.connect(self.get_table_2)

        self.toolButton_apnselect.clicked.connect(self.get_apn_path)
        self.toolButton_apnselect_2.clicked.connect(self.get_apn_path_2)

        self.toolButton_apnselect_reportpath.clicked.connect(self.get_report_path)
        self.toolButton_apnselect_reportpath_2.clicked.connect(self.get_report_path_2)

        self.pushButton_starttest.clicked.connect(self.start_test)
        self.pushButton_starttest_2.clicked.connect(self.start_test_2)

        self.comboBox_market.currentIndexChanged.connect(self.get_countrys)
        ################################################################################################################


    def start_test(self):
        project_name = self.comboBox_list.currentText()
        if not self.comboBox_list.currentText():
            tk.messagebox.showerror('ERROR', '请选择正确的表！')
            return
        if not self.lineEdit_apnpath.text():
            tk.messagebox.showerror('ERROR', '请选择正确的文件！')
            return
        if not self.lineEdit_apnpath_reportpath.text():
            tk.messagebox.showerror('ERROR', '测试结果保存路径不能为空！')
            return

        if re.search(r'DORO', project_name, re.I):
            self.start_test_doro()
        else:
            self.start_test_transsion()

    def start_test_doro(self):
        reportpath_doro = self.lineEdit_apnpath_reportpath.text()
        match_single_doro = []
        match_mult_doro = []
        match_empty_doro = []
        empty_doro = ['-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-']
        result_title_doro = ['Account Name',  'MCCMNC', 'APN', 'Username', 'Password', 'Auth.type', 'Homepage', 'Connection type',
               'Use proxy', 'Proxy address', 'Proxy port', 'Proxy user name', 'Proxy password', 'Type', 'Mvno match data']
        separate_list = ['-----------------------------------------------']
        match_single_doro.append(result_title_doro)
        match_mult_doro.append(result_title_doro)
        match_empty_doro.append(result_title_doro)

        (table_name, date, file_name) = self.comboBox_list.currentText().split(r'||')
        global origin_file_name
        global update_date
        update_date = date.strip()
        origin_file_name = file_name.strip()
        table_name_doro = table_name.strip()
        apn_doro_list = self.conversion_apn_doro(apn_path)
        if re.search(r'DORO', table_name_doro, re.I):
            select_table = 'DORO_APN_XML'
        for i in range(0, len(apn_doro_list)):
            select_database_result_doro = self.select_database(select_table, apn_doro_list[i])
            # print('select_database_result_doro:', select_database_result_doro)
            if len(select_database_result_doro) == 1:
                if len(select_database_result_doro) == 1:
                    result_red = self.complear_list_doro_end(select_database_result_doro[0], apn_doro_list[i])
                    match_single_doro.append(result_red)
                    match_single_doro.append(list(apn_doro_list[i]))
                    match_single_doro.append(list(select_database_result_doro[0]))
                elif len(select_database_result_doro) == 0:
                    match_empty_doro.append(list(apn_doro_list[i]))
                    match_empty_doro.append(empty_doro)
                elif len(select_database_result_doro) > 1:
                    match_mult_doro.append(list(apn_doro_list[i]))
                    for k in range(0, len(select_database_result_doro)):
                        # result_mult = self.complear_list_end(apn_list[j], select_result[k])
                        match_mult_doro.append(list(select_database_result_doro[k]))
                    match_mult_doro.append((k + 1))

        self.import_report(reportpath_doro, table_name_doro, match_single_doro, match_mult_doro, match_empty_doro)
        self.progressBar.setProperty("value", 100)
        self.progressBar_2.setProperty("value", 0)
        tk.messagebox.showinfo('COMPLETE', '测试完成，详细测试结果见测试报告！')
        self.progressBar.setProperty("value", 0)


    def start_test_transsion(self):

        reportpath = self.lineEdit_apnpath_reportpath.text()
        match_single = []
        match_mult = []
        match_empty = []
        empty = ['-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-']
        result_buf = ['Account_Name', 'APN', 'MCCMNC', 'UserName', 'Password', 'Proxy_Address', 'Proxy_Port',
                      'Proxy_User_Name', 'Proxy_Password', 'Auty.type', 'Use_proxy', 'Mvno_match_data']
        separate_list = ['-----------------------------------------------']
        match_single.append(result_buf)
        match_mult.append(result_buf)
        match_empty.append(result_buf)

        (table_name_1, date, file_name) = self.comboBox_list.currentText().split(r'||')
        global origin_file_name
        global update_date
        update_date = date.strip()
        origin_file_name = file_name.strip()

        apn_list = self.conversion_apn(apn_path)
        # print('anp list=====', apn_list)

        for i in range(0, len(apn_list)):
            select_database_result = self.select_database(table_name_1, apn_list[i])
            if len(select_database_result) == 1:
                result_red = self.complear_list_end(select_database_result[0], apn_list[i])
                match_single.append(result_red)
                match_single.append(list(apn_list[i]))
                match_single.append(list(select_database_result[0]))
            elif len(select_database_result) == 0:
                match_empty.append(list(apn_list[i]))
                match_empty.append(empty)
            elif len(select_database_result) > 1:
                match_mult.append(list(apn_list[i]))
                for k in range(0, len(select_database_result)):
                    # result_mult = self.complear_list_end(apn_list[j], select_result[k])
                    match_mult.append(list(select_database_result[k]))
                match_mult.append((k + 1))

        self.import_report(reportpath, table_name_1, match_single, match_mult, match_empty)
        self.progressBar.setProperty("value", 100)
        self.progressBar_2.setProperty("value", 0)
        tk.messagebox.showinfo('COMPLETE', '测试完成，详细测试结果见测试报告！')
        self.progressBar.setProperty("value", 0)

        # self.close_database()

    def start_test_2(self):
        match_single2 = []
        match_mult2 = []
        match_empty2 = []
        empty2 = ['-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-']
        result_buf2 = ['Account_Name', 'APN', 'MCCMNC', 'UserName', 'Password', 'Proxy_Address', 'Proxy_Port',
                      'Proxy_User_Name', 'Proxy_Password', 'Auty.type', 'Use_proxy', 'Mvno_match_data']
        separate_list = ['-----------------------------------------------']
        match_single2.append(result_buf2)
        match_mult2.append(result_buf2)
        match_empty2.append(result_buf2)

        if not self.comboBox_country2.currentText():
            tk.messagebox.showerror('ERROR', '请选择国家！')
            return
        if not self.comboBox_list_2.currentText():
            tk.messagebox.showerror('ERROR', '请选择正确的表！')
            return
        else:
            (table_name_2, date, file_name) = self.comboBox_list_2 .currentText().split(r'||')
            global origin_file_name
            global update_date
            update_date = date.strip()
            origin_file_name = file_name.strip()

        if not self.lineEdit_apnpath_2.text():
            tk.messagebox.showerror('ERROR', '请选择正确的文件！')
            return
        else:
            apn_path2 = self.lineEdit_apnpath_2.text()

        if not self.lineEdit_apnpath_reportpath_2.text():
            tk.messagebox.showerror('ERROR', '测试结果保存路径不能为空！')
            return
        else:
            report_path2 = self.lineEdit_apnpath_reportpath_2.text()

        market = self.comboBox_market.currentIndex()

        (com_mcc, coun_name) = self.comboBox_country2.currentText().split(r'|')
        (mcc_str, get_mcc) = (com_mcc.strip()).split(r':')
        print('get_mcc============', get_mcc)

        txt_list = self.conversion_apn(apn_path2)
        for i in range(0, len(txt_list)):
            # print('-----------------------', txt_list[i][2])
            mcc_str = txt_list[i][2]
            if mcc_str.startswith(get_mcc):
                select_db_result2 = self.select_database(table_name_2, txt_list[i])

                if len(select_db_result2) == 1:
                    result_red = self.complear_list_end(select_db_result2[0], txt_list[i])
                    match_single2.append(result_red)
                    match_single2.append(list(txt_list[i]))
                    match_single2.append(list(select_db_result2[0]))
                elif len(select_db_result2) == 0:
                    match_empty2.append(list(txt_list[i]))
                    match_empty2.append(empty2)
                elif len(select_db_result2) > 1:
                    match_mult2.append(list(txt_list[i]))
                    for k in range(0, len(select_db_result2)):
                        # result_mult = self.complear_list_end(apn_list[j], select_result[k])
                        match_mult2.append(list(select_db_result2[k]))
                    match_mult2.append((k + 1))

        print('get mcc==========', get_mcc)
        try:
            sql_mcc = '''SELECT * from COUNTRYS_MCC where MCC=\'{0}\' '''.format(get_mcc)
            cursor.execute(sql_mcc)
            select_result = cursor.fetchall()
            print('select_result===========', select_result)
            if select_result:
                country_name = select_result[0][1]
            else:
                country_name = '未知'


            table_name_2_end = '%s_%s_%s'%(table_name_2, country_name, get_mcc)
            self.import_report(report_path2, table_name_2_end, match_single2, match_mult2, match_empty2)

            self.progressBar_2.setProperty("value", 100)
            self.progressBar.setProperty("value", 0)
            tk.messagebox.showinfo('COMPLETE', '测试完成，详细测试结果见测试报告！')
            self.progressBar_2.setProperty("value", 0)
        except:
            tk.messagebox.showerror('ERROR', '服务器链接错误！')
            return
        print('end==================================')


        ################################################################################################################

    def get_countrys(self):
        global conn
        global cursor
        self.comboBox_country2.clear()
        market = self.comboBox_market.currentIndex()
        try:
            sql_market = '''SELECT * from COUNTRYS_MCC where market=\'{0}\' '''.format(market)
            cursor.execute(sql_market)
            select_result = cursor.fetchall()

            for i in range(0, len(select_result)):
                con_name = re.sub('\n', '', select_result[i][1])
                set_name = 'MCC:{0}   |  {1}'.format(select_result[i][0], con_name)
                self.comboBox_country2.addItem(set_name)
        except:
            tkinter.messagebox.showerror('错误', '服务器连接错误！')
            return


    def get_table_all(self):
        table_name = self.get_table()
        com_name = table_name[1][0] + ' || ' + str(table_name[1][1]) + ' || ' + str(table_name[1][2])
        if not com_name == self.comboBox_list.itemText(1):
            for i in range(0, len(table_name)):
                com_name = table_name[i][0] + ' || ' + str(table_name[i][1]) + ' || ' + str(table_name[i][2])
                self.comboBox_list.addItem(com_name)

    def get_table_2(self):
        table_name = self.get_table()
        com_name = table_name[1][0] + ' || ' + str(table_name[1][1]) + ' || ' + str(table_name[1][2])
        if not com_name == self.comboBox_list_2.itemText(1):
            for i in range(0, len(table_name)):
                com_name = table_name[i][0] + ' || ' + str(table_name[i][1]) + ' || ' + str(
                    table_name[i][2])
                self.comboBox_list_2.addItem(com_name)

    def get_apn_path(self):
        global apn_path
        global file_name
        apn_path = filedialog.askopenfilename()
        if apn_path.endswith('.txt') | apn_path.endswith('.TXT'):
            self.lineEdit_apnpath.setText(apn_path)
            file_name = os.path.basename(apn_path)
        else:
            tkinter.messagebox.showerror('文件类型错误', '请选择正确的文件类型')
            return None

    def get_apn_path_2(self):
        global apn_path_2
        global file_name_2
        apn_path_2 = filedialog.askopenfilename()
        if apn_path_2.endswith('.txt') | apn_path_2.endswith('.TXT'):
            self.lineEdit_apnpath_2.setText(apn_path_2)
            file_name_2 = os.path.basename(apn_path_2)
        else:
            tkinter.messagebox.showerror('文件类型错误', '请选择正确的文件类型')
            return None

    def get_report_path(self):
        global report_path
        report_path = filedialog.askdirectory()
        self.lineEdit_apnpath_reportpath.setText(report_path)

    def get_report_path_2(self):
        global report_path_2
        report_path_2 = filedialog.askdirectory()
        self.lineEdit_apnpath_reportpath_2.setText(report_path_2)


        ################################################################################################################
    def connect_database(self):
        try:
            global conn
            global cursor
            conn = sqlite3.connect('//192.168.3.88/sql//APNCHECKTEST/APNTEST.db')#//192.168.3.88/sql/APNCHECK/
            cursor = conn.cursor()
        except:
            tk.messagebox.showerror('ERROR', '服务器链接错误！')
            return

    def get_table(self):
        global conn
        global cursor
        cursor.execute('''select * from UPDATEDATE''')
        table_name = cursor.fetchall()
        # print('table name ==*******************==', table_name)
        return table_name


    #select 数据库
    def select_database(self, tablename, list1):
        global conn
        global cursor
        table_name = tablename.strip()

        if re.search(r'DORO', table_name, re.I):
            mccmnc = list1[1]
            accountName = list1[0]
            apn = list1[2]
            mnvo_data = list1[-1]
        else:
            mccmnc = list1[2]
            accountName = list1[0]
            apn = list1[1]
            mnvo_data = list1[-1]

        try:
        # if True:
            sql_str = '''SELECT * from {0} where MCCMNC=\'{1}\' and Account_Name=\'{2}\''''.format(table_name, mccmnc, accountName)
            cursor.execute(sql_str)
            select_result = cursor.fetchall()
            if len(select_result) > 1:
                sql_str1 = '''SELECT * from {0} where MCCMNC=\'{1}\' and Account_Name=\'{2}\' and APN=\'{3}\'''' \
                    .format(table_name, mccmnc, accountName, apn)
                cursor.execute(sql_str1)
                select_result = cursor.fetchall()
                if len(select_result) > 1:
                    if not list1[-1] == '':
                        sql_str2 = '''SELECT * from {0} where MCCMNC=\'{1}\' and Account_Name=\'{2}\' and APN=\'{3}\' and Mnvo_match_data=\'{4}\'''' \
                            .format(table_name, mccmnc, accountName, apn, mnvo_data)
                        cursor.execute(sql_str2)
                        select_result = cursor.fetchall()
            return select_result
        except:
            tkinter.messagebox.showerror('错误', '服务器连接错误！')
            return


    #解析Doro项目 apn文件
    def conversion_apn_doro(self, file_path):
        file_path = self.lineEdit_apnpath.text()#TODO
        apn_list = ['', '', '', '', '', '', '', '', '', '', '', '', '', '', '']
        par = ['Account Name',  'MCCMNC', 'APN', 'Username', 'Password', 'Auth.type', 'Homepage', 'Connection type',
               'Use proxy', 'Proxy address', 'Proxy port', 'Proxy user name', 'Proxy password', 'Type', 'Mvno match data']
        par_list = []
        replease = '\n""'
        fp = open(file_path, 'r', errors="ignore")
        readLine = fp.readlines()
        for i in range(0, len(readLine)):
            for parIndex in range(0, len(par)):
                if re.search(par[parIndex], readLine[i]):
                    str_list = readLine[i].split(':', 1)
                    apn_list[parIndex] = (str_list[1].strip(replease))
                    if parIndex == 14:
                        tuple_apn = tuple(apn_list)
                        par_list.append(tuple_apn)
                        apn_list = ['', '', '', '', '', '', '', '', '', '', '', '', '', '', '']
        self.progressBar.setProperty("value", 30)
        return par_list



    #解析传音txt apn文件
    def conversion_apn(self, file_path):
        apn_list = ['', '', '', '', '', '', '', '', '', '', '', '']
        par_list = []

        fp = open(file_path, 'r', errors="ignore")
        readLine = fp.readlines()

        for i in range(0, len(readLine)):
            if re.search(r'Account Name', readLine[i], re.I):
                str_list = readLine[i].split(':')
                apn_list[0] = (str_list[1].strip('\n'))

            if re.search(r'APN', readLine[i], re.I):
                str_list = readLine[i].split(':')
                apn_list[1] = (str_list[1].strip('\n'))

            if re.search(r'MCCMNC', readLine[i], re.I):
                str_list = readLine[i].split(':')
                apn_list[2] = (str_list[1].strip('\n'))

            if re.search(r'Username', readLine[i], re.I):
                str_list = readLine[i].split(':')
                apn_list[3] = (str_list[1].strip('\n'))

            # if re.search(r'Password', readLine[i]):
            if readLine[i].startswith(r'Password'):
                str_list = readLine[i].split(':')
                apn_list[4] = (str_list[1].strip('\n'))

            if re.search(r'Proxy address', readLine[i], re.I):
                str_list = readLine[i].split(':')
                apn_list[5] = (str_list[1].strip('\n'))

            if re.search(r'Proxy port', readLine[i], re.I):
                str_list = readLine[i].split(':')
                apn_list[6] = (str_list[1].strip('\n'))

            if re.search(r'Proxy user name', readLine[i], re.I):
                str_list = readLine[i].split(':')
                apn_list[7] = (str_list[1].strip('\n'))

            if re.search(r'Proxy password', readLine[i], re.I):
                str_list = readLine[i].split(':')
                apn_list[8] = (str_list[1].strip('\n'))

            if re.search(r'Auth.type', readLine[i], re.I):
                str_list = readLine[i].split(':')
                apn_list[9] = (str_list[1].strip('\n'))

            if re.search(r'Use proxy', readLine[i], re.I):
                str_list = readLine[i].split(':')
                apn_list[10] = (str_list[1].strip('\n'))

            if re.search(r'Mvno match data', readLine[i], re.I):
                str_list = readLine[i].split(':')
                apn_list[11] = (str_list[1].strip('\n'))

            if re.search(r'--------', readLine[i], re.I):
                # print('apn list ====', apn_list)
                tuple_apn = tuple(apn_list)
                par_list.append(tuple_apn)
                apn_list = ['', '', '', '', '', '', '', '', '', '', '', '']

        self.progressBar.setProperty("value", 30)
        self.progressBar_2.setProperty("value", 30)
        return par_list

    def complear_list_doro_end(self, list1, list2):#DORO项目比对
        result_index = []
        list1 = list(list1)
        list2 = list(list2)
        if len(list1) == len(list2):
            for i in range(0, len(list1)):
                # if isinstance(list1[i], str):
                if not str(list1[i]).strip().upper() == str(list2[i]).strip().upper():
                    result_index.append(i)
            if result_index == []:
                result_index.append(-2)  # 完全匹配
            return result_index
        else:
            result_index.append(-1)  # 都不匹配全红色
            return result_index

    #比对
    def complear_list_end(self, list1, list2):
        result_index = []
        list1 = list(list1)
        list2 = list(list2)
        if len(list1) == len(list2):
            for i in range(0, len(list2)):
                if i == 10 or i == 11:
                    if not list1[i].upper() == list2[i].upper():
                        result_index.append(i)
                elif not str(list1[i]).strip() == str(list2[i]).strip():
                    result_index.append(i)

            if result_index == []:
                result_index.append(-2)  # 完全匹配
            return result_index
        else:
            result_index.append(-1)  # 全红色
            return result_index


    #写报告
    def import_report(self, report_path, excel_name, single_msg, mult_msg, empty_msg):
        mult_num = 0
        single_num = 0
        empty_num = 0
        num_red = 0
        num_green = 0
        num = 0
        separate = ['-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-']
        sheet_name = '{0}_测试结果'.format(excel_name)
        date = datetime.datetime.now().strftime('%Y%m%d%H%M%S')  # 当前时间
        workbook = xlsxwriter.Workbook('{0}/{1}_{2}.xlsx'.format(report_path, sheet_name, date))
        worksheet1 = workbook.add_worksheet('匹配结果信息及说明')
        worksheet2 = workbook.add_worksheet('匹配到单条')
        worksheet3 = workbook.add_worksheet('匹配到多条')
        worksheet4 = workbook.add_worksheet('未匹配到相关项')
        worksheet1.set_column('A:A', 200)
        # worksheet1.set_row(0, 150)
        worksheet2.set_column('A:Q', 25)
        worksheet2.set_row(0, 40)
        worksheet3.set_column('A:Q', 25)
        worksheet3.set_row(0, 40)
        worksheet4.set_column('A:Q', 25)
        worksheet4.set_row(0, 40)
        bold_ok = workbook.add_format({'bold': True, 'bg_color': 'cyan', 'align': 'left'})#cyan
        bold_err = workbook.add_format({'bold': True, 'bg_color': 'red', 'align': 'left'})
        bold_title = workbook.add_format({'bold': 1, 'bg_color': 'green', 'align': 'center', 'font_size': 14})
        bold_sep = workbook.add_format({'bold': True, 'bg_color': 'white', 'align': 'left'})
        bold_orange = workbook.add_format({'bold': True, 'bg_color': 'yellow', 'align': 'left'})
        bold_total = workbook.add_format({'bold': True,  'bg_color': 'cyan', 'align': 'left', 'font_size': 20})

        # 写匹配到0个
        if len(empty_msg):
            for q in range(0, len(empty_msg)):
                if q == 0:
                    for r in range(0, len(empty_msg[0])):
                        worksheet4.write(0, r, empty_msg[0][r], bold_title)
                else:
                    if q % 2 == 0:
                        for h in range(0, len(empty_msg[q])):
                            worksheet4.write(q, h, empty_msg[q][h], bold_sep)
                    else:
                        for h in range(0, len(empty_msg[q])):
                            worksheet4.write(q, h, empty_msg[q][h], bold_ok)

        self.progressBar.setProperty("value", 60)
        self.progressBar_2.setProperty("value", 60)
        # 写匹配到多个
        if len(mult_msg):
            for j in range(0, len(mult_msg)):
                if j == 0:
                    for L in range(0, len(mult_msg[0])):
                        worksheet3.write(0, L, mult_msg[0][L], bold_title)
                else:
                    if not isinstance(mult_msg[j], int):
                        for p in range(0, len(mult_msg[j])):
                            worksheet3.write(j, p, mult_msg[j][p], bold_ok)
                    else:
                        for w in range(0, len(separate)):
                            worksheet3.write_row(j, w, separate[w], bold_sep)
        self.progressBar.setProperty("value", 70)
        self.progressBar_2.setProperty("value", 70)

        # 写匹配到一个
        if len(single_msg):
            for i in range(0, len(single_msg)):
                if i == 0:
                    for k in range(0, len(single_msg[0])):
                        worksheet2.write(0, k, single_msg[0][k], bold_title)
                else:
                    for m in range(0, len(single_msg[i])):
                        if i % 3 == 1:
                            color = single_msg[i]
                            num = i
                            for o in range(0, len(separate)):
                                worksheet2.write(i, o, separate[o], bold_sep)
                        elif i == (num + 1) or i == (num + 2):
                            if len(color) >= 1:
                                for r in range(0, len(color)):
                                    if m == color[r]:
                                        # print('m============', m)
                                        worksheet2.write(i, m, single_msg[i][m], bold_err)
                                        num_red = num_red + 1
                                        break
                                    else:
                                        worksheet2.write(i, m, single_msg[i][m], bold_ok)
                                        num_green = num_green + 1

            self.progressBar.setProperty("value", 90)
            self.progressBar_2.setProperty("value", 90)

            for h in range(0, len(mult_msg)):
                if isinstance(mult_msg[h], int):
                    mult_num = mult_num + 1

            single_num = (len(single_msg) - 1) / 3
            empty_num = (len(empty_msg) - 1) / 2
            total_num = mult_num + single_num + empty_num
            # global origin_file_name
            msg_in = ['总 计 测 试 项：{0} 项'.format(int(total_num)),
                     '匹 配 到 单 项：{0} 项'.format(int(single_num)),
                     '存 在 差 异 数：{0} 处'.format(int(num_red / 2)),
                     '匹 配 到 多 项：{0} 项'.format(mult_num),
                     '未匹配到相关项：{0} 项'.format(int(empty_num)),
                     ' ',
                     '原始文件：{0}'.format(origin_file_name),
                     '更新时间：{0}'.format(update_date),
                     ' ',
                     '说明：',
                     '各测试项目详细信息见各 sheet 页。',
                     '每个 sheet 页第一行是测试项名称，白色背景短横线为分隔符,',
                     '同一种颜色标记的，两行中的第一行为导入的 txt 文件中的项,',
                     '其余项为在原始文件中查找到的匹配项，红色为存在的差异处。',
                     '匹配到多项 sheet 页，txt中的条目在原始文件中根据 MCCMNC 码和 ACCOUNT NAME 查找到多条匹配项时，',
                     '再次根据 APN 进行匹配最终匹配到的多个相似项。',
                     '未匹配到相关项 sheet 页，txt 文件中的项在原始文件中未查找到相似项。']

            for w in range(0, len(msg_in)):
                worksheet1.write(w, 0, msg_in[w], bold_total)

            worksheet1.write(20, 0, '示例图：', bold_total)
            worksheet1.insert_image('A24', 'img02.png')
            worksheet1.insert_image('A44', 'img03.png')
            worksheet1.insert_image('A72', 'img04.png')

        workbook.close()

    def close_database(self):
        global conn
        global cursor
        cursor.close()
        conn.commit()
        conn.close()

    def softAbout(self):
        tkinter.messagebox.showinfo('About', ' 仅供内部使用，未经授权请勿传播!'
                                             '\n Author  :     '
                                             '\n Mail  :  '
                                             '\n Org   :    '
                                             '\n Team  :   '
                                             '\n Version : '
                                             '\n Date : 2019-03-18')

if __name__ == "__main__":
    app = QtWidgets.QApplication(sys.argv)  # 首先必须实例化QApplication类，作为GUI主程序入口
    MainWindow = MainWindow()  # 实例化MainWindow类，创建自带menu的窗体类型QMainWindow
    MainWindow.setFixedSize(792, 498)  # 禁止改变窗口大小
    MainWindow.show()  # 显示窗体
    sys.exit(app.exec_())  # 当来自操作系统的分发事件指派调用窗口时，
