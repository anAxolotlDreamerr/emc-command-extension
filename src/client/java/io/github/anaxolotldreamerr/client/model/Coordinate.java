package io.github.anaxolotldreamerr.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Coordinate {

    private final int x;
    private final int z;
    public Coordinate(int x, int y){
        this.x =x;
        this.z =y;
    }
    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String toString(){
        return "Coordinate{x=%d,z=%d}".formatted(x, z);
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if (!(o instanceof Coordinate that)) return false;
        return x == that.x && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }
}
