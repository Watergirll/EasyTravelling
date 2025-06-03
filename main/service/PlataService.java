package main.service;

import main.domain.Plata;
import main.domain.Rezervare;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class PlataService {
    private List<Plata> plati;
    private static int nextId = 1;

    public PlataService() {
        this.plati = new ArrayList<>();
    }

    /**
     * Creaza o plata noua pentru o rezervare
     */
    public Plata creazaPlata(int idRezervare, String emailClient, double suma, String metodaPlata) {
        Plata plata = new Plata(nextId++, idRezervare, emailClient, suma, metodaPlata);
        plati.add(plata);
        return plata;
    }

    /**
     * Procesează o plata - simuleaza procesul de plata
     */
    public boolean proceseazaPlata(int idPlata) {
        Plata plata = gasestePlataDupaId(idPlata);
        if (plata == null) {
            return false;
        }

        // Simuleaza procesul de plata (90% success rate)
        boolean success = Math.random() > 0.1; // 90% success rate
        
        if (success) {
            plata.completeazaPlata();
            System.out.println("✅ Plata procesata cu succes!");
            System.out.println("Tranzactie ID: " + plata.getTranzactieId());
        } else {
            plata.esuarePlata();
            System.out.println("❌ Plata a esuat! Te rugam sa incerci din nou.");
        }
        
        return success;
    }

    /**
     * Gaseste plata dupa ID
     */
    public Plata gasestePlataDupaId(int idPlata) {
        return plati.stream()
                .filter(plata -> plata.getIdPlata() == idPlata)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returneaza toate platile unui client
     */
    public List<Plata> getPlatiClient(String emailClient) {
        return plati.stream()
                .filter(plata -> plata.getEmailClient().equals(emailClient))
                .collect(Collectors.toList());
    }

    /**
     * Returneaza platile pentru o rezervare specifica
     */
    public List<Plata> getPlatiPentruRezervare(int idRezervare) {
        return plati.stream()
                .filter(plata -> plata.getIdRezervare() == idRezervare)
                .collect(Collectors.toList());
    }

    /**
     * Verifica daca o rezervare are plata completata
     */
    public boolean areRezervareaPlatita(int idRezervare) {
        return plati.stream()
                .anyMatch(plata -> plata.getIdRezervare() == idRezervare && 
                         "completed".equals(plata.getStatus()));
    }

    /**
     * Returneaza platile dupa status
     */
    public List<Plata> getPlatiDupaStatus(String status) {
        return plati.stream()
                .filter(plata -> plata.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    /**
     * Returneaza toate platile
     */
    public List<Plata> getToatePlatile() {
        return new ArrayList<>(plati);
    }

    /**
     * Calculeaza totalul platilor completate
     */
    public double getTotalPlatiCompletate() {
        return plati.stream()
                .filter(plata -> "completed".equals(plata.getStatus()))
                .mapToDouble(Plata::getSuma)
                .sum();
    }

    /**
     * Returneaza statistici despre plati
     */
    public String getStatisticiPlati() {
        long totalPlati = plati.size();
        long platiCompletate = getPlatiDupaStatus("completed").size();
        long platiPending = getPlatiDupaStatus("pending").size();
        long platiEsuate = getPlatiDupaStatus("failed").size();
        double totalVenituri = getTotalPlatiCompletate();

        return String.format("💳 STATISTICI PLATI:\n" +
                           "• Total plati: %d\n" +
                           "• Plati completate: %d (%.1f%%)\n" +
                           "• Plati in asteptare: %d\n" +
                           "• Plati esuate: %d\n" +
                           "• Total venituri: %.2f RON",
                           totalPlati, platiCompletate, 
                           totalPlati > 0 ? (platiCompletate * 100.0 / totalPlati) : 0,
                           platiPending, platiEsuate, totalVenituri);
    }

    /**
     * Afiseaza metodele de plata disponibile
     */
    public void afiseazaMetodePlataDispo() {
        System.out.println("💳 METODE DE PLATA DISPONIBILE:");
        System.out.println("1. Card bancar (Visa/Mastercard)");
        System.out.println("2. Transfer bancar");
        System.out.println("3. PayPal");
        System.out.println("4. Cash (la sediu)");
    }

    /**
     * Valideaza metoda de plata
     */
    public boolean esteMetodaValidaPlata(String metoda) {
        return metoda.equalsIgnoreCase("card") ||
               metoda.equalsIgnoreCase("transfer") ||
               metoda.equalsIgnoreCase("paypal") ||
               metoda.equalsIgnoreCase("cash");
    }
} 