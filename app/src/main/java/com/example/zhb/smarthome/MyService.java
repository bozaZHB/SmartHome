package com.example.zhb.smarthome; //MyService

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MyService extends IntentService {
    IMqttToken token;
    MqttAndroidClient client;
    public static final int NOTIFICATION_ID = 5435;
    Vrednosti vr;
    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        vr = new Vrednosti();

        String clientId = MqttClient.generateClientId();

        client = new MqttAndroidClient(this, vr.mqttBroker, clientId);//"tcp://192.168.0.17:1883"

        if (client.isConnected()) client.close(); //da ne bi izbacivalo gresku prvi put kada se ubije stek aplikacije
        try {
            token = client.connect();
            int timeSum=0;
            int timeLimit = 1999;
            while (!client.isConnected() && timeSum<timeLimit){//da ne vrti non stop i istituje konekciju
                synchronized (this){
                    try {
                        wait(25);
                        timeSum+=25;
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    try{
                        client.subscribe("bozaSub/kuca/#",0);
                    }catch (MqttException e){
                        e.printStackTrace();
                    }

                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) { }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) { }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception { }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) { }
        });

        String topic = "bozaSub/kuca/node3/vrata";
        String message = "1";
        try{
            client.publish(topic,message.getBytes(),0,false);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        client.unregisterResources();
        client.close();
    }

}