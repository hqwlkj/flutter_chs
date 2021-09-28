package com.parsec.flutter_chs.services;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;

import androidx.annotation.RequiresApi;

import com.parsec.flutter_chs.SDKExecutors;
import com.parsec.flutter_chs.constants.Constant;
import com.parsec.flutter_chs.utils.FormatDataUtils;
import com.parsec.flutter_chs.utils.ICallback;
import com.parsec.flutter_chs.utils.LoggerUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hsa.ctp.device.sdk.managerService.aidl.Beeper;
import cn.hsa.ctp.device.sdk.managerService.aidl.HealthCardReader;
import cn.hsa.ctp.device.sdk.managerService.aidl.HospitalCardReader;
import cn.hsa.ctp.device.sdk.managerService.aidl.HybridReader;
import cn.hsa.ctp.device.sdk.managerService.aidl.IDCardReader;
import cn.hsa.ctp.device.sdk.managerService.aidl.OnScanListener;
import cn.hsa.ctp.device.sdk.managerService.aidl.Scanner;

public class ChsService {
    /**
     * 读身份证
     */
    private static IDCardReader idCardReader;
    /**
     * 扫码
     */
    private static Scanner scanner;
    /**
     * 社保（医保）卡
     */
    private static HealthCardReader healthCardReader;
    /**
     * 4442晋安就诊卡卡
     */
    private static HospitalCardReader hospitalCardReader;
    /**
     * 蜂鸣器总开关
     */
    private static Beeper beeper;

    /**
     * 多合一读卡
     */
    private static HybridReader hybridReader;

    /**
     * 开启扫码
     *
     * @param context  上下文环境
     * @param timeout  超时时间，单位:秒
     * @param callback 结果回调函数
     */
    public static void scanOpen(final Context context, final Integer timeout, final ICallback callback) {
        SDKExecutors.getThreadPoolInstance().submit(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<String, Object>();
                try {
                    scanner = DeviceService.getInstance(context).getScanner();
                    scanner.startScan(timeout, new OnScanListener.Stub() {
                        final Map<String, Object> params = new HashMap<String, Object>();

                        @Override
                        public void onSuccess(String code) {
                            LoggerUtil.e("扫码成功: " + code);
                            JSONObject data = new JSONObject();
                            try {
                                data.put("barcode", code);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            params.put(Constant.RESULT_MESSAGE, "扫码成功");
                            params.put(Constant.RESULT_CODE, Constant.RESULT_SUCCESS);
                            params.put(Constant.RESULT_DATA, data.toString());
                            callback.callback(params);
                        }


                        /**
                         *	扫码出错
                         *    @param i - 错误码
                         *	<ul>
                         *	<li>ERROR_INIT_FAIL(1) - 初始化解码库失败</li>
                         *	<li>ERROR_ALREADY_INIT(2) - 已经初始化</li>
                         *	<li>ERROR_INIT_ENGINE(3) - 初始化扫码模组失败</li>
                         *	<li>ERROR_AUTH_LICENSE(4) - License认证失败</li>
                         *	<li>ERROR_NOT_FIND_DECODE_LIB(5) - 找不到底层解码库</li>
                         *	<li>ERROR_OPEN_CAMERA(6) - 打开摄像头失败</li>
                         *	<li>ERROR_NOT_SUPPORT (0x10) - 设备不支持</li>
                         * </ul>
                         */
                        @Override
                        public void onError(int i) {
                            params.put(Constant.RESULT_MESSAGE, "扫码错误: " + i);
                            params.put(Constant.RESULT_CODE, Constant.RESULT_ERROR);
                            callback.callback(params);
                        }

                        @Override
                        public void onTimeout() {
                            params.put(Constant.RESULT_MESSAGE, "扫码超时");
                            params.put(Constant.RESULT_CODE, Constant.RESULT_ERROR);
                            callback.callback(params);
                        }

                        @Override
                        public void onCancel() {
                            params.put(Constant.RESULT_MESSAGE, "扫码取消");
                            params.put(Constant.RESULT_CODE, Constant.RESULT_ERROR);
                            callback.callback(params);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    params.put(Constant.RESULT_MESSAGE, e.getMessage());
                    params.put(Constant.RESULT_CODE, Constant.RESULT_ERROR);
                    callback.callback(params);
                }
            }
        });
    }

    /**
     * 读取身份证
     *
     * @param context  上下文环境
     * @param timeout  超时时间，单位:秒
     * @param callback 结果回调函数
     */
    public static void readIdCard(final Context context, final Integer timeout, final List<String> results, final ICallback callback) {
        SDKExecutors.getThreadPoolInstance().submit(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<String, Object>();
                try {
                    idCardReader = DeviceService.getInstance(context).getIDCard();
                    if (idCardReader != null) {
                        // 设置读卡超时时间 请最少设置3*1000毫秒
                        idCardReader.setTimeOut(timeout * 1000);
                        Bundle bundle = idCardReader.readBaseMsg();
                        if (bundle != null && bundle.getInt("errorCode") == 0x90) {
                            JSONObject data = FormatDataUtils.idCardData(bundle, results);
                            params.put(Constant.RESULT_MESSAGE, "读身份证成功");
                            params.put(Constant.RESULT_CODE, Constant.RESULT_SUCCESS);
                            params.put(Constant.RESULT_DATA, data.toString());
                        } else {
                            LoggerUtil.e(bundle.toString());
                            params.put(Constant.RESULT_MESSAGE, "读身份证失败");
                            params.put(Constant.RESULT_CODE, Constant.RESULT_ERROR);
                        }
                    } else {
                        params.put(Constant.RESULT_MESSAGE, "身份证模块不存在或初始化失败");
                        params.put(Constant.RESULT_CODE, Constant.RESULT_ERROR);
                    }
                    callback.callback(params);
                } catch (Exception e) {
                    e.printStackTrace();
                    params.put(Constant.RESULT_MESSAGE, e.getMessage());
                    params.put(Constant.RESULT_CODE, Constant.RESULT_ERROR);
                    callback.callback(params);
                }
            }
        });
    }


    /**
     * 读取社保卡信息
     *
     * @param context  上下文环境
     * @param timeout  超时时间，单位:秒
     * @param callback 结果回调函数
     */
    public static void readSocialSecurityCard(final Context context, final Integer timeout,final List<String> results, final ICallback callback) {
        SDKExecutors.getThreadPoolInstance().submit(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<String, Object>();
                try {
                    healthCardReader = DeviceService.getInstance(context).getHealthCardReader();
                    Bundle bundle = healthCardReader.readCard(timeout * 1000); // 启动读社保卡 超时时间为毫秒
                    if (bundle != null && bundle.getInt("errorCode") == 0x90) {
                        JSONObject data = FormatDataUtils.healthCardData(bundle, results);
                        params.put(Constant.RESULT_MESSAGE, "社保卡读取成功");
                        params.put(Constant.RESULT_CODE, Constant.RESULT_SUCCESS);
                        params.put(Constant.RESULT_DATA, data.toString());
                    } else {
                        LoggerUtil.e(bundle.toString());
                        params.put(Constant.RESULT_MESSAGE, "社保卡读取失败");
                        params.put(Constant.RESULT_CODE, Constant.RESULT_ERROR);
                    }
                    callback.callback(params);
                } catch (Exception e) {
                    e.printStackTrace();
                    params.put(Constant.RESULT_MESSAGE, e.getMessage());
                    params.put(Constant.RESULT_CODE, Constant.RESULT_ERROR);
                    callback.callback(params);
                }
            }
        });
    }


    /**
     * 关闭扫码串口
     * 停止扫码，取消扫码过程
     */
    public static void closeScan() {
        try {
            if (scanner != null)
                scanner.stopScan();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭读取身份证串口
     */
    public static void closeReadIdCard() {
        try {
            if (idCardReader != null)
                idCardReader.closeDevice();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭读取社保卡串口
     */
    public static void closeReadSocialSecurityCard() {
        try {
            if (healthCardReader != null)
                healthCardReader.closeDevice();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启读取医院就诊卡信息
     *
     * @param context
     * @param callback
     */
    public static void openReadHospitalCard(final Context context, final ICallback callback) {
        SDKExecutors.getThreadPoolInstance().submit(new Runnable() {

            @Override
            public void run() {
                Map<String, Object> params = new HashMap<String, Object>();
                try {
                    hospitalCardReader = DeviceService.getInstance(context).getHospitalCardReader();
                    Bundle bundle = hospitalCardReader.readCard(10 * 1000);
                    if (bundle != null && bundle.getInt("errorCode") == 0x90) {
                        StringBuilder builder = new StringBuilder();
                        builder.append("用户信息：\r\n")
                                .append(bundle.getString("cardName"))
                                .append("\r\n")
                                .append(bundle.getString("cardNo"))
                                .append("\r\n")
                                .append(bundle.getString("cardNoSN"))
                                .append("\r\n")
                                .append(bundle.getString("backup01"));
                        String data = builder.toString();
                        params.put(Constant.RESULT_MESSAGE, "就诊卡读取成功");
                        params.put(Constant.RESULT_CODE, Constant.RESULT_SUCCESS);
                        params.put(Constant.RESULT_DATA, data);
                    } else {
                        params.put(Constant.RESULT_MESSAGE, "读就诊卡失败");
                        params.put(Constant.RESULT_CODE, Constant.RESULT_ERROR);
                    }
                    callback.callback(params);
                } catch (Exception e) {
                    e.printStackTrace();
                    params.put(Constant.RESULT_MESSAGE, e.getMessage());
                    params.put(Constant.RESULT_CODE, Constant.RESULT_ERROR);
                    callback.callback(params);
                }
            }
        });
    }

    /**
     * 关闭读取医院就诊卡串口
     */
    public static void closeReadHospitalCard() {
        try {
            if (hospitalCardReader != null)
                hospitalCardReader.closeDevice();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启蜂鸣器总开关
     * 说明：蜂鸣器默认开启；设备重启后，开关会失效，需要重新设置
     *
     * @param context 上下文环境
     */
    public static void beepSwitchOpen(final Context context) {
        SDKExecutors.getThreadPoolInstance().submit(new Runnable() {

            @Override
            public void run() {
                try {
                    beeper = DeviceService.getInstance(context).getBeeper();
                    // b ：true 开启读卡等蜂鸣 ；false 关闭读卡等蜂鸣
                    beeper.beepSwitch(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 关闭蜂鸣器总开关
     * 说明：蜂鸣器默认开启；设备重启后，开关会失效，需要重新设置
     *
     * @param context 上下文环境
     */
    public static void beepSwitchClose(final Context context) {
        SDKExecutors.getThreadPoolInstance().submit(new Runnable() {

            @Override
            public void run() {
                try {
                    beeper = DeviceService.getInstance(context).getBeeper();
                    // b ：true 开启读卡等蜂鸣 ；false 关闭读卡等蜂鸣
                    beeper.beepSwitch(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 寻身份证
     *
     * @param context  上下文环境
     * @param callback 回调函数
     */
    public static void findIdCard(final Context context, final ICallback callback) {
        SDKExecutors.getThreadPoolInstance().submit(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<String, Object>();
                try {
                    idCardReader = DeviceService.getInstance(context).getIDCard();
                    //设置寻卡超时
                    idCardReader.setFindCardTimeOut(500);
                    int result = idCardReader.findCard();
                    if (result == 0x9f) {
                        params.put(Constant.RESULT_MESSAGE, "寻身份证成功");
                        params.put(Constant.RESULT_CODE, Constant.RESULT_SUCCESS);
                    } else {
                        params.put(Constant.RESULT_MESSAGE, "寻身份证失败");
                        params.put(Constant.RESULT_CODE, Constant.RESULT_ERROR);
                    }
                    callback.callback(params);
                } catch (Exception e) {
                    e.printStackTrace();
                    params.put(Constant.RESULT_MESSAGE, e.getMessage());
                    params.put(Constant.RESULT_CODE, Constant.RESULT_ERROR);
                    callback.callback(params);
                }
            }
        });
    }

    /**
     * 寻社保卡
     *
     * @param context  上下文环境
     * @param callback 回调函数
     */
    public static void findHealthCard(final Context context, final ICallback callback) {
        SDKExecutors.getThreadPoolInstance().submit(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<String, Object>();
                try {
                    healthCardReader = DeviceService.getInstance(context).getHealthCardReader();
                    int result = healthCardReader.findCard(500);
                    if (result == 0x9f) {
                        params.put(Constant.RESULT_MESSAGE, "寻社保卡成功");
                        params.put(Constant.RESULT_CODE, Constant.RESULT_SUCCESS);
                    } else {
                        params.put(Constant.RESULT_MESSAGE, "寻社保卡失败");
                        params.put(Constant.RESULT_CODE, Constant.RESULT_ERROR);
                    }
                    callback.callback(params);
                } catch (Exception e) {
                    e.printStackTrace();
                    params.put(Constant.RESULT_MESSAGE, e.getMessage());
                    params.put(Constant.RESULT_CODE, Constant.RESULT_ERROR);
                    callback.callback(params);
                }
            }
        });
    }

    /**
     * 启动多合一读卡
     *
     * @param context      上下文环境
     * @param timeout      超时时间，单位:秒
     * @param isIdCard     支持身份证
     * @param isScan       支持扫码
     * @param isHealthCard 支持社保卡
     * @param idCardResults 身份证返回结果KEY
     * @param healthCardResults 社保卡返回结果KEY
     * @param callback     回调函数
     */
    public static void readDevice(final Context context, final Integer timeout, final boolean isIdCard, final boolean isScan, final boolean isHealthCard, final List<String> idCardResults, final List<String> healthCardResults,  final ICallback callback) {
        SDKExecutors.getThreadPoolInstance().submit(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> params = new HashMap<String, Object>();
                try {
                    hybridReader = DeviceService.getInstance(context).getHybridReader();
                    if(hybridReader != null){
                        Bundle bundle = hybridReader.readDevice(timeout * 1000, isIdCard, isScan, isHealthCard);
                        LoggerUtil.e(bundle.get("errorCode").toString());
                        if (bundle.getInt("errorCode") == 0x90) {
                            JSONObject data = new JSONObject();
                            switch (bundle.getString("type")) {
                                case "SCAN":
                                    data.put("barcode", bundle.get("barcode"));
                                    break;
                                case "IDCARD":
                                    data = FormatDataUtils.idCardData(bundle, idCardResults);
                                    break;
                                case "HEALTHCARD":
                                    data = FormatDataUtils.healthCardData(bundle, healthCardResults);
                                    break;
                                default:
                                    data.put("error", "卡片类型不正确，读取失败");
                                    break;
                            }
                            data.put("type", bundle.get("type"));
                            params.put(Constant.RESULT_MESSAGE, "识别成功");
                            params.put(Constant.RESULT_CODE, Constant.RESULT_SUCCESS);
                            params.put(Constant.RESULT_DATA, data.toString());
                        } else {
                            params.put(Constant.RESULT_MESSAGE, "信息读取失败，请稍候尝试");
                            params.put(Constant.RESULT_CODE, Constant.RESULT_ERROR);
                        }
                    } else {
                        params.put(Constant.RESULT_MESSAGE, "读卡器模块初始化失败，请稍候尝试");
                        params.put(Constant.RESULT_CODE, Constant.RESULT_ERROR);
                    }
                    callback.callback(params);
                } catch (Exception e) {
                    e.printStackTrace();
                    params.put(Constant.RESULT_MESSAGE, e.getMessage());
                    params.put(Constant.RESULT_CODE, Constant.RESULT_ERROR);
                    callback.callback(params);
                }
            }
        });
    }

    /**
     * 关闭多合一读卡接口
     */
    public static void closeDevice(){
        try {
            if(hybridReader != null)
                hybridReader.closeDevice();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
