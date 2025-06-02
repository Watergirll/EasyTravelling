package main.service;

import main.domain.*;
import main.persistence.ClientRepository;
import main.persistence.AngajatRepository;
import main.service.UserService;
import java.util.List;
import java.util.ArrayList;

public class AdminService {
    private ClientRepository clientRepository;
    private AngajatRepository angajatRepository;
    private UserService userService;

    public AdminService() {
        this.clientRepository = new ClientRepository();
        this.angajatRepository = new AngajatRepository();
        this.userService = new UserService();
    }

    // =============== MANAGEMENT REZERVaRI ===============
    
    public List<Rezervare> getToateRezervarile() {
        List<Client> clienti = getToatiClientiiBD();
        List<Rezervare> toateRezervarile = new ArrayList<>();
        
        for (Client client : clienti) {
            if (client.getRezervari() != null) {
                toateRezervarile.addAll(client.getRezervari());
            }
        }
        return toateRezervarile;
    }

    public List<Rezervare> getRezervariByClient(String emailClient) {
        List<Client> clienti = getToatiClientiiBD();
        
        for (Client client : clienti) {
            if (client.getEmail().equals(emailClient)) {
                return client.getRezervari() != null ? client.getRezervari() : new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    public boolean anuleazaRezervare(String emailClient, int indexRezervare) {
        List<Client> clienti = getToatiClientiiBD();
        
        for (Client client : clienti) {
            if (client.getEmail().equals(emailClient)) {
                List<Rezervare> rezervari = client.getRezervari();
                if (rezervari != null && indexRezervare >= 0 && indexRezervare < rezervari.size()) {
                    rezervari.remove(indexRezervare);
                    salveazaClient(client);
                    return true;
                }
            }
        }
        return false;
    }

    // =============== MANAGEMENT CLIENtI ===============
    
    public List<Client> getToatiClientiiBD() {
        try {
            return clientRepository.loadAll();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Client cautaClientDupaEmail(String email) {
        List<Client> clienti = getToatiClientiiBD();
        
        for (Client client : clienti) {
            if (client.getEmail().equalsIgnoreCase(email)) {
                return client;
            }
        }
        return null;
    }

    public boolean actualizeazaClient(Client clientActualizat) {
        try {
            List<Client> clienti = getToatiClientiiBD();
            
            for (int i = 0; i < clienti.size(); i++) {
                if (clienti.get(i).getEmail().equals(clientActualizat.getEmail())) {
                    clienti.set(i, clientActualizat);
                    clientRepository.saveAll(clienti);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean stergeClient(String email) {
        try {
            List<Client> clienti = getToatiClientiiBD();
            
            for (int i = 0; i < clienti.size(); i++) {
                if (clienti.get(i).getEmail().equals(email)) {
                    clienti.remove(i);
                    clientRepository.saveAll(clienti);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean salveazaClient(Client client) {
        return actualizeazaClient(client);
    }

    // =============== MANAGEMENT ANGAJAtI ===============
    
    public List<Angajat> getToatiAngajatiiBD() {
        try {
            return angajatRepository.loadAll();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Angajat cautaAngajatDupaEmail(String email) {
        List<Angajat> angajati = getToatiAngajatiiBD();
        
        for (Angajat angajat : angajati) {
            if (angajat.getEmail().equalsIgnoreCase(email)) {
                return angajat;
            }
        }
        return null;
    }

    public boolean actualizeazaAngajat(Angajat angajatActualizat) {
        try {
            List<Angajat> angajati = getToatiAngajatiiBD();
            
            for (int i = 0; i < angajati.size(); i++) {
                if (angajati.get(i).getEmail().equals(angajatActualizat.getEmail())) {
                    angajati.set(i, angajatActualizat);
                    angajatRepository.saveAll(angajati);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean stergeAngajat(String email) {
        try {
            List<Angajat> angajati = getToatiAngajatiiBD();
            
            for (int i = 0; i < angajati.size(); i++) {
                if (angajati.get(i).getEmail().equals(email)) {
                    angajati.remove(i);
                    angajatRepository.saveAll(angajati);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean adaugaAngajat(Angajat angajatNou) {
        try {
            List<Angajat> angajati = getToatiAngajatiiBD();
            
            // Verifica daca email-ul exista deja
            for (Angajat ang : angajati) {
                if (ang.getEmail().equals(angajatNou.getEmail())) {
                    return false; // Email deja existent
                }
            }
            
            angajati.add(angajatNou);
            angajatRepository.saveAll(angajati);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // =============== STATISTICI PENTRU ADMIN ===============
    
    public int getTotalClienti() {
        return getToatiClientiiBD().size();
    }

    public int getTotalAngajati() {
        return getToatiAngajatiiBD().size();
    }

    public int getTotalRezervari() {
        return getToateRezervarile().size();
    }

    public int getClientiActivi() {
        List<Client> clienti = getToatiClientiiBD();
        int activi = 0;
        
        for (Client client : clienti) {
            if (client.getRezervari() != null && !client.getRezervari().isEmpty()) {
                activi++;
            }
        }
        return activi;
    }

    public double getSalariuMediuAngajati() {
        List<Angajat> angajati = getToatiAngajatiiBD();
        if (angajati.isEmpty()) {
            return 0.0;
        }

        double totalSalarii = 0.0;
        for (Angajat ang : angajati) {
            totalSalarii += ang.calculSalariu();
        }
        
        return totalSalarii / angajati.size();
    }

    // =============== VALIDaRI ===============
    
    public boolean emailExistaLaClienti(String email) {
        return cautaClientDupaEmail(email) != null;
    }

    public boolean emailExistaLaAngajati(String email) {
        return cautaAngajatDupaEmail(email) != null;
    }

    // =============== OPERAtII BATCH ===============
    
    public boolean salveazaToateCliente(List<Client> clienti) {
        try {
            clientRepository.saveAll(clienti);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean salveazaToatiAngajatii(List<Angajat> angajati) {
        try {
            angajatRepository.saveAll(angajati);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
} 
