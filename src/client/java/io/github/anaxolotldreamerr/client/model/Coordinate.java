package io.github.anaxolotldreamerr.client.model;

import java.util.Objects;

public final class Coordinate {

    private final int x;
    private final int y;
    public Coordinate(int x, int y){
        this.x =x;
        this.y =y;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString(){
        return "Coordinate{x=%d,y=%d}".formatted(x,y);
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if (!(o instanceof Coordinate that)) return false;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
