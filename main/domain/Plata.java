package main.domain;

import java.time.LocalDate;

public class Plata {
    private int nrPlata;
    private double suma;
    private LocalDate dataPlata;
    private Rezervare rezervare;

    public Plata(int nrPlata, double suma, LocalDate dataPlata, Rezervare rezervare) {
        this.nrPlata = nrPlata;
        this.suma = suma;
        this.dataPlata = dataPlata;
        this.rezervare = rezervare;
    }

    public int getNrPlata() {
        return nrPlata;
    }

    public double getSuma() {
        return suma;
    }

    public LocalDate getDataPlata() {
        return dataPlata;
    }

    public Rezervare getRezervare() {
        return rezervare;
    }

    //nu folosesc o relatie 1:1 bidirectionala sincronizata cu Rezervare pentru ca voi accesa
    //mereu campul plata dintr un singur sens: Rezervare -> Plata
    //deci nu am de ce sa am un modificator al rezervarii aferente platii

    @Override
    public String toString() {
        return "Plata #" + nrPlata + " | Suma: " + suma + " RON | Data: " + dataPlata +
                " | Rezervare ID: " + (rezervare != null ? rezervare.getIdRezervare() : "N/A");
    }
}

