package model;

public class AgentVanzari extends Angajat {
    private double comision;

    public AgentVanzari(String nume, String prenume, String email, String job, int salariuBaza, double comision) {
        super(nume, prenume, email, job, salariuBaza);
        this.comision = comision;
    }

    @Override
    public double calculSalariu() {
        return salariuBaza + salariuBaza*this.comision;
    }


    public double getComisionTotal() {
        return comision;
    }


    @Override
    public String toString() {
        return "Agent: " + super.toString() + " | Comision: " + comision;
    }
}
