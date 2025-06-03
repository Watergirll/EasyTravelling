package main.service;

import main.domain.*;
import main.persistence.ClientRepository;
import main.persistence.AngajatRepository;
import main.service.UserService;
import java.util.List;
import java.util.ArrayList;

public class AdminService {
    private UserService userService;
    private AuditService auditService;

    public AdminService() {
        this.userService = new UserService();
        this.auditService = AuditService.getInstance();
    }
    
    /**
     * Constructor cu UserService partajat
     */
    public AdminService(UserService sharedUserService) {
        this.userService = sharedUserService;
        this.auditService = AuditService.getInstance();
    }

    // =============== MANAGEMENT REZERVARI ===============
    
    public List<Rezervare> getToateRezervarile() {
        return userService.getToateRezervarile();
    }

    public List<Rezervare> getRezervariByClient(String emailClient) {
        return userService.getRezervariByClient(emailClient);
    }

    public boolean anuleazaRezervare(String emailClient, int indexRezervare) {
        return userService.anuleazaRezervare(emailClient, indexRezervare);
    }

    // =============== MANAGEMENT CLIENTI ===============
    
    public List<Client> getToatiClientiiBD() {
        try {
            return userService.getClientRepository().loadAll();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Client cautaClientDupaEmail(String email) {
        return userService.cautaClientDupaEmail(email);
    }

    public boolean actualizeazaClient(Client clientActualizat) {
        return userService.actualizeazaClient(clientActualizat);
    }

    public boolean stergeClient(String email) {
        return userService.stergeUtilizator(email);
    }

    public boolean adaugaClient(Client clientNou) {
        return userService.createClient(clientNou.getNume(), clientNou.getPrenume(), 
                                       clientNou.getEmail(), clientNou.getTelefon(), 
                                       clientNou.getParola());
    }

    // =============== MANAGEMENT ANGAJATI ===============
    
    public List<Angajat> getToatiAngajatiiBD() {
        try {
            return userService.getAngajatRepository().loadAll();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Angajat cautaAngajatDupaEmail(String email) {
        return userService.cautaAngajatDupaEmail(email);
    }

    public boolean actualizeazaAngajat(Angajat angajatActualizat) {
        return userService.actualizeazaUtilizator(angajatActualizat);
    }

    public boolean stergeAngajat(String email) {
        return userService.stergeUtilizator(email);
    }

    public boolean adaugaAngajat(Angajat angajatNou) {
        // Determina tipul si parametrii
        String tipAngajat = "agent";
        Object[] extraParams = {5.0}; // comision default
        
        if (angajatNou instanceof Ghid) {
            tipAngajat = "ghid";
            extraParams = new Object[]{((Ghid) angajatNou).getIdLocatie()};
        } else if (angajatNou instanceof AgentVanzari) {
            tipAngajat = "agent";
            extraParams = new Object[]{((AgentVanzari) angajatNou).getComisionPercentage()};
        }
        
        return userService.createEmployee(angajatNou.getNume(), angajatNou.getPrenume(),
                                        angajatNou.getEmail(), tipAngajat, 
                                        angajatNou.getJobTitle(), 
                                        (int)angajatNou.getSalariuBaza(),
                                        angajatNou.getParola(), extraParams);
    }

    // =============== STATISTICI PENTRU ADMIN ===============
    
    public int getTotalClienti() {
        return userService.getTotalClienti();
    }

    public int getTotalAngajati() {
        return userService.getTotalAngajati();
    }

    public int getTotalRezervari() {
        return userService.getTotalRezervari();
    }

    public int getClientiActivi() {
        return userService.getClientiActivi();
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

    // =============== VALIDARI ===============
    
    public boolean emailExistaLaClienti(String email) {
        return userService.emailExistaLaClienti(email);
    }

    public boolean emailExistaLaAngajati(String email) {
        return userService.emailExistaLaAngajati(email);
    }

    // =============== OPERATII BATCH ===============
    
    public boolean salveazaToateCliente(List<Client> clienti) {
        try {
            userService.getClientRepository().saveAll(clienti);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean salveazaToatiAngajatii(List<Angajat> angajati) {
        try {
            userService.getAngajatRepository().saveAll(angajati);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // =============== GETTER PENTRU USER SERVICE ===============
    
    public UserService getUserService() {
        return userService;
    }
} 
