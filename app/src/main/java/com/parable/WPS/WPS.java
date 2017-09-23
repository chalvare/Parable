package com.parable.WPS;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.SystemClock;

import java.util.List;

/**
 * Created by Altair on 10/11/15.
 */
public class WPS {
    private WifiManager manager;
    public WPS(Context context) {
        manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
    }

    public List<ScanResult> scanAndShow() throws WPSException {
        //manager.setWifiEnabled(true);
        if(manager.isWifiEnabled()){
            manager.startScan();
            SystemClock.sleep(500);
            return manager.getScanResults();
        }else{
            throw new WPSException("El dispositivo WIFI está desactivado.");
        }
    }
}
