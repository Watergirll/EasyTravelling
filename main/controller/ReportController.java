package main.controller;

import main.service.ReportService;
import main.domain.*;
import java.util.List;
import java.util.Scanner;

public class ReportController {
    private ReportService reportService;

    public ReportController() {
        this.reportService = new ReportService();
    }

    public void genereazaRapoarte(Scanner sc) {
        boolean inReportMenu = true;

        while (inReportMenu) {
            System.out.println("\n=== RAPOARTE sI STATISTICI ===");
            System.out.println("1. Raport clienti");
            System.out.println("2. Raport angajati");
            System.out.println("3. Statistici generale");
            System.out.println("4. Raport salarii");
            System.out.println("5. Raport performanta angajati");
            System.out.println("6. Backup date utilizatori");
            System.out.println("7. Back");
            System.out.print("Alege optiunea: ");

            String optiune = sc.nextLine().trim();

            switch (optiune) {
                case "1":
                    genereazaRaportClienti();
                    break;
                case "2":
                    genereazaRaportAngajati();
                    break;
                case "3":
                    genereazaStatisticiGenerale();
                    break;
                case "4":
                    genereazaRaportSalarii();
                    break;
                case "5":
                    genereazaRaportPerformanta();
                    break;
                case "6":
                    efectueazaBackup();
                    break;
                case "7":
                    inReportMenu = false;
                    break;
                default:
                    System.out.println("Optiune invalida!");
            }
        }
    }

    private void genereazaRaportClienti() {
        System.out.println("\n=== RAPORT CLIENtI ===");
        
        List<Client> clienti = reportService.getRaportClienti();
        
        if (clienti.isEmpty()) {
            System.out.println("Nu exista clienti inregistrati.");
            return;
        }

        System.out.println("Total clienti: " + reportService.getTotalClienti());
        System.out.println("\nDetalii clienti:");
        System.out.println("?????????????????????????????????????????????");
        
        for (Client client : clienti) {
            int nrRezervari = client.getRezervari() != null ? client.getRezervari().size() : 0;
            System.out.println("? " + client.getNume() + " " + client.getPrenume() + 
                             " | " + client.getEmail() + 
                             " | " + client.getTelefon() + 
                             " | Rezervari: " + nrRezervari);
        }
        
        System.out.println("?????????????????????????????????????????????");
        System.out.println("Clienti cu rezervari: " + reportService.getClientiCuRezervari().size());
        System.out.println("Total rezervari: " + reportService.getTotalRezervari());
        System.out.println("Rata de conversie: " + String.format("%.2f", reportService.getRataConversie()) + "%");
    }

    private void genereazaRaportAngajati() {
        System.out.println("\n=== RAPORT ANGAJAtI ===");
        
        List<Angajat> angajati = reportService.getRaportAngajati();
        
        if (angajati.isEmpty()) {
            System.out.println("Nu exista angajati inregistrati.");
            return;
        }

        System.out.println("Total angajati: " + reportService.getTotalAngajati());
        System.out.println("\nDetalii angajati:");
        System.out.println("?????????????????????????????????????????????");
        
        for (Angajat angajat : angajati) {
            String tip = "";
            if (angajat instanceof Ghid) {
                tip = "Ghid";
            } else if (angajat instanceof AgentVanzari) {
                tip = "Agent Vanzari";
            }
            
            System.out.println("? " + angajat.getNume() + " " + angajat.getPrenume() + 
                             " | " + angajat.getEmail() + 
                             " | " + tip + 
                             " | Salariu: " + String.format("%.2f", angajat.calculSalariu()) + " RON");
        }

        System.out.println("?????????????????????????????????????????????");
        System.out.println("Ghizi: " + reportService.getGhizi().size());
        System.out.println("Agenti vanzari: " + reportService.getAgentiVanzari().size());
    }

    private void genereazaStatisticiGenerale() {
        System.out.println("\n=== STATISTICI GENERALE ===");
        
        System.out.println("📊 STATISTICI PLATFORM");
        int totalClienti = reportService.getTotalClienti();
        int totalAngajati = reportService.getTotalAngajati();
        System.out.println("└─ Clienti: " + totalClienti);
        System.out.println("└─ Angajati: " + totalAngajati);

        if (totalAngajati > 0) {
            System.out.println("   └─ Ghizi: " + reportService.getGhizi().size());
            System.out.println("   └─ Agenti: " + reportService.getAgentiVanzari().size());
        }

        System.out.println("Total rezervari: " + reportService.getTotalRezervari());
        System.out.println("Rata activitate clienti: " + String.format("%.1f%%", reportService.getRataConversie()));
    }

    private void genereazaRaportSalarii() {
        System.out.println("\n=== RAPORT SALARII ===");
        
        List<Angajat> angajati = reportService.getRaportAngajati();
        
        if (angajati.isEmpty()) {
            System.out.println("Nu exista angajati inregistrati.");
            return;
        }

        System.out.println("DETALII SALARII:");
        System.out.println("?????????????????????????????????????????????");
        
        double salariuMinim = Double.MAX_VALUE;
        double salariuMaxim = Double.MIN_VALUE;
        
        for (Angajat angajat : angajati) {
            double salariu = angajat.calculSalariu();
            
            if (salariu < salariuMinim) salariuMinim = salariu;
            if (salariu > salariuMaxim) salariuMaxim = salariu;
            
            String tip = angajat instanceof Ghid ? "Ghid" : "Agent";
            System.out.println("? " + angajat.getNume() + " " + angajat.getPrenume() + 
                             " (" + tip + ")");
            System.out.println("  Salariu: " + String.format("%.2f", salariu) + " RON");
            System.out.println();
        }
        
        System.out.println("?????????????????????????????????????????????");
        System.out.println("STATISTICI SALARII:");
        System.out.println("Total cost salarii: " + String.format("%.2f", reportService.getTotalSalarii()) + " RON");
        System.out.println("Salariul minim: " + String.format("%.2f", salariuMinim) + " RON");
        System.out.println("Salariul maxim: " + String.format("%.2f", salariuMaxim) + " RON");
        System.out.println("Salariul mediu: " + String.format("%.2f", reportService.getSalariuMediuAngajati()) + " RON");
    }

    private void genereazaRaportPerformanta() {
        System.out.println("\n=== RAPORT PERFORMANta ANGAJAtI ===");
        
        List<Angajat> angajati = reportService.getRaportAngajati();
        
        if (angajati.isEmpty()) {
            System.out.println("Nu exista angajati inregistrati.");
            return;
        }

        System.out.println("ANALIZA PERFORMANtELOR:");
        System.out.println("?????????????????????????????????????????????");
        
        for (Angajat angajat : angajati) {
            System.out.println("? " + angajat.getNume() + " " + angajat.getPrenume());
            
            if (angajat instanceof Ghid) {
                Ghid ghid = (Ghid) angajat;
                System.out.println("  Tip: Ghid turistic");
                System.out.println("  Locatie: " + ghid.getIdLocatie());
                System.out.println("  Limbi vorbite: " + ghid.getLimbiVorbite().size());
                
                // Calculam eficienta
                double eficienta = ghid.getLimbiVorbite().size() * 20; // 20 puncte per limba
                System.out.println("  Scor eficienta: " + eficienta + " puncte");
                
            } else if (angajat instanceof AgentVanzari) {
                AgentVanzari agent = (AgentVanzari) angajat;
                System.out.println("  Tip: Agent vanzari");
                System.out.println("  Comision: " + String.format("%.2f", agent.getComisionPercentage()) + "%");
                System.out.println("  Total vanzari: " + String.format("%.2f", agent.getTotalVanzari()) + " RON");
                System.out.println("  Comision castigat: " + String.format("%.2f", agent.calculComision()) + " RON");
                
                // Calculam eficienta pe baza vanzarilor
                double eficienta = agent.getTotalVanzari() / 100; // 1 punct per 100 RON vandut
                System.out.println("  Scor eficienta: " + String.format("%.1f", eficienta) + " puncte");
            }
            
            System.out.println("  Salariu total: " + String.format("%.2f", angajat.calculSalariu()) + " RON");
            System.out.println();
        }
    }

    private void efectueazaBackup() {
        System.out.println("\n=== BACKUP DATE UTILIZATORI ===");
        
        if (reportService.efectueazaBackup()) {
            System.out.println("✅ Backup realizat cu succes!");
            System.out.println("Date verificate si sincronizate.");
            
            // Afisam statistici backup
            System.out.println("\n=== STATISTICI BACKUP ===");
            System.out.println("• Clienti verificati: " + reportService.getTotalClienti());
            System.out.println("• Angajati verificati: " + reportService.getTotalAngajati());
            System.out.println("• Total inregistrari: " + (reportService.getTotalClienti() + reportService.getTotalAngajati()));
            
            // Afisez si raportul complet
            System.out.println("\n" + reportService.genereazaRaportComplet());
        } else {
            System.out.println("❌ Eroare la realizarea backup-ului!");
        }
    }
} 
