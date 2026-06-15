package io.github.anaxolotldreamerr.client.commands.cx.childcommand.tx;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.ECommand;
import io.github.anaxolotldreamerr.client.commands.cx.CXArgument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.search.PlayerSearch;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.search.TownSearch;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.PlayerType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TownType;
import io.github.anaxolotldreamerr.client.identifier.PlayerIdentifier;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import io.github.anaxolotldreamerr.client.model.Town;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;

import java.util.Set;
import java.util.stream.Collectors;

public class Border implements ECommand {
    public static final String FAVORITE_NAME = "show_border";
    private static Cache<TownIdentifier> cache = TownType.getInstance().cache();
    private static final Command<FabricClientCommandSource> SHOW_BORDER =context -> {
        if(!Cache.getInstance(TownType.filePath()).favoritesSet().stream().map(Favorite::id).collect(Collectors.toSet()).contains(FAVORITE_NAME)) {
            cache.addFavorites(new Favorite<>(FAVORITE_NAME, FAVORITE_NAME, Set.of()));
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
                Minecraft.getInstance().player.connection.sendCommand("favorites -t add -i "+FAVORITE_NAME+" " + object);
            }
        }else {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.connection.sendCommand("favorites -t add -i "+FAVORITE_NAME+" "+search+" "+object);
            }
        }
        return 0;
    };
    private static final Command<FabricClientCommandSource> HIDE_BORDER =context -> {
        if(!Cache.getInstance(TownType.filePath()).favoritesSet().stream().map(Favorite::id).collect(Collectors.toSet()).contains(FAVORITE_NAME)) {
            cache.addFavorites(new Favorite<>(FAVORITE_NAME, FAVORITE_NAME, Set.of()));
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
                Minecraft.getInstance().player.connection.sendCommand("favorites -t remove -i "+FAVORITE_NAME+" " + object);
            }
        }else {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.connection.sendCommand("favorites -t remove -i "+FAVORITE_NAME+" "+search+" "+object);
            }
        }
        return 0;
    };
    @Override
    public String execute() {
        return "";
    }

    @Override
    public void register(CommandNode<FabricClientCommandSource> node) {
        node.addChild(ClientCommandManager.literal("border").build());
        CommandNode<FabricClientCommandSource> border = node.getChild("border");
        border.addChild(ClientCommandManager.literal("show").build());
        border.addChild(ClientCommandManager.literal("hide").build());
        CommandNode<FabricClientCommandSource> show = border.getChild("show");
        CommandNode<FabricClientCommandSource> hide = border.getChild("hide");
        show.addChild(CXArgument.SEARCH.apply(SHOW_BORDER).build());
        show.addChild(CXArgument.DEFAULT.apply(TownSearch.getName()).executes(SHOW_BORDER).build());
        hide.addChild(CXArgument.SEARCH.apply(HIDE_BORDER).build());
        hide.addChild(CXArgument.FROM_FAVORITE.apply(FAVORITE_NAME).executes(HIDE_BORDER).build());
    }
    public static void load(CommandNode<FabricClientCommandSource> node){new Border().register(node);}
}
