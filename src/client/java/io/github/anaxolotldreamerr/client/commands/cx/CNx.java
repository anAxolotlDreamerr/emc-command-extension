package io.github.anaxolotldreamerr.client.commands.cx;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.commands.EMCCommand;
import io.github.anaxolotldreamerr.client.commands.cx.childcommand.Favorites;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.NationType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TownType;
import io.github.anaxolotldreamerr.client.model.Nation;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;

public class CNx implements EMCCommand {
    private static  CNx INSTANCE = new CNx();
    private CNx(){}
    @Override
    public EMCCommand register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        CommandNode<FabricClientCommandSource> nx = dispatcher.register(ClientCommandManager.literal("nx").executes(context -> {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.getConnection().sendCommand("favorites -n");
            return 0;
        }));

        new Favorites(NationType.name()).register(nx);
        return this;
    }
    public static CNx getInstance(){
        return INSTANCE;
    }
}
