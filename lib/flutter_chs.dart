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
  ///
  /// timeout     - 读卡超时时间（单位：秒）， 默认：5秒
  ///
  /// idCardKeys  - 需要返回的key信息，null 返回全部读取到的信息
  ///
  /// 可选key:
  /// * name 姓名
  /// * sex 性别
  /// * nation 国籍
  /// * id_number 身份证号码
  /// * useful_start_year 证件签发日期-年
  /// * useful_start_moth 证件签发日期-月
  /// * useful_start_day 证件签发日期-日
  /// * useful_end_year  证件终止日期-年
  /// * useful_end_moth  证件终止日期-月
  /// * useful_end_day 证件终止日期-日
  /// * birth_year 出生日期-年
  /// * birth_moth 出生日期-月
  /// * birth_day 出生日期-日
  /// * address 地址
  /// * sign_office 签发机关
  /// * version 证件版本号
  /// * flag 证件类型标志
  /// * reserved 预留项
  /// * photo 相片,  byte[]类型
  /// * fingerprint指纹信息， byte[]类型，为空时表示无指纹信息
  ///
  static Future<Map<String, dynamic>?> readIdCard({int? timeout, List<String>? idCardKeys}) async {
    return await _channel.invokeMapMethod('readIdCard', {'timeout': timeout, 'idCardKeys': idCardKeys});
  }

  /// 读取社保卡信息
  ///
  /// timeout        - 读卡超时时间，单位秒; 默认：30 秒
  ///
  /// healthCardKeys - 需要返回的key信息，null 返回全部读取到的信息
  ///
  /// 可选KEY:
  /// * name 姓名
  /// * cardNo 卡号
  /// * sex 性别
  /// * idCardNo 身份证号
  /// * districtCode 地区码
  static Future<Map<String, dynamic>?> readSocialSecurityCard(
      {int? timeout, List<String>? healthCardKeys}) async {
    return await _channel.invokeMapMethod('readSocialSecurityCard', {'timeout': timeout, 'healthCardKeys': healthCardKeys});
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
  ///
  /// timeout        - 读卡超时时间，单位秒; 默认：30 秒
  /// isIdCard       - 支持身份证 默认：true
  /// isScan         - 支持扫码 默认：true
  /// isHealthCard   - 支持社保卡 默认：true
  /// idCardKeys     - 身份证需要返回的key信息，null 返回全部读取到的信息
  ///    Note: 参数参考 [readIdCard] 的参数说明
  /// healthCardKeys - 社保卡需要返回的key信息，null 返回全部读取到的信息
  ///    Note: 参数参考 [readSocialSecurityCard] 的参数说明
  ///
  static Future<Map<String, dynamic>?> openReadDevice(
      {int? timeout,
      bool isIdCard = true,
      bool isScan = true,
      bool isHealthCard = true,
        List<String>? idCardKeys,
        List<String>? healthCardKeys
      }) async {
    return await _channel.invokeMapMethod('readDevice', {
      'timeout': timeout,
      'isIdCard': isIdCard,
      'isScan': isScan,
      'isHealthCard': isHealthCard,
      'idCardKeys': idCardKeys,
      'healthCardKeys': healthCardKeys,
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
