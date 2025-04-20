package model;

import model.enums.TipCamera;

import java.util.ArrayList;
import java.util.List;

public class Cazare {
    private int idCazare;
    private String nume;
    private Locatie locatie;
    private List<Camera> camere = new ArrayList<>();

    public Cazare(int idCazare, String nume) {
        this.idCazare = idCazare;
        this.nume = nume;

    }

    public int getIdCazare() {
        return idCazare;
    }

    public String getNume() {
        return nume;
    }

    public Locatie getLocatie() {
        return locatie;
    }

    public void setLocatie(Locatie locatie) {
        this.locatie = locatie;
        if (!locatie.getCazari().contains(this)) {
            locatie.adaugaCazare(this);
        }
    }

    //compozitie
    public void adaugaCamera(TipCamera tip, double pret, int nrDisponibile) {
        Camera cam = new Camera(tip, pret, nrDisponibile);
        camere.add(cam);
    }

    public List<Camera> getCamere() {
        return camere;
    }

    @Override
    public String toString() {
        return "Cazare: " + nume +
                (locatie != null ? locatie.getNume() : "locatie necunoscuta") +
                " | Camere: " + camere.size();
    }
}
