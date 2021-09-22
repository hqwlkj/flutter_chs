import 'dart:async';

import 'package:common_utils/common_utils.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_chs/flutter_chs.dart';
import 'package:flutter_chs_example/services/home_service.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

class HomePage extends StatefulWidget {
  const HomePage({Key key}) : super(key: key);

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  String _platformVersion = 'Unknown';
  Map<String, dynamic> _result;
  String _response = '';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await FlutterChs.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  void _closeResult() {
    setState(() {
      _result = null;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('中国医疗保障 DEMO :$_platformVersion'),
      ),
      body: Column(
        children: [
          Row(
            children: [
              TextButton(
                  onPressed: () async {
                    Map<String, dynamic> result = await FlutterChs.scanOpen();
                    print("中国医疗保障 DEMO: $result");
                    setState(() {
                      _result = result;
                    });
                  },
                  child: const Text('扫码开')),
              TextButton(
                  onPressed: () async {
                    Map<String, dynamic> result = await FlutterChs.readIdCard(timeout: 10);
                    print("中国医疗保障 DEMO: $result");
                    setState(() {
                      _result = result;
                    });
                  },
                  child: const Text('身份证开')),
              TextButton(
                  onPressed: () async {
                    Map<String, dynamic> result =
                    await FlutterChs.readSocialSecurityCard();
                    print("中国医疗保障 DEMO: $result");
                    setState(() {
                      _result = result;
                    });
                  },
                  child: const Text('社保卡开')),
              TextButton(
                  onPressed: () async {
                    Map<String, dynamic> result = await FlutterChs.openReadDevice();
                    print("中国医疗保障 DEMO: $result");
                    setState(() {
                      _result = result;
                    });
                  },
                  child: const Text('多合一读卡开')),
            ],
          ),
          Row(
            children: [
              TextButton(
                  onPressed: () async {
                    await FlutterChs.closeScan;
                    _closeResult();
                  },
                  child: const Text('扫码关')),
              TextButton(
                  onPressed: () async {
                    await FlutterChs.closeReadIdCard;
                    _closeResult();
                  },
                  child: const Text('身份证关')),
              TextButton(
                  onPressed: () async {
                    await FlutterChs.closeReadSocialSecurityCard;
                    _closeResult();
                  },
                  child: const Text('社保卡关')),
              TextButton(
                  onPressed: () async {
                    await FlutterChs.closeDevice;
                    _closeResult();
                  },
                  child: const Text('多合一读卡关')),
            ],
          ),
          Row(
            children: [
              TextButton(
                  onPressed: () async {
                    Map<String, dynamic> result =
                    await FlutterChs.openReadHospitalCard;
                    print(result);
                    setState(() {
                      _result = result;
                    });
                  },
                  child: const Text('就诊卡开')),
              TextButton(
                  onPressed: () async {
                    await FlutterChs.beepSwitchOpen;
                  },
                  child: const Text('开启蜂鸣器总开关')),
              TextButton(
                  onPressed: () async {
                    Map<String, dynamic> result = await FlutterChs.findIdCard;
                    FlutterChs.showLoading;
                    print("中国医疗保障 DEMO: $result");
                    setState(() {
                      _result = result;
                    });
                    FlutterChs.hideLoading;
                  },
                  child: const Text('寻身份证')),
            ],
          ),
          Row(
            children: [
              TextButton(
                  onPressed: () async {
                    await FlutterChs.closeReadHospitalCard;
                    _closeResult();
                  },
                  child: const Text('就诊卡关')),
              TextButton(
                  onPressed: () async {
                    await FlutterChs.beepSwitchClose;
                  },
                  child: const Text('关闭蜂鸣器总开关')),
              TextButton(
                  onPressed: () async {
                    Map<String, dynamic> result =
                    await FlutterChs.findHealthCard;
                    print("中国医疗保障 DEMO: $result");
                    setState(() {
                      _result = result;
                    });
                  },
                  child: const Text('寻社保卡')),
              TextButton(
                  onPressed: ()  {
                    FlutterChs.showLoading();
                    /// 3 秒后自动关闭
                    Future.delayed(const Duration(milliseconds: 3000), () {
                      FlutterChs.hideLoading; // 3s之后关闭LOADING
                    });
                  },
                  child: const Text('显示LOADING')),
              TextButton(
                  onPressed: ()  {
                    FlutterChs.showLoading(msg: '加载中...');
                    /// 3 秒后自动关闭
                    Future.delayed(const Duration(milliseconds: 3000), () {
                      FlutterChs.hideLoading; // 3s之后关闭LOADING
                    });
                  },
                  child: const Text('自定义LOADING'))
            ],
          ),
          Expanded(
              child: Container(
                width: double.infinity,
                height: ScreenUtil().setWidth(240),
                margin: const EdgeInsets.all(20),
                padding: const EdgeInsets.all(20),
                decoration: BoxDecoration(
                    border: Border.all(color: const Color(0xff333333), width: 0.5),
                    borderRadius: BorderRadius.circular(10)),
                child: SingleChildScrollView(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text("状态码：${_result != null ? _result['code'] : '--'}"),
                      Text("消    息：${_result != null ? _result['message'] : '--'}"),
                      Text("数    据：${_result != null ? _result['data'] : '--'}"),
                    ],
                  ),
                ),
              )),
          Expanded(
              child: Column(
                children: [
                  Row(
                    children: [
                      TextButton(
                          onPressed: () async {
                            Response response =
                            await HomeService.getYbPeopleinfo(context, params: {
                              "GetYbPeopleinfoResponse": {'CardNo': 'E43746648'}
                            });
                            LogUtil.e('=================获取人员基础信息===============');
                            LogUtil.e(response);
                            setState(() {
                              _response = response.toString();
                            });
                          },
                          child: const Text('获取人员基础信息'))
                    ],
                  ),
                  Container(
                    width: double.infinity,
                    height: ScreenUtil().setWidth(240),
                    margin: const EdgeInsets.all(20),
                    padding: const EdgeInsets.all(20),
                    decoration: BoxDecoration(
                        border: Border.all(color: const Color(0xff333333), width: 0.5),
                        borderRadius: BorderRadius.circular(10)),
                    child: SingleChildScrollView(
                      child: Text(_response),
                    ),
                  )
                ],
              ))
        ],
      ),
    );
  }
}
