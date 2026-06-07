package io.github.anaxolotldreamerr.client.commands.debuggingcommand;

import com.mojang.brigadier.CommandDispatcher;
import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.EMCCommand;
import io.github.anaxolotldreamerr.client.util.ChatUtil;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.Date;

public class CUpdate implements EMCCommand {
    private static final CUpdate INSTANCE = new CUpdate();
    @Override
    public EMCCommand register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("cache")
                        .executes(context -> {
                            Date date = new Date(System.currentTimeMillis());
                            Date last = Cache.updateDate();
                            ChatUtil.send(Component.literal("The last update was "+(int)((date.getTime()-last.getTime())/1000)+" s "+ "ago"));
                            return 0;
                        })
                .then(ClientCommandManager.literal("update")
                        .executes((context -> {
                            try {
                                Cache.update();
                                ChatUtil.send(Component.literal("successfully update").withStyle(ChatFormatting.LIGHT_PURPLE));
                            }catch (Exception e){
                                ChatUtil.sendException(e);
                            }
                            return 0;
                })).then(ClientCommandManager.literal("others").executes(commandContext -> {
                    try {
                        Cache.updateObjects();
                        ChatUtil.send(Component.literal("Successfully update").withStyle(ChatFormatting.LIGHT_PURPLE));
                    }catch (Exception e){
                        ChatUtil.sendException(e);
                    }
                    return 0;
                        }))));
        return this;
    }
    public static CUpdate getInstance(){
        return INSTANCE;
    }
}
