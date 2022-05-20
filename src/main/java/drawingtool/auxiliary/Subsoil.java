package main.java.drawingtool.auxiliary;

public enum Subsoil {
    empty       (0),
    street      (1),
    pavement    (2),
    crossing    (3),
    unavailable (4),
    underground (5);

    private final int intValue;
    Subsoil (int x){
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
            case 5 -> underground;
            default -> unavailable;
        };
    }
}
