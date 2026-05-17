package io.github.anaxolotldreamerr.client.exception;

import io.github.anaxolotldreamerr.client.util.ChatUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;

public class ExceptionRegistry {
    private static Map<Exception,Boolean> exceptions = new HashMap<>();
    public static void register(){
        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            for(Exception e : exceptions.keySet())
            if(exceptions.get(e)) ChatUtil.send(Component
                    .literal(e.getMessage())
                    .withStyle(ChatFormatting.RED)
            );
        }));
    }
    public static void turnOn(Exception e){
        ChatUtil.send(Component
                .literal(e.getMessage())
                .withStyle(ChatFormatting.RED));
        exceptions.put(e,Boolean.TRUE);
    }
    public static void turnOff(Exception e){
        ChatUtil.send(Component
                .literal("[ECE]A exception has been solved")
                .withStyle(ChatFormatting.GREEN));
        exceptions.put(e,Boolean.FALSE);
    }
}
