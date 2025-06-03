package main.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Plata {
    private int idPlata;
    private int idRezervare;
    private String emailClient;
    private double suma;
    private String metodaPlata; // card, transfer, cash
    private LocalDateTime dataPlata;
    private LocalDate dataPlataVeche; // pentru compatibilitate cu BD
    private String status; // pending, completed, failed
    private String tranzactieId;
    private Rezervare rezervare; // pentru compatibilitate cu structura veche
    
    // Constructor NOU pentru catalog (cu functionalitate extinsa)
    public Plata(int idPlata, int idRezervare, String emailClient, double suma, String metodaPlata) {
        this.idPlata = idPlata;
        this.idRezervare = idRezervare;
        this.emailClient = emailClient;
        this.suma = suma;
        this.metodaPlata = metodaPlata;
        this.dataPlata = LocalDateTime.now();
        this.status = "pending";
        this.tranzactieId = "TRX" + System.currentTimeMillis();
        this.rezervare = null; // nu avem referinta directa la rezervare
    }
    
    // Constructor VECHI pentru compatibilitate cu BD si clasa Rezervare
    public Plata(int nrPlata, double suma, LocalDate data, Rezervare rezervare) {
        this.idPlata = nrPlata;
        this.suma = suma;
        this.dataPlataVeche = data;
        this.dataPlata = data.atStartOfDay(); // convertim la LocalDateTime
        this.rezervare = rezervare;
        this.idRezervare = rezervare != null ? rezervare.getIdRezervare() : 0;
        this.emailClient = rezervare != null && rezervare.getClient() != null ? 
                          rezervare.getClient().getEmail() : "necunoscut";
        this.metodaPlata = "cash"; // default pentru platile vechi
        this.status = "completed"; // platile vechi se considera completate
        this.tranzactieId = "OLD" + System.currentTimeMillis();
    }
    
    // Getters
    public int getIdPlata() { return idPlata; }
    public int getIdRezervare() { return idRezervare; }
    public String getEmailClient() { return emailClient; }
    public double getSuma() { return suma; }
    public String getMetodaPlata() { return metodaPlata; }
    public LocalDateTime getDataPlata() { return dataPlata; }
    public LocalDate getDataPlataVeche() { return dataPlataVeche; }
    public String getStatus() { return status; }
    public String getTranzactieId() { return tranzactieId; }
    public Rezervare getRezervare() { return rezervare; }
    
    // Setters
    public void setStatus(String status) { this.status = status; }
    public void setRezervare(Rezervare rezervare) { 
        this.rezervare = rezervare;
        if (rezervare != null) {
            this.idRezervare = rezervare.getIdRezervare();
            if (this.emailClient == null || this.emailClient.equals("necunoscut")) {
                this.emailClient = rezervare.getClient() != null ? 
                                  rezervare.getClient().getEmail() : "necunoscut";
            }
        }
    }
    
    public void completeazaPlata() {
        this.status = "completed";
        this.dataPlata = LocalDateTime.now();
    }
    
    public void esuarePlata() {
        this.status = "failed";
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String statusIcon = switch (status) {
            case "completed" -> "✅";
            case "failed" -> "❌";
            default -> "⏳";
        };
        
        // Pentru platile vechi (cu rezervare directa)
        if (rezervare != null) {
            return String.format("%s Plata #%d - %s\n" +
                               "   💰 Suma: %.2f RON | 📅 Data: %s\n" +
                               "   📋 Rezervare: #%d",
                               statusIcon, idPlata, 
                               status.equals("completed") ? "Platita" : "In asteptare",
                               suma, 
                               dataPlataVeche != null ? dataPlataVeche.toString() : dataPlata.format(formatter),
                               idRezervare);
        }
        
        // Pentru platile noi (cu functionalitate extinsa)
        return String.format("%s Plata #%d (ID Rezervare: %d)\n" +
                           "   💰 Suma: %.2f RON | 💳 Metoda: %s\n" +
                           "   📅 Data: %s | 🔢 ID Tranzactie: %s\n" +
                           "   👤 Client: %s",
                           statusIcon, idPlata, idRezervare,
                           suma, metodaPlata,
                           dataPlata.format(formatter), tranzactieId,
                           emailClient);
    }
}

