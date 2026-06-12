package io.github.anaxolotldreamerr.client.event;


import io.github.anaxolotldreamerr.client.commands.favorites.argument.ArgumentFactory;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.util.HatredManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HatredPlayerWarningHandler {
    private static final Map<String,Integer> IN_RANGE = new HashMap<>();
    public static void register(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            IN_RANGE.clear();
            LocalPlayer self = client.player;
            if (self == null || client.level == null) {
                return;
            }
            for(Player player : client.level.players()){
                if (player == self) {
                    continue;
                }
                if(!HatredManager.isHatredPlayer(player.getUUID().toString())){
                    continue;
                }
                int distance = (int)player.distanceTo(self);
                if(distance <100){
                    IN_RANGE.put(player.getName().getString(),distance);
                }
            }
            if(!IN_RANGE.isEmpty() && client.player != null){
                MutableComponent component = Component.empty();
                for(Map.Entry<String,Integer> entry:IN_RANGE.entrySet()){
                    component.append(" "+entry.getKey()+"("+entry.getValue()+"m)");
                }
                client.player.displayClientMessage(component.append("!!!").withStyle(ChatFormatting.DARK_RED),true);
            }
        });
    }
}
