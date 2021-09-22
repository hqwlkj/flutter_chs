import 'dart:async';

import 'package:flutter/services.dart';

class FlutterChs {
  static const MethodChannel _channel = MethodChannel('flutter_chs');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  /// 开启扫码
  /// timeout - 扫码超时时间，单位秒; 默认：30 秒
  static Future<Map<String, dynamic>?> scanOpen({int? timeout}) async {
    return await _channel.invokeMapMethod('scanOpen', {'timeout': timeout});
  }

  /// 读取身份证
  /// timeout 读卡超时时间（单位：秒）， 默认：5秒
  static Future<Map<String, dynamic>?> readIdCard({int? timeout}) async {
    return await _channel.invokeMapMethod('readIdCard', {'timeout': timeout});
  }

  /// 读取社保卡信息
  /// timeout - 读卡超时时间，单位秒; 默认：30 秒
  static Future<Map<String, dynamic>?> readSocialSecurityCard(
      {int? timeout}) async {
    return await _channel.invokeMapMethod('readSocialSecurityCard', {'timeout': timeout});
  }

  /// 扫码关 closeScan
  static Future<void> get closeScan async {
    await _channel.invokeMethod('closeScan');
  }

  /// 身份证关 closeReadIdCard
  static Future<void> get closeReadIdCard async {
    await _channel.invokeMethod('closeReadIdCard');
  }

  /// 社保卡关 closeReadSocialSecurityCard
  static Future<void> get closeReadSocialSecurityCard async {
    await _channel.invokeMethod('closeReadSocialSecurityCard');
  }

  /// 就诊卡开 openReadHospitalCard
  static Future<Map<String, dynamic>?> get openReadHospitalCard async {
    return await _channel.invokeMapMethod('openReadHospitalCard');
  }

  /// 开启蜂鸣器总开关
  static Future<void> get beepSwitchOpen async {
    await _channel.invokeMethod('beepSwitchOpen');
  }

  /// 寻身份证 findIdCard
  static Future<Map<String, dynamic>?> get findIdCard async {
    return await _channel.invokeMapMethod('findIdCard');
  }

  /// 就诊卡关 closeReadHospitalCard
  static Future<void> get closeReadHospitalCard async {
    await _channel.invokeMethod('closeReadHospitalCard');
  }

  /// 关闭蜂鸣器总开关
  static Future<void> get beepSwitchClose async {
    await _channel.invokeMethod('beepSwitchClose');
  }

  /// 寻社保卡  findHealthCard
  static Future<Map<String, dynamic>?> get findHealthCard async {
    return await _channel.invokeMapMethod('findHealthCard');
  }

  /// 多合一读卡
  /// timeout       - 读卡超时时间，单位秒; 默认：30 秒
  /// isIdCard      - 支持身份证 默认：true
  /// isScan        - 支持扫码 默认：true
  /// isHealthCard  - 支持社保卡 默认：true
  static Future<Map<String, dynamic>?> openReadDevice(
      {int? timeout,
      bool isIdCard = true,
      bool isScan = true,
      bool isHealthCard = true}) async {
    return await _channel.invokeMapMethod('readDevice', {
      'timeout': timeout,
      'isIdCard': isIdCard,
      'isScan': isScan,
      'isHealthCard': isHealthCard,
    });
  }

  /// 关闭多合一读卡器
  static Future<void> get closeDevice async {
    await _channel.invokeMethod('closeDevice');
  }

  /// 显示LOADNIG
  static Future<void> showLoading({String? msg}) async {
    await _channel.invokeMethod('showLoading', {'msg': msg});
  }

  /// 关闭LOADING
  static Future<void> get hideLoading async {
    await _channel.invokeMethod('hideLoading');
  }
}
