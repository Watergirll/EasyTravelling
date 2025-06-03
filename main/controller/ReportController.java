package main.controller;

import main.service.ReportService;
import main.service.AuditService;
import main.domain.*;
import java.util.List;
import java.util.Scanner;

public class ReportController {
    private ReportService reportService;
    private AuditService auditService;

    public ReportController() {
        this.reportService = new ReportService();
        this.auditService = AuditService.getInstance();
    }
    
    /**
     * Constructor cu UserService partajat
     */
    public ReportController(main.service.UserService sharedUserService) {
        this.reportService = new ReportService(sharedUserService);
        this.auditService = AuditService.getInstance();
    }

    public void genereazaRapoarte(Scanner sc) {
        boolean inReportMenu = true;

        while (inReportMenu) {
            System.out.println("\n=== RAPOARTE SI STATISTICI ===");
            System.out.println("1. Raport clienti");
            System.out.println("2. Raport angajati");
            System.out.println("3. Raport rezervari");
            System.out.println("4. Raport financiar");
            System.out.println("5. Backup all data");
            System.out.println("6. Back");
            System.out.print("Alege optiunea: ");

            String optiune = sc.nextLine().trim();

            switch (optiune) {
                case "1":
                    auditService.logAction(AuditService.Actions.GENERATE_REPORT);
                    afiseazaRaportClienti();
                    break;
                case "2":
                    auditService.logAction(AuditService.Actions.GENERATE_REPORT);
                    afiseazaRaportAngajati();
                    break;
                case "3":
                    auditService.logAction(AuditService.Actions.GENERATE_REPORT);
                    afiseazaRaportRezervari();
                    break;
                case "4":
                    auditService.logAction(AuditService.Actions.GENERATE_REPORT);
                    afiseazaRaportFinanciar();
                    break;
                case "5":
                    auditService.logAction(AuditService.Actions.BACKUP_DATA);
                    efectueazaBackup();
                    break;
                case "6":
                    inReportMenu = false;
                    break;
                default:
                    System.out.println("Optiune invalida!");
            }
        }
    }

    private void afiseazaRaportClienti() {
        System.out.println("\n=== RAPORT CLIENTI ===");
        System.out.println("Total clienti: " + reportService.getTotalClienti());
        System.out.println("Clienti cu rezervari: " + reportService.getClientiCuRezervari().size());
        System.out.println("Total rezervari: " + reportService.getTotalRezervari());
        System.out.println("Rata de conversie: " + String.format("%.2f", reportService.getRataConversie()) + "%");
        
        System.out.println("\nDetalii clienti:");
        reportService.getRaportClienti().forEach(client -> {
            int nrRezervari = client.getRezervari() != null ? client.getRezervari().size() : 0;
            System.out.println("• " + client.getNume() + " " + client.getPrenume() + 
                             " (" + client.getEmail() + ") - " + nrRezervari + " rezervari");
        });
    }

    private void afiseazaRaportAngajati() {
        System.out.println("\n=== RAPORT ANGAJATI ===");
        System.out.println("Total angajati: " + reportService.getTotalAngajati());
        System.out.println("Ghizi: " + reportService.getRaportGhizi().size());
        System.out.println("Agenti vanzari: " + reportService.getRaportAgenti().size());
        System.out.println("Salariu mediu: " + String.format("%.2f", reportService.getSalariuMediuAngajati()) + " RON");
        
        System.out.println("\nDetalii angajati:");
        reportService.getRaportAngajati().forEach(angajat -> {
            String tip = angajat instanceof Ghid ? "Ghid" : "Agent Vanzari";
            System.out.println("• " + angajat.getNume() + " " + angajat.getPrenume() + 
                             " (" + tip + ") - " + String.format("%.2f", angajat.calculSalariu()) + " RON");
        });
    }

    private void afiseazaRaportRezervari() {
        System.out.println("\n=== RAPORT REZERVARI ===");
        System.out.println("Total rezervari: " + reportService.getTotalRezervari());
        System.out.println("Clienti cu rezervari: " + reportService.getClientiCuRezervari().size() + " din " + reportService.getTotalClienti());
        System.out.println("Rata de activitate: " + String.format("%.2f", reportService.getRataConversie()) + "%");
    }

    private void afiseazaRaportFinanciar() {
        System.out.println("\n=== RAPORT FINANCIAR ===");
        System.out.println("Total salarii angajati: " + String.format("%.2f", reportService.getTotalSalarii()) + " RON");
        System.out.println("Salariu mediu: " + String.format("%.2f", reportService.getSalariuMediuAngajati()) + " RON");
        System.out.println("Cost lunar estimat: " + String.format("%.2f", reportService.getTotalSalarii()) + " RON");
    }

    private void efectueazaBackup() {
        System.out.println("\n=== BACKUP DATE ===");
        boolean succes = reportService.efectueazaBackup();
        
        if (succes) {
            System.out.println("✅ Backup efectuat cu succes!");
            System.out.println("Raport complet:");
            System.out.println(reportService.genereazaRaportComplet());
        } else {
            System.out.println("❌ Eroare la efectuarea backup-ului!");
        }
    }
} 
