package main.domain;

import main.domain.enums.JobType;

public class AgentVanzari extends Angajat {
    private double comisionPercentage; // procentul de comision din vanzari
    private double totalVanzari; // total vanzari pentru luna curenta

    // Constructor cu JobType
    public AgentVanzari(String nume, String prenume, String email, JobType jobType, int salariuBaza, double comisionPercentage) {
        super(nume, prenume, email, jobType, salariuBaza);
        this.comisionPercentage = comisionPercentage;
        this.totalVanzari = 0.0;
    }

    // Constructor cu String pentru compatibilitate cu codul existent
    public AgentVanzari(String nume, String prenume, String email, String job, int salariuBaza, double comisionPercentage) {
        super(nume, prenume, email, JobType.AGENT_VANZARI, salariuBaza);
        this.comisionPercentage = comisionPercentage;
        this.totalVanzari = 0.0;
    }

    // Constructor implicit pentru agenti de vanzari (foloseste automat JobType.AGENT_VANZARI)
    public AgentVanzari(String nume, String prenume, String email, int salariuBaza, double comisionPercentage) {
        super(nume, prenume, email, JobType.AGENT_VANZARI, salariuBaza);
        this.comisionPercentage = comisionPercentage;
        this.totalVanzari = 0.0;
    }

    public double getComisionPercentage() {
        return comisionPercentage;
    }

    public void setComisionPercentage(double comisionPercentage) {
        this.comisionPercentage = comisionPercentage;
    }

    public double getTotalVanzari() {
        return totalVanzari;
    }

    public void setTotalVanzari(double totalVanzari) {
        this.totalVanzari = totalVanzari;
    }

    // Adauga o vanzare noua
    public void adaugaVanzare(double valoare) {
        this.totalVanzari += valoare;
    }

    // Calculeaza comisionul pe baza vanzarilor
    public double calculComision() {
        return totalVanzari * (comisionPercentage / 100.0);
    }

    // Logica: salariul de baza + comision din vanzari
    @Override
    public double calculSalariu() {
        return this.getSalariuBaza() + calculComision();
    }

    // Calculul salariului cu bonus din job
    @Override
    public double calculSalariuCuBonus() {
        double salariuCuComision = calculSalariu();
        if (getJobType() != null) {
            return salariuCuComision * getJobType().getBonusFactor();
        }
        return salariuCuComision;
    }

    // Afisare frumoasa a obiectului
    @Override
    public String toString() {
        return "Agent Vanzari: " + super.toString() + 
               " | Comision: " + comisionPercentage + "%" + 
               " | Total vanzari: " + totalVanzari + " RON";
    }
}

