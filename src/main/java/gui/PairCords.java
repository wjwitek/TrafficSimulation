package main.java.gui;

import java.util.Objects;

public class PairCords {
    public Cords begin, end;
    public PairCords(){
        this.begin = new Cords(0,0);
        this.end = new Cords(0,0);
    }
    public PairCords(int x1, int y1, int x2, int y2){
        this.begin = new Cords(x1, y1);
        this.end = new Cords(x2, y2);
    }
    public PairCords(Cords begin, Cords end){
        this.begin = begin;
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PairCords pairCords = (PairCords) o;
        return begin.equals(pairCords.begin) && end.equals(pairCords.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(begin, end);
    }
}
