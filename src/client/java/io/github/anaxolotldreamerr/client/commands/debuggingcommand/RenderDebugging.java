package io.github.anaxolotldreamerr.client.commands.debuggingcommand;

import com.mojang.brigadier.CommandDispatcher;
import io.github.anaxolotldreamerr.client.commands.EMCCommand;
import io.github.anaxolotldreamerr.client.model.Chunk;
import io.github.anaxolotldreamerr.client.util.ChatUtil;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

import java.util.HashSet;
import java.util.Set;

public class RenderDebugging implements EMCCommand {
    private static final RenderDebugging INSTANCE = new RenderDebugging();
    private Set<Chunk> chunks=new HashSet<>();
    private RenderDebugging(){}
    @Override
    public EMCCommand register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("renderWall").then(ClientCommandManager.literal("chunkInDistance").executes(context -> {
                    ChatUtil.send(Component.literal(Chunk.getChunksInRenderDistance().toString()));
            return 0;
        }))
                .then(ClientCommandManager.literal("chunkInFavorite").executes(context -> {
                    ChatUtil.send(Component.literal(chunks.toString()));
                    return 0;
                })));
        return this;
    }

    public void chunks(Set<Chunk> chunks) {
        this.chunks.addAll(chunks.isEmpty()?Set.of():chunks);
    }

    public static RenderDebugging getInstance(){
        return INSTANCE;
    }
}
