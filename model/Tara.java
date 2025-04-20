package model;

import java.util.ArrayList;
import java.util.List;

public class Tara {
    private String nume;
    private List<Catalog> cataloage = new ArrayList<>(); // colectie many

    public Tara(String nume) {
        this.nume = nume;

    }

    public String getNume() {
        return nume;
    }


    public List<Catalog> getCataloage() {
        return cataloage;
    }

    public void adaugaCatalog(Catalog catalog) {
        if (!cataloage.contains(catalog)) {
            cataloage.add(catalog);
            catalog.setTara(this); // ↔ sincronizare inversa
        }
    }

    @Override
    public String toString() {
        return nume + "| Cataloage: " + cataloage.size();
    }
}
