#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2018/10/26 10:05
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
from tkinter import filedialog
import xlsxwriter
from UI_functionAPNTest import *
root = tk.Tk()
root.withdraw()
global apn_path
global file_name
global report_path


class MainWindow(QtWidgets.QMainWindow, Ui_MainWindow):
    def __init__(self, parent=None):
        super(MainWindow, self).__init__(parent)
        self.setupUi(self)

        ################################################################################################################
        self.progressBar.setProperty("value", 0)
        self.pushButton_about.clicked.connect(self.softAbout)
        self.pushButton_getlist.clicked.connect(self.get_database_table)
        self.toolButton_apnselect.clicked.connect(self.get_apn_txt)
        self.pushButton_starttest.clicked.connect(self.compare_file)
        self.toolButton_apnselect_reportpath.clicked.connect(self.report_path)
        ################################################################################################################

    def compare_file(self):
        self.progressBar.setProperty("value", 0)
        global apn_path
        global file_name
        global report_path
        result_red = []
        result_signle = []
        match_single = []
        match_mult = []
        match_empty = []
        result_buf = ['Account_Name', 'APN', 'MCCMNC', 'UserName', 'Password', 'Proxy_Address', 'Proxy_Port',
                      'Proxy_User_Name', 'Proxy_Password', 'Auty.type', 'Use_proxy', 'Mvno_match_data']
        separate_list = ['-----------------------------------------------']
        match_single.append(result_buf)
        match_mult.append(result_buf)
        match_empty.append(result_buf)

        try:
            conn = sqlite3.connect('xxxxxxxx/APNTEST.db')
            cursor = conn.cursor()
        except:
            tk.messagebox.showerror('ERROR', '服务器链接错误！')
            return

        if not self.comboBox_list.itemText(1):
            tk.messagebox.showerror('ERROR', '请选择正确的表名！')
            return

        if not self.lineEdit_apnpath.text():
            tk.messagebox.showerror('ERROR', '请选择正确的 APN 文件！')
            return

        if not apn_path and file_name:
            tk.messagebox.showerror('ERROR', '文件不存在或文件名错误！')
            return

        try:
            report_path
        except:
            tk.messagebox.showerror('ERROR', '请选择报告保存路径！')
            return

        apn_list = self.conversion_apn(apn_path)
        ##############################################################################
        (table_name, date) = self.comboBox_list.currentText().split(r'     更新时间： ')
        empty = ['-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-']

        for j in range(0, len(apn_list)):
            sql_str = '''SELECT * from {0} where MCCMNC=\'{1}\' and Account_Name=\'{2}\''''.format(table_name, apn_list[j][2], apn_list[j][0])
            cursor.execute(sql_str)
            select_result = cursor.fetchall()
            if len(select_result) > 1:
                sql_str1 = '''SELECT * from {0} where MCCMNC=\'{1}\' and Account_Name=\'{2}\' and APN=\'{3}\''''\
                    .format(table_name, apn_list[j][2], apn_list[j][0], apn_list[j][1])
                cursor.execute(sql_str1)
                select_result = cursor.fetchall()
                if len(select_result) > 1:
                    if not apn_list[j][-1] == '':
                        sql_str2 = '''SELECT * from {0} where MCCMNC=\'{1}\' and Account_Name=\'{2}\' and APN=\'{3}\' and Mnvo_match_data=\'{4}\'''' \
                            .format(table_name, apn_list[j][2], apn_list[j][0], apn_list[j][1], apn_list[j][-1])
                        cursor.execute(sql_str2)
                        select_result = cursor.fetchall()

            if len(select_result) == 1:
                # print('select_result===========', select_result[0])
                result_red = self.complear_list_end(select_result[0], apn_list[j])
                match_single.append(result_red)
                match_single.append(apn_list[j])
                match_single.append(list(select_result[0]))
            elif len(select_result) == 0:
                match_empty.append(apn_list[j])
                match_empty.append(empty)
            elif len(select_result) > 1:
                match_mult.append(apn_list[j])
                for k in range(0, len(select_result)):
                    # result_mult = self.complear_list_end(apn_list[j], select_result[k])
                    match_mult.append(list(select_result[k]))
                match_mult.append((k + 1))

        self.progressBar.setProperty("value", 50)
        # for h in range(0, len(match_mult)):
        #     if isinstance(match_mult[h], int):
        #         mult_num = mult_num + 1
        # print('match mult==================', mult_num)
        self.import_report(table_name, match_single, match_mult, match_empty)
        print('end================================')
        cursor.close()
        conn.close()

    def import_report(self, excel_name, single_msg, mult_msg, empty_msg):
        global report_path
        mult_num = 0
        single_num = 0
        empty_num = 0
        num_red = 0
        num_green = 0
        num = 0
        separate = ['-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-']
        sheet_name = '{0}_测试报告'.format(excel_name)
        date = datetime.datetime.now().strftime('%Y%m%d%H%M%S')  # 当前时间
        workbook = xlsxwriter.Workbook('{0}/{1}_{2}.xlsx'.format(report_path, sheet_name, date))
        worksheet1 = workbook.add_worksheet('匹配结果信息及说明')
        worksheet2 = workbook.add_worksheet('匹配到单条')
        worksheet3 = workbook.add_worksheet('匹配到多条')
        worksheet4 = workbook.add_worksheet('未匹配到相关项')
        worksheet1.set_column('A:A', 200)
        # worksheet1.set_row(0, 150)
        worksheet2.set_column('A:L', 25)
        worksheet2.set_row(0, 40)
        worksheet3.set_column('A:L', 25)
        worksheet3.set_row(0, 40)
        worksheet4.set_column('A:L', 25)
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

            for h in range(0, len(mult_msg)):
                if isinstance(mult_msg[h], int):
                    mult_num = mult_num + 1

            single_num = (len(single_msg) - 1) / 3
            empty_num = (len(empty_msg) - 1) / 2
            total_num = mult_num + single_num + empty_num

            msg_in = ['总 计 测 试 项：{0} 项'.format(int(total_num)),
                     '匹 配 到 单 项：{0} 项'.format(int(single_num)),
                     '存 在 差 异 数：{0} 处'.format(int(num_red / 2)),
                     '匹 配 到 多 项：{0} 项'.format(mult_num),
                     '未匹配到相关项：{0} 项'.format(int(empty_num)),
                     ' ',
                     '说明：',
                     '各测试项目详细信息见各 sheet 页。',
                     '每个 sheet 页第一行是测试项名称，白色背景短横线为分隔符,',
                     '同一种颜色标记的，两行中的第一行为导入的 txt 文件中的项,',
                     '其余项为在原始 EXCEl 表格中查找到的匹配项，红色为存在的差异处。',
                     '匹配到多项 sheet 页，txt中的条目在原始 EXCEL 中根据 MCCMNC 码和 ACCOUNT NAME 查找到多条匹配项时，',
                     '再次根据 APN 进行匹配最终匹配到的多个相似项。',
                     '未匹配到相关项 sheet 页，txt 文件中的项在原始 EXCEL 中未查找到相似项。']

            for w in range(0, len(msg_in)):
                worksheet1.write(w, 0, msg_in[w], bold_total)

            worksheet1.write(17, 0, '示例图：', bold_total)
            worksheet1.insert_image('A20', 'img02.png')
            worksheet1.insert_image('A40', 'img03.png')
            worksheet1.insert_image('A68', 'img04.png')

        workbook.close()
        self.progressBar.setProperty("value", 100)
        tk.messagebox.showinfo('COMPLETE', '测试完成，详细测试结果见测试报告！')
        self.progressBar.setProperty("value", 0)

    def complear_list(self, list1, list2):
        result_index = []
        if len(list1) == len(list2):
            for i in range(0, len(list1)):
                if list1[i] == list2[i]:
                    pass
                else:
                    if i not in[0, 2]:
                        result_index.append(i)
            return result_index
        else:
            result_index.append(-1)
            return result_index

    def complear_list_end(self, list1, list2):
        result_index = []
        list1 = list(list1)
        list2 = list(list2)
        if len(list1) == len(list2):
            for i in range(0, len(list2)):
                if i != 0 and i != 2 and i != 10:
                    if str(list1[i]).strip() == str(list2[i]).strip():
                        pass
                    else:
                        result_index.append(i)
                elif i == 10:
                    list1_10 = list1[10].upper()
                    list2_10 = list2[10].upper()
                    if not list1_10 == list2_10:
                        result_index.append(i)
            if result_index == []:
                result_index.append(-2)#完全匹配
            return result_index
        else:
            result_index.append(-1)# 全红色
            return result_index

    def conversion_apn(self, file_path):
        apn_list = ['', '', '', '', '', '', '', '', '', '', '', '']
        par_list = []
        fp = open(file_path, 'r')
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
                print('apn list ====', apn_list)
                tuple_apn = tuple(apn_list)
                par_list.append(tuple_apn)
                apn_list = ['', '', '', '', '', '', '', '', '', '', '', '']

        return par_list

    def get_database_table(self):
        try:
        # if True:
            conn = sqlite3.connect('xxxxxxx/APNTEST.db')
            cursor = conn.cursor()
            cursor.execute('''select * from UPDATEDATE''')
            table_name = cursor.fetchall()
            com_name = table_name[1][0] + '     更新时间： ' + str(table_name[1][1])
            if not com_name == self.comboBox_list.itemText(1):
                for i in range(0, len(table_name)):
                    com_name = table_name[i][0] + '     更新时间： ' + str(table_name[i][1])
                    self.comboBox_list.addItem(com_name)
        except:
            tk.messagebox.showerror('ERROR', '服务器链接错误！')
            cursor.close()
            conn.close()
            # return None

    def report_path(self):
        global report_path
        report_path = filedialog.askdirectory()
        self.lineEdit_apnpath_reportpath.setText(report_path)

    def get_apn_txt(self):
        global apn_path
        global file_name
        apn_path = filedialog.askopenfilename()
        if apn_path.endswith('.txt') | apn_path.endswith('.TXT'):
            self.lineEdit_apnpath.setText(apn_path)
            file_name = os.path.basename(apn_path)
        else:
            tkinter.messagebox.showerror('文件类型错误', '请选择正确的文件类型')
            return None

    def softAbout(self):
        tkinter.messagebox.showinfo('About', ' Date : 2018-11-07')

if __name__ == "__main__":
    app = QtWidgets.QApplication(sys.argv)  # 首先必须实例化QApplication类，作为GUI主程序入口
    MainWindow = MainWindow()  # 实例化MainWindow类，创建自带menu的窗体类型QMainWindow
    MainWindow.setFixedSize(772, 440)  # 禁止改变窗口大小
    MainWindow.show()  # 显示窗体
    sys.exit(app.exec_())  # 当来自操作系统的分发事件指派调用窗口时，
