package main.service;

import main.domain.Client;
import main.domain.Angajat;
import main.domain.Ghid;
import main.domain.AgentVanzari;
import main.persistence.ClientRepository;
import main.persistence.AngajatRepository;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    private List<Object> utilizatoriMemorie = new ArrayList<>();
    private Object utilizatorCurent = null;
    private Map<String, String> paroleCuEmail = new HashMap<>();
    private static final String CSV_FILE_PATH = "data/users.csv";

    // Repository-uri pentru sincronizarea cu BD
    private ClientRepository clientRepository;
    private AngajatRepository angajatRepository;
    
    // Helper lists pentru tracking
    private List<ClientParola> clientiParole = new ArrayList<>();
    private List<AngajatParola> angajatiParole = new ArrayList<>();

    // Helper classes
    private static class ClientParola {
        Client client;
        String parola;
        
        ClientParola(Client client, String parola) {
            this.client = client;
            this.parola = parola;
        }
    }
    
    private static class AngajatParola {
        Angajat angajat;
        String parola;
        
        AngajatParola(Angajat angajat, String parola) {
            this.angajat = angajat;
            this.parola = parola;
        }
    }

    public UserService() {
        this.utilizatoriMemorie = new ArrayList<>();
        this.utilizatorCurent = null;
        this.clientRepository = new ClientRepository();
        this.angajatRepository = new AngajatRepository();
        
        try {
            // Initializam repository-urile
            if (clientRepository != null && angajatRepository != null) {
                System.out.println("✔ Repository-uri initializate cu succes.");
            }
        } catch (Exception e) {
            System.err.println("⚠ Eroare la initializarea repository-urilor: " + e.getMessage());
            System.err.println("⚠ Continua doar cu CSV (fara sincronizare BD).");
        }
        
        // Incarca automat utilizatorii la pornire
        incarcaUseriDinFisier();
    }

    // =============== AUTENTIFICARE ===============
    
    /**
     * Autentifica un utilizator (client sau angajat)
     * @param email Email-ul utilizatorului
     * @param parola Parola utilizatorului
     * @return utilizatorul daca autentificarea reuseste, null altfel
     */
    public Object autentifica(String email, String parola) {
        for (Object utilizator : utilizatoriMemorie) {
            if (verificaCredentiale(utilizator, email, parola)) {
                utilizatorCurent = utilizator;
                return utilizator;
            }
        }
        return null;
    }

    /**
     * Verifica daca un email exista deja in sistem
     */
    public boolean emailExista(String email) {
        return gasesteDupaEmail(email) != null;
    }

    /**
     * Returneaza tipul utilizatorului (CLIENT, GHID, AGENT)
     */
    public String getTipUtilizator(Object utilizator) {
        if (utilizator instanceof Client) return "CLIENT";
        if (utilizator instanceof Ghid) return "GHID";
        if (utilizator instanceof AgentVanzari) return "AGENT";
        return "NECUNOSCUT";
    }

    /**
     * Returneaza utilizatorul curent logat
     */
    public Object getUtilizatorCurent() {
        return utilizatorCurent;
    }

    /**
     * Verifica daca utilizatorul curent este client
     */
    public boolean esteClient() {
        return utilizatorCurent instanceof Client;
    }

    /**
     * Verifica daca utilizatorul curent este angajat
     */
    public boolean esteAngajat() {
        return utilizatorCurent instanceof Angajat;
    }

    /**
     * Returneaza utilizatorul curent ca Client (cu verificare)
     */
    public Client getClientCurent() {
        if (esteClient()) {
            return (Client) utilizatorCurent;
        }
        return null;
    }

    /**
     * Returneaza utilizatorul curent ca Angajat (cu verificare)
     */
    public Angajat getAngajatCurent() {
        if (esteAngajat()) {
            return (Angajat) utilizatorCurent;
        }
        return null;
    }

    /**
     * Deconecteaza utilizatorul curent
     */
    public void deconecteaza() {
        utilizatorCurent = null;
    }

    // =============== INREGISTRARE ===============
    
    /**
     * Creeaza un client nou
     */
    public boolean inregistreazaClient(String nume, String prenume, String email, String telefon, String parola) {
        if (emailExista(email)) {
            return false;
        }

        Client clientNou = new Client(nume, prenume, email, telefon);
        paroleCuEmail.put(email, parola);

        // Salveaza in memorie
        utilizatoriMemorie.add(clientNou);
        clientiParole.add(new ClientParola(clientNou, parola));

        // Salveaza in CSV
        salveazaUtilizatorInCSV(clientNou);

        // Sincronizeaza cu BD (fara parola)
        if (clientRepository != null) {
            try {
                clientRepository.syncFromCSV(clientNou);
            } catch (Exception e) {
                System.err.println("Eroare la sincronizarea clientului cu BD: " + e.getMessage());
            }
        }

        return true;
    }

    /**
     * Creeaza un angajat nou
     */
    public boolean inregistreazaAngajat(String nume, String prenume, String email, 
                                       String job, double salariuBaza, String parola, 
                                       String tipSpecific, Object... parametriAditional) {
        if (emailExista(email)) {
            return false;
        }

        Angajat angajatNou = null;
        
        switch (tipSpecific.toLowerCase()) {
            case "ghid":
                int idLocatie = parametriAditional.length > 0 ? (Integer) parametriAditional[0] : 1;
                angajatNou = new Ghid(nume, prenume, email, job, (int)salariuBaza, idLocatie);
                break;
            case "agent":
                double comision = parametriAditional.length > 0 ? (Double) parametriAditional[0] : 0.05;
                angajatNou = new AgentVanzari(nume, prenume, email, job, (int)salariuBaza, comision);
                break;
            default:
                return false;
        }

        if (angajatNou != null) {
            paroleCuEmail.put(email, parola);

            // Salveaza in memorie
            utilizatoriMemorie.add(angajatNou);
            angajatiParole.add(new AngajatParola(angajatNou, parola));

            // Salveaza in CSV
            salveazaUtilizatorInCSV(angajatNou);

            // Sincronizeaza cu BD (fara parola)
            if (angajatRepository != null) {
                try {
                    angajatRepository.syncFromCSV(angajatNou);
                } catch (Exception e) {
                    System.err.println("Eroare la sincronizarea angajatului cu BD: " + e.getMessage());
                }
            }

            return true;
        }
        return false;
    }

    /**
     * Sincronizeaza toti utilizatorii din CSV cu BD
     */
    public void syncAllWithDatabase() {
        if (clientRepository == null || angajatRepository == null) {
            System.out.println("⚠ Sincronizarea cu BD nu este disponibila.");
            return;
        }
        
        System.out.println("🔄 Incep sincronizarea cu baza de date...");
        
        int syncedClients = 0;
        int syncedEmployees = 0;
        
        // Sincronizeaza clientii
        for (ClientParola cp : clientiParole) {
            try {
                clientRepository.syncFromCSV(cp.client);
                syncedClients++;
            } catch (Exception e) {
                System.err.println("Eroare la sincronizarea clientului " + cp.client.getEmail() + ": " + e.getMessage());
            }
        }
        
        // Sincronizeaza angajatii
        for (AngajatParola ap : angajatiParole) {
            try {
                angajatRepository.syncFromCSV(ap.angajat);
                syncedEmployees++;
            } catch (Exception e) {
                System.err.println("Eroare la sincronizarea angajatului " + ap.angajat.getEmail() + ": " + e.getMessage());
            }
        }
        
        System.out.println("✔ Sincronizare completa: " + syncedClients + " clienti si " + syncedEmployees + " angajati.");
    }

    // =============== OPERATII CRUD ===============
    
    /**
     * Sterge un utilizator (din CSV si BD)
     */
    public boolean stergeUtilizator(String email) {
        Object utilizator = gasesteDupaEmail(email);
        if (utilizator == null) {
            return false;
        }

        try {
            // Sterge din BD
            if (utilizator instanceof Client && clientRepository != null) {
                clientRepository.delete((Client) utilizator);
            } else if (utilizator instanceof Angajat && angajatRepository != null) {
                angajatRepository.delete((Angajat) utilizator);
            }

            // Sterge din memorie
            utilizatoriMemorie.removeIf(u -> getEmailUtilizator(u).equals(email));
            clientiParole.removeIf(cp -> cp.client.getEmail().equals(email));
            angajatiParole.removeIf(ap -> ap.angajat.getEmail().equals(email));

            // Reincarca si rescrie CSV-ul
            rescrieCsvCuDataCurenta();

            System.out.println("✔ Utilizator sters complet: " + email);
            return true;
        } catch (Exception e) {
            System.err.println("Eroare la stergerea utilizatorului: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualizeaza un utilizator (in CSV si BD)
     */
    public boolean actualizeazaUtilizator(Object utilizator) {
        String email = getEmailUtilizator(utilizator);
        if (email == null) {
            return false;
        }

        try {
            // Actualizeaza in memorie
            for (int i = 0; i < utilizatoriMemorie.size(); i++) {
                if (getEmailUtilizator(utilizatoriMemorie.get(i)).equals(email)) {
                    utilizatoriMemorie.set(i, utilizator);
                    break;
                }
            }

            // Actualizeaza in BD
            if (utilizator instanceof Client && clientRepository != null) {
                clientRepository.update((Client) utilizator);
            } else if (utilizator instanceof Angajat && angajatRepository != null) {
                // Actualizeaza in BD
                angajatRepository.update((Angajat) utilizator);
            }

            // Reincarca si rescrie CSV-ul
            rescrieCsvCuDataCurenta();
            return true;
        } catch (Exception e) {
            System.err.println("Eroare la actualizarea utilizatorului: " + e.getMessage());
            return false;
        }
    }

    // ============ PERSISTENCE METHODS ============

    private void incarcaUseriDinFisier() {
        try {
            if (!Files.exists(Paths.get(CSV_FILE_PATH))) {
                System.out.println("Fisierul users.csv nu exista. Va fi creat la primul sign up.");
                return;
            }

            try (BufferedReader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH))) {
                String linie;
                boolean firstLine = true;
                
                while ((linie = reader.readLine()) != null) {
                    // Skip header
                    if (firstLine) {
                        firstLine = false;
                        continue;
                    }
                    
                    String[] parts = linie.split(",");
                    if (parts.length >= 5) {
                        String email = parts[0].trim();
                        String parola = parts[1].trim();
                        String tip = parts[2].trim();
                        String nume = parts[3].trim();
                        String prenume = parts[4].trim();
                        
                        paroleCuEmail.put(email, parola);
                        
                        // Creeaza obiectul corespunzator
                        switch (tip) {
                            case "CLIENT":
                                Client client = new Client(nume, prenume, email, "");
                                client.setParola(parola);
                                utilizatoriMemorie.add(client);
                                break;
                            case "GHID":
                                Ghid ghid = new Ghid(nume, prenume, email, "Ghid turistic", 3000, 1);
                                ghid.setParola(parola);
                                utilizatoriMemorie.add(ghid);
                                break;
                            case "AGENT":
                                AgentVanzari agent = new AgentVanzari(nume, prenume, email, "Agent vanzari", 2500, 0.05);
                                agent.setParola(parola);
                                utilizatoriMemorie.add(agent);
                                break;
                        }
                    }
                }
                System.out.println("Au fost incarcati " + utilizatoriMemorie.size() + " utilizatori din fisier.");
                
                // Sincronizeaza automat cu BD la pornire
                syncAllWithDatabase();
            }
        } catch (IOException e) {
            System.err.println("Eroare la citirea fisierului users.csv: " + e.getMessage());
        }
    }

    private void salveazaUtilizatorInCSV(Object utilizator) {
        try {
            File file = new File(CSV_FILE_PATH);
            file.getParentFile().mkdirs();
            
            // Daca fisierul nu exista, adauga header-ul
            boolean addHeader = !file.exists();
            
            try (FileWriter writer = new FileWriter(file, true)) {
                if (addHeader) {
                    writer.write("email,parola,tip,nume,prenume\n");
                }
                String email = getEmailUtilizator(utilizator);
                String tip = getTipUtilizator(utilizator);
                String nume = getNumeUtilizator(utilizator);
                String prenume = getPrenumeUtilizator(utilizator);
                writer.write(email + "," + paroleCuEmail.get(email) + "," + tip + "," + nume + "," + prenume + "\n");
            }
        } catch (IOException e) {
            System.err.println("Eroare la salvarea utilizatorului in fisier: " + e.getMessage());
        }
    }

    private void rescrieCsvCuDataCurenta() {
        try {
            File file = new File(CSV_FILE_PATH);
            
            try (FileWriter writer = new FileWriter(file, false)) { // false = overwrite
                writer.write("email,parola,tip,nume,prenume\n");
                
                for (Object utilizator : utilizatoriMemorie) {
                    String email = getEmailUtilizator(utilizator);
                    String tip = getTipUtilizator(utilizator);
                    String nume = getNumeUtilizator(utilizator);
                    String prenume = getPrenumeUtilizator(utilizator);
                    String parola = paroleCuEmail.get(email);
                    writer.write(email + "," + parola + "," + tip + "," + nume + "," + prenume + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare la rescrierea fisierului CSV: " + e.getMessage());
        }
    }

    // ============ GETTER METHODS FOR REPOSITORIES ============

    public ClientRepository getClientRepository() {
        return clientRepository;
    }

    public AngajatRepository getAngajatRepository() {
        return angajatRepository;
    }

    private String getEmailUtilizator(Object utilizator) {
        if (utilizator instanceof Client) {
            return ((Client) utilizator).getEmail();
        } else if (utilizator instanceof Angajat) {
            return ((Angajat) utilizator).getEmail();
        } else if (utilizator instanceof Ghid) {
            return ((Ghid) utilizator).getEmail();
        } else if (utilizator instanceof AgentVanzari) {
            return ((AgentVanzari) utilizator).getEmail();
        }
        return null;
    }

    private String getNumeUtilizator(Object utilizator) {
        if (utilizator instanceof Client) {
            return ((Client) utilizator).getNume();
        } else if (utilizator instanceof Angajat) {
            return ((Angajat) utilizator).getNume();
        } else if (utilizator instanceof Ghid) {
            return ((Ghid) utilizator).getNume();
        } else if (utilizator instanceof AgentVanzari) {
            return ((AgentVanzari) utilizator).getNume();
        }
        return null;
    }

    private String getPrenumeUtilizator(Object utilizator) {
        if (utilizator instanceof Client) {
            return ((Client) utilizator).getPrenume();
        } else if (utilizator instanceof Angajat) {
            return ((Angajat) utilizator).getPrenume();
        } else if (utilizator instanceof Ghid) {
            return ((Ghid) utilizator).getPrenume();
        } else if (utilizator instanceof AgentVanzari) {
            return ((AgentVanzari) utilizator).getPrenume();
        }
        return null;
    }

    private boolean verificaCredentiale(Object utilizator, String email, String parola) {
        if (utilizator instanceof Client) {
            Client client = (Client) utilizator;
            return client.getEmail().equals(email) && client.getParola().equals(parola);
        } else if (utilizator instanceof Angajat) {
            Angajat angajat = (Angajat) utilizator;
            return angajat.getEmail().equals(email) && angajat.getParola().equals(parola);
        } else if (utilizator instanceof Ghid) {
            Ghid ghid = (Ghid) utilizator;
            return ghid.getEmail().equals(email) && ghid.getParola().equals(parola);
        } else if (utilizator instanceof AgentVanzari) {
            AgentVanzari agent = (AgentVanzari) utilizator;
            return agent.getEmail().equals(email) && agent.getParola().equals(parola);
        }
        return false;
    }

    private Object gasesteDupaEmail(String email) {
        for (Object utilizator : utilizatoriMemorie) {
            if (getEmailUtilizator(utilizator).equals(email)) {
                return utilizator;
            }
        }
        return null;
    }

    // =============== COMPATIBILITY METHODS (English names) ===============
    
    /**
     * Compatibility method for UnifiedAuthController
     */
    public Object authenticateUser(String email, String parola) {
        return autentifica(email, parola);
    }
    
    /**
     * Compatibility method for UnifiedAuthController
     */
    public boolean emailExists(String email) {
        return emailExista(email);
    }
    
    /**
     * Compatibility method for UnifiedAuthController
     */
    public String getUserType(String email) {
        Object utilizator = gasesteDupaEmail(email);
        return utilizator != null ? getTipUtilizator(utilizator) : null;
    }
    
    /**
     * Compatibility method for UnifiedAuthController
     */
    public boolean createClient(String nume, String prenume, String email, String telefon, String parola) {
        return inregistreazaClient(nume, prenume, email, telefon, parola);
    }
    
    /**
     * Compatibility method for UnifiedAuthController
     */
    public boolean createEmployee(String nume, String prenume, String email, String tipAngajat, String job, int salariuBaza, String parola, Object... extraParams) {
        return inregistreazaAngajat(nume, prenume, email, job, salariuBaza, parola, tipAngajat, extraParams);
    }
    
    /**
     * Compatibility method for UnifiedAuthController
     */
    public void logout() {
        deconecteaza();
    }
    
    /**
     * Compatibility method for UnifiedAuthController
     */
    public Object getCurrentUser() {
        return getUtilizatorCurent();
    }
} 
