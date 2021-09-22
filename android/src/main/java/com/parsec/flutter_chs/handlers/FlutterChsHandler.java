package com.parsec.flutter_chs.handlers;

import android.content.Context;

import com.parsec.flutter_chs.loading.LoadingDialog;

import io.flutter.plugin.common.MethodChannel;

public class FlutterChsHandler {
    private static Context context;
    private static MethodChannel channel;

    private static LoadingDialog loadingDialog;

    public static  void initDialog(Context _context){
        loadingDialog = new LoadingDialog(_context);
    }
    public static void setContext(Context _context) {
        context = _context;
    }

    public static Context getContext() {
        return context;
    }

    public static void setChannel(MethodChannel _channel) {
        channel = _channel;
    }

    public static MethodChannel getChannel() {
        return channel;
    }

    public static void showDialog(String msg){
        loadingDialog.show(msg);
    }

    public static void hideDialog(){
        loadingDialog.hide();
    }
}
