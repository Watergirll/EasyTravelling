package main.domain.enums;

public enum JobType {
    GHID("Ghid turistic", "Turism", 1.1),
    AGENT_VANZARI("Agent vanzari", "Vanzari", 1.2),
    DIRECTOR("Director", "Management", 1.5);

    private final String titlu;
    private final String departament;
    private final double bonusFactor;

    JobType(String titlu, String departament, double bonusFactor) {
        this.titlu = titlu;
        this.departament = departament;
        this.bonusFactor = bonusFactor;
    }

    public String getTitlu() {
        return titlu;
    }

    public String getDepartament() {
        return departament;
    }

    public double getBonusFactor() {
        return bonusFactor;
    }

    @Override
    public String toString() {
        return titlu + " (" + departament + ")";
    }
} 
