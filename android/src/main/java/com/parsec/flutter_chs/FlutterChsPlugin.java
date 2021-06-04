package com.parsec.flutter_chs;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.parsec.flutter_chs.handlers.FlutterChsHandler;
import com.parsec.flutter_chs.services.ChsService;
import com.parsec.flutter_chs.utils.ICallback;
import com.parsec.flutter_chs.utils.LoggerUtil;

import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * FlutterChsPlugin
 */
public class FlutterChsPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private final Handler uiThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_chs");
        FlutterChsHandler.setContext(flutterPluginBinding.getApplicationContext());
        FlutterChsHandler.setChannel(channel);
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        LoggerUtil.e("call_method: " + call.method);
        Integer timeout = call.argument("timeout");
        LoggerUtil.e("readTime:" + timeout);
        switch (call.method) {
            case "getPlatformVersion":
                result.success("Android " + android.os.Build.VERSION.RELEASE);
                break;
            case "beepSwitchOpen":
                ChsService.beepSwitchOpen(FlutterChsHandler.getContext());
                break;
            case "beepSwitchClose":
                ChsService.beepSwitchClose(FlutterChsHandler.getContext());
                break;
            case "scanOpen":
                if (timeout == null)
                    timeout = 30; // 扫码超时时间，默认30 秒
                scanOpen(result, timeout);
                break;
            case "readIdCard":
                if (timeout == null)
                    timeout = 5; // 默认为 5 秒
                readIdCard(result, timeout);
                break;
            case "readSocialSecurityCard":
                if (timeout == null)
                    timeout = 30; // 扫码超时时间，默认30 秒
                readSocialSecurityCard(result, timeout);
                break;
            case "closeScan":
                ChsService.closeScan();
                break;
            case "closeReadIdCard":
                ChsService.closeReadIdCard();
                break;
            case "closeReadSocialSecurityCard":
                ChsService.closeReadSocialSecurityCard();
                break;
            case "openReadHospitalCard":
                openReadHospitalCard(result);
                break;
            case "closeReadHospitalCard":
                ChsService.closeReadHospitalCard();
                break;
            case "findIdCard":
                findIdCard(result);
                break;
            case "findHealthCard":
                findHealthCard(result);
                break;
            case "readDevice":
                if (timeout == null)
                    timeout = 30; // 扫码超时时间，默认30 秒
                boolean isIdCard = call.argument("isIdCard");
                boolean isScan = call.argument("isScan");
                boolean isHealthCard = call.argument("isHealthCard");
                readDevice(result, timeout, isIdCard, isScan, isHealthCard);
                break;
            case "closeDevice":
                ChsService.closeDevice();
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    private void readDevice(@NonNull final Result result, Integer timeout, boolean isIdCard, boolean isScan, boolean isHealthCard) {
        ChsService.readDevice(FlutterChsHandler.getContext(), timeout, isIdCard, isScan, isHealthCard, new ICallback() {
            @Override
            public void callback(final Map<String, Object> params) {
                uiThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        result.success(params);
                    }
                });
            }
        });
    }

    private void scanOpen(@NonNull final Result result, final Integer timeout) {
        ChsService.scanOpen(FlutterChsHandler.getContext(), timeout, new ICallback() {
            @Override
            public void callback(final Map<String, Object> params) {
                uiThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        result.success(params);
                    }
                });
            }
        });
    }

    private void readIdCard(@NonNull final Result result, final Integer timeout) {
        ChsService.readIdCard(FlutterChsHandler.getContext(), timeout, new ICallback() {
            @Override
            public void callback(final Map<String, Object> params) {
                uiThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        result.success(params);
                    }
                });
            }
        });
    }

    private void readSocialSecurityCard(@NonNull final Result result, final Integer timeout) {
        ChsService.readSocialSecurityCard(FlutterChsHandler.getContext(), timeout, new ICallback() {
            @Override
            public void callback(final Map<String, Object> params) {
                uiThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        result.success(params);
                    }
                });
            }
        });
    }

    private void openReadHospitalCard(@NonNull final Result result) {
        ChsService.openReadHospitalCard(FlutterChsHandler.getContext(), new ICallback() {
            @Override
            public void callback(final Map<String, Object> params) {
                uiThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        result.success(params);
                    }
                });
            }
        });
    }

    private void findIdCard(@NonNull final Result result) {
        ChsService.findIdCard(FlutterChsHandler.getContext(), new ICallback() {
            @Override
            public void callback(final Map<String, Object> params) {
                uiThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        result.success(params);
                    }
                });
            }
        });
    }

    private void findHealthCard(@NonNull final Result result) {
        ChsService.findHealthCard(FlutterChsHandler.getContext(), new ICallback() {
            @Override
            public void callback(final Map<String, Object> params) {
                uiThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        result.success(params);
                    }
                });
            }
        });
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onDetachedFromActivity() {
        // TODO("Not yet implemented")
    }


    @Override
    public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
        // TODO("Not yet implemented")
    }


    @Override
    public void onAttachedToActivity(ActivityPluginBinding binding) {
        FlutterChsHandler.setContext(binding.getActivity());
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        // TODO("Not yet implemented")
    }
}
