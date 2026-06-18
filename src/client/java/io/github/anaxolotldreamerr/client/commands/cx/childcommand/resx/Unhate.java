package io.github.anaxolotldreamerr.client.commands.cx.childcommand.resx;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.ECommand;
import io.github.anaxolotldreamerr.client.commands.cx.CXArgument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.PlayerType;
import io.github.anaxolotldreamerr.client.identifier.PlayerIdentifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;

import java.util.Set;
import java.util.stream.Collectors;

public class Unhate implements ECommand {
    private Cache<PlayerIdentifier> cache = PlayerType.getInstance().cache();
    private final Command<FabricClientCommandSource> COMMAND = context -> {
        if(!Cache.getInstance(PlayerType.filePath()).favoritesSet().stream().map(Favorite::id).collect(Collectors.toSet()).contains("hate")) {
            cache.addFavorites(new Favorite<>("hate", "hate", Set.of()));
        }
        String search;
        String object = context.getArgument("object",String.class);
        try{
            search = context.getArgument("search",String.class);
        }catch (Exception e){
            search = null;
        }
        if(search == null) {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.connection.sendCommand("favorites -p remove -i hate " + object);
            }
        }else {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.connection.sendCommand("favorites -p remove -i hate "+search+" "+object);
            }
        }
        return 0;
    };
    private Unhate(){}
    @Override
    public String execute() {
        return "";
    }

    @Override
    public void register(CommandNode<FabricClientCommandSource> node) {
        node.addChild(ClientCommandManager.literal("unhate").build());
        CommandNode<FabricClientCommandSource> remove = node.getChild("unhate");
        remove.addChild(CXArgument.SEARCH.apply(COMMAND).build());
        remove.addChild(CXArgument.FROM_FAVORITE.apply(PlayerType.name(),"hate").executes(COMMAND).build());
    }
    public static void load(CommandNode<FabricClientCommandSource> node){
        new Unhate().register(node);
    }
}
