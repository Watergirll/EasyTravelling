package main.domain;

import main.domain.enums.JobType;
import java.util.Set;
import java.util.TreeSet;

public class Ghid extends Angajat {
    private int idLocatie; // presupunem ca fiecare ghid este asignat unei singure locatii
    private Set<String> limbiVorbite = new TreeSet<>(); // TreeSet = colectie sortata automat

    // Constructor cu JobType
    public Ghid(String nume, String prenume, String email, JobType jobType, int salariuBaza, int idLocatie) {
        super(nume, prenume, email, jobType, salariuBaza);
        this.idLocatie = idLocatie;
    }

    // Constructor cu String pentru compatibilitate cu codul existent
    public Ghid(String nume, String prenume, String email, String job, int salariuBaza, int idLocatie) {
        super(nume, prenume, email, JobType.GHID, salariuBaza);
        this.idLocatie = idLocatie;
    }

    // Constructor implicit pentru ghizi (foloseste automat JobType.GHID)
    public Ghid(String nume, String prenume, String email, int salariuBaza, int idLocatie) {
        super(nume, prenume, email, JobType.GHID, salariuBaza);
        this.idLocatie = idLocatie;
    }

    // Adauga o limba noua vorbita
    public void adaugaLimba(String limba) {
        limbiVorbite.add(limba.toLowerCase()); // folosim lowercase ca sa evitam duplicatele gen "Romana" vs "romana"
    }

    // Verifica daca ghidul vorbeste o anumita limba
    public boolean vorbeste(String limba) {
        return limbiVorbite.contains(limba.toLowerCase());
    }

    // Get pentru id-ul locatiei
    public int getIdLocatie() {
        return idLocatie;
    }

    // Get pentru lista de limbi
    public Set<String> getLimbiVorbite() {
        return limbiVorbite;
    }

    //logica: sa plateasca 10% in plus la salariu pentru fiecare limba vorbita
    @Override
    public double calculSalariu() {
        return this.getSalariuBaza() * (1 + 0.1 * limbiVorbite.size());
    }

    // Calculul salariului cu bonus din job
    @Override
    public double calculSalariuCuBonus() {
        double salariuCuLimbi = calculSalariu();
        if (getJobType() != null) {
            return salariuCuLimbi * getJobType().getBonusFactor();
        }
        return salariuCuLimbi;
    }

    // Afisare frumoasa a obiectului
    @Override
    public String toString() {
        return "Ghid: " + super.toString() + " | Locatie: " + idLocatie + " | Limbi vorbite (ordonat): " + limbiVorbite;
    }
}

