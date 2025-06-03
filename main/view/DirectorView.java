package main.view;

import main.domain.Director;
import main.controller.*;
import main.service.AuditService;
import main.service.LocatieService;
import main.domain.Locatie;
import java.util.Scanner;

public class DirectorView {
    private UnifiedAuthController authController;
    private AdminController adminController;
    private ReportController reportController;
    private CatalogController catalogController;
    private LocatieController locatieController;
    private ServiciuController serviciuController;
    private AuditService auditService;
    private LocatieService locatieService;

    public DirectorView(UnifiedAuthController authController, AdminController adminController,
                       ReportController reportController, CatalogController catalogController,
                       LocatieController locatieController, ServiciuController serviciuController) {
        this.authController = authController;
        this.adminController = adminController;
        this.reportController = reportController;
        this.catalogController = catalogController;
        this.locatieController = locatieController;
        this.serviciuController = serviciuController;
        this.auditService = AuditService.getInstance();
        this.locatieService = new LocatieService();
    }

    public void afiseazaMeniuDirector(Scanner sc) {
        boolean inDirectorMenu = true;
        
        while (inDirectorMenu) {
            afiseazaOptiuniDirector();
            String optiune = sc.nextLine().trim();
            
            switch (optiune) {
                case "1":
                    auditService.logAction(AuditService.Actions.MANAGEMENT_CLIENTI);
                    adminController.managementClienti(sc);
                    break;
                case "2":
                    auditService.logAction(AuditService.Actions.MANAGEMENT_ANGAJATI);
                    adminController.managementAngajati(sc);
                    break;
                case "3":
                    auditService.logAction(AuditService.Actions.MANAGEMENT_REZERVARI);
                    adminController.afiseazaManagementAdmin(sc);
                    break;
                case "4":
                    auditService.logAction(AuditService.Actions.RAPOARTE_STATISTICI);
                    reportController.genereazaRapoarte(sc);
                    break;
                case "5":
                    catalogController.afiseazaMeniuCatalog();
                    break;
                case "6":
                    auditService.logAction(AuditService.Actions.CAUTARE_LOCATII);
                    locatieController.afiseazaLocatiiDisponibile(sc);
                    break;
                case "7":
                    auditService.logAction(AuditService.Actions.CAUTARE_SERVICII);
                    serviciuController.afiseazaServiciiDisponibile(sc);
                    break;
                case "8":
                    auditService.logAction(AuditService.Actions.LOGOUT);
                    authController.getUserService().logout();
                    afiseazaLogoutMessage();
                    inDirectorMenu = false;
                    break;
                default:
                    afiseazaOptiuneInvalida();
            }
        }
    }

    private void afiseazaOptiuniDirector() {
        System.out.println("\n=== MENIU DIRECTOR (ADMIN) ===");
        System.out.println("Utilizator: " + authController.getCurrentUserName() + " (" + authController.getCurrentUserType() + ")");
        System.out.println("1. 👥 Management clienti");
        System.out.println("2. 🏢 Management angajati");
        System.out.println("3. 📋 Management rezervari");
        System.out.println("4. 📊 Rapoarte si statistici");
        System.out.println("5. 🏖️ Catalog oferte");
        System.out.println("6. 🌍 Cautare locatii turistice");
        System.out.println("7. ⚙️ Cautare servicii disponibile");
        System.out.println("8. 🔓 Logout");
        System.out.print("Alege optiunea: ");
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

    /**
     * Helper method pentru obtinerea numelui locatiei din ID (poate fi folosita pentru rapoarte)
     */
    public String getNumeLocatie(int idLocatie) {
        Locatie locatie = locatieService.getLocatieById(idLocatie);
        if (locatie != null) {
            return locatie.getNume() + ", " + locatie.getTara().getNume() + " (ID: " + idLocatie + ")";
        } else {
            return "Locatie necunoscuta (ID: " + idLocatie + ")";
        }
    }
} 