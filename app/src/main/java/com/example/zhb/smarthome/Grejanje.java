package com.example.zhb.smarthome;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Calendar;

public class Grejanje extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txvTemperatureOutdoor, txvTemperatureDnevniBoravak, txvKotaoStatus;
    private ImageButton imageButtonElMotor, imageButtonKotao, imageButtonPlin;
    private ImageView imageButtonSaveWorkingDays, imageButtonSaveWeekend, imbTimeWorkingDays1, imbTimeWorkingDays2, imbTimeWorkingDays3, imbTimeWorkingDays4, imbTimeWorkingDays5;
    private ImageView imageButtonWeekend, imbTimeWeekend1, imbTimeWeekend2, imbTimeWeekend3, imbTimeWeekend4, imbTimeWeekend5;

    private LinearLayout llWorkingDays2, llWorkingDays3, llWorkingDays4, llWorkingDays5, llWorkingDays6;
    private LinearLayout llWeekend2, llWeekend3, llWeekend4, llWeekend5, llWeekend6;

    private EditText edtTemperatureWorkingDay1, edtTemperatureWorkingDay2, edtTemperatureWorkingDay3, edtTemperatureWorkingDay4, edtTemperatureWorkingDay5, edtTemperatureWorkingDay6;
    private EditText edtTemperatureWeekend1, edtTemperatureWeekend2, edtTemperatureWeekend3, edtTemperatureWeekend4, edtTemperatureWeekend5, edtTemperatureWeekend6;

    private EditText edtTimeWorkingDayFrom1, edtTimeWorkingDayFrom2, edtTimeWorkingDayFrom3, edtTimeWorkingDayFrom4, edtTimeWorkingDayFrom5, edtTimeWorkingDayFrom6;
    private EditText edtTimeWeekendFrom1, edtTimeWeekendFrom2, edtTimeWeekendFrom3, edtTimeWeekendFrom4, edtTimeWeekendFrom5, edtTimeWeekendFrom6;

    private EditText edtTimeWorkingDayTo1, edtTimeWorkingDayTo2, edtTimeWorkingDayTo3, edtTimeWorkingDayTo4, edtTimeWorkingDayTo5, edtTimeWorkingDayTo6;
    private EditText edtTimeWeekendTo1, edtTimeWeekendTo2, edtTimeWeekendTo3, edtTimeWeekendTo4, edtTimeWeekendTo5, edtTimeWeekendTo6;

    Vrednosti vrednosti = new Vrednosti();
    MqttAndroidClient client;

    private int timeWorkingDay1, timeWorkingDay2, timeWorkingDay3, timeWorkingDay4, timeWorkingDay5, timeWorkingDay6;
    private int timeWeekend1, timeWeekend2, timeWeekend3, timeWeekend4, timeWeekend5, timeWeekend6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_grejanje);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initialization();

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), vrednosti.mqttBroker, clientId);//"tcp://192.168.0.17:1883"

        loadInitData();
    }

    private void loadInitData() {
        txvKotaoStatus.setText(vrednosti.temperaturaRelej ? "RADI" : "NE RADI");
        setTypeOfHeating();
        txvTemperatureDnevniBoravak.setText(vrednosti.temperaturaBoravak);

        if (vrednosti.brojVremenaRadniDan == 1) {
            llWorkingDays2.setVisibility(View.GONE);
            llWorkingDays3.setVisibility(View.GONE);
            llWorkingDays4.setVisibility(View.GONE);
            llWorkingDays5.setVisibility(View.GONE);
            llWorkingDays6.setVisibility(View.GONE);
        } else if (vrednosti.brojVremenaRadniDan == 2) {
            llWorkingDays2.setVisibility(View.VISIBLE);
            llWorkingDays3.setVisibility(View.GONE);
            llWorkingDays4.setVisibility(View.GONE);
            llWorkingDays5.setVisibility(View.GONE);
            llWorkingDays6.setVisibility(View.GONE);
        } else if (vrednosti.brojVremenaRadniDan == 3) {
            llWorkingDays2.setVisibility(View.VISIBLE);
            llWorkingDays3.setVisibility(View.VISIBLE);
            llWorkingDays4.setVisibility(View.GONE);
            llWorkingDays5.setVisibility(View.GONE);
            llWorkingDays6.setVisibility(View.GONE);
        } else if (vrednosti.brojVremenaRadniDan == 4) {
            llWorkingDays2.setVisibility(View.VISIBLE);
            llWorkingDays3.setVisibility(View.VISIBLE);
            llWorkingDays4.setVisibility(View.VISIBLE);
            llWorkingDays5.setVisibility(View.GONE);
            llWorkingDays6.setVisibility(View.GONE);
        } else if (vrednosti.brojVremenaRadniDan == 5) {
            llWorkingDays2.setVisibility(View.VISIBLE);
            llWorkingDays3.setVisibility(View.VISIBLE);
            llWorkingDays4.setVisibility(View.VISIBLE);
            llWorkingDays5.setVisibility(View.VISIBLE);
            llWorkingDays6.setVisibility(View.GONE);
        } else if (vrednosti.brojVremenaRadniDan == 6) {
            llWorkingDays2.setVisibility(View.VISIBLE);
            llWorkingDays3.setVisibility(View.VISIBLE);
            llWorkingDays4.setVisibility(View.VISIBLE);
            llWorkingDays5.setVisibility(View.VISIBLE);
            llWorkingDays6.setVisibility(View.VISIBLE);
        }

        timeWorkingDay1 = Integer.parseInt(vrednosti.vremeGrejanjaRadniDan1);
        timeWorkingDay2 = Integer.parseInt(vrednosti.vremeGrejanjaRadniDan2);
        timeWorkingDay3 = Integer.parseInt(vrednosti.vremeGrejanjaRadniDan3);
        timeWorkingDay4 = Integer.parseInt(vrednosti.vremeGrejanjaRadniDan4);
        timeWorkingDay5 = Integer.parseInt(vrednosti.vremeGrejanjaRadniDan5);
        timeWorkingDay6 = Integer.parseInt(vrednosti.vremeGrejanjaRadniDan6);

        edtTimeWorkingDayTo1.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaRadniDan1));
        edtTimeWorkingDayFrom2.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaRadniDan1));
        edtTimeWorkingDayTo2.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaRadniDan2));
        edtTimeWorkingDayFrom3.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaRadniDan2));
        edtTimeWorkingDayTo3.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaRadniDan3));
        edtTimeWorkingDayFrom4.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaRadniDan3));
        edtTimeWorkingDayTo4.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaRadniDan4));
        edtTimeWorkingDayFrom5.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaRadniDan4));
        edtTimeWorkingDayTo5.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaRadniDan5));
        edtTimeWorkingDayFrom6.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaRadniDan5));
        edtTimeWorkingDayTo6.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaRadniDan6));

        edtTemperatureWorkingDay1.setText(vrednosti.temperaturaGrejanjaRadniDan1);
        edtTemperatureWorkingDay2.setText(vrednosti.temperaturaGrejanjaRadniDan2);
        edtTemperatureWorkingDay3.setText(vrednosti.temperaturaGrejanjaRadniDan3);
        edtTemperatureWorkingDay4.setText(vrednosti.temperaturaGrejanjaRadniDan4);
        edtTemperatureWorkingDay5.setText(vrednosti.temperaturaGrejanjaRadniDan5);
        edtTemperatureWorkingDay6.setText(vrednosti.temperaturaGrejanjaRadniDan6);

        // vikend

        if (vrednosti.brojVremenaVikend == 1) {
            llWeekend2.setVisibility(View.GONE);
            llWeekend3.setVisibility(View.GONE);
            llWeekend4.setVisibility(View.GONE);
            llWeekend5.setVisibility(View.GONE);
            llWeekend6.setVisibility(View.GONE);
        } else if (vrednosti.brojVremenaVikend == 2) {
            llWeekend2.setVisibility(View.VISIBLE);
            llWeekend3.setVisibility(View.GONE);
            llWeekend4.setVisibility(View.GONE);
            llWeekend5.setVisibility(View.GONE);
            llWeekend6.setVisibility(View.GONE);
        } else if (vrednosti.brojVremenaVikend == 3) {
            llWeekend2.setVisibility(View.VISIBLE);
            llWeekend3.setVisibility(View.VISIBLE);
            llWeekend4.setVisibility(View.GONE);
            llWeekend5.setVisibility(View.GONE);
            llWeekend6.setVisibility(View.GONE);
        } else if (vrednosti.brojVremenaVikend == 4) {
            llWeekend2.setVisibility(View.VISIBLE);
            llWeekend3.setVisibility(View.VISIBLE);
            llWeekend4.setVisibility(View.VISIBLE);
            llWeekend5.setVisibility(View.GONE);
            llWeekend6.setVisibility(View.GONE);
        } else if (vrednosti.brojVremenaVikend == 5) {
            llWeekend2.setVisibility(View.VISIBLE);
            llWeekend3.setVisibility(View.VISIBLE);
            llWeekend4.setVisibility(View.VISIBLE);
            llWeekend5.setVisibility(View.VISIBLE);
            llWeekend6.setVisibility(View.GONE);
        } else if (vrednosti.brojVremenaVikend == 6) {
            llWeekend2.setVisibility(View.VISIBLE);
            llWeekend3.setVisibility(View.VISIBLE);
            llWeekend4.setVisibility(View.VISIBLE);
            llWeekend5.setVisibility(View.VISIBLE);
            llWeekend6.setVisibility(View.VISIBLE);
        }

        timeWeekend1 = Integer.parseInt(vrednosti.vremeGrejanjaVikend1);
        timeWeekend2 = Integer.parseInt(vrednosti.vremeGrejanjaVikend2);
        timeWeekend3 = Integer.parseInt(vrednosti.vremeGrejanjaVikend3);
        timeWeekend4 = Integer.parseInt(vrednosti.vremeGrejanjaVikend4);
        timeWeekend5 = Integer.parseInt(vrednosti.vremeGrejanjaVikend5);
        timeWeekend6 = Integer.parseInt(vrednosti.vremeGrejanjaVikend6);

        edtTimeWeekendTo1.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaVikend1));
        edtTimeWeekendFrom2.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaVikend1));
        edtTimeWeekendTo2.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaVikend2));
        edtTimeWeekendFrom3.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaVikend2));
        edtTimeWeekendTo3.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaVikend3));
        edtTimeWeekendFrom4.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaVikend3));
        edtTimeWeekendTo4.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaVikend4));
        edtTimeWeekendFrom5.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaVikend4));
        edtTimeWeekendTo5.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaVikend5));
        edtTimeWeekendFrom6.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaVikend5));
        edtTimeWeekendTo6.setText(timeFormatFromMessage(vrednosti.vremeGrejanjaVikend6));

        edtTemperatureWeekend1.setText(vrednosti.temperaturaGrejanjaVikend1);
        edtTemperatureWeekend2.setText(vrednosti.temperaturaGrejanjaVikend2);
        edtTemperatureWeekend3.setText(vrednosti.temperaturaGrejanjaVikend3);
        edtTemperatureWeekend4.setText(vrednosti.temperaturaGrejanjaVikend4);
        edtTemperatureWeekend5.setText(vrednosti.temperaturaGrejanjaVikend5);
        edtTemperatureWeekend6.setText(vrednosti.temperaturaGrejanjaVikend6);
    }

    private void initialization() {
        txvTemperatureOutdoor = findViewById(R.id.txvTemperatureOutdoor);
        txvTemperatureDnevniBoravak = findViewById(R.id.txvTemperatureDnevniBoravak);
        txvKotaoStatus = findViewById(R.id.txvKotaoStatus);
        imageButtonElMotor = findViewById(R.id.imageButtonElMotor);
        imageButtonKotao = findViewById(R.id.imageButtonKotao);
        imageButtonPlin = findViewById(R.id.imageButtonPlin);
        imageButtonSaveWorkingDays = findViewById(R.id.imageButtonSaveWorkingDays);
        imageButtonSaveWeekend = findViewById(R.id.imageButtonSaveWeekend);

        imbTimeWorkingDays1 = findViewById(R.id.imbTimeWorkingDays1);
        imbTimeWorkingDays2 = findViewById(R.id.imbTimeWorkingDays2);
        imbTimeWorkingDays3 = findViewById(R.id.imbTimeWorkingDays3);
        imbTimeWorkingDays4 = findViewById(R.id.imbTimeWorkingDays4);
        imbTimeWorkingDays5 = findViewById(R.id.imbTimeWorkingDays5);

        imbTimeWeekend1 = findViewById(R.id.imbTimeWeekend1);
        imbTimeWeekend2 = findViewById(R.id.imbTimeWeekend2);
        imbTimeWeekend3 = findViewById(R.id.imbTimeWeekend3);
        imbTimeWeekend4 = findViewById(R.id.imbTimeWeekend4);
        imbTimeWeekend5 = findViewById(R.id.imbTimeWeekend5);

        llWorkingDays2 = findViewById(R.id.llWorkingDays2);
        llWorkingDays3 = findViewById(R.id.llWorkingDays3);
        llWorkingDays4 = findViewById(R.id.llWorkingDays4);
        llWorkingDays5 = findViewById(R.id.llWorkingDays5);
        llWorkingDays6 = findViewById(R.id.llWorkingDays6);

        llWeekend2 = findViewById(R.id.llWeekend2);
        llWeekend3 = findViewById(R.id.llWeekend3);
        llWeekend4 = findViewById(R.id.llWeekend4);
        llWeekend5 = findViewById(R.id.llWeekend5);
        llWeekend6 = findViewById(R.id.llWeekend6);

        edtTemperatureWorkingDay1 = findViewById(R.id.edtTemperatureWorkingDay1);
        edtTemperatureWorkingDay2 = findViewById(R.id.edtTemperatureWorkingDay2);
        edtTemperatureWorkingDay3 = findViewById(R.id.edtTemperatureWorkingDay3);
        edtTemperatureWorkingDay4 = findViewById(R.id.edtTemperatureWorkingDay4);
        edtTemperatureWorkingDay5 = findViewById(R.id.edtTemperatureWorkingDay5);
        edtTemperatureWorkingDay6 = findViewById(R.id.edtTemperatureWorkingDay6);

        edtTemperatureWeekend1 = findViewById(R.id.edtTemperatureWeekend1);
        edtTemperatureWeekend2 = findViewById(R.id.edtTemperatureWeekend2);
        edtTemperatureWeekend3 = findViewById(R.id.edtTemperatureWeekend3);
        edtTemperatureWeekend4 = findViewById(R.id.edtTemperatureWeekend4);
        edtTemperatureWeekend5 = findViewById(R.id.edtTemperatureWeekend5);
        edtTemperatureWeekend6 = findViewById(R.id.edtTemperatureWeekend6);

        edtTimeWorkingDayFrom1 = findViewById(R.id.edtTimeWorkingDayFrom1);
        edtTimeWorkingDayFrom2 = findViewById(R.id.edtTimeWorkingDayFrom2);
        edtTimeWorkingDayFrom3 = findViewById(R.id.edtTimeWorkingDayFrom3);
        edtTimeWorkingDayFrom4 = findViewById(R.id.edtTimeWorkingDayFrom4);
        edtTimeWorkingDayFrom5 = findViewById(R.id.edtTimeWorkingDayFrom5);
        edtTimeWorkingDayFrom6 = findViewById(R.id.edtTimeWorkingDayFrom6);

        edtTimeWeekendFrom1 = findViewById(R.id.edtTimeWeekendFrom1);
        edtTimeWeekendFrom2 = findViewById(R.id.edtTimeWeekendFrom2);
        edtTimeWeekendFrom3 = findViewById(R.id.edtTimeWeekendFrom3);
        edtTimeWeekendFrom4 = findViewById(R.id.edtTimeWeekendFrom4);
        edtTimeWeekendFrom5 = findViewById(R.id.edtTimeWeekendFrom5);
        edtTimeWeekendFrom6 = findViewById(R.id.edtTimeWeekendFrom6);

        edtTimeWorkingDayTo1 = findViewById(R.id.edtTimeWorkingDayTo1);
        edtTimeWorkingDayTo2 = findViewById(R.id.edtTimeWorkingDayTo2);
        edtTimeWorkingDayTo3 = findViewById(R.id.edtTimeWorkingDayTo3);
        edtTimeWorkingDayTo4 = findViewById(R.id.edtTimeWorkingDayTo4);
        edtTimeWorkingDayTo5 = findViewById(R.id.edtTimeWorkingDayTo5);
        edtTimeWorkingDayTo6 = findViewById(R.id.edtTimeWorkingDayTo6);

        edtTimeWeekendTo1 = findViewById(R.id.edtTimeWeekendTo1);
        edtTimeWeekendTo2 = findViewById(R.id.edtTimeWeekendTo2);
        edtTimeWeekendTo3 = findViewById(R.id.edtTimeWeekendTo3);
        edtTimeWeekendTo4 = findViewById(R.id.edtTimeWeekendTo4);
        edtTimeWeekendTo5 = findViewById(R.id.edtTimeWeekendTo5);
        edtTimeWeekendTo6 = findViewById(R.id.edtTimeWeekendTo6);

        llWorkingDays2.setVisibility(View.GONE);
        llWorkingDays3.setVisibility(View.GONE);
        llWorkingDays4.setVisibility(View.GONE);
        llWorkingDays5.setVisibility(View.GONE);
        llWorkingDays6.setVisibility(View.GONE);

        llWeekend2.setVisibility(View.GONE);
        llWeekend3.setVisibility(View.GONE);
        llWeekend4.setVisibility(View.GONE);
        llWeekend5.setVisibility(View.GONE);
        llWeekend6.setVisibility(View.GONE);

        edtTimeWorkingDayFrom1.setKeyListener(null);
        edtTimeWorkingDayFrom2.setKeyListener(null);
        edtTimeWorkingDayFrom3.setKeyListener(null);
        edtTimeWorkingDayFrom4.setKeyListener(null);
        edtTimeWorkingDayFrom5.setKeyListener(null);
        edtTimeWorkingDayFrom6.setKeyListener(null);

        edtTimeWeekendFrom1.setKeyListener(null);
        edtTimeWeekendFrom2.setKeyListener(null);
        edtTimeWeekendFrom3.setKeyListener(null);
        edtTimeWeekendFrom4.setKeyListener(null);
        edtTimeWeekendFrom5.setKeyListener(null);
        edtTimeWeekendFrom6.setKeyListener(null);

        edtTimeWorkingDayTo1.setKeyListener(null);
        edtTimeWorkingDayTo2.setKeyListener(null);
        edtTimeWorkingDayTo3.setKeyListener(null);
        edtTimeWorkingDayTo4.setKeyListener(null);
        edtTimeWorkingDayTo5.setKeyListener(null);
        edtTimeWorkingDayTo6.setKeyListener(null);

        edtTimeWeekendTo1.setKeyListener(null);
        edtTimeWeekendTo2.setKeyListener(null);
        edtTimeWeekendTo3.setKeyListener(null);
        edtTimeWeekendTo4.setKeyListener(null);
        edtTimeWeekendTo5.setKeyListener(null);
        edtTimeWeekendTo6.setKeyListener(null);

        setListenerChoseTimeWorkingDays();
        setListenerChoseTimeWeekend();
        setListenerForButtons();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mqttProccess();
        setStartingGui();
    }

    private void setListenerChoseTimeWorkingDays() {
        imbTimeWorkingDays1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Grejanje.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hours = fillingWithZero(selectedHour);
                        String minutes = fillingWithZero(selectedMinute);

                        if (hours.equals("00") && minutes.equals("00")) {
                            hours = "24";
                            llWorkingDays2.setVisibility(View.GONE);
                            llWorkingDays3.setVisibility(View.GONE);
                            llWorkingDays4.setVisibility(View.GONE);
                            llWorkingDays5.setVisibility(View.GONE);
                            llWorkingDays6.setVisibility(View.GONE);
                        } else {
                            llWorkingDays2.setVisibility(View.VISIBLE);
                            timeWorkingDay2 = 2400;
                            edtTimeWorkingDayTo2.setText("24:00");
                            llWorkingDays3.setVisibility(View.GONE);
                            llWorkingDays4.setVisibility(View.GONE);
                            llWorkingDays5.setVisibility(View.GONE);
                            llWorkingDays6.setVisibility(View.GONE);
                        }

                        timeWorkingDay1 = Integer.parseInt((hours + minutes));

                        edtTimeWorkingDayTo1.setText(hours + ":" + minutes);
                        edtTimeWorkingDayFrom2.setText(hours + ":" + minutes);
                    }
                }, getHours(), getMinutes(), true);//Yes 24 hour time
                mTimePicker.setTitle("Izaberite vreme");
                mTimePicker.show();
            }
        });

        imbTimeWorkingDays2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Grejanje.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hours = set24hIfIs00(fillingWithZero(selectedHour), selectedMinute);
                        String minutes = fillingWithZero(selectedMinute);

                        timeWorkingDay2 = Integer.parseInt((hours + minutes));

                        if (timeWorkingDay2 > timeWorkingDay1) {
                            edtTimeWorkingDayTo2.setText(hours + ":" + minutes);

                            if (timeWorkingDay2 == 2400) {
                                llWorkingDays3.setVisibility(View.GONE);
                                llWorkingDays4.setVisibility(View.GONE);
                                llWorkingDays5.setVisibility(View.GONE);
                                llWorkingDays6.setVisibility(View.GONE);
                            } else {
                                llWorkingDays3.setVisibility(View.VISIBLE);
                                edtTimeWorkingDayFrom3.setText(hours + ":" + minutes);
                                edtTimeWorkingDayTo3.setText("24:00");
                                timeWorkingDay3 = 2400;
                                llWorkingDays4.setVisibility(View.GONE);
                                llWorkingDays5.setVisibility(View.GONE);
                                llWorkingDays6.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(Grejanje.this, "Vreme mora biti veće od prethodnog izabranog vremena", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, getHours(), getMinutes(), true);//Yes 24 hour time
                mTimePicker.setTitle("Izaberite vreme");
                mTimePicker.show();
            }
        });

        imbTimeWorkingDays3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Grejanje.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hours = set24hIfIs00(fillingWithZero(selectedHour), selectedMinute);
                        String minutes = fillingWithZero(selectedMinute);

                        timeWorkingDay3 = Integer.parseInt((hours + minutes));

                        if (timeWorkingDay3 > timeWorkingDay2) {
                            edtTimeWorkingDayTo3.setText(hours + ":" + minutes);

                            if (timeWorkingDay3 == 2400) {
                                llWorkingDays4.setVisibility(View.GONE);
                                llWorkingDays5.setVisibility(View.GONE);
                                llWorkingDays6.setVisibility(View.GONE);
                            } else {
                                llWorkingDays4.setVisibility(View.VISIBLE);
                                edtTimeWorkingDayFrom4.setText(hours + ":" + minutes);
                                timeWorkingDay4 = 2400;
                                edtTimeWorkingDayTo4.setText("24:00");
                                llWorkingDays5.setVisibility(View.GONE);
                                llWorkingDays6.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(Grejanje.this, "Vreme mora biti veće od prethodnog izabranog vremena", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, getHours(), getMinutes(), true);//Yes 24 hour time
                mTimePicker.setTitle("Izaberite vreme");
                mTimePicker.show();
            }
        });

        imbTimeWorkingDays4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Grejanje.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hours = set24hIfIs00(fillingWithZero(selectedHour), selectedMinute);
                        String minutes = fillingWithZero(selectedMinute);

                        timeWorkingDay4 = Integer.parseInt((hours + minutes));

                        if (timeWorkingDay4 > timeWorkingDay3) {
                            edtTimeWorkingDayTo4.setText(hours + ":" + minutes);

                            if (timeWorkingDay4 == 2400) {
                                llWorkingDays5.setVisibility(View.GONE);
                                llWorkingDays6.setVisibility(View.GONE);
                            } else {
                                llWorkingDays5.setVisibility(View.VISIBLE);
                                edtTimeWorkingDayFrom5.setText(hours + ":" + minutes);
                                timeWorkingDay5 = 2400;
                                edtTimeWorkingDayTo5.setText("24:00");
                                llWorkingDays6.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(Grejanje.this, "Vreme mora biti veće od prethodnog izabranog vremena", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, getHours(), getMinutes(), true);//Yes 24 hour time
                mTimePicker.setTitle("Izaberite vreme");
                mTimePicker.show();
            }
        });

        imbTimeWorkingDays5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Grejanje.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hours = set24hIfIs00(fillingWithZero(selectedHour), selectedMinute);
                        String minutes = fillingWithZero(selectedMinute);

                        timeWorkingDay5 = Integer.parseInt((hours + minutes));

                        if (timeWorkingDay5 > timeWorkingDay4) {
                            edtTimeWorkingDayTo5.setText(hours + ":" + minutes);

                            if (timeWorkingDay5 == 2400) {
                                llWorkingDays6.setVisibility(View.GONE);
                            } else {
                                llWorkingDays6.setVisibility(View.VISIBLE);
                                timeWorkingDay6 = 2400;
                                edtTimeWorkingDayFrom6.setText(hours + ":" + minutes);
                                edtTimeWorkingDayTo6.setText("24:00");
                            }
                        } else {
                            Toast.makeText(Grejanje.this, "Vreme mora biti veće od prethodnog izabranog vremena", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, getHours(), getMinutes(), true);//Yes 24 hour time
                mTimePicker.setTitle("Izaberite vreme");
                mTimePicker.show();
            }
        });
    }

    private void setListenerChoseTimeWeekend() {
        imbTimeWeekend1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Grejanje.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hours = fillingWithZero(selectedHour);
                        String minutes = fillingWithZero(selectedMinute);

                        if (hours.equals("00") && minutes.equals("00")) {
                            hours = "24";
                            llWeekend2.setVisibility(View.GONE);
                            llWeekend3.setVisibility(View.GONE);
                            llWeekend4.setVisibility(View.GONE);
                            llWeekend5.setVisibility(View.GONE);
                            llWeekend6.setVisibility(View.GONE);
                        } else {
                            llWeekend2.setVisibility(View.VISIBLE);
                            edtTimeWeekendTo2.setText("24:00");
                            timeWeekend2 = 2400;
                            llWeekend3.setVisibility(View.GONE);
                            llWeekend4.setVisibility(View.GONE);
                            llWeekend5.setVisibility(View.GONE);
                            llWeekend6.setVisibility(View.GONE);
                        }

                        timeWeekend1 = Integer.parseInt((hours + minutes));

                        edtTimeWeekendTo1.setText(hours + ":" + minutes);
                        edtTimeWeekendFrom2.setText(hours + ":" + minutes);
                    }
                }, getHours(), getMinutes(), true);//Yes 24 hour time
                mTimePicker.setTitle("Izaberite vreme");
                mTimePicker.show();
            }
        });

        imbTimeWeekend2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Grejanje.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hours = set24hIfIs00(fillingWithZero(selectedHour), selectedMinute);
                        String minutes = fillingWithZero(selectedMinute);

                        timeWeekend2 = Integer.parseInt((hours + minutes));

                        if (timeWeekend2 > timeWeekend1) {
                            edtTimeWeekendTo2.setText(hours + ":" + minutes);

                            if (timeWeekend2 == 2400) {
                                llWeekend3.setVisibility(View.GONE);
                                llWeekend4.setVisibility(View.GONE);
                                llWeekend5.setVisibility(View.GONE);
                                llWeekend6.setVisibility(View.GONE);
                            } else {
                                llWeekend3.setVisibility(View.VISIBLE);
                                edtTimeWeekendFrom3.setText(hours + ":" + minutes);
                                edtTimeWeekendTo3.setText("24:00");
                                timeWeekend3 = 2400;
                                llWeekend4.setVisibility(View.GONE);
                                llWeekend5.setVisibility(View.GONE);
                                llWeekend6.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(Grejanje.this, "Vreme mora biti veće od prethodnog izabranog vremena", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, getHours(), getMinutes(), true);//Yes 24 hour time
                mTimePicker.setTitle("Izaberite vreme");
                mTimePicker.show();
            }
        });

        imbTimeWeekend3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Grejanje.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hours = set24hIfIs00(fillingWithZero(selectedHour), selectedMinute);
                        String minutes = fillingWithZero(selectedMinute);

                        timeWeekend3 = Integer.parseInt((hours + minutes));

                        if (timeWeekend3 > timeWeekend2) {
                            edtTimeWeekendTo3.setText(hours + ":" + minutes);

                            if (timeWeekend3 == 2400) {
                                llWeekend4.setVisibility(View.GONE);
                                llWeekend5.setVisibility(View.GONE);
                                llWeekend6.setVisibility(View.GONE);
                            } else {
                                llWeekend4.setVisibility(View.VISIBLE);
                                edtTimeWeekendFrom4.setText(hours + ":" + minutes);
                                edtTimeWeekendTo4.setText("24:00");
                                timeWeekend4 = 2400;
                                llWeekend5.setVisibility(View.GONE);
                                llWeekend6.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(Grejanje.this, "Vreme mora biti veće od prethodnog izabranog vremena", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, getHours(), getMinutes(), true);//Yes 24 hour time
                mTimePicker.setTitle("Izaberite vreme");
                mTimePicker.show();
            }
        });

        imbTimeWeekend4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Grejanje.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hours = set24hIfIs00(fillingWithZero(selectedHour), selectedMinute);
                        String minutes = fillingWithZero(selectedMinute);

                        timeWeekend4 = Integer.parseInt((hours + minutes));

                        if (timeWeekend4 > timeWeekend3) {
                            edtTimeWeekendTo4.setText(hours + ":" + minutes);

                            if (timeWeekend4 == 2400) {
                                llWeekend5.setVisibility(View.GONE);
                                llWeekend6.setVisibility(View.GONE);
                            } else {
                                llWeekend5.setVisibility(View.VISIBLE);
                                edtTimeWeekendFrom5.setText(hours + ":" + minutes);
                                edtTimeWeekendTo5.setText("24:00");
                                timeWeekend5 = 2400;
                                llWeekend6.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(Grejanje.this, "Vreme mora biti veće od prethodnog izabranog vremena", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, getHours(), getMinutes(), true);//Yes 24 hour time
                mTimePicker.setTitle("Izaberite vreme");
                mTimePicker.show();
            }
        });

        imbTimeWeekend5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Grejanje.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hours = set24hIfIs00(fillingWithZero(selectedHour), selectedMinute);
                        String minutes = fillingWithZero(selectedMinute);

                        timeWeekend5 = Integer.parseInt((hours + minutes));

                        if (timeWeekend5 > timeWeekend4) {
                            edtTimeWeekendTo5.setText(hours + ":" + minutes);

                            if (timeWeekend5 == 2400) {
                                llWeekend6.setVisibility(View.GONE);
                            } else {
                                llWeekend6.setVisibility(View.VISIBLE);
                                edtTimeWeekendFrom6.setText(hours + ":" + minutes);
                                edtTimeWeekendTo6.setText("24:00");
                                timeWeekend6 = 2400;
                            }
                        } else {
                            Toast.makeText(Grejanje.this, "Vreme mora biti veće od prethodnog izabranog vremena", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, getHours(), getMinutes(), true);//Yes 24 hour time
                mTimePicker.setTitle("Izaberite vreme");
                mTimePicker.show();
            }
        });
    }

    private void setListenerForButtons() {
        imageButtonElMotor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("bozaSub/kuca/nodeGrejanje/vrstaRada", "0");
            }
        });

        imageButtonKotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("bozaSub/kuca/nodeGrejanje/vrstaRada", "1");
            }
        });

        imageButtonPlin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommand("bozaSub/kuca/nodeGrejanje/vrstaRada", "2");
            }
        });

        imageButtonSaveWorkingDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageForSending;
                String numberOfSegment = "6";

                if (edtTimeWorkingDayTo1.getText().toString().equals("24:00"))
                    numberOfSegment = "1";
                else if (edtTimeWorkingDayTo2.getText().toString().equals("24:00"))
                    numberOfSegment = "2";
                else if (edtTimeWorkingDayTo3.getText().toString().equals("24:00"))
                    numberOfSegment = "3";
                else if (edtTimeWorkingDayTo4.getText().toString().equals("24:00"))
                    numberOfSegment = "4";
                else if (edtTimeWorkingDayTo5.getText().toString().equals("24:00"))
                    numberOfSegment = "5";
                else if (edtTimeWorkingDayTo6.getText().toString().equals("24:00"))
                    numberOfSegment = "6";
                messageForSending = "1" + numberOfSegment;
                messageForSending = messageForSending + paddingWithZero(String.valueOf(timeWorkingDay1)) + edtTemperatureWorkingDay1.getText().toString();
                messageForSending = messageForSending + paddingWithZero(String.valueOf(timeWorkingDay2)) + edtTemperatureWorkingDay2.getText().toString();
                messageForSending = messageForSending + paddingWithZero(String.valueOf(timeWorkingDay3)) + edtTemperatureWorkingDay3.getText().toString();
                messageForSending = messageForSending + paddingWithZero(String.valueOf(timeWorkingDay4)) + edtTemperatureWorkingDay4.getText().toString();
                messageForSending = messageForSending + paddingWithZero(String.valueOf(timeWorkingDay5)) + edtTemperatureWorkingDay5.getText().toString();
                messageForSending = messageForSending + paddingWithZero(String.valueOf(timeWorkingDay6)) + edtTemperatureWorkingDay6.getText().toString();
                Log.e("poruka", messageForSending);
                sendCommand("bozaSub/kuca/nodeGrejanje/planogram", messageForSending);
                Toast.makeText(Grejanje.this, "Poslat planogram za radne dane na server", Toast.LENGTH_SHORT).show();
                sendCommand("bozaSub/kuca/nodeGrejanje/proveraPlanograma", "1");
            }
        });

        imageButtonSaveWeekend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageForSending;
                String numberOfSegment = "6";

                Log.e("poruka", timeWeekend1 + "");
                Log.e("poruka", timeWeekend2 + "");
                Log.e("poruka", timeWeekend3 + "");
                Log.e("poruka", timeWeekend4 + "");
                Log.e("poruka", timeWeekend5 + "");
                Log.e("poruka", timeWeekend6 + "");
                Log.e("poruka", edtTemperatureWeekend1.getText().toString() + "");
                Log.e("poruka", edtTemperatureWeekend2.getText().toString() + "");
                Log.e("poruka", edtTemperatureWeekend3.getText().toString() + "");
                Log.e("poruka", edtTemperatureWeekend4.getText().toString() + "");
                Log.e("poruka", edtTemperatureWeekend5.getText().toString() + "");
                Log.e("poruka", edtTemperatureWeekend6.getText().toString() + "");

                if (edtTimeWeekendTo1.getText().toString().equals("24:00"))
                    numberOfSegment = "1";
                else if (edtTimeWeekendTo2.getText().toString().equals("24:00"))
                    numberOfSegment = "2";
                else if (edtTimeWeekendTo3.getText().toString().equals("24:00"))
                    numberOfSegment = "3";
                else if (edtTimeWeekendTo4.getText().toString().equals("24:00"))
                    numberOfSegment = "4";
                else if (edtTimeWeekendTo5.getText().toString().equals("24:00"))
                    numberOfSegment = "5";
                else if (edtTimeWeekendTo6.getText().toString().equals("24:00"))
                    numberOfSegment = "6";
                messageForSending = "0" + numberOfSegment;
                messageForSending = messageForSending + paddingWithZero(String.valueOf(timeWeekend1)) + edtTemperatureWeekend1.getText().toString();
                messageForSending = messageForSending + paddingWithZero(String.valueOf(timeWeekend2)) + edtTemperatureWeekend2.getText().toString();
                messageForSending = messageForSending + paddingWithZero(String.valueOf(timeWeekend3)) + edtTemperatureWeekend3.getText().toString();
                messageForSending = messageForSending + paddingWithZero(String.valueOf(timeWeekend4)) + edtTemperatureWeekend4.getText().toString();
                messageForSending = messageForSending + paddingWithZero(String.valueOf(timeWeekend5)) + edtTemperatureWeekend5.getText().toString();
                messageForSending = messageForSending + paddingWithZero(String.valueOf(timeWeekend6)) + edtTemperatureWeekend6.getText().toString();
                Log.e("poruka", messageForSending);
                sendCommand("bozaSub/kuca/nodeGrejanje/planogram", messageForSending);
                Toast.makeText(Grejanje.this, "Poslat planogram za vikend na server", Toast.LENGTH_SHORT).show();
                sendCommand("bozaSub/kuca/nodeGrejanje/proveraPlanograma", "1");
            }
        });
    }

    private String set24hIfIs00(String hours, int minutes) {
        if (hours.equals("00") && minutes == 0)
            return "24";
        return hours;
    }

    private String fillingWithZero(int number) {
        String returner = number + "";
        if (returner.length() == 1)
            returner = "0" + returner;
        return returner;
    }

    private int getHours() {
        Calendar mcurrentTime = Calendar.getInstance();
        return mcurrentTime.get(Calendar.HOUR_OF_DAY);
    }

    private int getMinutes() {
        Calendar mcurrentTime = Calendar.getInstance();
        return mcurrentTime.get(Calendar.MINUTE);
    }

    private void mqttProccess() {
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    ucitavanjeTeksta();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                    Toast.makeText(MainActivity.this,"Problem sa konekcijom sa brokerom",Toast.LENGTH_LONG).show();
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
            public void messageArrived(String topic, MqttMessage message) {
                String messageFromMqttString = new String(message.getPayload());
                mqtt mqttClient = new mqtt();
                mqttClient.dolazecePoruke(topic, messageFromMqttString);

                switch (topic) {
                    case "bozaSub/kuca/node2/dvoriste/temperatura":
                        mqttTemperatureOutdoor();
                        break;
                    case "bozaSub/kuca/nodeGrejanje/temperaturaDnevniBoravak":
                        txvTemperatureDnevniBoravak.setText(vrednosti.temperaturaBoravak);
                        break;
                    case "bozaSub/kuca/nodeGrejanje/vrstaRada/stanje":
                        setTypeOfHeating();
                        break;
                    case "bozaSub/kuca/nodeGrejanje/grejanjeTemp/stanje":
                        txvKotaoStatus.setText(vrednosti.temperaturaRelej ? "RADI" : "NE RADI");
                        break;
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    private String paddingWithZero(String data) {
        if (data.length() == 3)
            return "0" + data;
        return data;
    }

    private String timeFormatFromMessage(String message) {
        String temp = message;
        if (message.length() == 3) {
            temp = "0" + message;
        }
        temp = temp.substring(0, 2) + ":" + temp.substring(2);
        return temp;
    }

    private void setStartingGui() {
        txvTemperatureOutdoor.setText(vrednosti.temperaturaNapolje);
    }

    private void mqttTemperatureOutdoor() {
        txvTemperatureOutdoor.setText(vrednosti.temperaturaNapolje);
        int temperatura = Integer.parseInt(vrednosti.temperaturaNapolje.substring(0, vrednosti.temperaturaNapolje.indexOf('°')));
        TextView tw = findViewById(R.id.txtMenuSign);
        if (temperatura > 25)
            tw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sun, 0, 0, 0);
        else if (temperatura > 15)
            tw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cloudy, 0, 0, 0);
        else if (temperatura > 10)
            tw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cloud, 0, 0, 0);
        else if (temperatura > 5)
            tw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wind, 0, 0, 0);
        else if (temperatura > 0)
            tw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rain, 0, 0, 0);
        else tw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.snow, 0, 0, 0);
        tw.setText(vrednosti.temperaturaNapolje.substring(0, vrednosti.temperaturaNapolje.indexOf('C')));
    }

    private void setTypeOfHeating() {
        if (vrednosti.vrstaRada == vrstaRadaGrejanje.elektroMotor) {
            imageButtonElMotor.setBackgroundResource(R.drawable.btn_circle_on);
            imageButtonKotao.setBackgroundResource(R.drawable.btn_circle_off);
            imageButtonPlin.setBackgroundResource(R.drawable.btn_circle_off);
        } else if (vrednosti.vrstaRada == vrstaRadaGrejanje.kotao) {
            imageButtonElMotor.setBackgroundResource(R.drawable.btn_circle_off);
            imageButtonKotao.setBackgroundResource(R.drawable.btn_circle_on);
            imageButtonPlin.setBackgroundResource(R.drawable.btn_circle_off);
        } else if (vrednosti.vrstaRada == vrstaRadaGrejanje.plin) {
            imageButtonElMotor.setBackgroundResource(R.drawable.btn_circle_off);
            imageButtonKotao.setBackgroundResource(R.drawable.btn_circle_off);
            imageButtonPlin.setBackgroundResource(R.drawable.btn_circle_on);
        }
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
        try {
            client.subscribe("bozaSub/kuca/#", 0);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void sendCommand(String topic, String message) {
        try {
            client.publish(topic, message.getBytes(), 0, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent searchIntent = new Intent(Grejanje.this, Settings.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent searchIntent = new Intent(Grejanje.this, MainActivity.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.dvoriste) {
            Intent searchIntent = new Intent(Grejanje.this, Dvoriste.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.kapija) {
            Intent searchIntent = new Intent(Grejanje.this, Kapija.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.fontana) {
            Intent searchIntent = new Intent(Grejanje.this, Fontana.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.grejanje) {
            Intent searchIntent = new Intent(Grejanje.this, Grejanje.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.klimaH) {
            Intent searchIntent = new Intent(Grejanje.this, KlimaH.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.klimaK) {
            Intent searchIntent = new Intent(Grejanje.this, KlimaK.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.kucica) {
            Intent searchIntent = new Intent(Grejanje.this, Kucica.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.ruter) {
            Intent searchIntent = new Intent(Grejanje.this, DeviceList.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

