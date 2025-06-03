package main.service;

import main.domain.Oferta;
import main.domain.enums.Sezon;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CatalogService {
    private List<Oferta> oferteDisponibile;
    private static int nextId = 1;

    public CatalogService() {
        this.oferteDisponibile = new ArrayList<>();
        initializeOferte(); // Incarca oferele demo
    }

    /**
     * Initializeaza cateva oferte demo pentru testare
     */
    private void initializeOferte() {
        // Oferte de vara
        adaugaOferta("Vacanta la Mare - Mamaia", "Relaxare pe litoral cu plaja privata", 
                    "Mamaia", Sezon.VARA, 1200.0, "Hotel 4*", 7, "Piscina, Spa, WiFi");
        
        adaugaOferta("Exotic Paradise - Antalya", "Vacanta exotica in Turcia", 
                    "Antalya", Sezon.VARA, 1800.0, "Resort 5*", 10, "All Inclusive, Piscina, Spa");
        
        // Oferte de iarna
        adaugaOferta("Schi in Alpi - Brasov", "Weekend la munte cu schi", 
                    "Brasov", Sezon.IARNA, 800.0, "Cabana", 3, "Partii schi, Restaurant");
        
        adaugaOferta("Craciun in Paris", "Sarbatori magice in Orasul Luminilor", 
                    "Paris", Sezon.IARNA, 2500.0, "Hotel boutique", 5, "Mic dejun inclus, Tur ghidat");
        
        // Oferte de primavara
        adaugaOferta("Cherry Blossom - Japonia", "Florile de cires in Kyoto", 
                    "Kyoto", Sezon.PRIMAVARA, 4500.0, "Hotel traditional", 12, "Tur cultural, Guide");
        
        // Oferte tot anul
        adaugaOferta("City Break - Viena", "Cultura si arta in capitala Austriei", 
                    "Viena", Sezon.TOT_ANUL, 900.0, "Hotel 3*", 4, "Mic dejun, Tur optional");
        
        adaugaOferta("Safari Adventure - Kenya", "Aventura in savana africana", 
                    "Kenya", Sezon.TOT_ANUL, 3200.0, "Lodge safari", 8, "Safari jeep, All inclusive");
    }

    /**
     * Adauga o oferta noua in catalog
     */
    public void adaugaOferta(String nume, String descriere, String locatie, 
                           Sezon sezon, double pret, String tipCazare, int numarZile, String facilitati) {
        Oferta oferta = new Oferta(nextId++, nume, descriere, locatie, sezon, pret, tipCazare, numarZile);
        oferta.setFacilitati(facilitati);
        oferteDisponibile.add(oferta);
    }

    /**
     * Returneaza toate ofertele disponibile
     */
    public List<Oferta> getToateOfertele() {
        return oferteDisponibile.stream()
                .filter(Oferta::isDisponibila)
                .collect(Collectors.toList());
    }

    /**
     * Filtreaza ofertele dupa sezon
     */
    public List<Oferta> getOferteDupaSezon(Sezon sezon) {
        return oferteDisponibile.stream()
                .filter(Oferta::isDisponibila)
                .filter(oferta -> oferta.getSezon() == sezon || oferta.getSezon() == Sezon.TOT_ANUL)
                .collect(Collectors.toList());
    }

    /**
     * Filtreaza ofertele dupa locatie
     */
    public List<Oferta> getOferteDupaLocatie(String locatie) {
        return oferteDisponibile.stream()
                .filter(Oferta::isDisponibila)
                .filter(oferta -> oferta.getLocatie().toLowerCase().contains(locatie.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Filtreaza ofertele dupa sezon SI locatie
     */
    public List<Oferta> getOferteFiltered(Sezon sezon, String locatie) {
        return oferteDisponibile.stream()
                .filter(Oferta::isDisponibila)
                .filter(oferta -> {
                    boolean matchSezon = (sezon == null || oferta.getSezon() == sezon || oferta.getSezon() == Sezon.TOT_ANUL);
                    boolean matchLocatie = (locatie == null || locatie.isEmpty() || 
                                          oferta.getLocatie().toLowerCase().contains(locatie.toLowerCase()));
                    return matchSezon && matchLocatie;
                })
                .collect(Collectors.toList());
    }

    /**
     * Filtreaza ofertele dupa pret maxim
     */
    public List<Oferta> getOferteDupaPret(double pretMaxim) {
        return oferteDisponibile.stream()
                .filter(Oferta::isDisponibila)
                .filter(oferta -> oferta.getPret() <= pretMaxim)
                .collect(Collectors.toList());
    }

    /**
     * Gaseste oferta dupa ID
     */
    public Oferta getOfertaDupaId(int idOferta) {
        return oferteDisponibile.stream()
                .filter(oferta -> oferta.getIdOferta() == idOferta)
                .findFirst()
                .orElse(null);
    }

    /**
     * Actualizeaza disponibilitatea unei oferte
     */
    public boolean actualizeazaDisponibilitate(int idOferta, boolean disponibila) {
        Oferta oferta = getOfertaDupaId(idOferta);
        if (oferta != null) {
            oferta.setDisponibila(disponibila);
            return true;
        }
        return false;
    }

    /**
     * Returneaza statistici despre oferte
     */
    public String getStatisticiCatalog() {
        int total = oferteDisponibile.size();
        long disponibile = oferteDisponibile.stream().filter(Oferta::isDisponibila).count();
        
        double pretMediu = oferteDisponibile.stream()
                .filter(Oferta::isDisponibila)
                .mapToDouble(Oferta::getPret)
                .average()
                .orElse(0.0);

        return String.format("📊 STATISTICI CATALOG:\n" +
                           "• Total oferte: %d\n" +
                           "• Oferte disponibile: %d\n" +
                           "• Pret mediu: %.2f RON\n" +
                           "• Sezoane: Vara(%d), Iarna(%d), Primavara(%d), Toamna(%d), Tot anul(%d)",
                           total, disponibile, pretMediu,
                           countBySezon(Sezon.VARA), countBySezon(Sezon.IARNA),
                           countBySezon(Sezon.PRIMAVARA), countBySezon(Sezon.TOAMNA),
                           countBySezon(Sezon.TOT_ANUL));
    }

    private long countBySezon(Sezon sezon) {
        return oferteDisponibile.stream()
                .filter(Oferta::isDisponibila)
                .filter(oferta -> oferta.getSezon() == sezon)
                .count();
    }
} 