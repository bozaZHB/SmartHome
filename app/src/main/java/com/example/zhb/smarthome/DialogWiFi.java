package com.example.zhb.smarthome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;

public class DialogWiFi extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder1.setTitle("Smart Home Božanić");
        builder1.setMessage("Wifi isključen!");
        builder1.setCancelable(true);
        builder1.setIcon(R.mipmap.ic_launcher);
        builder1.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        builder1.setNeutralButton("Uključi WiFi",
                new DialogInterface.OnClickListener(){
                    @SuppressLint("MissingPermission")
                    public void onClick(DialogInterface dialog, int id) {
                        @SuppressLint("WifiManagerLeak") WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                        wifi.setWifiEnabled(true);
                        synchronized (this){
                            try {
                                wait(4000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                        Intent intent = new Intent(getApplicationContext(), Splash.class);
                        startActivity(intent);
                        finish();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}