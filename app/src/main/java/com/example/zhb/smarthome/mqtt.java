package com.example.zhb.smarthome;

import android.util.Log;

public class mqtt {
    Vrednosti vr;

    public void dolazecePoruke(String topic, String poruka) {
        if (topic.equals("bozaSub/kuca/node1/prskalica1/stanje")) {
            if (poruka.substring(0, 1).equals("1")) {
                vr.prskalica1 = true;
                vr.prskalica2 = false;
                vr.prskalica3 = false;
                vr.prskalica4 = false;
                vr.prskalica5 = false;
                vr.prskalica6 = false;
            } else if (poruka.substring(0, 1).equals("0")) vr.prskalica1 = false;
        } else if (topic.equals("bozaSub/kuca/node1/prskalica2/stanje")) {
            if (poruka.substring(0, 1).equals("1")) {
                vr.prskalica1 = false;
                vr.prskalica2 = true;
                vr.prskalica3 = false;
                vr.prskalica4 = false;
                vr.prskalica5 = false;
                vr.prskalica6 = false;
            } else if (poruka.substring(0, 1).equals("0")) vr.prskalica2 = false;
        } else if (topic.equals("bozaSub/kuca/node1/prskalica3/stanje")) {
            if (poruka.substring(0, 1).equals("1")) {
                vr.prskalica1 = false;
                vr.prskalica2 = false;
                vr.prskalica3 = true;
                vr.prskalica4 = false;
                vr.prskalica5 = false;
                vr.prskalica6 = false;
            } else if (poruka.substring(0, 1).equals("0")) vr.prskalica3 = false;
        } else if (topic.equals("bozaSub/kuca/node1/prskalica4/stanje")) {
            if (poruka.substring(0, 1).equals("1")) {
                vr.prskalica1 = false;
                vr.prskalica2 = false;
                vr.prskalica3 = false;
                vr.prskalica4 = true;
                vr.prskalica5 = false;
                vr.prskalica6 = false;
            } else if (poruka.substring(0, 1).equals("0")) vr.prskalica4 = false;
        } else if (topic.equals("bozaSub/kuca/node1/prskalica5/stanje")) {
            if (poruka.substring(0, 1).equals("1")) {
                vr.prskalica1 = false;
                vr.prskalica2 = false;
                vr.prskalica3 = false;
                vr.prskalica4 = false;
                vr.prskalica5 = true;
                vr.prskalica6 = false;
            } else if (poruka.substring(0, 1).equals("0")) vr.prskalica5 = false;
        } else if (topic.equals("bozaSub/kuca/node1/prskalica6/stanje")) {
            if (poruka.substring(0, 1).equals("1")) {
                vr.prskalica1 = false;
                vr.prskalica2 = false;
                vr.prskalica3 = false;
                vr.prskalica4 = false;
                vr.prskalica5 = false;
                vr.prskalica6 = true;
            } else if (poruka.substring(0, 1).equals("0")) vr.prskalica6 = false;
        } else if (topic.equals("bozaSub/kuca/node1/fontanaPumpa/stanje")) {
            if (poruka.substring(0, 1).equals("1")) vr.fontanaPrskalica = true;
            else if (poruka.substring(0, 1).equals("0")) vr.fontanaPrskalica = false;
        } else if (topic.equals("bozaSub/kuca/node1/radio/stanje")) {
            if (poruka.substring(0, 1).equals("1")) vr.dvoristeRadio = true;
            else if (poruka.substring(0, 1).equals("0")) vr.dvoristeRadio = false;
        } else if (topic.equals("bozaSub/kuca/node1/neodredjeno/stanje")) {
            if (poruka.substring(0, 1).equals("1")) vr.neodredjenoFontana = true;
            else if (poruka.substring(0, 1).equals("0")) vr.neodredjenoFontana = false;
        } else if (topic.equals("bozaSub/kuca/node1/prskalicaAll/stanje")) {
            if (poruka.substring(0, 1).equals("1")) vr.prskalicaAll = true;
            else if (poruka.substring(0, 1).equals("0")) {
                vr.prskalicaAll = false;
                vr.prskalica1 = false;
                vr.prskalica2 = false;
                vr.prskalica3 = false;
                vr.prskalica4 = false;
                vr.prskalica5 = false;
                vr.prskalica6 = false;
            }
        } else if (topic.equals("bozaSub/kuca/node1/stanje/stanje")) {
            if (poruka.substring(0, 1).equals("1")) vr.prskalica1 = true;
            else if (poruka.substring(0, 1).equals("0")) vr.prskalica1 = false;
            if (poruka.substring(1, 2).equals("1")) vr.prskalica2 = true;
            else if (poruka.substring(1, 2).equals("0")) vr.prskalica2 = false;
            if (poruka.substring(2, 3).equals("1")) vr.prskalica3 = true;
            else if (poruka.substring(2, 3).equals("0")) vr.prskalica3 = false;
            if (poruka.substring(3, 4).equals("1")) vr.prskalica4 = true;
            else if (poruka.substring(3, 4).equals("0")) vr.prskalica4 = false;
            if (poruka.substring(4, 5).equals("1")) vr.prskalica5 = true;
            else if (poruka.substring(4, 5).equals("0")) vr.prskalica5 = false;
            if (poruka.substring(5, 6).equals("1")) vr.prskalica6 = true;
            else if (poruka.substring(5, 6).equals("0")) vr.prskalica6 = false;

            if (poruka.substring(0, 1).equals("1") || poruka.substring(1, 2).equals("1") || poruka.substring(2, 3).equals("1") || poruka.substring(3, 4).equals("1") || poruka.substring(4, 5).equals("1") || poruka.substring(5, 6).equals("1"))
                vr.prskalicaAll = true;
            else vr.prskalicaAll = false;

            if (poruka.substring(6, 7).equals("1")) vr.fontanaPrskalica = true;
            else if (poruka.substring(6, 7).equals("0")) vr.fontanaPrskalica = false;

            if (poruka.substring(7, 8).equals("1")) vr.neodredjenoFontana = true;
            else if (poruka.substring(7, 8).equals("0")) vr.neodredjenoFontana = false;

            if (poruka.substring(8, 9).equals("1")) vr.prskalicaTimer = true;
            else if (poruka.substring(8, 9).equals("0")) vr.prskalicaTimer = false;

            vr.nodeFontana = true;
        } else if (topic.equals("bozaSub/kuca/node1/timer")) {
            if (poruka.equals("1")) vr.prskalicaTimer = true;
            else if (poruka.equals("0")) vr.prskalicaTimer = false;
        } else if (topic.equals("bozaSub/kuca/node1/trajanje/stanje")) {
            vr.trajanjePrskalice = poruka;
        } else if (topic.equals("bozaSub/kuca/node1/vreme1/stanje") || topic.equals("bozaSub/kuca/node1/vreme1")) {
            vr.vremePrskalica1 = poruka;
        } else if (topic.equals("bozaSub/kuca/node1/vreme2/stanje") || topic.equals("bozaSub/kuca/node1/vreme2")) {
            vr.vremePrskalica2 = poruka;
        } else if (topic.equals("bozaSub/kuca/node2/dvoriste/temperatura")) {
            vr.temperaturaNapolje = poruka.substring(0, poruka.indexOf('.')) + "°C";
        } else if (topic.equals("bozaSub/kuca/node2/dvoriste/vlaznost")) {
            vr.vlaznostNapolje = poruka.substring(0, poruka.indexOf('.')) + "%";
        } else if (topic.equals("bozaSub/kuca/node2/neonka/stanje")) {
            if (poruka.equals("1")) vr.svetlo1 = true;
            else if (poruka.equals("0")) vr.svetlo1 = false;

            vr.nodeTerasaNeonka = true;
        } else if (topic.equals("bozaSub/kuca/node3/vrata/stanje")) {
            if (poruka.equals("1")) vr.ulaznaVrata = true;
            else if (poruka.equals("0")) vr.ulaznaVrata = false;

            vr.nodeVrataUlazna = true;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/temperaturaDnevniBoravak")) {
//            vr.temperaturaBoravak = poruka.substring(0, poruka.indexOf('.'))+"°C";
            vr.temperaturaBoravak = poruka + "°C";
        }
        //vreme na mikrokontroleru
        else if (topic.equals("bozaSub/kuca/node1/vremeNaMikrokontroleru/stanje")) {
            vr.vremeMikrokontroler = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/vrstaRada/stanje")) {
            if (poruka.equals("0")) {
                vr.vrstaRada = vrstaRadaGrejanje.elektroMotor;
            } else if (poruka.equals("1")) {
                vr.vrstaRada = vrstaRadaGrejanje.kotao;
            } else if (poruka.equals("2")) {
                vr.vrstaRada = vrstaRadaGrejanje.plin;
            }
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/grejanjeTemp/stanje")) {
            vr.temperaturaRelej = poruka.equals("1");
        }
        // radni dan
        else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/10/stanje")) {
            vr.brojVremenaRadniDan = Integer.parseInt(poruka);
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/11/temperatura")) {
            vr.temperaturaGrejanjaRadniDan1 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/11/vreme")) {
            vr.vremeGrejanjaRadniDan1 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/12/temperatura")) {
            vr.temperaturaGrejanjaRadniDan2 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/12/vreme")) {
            vr.vremeGrejanjaRadniDan2 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/13/temperatura")) {
            vr.temperaturaGrejanjaRadniDan3 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/13/vreme")) {
            vr.vremeGrejanjaRadniDan3 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/14/temperatura")) {
            vr.temperaturaGrejanjaRadniDan4 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/14/vreme")) {
            vr.vremeGrejanjaRadniDan4 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/15/temperatura")) {
            vr.temperaturaGrejanjaRadniDan5 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/15/vreme")) {
            vr.vremeGrejanjaRadniDan5 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/16/temperatura")) {
            vr.temperaturaGrejanjaRadniDan6 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/16/vreme")) {
            vr.vremeGrejanjaRadniDan6 = poruka;
        }
        // vikend
        else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/00/stanje")) {
            vr.brojVremenaVikend = Integer.parseInt(poruka);
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/01/temperatura")) {
            vr.temperaturaGrejanjaVikend1 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/01/vreme")) {
            vr.vremeGrejanjaVikend1 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/02/temperatura")) {
            vr.temperaturaGrejanjaVikend2 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/02/vreme")) {
            vr.vremeGrejanjaVikend2 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/03/temperatura")) {
            vr.temperaturaGrejanjaVikend3 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/03/vreme")) {
            vr.vremeGrejanjaVikend3 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/04/temperatura")) {
            vr.temperaturaGrejanjaVikend4 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/04/vreme")) {
            vr.vremeGrejanjaVikend4 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/05/temperatura")) {
            vr.temperaturaGrejanjaVikend5 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/05/vreme")) {
            vr.vremeGrejanjaVikend5 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/06/temperatura")) {
            vr.temperaturaGrejanjaVikend6 = poruka;
        } else if (topic.equals("bozaSub/kuca/nodeGrejanje/proveraPlanograma/06/vreme")) {
            vr.vremeGrejanjaVikend6 = poruka;
        }
    }

    private String timeFormatFromMessage(String message){
        String temp = message;
        if(message.length() == 3){
            temp = "0"+message;
        }
        temp = temp.substring(0,2) + ":" + temp.substring(2);
        return temp;
    }

}
