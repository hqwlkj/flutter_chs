
<p align="center">
  <a href="http://www.cqkqinfo.com/">
    <img width="120" src="http://www.cqkqinfo.com/images/home/logo.png">
  </a>
<a href="https://www.newlandpayment.cn/">
    <img width="120" src="https://www.newlandpayment.cn/temp/temp/52/52-0201/202003030938/logo.png">
  </a>
</p>

<h1 align="center">Flutter CHS</h1>

CHINA HEALTHCARE SECURITY 缩写：CHS

中国医疗保障局（福建新大陆支付技术有限公司）Flutter插件

如果需要微信刷脸的功能请安装 [wechat_face_payment](https://pub.dev/packages/wechat_face_payment) 插件。

## 安装依赖

```dart
  // Run this command:
  flutter pub add flutter_chs

```
这将在包的 pubspec.yaml 中添加这样的一行（并运行一个隐式 dart pub get）：
```dart
    dependencies:
      dio: ^0.0.1
```
或者，您的编辑器可能支持 dart pub get 或 flutter pub get。 查看您的编辑器的文档以了解更多信息。
## 导入依赖

现在在您的 Dart 代码中，您可以使用：
```dart
    import 'package:flutter_chs/flutter_chs.dart';
```

## 使用


<li>读取身份证</li>

```dart
    Map<String, dynamic> result = await FlutterChs.readIdCard(timeout: 10);
    print("DEMO-RESULT: $result");
```


<li>读取社保卡</li>

```dart
    Map<String, dynamic> result = await FlutterChs.readSocialSecurityCard();
    print("DEMO-RESULT: $result");
```


<li>开启扫码</li>

```dart
    Map<String, dynamic> result = await FlutterChs.scanOpen(timeout: 10);
    print("DEMO-RESULT: $result");
```


<li>开启蜂鸣器总开关</li>

```dart
    await FlutterChs.beepSwitchOpen;
```

<li>开启蜂鸣器总开关</li>

```dart
    await FlutterChs.beepSwitchClose;
```

<li>寻身份证</li>

```dart
    Map<String, dynamic> result = await FlutterChs.findIdCard;
    print("DEMO-RESULT: $result");
```

<li>寻社保卡</li>

```dart
    Map<String, dynamic> result = await FlutterChs.findHealthCard;
    print("DEMO-RESULT: $result");
```

<li>多合一（NFC、IC卡、扫描）</li>

```dart
    /// 可选参数说明
    /// timeout       - 读卡超时时间，单位秒; 默认：30 秒
    /// isIdCard      - 支持身份证 默认：true
    /// isScan        - 支持扫码 默认：true
    /// isHealthCard  - 支持社保卡 默认：true

    Map<String, dynamic> result = await FlutterChs.openReadDevice()
    print("DEMO-RESULT: $result");
```


<li>LOADING</li>

```dart
    /// 显示默认的loading
    
    FlutterChs.showLoading();

    /// 显示自定义 `msg` 的 loading

    FlutterChs.showLoading(msg: '加载中...');

    /// 关闭 loading
    FlutterChs.hideLoading;
    
```

返回的数据结构及说明

```json

{
  "code": int, // 状态码 0：成功 1：失败
  "message": String, // 成功或失败对应的提示信息
  "data": String // 成功后返回对应的数据
}
```
