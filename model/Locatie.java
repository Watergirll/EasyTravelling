package model;

import java.util.ArrayList;
import java.util.List;

public class Locatie {
    private int idLocatie;
    private String nume;
    private Tara tara;
    private List<Cazare> cazari = new ArrayList<>();

    public Locatie(int idLocatie, String nume, Tara tara) {
        this.idLocatie = idLocatie;
        this.nume = nume;
        this.tara = tara;
    }

    public int getIdLocatie() {
        return idLocatie;
    }

    public String getNume() {
        return nume;
    }


    public Tara getTara() {
        return tara;
    }

    public List<Cazare> getCazari() {
        return cazari;
    }

    public void adaugaCazare(Cazare c) {
        if (!cazari.contains(c)) {
            cazari.add(c);
            c.setLocatie(this); // sincronizare inversa
        }
    }

    @Override
    public String toString() {
        return idLocatie + ": " + nume + " | Tara: " + (tara != null ? tara.getNume() : "necunoscuta") +
                " | Cazari: " + cazari.size();
    }
}
