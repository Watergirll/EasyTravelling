package main.service;

import main.domain.*;
import main.service.UserService;
import main.persistence.ClientRepository;
import main.persistence.AngajatRepository;
import java.util.List;
import java.util.ArrayList;

public class ReportService {
    private UserService userService;
    private ClientRepository clientRepository;
    private AngajatRepository angajatRepository;

    public ReportService() {
        this.userService = new UserService();
        this.clientRepository = new ClientRepository();
        this.angajatRepository = new AngajatRepository();
    }

    // Rapoarte pentru clienti
    public List<Client> getRaportClienti() {
        try {
            return clientRepository.loadAll();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public int getTotalClienti() {
        return getRaportClienti().size();
    }

    public List<Client> getClientiCuRezervari() {
        List<Client> clienti = getRaportClienti();
        List<Client> clientiCuRezervari = new ArrayList<>();
        
        for (Client client : clienti) {
            if (client.getRezervari() != null && !client.getRezervari().isEmpty()) {
                clientiCuRezervari.add(client);
            }
        }
        return clientiCuRezervari;
    }

    // Rapoarte pentru angajati
    public List<Angajat> getRaportAngajati() {
        try {
            return angajatRepository.loadAll();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public int getTotalAngajati() {
        return getRaportAngajati().size();
    }

    public List<Ghid> getGhizi() {
        List<Angajat> angajati = getRaportAngajati();
        List<Ghid> ghizi = new ArrayList<>();
        
        for (Angajat ang : angajati) {
            if (ang instanceof Ghid) {
                ghizi.add((Ghid) ang);
            }
        }
        return ghizi;
    }

    public List<AgentVanzari> getAgentiVanzari() {
        List<Angajat> angajati = getRaportAngajati();
        List<AgentVanzari> agenti = new ArrayList<>();
        
        for (Angajat ang : angajati) {
            if (ang instanceof AgentVanzari) {
                agenti.add((AgentVanzari) ang);
            }
        }
        return agenti;
    }

    // Calculeaza statistici salariale
    public double getSalariuMediuAngajati() {
        List<Angajat> angajati = getRaportAngajati();
        if (angajati.isEmpty()) {
            return 0.0;
        }

        double totalSalarii = 0.0;
        for (Angajat ang : angajati) {
            totalSalarii += ang.calculSalariu();
        }
        
        return totalSalarii / angajati.size();
    }

    public double getTotalSalarii() {
        List<Angajat> angajati = getRaportAngajati();
        double total = 0.0;
        
        for (Angajat ang : angajati) {
            total += ang.calculSalariu();
        }
        
        return total;
    }

    // Statistici rezervari
    public int getTotalRezervari() {
        List<Client> clienti = getRaportClienti();
        int totalRezervari = 0;
        
        for (Client client : clienti) {
            if (client.getRezervari() != null) {
                totalRezervari += client.getRezervari().size();
            }
        }
        
        return totalRezervari;
    }

    // Calculeaza rata de conversie (clienti cu rezervari vs total clienti)
    public double getRataConversie() {
        int totalClienti = getTotalClienti();
        if (totalClienti == 0) {
            return 0.0;
        }
        
        int clientiCuRezervari = getClientiCuRezervari().size();
        return (double) clientiCuRezervari / totalClienti * 100;
    }

    // Genereaza raport complet text pentru salvare
    public String genereazaRaportComplet() {
        StringBuilder raport = new StringBuilder();
        
        raport.append("=== RAPORT COMPLET EASYTRAVELLING ===\n\n");
        
        // Sectiunea Clienti
        raport.append("1. STATISTICI CLIENtI:\n");
        raport.append("   - Total clienti: ").append(getTotalClienti()).append("\n");
        raport.append("   - Clienti cu rezervari: ").append(getClientiCuRezervari().size()).append("\n");
        raport.append("   - Total rezervari: ").append(getTotalRezervari()).append("\n");
        raport.append("   - Rata de conversie: ").append(String.format("%.2f", getRataConversie())).append("%\n\n");
        
        // Sectiunea Angajati
        raport.append("2. STATISTICI ANGAJAtI:\n");
        raport.append("   - Total angajati: ").append(getTotalAngajati()).append("\n");
        raport.append("   - Ghizi: ").append(getGhizi().size()).append("\n");
        raport.append("   - Agenti vanzari: ").append(getAgentiVanzari().size()).append("\n");
        raport.append("   - Salariu mediu: ").append(String.format("%.2f", getSalariuMediuAngajati())).append(" RON\n");
        raport.append("   - Total salarii: ").append(String.format("%.2f", getTotalSalarii())).append(" RON\n\n");
        
        return raport.toString();
    }

    // Verifica daca datele pot fi salvate (simuleaza backup)
    public boolean efectueazaBackup() {
        try {
            // incearca sa acceseze toate datele pentru verificare
            getRaportClienti();
            getRaportAngajati();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
} 
