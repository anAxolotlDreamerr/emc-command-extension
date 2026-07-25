package io.github.anaxolotldreamerr.client.commands.toggle;

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

public class Toggle implements EMCCommand {
    @Override
    public EMCCommand register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        CommandNode<FabricClientCommandSource> togglex = dispatcher.register(ClientCommandManager.literal("togglex"));
        togglex.addChild(ClientCommandManager.literal("reset").then(
                ClientCommandManager.argument("feature",StringArgumentType.word())
                        .suggests((context, builder) -> {
                            Set<String> features = new HashSet<>();
                            features.add("all");
                            features.addAll(ConfigManager.features());
                            SharedSuggestionProvider.suggest(features,builder);
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            try {
                                String feature = context.getArgument("feature",String.class);
                                if ("all".equals(feature)) {
                                    ConfigManager.reset();
                                    ChatUtil.send(Component.literal("reset all features successfully!").withStyle(ChatFormatting.GREEN));
                                } else {
                                    ConfigManager.reset(feature);
                                    ChatUtil.send(Component.literal("reset "+feature+" successfully!Now it is "+ConfigManager.get(feature)).withStyle(ChatFormatting.GREEN));
                                }
                            }catch (Exception e){
                                ChatUtil.sendException(e);
                            }
                            return 0;
                        })
        ).build());

        togglex.addChild(ClientCommandManager.literal("reload").executes(context -> {
            try {
                ConfigManager.load();
                ChatUtil.send(Component.literal("reload config successfully!").withStyle(ChatFormatting.GREEN));
            } catch (IOException e) {
                ChatUtil.sendException(e);
            }
            return 0;
        }).build());

        togglex.addChild(ClientCommandManager.literal("toggle").then(
                ClientCommandManager.argument("feature",StringArgumentType.word())
                        .suggests((context, builder) -> {
                            SharedSuggestionProvider.suggest(ConfigManager.features(),builder);
                            return builder.buildFuture();
                        }).then(ClientCommandManager.argument("value",StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    SharedSuggestionProvider.suggest(ConfigManager.getSuggestions(context.getArgument("feature",String.class)),builder);
                                    return builder.buildFuture();
                                }).executes(context -> {
                                    try {
                                        String feature = context.getArgument("feature",String.class);
                                        String value = context.getArgument("value",String.class);
                                        ConfigManager.set(feature,value);
                                        ChatUtil.send(Component.literal("toggle "+feature+" successfully!Now it is "+ConfigManager.get(feature)).withStyle(ChatFormatting.GREEN));
                                    }catch (NullPointerException e){
                                        ChatUtil.sendException(new NullPointerException("no such feature:"+context.getArgument("feature",String.class)));
                                    }catch (Exception e){
                                        ChatUtil.sendException(e);
                                    }
                                    return 0;
                                }))
        ).build());
        return this;
    }

    public static void add(CommandNode<FabricClientCommandSource> node, Set<String> features){
        node.addChild(ClientCommandManager.literal("toggle").then(
                ClientCommandManager.argument("feature",new ToggleArgument(features))
                        .suggests(((commandContext, suggestionsBuilder) -> {
                            SharedSuggestionProvider.suggest(features,suggestionsBuilder);
                            return suggestionsBuilder.buildFuture();
                        })).then(
                                ClientCommandManager.argument("value", StringArgumentType.word())
                                        .suggests((commandContext, suggestionsBuilder) -> {
                                            String feature = commandContext.getArgument("feature",String.class);
                                            SharedSuggestionProvider.suggest(
                                                    ConfigManager.getSuggestions(feature),
                                                    suggestionsBuilder
                                            );
                                            return suggestionsBuilder.buildFuture();
                                        }).executes(commandContext -> {
                                            try {
                                                ConfigManager.set(commandContext.getArgument("feature",String.class),commandContext.getArgument("value",String.class));
                                                ChatUtil.send(Component.literal("toggle "+commandContext.getArgument("feature",String.class)+" successfully!Now it is "+ConfigManager.get(commandContext.getArgument("feature",String.class))).withStyle(ChatFormatting.GREEN));
                                            }catch (Exception e){
                                                ChatUtil.sendException(e);
                                            }
                                            return 0;
                                        })
                        )
        ).build());
    }
    private static class ToggleArgument implements ArgumentType<String>{
        private Set<String> features;
        public ToggleArgument(Set<String> features){
            this.features  =Set.copyOf(features);
        }
        @Override
        public String parse(StringReader stringReader) throws CommandSyntaxException {
            String value = stringReader.readUnquotedString();
            if (!features.contains(value)) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS
                        .literalIncorrect()
                        .create(value);
            }
            return value;
        }
    }
}
