package main.view;

import test.TestAngajat;
import main.service.RezervareService;
import main.controller.LocatieController;
import main.controller.ServiciuController;
import main.controller.ReportController;
import main.controller.AdminController;
import main.persistence.ClientRepository;
import main.controller.UnifiedAuthController;
import main.domain.Client;
import java.util.Scanner;
import java.util.List;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static ClientRepository clientRepository = new ClientRepository();
    private static UnifiedAuthController authController = new UnifiedAuthController();
    
    // Adaugam controller-ele noi
    private static LocatieController locatieController = new LocatieController();
    private static ServiciuController serviciuController = new ServiciuController();
    private static ReportController reportController = new ReportController();
    private static AdminController adminController = new AdminController();

    public static void main(String[] args) {
        // Incarcam clientii din CSV
        try {
            List<Client> clienti = clientRepository.loadAll();
            System.out.println("Au fost incarcati " + clienti.size() + " clienti din fisier.");
        } catch (Exception e) {
            System.out.println("Eroare la incarcarea clientilor: " + e.getMessage());
        }

        // Meniul principal cu 10 actiuni
        boolean running = true;
        while (running) {
            afiseazaMeniuPrincipal();
            String optiune = sc.nextLine().trim();

            switch (optiune) {
                case "1":
                    testeazaAngajati();
                    break;
                case "2":
                    testeazaCreeareaUneiRezervari();
                    break;
                case "3":
                    authController.handleAuthMenu(sc);
                    break;
                case "4":
                    locatieController.afiseazaLocatiiDisponibile(sc);
                    break;
                case "5":
                    serviciuController.afiseazaServiciiDisponibile(sc);
                    break;
                case "6":
                    adminController.afiseazaManagementAdmin(sc);
                    break;
                case "7":
                    reportController.genereazaRapoarte(sc);
                    break;
                case "8":
                    adminController.managementClienti(sc);
                    break;
                case "9":
                    adminController.managementAngajati(sc);
                    break;
                case "10":
                    System.out.println("La revedere!");
                    running = false;
                    break;
                default:
                    System.out.println("Optiune invalida!");
            }
        }
        
        sc.close();
    }

    private static void afiseazaMeniuPrincipal() {
        System.out.println("\n=== MENIU EASYTRAVELLING ===");
        System.out.println("1. Testeaza angajati");
        System.out.println("2. Testeaza creearea unei rezervari");
        System.out.println("3. Autentificare / Cont client");
        System.out.println("4. Cautare locatii turistice");
        System.out.println("5. Cautare servicii disponibile");
        System.out.println("6. Management rezervari (Admin)");
        System.out.println("7. Rapoarte si statistici");
        System.out.println("8. Management clienti (Admin)");
        System.out.println("9. Management angajati (Admin)");
        System.out.println("10. Iesire");
        System.out.print("Alege optiunea: ");
    }

    private static void testeazaAngajati() {
        TestAngajat.ruleazaTestAngajati();
    }

    private static void testeazaCreeareaUneiRezervari() {
        RezervareService service = new RezervareService();
        service.simuleazaRezervare(sc);
    }
} 