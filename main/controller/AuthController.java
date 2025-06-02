package main.controller;

import main.service.AuthService;
import main.domain.Client;
import java.util.Scanner;

public class AuthController {
    private AuthService authService;

    public AuthController() {
        this.authService = new AuthService();
    }

    public void handleAuthMenu(Scanner sc) {
        boolean inAuthMenu = true;
        
        while (inAuthMenu) {
            displayAuthMenu();
            String optiune = sc.nextLine().trim();
            
            switch (optiune) {
                case "1":
                    handleLogin(sc);
                    break;
                case "2":
                    handleSignUp(sc);
                    break;
                case "3":
                    inAuthMenu = false;
                    break;
                default:
                    System.out.println("Optiune invalida. Incearca din nou.");
            }
        }
    }

    private void displayAuthMenu() {
        System.out.println("\n=== AUTENTIFICARE / CONT CLIENT ===");
        System.out.println("1. Login");
        System.out.println("2. Sign Up (Creare cont nou)");
        System.out.println("3. Back (Inapoi la meniul principal)");
        System.out.print("Alege optiunea: ");
    }

    private void handleLogin(Scanner sc) {
        System.out.println("\n=== LOGIN ===");
        
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        
        System.out.print("Parola: ");
        String parola = sc.nextLine().trim();
        
        // Apel catre SERVICE (fara UI logic)
        Client client = authService.authenticateClient(email, parola);
        
        if (client != null) {
            System.out.println("Login reusit! Bun venit, " + client.getNume() + " " + client.getPrenume() + "!");
            handleClientMenu(sc, client);
        } else {
            System.out.println("Email sau parola incorecte!");
        }
    }

    private void handleSignUp(Scanner sc) {
        System.out.println("\n=== CREARE CONT NOU ===");
        
        System.out.print("Nume: ");
        String nume = sc.nextLine().trim();
        
        System.out.print("Prenume: ");
        String prenume = sc.nextLine().trim();
        
        String email;
        do {
            System.out.print("Email: ");
            email = sc.nextLine().trim();
            
            if (authService.emailExists(email)) {
                System.out.println("Email-ul este deja folosit. Incearca cu alt email.");
            }
        } while (authService.emailExists(email));
        
        System.out.print("Telefon: ");
        String telefon = sc.nextLine().trim();
        
        System.out.print("Parola: ");
        String parola = sc.nextLine().trim();
        
        // Apel catre SERVICE pentru crearea contului
        boolean success = authService.createClient(nume, prenume, email, telefon, parola);
        
        if (success) {
            System.out.println("Cont creat cu succes! Poti sa te loghezi acum.");
        } else {
            System.out.println("Eroare la crearea contului. Incearca din nou.");
        }
    }

    private void handleClientMenu(Scanner sc, Client client) {
        boolean inClientMenu = true;
        
        while (inClientMenu) {
            displayClientMenu(client);
            String optiune = sc.nextLine().trim();
            
            switch (optiune) {
                case "1":
                    handleViewProfile(client);
                    break;
                case "2":
                    handleViewReservations(client);
                    break;
                case "3":
                    handleLogout(client);
                    inClientMenu = false;
                    break;
                default:
                    System.out.println("Optiune invalida. Incearca din nou.");
            }
        }
    }

    private void displayClientMenu(Client client) {
        System.out.println("\n=== MENIU CLIENT ===");
        System.out.println("Logat ca: " + client.getNume() + " " + client.getPrenume());
        System.out.println("1. Vezi profilul");
        System.out.println("2. Vezi rezervarile");
        System.out.println("3. Logout");
        System.out.print("Alege optiunea: ");
    }

    private void handleViewProfile(Client client) {
        System.out.println("\n=== PROFIL CLIENT ===");
        System.out.println(client.toString());
    }

    private void handleViewReservations(Client client) {
        System.out.println("\n=== REZERVARILE MELE ===");
        if (client.getRezervari().isEmpty()) {
            System.out.println("Nu ai nicio rezervare.");
        } else {
            client.getRezervari().forEach(System.out::println);
        }
    }

    private void handleLogout(Client client) {
        System.out.println("La revedere, " + client.getNume() + "!");
        authService.logout();
    }

    // Metode publice pentru alte controllere care ar putea avea nevoie
    public Client getCurrentClient() {
        return authService.getCurrentClient();
    }

    public AuthService getAuthService() {
        return authService;
    }
} 
