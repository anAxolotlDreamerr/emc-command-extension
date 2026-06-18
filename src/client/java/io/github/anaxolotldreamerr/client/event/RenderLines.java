package io.github.anaxolotldreamerr.client.event;

import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.cx.childcommand.tx.Border;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;
import io.github.anaxolotldreamerr.client.model.Chunk;
import io.github.anaxolotldreamerr.client.model.Favorite;
import io.github.anaxolotldreamerr.client.model.Line;
import io.github.anaxolotldreamerr.client.model.Town;
import io.github.anaxolotldreamerr.client.render.RenderFilledBox;
import io.github.anaxolotldreamerr.client.util.ChatUtil;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RenderLines {
    private volatile static Set<Line> lines = new HashSet<>();
    private static final ScheduledExecutorService SCHEDULER =
            Executors.newSingleThreadScheduledExecutor();
    private static boolean isStarting = false;
    public static void register() {
        if (!isStarting) {
            isStarting = true;
            SCHEDULER.scheduleAtFixedRate(() -> {
                try {

                    RenderFilledBox render = RenderFilledBox.getInstance();
                    if (render == null) return;
                    Set<Line> linesByCamera = render.getAxisAlignedWall();
                    lines.clear();
                    Map<String, Favorite<Identifier>> map =
                            ArgumentFactory.queryArgument("-i")
                                    .map(ArgumentFactory.typeArgument("-t").cache());

                    if (map == null || !map.containsKey(Border.FAVORITE_NAME)) {
                        return;
                    }

                    Set<Identifier> set = map.get(Border.FAVORITE_NAME).objects();

                    if (set == null) {
                        return;
                    }
                    Set<Town> towns = Cache.getAllTowns(set.stream().map(TownIdentifier::byIdentifier).collect(Collectors.toSet()));
                    Set<Chunk> chunks = new HashSet<>();
                    for (Town town : towns) {
                        chunks.addAll(town.chunks());
                    }
                    lines.addAll(Chunk.edgeOf(chunks));
                    Set<Line> lineSet = new HashSet<>();
                    for(Chunk chunk : Chunk.getChunksInRenderDistance()){
                        lineSet.addAll(Chunk.edgeOf(Set.of(chunk)));
                    }
                    lines.retainAll(lineSet);
                    Set<Line> removing = linesByCamera.stream().filter(line -> !lines.contains(line)).collect(Collectors.toSet());
                    render.removeAll(removing);
                    render.addAll(lines);
                }catch (Exception e){
                    ChatUtil.sendException(e);
                }
            }, 0, 1, TimeUnit.MILLISECONDS);
        }
    }
}
