package io.github.anaxolotldreamerr.client.commands.favorites.childcommand;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.ECommand;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorites;

import io.github.anaxolotldreamerr.client.util.ChatUtil;
import io.github.anaxolotldreamerr.client.util.FavoritesUtil;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;


import java.util.Collections;



//format:<type> create [Name] ([id])
//          0     1      2       3
public class Create implements ECommand {
    private static final String NAME = "create";
    private Cache<Identifier> cache;
    private Favorites<Identifier> favorites;
    private static final Command<FabricClientCommandSource> COMMAND = (context -> {
        new Thread(()->{
            String[] temp = context.getInput().split(" ");
            String[] temp2 = new String[temp.length-1];
            for(int i = 0;i<temp2.length;i++){
                temp2[i] = temp[i+1];
            }
            try{
                ChatUtil.send(Component.literal(parse(temp2).execute()).withStyle(ChatFormatting.GREEN));
            }catch (Exception e){
                ChatUtil.sendException(e);
            }
        }).start();
        return 0;
    }
    );
    private Create(){};
    public static void register(CommandNode<FabricClientCommandSource> node){

        node.addChild(
                ClientCommandManager.literal("create")
                        .then(ClientCommandManager.argument("name",StringArgumentType.word())
                                .executes(COMMAND)
                                .then(ClientCommandManager.argument("id",StringArgumentType.word())
                                        .executes(COMMAND)
                                )

                        ).build()
        );
    }
    public static Create parse(String[] args){
        Create create = new Create();
        if(args.length < 3) throw new IllegalArgumentException("The Command is illegal!");
        create.cache = ArgumentFactory.typeArgument(args[0]).cache();
        if(args.length >=4) create.favorites = new Favorites<Identifier>(args[2]
                ,args[3]
                ,Collections.EMPTY_SET);
        else create.favorites = new Favorites<Identifier>(args[2]
                ,Integer.valueOf(Integer.sum(1,FavoritesUtil.maxId(create.cache.favoritesSet()))).toString()
                ,Collections.EMPTY_SET);
        return create;
    }
    public String name(){return NAME;}
    @Override
    public String execute() {
        if(cache.addFavorites(favorites)) {
            return "Successfully add favorites:" + favorites.name() + " to town favorites";
        }else {
            return "";
        }
    }
}
