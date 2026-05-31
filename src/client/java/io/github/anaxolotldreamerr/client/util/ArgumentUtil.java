package io.github.anaxolotldreamerr.client.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class ArgumentUtil {
    public static LiteralArgumentBuilder<FabricClientCommandSource> emptyLiteralArgumentBuilder(){
        return ClientCommandManager.literal("");
    }
    public static Command<FabricClientCommandSource> emptyCommand(){
        return (context)->0;
    }
    public static RequiredArgumentBuilder<FabricClientCommandSource,String> emptyRequiredArgumentBuilder(){
        return ClientCommandManager.argument("", StringArgumentType.word());
    }
}
