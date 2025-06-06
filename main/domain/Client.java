package main.domain;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private static int idClient = 0;
    private String nume;
    private String prenume;
    private String email;
    private String parola;
    private String telefon;

    // Asociere 1:N: un client poate avea mai multe rezervari
    private List<Rezervare> rezervari = new ArrayList<>();

    public Client(String nume, String prenume, String email, String telefon) {
        idClient = idClient++;
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.telefon = telefon;
    }


    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
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

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public List<Rezervare> getRezervari() {
        return rezervari;
    }

    //gestionarea unei relatii bidirectionale sincronizate
    //setarea automata a clientului in Rezervare atunci cand r este asignata lui c
    public void adaugaRezervare(Rezervare r) {
        rezervari.add(r);
        r.setClient(this);
    }

    @Override
    public String toString() {
        return idClient + ": " + nume + " " + prenume + " | " + email + " | " + telefon +
                " | Nr. rezervari: " + rezervari.size();
    }
}

