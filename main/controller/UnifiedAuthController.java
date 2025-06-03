package main.controller;

import main.service.UserService;
import main.service.AuditService;
import main.service.LocatieService;
import main.service.CatalogService;
import main.service.PlataService;
import main.service.AdminService;
import main.domain.Client;
import main.domain.Angajat;
import main.domain.Ghid;
import main.domain.AgentVanzari;
import main.domain.Locatie;
import main.domain.Oferta;
import main.domain.Rezervare;
import main.domain.Plata;
import main.domain.Pachet;
import main.domain.enums.Sezon;
import java.util.Scanner;
import java.util.List;
import java.time.LocalDate;

public class UnifiedAuthController {
    private UserService userService;
    private AuditService auditService;
    private LocatieService locatieService;
    private CatalogService catalogService;
    private PlataService plataService;
    private AdminService adminService;

    public UnifiedAuthController() {
        this.userService = new UserService();
        this.auditService = AuditService.getInstance();
        this.locatieService = new LocatieService();
        this.catalogService = new CatalogService();
        this.plataService = new PlataService();
        this.adminService = new AdminService(this.userService);
    }

    public void handleAuthMenu(Scanner sc) {
        boolean inAuthMenu = true;
        
        while (inAuthMenu) {
            displayMainAuthMenu();
            String optiune = sc.nextLine().trim();
            
            switch (optiune) {
                case "1":
                    handleLogin(sc);
                    if (userService.getCurrentUser() != null) {
                        inAuthMenu = false;
                    }
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
                auditService.logAction(AuditService.Actions.LOGIN_CLIENT);
                System.out.println("Login CLIENT reusit! Bun venit, " + client.getNume() + " " + client.getPrenume() + "!");
                // Nu mai afisam meniul aici, lasam Main.java sa se ocupe de redirectare
            } else if (user instanceof Angajat) {
                Angajat angajat = (Angajat) user;
                auditService.logAction(AuditService.Actions.LOGIN_ANGAJAT);
                
                if (angajat instanceof main.domain.Director) {
                    System.out.println("Login DIRECTOR reusit! Bun venit, " + angajat.getNume() + " " + angajat.getPrenume() + "!");
                } else if (angajat instanceof Ghid) {
                    Ghid ghid = (Ghid) angajat;
                    String numeLocatie = getNumeLocatie(ghid.getIdLocatie());
                    System.out.println("Login GHID reusit! Bun venit, " + ghid.getNume() + " " + ghid.getPrenume() + "!");
                    System.out.println("📍 Locatie de lucru: " + numeLocatie);
                } else {
                    System.out.println("Login ANGAJAT reusit! Bun venit, " + angajat.getNume() + " " + angajat.getPrenume() + "!");
                }
                
                // Nu mai afisam meniul aici, lasam Main.java sa se ocupe de redirectare
            }
        } else {
            System.out.println("Email sau parola incorecte!");
        }
    }

    private void handleClientSignUp(Scanner sc) {
        System.out.println("\n=== SIGN UP CLIENT ===");
        
        System.out.print("Nume: ");
        String nume = sc.nextLine().trim();
        
        System.out.print("Prenume: ");
        String prenume = sc.nextLine().trim();
        
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        
        System.out.print("Telefon: ");
        String telefon = sc.nextLine().trim();
        
        System.out.print("Parola: ");
        String parola = sc.nextLine().trim();
        
        boolean success = userService.createClient(nume, prenume, email, telefon, parola);
        
        if (success) {
            auditService.logAction(AuditService.Actions.SIGNUP_CLIENT);
            System.out.println("Cont CLIENT creat cu succes! Poti sa te loghezi acum.");
        } else {
            System.out.println("Eroare la crearea contului. Incearca din nou.");
        }
    }

    private void handleEmployeeSignUp(Scanner sc) {
        System.out.println("\n=== SIGN UP ANGAJAT ===");
        
        System.out.print("Nume: ");
        String nume = sc.nextLine().trim();
        
        System.out.print("Prenume: ");
        String prenume = sc.nextLine().trim();
        
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        
        System.out.println("\nTipuri de angajati disponibili:");
        System.out.println("1. Ghid");
        System.out.println("2. Agent de vanzari");
        System.out.println("3. Director");
        System.out.print("Alege tipul: ");
        
        String tipOptiune = sc.nextLine().trim();
        String tipAngajat;
        String job = "";
        Object extraParam = null;
        
        switch (tipOptiune) {
            case "1":
                tipAngajat = "ghid";
                job = "GHID";
                
                // Afiseaza locatiile disponibile pentru ghizi
                System.out.println("\n📍 LOCATII DISPONIBILE:");
                var locatiiDisponibile = locatieService.getToateLocatiile();
                for (int i = 0; i < locatiiDisponibile.size(); i++) {
                    Locatie loc = locatiiDisponibile.get(i);
                    System.out.println((i+1) + ". " + loc.getNume() + ", " + loc.getTara().getNume() + " (ID: " + loc.getIdLocatie() + ")");
                }
                
                System.out.print("Selecteaza numarul locatiei (1-" + locatiiDisponibile.size() + "): ");
                try {
                    int alegereLocatie = Integer.parseInt(sc.nextLine().trim());
                    if (alegereLocatie >= 1 && alegereLocatie <= locatiiDisponibile.size()) {
                        extraParam = locatiiDisponibile.get(alegereLocatie - 1).getIdLocatie();
                        System.out.println("✅ Locatie selectata: " + getNumeLocatie((Integer) extraParam));
                    } else {
                        System.out.println("❌ Numar locatie invalid!");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("❌ Numar invalid!");
                    return;
                }
                break;
            case "2":
                tipAngajat = "agent";
                job = "AGENT_VANZARI";
                System.out.print("Comision (%): ");
                try {
                    extraParam = Double.parseDouble(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Comision invalid!");
                    return;
                }
                break;
            case "3":
                tipAngajat = "director";
                job = "DIRECTOR";
                break;
            default:
                System.out.println("Tip invalid!");
                return;
        }
        
        System.out.print("Salariu baza: ");
        double salariuBaza;
        try {
            salariuBaza = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Salariu invalid!");
            return;
        }
        
        System.out.print("Parola: ");
        String parola = sc.nextLine().trim();
        
        boolean success = userService.createEmployee(nume, prenume, email, tipAngajat, job, (int)salariuBaza, parola, extraParam);
        
        if (success) {
            auditService.logAction(AuditService.Actions.SIGNUP_ANGAJAT);
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
        auditService.logAction(AuditService.Actions.VIEW_PROFILE);
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
        auditService.logAction(AuditService.Actions.LOGOUT);
        System.out.println("La revedere, " + client.getNume() + "!");
        userService.logout();
    }

    // ============ EMPLOYEE SPECIFIC METHODS ============

    private void handleViewEmployeeProfile(Angajat angajat) {
        auditService.logAction(AuditService.Actions.VIEW_PROFILE);
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
        boolean inAgentMenu = true;
        
        while (inAgentMenu) {
            System.out.println("\n=== OPTIUNI AGENT VANZARI ===");
            System.out.println("Agent: " + agent.getNume() + " " + agent.getPrenume());
            System.out.println("Comision actual: " + String.format("%.2f", agent.getComisionPercentage()) + "%");
            System.out.println("Bonus lunar: " + String.format("%.2f", agent.calculComision()) + " RON");
            System.out.println("Salariu total: " + String.format("%.2f", agent.calculSalariu()) + " RON");
            System.out.println();
            System.out.println("1. 🏖️  Vezi catalogul de oferte");
            System.out.println("2. 🎯  Creeaza rezervare pentru client");
            System.out.println("3. 📋  Gestioneaza rezervari existente");
            System.out.println("4. 👥  Cauta client dupa email");
            System.out.println("5. 📊  Statisticile mele de vanzari");
            System.out.println("6. 🔙  Inapoi la meniul principal");
            System.out.print("Alege optiunea: ");
            
            String optiune = sc.nextLine().trim();
            
            switch (optiune) {
                case "1":
                    afiseazaCatalogPentruAgent(sc, agent);
                    break;
                case "2":
                    creazaRezervareAgent(sc, agent);
                    break;
                case "3":
                    gestioneazaRezervariAgent(sc, agent);
                    break;
                case "4":
                    cautaClientAgent(sc, agent);
                    break;
                case "5":
                    afiseazaStatisticiVanzariAgent(agent);
                    break;
                case "6":
                    inAgentMenu = false;
                    break;
                default:
                    System.out.println("Optiune invalida!");
            }
        }
    }

    private void afiseazaCatalogPentruAgent(Scanner sc, AgentVanzari agent) {
        auditService.logAction(AuditService.Actions.VIEW_CATALOG);
        
        System.out.println("\n🏖️ CATALOG OFERTE DISPONIBILE");
        System.out.println("===============================");
        
        List<Oferta> oferte = catalogService.getToateOfertele();
        if (oferte.isEmpty()) {
            System.out.println("Nu sunt oferte disponibile momentan.");
            return;
        }
        
        System.out.println("1. Vezi toate ofertele (" + oferte.size() + ")");
        System.out.println("2. Filtreaza dupa sezon");
        System.out.println("3. Filtreaza dupa pret maxim");
        System.out.println("4. Filtreaza dupa locatie");
        System.out.print("Alege optiunea: ");
        
        String optiune = sc.nextLine().trim();
        List<Oferta> oferteFiltered;
        
        switch (optiune) {
            case "1":
                oferteFiltered = oferte;
                break;
            case "2":
                oferteFiltered = filtreazaDupaSezon(sc, oferte);
                break;
            case "3":
                oferteFiltered = filtreazaDupaPret(sc, oferte);
                break;
            case "4":
                oferteFiltered = filtreazaDupaLocatie(sc, oferte);
                break;
            default:
                System.out.println("Optiune invalida!");
                return;
        }
        
        afiseazaOferteLista(oferteFiltered);
    }

    private List<Oferta> filtreazaDupaSezon(Scanner sc, List<Oferta> oferte) {
        System.out.println("\nSezoane disponibile:");
        System.out.println("1. Primavara");
        System.out.println("2. Vara");
        System.out.println("3. Toamna");
        System.out.println("4. Iarna");
        System.out.println("5. Tot anul");
        System.out.print("Alege sezonul: ");
        
        String alegere = sc.nextLine().trim();
        Sezon sezonSelectat = null;
        
        switch (alegere) {
            case "1": sezonSelectat = Sezon.PRIMAVARA; break;
            case "2": sezonSelectat = Sezon.VARA; break;
            case "3": sezonSelectat = Sezon.TOAMNA; break;
            case "4": sezonSelectat = Sezon.IARNA; break;
            case "5": sezonSelectat = Sezon.TOT_ANUL; break;
            default:
                System.out.println("Alegere invalida!");
                return oferte;
        }
        
        return catalogService.getOferteDupaSezon(sezonSelectat);
    }

    private List<Oferta> filtreazaDupaPret(Scanner sc, List<Oferta> oferte) {
        System.out.print("Introdu pretul maxim dorit: ");
        try {
            double pretMaxim = Double.parseDouble(sc.nextLine().trim());
            return catalogService.getOferteDupaPret(pretMaxim);
        } catch (NumberFormatException e) {
            System.out.println("Pret invalid!");
            return oferte;
        }
    }

    private List<Oferta> filtreazaDupaLocatie(Scanner sc, List<Oferta> oferte) {
        System.out.print("Introdu locatia dorita: ");
        String locatie = sc.nextLine().trim();
        return catalogService.getOferteDupaLocatie(locatie);
    }

    private void afiseazaOferteLista(List<Oferta> oferte) {
        if (oferte.isEmpty()) {
            System.out.println("Nu au fost gasite oferte care sa corespunda criteriilor.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(50));
        for (int i = 0; i < oferte.size(); i++) {
            System.out.printf("\n[%d] %s\n", (i + 1), oferte.get(i));
            System.out.println("-".repeat(40));
        }
        System.out.println("\nTotal oferte afisate: " + oferte.size());
    }

    private void creazaRezervareAgent(Scanner sc, AgentVanzari agent) {
        auditService.logAction(AuditService.Actions.CREATE_RESERVATION);
        
        System.out.println("\n🎯 CREARE REZERVARE NOUA");
        System.out.println("==========================");
        
        // Pas 1: Obtine detaliile clientului
        System.out.print("Email client: ");
        String emailClient = sc.nextLine().trim();
        
        Client client = adminService.cautaClientDupaEmail(emailClient);
        if (client == null) {
            System.out.println("Clientul nu a fost gasit. Doresti sa creezi un cont nou? (da/nu): ");
            String raspuns = sc.nextLine().trim();
            if (raspuns.equalsIgnoreCase("da")) {
                client = creazaClientNou(sc, emailClient);
                if (client == null) {
                    System.out.println("Nu s-a putut crea clientul. Rezervarea anulata.");
                    return;
                }
            } else {
                System.out.println("Rezervarea anulata.");
                return;
            }
        }
        
        System.out.println("✅ Client gasit: " + client.getNume() + " " + client.getPrenume());
        
        // Pas 2: Selecteaza oferta
        List<Oferta> oferte = catalogService.getToateOfertele();
        if (oferte.isEmpty()) {
            System.out.println("Nu sunt oferte disponibile pentru rezervare.");
            return;
        }
        
        System.out.println("\n🏖️ OFERTE DISPONIBILE:");
        for (int i = 0; i < oferte.size(); i++) {
            Oferta oferta = oferte.get(i);
            System.out.printf("[%d] %s - %.2f RON (%d zile)\n", 
                            (i + 1), oferta.getNume(), oferta.getPret(), oferta.getNumarZile());
        }
        
        System.out.print("Selecteaza numarul ofertei: ");
        try {
            int numarOferta = Integer.parseInt(sc.nextLine().trim()) - 1;
            if (numarOferta < 0 || numarOferta >= oferte.size()) {
                System.out.println("Numar invalid!");
                return;
            }
            
            Oferta ofertaSelectata = oferte.get(numarOferta);
            
            // Pas 3: Creaza rezervarea reala
            System.out.println("\n📝 DETALII REZERVARE:");
            System.out.println("Client: " + client.getNume() + " " + client.getPrenume());
            System.out.println("Email: " + client.getEmail());
            System.out.println("Oferta: " + ofertaSelectata.getNume());
            System.out.println("Pret: " + ofertaSelectata.getPret() + " RON");
            System.out.println("Agent: " + agent.getNume() + " " + agent.getPrenume());
            
            System.out.print("Confirmi rezervarea? (da/nu): ");
            String confirmare = sc.nextLine().trim();
            
            if (confirmare.equalsIgnoreCase("da")) {
                // Creaza un pachet simplu din oferta pentru compatibilitate
                Pachet pachetTemporal = new Pachet(ofertaSelectata.getIdOferta(), 
                                                 ofertaSelectata.getNume(), 
                                                 ofertaSelectata.getNumarZile());
                
                // Creaza rezervarea reala
                Rezervare rezervareNoua = new Rezervare(client, agent, pachetTemporal, LocalDate.now());
                
                // Adauga rezervarea la client (sincronizat bidirectional)
                client.adaugaRezervare(rezervareNoua);
                
                // Salveaza clientul actualizat in repository
                boolean salvat = adminService.actualizeazaClient(client);
                
                if (salvat) {
                    System.out.println("\n🎉 REZERVARE CREATA CU SUCCES!");
                    System.out.println("ID Rezervare: " + rezervareNoua.getIdRezervare());
                    System.out.println("Data rezervare: " + rezervareNoua.getDataRezervare());
                    System.out.println("📋 Rezervarea a fost adaugata la contul clientului!");
                    
                    // Creaza plata
                    System.out.print("Procesez plata acum? (da/nu): ");
                    String plata = sc.nextLine().trim();
                    if (plata.equalsIgnoreCase("da")) {
                        proceseazaPlataPentruRezervare(sc, rezervareNoua.getIdRezervare(), ofertaSelectata.getPret(), emailClient);
                        
                        // Actualizeaza rezervarea cu plata
                        rezervareNoua.efectueazaPlata(1, ofertaSelectata.getPret(), LocalDate.now());
                        adminService.actualizeazaClient(client); // Salveaza din nou cu plata
                    }
                    
                    // Actualizeaza comisionul agentului (simulator)
                    double comisionCastigat = ofertaSelectata.getPret() * agent.getComisionPercentage() / 100;
                    System.out.printf("💰 Comision castigat: %.2f RON\n", comisionCastigat);
                    
                    // Afiseaza confirmarea finala
                    System.out.println("\n✅ REZERVARE FINALIZATA:");
                    System.out.println(rezervareNoua.toString());
                    
                } else {
                    System.out.println("❌ Eroare la salvarea rezervarii! Contacteaza administratorul.");
                }
                
            } else {
                System.out.println("Rezervarea a fost anulata.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Numar invalid!");
        }
    }

    private Client creazaClientNou(Scanner sc, String email) {
        System.out.println("\n👤 CREARE CLIENT NOU");
        System.out.println("Nume: ");
        String nume = sc.nextLine().trim();
        System.out.println("Prenume: ");
        String prenume = sc.nextLine().trim();
        System.out.println("Telefon: ");
        String telefon = sc.nextLine().trim();
        System.out.println("Parola: ");
        String parola = sc.nextLine().trim();
        
        boolean success = userService.createClient(nume, prenume, email, telefon, parola);
        if (success) {
            System.out.println("✅ Client creat cu succes!");
            return adminService.cautaClientDupaEmail(email);
        } else {
            System.out.println("❌ Eroare la crearea clientului!");
            return null;
        }
    }

    private void proceseazaPlataPentruRezervare(Scanner sc, int idRezervare, double suma, String emailClient) {
        System.out.println("\n💳 PROCESARE PLATA");
        System.out.println("Metode de plata disponibile:");
        System.out.println("1. Card");
        System.out.println("2. Transfer bancar");
        System.out.println("3. PayPal");
        System.out.println("4. Cash");
        System.out.print("Selecteaza metoda: ");
        
        String alegereMetoda = sc.nextLine().trim();
        String metodaPlata = "cash";
        
        switch (alegereMetoda) {
            case "1": metodaPlata = "card"; break;
            case "2": metodaPlata = "transfer"; break;
            case "3": metodaPlata = "paypal"; break;
            case "4": metodaPlata = "cash"; break;
        }
        
        Plata plata = plataService.creazaPlata(idRezervare, emailClient, suma, metodaPlata);
        boolean success = plataService.proceseazaPlata(plata.getIdPlata());
        
        if (success) {
            System.out.println("✅ Plata procesata cu succes!");
            System.out.println("ID Tranzactie: " + plata.getTranzactieId());
        } else {
            System.out.println("❌ Plata a esuat!");
        }
    }

    private void gestioneazaRezervariAgent(Scanner sc, AgentVanzari agent) {
        System.out.println("\n📋 GESTIONARE REZERVARI");
        System.out.println("========================");
        
        System.out.println("1. Vezi toate rezervarile");
        System.out.println("2. Cauta rezervari dupa client");
        System.out.println("3. Anuleaza rezervare");
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
                anuleazaRezervareAgent(sc);
                break;
            default:
                System.out.println("Optiune invalida!");
        }
    }

    private void afiseazaToateRezervarile() {
        List<Rezervare> rezervari = adminService.getToateRezervarile();
        
        if (rezervari.isEmpty()) {
            System.out.println("Nu exista rezervari in sistem.");
            return;
        }
        
        System.out.println("\n📋 TOATE REZERVARILE (" + rezervari.size() + "):");
        System.out.println("=".repeat(50));
        
        for (int i = 0; i < rezervari.size(); i++) {
            System.out.println((i + 1) + ". " + rezervari.get(i));
            System.out.println("-".repeat(30));
        }
    }

    private void cautaRezervariClient(Scanner sc) {
        System.out.print("Introdu email-ul clientului: ");
        String email = sc.nextLine().trim();
        
        List<Rezervare> rezervari = adminService.getRezervariByClient(email);
        
        if (rezervari.isEmpty()) {
            System.out.println("Clientul nu are rezervari.");
        } else {
            System.out.println("\n📋 REZERVARILE CLIENTULUI " + email + ":");
            for (int i = 0; i < rezervari.size(); i++) {
                System.out.println((i + 1) + ". " + rezervari.get(i));
            }
        }
    }

    private void anuleazaRezervareAgent(Scanner sc) {
        System.out.print("Email client: ");
        String email = sc.nextLine().trim();
        
        List<Rezervare> rezervari = adminService.getRezervariByClient(email);
        if (rezervari.isEmpty()) {
            System.out.println("Clientul nu are rezervari.");
            return;
        }
        
        System.out.println("\nRezervarile clientului:");
        for (int i = 0; i < rezervari.size(); i++) {
            System.out.println((i + 1) + ". " + rezervari.get(i));
        }
        
        System.out.print("Numarul rezervarii de anulat: ");
        try {
            int numar = Integer.parseInt(sc.nextLine().trim()) - 1;
            boolean success = adminService.anuleazaRezervare(email, numar);
            
            if (success) {
                System.out.println("✅ Rezervare anulata cu succes!");
            } else {
                System.out.println("❌ Nu s-a putut anula rezervarea!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Numar invalid!");
        }
    }

    private void cautaClientAgent(Scanner sc, AgentVanzari agent) {
        System.out.print("Introdu email-ul clientului: ");
        String email = sc.nextLine().trim();
        
        Client client = adminService.cautaClientDupaEmail(email);
        if (client == null) {
            System.out.println("Clientul nu a fost gasit.");
            System.out.print("Doresti sa creezi un cont nou pentru acest email? (da/nu): ");
            String raspuns = sc.nextLine().trim();
            if (raspuns.equalsIgnoreCase("da")) {
                Client clientNou = creazaClientNou(sc, email);
                if (clientNou != null) {
                    afiseazaDetaliiClient(clientNou);
                }
            }
        } else {
            afiseazaDetaliiClient(client);
        }
    }

    private void afiseazaDetaliiClient(Client client) {
        System.out.println("\n👤 DETALII CLIENT:");
        System.out.println("Nume: " + client.getNume() + " " + client.getPrenume());
        System.out.println("Email: " + client.getEmail());
        System.out.println("Telefon: " + client.getTelefon());
        System.out.println("Numar rezervari: " + (client.getRezervari() != null ? client.getRezervari().size() : 0));
        
        if (client.getRezervari() != null && !client.getRezervari().isEmpty()) {
            System.out.println("\nRezervarile clientului:");
            for (int i = 0; i < client.getRezervari().size(); i++) {
                System.out.println((i + 1) + ". " + client.getRezervari().get(i));
            }
        }
    }

    private void afiseazaStatisticiVanzariAgent(AgentVanzari agent) {
        System.out.println("\n📊 STATISTICILE MELE DE VANZARI");
        System.out.println("================================");
        System.out.println("Agent: " + agent.getNume() + " " + agent.getPrenume());
        System.out.println("Email: " + agent.getEmail());
        System.out.println("Salariu baza: " + agent.getSalariuBaza() + " RON");
        System.out.println("Procent comision: " + String.format("%.2f", agent.getComisionPercentage()) + "%");
        System.out.println("Bonus lunar estimat: " + String.format("%.2f", agent.calculComision()) + " RON");
        System.out.println("Salariu total estimat: " + String.format("%.2f", agent.calculSalariu()) + " RON");
        System.out.println();
        
        // Simuleaza statistici suplimentare
        int rezervariLuna = (int)(Math.random() * 20) + 5; // 5-24 rezervari
        double vanzariLuna = rezervariLuna * 1500; // media de 1500 RON per rezervare
        double comisionCastigat = vanzariLuna * agent.getComisionPercentage() / 100;
        
        System.out.println("📈 PERFORMANTA LUNA CURENTA:");
        System.out.println("Rezervari create: " + rezervariLuna);
        System.out.println("Valoare vanzari: " + String.format("%.2f", vanzariLuna) + " RON");
        System.out.println("Comision castigat: " + String.format("%.2f", comisionCastigat) + " RON");
        System.out.println("Rating client: " + String.format("%.1f", (Math.random() * 2 + 3)) + "/5⭐");
    }

    private void handleEmployeeLogout(Angajat angajat) {
        auditService.logAction(AuditService.Actions.LOGOUT);
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
    
    /**
     * Verifică dacă un utilizator este autentificat
     */
    public boolean isUserLoggedIn() {
        return userService.getCurrentUser() != null;
    }
    
    /**
     * Verifică dacă utilizatorul curent este admin (Director)
     */
    public boolean isUserAdmin() {
        Object currentUser = userService.getCurrentUser();
        if (currentUser instanceof Angajat) {
            Angajat angajat = (Angajat) currentUser;
            // Verifică dacă job title-ul este DIRECTOR sau dacă este instanță de Director
            return angajat.getJobTitle().equals("DIRECTOR") || 
                   currentUser instanceof main.domain.Director;
        }
        return false;
    }
    
    /**
     * Returnează numele complet al utilizatorului curent
     */
    public String getCurrentUserName() {
        Object currentUser = userService.getCurrentUser();
        if (currentUser instanceof Client) {
            Client client = (Client) currentUser;
            return client.getNume() + " " + client.getPrenume();
        } else if (currentUser instanceof Angajat) {
            Angajat angajat = (Angajat) currentUser;
            return angajat.getNume() + " " + angajat.getPrenume();
        }
        return "Necunoscut";
    }
    
    /**
     * Returnează tipul utilizatorului curent pentru afișare
     */
    public String getCurrentUserType() {
        Object currentUser = userService.getCurrentUser();
        if (currentUser instanceof Client) {
            return "CLIENT";
        } else if (currentUser instanceof main.domain.Director) {
            return "DIRECTOR";
        } else if (currentUser instanceof Ghid) {
            return "GHID";
        } else if (currentUser instanceof AgentVanzari) {
            return "AGENT VANZARI";
        } else if (currentUser instanceof Angajat) {
            Angajat angajat = (Angajat) currentUser;
            return angajat.getJobTitle();
        }
        return "NECUNOSCUT";
    }

    /**
     * Helper method pentru obtinerea numelui locatiei din ID
     */
    private String getNumeLocatie(int idLocatie) {
        Locatie locatie = locatieService.getLocatieById(idLocatie);
        if (locatie != null) {
            return locatie.getNume() + ", " + locatie.getTara().getNume() + " (ID: " + idLocatie + ")";
        } else {
            return "Locatie necunoscuta (ID: " + idLocatie + ")";
        }
    }
} 
