# LXN_UIAutomator2
对 UIAutomator2 的二次封装,使用JSON文件编写case

UIAutomator2 的二次封装.初版(Author:LiuXiangNan)
1. 将原来 UIAutomator2 的 API 进行了二次封装,可以使用 JSON 文件来编写测试 case 实现灵活配置.
2. 该封装的主要文件(暂命名),
	jsontest.json        JSON文件用于编写测试 case,主要控件信息.
	parseJson.py         解析JSON文件,返回控件信息
	runUiautomator2.py   对外调用接口,根据 id 值调用相应的 json 文件信息
3. 已完成功能;
	通过 type 标签识别操作类型, app 则为启动app动作, controls 为控件信息.
	eventType 事件类型,点击,长按,拖拽滑动等,目前只实现了 点击 其余待后续添加.
	其余标签皆为控件信息标签,
4. 使用
	4.1 代码中定义 caselist = ["id_1", "id_2"....] 该顺序决定执行case的顺序, 可在配置文件中定义.
	4.2 JSON 文件中按照模板填写case信息
	4.3 调用 
			d = u2.connect_usb()
			caselist = ["id_1", "id_2", "id_3", "id_4", "id_5"]
			for case in caselist:
        		execute_u2(d, case)
