package main.service;

import main.domain.*;
import main.service.UserService;
import main.service.AuditService;
import main.persistence.ClientRepository;
import main.persistence.AngajatRepository;
import java.util.List;
import java.util.ArrayList;

public class ReportService {
    private UserService userService;
    private AuditService auditService;

    public ReportService() {
        this.userService = new UserService();
        this.auditService = AuditService.getInstance();
    }
    
    /**
     * Constructor cu UserService partajat
     */
    public ReportService(UserService sharedUserService) {
        this.userService = sharedUserService;
        this.auditService = AuditService.getInstance();
    }

    // Rapoarte pentru clienti
    public List<Client> getRaportClienti() {
        try {
            return userService.getClientRepository().loadAll();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public int getTotalClienti() {
        return userService.getTotalClienti();
    }

    public List<Client> getClientiCuRezervari() {
        try {
            List<Client> clienti = userService.getClientRepository().loadAll();
            List<Client> clientiCuRezervari = new ArrayList<>();
            
            for (Client client : clienti) {
                if (client.getRezervari() != null && !client.getRezervari().isEmpty()) {
                    clientiCuRezervari.add(client);
                }
            }
            return clientiCuRezervari;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // Rapoarte pentru angajati
    public List<Angajat> getRaportAngajati() {
        try {
            return userService.getAngajatRepository().loadAll();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public int getTotalAngajati() {
        return userService.getTotalAngajati();
    }

    public List<Ghid> getRaportGhizi() {
        List<Angajat> angajati = getRaportAngajati();
        List<Ghid> ghizi = new ArrayList<>();
        
        for (Angajat ang : angajati) {
            if (ang instanceof Ghid) {
                ghizi.add((Ghid) ang);
            }
        }
        return ghizi;
    }

    public List<AgentVanzari> getRaportAgenti() {
        List<Angajat> angajati = getRaportAngajati();
        List<AgentVanzari> agenti = new ArrayList<>();
        
        for (Angajat ang : angajati) {
            if (ang instanceof AgentVanzari) {
                agenti.add((AgentVanzari) ang);
            }
        }
        return agenti;
    }

    // Rapoarte pentru rezervari
    public List<Rezervare> getRaportRezervari() {
        return userService.getToateRezervarile();
    }

    public int getTotalRezervari() {
        return userService.getTotalRezervari();
    }

    public List<Rezervare> getRezervariClient(String emailClient) {
        return userService.getRezervariByClient(emailClient);
    }

    // Statistici generale
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

    public int getClientiActivi() {
        return userService.getClientiActivi();
    }

    public double getProcentClientiActivi() {
        int totalClienti = getTotalClienti();
        if (totalClienti == 0) {
            return 0.0;
        }
        return (double) getClientiActivi() / totalClienti * 100;
    }
    
    // Getter pentru UserService
    public UserService getUserService() {
        return userService;
    }

    // Calculeaza statistici salariale
    public double getTotalSalarii() {
        List<Angajat> angajati = getRaportAngajati();
        double total = 0.0;
        
        for (Angajat ang : angajati) {
            total += ang.calculSalariu();
        }
        
        return total;
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
        raport.append("   - Ghizi: ").append(getRaportGhizi().size()).append("\n");
        raport.append("   - Agenti vanzari: ").append(getRaportAgenti().size()).append("\n");
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
