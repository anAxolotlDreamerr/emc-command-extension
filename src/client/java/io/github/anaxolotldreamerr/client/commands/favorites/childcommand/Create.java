package io.github.anaxolotldreamerr.client.commands.favorites.childcommand;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.ECommand;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorites;
import io.github.anaxolotldreamerr.client.util.ChatUtil;
import io.github.anaxolotldreamerr.client.util.FavoritesUtil;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.Collections;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

//format:<type> create [Name] ([id])
//          0     1      2       3
public class Create implements ECommand {
    private static final String NAME = "create";
    private Cache<Identifier> cache;
    private Favorites<Identifier> favorites;
    private Create(){};
    public static void register(CommandNode<FabricClientCommandSource> node){
        node.addChild(
                LiteralArgumentBuilder
                        .<FabricClientCommandSource>literal(NAME)
                        .then(argument("type", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    builder.suggest("-t");
                                    return builder.buildFuture();
                                })
                        .then(argument("name", StringArgumentType.word())
                                .then(argument("id", StringArgumentType.word()).executes((context -> {
                                    new Thread(()->{
                                        String[] temp = context.getInput().split(" ");
                                        String[] temp2 = new String[temp.length-1];
                                        for(int i = 0;i<temp2.length;i++){
                                            temp2[i] = temp[i+1];
                                        }
                                        ChatUtil.send(Component.literal(parse(temp2).execute()).withStyle(ChatFormatting.GREEN));
                                    }).start();
                                    return 0;
                                }
                                ))))
        ).build());
    }
    public static Create parse(String[] args){
        Create create = new Create();
        if(args.length < 3) throw new IllegalArgumentException("The Command is illegal!");
        create.cache = ArgumentFactory.typeArgument(args[0]).cache();
        if(args.length >=4) create.favorites = new Favorites<Identifier>(args[2]
                ,args[3]
                ,Collections.EMPTY_SET);
        else create.favorites = new Favorites<Identifier>(args[2]
                ,FavoritesUtil.maxId(create.cache.favoritesSet()).toString()
                ,Collections.EMPTY_SET);
        return create;
    }
    public String name(){return NAME;}
    @Override
    public String execute() {
        cache.addFavorites(favorites);
        return "Successfully add favorites:"+favorites.name()+" to town favorites";
    }
}
