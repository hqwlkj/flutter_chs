package com.parsec.flutter_chs.handlers;

import android.content.Context;
import io.flutter.plugin.common.MethodChannel;

public class FlutterChsHandler {
    private static Context context;
    private static MethodChannel channel;

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
}
