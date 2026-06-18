package io.github.anaxolotldreamerr.client.commands.cx;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.commands.EMCCommand;
import io.github.anaxolotldreamerr.client.commands.cx.childcommand.Favorites;
import io.github.anaxolotldreamerr.client.commands.cx.childcommand.tx.Border;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TownType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;

public class CTx implements EMCCommand {
    private static  CTx INSTANCE = new CTx();
    private CTx(){}
    @Override
    public EMCCommand register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        CommandNode<FabricClientCommandSource> tx = dispatcher.register(ClientCommandManager.literal("tx"));

        new Favorites(TownType.name()).register(tx);
        Border.load(tx);
        return this;
    }
    public static CTx getInstance(){
        return INSTANCE;
    }
}
