package main.domain;

import main.domain.enums.TipTransport;

public class Transport {
    private static int idTransport = 0;
    private TipTransport tip;
    private int capacitate;
    private FirmaTransport furnizor;

    public Transport(TipTransport tip, int capacitate, FirmaTransport furnizor) {
        idTransport = idTransport + 1;
        this.tip = tip;
        this.capacitate = capacitate;
        this.furnizor = furnizor;
    }

    public int getIdTransport() {
        return idTransport;
    }

    public TipTransport getTip() {
        return tip;
    }

    public int getCapacitate() {
        return capacitate;
    }

    public FirmaTransport getFurnizor() {
        return furnizor;
    }

    @Override
    public String toString() {
        return "Transport ID: " + idTransport + " | Tip: " + tip +  " | Capacitate: " + capacitate +  " | Firma: " + furnizor;
    }
}

