package main.domain;

import main.domain.enums.JobType;

public abstract class Angajat {
    private static int nextId = 1; // ID auto-incrementat intern

    protected int idAngajat;
    protected String nume;
    protected String prenume;
    protected String email;
    private String parola;
    protected JobType jobType; // relatia cu enum-ul JobType
    protected int salariuBaza;

    public Angajat(String nume, String prenume, String email, JobType jobType, int salariuBaza) {
        this.idAngajat = nextId++;
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.jobType = jobType;
        this.salariuBaza = salariuBaza;
    }

    // Constructor alternativ cu String pentru compatibilitate cu codul existent
    public Angajat(String nume, String prenume, String email, String jobTitlu, int salariuBaza) {
        this(nume, prenume, email, 
             jobTitlu.toLowerCase().contains("ghid") ? JobType.GHID : JobType.AGENT_VANZARI, 
             salariuBaza);
    }

    public int getIdAngajat() {
        return idAngajat;
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public String getEmail() {
        return email;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    // Metoda pentru compatibilitate cu codul existent
    public String getJobTitle() {
        return jobType != null ? jobType.getTitlu() : "Nedefinit";
    }

    // Metoda pentru compatibilitate cu codul existent
    public void setJob(String jobTitlu) {
        this.jobType = jobTitlu.toLowerCase().contains("ghid") ? JobType.GHID : JobType.AGENT_VANZARI;
    }

    public int getSalariuBaza() {
        return salariuBaza;
    }

    public void setSalariuBaza(int salariuBaza) {
        this.salariuBaza = salariuBaza;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //in caz ca isi schimba numele de familie
    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    // Metoda pentru calculul salariului cu bonus din job
    public double calculSalariuCuBonus() {
        double salariuDeBase = calculSalariu();
        if (jobType != null) {
            return salariuDeBase * jobType.getBonusFactor();
        }
        return salariuDeBase;
    }

    abstract public double calculSalariu();

    @Override
    public String toString() {
        String jobInfo = jobType != null ? jobType.toString() : "Job nedefinit";
        return idAngajat + ": " + nume + " " + prenume + " | " + email + " | " + jobInfo;
    }
}

