package main.service;

import main.domain.Locatie;
import main.domain.Tara;
import java.util.ArrayList;
import java.util.List;

public class LocatieService {
    private List<Locatie> locatii;

    public LocatieService() {
        this.locatii = new ArrayList<>();
        initializeazaLocatii();
    }

    private void initializeazaLocatii() {
        // Adaugam locatii demo folosind constructorul existent
        locatii.add(new Locatie(1, "Brasov", new Tara("Romania")));
        locatii.add(new Locatie(2, "Sinaia", new Tara("Romania")));
        locatii.add(new Locatie(3, "Sighisoara", new Tara("Romania")));
        locatii.add(new Locatie(4, "Cluj-Napoca", new Tara("Romania")));
        locatii.add(new Locatie(5, "Constanta", new Tara("Romania")));
        locatii.add(new Locatie(6, "Paris", new Tara("Franta")));
        locatii.add(new Locatie(7, "Roma", new Tara("Italia")));
        locatii.add(new Locatie(8, "Barcelona", new Tara("Spania")));
    }

    public List<Locatie> getToateLocatiile() {
        return new ArrayList<>(locatii);
    }

    public List<Locatie> cautaDupaTara(String numeTara) {
        List<Locatie> rezultate = new ArrayList<>();
        for (Locatie loc : locatii) {
            if (loc.getTara() != null && 
                loc.getTara().getNume().toLowerCase().contains(numeTara.toLowerCase())) {
                rezultate.add(loc);
            }
        }
        return rezultate;
    }

    public List<Locatie> cautaDupaNumeLocatie(String numeLocatie) {
        List<Locatie> rezultate = new ArrayList<>();
        for (Locatie loc : locatii) {
            if (loc.getNume().toLowerCase().contains(numeLocatie.toLowerCase())) {
                rezultate.add(loc);
            }
        }
        return rezultate;
    }

    public Locatie getLocatieById(int id) {
        for (Locatie loc : locatii) {
            if (loc.getIdLocatie() == id) {
                return loc;
            }
        }
        return null;
    }

    public int getTotalLocatii() {
        return locatii.size();
    }

    public List<String> getTariDisponibile() {
        List<String> tari = new ArrayList<>();
        for (Locatie loc : locatii) {
            if (loc.getTara() != null) {
                String numeTara = loc.getTara().getNume();
                if (!tari.contains(numeTara)) {
                    tari.add(numeTara);
                }
            }
        }
        return tari;
    }
} 
