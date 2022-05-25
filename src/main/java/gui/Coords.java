package main.java.gui;

import java.util.Objects;

public class Coords {
    public int x,y;
    public Coords(int x, int y){
        this.x=x;
        this.y=y;
    }
    public void change(int x, int y){
        this.x=x;
        this.y=y;
    }

    public Coords add(Coords other){
        return new Coords(other.x + x, other.y + y);
    }

    public Coords multiplyByConstant(int n){
        return new Coords(x * n, y * n);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coords coords = (Coords) o;
        return x == coords.x && y == coords.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
