#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2020/4/15 下午1:17
# @Author  : LiuXiangNan
# @Site    : 
# @File    : runUiautomator2.py

import uiautomator2 as u2
from logzero import logger
from LXNUIAutomator2.parseJson import *

click = "click"
longClick = "longClick"
waitTime = 30


def execute_u2(d, id):
    if isApp(id):
        logger.debug("luncher app:{0}".format(get_pkgname(id)))
        d.app_start(get_pkgname(id))
    elif isControls(id):
        if get_resourceId(id):
            resourceId(d, id)
        elif get_text(id):
            text(d, id)
        elif get_description(id):
            description(d, id)
        elif get_X(d, id):
            coordinate(id)
        elif get_XPath(d, id):
            XPath(id)


def resourceId(d, id):
    """
    根据 resourceid 来操作控件
    :param d:
    :param id:
    :return:
    """
    logger.debug("resource id:{0}".format(get_resourceId(id)))
    if re.match(click, get_eventType(id)):
        d(scrollable=get_scrollable).scroll.to(resourceId=get_resourceId(id))
        d(resourceId=get_resourceId(id)).click(timeout=waitTime)


def text(d, id):
    """
    根据 text 来操作控件
    :param d:
    :param id:
    :return:
    """
    logger.debug("text: {0}".format(get_text(id)))
    if re.match(click, get_eventType(id)):
        d(scrollable=get_scrollable(id)).scroll.to(text=get_text(id))
        d(text=get_text(id)).click(timeout=waitTime)


def description(d, id):
    """
    根据 description 来操作控件
    :param d:
    :param id:
    :return:
    """
    logger.debug("description:{0}".format(get_description(id)))
    if re.match(click, get_eventType(id)):
        d(scrollable=get_scrollable(id)).scroll.to(description=get_description(id))
        d(description=get_description(id)).click(timeout=waitTime)


def coordinate(d, id):
    """
    根据 坐标 来操作控件
    :param d:
    :param id:
    :return:
    """
    logger.debug("X:{0},   Y:{1}".format(get_X(id), get_Y(id)))
    if re.match(click, get_eventType(id)):
        d.click(get_X(id), get_Y(id))


def XPath(d, id):
    """
    根据 XPath 来操作控件
    :param d:
    :param id:
    :return:
    """
    logger.debug("XPath:{0}".format(get_XPath(id)))
    if re.match(click, get_eventType(id)):
        d.xpath(get_XPath(id)).click(timeout=waitTime)
