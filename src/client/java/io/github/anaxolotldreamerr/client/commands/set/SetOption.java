package io.github.anaxolotldreamerr.client.commands.set;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.commands.EMCCommand;
import io.github.anaxolotldreamerr.client.config.ConfigManager;
import io.github.anaxolotldreamerr.client.util.ChatUtil;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

import java.io.IOException;
import java.util.*;

public class SetOption implements EMCCommand {
    @Override
    public EMCCommand register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        CommandNode<FabricClientCommandSource> o = dispatcher.register(ClientCommandManager.literal("options"));
        o.addChild(ClientCommandManager.literal("reset").then(
                ClientCommandManager.argument("option",StringArgumentType.word())
                        .suggests((context, builder) -> {
                            java.util.Set<String> options = new HashSet<>();
                            options.add("all");
                            options.addAll(ConfigManager.options());
                            SharedSuggestionProvider.suggest(options,builder);
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            try {
                                String option = context.getArgument("option",String.class);
                                if ("all".equals(option)) {
                                    ConfigManager.reset();
                                    ChatUtil.send(Component.literal("reset all options successfully!").withStyle(ChatFormatting.GREEN));
                                } else {
                                    ConfigManager.reset(option);
                                    ChatUtil.send(Component.literal("reset "+option+" successfully!Now it is "+ConfigManager.get(option)).withStyle(ChatFormatting.GREEN));
                                }
                            }catch (Exception e){
                                ChatUtil.sendException(e);
                            }
                            return 0;
                        })
        ).build());

        o.addChild(ClientCommandManager.literal("reload").executes(context -> {
            try {
                ConfigManager.load();
                ChatUtil.send(Component.literal("reload config successfully!").withStyle(ChatFormatting.GREEN));
            } catch (IOException e) {
                ChatUtil.sendException(e);
            }
            return 0;
        }).build());

        o.addChild(ClientCommandManager.literal("set").then(
                ClientCommandManager.argument("option",StringArgumentType.word())
                        .suggests((context, builder) -> {
                            SharedSuggestionProvider.suggest(ConfigManager.options(),builder);
                            return builder.buildFuture();
                        }).then(ClientCommandManager.argument("value",StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    SharedSuggestionProvider.suggest(ConfigManager.getSuggestions(context.getArgument("option",String.class)),builder);
                                    return builder.buildFuture();
                                }).executes(context -> {
                                    try {
                                        String option = context.getArgument("option",String.class);
                                        String value = context.getArgument("value",String.class);
                                        ConfigManager.set(option,value);
                                        ChatUtil.send(Component.literal("set "+option+" successfully!Now it is "+ConfigManager.get(option)).withStyle(ChatFormatting.GREEN));
                                    }catch (NullPointerException e){
                                        ChatUtil.sendException(new NullPointerException("no such option:"+context.getArgument("option",String.class)));
                                    }catch (Exception e){
                                        ChatUtil.sendException(e);
                                    }
                                    return 0;
                                }))
        ).build());
        return this;
    }

    public static void add(CommandNode<FabricClientCommandSource> node, java.util.Set<String> options){
        node.addChild(ClientCommandManager.literal("set").then(
                ClientCommandManager.argument("option",new setArgument(options))
                        .suggests(((commandContext, suggestionsBuilder) -> {
                            SharedSuggestionProvider.suggest(options,suggestionsBuilder);
                            return suggestionsBuilder.buildFuture();
                        })).then(
                                ClientCommandManager.argument("value", StringArgumentType.word())
                                        .suggests((commandContext, suggestionsBuilder) -> {
                                            String option = commandContext.getArgument("option",String.class);
                                            SharedSuggestionProvider.suggest(
                                                    ConfigManager.getSuggestions(option),
                                                    suggestionsBuilder
                                            );
                                            return suggestionsBuilder.buildFuture();
                                        }).executes(commandContext -> {
                                            try {
                                                ConfigManager.set(commandContext.getArgument("option",String.class),commandContext.getArgument("value",String.class));
                                                ChatUtil.send(Component.literal("set "+commandContext.getArgument("option",String.class)+" successfully!Now it is "+ConfigManager.get(commandContext.getArgument("option",String.class))).withStyle(ChatFormatting.GREEN));
                                            }catch (Exception e){
                                                ChatUtil.sendException(e);
                                            }
                                            return 0;
                                        })
                        )
        ).build());
    }
    private static class setArgument implements ArgumentType<String>{
        private java.util.Set<String> options;
        public setArgument(java.util.Set<String> options){
            this.options  = java.util.Set.copyOf(options);
        }
        @Override
        public String parse(StringReader stringReader) throws CommandSyntaxException {
            String value = stringReader.readUnquotedString();
            if (!options.contains(value)) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS
                        .literalIncorrect()
                        .create(value);
            }
            return value;
        }
    }
}
