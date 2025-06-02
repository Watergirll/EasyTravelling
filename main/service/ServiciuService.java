package main.service;

import main.domain.Serviciu;
import java.util.ArrayList;
import java.util.List;

public class ServiciuService {
    private List<Serviciu> servicii;

    public ServiciuService() {
        this.servicii = new ArrayList<>();
        initializeazaServicii();
    }

    private void initializeazaServicii() {
        // Adaugam servicii demo
        servicii.add(new Serviciu(1, "Transport cu autocarul", "Transport", 50.0, 50, true));
        servicii.add(new Serviciu(2, "Ghid turistic personal", "Ghidare", 120.0, 1, true));
        servicii.add(new Serviciu(3, "Cazare hotel 4*", "Cazare", 200.0, 20, true));
        servicii.add(new Serviciu(4, "Masa completa (3 feluri)", "Restaurante", 80.0, 100, true));
        servicii.add(new Serviciu(5, "Excursie cu barca", "Activitati recreative", 150.0, 15, false));
        servicii.add(new Serviciu(6, "Bilete muzeu", "Intrari", 25.0, 200, true));
        servicii.add(new Serviciu(7, "Transfer aeroport", "Transport", 60.0, 10, true));
        servicii.add(new Serviciu(8, "Tur fotografic", "Experiente", 180.0, 5, false));
    }

    public List<Serviciu> getToateServiciile() {
        return new ArrayList<>(servicii);
    }

    public List<Serviciu> cautaDupaCategorie(String categoria) {
        List<Serviciu> rezultate = new ArrayList<>();
        for (Serviciu serv : servicii) {
            if (serv.getCategorie().toLowerCase().contains(categoria.toLowerCase())) {
                rezultate.add(serv);
            }
        }
        return rezultate;
    }

    public List<Serviciu> cautaDupaPret(double pretMaxim) {
        List<Serviciu> rezultate = new ArrayList<>();
        for (Serviciu serv : servicii) {
            if (serv.getPret() <= pretMaxim) {
                rezultate.add(serv);
            }
        }
        return rezultate;
    }

    public List<Serviciu> getServiciiDisponibile() {
        List<Serviciu> disponibile = new ArrayList<>();
        for (Serviciu serv : servicii) {
            if (serv.isDisponibil() && serv.getLocuriDisponibile() > 0) {
                disponibile.add(serv);
            }
        }
        return disponibile;
    }

    public Serviciu getServiciuById(int id) {
        for (Serviciu serv : servicii) {
            if (serv.getIdServiciu() == id) {
                return serv;
            }
        }
        return null;
    }

    public boolean rezervaServiciu(int idServiciu, int locuriDorite) {
        Serviciu serviciu = getServiciuById(idServiciu);
        if (serviciu != null && serviciu.isDisponibil() && 
            serviciu.getLocuriDisponibile() >= locuriDorite) {
            serviciu.setLocuriDisponibile(serviciu.getLocuriDisponibile() - locuriDorite);
            return true;
        }
        return false;
    }

    public double calculeazaPretTotal(int idServiciu, int locuriDorite) {
        Serviciu serviciu = getServiciuById(idServiciu);
        if (serviciu != null) {
            return serviciu.getPret() * locuriDorite;
        }
        return 0.0;
    }

    public List<String> getCategoriiDisponibile() {
        List<String> categorii = new ArrayList<>();
        for (Serviciu serv : servicii) {
            if (!categorii.contains(serv.getCategorie())) {
                categorii.add(serv.getCategorie());
            }
        }
        return categorii;
    }

    public int getTotalServicii() {
        return servicii.size();
    }
} 
