package com.example.zhb.smarthome;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    MqttAndroidClient client;
    Vrednosti vr;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView txtObavestenje, txtLakuNoc, txtFontanaPumpa, txtOsvetljenjeFontana, txtTrenutniDatum;
    private TextSwitcher textSwitcher,textSwitcher2,textSwitcher3;
    private int stringIndex = 0;
    private int stringIndex2 = 0;
    private int stringIndex3 = 0;
    private ImageSwitcher imageSwitcher;
    int count = 0;
    public Context context;
    private String[] row = {"Temperatura Napolje", "Temperatura Hodnik", "Temperatura Dnevni boravak"};
    private String[] row2 = {"-12°C", "18°C", "25°C"};
    private String[] row3 = {"vlažnost: 40%", "vlažnost: 50%", "vlažnost: 60%"};
    ImageButton imageButtonLakuNoc, imageButtonFontanaPrskalica, imageButtonOsvetljenjeFontane;
    Integer[] images={R.drawable.cetvrta, R.drawable.peta, R.drawable.sesta, R.drawable.osma};
    int i = 0;

    Boolean osvetljenjeFontaneSlanje=false;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vr = new Vrednosti();

        txtObavestenje = findViewById(R.id.txtObavestenje);
        txtLakuNoc = findViewById(R.id.txtLakuNoc);
        txtFontanaPumpa = findViewById(R.id.txtFontanaPrskalica);
        txtOsvetljenjeFontana = findViewById(R.id.txtOsvetljenjeFontane);
        txtTrenutniDatum = findViewById(R.id.txtTrenutniDatum);
        //ucitavanje temperature za ikonicu
        iconTemperature();
        //trenutni datum
        txtTrenutniDatum.setText(getDateTimeMounth() + " " + getDateTimeYear());
        //kraj trenutni datum

        //da li je IMEI admin
        if(!vr.IMEI) txtObavestenje.setVisibility(View.GONE);

        //broj konektovanih mikrokontrolera
        int brojKonektovanih = 0;
        if (vr.nodeFontana) brojKonektovanih++;
        if (vr.nodeTerasaNeonka) brojKonektovanih++;
        if (vr.nodeVrataUlazna) brojKonektovanih++;
        txtObavestenje.setText(brojKonektovanih+"/3");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //laku noc
        imageButtonLakuNoc = findViewById(R.id.imageButtonLakuNoc);

        //fontana pumpa
        imageButtonFontanaPrskalica = findViewById(R.id.imageButtonFontanaPrskalica);
        //osvetljenje fontane
        imageButtonOsvetljenjeFontane =findViewById(R.id.imageButtonOsvetljenjeFontane);



        imageButtonOsvetljenjeFontane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand("bozaSub/kuca/node1/fontanaOsvetljenje",osvetljenjeFontaneSlanje==true?"3":"4" );
                osvetljenjeFontaneSlanje=!osvetljenjeFontaneSlanje;
            }
        });
        imageButtonLakuNoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vr.prskalicaAll==true)sendCommand("bozaSub/kuca/node1/prskalicaAll","0");
                sendCommand("bozaSub/kuca/node1/fontanaOsvetljenje","3");
                if(vr.dvoristeRadio==true)sendCommand("bozaSub/kuca/node1/radio","0");
                if(vr.neodredjenoFontana==true)sendCommand("bozaSub/kuca/node1/neodredjeno","0");
                if(vr.svetlo1==true)sendCommand("bozaSub/kuca/node2/neonka","0");
                if(vr.fontanaPrskalica==true)sendCommand("bozaSub/kuca/node1/fontanaPumpa","0");
            }
        });

        imageButtonFontanaPrskalica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCommand("bozaSub/kuca/node1/fontanaPumpa",vr.fontanaPrskalica==true?"0":"1" );
            }
        });

        //slajder
        textSwitcher = findViewById(R.id.textSwither);
        textSwitcher2 = findViewById(R.id.textSwither2);
        textSwitcher3 = findViewById(R.id.textSwither3);
        imageSwitcher = findViewById(R.id.imageSwither);

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return imageView;
            }
        });
        final Thread t = new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (stringIndex == row.length - 1) {
                                    stringIndex = 0;
                                    textSwitcher.setText(row[stringIndex]);
                                } else {
                                    textSwitcher.setText(row[++stringIndex]);
                                }

                                if (i < images.length) {
                                    //imageSwitcher.setImageResource(images[i]);
                                    imageSwitcher.setBackgroundResource(images[i]);
                                    i++;
                                }
                                if (i == images.length) i = 0;
                                new CountDownTimer(100, 100) {

                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        // do something after 1s
                                    }

                                    @Override
                                    public void onFinish() {
                                        if (stringIndex2 == row2.length - 1) {
                                            stringIndex2 = 0;
                                            textSwitcher2.setText(row2[stringIndex2]);
                                        } else {
                                            textSwitcher2.setText(row2[++stringIndex2]);
                                        }
                                    }

                                }.start();
                                new CountDownTimer(200, 100) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        // do something after 1s
                                    }

                                    @Override
                                    public void onFinish() {
                                        if (stringIndex3 == row3.length - 1) {
                                            stringIndex3 = 0;
                                            textSwitcher3.setText(row3[stringIndex3]);
                                        } else {
                                            textSwitcher3.setText(row3[++stringIndex3]);
                                        }
                                    }

                                }.start();
                            }
                        });
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Animation in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.in);
        Animation out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.out);

        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);

        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                textView = new TextView(MainActivity.this);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(20);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                return textView;
            }
        });
        textSwitcher.setText(row[stringIndex]);

        textSwitcher2.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                textView = new TextView(MainActivity.this);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(48);
                textView.setTextAppearance(getApplicationContext(), R.style.boldText);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                return textView;
            }
        });
        textSwitcher2.setText(row2[stringIndex2]);

        textSwitcher3.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                textView = new TextView(MainActivity.this);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(20);
                textView.setGravity(Gravity.LEFT);
                return textView;
            }
        });
        textSwitcher3.setText(row3[stringIndex3]);

        t.start();
        //gotovo slajder

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), vr.mqttBroker , clientId);


        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });

    }
    private boolean proveraLakuNoc(){
        if(vr.prskalicaAll==false && vr.dvoristeRadio==false && vr.neodredjenoFontana==false && vr.svetlo1==false && vr.fontanaPrskalica==false) return true;
        else return false;
    }

    public void onStart(){
        super.onStart();
        context = getApplicationContext(); //za toast u tredu
        //on start za pozadinu dugmica
        if (proveraLakuNoc()) imageButtonLakuNoc.setBackgroundResource(R.drawable.btn_circle_on);
        else imageButtonLakuNoc.setBackgroundResource(R.drawable.btn_circle_off);

        if (vr.fontanaPrskalica) imageButtonFontanaPrskalica.setBackgroundResource(R.drawable.btn_circle_on);
        else imageButtonFontanaPrskalica.setBackgroundResource(R.drawable.btn_circle_off);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    try {
                        ucitavanjeTeksta();
                        //sendCommand("bozaSub/kuca/node1/stanje","s");
                    }catch (Exception e){

                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(context, "nece da se upari", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                //Toast.makeText(MainActivity.this,"Prekid konekcije!!!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String poruka = new String(message.getPayload());
                mqtt mq = new mqtt();
                mq.dolazecePoruke(topic,poruka);
                if (topic.equals("bozaSub/kuca/node2/dvoriste/temperatura")) {
                    row2[0] = vr.temperaturaNapolje;
                }
                else if (topic.equals("bozaSub/kuca/node2/dvoriste/vlaznost")) {
                    row3[0] = "vlažnost: "+vr.vlaznostNapolje;
                }

                if (vr.fontanaPrskalica) imageButtonFontanaPrskalica.setBackgroundResource(R.drawable.btn_circle_on);
                else imageButtonFontanaPrskalica.setBackgroundResource(R.drawable.btn_circle_off);

                if(proveraLakuNoc()==true)imageButtonLakuNoc.setBackgroundResource(R.drawable.btn_circle_on);
                else imageButtonLakuNoc.setBackgroundResource(R.drawable.btn_circle_off);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            IMqttToken disconToken = client.disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void ucitavanjeTeksta() {
        try{
            client.subscribe("bozaSub/kuca/#",0);
        }catch (MqttException e){
            e.printStackTrace();
        }
    }
    private void sendCommand(String topic, String message)
    {
        try{
            if (client.isConnected()) client.publish(topic,message.getBytes(),0,false);
            else Toast.makeText(context, "Ne postoji konekcija sa serverom", Toast.LENGTH_SHORT).show();
        }catch (MqttException e){
            finish();
        }
    }
    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.nav_drawer, menu);

        /*final MenuItem item = menu.findItem(R.id.action_sign);
        TextView cartCount = (TextView) item.getActionView().findViewById(R.id.txtMenuSign);
        cartCount.setText("10");*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_settings){
            Intent searchIntent = new Intent(MainActivity.this, Settings.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.home){
            Intent searchIntent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.dvoriste){
            Intent searchIntent = new Intent(MainActivity.this, Dvoriste.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.kapija){
            Intent searchIntent = new Intent(MainActivity.this, Kapija.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.fontana){
            Intent searchIntent = new Intent(MainActivity.this, Fontana.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.grejanje){
            Intent searchIntent = new Intent(MainActivity.this, Grejanje.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.klimaH){
            Intent searchIntent = new Intent(MainActivity.this, KlimaH.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.klimaK){
            Intent searchIntent = new Intent(MainActivity.this, KlimaK.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.kucica){
            Intent searchIntent = new Intent(MainActivity.this, Kucica.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.ruter){
            Intent searchIntent = new Intent(MainActivity.this, DeviceList.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String getDateTimeYear() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy"); //yyyy/MM/dd HH:mm:ss
        Date date = new Date();
        return dateFormat.format(date);
    }
    private String getDateTimeMounth() {
        DateFormat dateFormat = new SimpleDateFormat("MM"); //yyyy/MM/dd HH:mm:ss
        Date date = new Date();
        switch (dateFormat.format(date)){
            case "01":
                return "JANUAR";
            case "02":
                return "FEBRUAR";
            case "03":
                return "MART";
            case "04":
                return "APRIL";
            case "05":
                return "MAJ";
            case "06":
                return "JUN";
            case "07":
                return "JUL";
            case "08":
                return "AVGUST";
            case "09":
                return "SEPTEMBAR";
            case "10":
                return "OKTOBAR";
            case "11":
                return "NOVEMBAR";
            case "12":
                return "DECEMBAR";
            default:
                return "GREŠKA";
        }
    }



    private void iconTemperature() {
        final Thread t2 = new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new CountDownTimer(100, 100) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                    }
                                    @Override
                                    public void onFinish() {
                                        int temperatura =  Integer.parseInt(vr.temperaturaNapolje.substring(0, vr.temperaturaNapolje.indexOf('°')));
                                        TextView tw = findViewById(R.id.txtMenuSign);
                                        if (temperatura>25) tw.setCompoundDrawablesWithIntrinsicBounds( R.drawable.sun, 0, 0, 0);
                                        else if (temperatura>15) tw.setCompoundDrawablesWithIntrinsicBounds( R.drawable.cloudy, 0, 0, 0);
                                        else if (temperatura>10) tw.setCompoundDrawablesWithIntrinsicBounds( R.drawable.cloud, 0, 0, 0);
                                        else if (temperatura>5) tw.setCompoundDrawablesWithIntrinsicBounds( R.drawable.wind, 0, 0, 0);
                                        else if (temperatura>0) tw.setCompoundDrawablesWithIntrinsicBounds( R.drawable.rain, 0, 0, 0);
                                        else tw.setCompoundDrawablesWithIntrinsicBounds( R.drawable.snow, 0, 0, 0);
                                        tw.setText(vr.temperaturaNapolje.substring(0, vr.temperaturaNapolje.indexOf('C')));
                                    }
                                }.start();
                        }});
                        Thread.sleep(10000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }}}
        };
        t2.start();
    }

    private void refreshList() {

        sendCommand("bozaSub/kuca/node1/stanje","s");
        zhbToast("UČITAVANJE STANJA...", false);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void zhbToast(String sadrzaj, boolean kratkoTrajanje) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_custom, null);
        ((TextView) layout.findViewById(android.R.id.message)).setText(sadrzaj);
        Toast toast = new Toast(this);
        toast.setGravity(Gravity.BOTTOM, 0, 70);
        toast.setDuration(kratkoTrajanje? 1 : 0);
        toast.setView(layout);
        toast.show();
    }
}
