#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2020/4/15 上午10:01
# @Author  : LiuXiangNan
# @Site    : 
# @File    : parseJson.py
import json
import re


def parse_json(id):
    """
    解析 json 文件
    :param id:
    :return:
    """
    f = open('./jsontest.json', encoding='utf-8')
    res = f.read()
    return json.loads(res)[id]


def get_type(id):
    if 'type' in parse_json(id):
        return parse_json(id)['type']
    else:
        return None


# def get_eventName(id):
#     return parse_json(id)['eventName']


def get_pkgname(id):
    if 'pkgName' in parse_json(id):
        return parse_json(id)['pkgName']
    else:
        return None


def get_classname(id):
    if 'className' in parse_json(id):
        return parse_json(id)['className']
    else:
        return None


def get_description(id):
    if 'description' in parse_json(id):
        return parse_json(id)['description']
    else:
        return None


def get_resourceId(id):
    if 'resourceId' in parse_json(id):
        return parse_json(id)['resourceId']
    else:
        return None


def get_text(id):
    if 'text' in parse_json(id):
        return parse_json(id)['text']
    else:
        return None


def get_clickAble(id):
    if 'clickAble' in parse_json(id):
        return parse_json(id)['clickAble']
    else:
        return None


def get_eventType(id):
    if 'eventType' in parse_json(id):
        return parse_json(id)['eventType']
    else:
        return None


def get_X(id):
    if 'X' in parse_json(id):
        return parse_json(id)['X']
    else:
        return None


def get_Y(id):
    if 'Y' in parse_json(id):
        return parse_json(id)['Y']
    else:
        return None


def get_dX(id):
    if 'dX' in parse_json(id):
        return parse_json(id)['dX']
    else:
        return None


def get_dY(id):
    if 'dY' in parse_json(id):
        return parse_json(id)['dY']
    else:
        return None


def get_step(id):
    if 'step' in parse_json(id):
        return parse_json(id)['step']
    else:
        return None


def get_scrollable(id):
    if 'scrollable' in parse_json(id):
        return parse_json(id)['scrollable']
    else:
        return None


def get_XPath(id):
    if 'XPath' in parse_json(id):
        return parse_json(id)['XPath']
    else:
        return None


def isApp(id):
    if re.match("app", get_type(id)):
        return True
    else:
        return False


def isControls(id):
    print("is controls")
    if re.match("controls", get_type(id)):
        return True
    else:
        return False
