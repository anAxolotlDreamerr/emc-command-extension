package io.github.anaxolotldreamerr.client.util;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ChatUtil {
    private static Minecraft mc = Minecraft.getInstance();
    public static void send(Component component){
        mc.execute(()-> mc.gui.getChat().addMessage(component));
    }
    public static void sendException(Exception e){
        mc.execute(()->mc.gui.getChat().addMessage(Component.literal("[ECE]"+e.getMessage()).withStyle(ChatFormatting.RED)));
    }
}
