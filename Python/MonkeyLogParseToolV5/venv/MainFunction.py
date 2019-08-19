#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2019/4/22 16:41
# @Author  : LiuXiangNan
# @Site    : 
# @File    : MainFunction.py

from PyQt5 import QtWidgets
from MonkeyLogParseUI import Ui_MainWindow
# import sys
from tkinter import filedialog
import tkinter.messagebox
import tkinter as tk
import threading
import datetime
import configparser


root = tk.Tk()
root.withdraw()

config = configparser.ConfigParser()
config.read('setting.ini', encoding='utf-8')
java_exception = config.get('EXCEPTION_MAP', 'EXCEPTION')
exception_list = java_exception.split('|')


#############################################
log_path = ''
report_path = ''
project_name = ''
dir_name_list = []
patteran_e = r' E '
pattern_name = r'main_log'
java_exception = config.get('EXCEPTION_MAP', 'EXCEPTION')
exception_list = java_exception.split('|')

# conn = sqlite3.connect('./MonkeyParseLog.db', check_same_thread=False)
# conn = sqlite3.connect('')
# cursor = conn.cursor()
search_result = []
r1 = u'[a-zA-Z0-9’!"#$%&\'()*+,-./:;<=>?@，。?★、…【】《》？“”‘’！[\\]^`{|}~]+'  # 用户也可以在此进行自定义过滤字符
r2 = u'[\./:\\\]'
#############################################

class MainWindow(QtWidgets.QMainWindow, Ui_MainWindow):
    def __init__(self, parent=None):
        super(MainWindow, self).__init__(parent)
        self.setupUi(self)
        ################################################################################################################
        self.toolButton_path.clicked.connect(self.select_path)
        self.toolButton_path_report.clicked.connect(self.select_path_report)

        self.pushButton_start.clicked.connect(self.start_parse)
        self.pushButton_about.clicked.connect(self.about)
        self.pushButton_help.clicked.connect(self.softHelp)

        ################################################################################################################

    def about():
        tkinter.messagebox.showinfo('About', ' 仅供内部使用，未经授权请勿传播!'
                                             '\n '
                                             '\n Author  :  '
                                             '\n Mail  :   '
                                             '\n Org   :    '
                                             '\n Team  :   '
                                             '\n Version :   '
                                             '\n Date : 2018-12-19')

    def softHelp():
        tkinter.messagebox.showinfo('Help', 'monkey log解析工具！')


    def start_parse(self):
        global project_name
        project_name = self.lineEdit_name.text()
        if not project_name:
            tk.messagebox.showerror('ERROR', '请选择正确的项目名！')
            return
        if not log_path:
            tk.messagebox.showerror('ERROR', '请选择正确的文件路径！')
            return
        if not report_path:
            tk.messagebox.showerror('ERROR', '请选择正确的report输出路径！')
            return

        startThread = threading.Thread(target=self.start_parse_thread, name='start_parse_thread')
        startThread.start()



    def start_parse_thread(self):
        self.main_analysislog()
        print('search end')
        self.parse_analysisresult()

    """解析AnalysisResult文件"""  ###################################################################################

    def parse_analysisresult(self):
        pattern_JE = r'E AndroidRuntime:'
        reportName = '%s/%s_MainExceptionInfo.txt' % (report_path, project_name)
        fpw = open(reportName, 'w', encoding='utf-8')
        for i in range(0, len(search_result)):
            # buffer_fpName = re.sub(r2, '_', search_result[i])
            # print('analysis result path:', buffer_fpName)
            fpw.write('FILE PATH:  ' + search_result[i])
            fpw.write('\n\n')
            fpr = open(search_result[i], 'r')
            readLine = fpr.readlines()
            for readLineIndex in range(0, len(readLine)):
                for exIndex in range(0, len(exception_list)):
                    if re.search(r'%s' % (exception_list[exIndex]), str(readLine[readLineIndex]), re.I):
                        fpw.write(readLine[readLineIndex])
                        fpw.write('\n')

            fpr.close()
            fpw.write('**********************************************************************************\n\n')

        fpw.write('\n\n==================================================================================\n'
                  '各个具体异常信息请查看相应目录下的 analysis_result.txt 文件以及原始 MTKLOG 文件！\n'
                  '{0}\n=================================================================================='
                  .format(datetime.datetime.now().strftime('%Y%m%d_%H%M%S')))
        fpw.close()
        print('fpw end')
        self.pick_up_Exception()

    def pick_up_Exception(self):
        print('pick up je')
        target_JE_list = []
        target_ANR_list = []
        pattern_JE = r'E AndroidRuntime:'
        pattern_ANR = r'ANR in '
        for i in range(0, len(search_result)):
            fp = open(search_result[i], 'r')
            readLine = fp.readlines()
            for j in range(0, len(readLine)):
                write_readline = str(readLine[j]).strip()
                if re.search(pattern_JE, write_readline):
                    je_list = [search_result[i], write_readline]
                    target_JE_list.append(je_list)
                elif re.search(pattern_ANR, write_readline):
                    target_ANR_list.append(write_readline)

            print(target_JE_list)
        (dup_target_JE_list, je_pkg) = self.dup_JE_list('JE_result', target_JE_list)
        (dup_target_ANR_list, anr_mod) = self.dup_ANR_list('ANR_result', target_ANR_list)

        self.write_JE_to_txt('%s_JE_Info' % project_name, dup_target_JE_list)
        self.write_ANR_to_txt('%s_ANR_Info' % project_name, dup_target_ANR_list)
        self.write_JE_pkg(je_pkg)
        self.write_ANR_module(anr_mod)

        print('***********************************************************\n'
              '******* Analysis Log Complete, Please Check Report! *******\n'
              '***********************************************************\n'
              'Report Path : %s' % report_path)

    def write_JE_to_txt(self, fileName, write_list):
        # os.chdir(Path.cwd().parent)
        # print('write_JE_to_txt========', write_list)
        fileNameJEtxt = '%s/%s.txt' % (report_path, fileName)
        fpw = open(fileNameJEtxt, 'w', encoding='utf-8')
        for i in range(0, len(write_list)):
            # print(write_list[i] + '\n')
            write_path = write_list[i][1] + ' ' + str(write_list[i][2])
            fpw.write(write_path)
            fpw.write('\n')

            write_str1 = str(write_list[i][3]).strip() + ' ' + str(write_list[i][4]).strip() \
                         + ' ' + str(write_list[i][5]).strip() + ' ' + str(write_list[i][6]).strip() \
                         + ' ' + str(write_list[i][7]).strip() + ' ' + str(write_list[i][8]).strip()
            fpw.write(write_str1)
            fpw.write('\n')

            write_str2 = str(write_list[i][3]).strip() + ' ' + str(write_list[i][4]).strip() \
                         + ' ' + str(write_list[i][5]).strip() + ' ' + str(write_list[i][6]).strip() \
                         + ' ' + str(write_list[i][7]).strip() + ' ' + str(write_list[i][9]).strip()
            fpw.write(write_str2)
            fpw.write('\n')

            write_str3 = str(write_list[i][3]).strip() + ' ' + str(write_list[i][4]).strip() \
                         + ' ' + str(write_list[i][5]).strip() + ' ' + str(write_list[i][6]).strip() \
                         + ' ' + str(write_list[i][7]).strip() + ' ' + str(write_list[i][10]).strip()
            fpw.write(write_str3)
            fpw.write('\n\n')
        fpw.close()

    def write_JE_pkg(self, msg):
        # print('msg=================\n', msg)
        fileNameJEpkg = '%s/%s_JE_module.txt' % (report_path, project_name)
        fpw = open(fileNameJEpkg, 'w', encoding='utf-8')
        if msg == []:
            write_str = '未找到JE信息，请查看原始 LOG 文件！'
            fpw.write(write_str)
            fpw.write('\n')
        else:
            for i in range(0, len(msg)):
                write_str1 = str(msg[i][-1])
                fpw.write(write_str1)
                fpw.write('\n')
        fpw.close()

    def write_ANR_module(self, write_list):
        fileNameANRm = '%s/%s_ANR_module.txt' % (report_path, project_name)
        fpw = open(fileNameANRm, 'w', encoding='utf-8')
        if write_list == []:
            write_str = '未找到ANR信息，请查看原始文件！'
            fpw.write(write_str)
            fpw.write('\n')
        for i in range(0, len(write_list)):
            write_str = str(write_list[i][1])
            fpw.write(write_str)
            fpw.write('\n')
        fpw.close()

    def write_ANR_to_txt(self, fileName, write_list):
        # os.chdir(Path.cwd().parent)
        print('write_JE_to_txt:', Path.cwd())
        fileNameANRt = '%s/%s.txt' % (report_path, fileName)
        fpw = open(fileNameANRt, 'w', encoding='utf-8')
        if write_list == []:
            write_str = '未找到ANR信息，请查看原始文件！'
            fpw.write(write_str)
            fpw.write('\n')
        else:
            for i in range(0, len(write_list)):
                write_str = str(write_list[i][1]).strip() + ' ' + str(write_list[i][2]).strip() \
                            + ' ' + str(write_list[i][3]).strip() + '' + str(write_list[i][4]).strip()
                fpw.write(write_str)
                fpw.write('\n')
        fpw.close()

    def dup_JE_list(self, table_name, list1):  # 数据库去重
        # os.chdir(Path.cwd().parent)
        # insert_list = []
        # table_name = 'analysis_result'
        data_id = 0
        row_num = -5
        buffer_list = ['', '', '', '', '', '', '', '', '', '']
        print('dup list:', Path.cwd())
        conn = sqlite3.connect('./MonkeyParseLog.db')
        cursor = conn.cursor()
        cursor.execute('DROP TABLE IF EXISTS {0}'.format(table_name))
        cursor.execute('''CREATE TABLE {0}(
                                id INT PRIMARY KEY,
                                path STRING,
                                data STRING,
                                time STRING,
                                pid STRING,
                                tid STRING,
                                ear STRING,
                                msg1 STRING,
                                msg2 STRING,
                                msg3 STRING,
                                pkg STRING
                            )'''.format(table_name))
        for j in range(0, len(list1)):
            # if re.search(r'E AndroidRuntime: FATAL EXCEPTION:', list1[j]):
            # row_num = j
            if re.search(r'E AndroidRuntime: FATAL EXCEPTION:', list1[j][1]):
                (date, str1) = str(list1[j][1]).strip().split(' ', 1)
                (time, str2) = str(str1).strip().split(' ', 1)
                (pid, str3) = str(str2).strip().split(' ', 1)
                (tid, str4) = str(str3).strip().split(' ', 1)
                (e, str5) = str(str4).strip().split(' ', 1)
                (ar, str6) = str(str5).strip().split(' ', 1)
                are = e + ' ' + ar
                row_num = j
                # print('db row num===========', row_num)
                # print('list[j]==============', list1[j][1])
                # buffer_list[0] = row_num
                buffer_list[0] = date
                buffer_list[1] = time
                buffer_list[2] = pid
                buffer_list[3] = tid
                buffer_list[4] = are
                buffer_list[5] = str6
            elif j == row_num + 1 and j >= 5:
                (ex, str7) = str(list1[j][1]).strip().split(r'E AndroidRuntime:', 1)
                buffer_list[6] = str7
                (pkg, pid) = str(str7).strip().split(r',')
                buffer_list[8] = pkg
            elif j == row_num + 2 and j >= 5:
                (ex, str8) = str(list1[j][1]).strip().split(r'E AndroidRuntime:', 1)
                buffer_list[7] = str8
                data_id = data_id + 1
                insert_db = [data_id, list1[j][0], buffer_list[0], buffer_list[1], buffer_list[2], buffer_list[3],
                             buffer_list[4], buffer_list[5], buffer_list[6], buffer_list[7], buffer_list[8]]
                print(insert_db)
                cursor.executemany('INSERT INTO {0} VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)'.format(table_name),
                                   [insert_db])

        cursor.execute(
            'delete from {0} where id not in(select max(id) from {0} group by msg1,msg2,msg3)'.format(table_name))
        cursor.execute('SELECT * from {0}'.format(table_name))
        data = cursor.fetchall()
        cursor.execute('delete from {0} where id not in(select max(id) from {0} group by pkg)'.format(table_name))
        cursor.execute('SELECT * from {0}'.format(table_name))
        pkg_msg = cursor.fetchall()
        conn.commit()
        cursor.close()
        conn.close()
        return data, pkg_msg

    def dup_ANR_list(self, table_name, list1):
        # os.chdir(Path.cwd().parent)
        insert_list = []
        data_id = 0
        conn = sqlite3.connect('./MonkeyParseLog.db')
        cursor = conn.cursor()
        cursor.execute('DROP TABLE IF EXISTS {0}'.format(table_name))
        cursor.execute('''CREATE TABLE {0}(
                                    id INT PRIMARY KEY,
                                    pkg STRING,
                                    date STRING,
                                    time STRING,
                                    msg STRING
                                )'''.format(table_name))
        for i in range(0, len(list1)):
            if re.search(r'ANR in ', list1[i]):
                (pkg, str1) = str(list1[i]).split(' at ', 1)
                (date, str2) = str(str1).strip().split(' ', 1)
                (time, path) = str(str2).strip().split(' ', 1)
                data_id = data_id + 1
                insert_db = [data_id, pkg, date, time, path]
                print(insert_db)
                cursor.executemany('INSERT INTO {0} VALUES (?, ?, ?, ?, ?)'.format(table_name), [insert_db])

        cursor.execute('SELECT * from {0}'.format(table_name))
        anr_all_data = cursor.fetchall()
        cursor.execute('delete from {0} where id not in(select max(id) from {0} group by pkg)'.format(table_name))
        cursor.execute('SELECT * from {0}'.format(table_name))
        data = cursor.fetchall()
        conn.commit()
        cursor.close()
        conn.close()
        return anr_all_data, data

    """运行解析工具"""  ############################################################################################

    def main_analysislog(self):
        print('main analysis log')
        origin_path = Path.cwd()
        self.AnalysisLog_AQQT(log_path)
        print('analysis end, Generating related reports!')
        self.search(path=log_path, name='analysis_result.txt')
        os.chdir(origin_path)
        print('result : ', search_result)

    def AnalysisLog_AQQT(self, path_log):
        print('AnalysisLog_AQQT')
        print('path_log :', path_log)
        path = os.path.abspath('./tools/QAAT-ProGuard.jar')
        print('path :', path)
        os.chdir(str(path_log))
        for root1 in os.listdir('./'):
            if os.path.isdir(root1):
                os.chdir(root1)
                print('root2 current dir is :', Path.cwd())
                print('java -jar {0} -d'.format(path))
                analysis_result = subprocess.Popen('java -jar {0} -d'.format(path), shell=True,
                                                   stdout=subprocess.PIPE, stderr=subprocess.PIPE).communicate()
                print('analysis child complete!')
                os.chdir(Path.cwd().parent)

    def search(self, path=".", name=""):
        for item in os.listdir(path):
            item_path = os.path.join(path, item)
            if os.path.isdir(item_path):
                self.search(item_path, name)
            elif os.path.isfile(item_path):
                if name in item:
                    global search_result
                    search_result.append(item_path)
        # print('search end')

    ####################################################################################################################

    def select_path(self):
        global log_path
        log_path = filedialog.askdirectory()
        self.lineEdit_path.setText(log_path)
        # return log_path

    def select_path_report(self):
        global report_path
        report_path = filedialog.askdirectory()
        self.lineEdit_path_report.setText(report_path)
        # return report_path


def check_env(self):
    try:
        print('check test environment...')
        conn = sqlite3.connect('//192.168.3.88/sql//ToolCheckEnv.db')
        conn.close()
        print('check test environment ok!')
        return True
    except:
        tk.messagebox.showerror('ERROR', '无法运行该工具，该工具仅限内部使用,\n请确定已经正确连接内部服务器！')
        print('error environment')
        return False


if __name__ == "__main__":
    app = QtWidgets.QApplication(sys.argv)  # 首先必须实例化QApplication类，作为GUI主程序入口
    MainWindow = MainWindow()  # 实例化MainWindow类，创建自带menu的窗体类型QMainWindow
    MainWindow.setFixedSize(574, 306)  # 禁止改变窗口大小
    MainWindow.show()  # 显示窗体
    sys.exit(app.exec_())  # 当来自操作系统的分发事件指派调用窗口时，
