#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2018/12/19 14:48
# @Author  : LiuXiangNan
# @Site    : 
# @File    : AnalysisLog2.py
import os
import re
import sqlite3
import tkinter.messagebox
import tkinter as tk
from pathlib import Path
import subprocess
root = tk.Tk()
root.withdraw()
result = []
global origin_path


def about():
    tkinter.messagebox.showinfo('About', ' 仅供内部使用，未经授权请勿传播!'
                                         '\n '
                                         '\n Author  :  LiuXiangNan'
                                         '\n Mail  : '
                                         '\n Org   :    '
                                         '\n Team  :   '
                                         '\n Version :  '
                                         '\n Date : 2018-12-19')


def softHelp():
    tkinter.messagebox.showinfo('Help', ' 该工具用于封装 Android Monkey 测试命令！'
                                        '\n 简化测试人员对 Monkey 命令的使用'
                                        '\n Monkey 测试帮助请查看 Description 页面。')
def main_analysislog(path_log):
    print('main_analysislog')
    origin_path = Path.cwd()
    AnalysisLog_AQQT(path_log)
    search(path=path_log, name='analysis_result.txt')
    os.chdir(origin_path)
    write_list = pick_up_Exception()

    print('end')

def AnalysisLog_AQQT(path_log):
    print('AnalysisLog_AQQT')
    print('path_log ======', path_log)
    path = os.path.abspath('QAAT-ProGuard.jar')
    print('path =========', path)
    os.chdir(str(path_log))
    for root1 in os.listdir('./'):
        if os.path.isdir(root1):
            os.chdir(root1)
            print('root2 current dir is ====', Path.cwd())
            analysis_result = subprocess.Popen('java -jar {0} -d'.format(path), shell=True,
                                               stdout=subprocess.PIPE,stderr=subprocess.PIPE).communicate()
            print('analysis child end')
            os.chdir(Path.cwd().parent)
    print('analysis end')


def search(path=".", name=""):
    # print('search')
    for item in os.listdir(path):
        item_path = os.path.join(path, item)
        if os.path.isdir(item_path):
            search(item_path, name)
        elif os.path.isfile(item_path):
            if name in item:
                global result
                result.append(item_path)


def pick_up_Exception():
    print('pick up je')
    target_JE_list = []
    target_ANR_list = []
    pattern_JE = r'E AndroidRuntime:'
    pattern_ANR = r'ANR in '
    for i in range(0, len(result)):
        fp = open(result[i], 'r')
        readLine = fp.readlines()
        for j in range(0, len(readLine)):
            write_readline = str(readLine[j]).strip()
            # print('write_readline===========', write_readline)
            if re.search(pattern_JE, write_readline):
                je_list = [result[i], write_readline]
                target_JE_list.append(je_list)
            elif re.search(pattern_ANR, write_readline):
                target_ANR_list.append(write_readline)

    print(target_JE_list)


    (dup_target_JE_list, je_pkg) = dup_JE_list('JE_result', target_JE_list)
    (dup_target_ANR_list, anr_mod) = dup_ANR_list('ANR_result', target_ANR_list)

    write_JE_to_txt('EXCEPTION', dup_target_JE_list)
    write_ANR_to_txt('ANR', dup_target_ANR_list)
    write_JE_pkg(je_pkg)
    write_ANR_module(anr_mod)


def write_JE_pkg(msg):
    # print('msg=================\n', msg)
    fpw = open('./report/Exception_module.txt', 'w', encoding='utf-8')
    for i in range(0, len(msg)):
        write_str1 = str(msg[i][-1])
        fpw.write(write_str1)
        fpw.write('\n')
    fpw.close()


def write_JE_to_txt(fileName, write_list):
    # os.chdir(Path.cwd().parent)
    # print('write_JE_to_txt========', write_list)
    fpw = open('./report/{0}.txt'.format(fileName), 'w', encoding='utf-8')
    for i in range(0, len(write_list)):
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


def write_ANR_module(write_list):
    fpw = open('./report/ANR_module.txt', 'w', encoding='utf-8')
    for i in range(0, len(write_list)):
        write_str = str(write_list[i][1])
        fpw.write(write_str)
        fpw.write('\n')
    fpw.close()


def write_ANR_to_txt(fileName, write_list):
    # os.chdir(Path.cwd().parent)
    print('write_JE_to_txt========', Path.cwd())
    fpw = open('./report/{0}.txt'.format(fileName), 'w', encoding='utf-8')
    for i in range(0, len(write_list)):
        write_str = str(write_list[i][1]).strip() + ' ' + str(write_list[i][2]).strip() \
                    + ' ' + str(write_list[i][3]).strip() + '' + str(write_list[i][4]).strip()
        fpw.write(write_str)
        fpw.write('\n')
    fpw.close()
def dup_ANR_list(table_name, list1):
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

def dup_JE_list(table_name, list1):  # 数据库去重
    # os.chdir(Path.cwd().parent)
    # insert_list = []
    # table_name = 'analysis_result'
    data_id = 0
    row_num = -5
    buffer_list = ['', '', '', '', '', '', '', '', '', '']
    print('dup list========', Path.cwd())
    conn = sqlite3.connect('./MonkeyParseLog.db')
    cursor = conn.cursor()
    cursor.execute('DROP TABLE IF EXISTS {0}'.format(table_name))
    cursor.execute('''CREATE TABLE {0}(
                        id INT PRIMARY KEY,
                        path STRING,
                        row_num INT,
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
            buffer_list[0] = row_num
            buffer_list[1] = date
            buffer_list[2] = time
            buffer_list[3] = pid
            buffer_list[4] = tid
            buffer_list[5] = are
            buffer_list[6] = str6
        elif j == row_num + 1 and j >= 5:
            (ex, str7) = str(list1[j][1]).strip().split(r'E AndroidRuntime:', 1)
            buffer_list[7] = str7
            (pkg, pid) = str(str7).strip().split(r',')
            buffer_list[9] = pkg
        elif j == row_num + 2 and j >= 5:
            (ex, str8) = str(list1[j][1]).strip().split(r'E AndroidRuntime:', 1)
            buffer_list[8] = str8
            data_id = data_id + 1
            insert_db = [data_id, list1[j][0], buffer_list[0], buffer_list[1], buffer_list[2], buffer_list[3],
                         buffer_list[4], buffer_list[5], buffer_list[6], buffer_list[7], buffer_list[8], buffer_list[9]]
            cursor.executemany('INSERT INTO {0} VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)'.format(table_name),
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


