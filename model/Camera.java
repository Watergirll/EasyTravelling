package model;

import model.enums.TipCamera;

public class Camera {
    private TipCamera tip;
    private double pretPeNoapte;
    private int nrCamereDisponibile;

    public Camera(TipCamera tip, double pretPeNoapte, int nrCamereDisponibile) {
        this.tip = tip;
        this.pretPeNoapte = pretPeNoapte;
        this.nrCamereDisponibile = nrCamereDisponibile;
    }

    public TipCamera getTip() {
        return tip;
    }

    public double getPretPeNoapte() {
        return pretPeNoapte;
    }

    public int getNrCamereDisponibile() {
        return nrCamereDisponibile;
    }

    public void setNrCamereDisponibile(int nr) {
        this.nrCamereDisponibile = nr;
    }

    @Override
    public String toString() {
        return "Camera " + tip +
                " | Pret: " + pretPeNoapte + " RON/noapte" +
                " | Disponibile: " + nrCamereDisponibile;
    }
}
