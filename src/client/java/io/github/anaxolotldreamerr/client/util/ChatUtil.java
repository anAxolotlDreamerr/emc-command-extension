package io.github.anaxolotldreamerr.client.util;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ChatUtil {
    private static Minecraft mc = Minecraft.getInstance();
    public static void send(Component component){
        if(component.getString().isEmpty()){ return; }
        mc.execute(()-> mc.gui.getChat().addMessage(component));
    }
    public static void sendException(Exception e){
        mc.execute(()->mc.gui.getChat().addMessage(Component.literal("[ECE]"+e.getMessage()).withStyle(ChatFormatting.RED)));
    }
    public static void sendWarning(String warning){
        if(warning.isEmpty()){ return; }
        mc.execute(()->mc.gui.getChat().addMessage(Component.literal("[ECE]Warning:"+warning).withStyle(ChatFormatting.YELLOW)));
    }
}
