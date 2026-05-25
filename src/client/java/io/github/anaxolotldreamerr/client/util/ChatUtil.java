package io.github.anaxolotldreamerr.client.util;

import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TownType;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.client.gui.Font;

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

    /*
    ====================================
    <Type> Favorite
    ------------------------------------
           name       |       id
    ...
    ------------------------------------
    ====================================
     */
    public static void showFavoriteList(Cache<Identifier> cache){
        MutableComponent text;
        if(cache.filePath().equals(TownType.filePath())) {
           text = Component
                   .literal("=".repeat(35)+"\n")
                   .withStyle(ChatFormatting.YELLOW)
                   .append(Component
                           .translatable("message.emccommandextension.favorite.list.town")
                           .append("\n")
                           .withStyle(ChatFormatting.BLUE)
                   )
                   .append(Component
                           .literal("-".repeat(35)+"\n")
                           .withStyle(ChatFormatting.WHITE))
                   .append(Component
                           .literal(
                                   padRightPixels(
                                           mc.font
                                           ," ".repeat(7)+"name"
                                           ,7*4)+"|"
                                           + " ".repeat(7)
                                           +"id\n"
                           ).withStyle(ChatFormatting.LIGHT_PURPLE)
                   )
           ;
           for(Favorite<Identifier> favorite : cache.favoritesSet()){
                text.append(Component
                        .literal(padRightPixels(mc.font
                        ,favorite.name()
                        ,17*4)+"|"+favorite.id()+"\n")
                        .withStyle(ChatFormatting.GREEN)
                );
           }
           text.append(Component
                   .literal("-".repeat(35)+"\n")
                   .withStyle(ChatFormatting.WHITE))
                   .append(Component
                           .literal("=".repeat(35))
                           .withStyle(ChatFormatting.YELLOW)
                   );
        }
        else {
            throw new IllegalArgumentException("Unknown filePath:"+cache.filePath());
        }
        mc.execute(()->mc.gui.getChat().addMessage((Component) text));
    }
    private static String padRightPixels(
            Font font,
            String text,
            int targetWidth
    ) {
        StringBuilder builder = new StringBuilder(text);
        while (font.width(builder.toString()) < targetWidth) {
            builder.append(" ");
        }
        return builder.toString();
    }
}
