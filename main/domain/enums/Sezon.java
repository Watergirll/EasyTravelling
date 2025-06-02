package main.domain.enums;

public enum Sezon {
    PRIMAVARA,
    VARA,
    TOAMNA,
    IARNA;

    public static Sezon fromString(String s) {
        return switch (s.trim().toLowerCase()) {
            case "primavara" -> PRIMAVARA;
            case "vara" -> VARA;
            case "toamna" -> TOAMNA;
            case "iarna" -> IARNA;
            default -> throw new IllegalArgumentException("Sezon necunoscut: " + s);
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case PRIMAVARA -> "Primavara";
            case VARA -> "Vara";
            case TOAMNA -> "Toamna";
            case IARNA -> "Iarna";
        };
    }
}

