package main.domain;

import main.domain.enums.JobType;

//clasa Singleton implementata cu Factory
public class Director extends Angajat {
    private static Director instanta;

    private Director(String nume, String prenume, String email, String job, int salariuBaza) {
        super(nume, prenume, email, JobType.DIRECTOR, salariuBaza);
    }

    public static Director getInstanceDirector(String nume, String prenume, String email, String job, int salariuBaza) {
        if (instanta == null) {
            instanta = new Director(nume, prenume, email, job, salariuBaza);
        }
        return instanta;
    }
    
    /**
     * Setter pentru parolă (necesar pentru autentificare)
     */
    public void setParolaDirector(String parola) {
        this.setParola(parola);
    }

    @Override
    public double calculSalariu() {
        return (double) salariuBaza * 1.5; // Director bonus
    }

    @Override
    public String toString() {
        return "Director: " + super.toString();
    }
}

