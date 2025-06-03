package main.view;

import main.domain.Client;
import main.controller.UnifiedAuthController;
import main.controller.CatalogController;
import main.controller.LocatieController;
import main.controller.ServiciuController;
import main.service.AuditService;
import java.util.Scanner;

public class ClientView {
    private UnifiedAuthController authController;
    private CatalogController catalogController;
    private LocatieController locatieController;
    private ServiciuController serviciuController;
    private AuditService auditService;

    public ClientView(UnifiedAuthController authController, CatalogController catalogController,
                     LocatieController locatieController, ServiciuController serviciuController) {
        this.authController = authController;
        this.catalogController = catalogController;
        this.locatieController = locatieController;
        this.serviciuController = serviciuController;
        this.auditService = AuditService.getInstance();
    }

    public void afiseazaMeniuClient(Client client, Scanner sc) {
        boolean inClientMenu = true;
        
        while (inClientMenu) {
            afiseazaOptiuniClient(client);
            String optiune = sc.nextLine().trim();
            
            switch (optiune) {
                case "1":
                    catalogController.afiseazaMeniuCatalog();
                    break;
                case "2":
                    auditService.logAction(AuditService.Actions.CAUTARE_LOCATII);
                    locatieController.afiseazaLocatiiDisponibile(sc);
                    break;
                case "3":
                    auditService.logAction(AuditService.Actions.CAUTARE_SERVICII);
                    serviciuController.afiseazaServiciiDisponibile(sc);
                    break;
                case "4":
                    afiseazaRezervariClient(client);
                    break;
                case "5":
                    auditService.logAction(AuditService.Actions.VIEW_PROFILE);
                    afiseazaProfilClient(client);
                    break;
                case "6":
                    auditService.logAction(AuditService.Actions.LOGOUT);
                    authController.getUserService().logout();
                    afiseazaLogoutMessage();
                    inClientMenu = false;
                    break;
                default:
                    afiseazaOptiuneInvalida();
            }
        }
    }

    private void afiseazaOptiuniClient(Client client) {
        System.out.println("\n=== MENIU CLIENT ===");
        System.out.println("Utilizator: " + client.getNume() + " " + client.getPrenume());
        System.out.println("1. 🏖️ Catalog oferte & Rezervari");
        System.out.println("2. 🌍 Cautare locatii turistice");
        System.out.println("3. ⚙️ Cautare servicii disponibile");
        System.out.println("4. 📋 Rezervarile mele");
        System.out.println("5. 👤 Profilul meu");
        System.out.println("6. 🔓 Logout");
        System.out.print("Alege optiunea: ");
    }

    private void afiseazaRezervariClient(Client client) {
        System.out.println("\n=== REZERVARILE MELE ===");
        if (client.getRezervari() == null || client.getRezervari().isEmpty()) {
            System.out.println("Nu ai nicio rezervare.");
        } else {
            for (int i = 0; i < client.getRezervari().size(); i++) {
                System.out.println((i + 1) + ". " + client.getRezervari().get(i));
            }
        }
    }

    private void afiseazaProfilClient(Client client) {
        System.out.println("\n=== PROFILUL MEU ===");
        System.out.println("Nume: " + client.getNume() + " " + client.getPrenume());
        System.out.println("Email: " + client.getEmail());
        System.out.println("Telefon: " + client.getTelefon());
        System.out.println("Numar rezervari: " + (client.getRezervari() != null ? client.getRezervari().size() : 0));
    }

    private void afiseazaLogoutMessage() {
        System.out.println("Logout efectuat cu succes!");
    }

    private void afiseazaOptiuneInvalida() {
        System.out.println("Optiune invalida!");
    }

    public void afiseazaAutentificareReusita() {
        System.out.println("\n✅ Autentificare reusita!");
        System.out.println("Tip utilizator: " + authController.getCurrentUserType());
        System.out.println("Nume: " + authController.getCurrentUserName());
    }
} 