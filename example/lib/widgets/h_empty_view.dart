import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';


/// 横向空组件（用于占位）
/// @author Yanghc
class HEmptyView extends StatelessWidget {
  final double width;

  // HEmptyView(this.width);
  const HEmptyView(this.width, {Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SizedBox(width: ScreenUtil().setWidth(width),);
  }
}
