package io.github.anaxolotldreamerr.client.commands.cx.childcommand.resx;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.ECommand;
import io.github.anaxolotldreamerr.client.commands.cx.CXArgument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.search.PlayerSearch;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.PlayerType;
import io.github.anaxolotldreamerr.client.identifier.PlayerIdentifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;

import java.util.Set;
import java.util.stream.Collectors;

public class Hate implements ECommand {
    private static Cache<PlayerIdentifier> cache = PlayerType.getInstance().cache();
    private static final Command<FabricClientCommandSource> COMMAND = context -> {
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
                Minecraft.getInstance().player.connection.sendCommand("favorites -p add -i hate " + object);
            }
        }else {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.connection.sendCommand("favorites -p add -i hate "+search+" "+object);
            }
        }
        return 0;
    };
    private Hate(){}
    @Override
    public String execute() {
        return "";
    }

    @Override
    public void register(CommandNode<FabricClientCommandSource> node) {
        node.addChild(ClientCommandManager.literal("hate").build());
        CommandNode<FabricClientCommandSource> hate = node.getChild("hate");
        hate.addChild(CXArgument.SEARCH.apply(COMMAND).build());
        hate.addChild(CXArgument.DEFAULT.apply(PlayerSearch.getName()).executes(COMMAND).build());
    }
    public static void load(CommandNode<FabricClientCommandSource> node){
        new Hate().register(node);
    }

    @Override
    public int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        return 0;
    }
}
