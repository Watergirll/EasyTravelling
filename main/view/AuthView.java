package main.view;

import main.controller.UnifiedAuthController;
import main.service.AuditService;
import java.util.Scanner;

public class AuthView {
    private UnifiedAuthController authController;
    private AuditService auditService;

    public AuthView(UnifiedAuthController authController) {
        this.authController = authController;
        this.auditService = AuditService.getInstance();
    }

    public void afiseazaMeniuPrincipal() {
        System.out.println("\n=== MENIU PRINCIPAL EASYTRAVELLING ===");
        System.out.println("1. 🔐 Autentificare / Creare cont");
        System.out.println("2. 🧪 Demo: Test angajati");
        System.out.println("3. 🧪 Demo: Test rezervare");
        System.out.println("4. ❌ Iesire");
        System.out.print("Alege optiunea: ");
    }

    public Object handleAuthentication(Scanner sc) {
        auditService.logAction(AuditService.Actions.AUTENTIFICARE);
        authController.handleAuthMenu(sc);
        
        // Returneaza utilizatorul curent dupa autentificare
        return authController.getCurrentUser();
    }

    public void afiseazaWelcomeMessage() {
        System.out.println("\n=== BINE ATI VENIT LA EASYTRAVELLING ===");
        System.out.println("Pentru a continua, trebuie sa va autentificati.");
    }

    public void afiseazaGoodbyeMessage() {
        auditService.logAction(AuditService.Actions.IESIRE);
        System.out.println("La revedere!");
    }

    public void afiseazaOptiuneInvalida() {
        System.out.println("Optiune invalida!");
    }
} 