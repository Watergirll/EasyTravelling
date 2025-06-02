package main.controller;

import main.service.AdminService;
import main.domain.*;
import main.domain.enums.JobType;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class AdminController {
    private AdminService adminService;

    public AdminController() {
        this.adminService = new AdminService();
    }

    // =============== ENTRY POINT PENTRU ADMIN MANAGEMENT ===============
    
    public void afiseazaManagementAdmin(Scanner sc) {
        boolean inAdminMenu = true;

        while (inAdminMenu) {
            System.out.println("\n=== MANAGEMENT ADMIN ===");
            System.out.println("1. Management rezervari");
            System.out.println("2. Management clienti");
            System.out.println("3. Management angajati");
            System.out.println("4. Statistici quick");
            System.out.println("5. Back");
            System.out.print("Alege optiunea: ");

            String optiune = sc.nextLine().trim();

            switch (optiune) {
                case "1":
                    managementRezervari(sc);
                    break;
                case "2":
                    managementClienti(sc);
                    break;
                case "3":
                    managementAngajati(sc);
                    break;
                case "4":
                    afiseazaStatisticiQuick();
                    break;
                case "5":
                    inAdminMenu = false;
                    break;
                default:
                    System.out.println("Optiune invalida!");
            }
        }
    }

    // =============== MANAGEMENT REZERVaRI ===============
    
    private void managementRezervari(Scanner sc) {
        boolean inRezervariMenu = true;

        while (inRezervariMenu) {
            System.out.println("\n=== MANAGEMENT REZERVaRI (ADMIN) ===");
            System.out.println("1. Vezi toate rezervarile");
            System.out.println("2. Cauta rezervarile unui client");
            System.out.println("3. Anuleaza rezervare");
            System.out.println("4. Statistici rezervari");
            System.out.println("5. Back");
            System.out.print("Alege optiunea: ");

            String optiune = sc.nextLine().trim();

            switch (optiune) {
                case "1":
                    afiseazaToateRezervarile();
                    break;
                case "2":
                    cautaRezervariClient(sc);
                    break;
                case "3":
                    anuleazaRezervare(sc);
                    break;
                case "4":
                    afiseazaStatisticiRezervari();
                    break;
                case "5":
                    inRezervariMenu = false;
                    break;
                default:
                    System.out.println("Optiune invalida!");
            }
        }
    }

    private void afiseazaToateRezervarile() {
        System.out.println("\n=== TOATE REZERVaRILE ===");
        List<Rezervare> rezervari = adminService.getToateRezervarile();
        
        if (rezervari.isEmpty()) {
            System.out.println("Nu exista rezervari in sistem.");
            return;
        }

        System.out.println("Total rezervari: " + rezervari.size());
        System.out.println("══════════════════════════════════════════");
        
        for (int i = 0; i < rezervari.size(); i++) {
            Rezervare rez = rezervari.get(i);
            System.out.println((i + 1) + ". " + rez);
        }
    }

    private void cautaRezervariClient(Scanner sc) {
        System.out.print("Introdu email-ul clientului: ");
        String email = sc.nextLine().trim();

        List<Rezervare> rezervari = adminService.getRezervariByClient(email);

        System.out.println("\n=== REZERVaRILE CLIENTULUI " + email + " ===");
        if (rezervari.isEmpty()) {
            System.out.println("Clientul nu are rezervari.");
        } else {
            for (int i = 0; i < rezervari.size(); i++) {
                System.out.println((i + 1) + ". " + rezervari.get(i));
            }
        }
    }

    private void anuleazaRezervare(Scanner sc) {
        System.out.print("Introdu email-ul clientului: ");
        String email = sc.nextLine().trim();

        List<Rezervare> rezervari = adminService.getRezervariByClient(email);
        
        if (rezervari.isEmpty()) {
            System.out.println("Clientul nu are rezervari de anulat.");
            return;
        }

        System.out.println("\nRezervarile clientului:");
        for (int i = 0; i < rezervari.size(); i++) {
            System.out.println((i + 1) + ". " + rezervari.get(i));
        }

        System.out.print("Alege numarul rezervarii de anulat: ");
        try {
            int numar = Integer.parseInt(sc.nextLine().trim()) - 1;
            
            boolean anulat = adminService.anuleazaRezervare(email, numar);
            
            if (anulat) {
                System.out.println("✅ Rezervarea a fost anulata cu succes!");
            } else {
                System.out.println("❌ Nu s-a putut anula rezervarea!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Numar invalid!");
        }
    }

    private void afiseazaStatisticiRezervari() {
        System.out.println("\n=== STATISTICI REZERVaRI ===");
        System.out.println("Total rezervari: " + adminService.getTotalRezervari());
        System.out.println("Clienti cu rezervari: " + adminService.getClientiActivi());
        System.out.println("Total clienti: " + adminService.getTotalClienti());
        
        if (adminService.getTotalClienti() > 0) {
            double rata = (double) adminService.getClientiActivi() / adminService.getTotalClienti() * 100;
            System.out.println("Rata de utilizare: " + String.format("%.2f", rata) + "%");
        }
    }

    // =============== MANAGEMENT CLIENtI ===============
    
    public void managementClienti(Scanner sc) {
        boolean inClientiMenu = true;

        while (inClientiMenu) {
            System.out.println("\n=== MANAGEMENT CLIENtI (ADMIN) ===");
            System.out.println("1. Vezi toti clientii");
            System.out.println("2. Cauta client dupa email");
            System.out.println("3. Actualizeaza client");
            System.out.println("4. sterge client");
            System.out.println("5. Statistici clienti");
            System.out.println("6. Back");
            System.out.print("Alege optiunea: ");

            String optiune = sc.nextLine().trim();

            switch (optiune) {
                case "1":
                    afiseazaToatiClientiiBD();
                    break;
                case "2":
                    cautaClient(sc);
                    break;
                case "3":
                    actualizeazaClient(sc);
                    break;
                case "4":
                    stergeClient(sc);
                    break;
                case "5":
                    afiseazaStatisticiClienti();
                    break;
                case "6":
                    inClientiMenu = false;
                    break;
                default:
                    System.out.println("Optiune invalida!");
            }
        }
    }

    private void afiseazaToatiClientiiBD() {
        System.out.println("\n=== TOtI CLIENtII DIN BAZA DE DATE ===");
        List<Client> clienti = adminService.getToatiClientiiBD();
        
        if (clienti.isEmpty()) {
            System.out.println("Nu exista clienti in baza de date.");
            return;
        }

        System.out.println("Total clienti: " + clienti.size());
        System.out.println("══════════════════════════════════════════");
        
        for (Client client : clienti) {
            int nrRezervari = client.getRezervari() != null ? client.getRezervari().size() : 0;
            System.out.println("• " + client.getNume() + " " + client.getPrenume() + 
                             " | " + client.getEmail() + 
                             " | " + client.getTelefon() + 
                             " | Rezervari: " + nrRezervari);
        }
    }

    private void cautaClient(Scanner sc) {
        System.out.print("Introdu email-ul clientului: ");
        String email = sc.nextLine().trim();

        Client client = adminService.cautaClientDupaEmail(email);
        
        if (client != null) {
            System.out.println("\n=== CLIENT GaSIT ===");
            int nrRezervari = client.getRezervari() != null ? client.getRezervari().size() : 0;
            System.out.println("Nume: " + client.getNume() + " " + client.getPrenume());
            System.out.println("Email: " + client.getEmail());
            System.out.println("Telefon: " + client.getTelefon());
            System.out.println("Numar rezervari: " + nrRezervari);
        } else {
            System.out.println("Nu s-a gasit clientul cu email-ul: " + email);
        }
    }

    private void actualizeazaClient(Scanner sc) {
        System.out.print("Introdu email-ul clientului de actualizat: ");
        String email = sc.nextLine().trim();

        Client client = adminService.cautaClientDupaEmail(email);
        
        if (client == null) {
            System.out.println("Nu s-a gasit clientul cu email-ul: " + email);
            return;
        }

        System.out.println("\nClient gasit: " + client.getNume() + " " + client.getPrenume());
        System.out.println("Introdu noile date (lasa gol pentru a pastra valoarea actuala):");
        
        System.out.print("Nume nou [" + client.getNume() + "]: ");
        String numeNou = sc.nextLine().trim();
        if (!numeNou.isEmpty()) {
            client.setNume(numeNou);
        }

        System.out.print("Prenume nou [" + client.getPrenume() + "]: ");
        String prenumeNou = sc.nextLine().trim();
        if (!prenumeNou.isEmpty()) {
            client.setPrenume(prenumeNou);
        }

        System.out.print("Telefon nou [" + client.getTelefon() + "]: ");
        String telefonNou = sc.nextLine().trim();
        if (!telefonNou.isEmpty()) {
            client.setTelefon(telefonNou);
        }

        boolean actualizat = adminService.actualizeazaClient(client);
        
        if (actualizat) {
            System.out.println("✅ Clientul a fost actualizat cu succes!");
        } else {
            System.out.println("❌ Nu s-a putut actualiza clientul!");
        }
    }

    private void stergeClient(Scanner sc) {
        System.out.print("Introdu email-ul clientului de sters: ");
        String email = sc.nextLine().trim();

        Client client = adminService.cautaClientDupaEmail(email);
        
        if (client == null) {
            System.out.println("Nu s-a gasit clientul cu email-ul: " + email);
            return;
        }

        System.out.println("Client gasit: " + client.getNume() + " " + client.getPrenume());
        System.out.print("Esti sigur ca vrei sa stergi acest client? (da/nu): ");
        String confirmare = sc.nextLine().trim().toLowerCase();

        if (confirmare.equals("da") || confirmare.equals("yes")) {
            boolean sters = adminService.stergeClient(email);
            
            if (sters) {
                System.out.println("✅ Clientul a fost sters cu succes!");
            } else {
                System.out.println("❌ Nu s-a putut sterge clientul!");
            }
        } else {
            System.out.println("Operatia a fost anulata.");
        }
    }

    private void afiseazaStatisticiClienti() {
        System.out.println("\n=== STATISTICI CLIENtI ===");
        System.out.println("Total clienti: " + adminService.getTotalClienti());
        System.out.println("Clienti activi (cu rezervari): " + adminService.getClientiActivi());
        System.out.println("Total rezervari: " + adminService.getTotalRezervari());
        
        if (adminService.getTotalClienti() > 0) {
            double rataActivitate = (double) adminService.getClientiActivi() / adminService.getTotalClienti() * 100;
            System.out.println("   • Rata de activitate: " + String.format("%.2f", rataActivitate) + "%");
        }
    }

    // =============== MANAGEMENT ANGAJAtI ===============
    
    public void managementAngajati(Scanner sc) {
        boolean inAngajatiMenu = true;

        while (inAngajatiMenu) {
            System.out.println("\n=== MANAGEMENT ANGAJAtI (ADMIN) ===");
            System.out.println("1. Vezi toti angajatii");
            System.out.println("2. Cauta angajat dupa email");
            System.out.println("3. Adauga angajat nou");
            System.out.println("4. Actualizeaza angajat");
            System.out.println("5. sterge angajat");
            System.out.println("6. Statistici angajati");
            System.out.println("7. Back");
            System.out.print("Alege optiunea: ");

            String optiune = sc.nextLine().trim();

            switch (optiune) {
                case "1":
                    afiseazaToatiAngajatiiBD();
                    break;
                case "2":
                    cautaAngajat(sc);
                    break;
                case "3":
                    adaugaAngajat(sc);
                    break;
                case "4":
                    actualizeazaAngajat(sc);
                    break;
                case "5":
                    stergeAngajat(sc);
                    break;
                case "6":
                    afiseazaStatisticiAngajati();
                    break;
                case "7":
                    inAngajatiMenu = false;
                    break;
                default:
                    System.out.println("Optiune invalida!");
            }
        }
    }

    private void afiseazaToatiAngajatiiBD() {
        System.out.println("\n=== TOtI ANGAJAtII DIN BAZA DE DATE ===");
        List<Angajat> angajati = adminService.getToatiAngajatiiBD();
        
        if (angajati.isEmpty()) {
            System.out.println("Nu exista angajati in baza de date.");
            return;
        }

        System.out.println("Total angajati: " + angajati.size());
        System.out.println("?????????????????????????????????????????????");
        
        for (Angajat angajat : angajati) {
            String tip = angajat instanceof Ghid ? "Ghid" : "Agent Vanzari";
            System.out.println("? " + angajat.getNume() + " " + angajat.getPrenume() + 
                             " | " + angajat.getEmail() + 
                             " | " + tip + 
                             " | Salariu: " + String.format("%.2f", angajat.calculSalariu()) + " RON");
        }
    }

    private void cautaAngajat(Scanner sc) {
        System.out.print("Introdu email-ul angajatului: ");
        String email = sc.nextLine().trim();

        Angajat angajat = adminService.cautaAngajatDupaEmail(email);
        
        if (angajat != null) {
            System.out.println("\n=== ANGAJAT GaSIT ===");
            String tip = angajat instanceof Ghid ? "Ghid" : "Agent Vanzari";
            System.out.println("Nume: " + angajat.getNume() + " " + angajat.getPrenume());
            System.out.println("Email: " + angajat.getEmail());
            System.out.println("Tip: " + tip);
            System.out.println("Salariu: " + String.format("%.2f", angajat.calculSalariu()) + " RON");
        } else {
            System.out.println("Nu s-a gasit angajatul cu email-ul: " + email);
        }
    }

    private void adaugaAngajat(Scanner sc) {
        System.out.println("\n=== ADAUGa ANGAJAT NOU ===");
        
        System.out.print("Nume: ");
        String nume = sc.nextLine().trim();
        
        System.out.print("Prenume: ");
        String prenume = sc.nextLine().trim();
        
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        
        // Verifica daca email-ul exista deja
        if (adminService.emailExistaLaAngajati(email)) {
            System.out.println("Email-ul exista deja in sistem!");
            return;
        }
        
        System.out.print("Parola: ");
        String parola = sc.nextLine().trim();
        
        System.out.print("Salariu baza: ");
        double salariu;
        try {
            salariu = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Salariu invalid!");
            return;
        }
        
        System.out.println("Tip angajat:");
        System.out.println("1. Ghid");
        System.out.println("2. Agent Vanzari");
        System.out.print("Alege optiunea: ");
        
        String tipOptiune = sc.nextLine().trim();
        Angajat angajatNou = null;
        
        switch (tipOptiune) {
            case "1":
                // Creeaza Ghid
                System.out.print("ID Locatie: ");
                int idLocatie;
                try {
                    idLocatie = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("ID locatie invalid!");
                    return;
                }
                
                List<String> limbi = new ArrayList<>();
                System.out.println("Introdu limbile vorbite (apasa Enter gol pentru a termina):");
                while (true) {
                    System.out.print("Limba: ");
                    String limba = sc.nextLine().trim();
                    if (limba.isEmpty()) break;
                    limbi.add(limba);
                }
                
                angajatNou = new Ghid(nume, prenume, email, JobType.GHID, (int)salariu, idLocatie);
                
                // Add languages to the guide
                if (angajatNou instanceof Ghid) {
                    Ghid ghid = (Ghid) angajatNou;
                    for (String limba : limbi) {
                        ghid.adaugaLimba(limba);
                    }
                }
                break;
                
            case "2":
                // Creeaza Agent Vanzari
                System.out.print("Comision percentage: ");
                double comision;
                try {
                    comision = Double.parseDouble(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Comision invalid!");
                    return;
                }
                
                System.out.print("Total vanzari: ");
                double totalVanzari;
                try {
                    totalVanzari = Double.parseDouble(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Total vanzari invalid!");
                    return;
                }
                
                angajatNou = new AgentVanzari(nume, prenume, email, JobType.AGENT_VANZARI, (int)salariu, comision);
                
                // Set the total sales for the agent
                if (angajatNou instanceof AgentVanzari) {
                    AgentVanzari agent = (AgentVanzari) angajatNou;
                    agent.setTotalVanzari(totalVanzari);
                }
                break;
                
            default:
                System.out.println("Optiune invalida!");
                return;
        }
        
        boolean adaugat = adminService.adaugaAngajat(angajatNou);
        
        if (adaugat) {
            System.out.println("✅ Angajatul a fost adaugat cu succes!");
        } else {
            System.out.println("❌ Nu s-a putut adauga angajatul!");
        }
    }

    private void actualizeazaAngajat(Scanner sc) {
        System.out.print("Introdu email-ul angajatului de actualizat: ");
        String email = sc.nextLine().trim();

        Angajat angajat = adminService.cautaAngajatDupaEmail(email);
        
        if (angajat == null) {
            System.out.println("Nu s-a gasit angajatul cu email-ul: " + email);
            return;
        }

        System.out.println("\nAngajat gasit: " + angajat.getNume() + " " + angajat.getPrenume());
        System.out.println("Introdu noile date (lasa gol pentru a pastra valoarea actuala):");
        
        System.out.print("Nume nou [" + angajat.getNume() + "]: ");
        String numeNou = sc.nextLine().trim();
        if (!numeNou.isEmpty()) {
            angajat.setNume(numeNou);
        }

        System.out.print("Prenume nou [" + angajat.getPrenume() + "]: ");
        String prenumeNou = sc.nextLine().trim();
        if (!prenumeNou.isEmpty()) {
            angajat.setPrenume(prenumeNou);
        }

        System.out.print("Salariu nou [" + angajat.calculSalariu() + "]: ");
        String salariuNou = sc.nextLine().trim();
        if (!salariuNou.isEmpty()) {
            try {
                double salariu = Double.parseDouble(salariuNou);
                angajat.setSalariuBaza((int)salariu);
            } catch (NumberFormatException e) {
                System.out.println("Salariu invalid, pastram valoarea veche.");
            }
        }

        boolean actualizat = adminService.actualizeazaAngajat(angajat);
        
        if (actualizat) {
            System.out.println("✅ Angajatul a fost actualizat cu succes!");
        } else {
            System.out.println("❌ Nu s-a putut actualiza angajatul!");
        }
    }

    private void stergeAngajat(Scanner sc) {
        System.out.print("Introdu email-ul angajatului de sters: ");
        String email = sc.nextLine().trim();

        Angajat angajat = adminService.cautaAngajatDupaEmail(email);
        
        if (angajat == null) {
            System.out.println("Nu s-a gasit angajatul cu email-ul: " + email);
            return;
        }

        System.out.println("Angajat gasit: " + angajat.getNume() + " " + angajat.getPrenume());
        System.out.print("Esti sigur ca vrei sa stergi acest angajat? (da/nu): ");
        String confirmare = sc.nextLine().trim().toLowerCase();

        if (confirmare.equals("da") || confirmare.equals("yes")) {
            boolean sters = adminService.stergeAngajat(email);
            
            if (sters) {
                System.out.println("✅ Angajatul a fost sters cu succes!");
            } else {
                System.out.println("❌ Nu s-a putut sterge angajatul!");
            }
        } else {
            System.out.println("Operatia a fost anulata.");
        }
    }

    private void afiseazaStatisticiAngajati() {
        System.out.println("\n=== STATISTICI ANGAJAtI ===");
        System.out.println("Total angajati: " + adminService.getTotalAngajati());
        System.out.println("Salariu mediu: " + String.format("%.2f", adminService.getSalariuMediuAngajati()) + " RON");
        
        List<Angajat> angajati = adminService.getToatiAngajatiiBD();
        int ghizi = 0, agenti = 0;
        for (Angajat ang : angajati) {
            if (ang instanceof Ghid) ghizi++;
            else if (ang instanceof AgentVanzari) agenti++;
        }
        
        System.out.println("Ghizi: " + ghizi);
        System.out.println("Agenti vanzari: " + agenti);
    }

    // =============== STATISTICI QUICK ===============
    
    private void afiseazaStatisticiQuick() {
        System.out.println("\n=== STATISTICI QUICK ADMIN ===");
        System.out.println("?????????????????????????????????????????????");
        System.out.println("📊 UTILIZATORI:");
        System.out.println("   • Total clienti: " + adminService.getTotalClienti());
        System.out.println("   • Total angajati: " + adminService.getTotalAngajati());
        System.out.println("   • Total utilizatori: " + (adminService.getTotalClienti() + adminService.getTotalAngajati()));

        System.out.println("\n📋 REZERVARI:");
        System.out.println("   • Total rezervari: " + adminService.getTotalRezervari());
        System.out.println("   • Clienti activi: " + adminService.getClientiActivi());
        
        if (adminService.getTotalClienti() > 0) {
            double rataActivitate = (double) adminService.getClientiActivi() / adminService.getTotalClienti() * 100;
            System.out.println("   • Rata de activitate: " + String.format("%.2f", rataActivitate) + "%");
        }
        
        System.out.println("\n💰 FINANCIAR:");
        System.out.println("   • Salariu mediu angajati: " + String.format("%.2f", adminService.getSalariuMediuAngajati()) + " RON");
        System.out.println("══════════════════════════════════════════");
    }
} 

