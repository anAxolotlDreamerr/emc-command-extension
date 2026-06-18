package io.github.anaxolotldreamerr.client.model;

import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SectionOcclusionGraph;
import net.minecraft.client.renderer.ViewArea;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.SectionPos;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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

    public static Set<Chunk> getChunksInRenderDistance() {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        LocalPlayer player = mc.player;

        if (level == null || player == null) return Set.of();

        int renderDist = mc.options.getEffectiveRenderDistance();

        // 当前玩家所在 chunk
        int playerChunkX = player.chunkPosition().x;
        int playerChunkZ = player.chunkPosition().z;

        Set<Chunk> chunks = new HashSet<>();

        int r = renderDist;

        for (int dx = -r; dx <= r; dx++) {
            for (int dz = -r; dz <= r; dz++) {

                // ⭐ 圆形裁剪（关键）
                if (dx * dx + dz * dz > r * r) continue;

                int cx = playerChunkX + dx;
                int cz = playerChunkZ + dz;

                chunks.add(new Chunk(cx, cz));
            }
        }
        return chunks;
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
