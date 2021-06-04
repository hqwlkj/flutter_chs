import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_chs_example/utils/request.dart';

class HomeService {
  /// 获取人员基础信息
  static Future<Response> getYbPeopleinfo(BuildContext context, {@required Map<String, dynamic> params}) async {
    return await Request.post(context, '/getYbPeopleinfo', data: params);
  }
}
