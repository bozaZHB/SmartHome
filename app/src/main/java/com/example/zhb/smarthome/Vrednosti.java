package com.example.zhb.smarthome;

enum vrstaRadaGrejanje {
    kotao,
    elektroMotor,
    plin,
    nepoznato
}

public class Vrednosti {
    //senzori
    public static boolean IMEI;
    public static String mqttBroker = "tcp://192.168.0.100:1883";//"tcp://192.168.0.100:1883"  "tcp://iot.eclipse.org:1883" tcp://broker.mqttdashboard.com

    public static String temperaturaNapolje = "22°C";
    public static String vlaznostNapolje = "55%";
    public static String temperaturaBoravak = "32°C";
    public static String vlaznostBoravak = "65%";
    public static String temperaturaHodnik = "52°C";
    public static String vlaznostHodnik = "75%";

    public static boolean prskalicaAll = false;
    public static boolean prskalica1 = false;
    public static boolean prskalica2 = false;
    public static boolean prskalica3 = false;
    public static boolean prskalica4 = false;
    public static boolean prskalica5 = false;
    public static boolean prskalica6 = false;
    public static boolean fontanaPrskalica = false;
    public static boolean fontanaSvetlo = false;
    public static boolean garazaSpolna = false;
    public static boolean garazaUnutrasnja = false;
    public static byte kapijaIza = 0;
    public static boolean svetlo1 = false; //neonka
    public static boolean svetlo2 = false;
    public static boolean svetlo3 = false;
    public static boolean svetlo4 = false;
    public static boolean kucicaVrata = false;
    public static boolean kucicaSvetloNapolje = false;
    public static boolean kucicaSvetloUnutra = false;
    public static boolean dvoristeRadio = false;
    public static boolean kucicaRadio = false;
    public static boolean ulaznaVrata = false;
    public static boolean neodredjenoFontana = false;
    public static String vremeMikrokontroler = "10:00"; //vreme na mikrokontroleru
    //tajmeri
    public static boolean prskalicaTimer = true;
    public static String vremePrskalica1 = "11:00";
    public static String vremePrskalica2 = "12:00";
    public static String trajanjePrskalice = "10";

    public static boolean grejanjeTimer = false;
    public static String vremeGrejanje = "10:00";
    public static String komandaGrejanje = "on";

    public static boolean klimaHodnikTimer = false;
    public static String vremeKlimaHodnik = "10:00";
    public static String komandaKlimaHodnik = "on";

    public static boolean klimaKuhinjaTimer = false;
    public static String vremeKlimaKuhinja = "10:00";
    public static String komandaKlimaKuhinja = "on";
    //provera konektivnosti
    public static boolean nodeFontana = false;
    public static boolean nodeTerasaNeonka = false;
    public static boolean nodeVrataUlazna = false;
    public static boolean nodeGrejanje = false;

    //grejanje
    public static int brojVremenaRadniDan = 0;
    public static int brojVremenaVikend = 0;

    public static boolean kotao = false;
    public static boolean eletroMotor = false;
    public static boolean plin = false;
    public static boolean glavniPrekidac = false;
    public static boolean temperaturaRelej = false;

    public static vrstaRadaGrejanje vrstaRada = vrstaRadaGrejanje.nepoznato;

    public static String vremeGrejanjaRadniDan1 = "11:00";
    public static String vremeGrejanjaRadniDan2 = "11:00";
    public static String vremeGrejanjaRadniDan3 = "11:00";
    public static String vremeGrejanjaRadniDan4 = "11:00";
    public static String vremeGrejanjaRadniDan5 = "11:00";
    public static String vremeGrejanjaRadniDan6 = "11:00";

    public static String temperaturaGrejanjaRadniDan1 = "11";
    public static String temperaturaGrejanjaRadniDan2 = "11";
    public static String temperaturaGrejanjaRadniDan3 = "11";
    public static String temperaturaGrejanjaRadniDan4 = "11";
    public static String temperaturaGrejanjaRadniDan5 = "11";
    public static String temperaturaGrejanjaRadniDan6 = "11";

    public static String vremeGrejanjaVikend1 = "15:00";
    public static String vremeGrejanjaVikend2 = "15:00";
    public static String vremeGrejanjaVikend3 = "15:00";
    public static String vremeGrejanjaVikend4 = "15:00";
    public static String vremeGrejanjaVikend5 = "15:00";
    public static String vremeGrejanjaVikend6 = "15:00";

    public static String temperaturaGrejanjaVikend1 = "22";
    public static String temperaturaGrejanjaVikend2 = "22";
    public static String temperaturaGrejanjaVikend3 = "22";
    public static String temperaturaGrejanjaVikend4 = "22";
    public static String temperaturaGrejanjaVikend5 = "22";
    public static String temperaturaGrejanjaVikend6 = "22";
}
