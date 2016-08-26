package com.mingseal.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;

import com.mingseal.data.dao.WiFiDao;
import com.mingseal.utils.L;

public class NetworkStateService extends Service {
    private static final String tag = "service";
    private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    private WiFiDao wifiDao;// wifi的ssid Dao
    //声明接口对象
    public INotifyService listener;
    private MyBinder mBinder = new MyBinder();

    /**
     * 定义接口及方法
     */
    public interface INotifyService {
        void notifyServiceEvent(int msg);
    }

    /**
     * 注册回调接口的方法，供外部调用
     *
     * @param event
     */
    public void setOnINotifyServiceListener(INotifyService event) {
        listener = event;
    }

    public NetworkStateService() {
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            L.d("广播接收者-->onReceive()");
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                L.d("网络状态已经改变");
//                connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//                info = connectivityManager.getActiveNetworkInfo();
                WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wifi.getConnectionInfo();
                String ssid_info = info.getSSID();
                String ssid = null;
                int _id = -1;
                //判断是否有连入网络，进行相应的字符串截取
                if (ssid_info.contains("\"")) {
                    //联入了网络
                    ssid = ssid_info.substring(1, ssid_info.lastIndexOf("\""));
                    L.d("ssid:======" + ssid);
                    //查询数据库
                    _id = wifiDao.findNumbySSID(ssid);
                }
                String unknownSSID = "<" + "unknown ssid" + ">";
                if (_id <= 0 || ssid_info.equals(unknownSSID)) {
                    //网络连接断开
                    if (listener != null) {
                        listener.notifyServiceEvent(1);
                        L.d("广播接受者--->网络连接断开");
                    }
                } else {
                    //保持连接
                    if (listener != null) {
                        listener.notifyServiceEvent(0);
                        L.d("广播接收者--->网络保持连接");
                    }
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.d("服务创建了。。");
        wifiDao = new WiFiDao(this);
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.d("服务销毁了。。");
        if (wifiDao != null) {
            wifiDao = null;
        }
        unregisterReceiver(mReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    //binder对象携带service对象
    public class MyBinder extends Binder {
        //返回当前service实列
        public NetworkStateService getService() {
            return NetworkStateService.this;
        }
    }
}
