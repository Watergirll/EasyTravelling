package main.controller;

import main.service.UserService;
import main.domain.Client;
import main.domain.Angajat;
import main.domain.Ghid;
import main.domain.AgentVanzari;
import java.util.Scanner;

public class UnifiedAuthController {
    private UserService userService;

    public UnifiedAuthController() {
        this.userService = new UserService();
    }

    public void handleAuthMenu(Scanner sc) {
        boolean inAuthMenu = true;
        
        while (inAuthMenu) {
            displayMainAuthMenu();
            String optiune = sc.nextLine().trim();
            
            switch (optiune) {
                case "1":
                    handleLogin(sc);
                    break;
                case "2":
                    handleClientSignUp(sc);
                    break;
                case "3":
                    handleEmployeeSignUp(sc);
                    break;
                case "4":
                    inAuthMenu = false;
                    break;
                default:
                    System.out.println("Optiune invalida. Incearca din nou.");
            }
        }
    }

    private void displayMainAuthMenu() {
        System.out.println("\n=== AUTENTIFICARE EASYTRAVELLING ===");
        System.out.println("1. Login (Client / Angajat)");
        System.out.println("2. Sign Up Client");
        System.out.println("3. Sign Up Angajat");
        System.out.println("4. Back (Inapoi la meniul principal)");
        System.out.print("Alege optiunea: ");
    }

    private void handleLogin(Scanner sc) {
        System.out.println("\n=== LOGIN UNIVERSAL ===");
        
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        
        System.out.print("Parola: ");
        String parola = sc.nextLine().trim();
        
        Object user = userService.authenticateUser(email, parola);
        
        if (user != null) {
            String userType = userService.getUserType(email);
            
            if (user instanceof Client) {
                Client client = (Client) user;
                System.out.println("Login CLIENT reusit! Bun venit, " + client.getNume() + " " + client.getPrenume() + "!");
                handleClientMenu(sc, client);
            } else if (user instanceof Angajat) {
                Angajat angajat = (Angajat) user;
                System.out.println("Login ANGAJAT reusit! Bun venit, " + angajat.getNume() + " " + angajat.getPrenume() + "!");
                handleEmployeeMenu(sc, angajat);
            }
        } else {
            System.out.println("Email sau parola incorecte!");
        }
    }

    private void handleClientSignUp(Scanner sc) {
        System.out.println("\n=== CREARE CONT CLIENT ===");
        
        System.out.print("Nume: ");
        String nume = sc.nextLine().trim();
        
        System.out.print("Prenume: ");
        String prenume = sc.nextLine().trim();
        
        String email;
        do {
            System.out.print("Email: ");
            email = sc.nextLine().trim();
            
            if (userService.emailExists(email)) {
                System.out.println("Email-ul este deja folosit. Incearca cu alt email.");
            }
        } while (userService.emailExists(email));
        
        System.out.print("Telefon: ");
        String telefon = sc.nextLine().trim();
        
        System.out.print("Parola: ");
        String parola = sc.nextLine().trim();
        
        boolean success = userService.createClient(nume, prenume, email, telefon, parola);
        
        if (success) {
            System.out.println("Cont CLIENT creat cu succes! Poti sa te loghezi acum.");
        } else {
            System.out.println("Eroare la crearea contului. Incearca din nou.");
        }
    }

    private void handleEmployeeSignUp(Scanner sc) {
        System.out.println("\n=== CREARE CONT ANGAJAT ===");
        
        System.out.print("Nume: ");
        String nume = sc.nextLine().trim();
        
        System.out.print("Prenume: ");
        String prenume = sc.nextLine().trim();
        
        String email;
        do {
            System.out.print("Email: ");
            email = sc.nextLine().trim();
            
            if (userService.emailExists(email)) {
                System.out.println("Email-ul este deja folosit. Incearca cu alt email.");
            }
        } while (userService.emailExists(email));
        
        System.out.println("Tip angajat:");
        System.out.println("1. Ghid turistic");
        System.out.println("2. Agent vanzari");
        System.out.print("Alege (1 sau 2): ");
        String tipChoice = sc.nextLine().trim();
        
        String tipAngajat;
        String job;
        int salariuBaza;
        Object extraParam = null;
        
        switch (tipChoice) {
            case "1":
                tipAngajat = "GHID";
                job = "Ghid turistic";
                salariuBaza = 3000;
                System.out.print("ID Locatie (default 1): ");
                String locatieStr = sc.nextLine().trim();
                extraParam = locatieStr.isEmpty() ? 1 : Integer.parseInt(locatieStr);
                break;
            case "2":
                tipAngajat = "AGENT";
                job = "Agent vanzari";
                salariuBaza = 2500;
                System.out.print("Comision (default 0.05): ");
                String comisionStr = sc.nextLine().trim();
                extraParam = comisionStr.isEmpty() ? 0.05 : Double.parseDouble(comisionStr);
                break;
            default:
                System.out.println("Optiune invalida!");
                return;
        }
        
        System.out.print("Parola: ");
        String parola = sc.nextLine().trim();
        
        boolean success = userService.createEmployee(nume, prenume, email, tipAngajat, job, salariuBaza, parola, extraParam);
        
        if (success) {
            System.out.println("Cont ANGAJAT creat cu succes! Poti sa te loghezi acum.");
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
                    handleViewClientProfile(client);
                    break;
                case "2":
                    handleViewReservations(client);
                    break;
                case "3":
                    handleClientLogout(client);
                    inClientMenu = false;
                    break;
                default:
                    System.out.println("Optiune invalida. Incearca din nou.");
            }
        }
    }

    private void handleEmployeeMenu(Scanner sc, Angajat angajat) {
        boolean inEmployeeMenu = true;
        
        while (inEmployeeMenu) {
            displayEmployeeMenu(angajat);
            String optiune = sc.nextLine().trim();
            
            switch (optiune) {
                case "1":
                    handleViewEmployeeProfile(angajat);
                    break;
                case "2":
                    handleViewSalary(angajat);
                    break;
                case "3":
                    if (angajat instanceof Ghid) {
                        handleGhidSpecificOptions(sc, (Ghid) angajat);
                    } else if (angajat instanceof AgentVanzari) {
                        handleAgentSpecificOptions(sc, (AgentVanzari) angajat);
                    }
                    break;
                case "4":
                    handleEmployeeLogout(angajat);
                    inEmployeeMenu = false;
                    break;
                default:
                    System.out.println("Optiune invalida. Incearca din nou.");
            }
        }
    }

    // ============ DISPLAY METHODS ============

    private void displayClientMenu(Client client) {
        System.out.println("\n=== MENIU CLIENT ===");
        System.out.println("Logat ca: " + client.getNume() + " " + client.getPrenume());
        System.out.println("1. Vezi profilul");
        System.out.println("2. Vezi rezervarile");
        System.out.println("3. Logout");
        System.out.print("Alege optiunea: ");
    }

    private void displayEmployeeMenu(Angajat angajat) {
        System.out.println("\n=== MENIU ANGAJAT ===");
        System.out.println("Logat ca: " + angajat.getNume() + " " + angajat.getPrenume() + " (" + angajat.getJobTitle() + ")");
        System.out.println("1. Vezi profilul");
        System.out.println("2. Vezi salariul");
        
        if (angajat instanceof Ghid) {
            System.out.println("3. Optiuni specifice Ghid");
        } else if (angajat instanceof AgentVanzari) {
            System.out.println("3. Optiuni specifice Agent");
        }
        
        System.out.println("4. Logout");
        System.out.print("Alege optiunea: ");
    }

    // ============ CLIENT SPECIFIC METHODS ============

    private void handleViewClientProfile(Client client) {
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

    private void handleClientLogout(Client client) {
        System.out.println("La revedere, " + client.getNume() + "!");
        userService.logout();
    }

    // ============ EMPLOYEE SPECIFIC METHODS ============

    private void handleViewEmployeeProfile(Angajat angajat) {
        System.out.println("\n=== PROFIL ANGAJAT ===");
        System.out.println(angajat.toString());
        System.out.println("Salariu baza: " + angajat.getSalariuBaza() + " RON");
    }

    private void handleViewSalary(Angajat angajat) {
        System.out.println("\n=== DETALII SALARIU ===");
        System.out.println("Nume: " + angajat.getNume() + " " + angajat.getPrenume());
        System.out.println("Job: " + angajat.getJobTitle());
        System.out.println("Salariu baza: " + angajat.getSalariuBaza() + " RON");
        System.out.println("Salariu total calculat: " + String.format("%.2f", angajat.calculSalariu()) + " RON");
        
        if (angajat instanceof Ghid) {
            Ghid ghid = (Ghid) angajat;
            System.out.println("Bonus limbi (+10% per limba): " + ghid.getLimbiVorbite().size() + " limbi");
            System.out.println("Limbi vorbite: " + ghid.getLimbiVorbite());
        } else if (angajat instanceof AgentVanzari) {
            AgentVanzari agent = (AgentVanzari) angajat;
            System.out.println("Comision: " + String.format("%.2f", agent.getComisionPercentage()) + "%");
            System.out.println("Bonus comision: " + String.format("%.2f", agent.calculComision()) + " RON");
        }
    }

    private void handleGhidSpecificOptions(Scanner sc, Ghid ghid) {
        System.out.println("\n=== OPTIUNI GHID ===");
        System.out.println("1. Adauga limba noua");
        System.out.println("2. Vezi limbile vorbite");
        System.out.print("Alege optiunea: ");
        
        String optiune = sc.nextLine().trim();
        
        switch (optiune) {
            case "1":
                System.out.print("Introdu limba noua: ");
                String limba = sc.nextLine().trim();
                ghid.adaugaLimba(limba);
                System.out.println("Limba '" + limba + "' a fost adaugata!");
                break;
            case "2":
                System.out.println("Limbi vorbite: " + ghid.getLimbiVorbite());
                break;
            default:
                System.out.println("Optiune invalida!");
        }
    }

    private void handleAgentSpecificOptions(Scanner sc, AgentVanzari agent) {
        System.out.println("\n=== OPTIUNI AGENT VANZARI ===");
        System.out.println("Comision actual: " + String.format("%.2f", agent.getComisionPercentage()) + "%");
        System.out.println("Bonus lunar: " + String.format("%.2f", agent.calculComision()) + " RON");
        System.out.println("Salariu total: " + String.format("%.2f", agent.calculSalariu()) + " RON");
    }

    private void handleEmployeeLogout(Angajat angajat) {
        System.out.println("La revedere, " + angajat.getNume() + "!");
        userService.logout();
    }

    // ============ PUBLIC METHODS ============

    public Object getCurrentUser() {
        return userService.getCurrentUser();
    }

    public UserService getUserService() {
        return userService;
    }
} 
