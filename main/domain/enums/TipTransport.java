package main.domain.enums;

public enum TipTransport {
    AVION,
    AUTOCAR,
    VAPOR;

    public static TipTransport fromString(String s) {
        return switch (s.trim().toLowerCase()) {
            case "avion" -> AVION;
            case "autocar" -> AUTOCAR;
            case "vapor" -> VAPOR;
            default -> throw new IllegalArgumentException("Transport necunoscut: " + s);
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case AUTOCAR -> "Autocar";
            case AVION -> "Avion";
            case VAPOR -> "Vapor";
        };
    }
}

