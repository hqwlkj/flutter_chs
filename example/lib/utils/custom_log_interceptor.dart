import 'package:common_utils/common_utils.dart';
import 'package:dio/dio.dart';

class CustomLogInterceptor extends LogInterceptor {
  CustomLogInterceptor({
    request = true,
    requestHeader = true,
    requestBody = false,
    responseHeader = true,
    responseBody = false,
    error = true,
    logPrint = print,
  }) : super(
            request: request,
            requestHeader: requestHeader,
            requestBody: requestBody,
            responseHeader: responseHeader,
            responseBody: responseBody,
            error: error,
            logPrint: logPrint);

  printKV(String key, Object v) {
    LogUtil.e('$key: $v');
  }

  printAll(msg) {
    LogUtil.e('$msg');
  }
}
