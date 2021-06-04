import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';

class ToastUtil{
  static void show(String msg,
      [ToastGravity gravity = ToastGravity.BOTTOM]){
    Fluttertoast.showToast(
        msg: msg,
        toastLength: Toast.LENGTH_SHORT,
        backgroundColor: Colors.black87,
        gravity: gravity,
        textColor: Colors.white);
  }
}
