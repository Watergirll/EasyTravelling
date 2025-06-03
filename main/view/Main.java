package main.view;

import test.TestAngajat;
import main.service.RezervareService;
import main.service.AuditService;
import main.controller.LocatieController;
import main.controller.ServiciuController;
import main.controller.ReportController;
import main.controller.AdminController;
import main.persistence.ClientRepository;
import main.controller.UnifiedAuthController;
import main.domain.Client;
import main.domain.Angajat;
import main.domain.Director;
import main.domain.Ghid;
import main.domain.AgentVanzari;
import java.util.Scanner;
import java.util.List;
import main.controller.CatalogController;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static ClientRepository clientRepository = new ClientRepository();
    private static AuditService auditService = AuditService.getInstance();
    
    // Controllers - initializati lazy pentru a evita duplicarile
    private static UnifiedAuthController authController;
    private static LocatieController locatieController;
    private static ServiciuController serviciuController;
    private static ReportController reportController;
    private static AdminController adminController;
    private static CatalogController catalogController;
    
    // Views - initializati lazy
    private static AuthView authView;
    private static ClientView clientView;
    private static DirectorView directorView;
    private static AngajatView angajatView;

    public static void main(String[] args) {
        // Incarcam clientii din CSV
        try {
            List<Client> clienti = clientRepository.loadAll();
            System.out.println("Au fost incarcati " + clienti.size() + " clienti din fisier.");
        } catch (Exception e) {
            System.out.println("Eroare la incarcarea clientilor: " + e.getMessage());
        }

        // Initializez view-urile - ACEASTA VA CREA authController PENTRU PRIMA OARA
        initializeViews();
        
        authView.afiseazaWelcomeMessage();
        
        // Bucla principala - primul pas este INTOTDEAUNA autentificarea
        boolean appRunning = true;
        while (appRunning) {
            // Afiseaza meniul de autentificare
            authView.afiseazaMeniuPrincipal();
            String optiune = sc.nextLine().trim();

            switch (optiune) {
                case "1":
                    handleAuthentication();
                    break;
                case "2":
                    auditService.logAction(AuditService.Actions.TEST_ANGAJATI);
                    testeazaAngajati();
                    break;
                case "3":
                    auditService.logAction(AuditService.Actions.TEST_REZERVARE);
                    testeazaCreeareaUneiRezervari();
                    break;
                case "4":
                    authView.afiseazaGoodbyeMessage();
                    appRunning = false;
                    break;
                default:
                    authView.afiseazaOptiuneInvalida();
            }
        }
        
        sc.close();
    }
    
    private static void handleAuthentication() {
        Object currentUser = authView.handleAuthentication(sc);
        
        // Dupa autentificare, verifica tipul utilizatorului si redirectioneaza
        if (currentUser != null) {
            redirectToUserSpecificMenu(currentUser);
        }
    }
    
    private static void redirectToUserSpecificMenu(Object user) {
        if (user instanceof Director) {
            directorView.afiseazaAutentificareReusita();
            directorView.afiseazaMeniuDirector(sc);
        } else if (user instanceof Ghid) {
            angajatView.afiseazaAutentificareReusita();
            angajatView.afiseazaMeniuGhid((Ghid) user, sc);
        } else if (user instanceof AgentVanzari) {
            angajatView.afiseazaAutentificareReusita();
            angajatView.afiseazaMeniuAgent((AgentVanzari) user, sc);
        } else if (user instanceof Client) {
            clientView.afiseazaAutentificareReusita();
            clientView.afiseazaMeniuClient((Client) user, sc);
        } else {
            System.out.println("⚠ Tip de utilizator nerecunoscut!");
        }
    }

    private static void testeazaAngajati() {
        TestAngajat.ruleazaTestAngajati();
    }

    private static void testeazaCreeareaUneiRezervari() {
        RezervareService service = new RezervareService();
        service.simuleazaRezervare(sc);
    }

    // Initializare lazy pentru controller-e si view-uri
    private static void initializeControllers() {
        if (authController == null) {
            authController = new UnifiedAuthController(); // PRIMA INITIALIZARE
        }
        
        if (locatieController == null) {
            locatieController = new LocatieController();
            serviciuController = new ServiciuController();
            
            // Foloseste aceeasi instanta de UserService pentru toate serviciile
            reportController = new ReportController(authController.getUserService());
            adminController = new AdminController(authController.getUserService());
            catalogController = new CatalogController();
        }
    }
    
    private static void initializeViews() {
        initializeControllers(); // Asigura ca avem controller-ii
        
        authView = new AuthView(authController);
        clientView = new ClientView(authController, catalogController, locatieController, serviciuController);
        directorView = new DirectorView(authController, adminController, reportController, 
                                       catalogController, locatieController, serviciuController);
        angajatView = new AngajatView(authController, catalogController, locatieController, serviciuController);
    }
} 