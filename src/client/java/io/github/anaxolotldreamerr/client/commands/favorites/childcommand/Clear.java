package io.github.anaxolotldreamerr.client.commands.favorites.childcommand;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.ECommand;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.query.NameQuery;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.query.QueryArgument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TypeArgument;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import io.github.anaxolotldreamerr.client.util.ArgumentUtil;
import io.github.anaxolotldreamerr.client.util.ChatUtil;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Clear implements ECommand {
    @Override
    public String execute() {
        return "";
    }

    @Override
    public void register(CommandNode<FabricClientCommandSource> node) {
        node.addChild(ClientCommandManager.literal("clear").build());
        CommandNode<FabricClientCommandSource> clear = node.getChild("clear");
        clear.addChild(QueryArgument.QUERY.apply(this, ArgumentUtil.emptyRequiredArgumentBuilder()).build());
        clear.addChild(QueryArgument.DEFAULT_QUERY.get().executes(this).build());
    }

    @Override
    public int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        try {
            String query;
            try {
                query = context.getArgument("query", String.class);
            }catch (Exception e){
                query= NameQuery.getName();
            }
            QueryArgument q = ArgumentFactory.queryArgument(query);
            String favorite = context.getArgument("favorite", String.class);
            TypeArgument<Identifier> type = ArgumentFactory.typeArgument(context.getArgument("type", String.class));
            Cache<Identifier> cache = type.cache();
            Map<String, ? extends Favorite<Identifier>> map = q.map(cache);
            if (!map.containsKey(favorite)) {
                throw new NullPointerException("no such favorite:" + favorite);
            }
            Favorite<Identifier> f = map.get(favorite);
            Set<Identifier> removed = f.removeAll(f.objects(), cache);
            ChatUtil.send(Component.literal(
                    removed.stream().map(Identifier::name).collect(Collectors.toSet())
                            + " from "
                            + f.name()
                            + " successfully!A total of "
                            + removed.size()).withStyle(
                    ChatFormatting.GREEN));
        }catch (Exception e){
            ChatUtil.sendException(e);
        }
        return 0;
    }
}
