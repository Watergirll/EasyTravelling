package model;

public abstract class Angajat {
    private static int nextId = 1; // ID auto-incrementat intern

    protected int idAngajat;
    protected String nume;
    protected String prenume;
    protected String email;
    protected String job; //referinta catre calsa Job => compozitie
    protected int salariuBaza;

    public Angajat(String nume, String prenume, String email, String job, int salariuBaza) {
        this.idAngajat = idAngajat + 1;
        this.idAngajat = idAngajat;
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.job = job;
        this.salariuBaza = salariuBaza;
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

    public String getJob() {
        return job;
    }

    public int getSalariuBaza() {
        return salariuBaza;
    }

    public void setJob(String job) {
        this.job = job;
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

    abstract public double calculSalariu();

    @Override
    public String toString() {
        return idAngajat + ": " + nume + " " + prenume + " | " + email + " | " + job;
    }
}
