package io.github.anaxolotldreamerr.client.commands.cx.childcommand;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.commands.ECommand;
import io.github.anaxolotldreamerr.client.commands.cx.CXArgument;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
import io.github.anaxolotldreamerr.client.util.ArgumentUtil;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

public class Favorites implements ECommand {
    private String type;
    private final Command<FabricClientCommandSource> CREATE = context -> {
        String name= "";
        String id = "";
        try {
            name =" "+context.getArgument("name",String.class);
        }catch (Exception e){}
        try {
            id =" "+context.getArgument("id",String.class);
        }catch (Exception e){}
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.connection.sendCommand("favorites "+type+" create"+name+id);
        }
        return 0;
    };
    private final Command<FabricClientCommandSource> DELETE = context -> {
        String query = "";
        String favorite = "";
        String search = "";
        String object = "";
        try {
            query =" "+context.getArgument("query",String.class);
        }catch (Exception e){}
        try {
            favorite =" "+context.getArgument("favorite",String.class);
        }catch (Exception e){}
        try {
            search =" "+context.getArgument("search",String.class);
        }catch (Exception e){}
        try {
            object =" "+context.getArgument("object",String.class);
        }catch (Exception e){}
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.connection.sendCommand("favorites "+type+" delete"+query+favorite+search+object);
        }
        return 0;
    };
    private final Command<FabricClientCommandSource> REMOVE= context -> {
        String query = "";
        String favorite = "";
        String search = "";
        String object = "";
        try {
            query =" "+context.getArgument("query",String.class);
        }catch (Exception e){}
        try {
            favorite =" "+context.getArgument("favorite",String.class);
        }catch (Exception e){}
        try {
            search =" "+context.getArgument("search",String.class);
        }catch (Exception e){}
        try {
            object =" "+context.getArgument("object",String.class);
        }catch (Exception e){}
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.connection.sendCommand("favorites "+type+" remove"+query+favorite+search+object);
        }
        return 0;
    };
    private final Command<FabricClientCommandSource> ADD = context -> {
        String query = "";
        String favorite = "";
        String search = "";
        String object = "";
        try {
            query =" "+context.getArgument("query",String.class);
        }catch (Exception e){}
        try {
            favorite =" "+context.getArgument("favorite",String.class);
        }catch (Exception e){}
        try {
            search =" "+context.getArgument("search",String.class);
        }catch (Exception e){}
        try {
            object =" "+context.getArgument("object",String.class);
        }catch (Exception e){}
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.connection.sendCommand("favorites "+type+" add"+query+favorite+search+object);
        }
        return 0;
    };
    private final Command<FabricClientCommandSource> SHOW = context -> {
        String query = "";
        String favorite = "";
        String search = "";
        String object = "";
        try {
            query =" "+context.getArgument("query",String.class);
        }catch (Exception e){}
        try {
            favorite =" "+context.getArgument("favorite",String.class);
        }catch (Exception e){}
        try {
            search =" "+context.getArgument("search",String.class);
        }catch (Exception e){}
        try {
            object =" "+context.getArgument("object",String.class);
        }catch (Exception e){}
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.connection.sendCommand("favorites "+type+" show"+query+favorite+search+object);
        }
        return 0;
    };
    public Favorites(String type){
        this.type = type;
    }
    @Override
    public String execute() {
        return "";
    }

    @Override
    public void register(CommandNode<FabricClientCommandSource> node) {
        node.addChild(ClientCommandManager.literal("favorites").executes(context -> {
            Minecraft minecraft = Minecraft.getInstance();
            ClientPacketListener packetListener = minecraft.getConnection();
            if(packetListener == null){return 0;}
            packetListener.sendCommand("favorites "+type);
            return 0;
        }).build());
        CommandNode<FabricClientCommandSource> favorites = node.getChild("favorites");
        favorites.addChild(ClientCommandManager.literal("create").build());
        favorites.addChild(ClientCommandManager.literal("delete").build());
        favorites.addChild(ClientCommandManager.literal("remove").build());
        favorites.addChild(ClientCommandManager.literal("add").build());
        favorites.addChild(ClientCommandManager.literal("show").build());
        CommandNode<FabricClientCommandSource> create = favorites.getChild("create");
        CommandNode<FabricClientCommandSource> delete = favorites.getChild("delete");
        CommandNode<FabricClientCommandSource> remove = favorites.getChild("remove");
        CommandNode<FabricClientCommandSource> add =favorites.getChild("add");
        CommandNode<FabricClientCommandSource> show = favorites.getChild("show");

        create.addChild(ClientCommandManager.argument("name",StringArgumentType.word())
                .executes(CREATE)
                .then(ClientCommandManager.argument("id",StringArgumentType.word())
                        .executes(CREATE)
                ).build());

        delete.addChild(CXArgument.QUERY.apply(type,DELETE,ArgumentUtil.emptyRequiredArgumentBuilder()).build());
        delete.addChild(CXArgument.DEFAULT_QUERY.apply(type).executes(DELETE).build());

        add.addChild(CXArgument.QUERY.apply(type,ArgumentUtil.emptyCommand(),CXArgument.SEARCH.apply(ADD)).build());
        add.addChild(CXArgument.QUERY.apply(type,ArgumentUtil.emptyCommand(),CXArgument.DEFAULT
                .apply(ArgumentFactory.defaultSearchArgument(type).name()).executes(ADD)).build());
        add.addChild(CXArgument.DEFAULT_QUERY.apply(type).then(CXArgument.SEARCH.apply(ADD)).build());
        add.addChild(CXArgument.DEFAULT_QUERY.apply(type).then(CXArgument.DEFAULT
                .apply(ArgumentFactory.defaultSearchArgument(type).name()).executes(ADD)).build());

        remove.addChild(CXArgument.QUERY.apply(type,ArgumentUtil.emptyCommand(),CXArgument.SEARCH.apply(REMOVE)).build());
        remove.addChild(CXArgument.QUERY.apply(type,ArgumentUtil.emptyCommand(),CXArgument.FROM_FAVORITE
                .apply(ArgumentFactory.defaultSearchArgument(type).name()).executes(REMOVE)).build());
        remove.addChild(CXArgument.DEFAULT_QUERY.apply(type).then(CXArgument.SEARCH.apply(REMOVE)).build());
        remove.addChild(CXArgument.DEFAULT_QUERY.apply(type).then(CXArgument.FROM_FAVORITE
                .apply(ArgumentFactory.defaultSearchArgument(type).name()).executes(REMOVE)).build());

        show.addChild(CXArgument.QUERY.apply(type,SHOW,ArgumentUtil.emptyRequiredArgumentBuilder()).build());
        show.addChild(CXArgument.DEFAULT_QUERY.apply(type).executes(SHOW).build());
    }
}
