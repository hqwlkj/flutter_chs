import 'package:common_utils/common_utils.dart';
import 'package:flutter/material.dart';
import 'package:flutter_chs_example/pages/home.dart';
import 'package:flutter_chs_example/utils/request.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

void main() {
  LogUtil.init(tag: 'CHS_APP');
  Request.init();
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key key}) : super(key: key);

  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return ScreenUtilInit(
        designSize: const Size(750, 1334),
        builder: () => MaterialApp(
              debugShowCheckedModeBanner: false,
              title: 'CHS-FLUTTER-APP',
              theme: ThemeData(
                primarySwatch: Colors.blue,
                buttonTheme: ButtonThemeData(shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(18.0),
                    side: const BorderSide(color: Colors.red)),)
              ),
              builder: (context, widget) {
                return MediaQuery(
                  //设置字体不随系统字体大小变化
                  data: MediaQuery.of(context).copyWith(textScaleFactor: 1.0),
                  child: widget,
                );
              },
              home: const HomePage(),
            ));
  }
}
