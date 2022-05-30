package main.java.gui;

import java.awt.*;

public enum Subsoil {
    empty       (0),
    street      (1),
    pavement    (2),
    crossing    (3),
    unavailable (4),
    underground (5),
    underground_street (7),
    underground_unavailable (8),
    underground_pavement (9),
    lights       (6),
    streetN      (10),
    streetE      (11),
    streetS      (12),
    streetW      (13),
    lights_cars  (14),
    crossingN    (15),
    crossingE    (16),
    crossingS    (17),
    crossingW    (18);


    private final int intValue;
    Subsoil(int x){
        intValue = x;
    }
    public int toInt(){
        return intValue;
    }
    public static Subsoil fromInt(int x){
        return switch(x){
            case 0 -> empty;
            case 1 -> street;
            case 2 -> pavement;
            case 3 -> crossing;
            case 4 -> unavailable;
            case 5 -> underground;
            case 6 -> lights;
            case 7 -> underground_street;
            case 8 -> underground_unavailable;
            case 9 -> underground_pavement;
            case 10 -> streetN;
            case 11 -> streetE;
            case 12 -> streetS;
            case 13 -> streetW;
            case 14 -> lights_cars;
            case 15 -> crossingN;
            case 16 -> crossingE;
            case 17 -> crossingS;
            case 18 -> crossingW;
            default -> unavailable;
        };
    }
    public static Color getColor(Subsoil s){
        return switch (s) {
            case empty -> new Color(1.0f, 1.0f, 1.0f, 1.0f);
            case pavement -> new Color(0.2f, 0.8f, 0.2f, 0.7f);
            case street -> new Color(0.3f, 0.3f, 0.3f, 0.7f);
            case crossing -> new Color(0.6f, 0.6f, 0.6f, 0.7f);
            case unavailable -> new Color(0.0f, 0.0f, 0.0f, 1.0f);
            case underground -> new Color(0.7f, 0.0f, 0.7f, 0.7f);
            case underground_street -> new Color(0.3f, 0.3f, 0.3f, 0.4f);
            case underground_unavailable -> new Color(0.0f, 0.0f, 0.0f, 0.4f);
            case underground_pavement -> new Color(0.2f, 0.8f, 0.2f, 0.4f);
            case streetN -> new Color(0.4f, 0.3f, 0.3f, 0.7f);
            case streetE -> new Color(0.3f, 0.4f, 0.3f, 0.7f);
            case streetS -> new Color(0.3f, 0.3f, 0.4f, 0.7f);
            case streetW -> new Color(0.3f, 0.3f, 0.3f, 0.9f);
            case crossingN -> new Color(0.7f, 0.6f, 0.6f, 0.7f);
            case crossingE -> new Color(0.6f, 0.7f, 0.6f, 0.7f);
            case crossingS -> new Color(0.6f, 0.6f, 0.7f, 0.7f);
            case crossingW -> new Color(0.6f, 0.6f, 0.6f, 0.9f);
            case lights_cars -> new Color(0.8f, 0.0f, 0.0f, 0.7f);
            case lights -> new Color(0.8f, 0.3f, 0.0f, 0.7f);
        };
    }
}
