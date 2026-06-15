package io.github.anaxolotldreamerr.client.event;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import io.github.anaxolotldreamerr.client.model.Line;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RenderLines {
    private static Set<Line> lines = new HashSet<>();

    public static Line add(Line line){
        RenderLines.lines.add(line);
        return line;
    }
    public static Line remove(Line line){
        RenderLines.lines.remove(line);
        return line;
    }
    public static Set<Line> addAll(Set<Line> lines){
        RenderLines.lines.addAll(lines);
        return lines;
    }
    public static Set<Line> removeAll(Set<Line> lines){
        RenderLines.lines.removeAll(lines);
        return lines;
    }
    public static void clear(){
        lines.clear();
    }
    public static void register(){
        WorldRenderEvents.AFTER_ENTITIES.register(ctx -> {
            renderMap(ctx);
        });
    }
    private static void renderMap(WorldRenderContext ctx) {

    }
}
