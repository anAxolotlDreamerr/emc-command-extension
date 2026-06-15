package io.github.anaxolotldreamerr.client.model;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
/*
---------------->x
|
|
|
|
↓
z
 */
public record Chunk(int x, int z) {
    public Chunk(int x,int z){
        this.x =x;
        this.z =z;
    }
    public static Chunk byPos(Coordinate pos){
        return new Chunk(pos.getX()>>4,pos.getZ()>>4);
    }
    public Line northEdge(){
        return new Line(new Coordinate(x*16,z*16),new Coordinate(x*16+16,z*16));
    }
    public Line westEdge(){
        return new Line(new Coordinate(x*16,z*16),new Coordinate(x*16,z*16+16));
    }
    public Line eastEdge(){
        return new Line(new Coordinate(x*16+16,z*16),new Coordinate(x*16+16,z*16+16));
    }
    public Line southEdge(){
        return new Line(new Coordinate(x*16,z*16+16),new Coordinate(x*16+16,z*16+16));
    }
    public static Set<Line> edgeOf(Set<Chunk> chunks) {
        Set<Line> edges = new HashSet<>();
        for (Chunk chunk : chunks) {
            if (!chunks.contains(chunk.north())) {
                edges.add(chunk.northEdge());
            }
            if (!chunks.contains(chunk.east())) {
                edges.add(chunk.eastEdge());
            }
            if (!chunks.contains(chunk.south())) {
                edges.add(chunk.southEdge());
            }
            if (!chunks.contains(chunk.west())) {
                edges.add(chunk.westEdge());
            }
        }
        return edges;
    }
    public boolean locateEastOf(Chunk chunk){
        return chunk.x+1==x;
    }
    public boolean locateWestOf(Chunk chunk){
        return chunk.x-1==x;
    }
    public boolean locateNorthOf(Chunk chunk){
        return chunk.z+1 == z;
    }
    public boolean locateSouthOf(Chunk chunk){
        return chunk.z-1==z;
    }
    public Chunk east(){
        return new Chunk(x+1,z);
    }
    public Chunk south(){
        return new Chunk(x,z+1);
    }
    public Chunk north(){
        return new Chunk(x,z-1);
    }
    public Chunk west(){
        return new Chunk(x-1,z);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Chunk chunk)) return false;
        return x == chunk.x && z == chunk.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }

    @Override
    public @NotNull String toString() {
        return "Chunk{" +
                "x=" + x +
                ", z=" + z +
                '}';
    }
}
