package main.java.gui;

import main.java.Program;

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
    lights_pedestrians_red       (6),
    streetN      (10),
    streetE      (11),
    streetS      (12),
    streetW      (13),
    crossingN    (16),
    crossingE    (17),
    crossingS    (18),
    crossingW    (19),
    lights_cars_red(14),
    lights_cars_green(15),
    lights_pedestrians_green (20),
    lights_pedestrians_green_blinking(21),
    underground_streetN (23),
    underground_streetS (22);


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
            case 6 -> lights_pedestrians_red;
            case 7 -> underground_street;
            case 8 -> underground_unavailable;
            case 9 -> underground_pavement;
            case 10 -> streetN;
            case 11 -> streetE;
            case 12 -> streetS;
            case 13 -> streetW;
            case 16 -> crossingN;
            case 17 -> crossingE;
            case 18 -> crossingS;
            case 19 -> crossingW;
            case 14 -> lights_cars_red;
            case 15 -> lights_cars_green;
            case 20 -> lights_pedestrians_green;
            case 21 -> lights_pedestrians_green_blinking;
            case 23 -> underground_streetN;
            case 22 -> underground_streetS;
            default -> unavailable;
        };
    }
    public static Color getColor(Subsoil s){
        if(Program.debug) {
            return switch (s) {
                case empty -> new Color(1.0f, 1.0f, 1.0f, 1.0f);
                case pavement -> new Color(0.2f, 0.6f, 0.2f, 0.7f);
                case unavailable -> new Color(0.0f, 0.0f, 0.0f, 1.0f);
                case underground -> new Color(0.7f, 0.0f, 0.7f, 0.7f);
                case underground_street -> new Color(0.3f, 0.3f, 0.3f, 0.4f);
                case underground_streetN -> new Color(0.4f, 0.3f, 0.3f, 0.4f);
                case underground_streetS -> new Color(0.3f, 0.3f, 0.4f, 0.4f);
                case underground_unavailable -> new Color(0.0f, 0.0f, 0.0f, 0.4f);
                case underground_pavement -> new Color(0.2f, 0.8f, 0.2f, 0.4f);

                case street -> new Color(0.3f, 0.3f, 0.3f, 0.7f);
                case streetN -> new Color(0.4f, 0.3f, 0.3f, 0.7f);
                case streetE -> new Color(0.3f, 0.4f, 0.3f, 0.7f);
                case streetS -> new Color(0.3f, 0.3f, 0.4f, 0.7f);
                case streetW -> new Color(0.3f, 0.3f, 0.3f, 0.9f);
                case crossing -> new Color(0.6f, 0.6f, 0.6f, 0.7f);
                case crossingN -> new Color(0.7f, 0.6f, 0.6f, 0.7f);
                case crossingE -> new Color(0.6f, 0.7f, 0.6f, 0.7f);
                case crossingS -> new Color(0.6f, 0.6f, 0.7f, 0.7f);
                case crossingW -> new Color(0.6f, 0.6f, 0.6f, 0.9f);
                case lights_pedestrians_green -> new Color(0.3f, 0.9f, 0.0f, 0.7f);
                case lights_pedestrians_green_blinking -> new Color(0.5f, 0.5f, 0.0f, 1f);

                case lights_cars_red -> new Color(0.8f, 0.0f, 0.0f, 0.7f);
                case lights_cars_green -> new Color(55, 253, 18);
                case lights_pedestrians_red -> new Color(0.8f, 0.3f, 0.0f, 0.7f);
            };
        }
        else{
            return switch (s) {
                case empty -> new Color(1.0f, 1.0f, 1.0f, 1.0f);
                case pavement -> new Color(0.2f, 0.6f, 0.2f, 0.7f);
                case unavailable -> new Color(0.0f, 0.0f, 0.0f, 1.0f);
                case underground -> new Color(0.7f, 0.0f, 0.7f, 0.7f);
                case underground_street -> new Color(0.3f, 0.3f, 0.3f, 0.4f);
                case underground_streetN -> new Color(0.4f, 0.3f, 0.3f, 0.4f);
                case underground_streetS -> new Color(0.3f, 0.3f, 0.4f, 0.4f);
                case underground_unavailable -> new Color(0.0f, 0.0f, 0.0f, 0.4f);
                case underground_pavement -> new Color(0.2f, 0.8f, 0.2f, 0.4f);

                case street, streetN, streetE, streetS, streetW -> new Color(0.3f, 0.3f, 0.3f, 0.7f);
                case crossing, crossingN, crossingE, crossingS, crossingW -> new Color(0.6f, 0.6f, 0.6f, 0.7f);
                case lights_pedestrians_green, lights_pedestrians_green_blinking -> new Color(0.3f, 0.9f, 0.0f, 0.7f);

                case lights_cars_red -> new Color(0.8f, 0.0f, 0.0f, 0.7f);
                case lights_cars_green -> new Color(55, 253, 18);
                case lights_pedestrians_red -> new Color(0.8f, 0.3f, 0.0f, 0.7f);
            };
        }
    }

    public static boolean driveable(Subsoil s){
        return (
                s==street ||
                s==streetS ||
                s==streetE ||
                s==streetN ||
                s==streetW ||

                s==crossingS ||
                s==crossingE ||
                s==crossingN ||
                s==crossingW ||

                s==underground_streetS ||
                s==underground_streetN ||

                s==lights_cars_red ||
                s==lights_cars_green
                );
    }

    public static boolean underground(Subsoil s){
        return s == underground_streetS ||
                s == underground ||
                s == underground_pavement ||
                s == underground_street ||
                s == underground_streetN ||
                s == underground_unavailable;
    }

    public static boolean crossing(Subsoil s){
        return s == crossingS ||
                s == crossing ||
                s == crossingE ||
                s == crossingN ||
                s == crossingW ||
                s == lights_pedestrians_green ||
                s == lights_pedestrians_red ||
                s == lights_pedestrians_green_blinking;
    }

    public static boolean walkable(Subsoil s){
        return (
                s==pavement ||

                s==underground ||
                s==underground_street ||
                s==underground_streetS ||
                s==underground_streetN ||
                s==underground_pavement ||
                s==underground_unavailable ||

                s==lights_pedestrians_green ||
                s==lights_pedestrians_green_blinking ||
                s==lights_pedestrians_red ||

                s==crossing ||
                s==crossingS ||
                s==crossingE ||
                s==crossingN ||
                s==crossingW
        );
    }
}
