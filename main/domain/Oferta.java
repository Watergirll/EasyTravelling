package main.domain;

import main.domain.enums.Sezon;

public class Oferta {
    private int idOferta;
    private String nume;
    private String descriere;
    private String locatie;
    private Sezon sezon;
    private double pret;
    private String tipCazare; // hotel, pensiune, apartament, etc.
    private int numarZile;
    private boolean disponibila;
    private String facilitati; // piscina, spa, wifi, etc.
    
    public Oferta(int idOferta, String nume, String descriere, String locatie, 
                  Sezon sezon, double pret, String tipCazare, int numarZile) {
        this.idOferta = idOferta;
        this.nume = nume;
        this.descriere = descriere;
        this.locatie = locatie;
        this.sezon = sezon;
        this.pret = pret;
        this.tipCazare = tipCazare;
        this.numarZile = numarZile;
        this.disponibila = true;
        this.facilitati = "";
    }
    
    // Getters
    public int getIdOferta() { return idOferta; }
    public String getNume() { return nume; }
    public String getDescriere() { return descriere; }
    public String getLocatie() { return locatie; }
    public Sezon getSezon() { return sezon; }
    public double getPret() { return pret; }
    public String getTipCazare() { return tipCazare; }
    public int getNumarZile() { return numarZile; }
    public boolean isDisponibila() { return disponibila; }
    public String getFacilitati() { return facilitati; }
    
    // Setters
    public void setDisponibila(boolean disponibila) { this.disponibila = disponibila; }
    public void setFacilitati(String facilitati) { this.facilitati = facilitati; }
    public void setPret(double pret) { this.pret = pret; }
    
    @Override
    public String toString() {
        return String.format("🏖️ %s (%s)\n" +
                           "   📍 Locatie: %s | 🗓️ Sezon: %s | ⏰ %d zile\n" +
                           "   🏨 Cazare: %s | 💰 Pret: %.2f RON\n" +
                           "   📝 %s\n" +
                           "   ✨ Facilitati: %s\n" +
                           "   Status: %s",
                           nume, (disponibila ? "DISPONIBILA" : "INDISPONIBILA"),
                           locatie, sezon.getNume(), numarZile,
                           tipCazare, pret,
                           descriere,
                           facilitati.isEmpty() ? "Standard" : facilitati,
                           disponibila ? "✅ Poate fi rezervata" : "❌ Nu este disponibila");
    }
} 