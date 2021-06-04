import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_chs_example/utils/toast.dart';
import 'package:flutter_chs_example/widgets/loading.dart';

import 'custom_log_interceptor.dart';


/// 网络请求管理
/// @author Yanghc
class Request {
  static Dio _dio;

  ///  服务器请求地址
  static final String mockUrl = 'http://192.168.31.102:8866/YbService';
  static final String stageUrl = 'http://192.168.65.47:8866/YbService';
  static final String baseUrl = 'http://192.168.31.102:8866/YbService';

  static const int CONNECT_TIMEOUT = 1000 * 15;
  static const int RECEIVE_TIMEOUT = 3000;
  static const CONTENT_TYPE_JSON = "application/json; charset=utf-8";
  static const CONTENT_TYPE_FORM = "application/x-www-form-urlencoded; charset=utf-8";

  static void init() async {
    const bool inProduction = const bool.fromEnvironment("dart.vm.product");
    _dio = Dio(BaseOptions(
        baseUrl: inProduction ? '$baseUrl' : '$stageUrl',
        connectTimeout: CONNECT_TIMEOUT,
        receiveTimeout:  RECEIVE_TIMEOUT,
        followRedirects: false))
      ..interceptors.add(InterceptorsWrapper(onRequest: (RequestOptions options, RequestInterceptorHandler handler) async {
        // 在请求被发送之前做一些事情
        //Set the token to headers
        // options.headers[HttpHeaders.authorizationHeader] = 'Bearer $token'; // Authorization
        return handler.next(options); //continue
        // 如果你想完成请求并返回一些自定义数据，可以返回一个`Response`对象或返回`dio.resolve(data)`。
        // 这样请求将会被终止，上层then会被调用，then中返回的数据将是你的自定义数据data.
        //
        // 如果你想终止请求并触发一个错误,你可以返回一个`DioError`对象，或返回`dio.reject(errMsg)`，
        // 这样请求将被中止并触发异常，上层catchError会被调用。
      }, onResponse: (Response response, ResponseInterceptorHandler handler) async {
        // 在返回响应数据之前做一些预处理
        return handler.next(response); // continue
      }, onError: (DioError e, ErrorInterceptorHandler handler) async {
        // 当请求失败时做一些预处理
        return handler.next(e); //continue
      }))
      ..interceptors.add(CustomLogInterceptor(responseBody: true, requestBody: true));
  }

  static Future<Response> _dioErrorInterceptor(DioError e) {
    if (e == null) {
      return Future.error(Response(data: -1, requestOptions: null));
    }

    switch (e.type) {
      case DioErrorType.cancel:
        return Future.error(Response(data: -1, statusMessage: '请求取消', requestOptions: null));
      case DioErrorType.connectTimeout:
        ToastUtil.show("连接超时");
        return Future.error(Response(data: -1, statusMessage: '连接超时', requestOptions: null));
      case DioErrorType.sendTimeout:
        ToastUtil.show("请求超时");
        return Future.error(Response(data: -1, statusMessage: '请求超时', requestOptions: null));
      case DioErrorType.receiveTimeout:
        ToastUtil.show("响应超时");
        return Future.error(Response(data: -1, statusMessage: '响应超时', requestOptions: null));
      case DioErrorType.response:
        if (e.response.statusCode >= 300 && e.response.statusCode < 400) {
          return Future.error(Response(data: -1, requestOptions: null));
        } else if (e.response.statusCode == 403) {
          // _reLogin();
          return Future.error(Response(data: -1, requestOptions: null));
        } else if (e.response.statusCode == 404) {
          _notFound(); // 现在是弹窗提示，正确的是显示一个 页面
          return Future.error(Response(data: -1, requestOptions: null));
        } else {
          return Future.value(e.response);
        }
        break;
      default:
        return Future.value(e.response);
    }
  }

  static Future<Response> get(BuildContext context,
      String url, {
        Map<String, dynamic> params,
        Options options,
        bool isShowLoading = true,
      }) async {
   if (isShowLoading) Loading.showLoading(context);
    try {
      return await _dio.get(url, queryParameters: params, options: options);
    } on DioError catch (e) {
      return Request._dioErrorInterceptor(e);
    } finally {
     Loading.hideLoading(context);
    }
  }

  static Future<Response> post(BuildContext context,
      String url, {
        data,
        Map<String, dynamic> queryParameters,
        Options options,
        CancelToken cancelToken,
        ProgressCallback onSendProgress,
        ProgressCallback onReceiveProgress,
        bool isShowLoading = true,
      }) async {
    if (isShowLoading) Loading.showLoading(context);
    try {
      return await _dio.post(url,
          data: data,
          queryParameters: queryParameters,
          options: options,
          cancelToken: cancelToken,
          onReceiveProgress: onReceiveProgress,
          onSendProgress: onSendProgress);
    } on DioError catch (e) {
      return Request._dioErrorInterceptor(e);
    } finally {
      Loading.hideLoading(context);
    }
  }

  static Future<Response> delete(BuildContext context, String url,
      {data,
        Map<String, dynamic> queryParameters,
        Options options,
        CancelToken cancelToken,
        bool isShowLoading = true}) async {
    if (isShowLoading) Loading.showLoading(context);
    try {
      return await _dio.delete(url,
          data: data,
          queryParameters: queryParameters,
          options: options,
          cancelToken: cancelToken);
    } on DioError catch (e) {
      return Request._dioErrorInterceptor(e);
    } finally {
      Loading.hideLoading(context);
    }
  }

  static Future<Response> put(BuildContext context, String url,
      {data,
        Map<String, dynamic> queryParameters,
        Options options,
        CancelToken cancelToken,
        ProgressCallback onSendProgress,
        ProgressCallback onReceiveProgress,
        bool isShowLoading = true}) async {
    if (isShowLoading) Loading.showLoading(context);
    try {
      return await _dio.put(url,
          data: data,
          queryParameters: queryParameters,
          options: options,
          cancelToken: cancelToken,
          onSendProgress: onSendProgress,
          onReceiveProgress: onReceiveProgress);
    } on DioError catch (e) {
      return Request._dioErrorInterceptor(e);
    } finally {
      Loading.hideLoading(context);
    }
  }

  /// 404 了
  static void _notFound() {
    ToastUtil.show('访问的资源不存在');
  }
}
