package model;

//clasa Singleton implementata cu Factory
public class Director extends Angajat {
    private static Director instanta;

    private Director(String nume, String prenume, String email, String job, int salariuBaza) {
        super(nume, prenume, email, job, salariuBaza);
    }

    public static Director getInstanceDirector(String nume, String prenume, String email, String job, int salariuBaza) {
        if (instanta == null) {
            instanta = new Director(nume, prenume, email, job, salariuBaza);
        }
        return instanta;
    }

    @Override
    public double calculSalariu() {
        return (double) salariuBaza;
    }

    @Override
    public String toString() {
        return "Director: " + super.toString();
    }
}
