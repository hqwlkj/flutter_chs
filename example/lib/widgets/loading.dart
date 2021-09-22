import 'package:flutter/material.dart';
import 'package:flutter_chs_example/widgets/v_empty_view.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';

/// 加载组件
/// @author Yanghc
class Loading {
  static bool isLoading = false;

  static void showLoading(BuildContext context,{String text}) {
    if (!isLoading) {
      isLoading = true;
      showGeneralDialog(
          context: context,
          barrierDismissible: false,
          barrierLabel:
              MaterialLocalizations.of(context).modalBarrierDismissLabel,
          transitionDuration: const Duration(milliseconds: 150),
          pageBuilder: (BuildContext context, Animation animation,
              Animation secondaryAnimation) {
            return Align(
              child: ClipRRect(
                borderRadius: BorderRadius.circular(10),
                child: Container(
                  width: ScreenUtil().setWidth(300),
                  height: ScreenUtil().setWidth(300),
                  color: Colors.black54,
                  child: text != null ? Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      SpinKitFadingCircle(
                        color: Colors.white,
                        size: ScreenUtil().setWidth(100),
                      ),
                      const VEmptyView(35),
                      Text(text, style: TextStyle(color: Colors.white, fontSize: ScreenUtil().setSp(28), fontWeight: FontWeight.w500, decoration: TextDecoration.none ),)
                    ],
                  ) :SpinKitFadingCircle(
                    color: Colors.white,
                    size: ScreenUtil().setWidth(100),
                  ),
                ),
              ),
            );
          }).then((v) {
        isLoading = false;
      });
    }
  }

  static void hideLoading(BuildContext context) {
    if (isLoading) {
      Navigator.of(context).pop();
    }
  }
}
