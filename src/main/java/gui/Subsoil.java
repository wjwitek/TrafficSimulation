package main.java.gui;

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
    lights      (6);

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
            default -> unavailable;
        };
    }
}
