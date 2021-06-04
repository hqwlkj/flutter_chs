package com.parsec.flutter_chs.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import cn.hsa.ctp.device.sdk.managerService.aidl.Beeper;
import cn.hsa.ctp.device.sdk.managerService.aidl.HealthCardReader;
import cn.hsa.ctp.device.sdk.managerService.aidl.HospitalCardReader;
import cn.hsa.ctp.device.sdk.managerService.aidl.HybridReader;
import cn.hsa.ctp.device.sdk.managerService.aidl.IDCardReader;
import cn.hsa.ctp.device.sdk.managerService.aidl.ManagerServiceInterface;
import cn.hsa.ctp.device.sdk.managerService.aidl.Scanner;

import static android.content.Context.BIND_AUTO_CREATE;

public class DeviceService {

    private static ManagerServiceInterface deviceService;
    private static DeviceService device;
    private final Object waitObj = new Object();

    public synchronized static DeviceService getInstance(Context context) {
        if (device == null) {
            device = new DeviceService(context);
        }
        return device;
    }

    private final Context context;

    private DeviceService(Context context) {
        this.context = context;
    }

    public void connect() {
        Log.d("DeviceService", "---------------->connect time:" + System.currentTimeMillis());
        if (deviceService == null) {
            Log.d("DeviceService", "---------------->run time:" + System.currentTimeMillis());
            Intent intent = new Intent();
            intent.setPackage("cn.hsa.ctp.device.sdk.managerService.aidl"); // 使用服务apk的时候使用
            intent.setAction("cn.hsa.ctp.device.sdk.managerService.start.aidl");
            context.bindService(intent, serviceConnection, BIND_AUTO_CREATE);
            try {
                synchronized (waitObj) {
                    waitObj.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect() {
        if (deviceService != null) {
            try {
                context.unbindService(serviceConnection);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                deviceService = null;
            }
        }
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            deviceService = null;
//            connect();
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            deviceService = ManagerServiceInterface.Stub.asInterface(service);
            Log.d("DeviceService", "---------------->connect success time:" + System.currentTimeMillis());
            synchronized (waitObj) {
                waitObj.notifyAll();
            }
        }
    };

    /**
     * 获取扫码器设备操作对象
     * getScanner: 参数 cameraID -扫码头 ID，0:BACK–后置扫码(默认), 1:FRONT- 前置扫码
     * @return 扫码器设备操作对象，详见 IScanner.aidl 定义
     */
    public Scanner getScanner() {
        try {
            connect();
            return Scanner.Stub.asInterface(deviceService.getScanner(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取身份证读卡器操作对象
     * @return 身份证读卡器操作模块的实例
     */
    public IDCardReader getIDCard() {
        try {
            connect();
            return IDCardReader.Stub.asInterface(deviceService.getIDCardReader());
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取社保卡读卡器操作对象
     * @return 社保卡读卡器操作模块的实例
     */
    public HealthCardReader getHealthCardReader() {
        try {
            connect();
            return HealthCardReader.Stub.asInterface(deviceService.getHealthCardReader());
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取晋安就诊卡读卡器操作对象
     * @return 晋安就诊卡读卡器操作模块的实例
     */
    public HospitalCardReader getHospitalCardReader() {
        try {
            connect();
            return HospitalCardReader.Stub.asInterface(deviceService.getHospitalCardReader());
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取蜂鸣器总开关操作对象
     * @return 蜂鸣器总开关操作模块的实例
     */
    public Beeper getBeeper() {
        try {
            connect();
            return Beeper.Stub.asInterface(deviceService.getBeeper());
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取多合一读卡对象
     * @return 多合一读卡对象
     */
    public HybridReader getHybridReader(){
        try {
            connect();
            return HybridReader.Stub.asInterface(deviceService.getHybridReader());
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }
}
