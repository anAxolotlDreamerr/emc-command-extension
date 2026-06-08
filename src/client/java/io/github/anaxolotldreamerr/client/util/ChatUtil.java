package io.github.anaxolotldreamerr.client.util;

import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.NationType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.PlayerType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TownType;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.*;
import net.minecraft.client.gui.Font;

public class ChatUtil {
    private static final Minecraft MC = Minecraft.getInstance();
    public static void send(Component component){
        if(component.getString().isEmpty()){ return; }
        MC.execute(()-> MC.gui.getChat().addMessage(Component.literal("[EMC]").withStyle(component.getStyle()).append(component)));
    }
    public static void sendException(Exception e){
        MC.execute(()-> MC.gui.getChat().addMessage(Component.literal("[ECE]"+e.getMessage()).withStyle(ChatFormatting.RED)));
    }
    public static void sendWarning(String warning){
        if(warning.isEmpty()){ return; }
        MC.execute(()-> MC.gui.getChat().addMessage(Component.literal("[ECE]Warning:"+warning).withStyle(ChatFormatting.YELLOW)));
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
        if(cache.filePath().equals(TownType.filePath())){
            showFavoriteList(cache,"message.emccommandextension.favorite.list.town",TownType.name());
        }else if(cache.filePath().equals(NationType.filePath())){
            showFavoriteList(cache,"message.emccommandextension.favorite.list.nation",NationType.name());
        }else if(cache.filePath().equals(PlayerType.filePath())){
            showFavoriteList(cache,"message.emccommandextension.favorite.list.player",PlayerType.name());
        }
        else {
            throw new IllegalArgumentException("Unknown filePath:"+cache.filePath());
        }

    }
    private static void showFavoriteList(Cache<Identifier> cache,String listKey,String type){
        MutableComponent text;
            text = Component
                    .literal("=".repeat(35)+"\n")
                    .withStyle(ChatFormatting.YELLOW)
                    .append(Component
                            .translatable(listKey)
                            .append("\n")
                            .withStyle(ChatFormatting.BLUE)
                    )
                    .append(Component
                            .literal("-".repeat(35)+"\n")
                            .withStyle(ChatFormatting.WHITE))
                    .append(Component
                            .literal(
                                    padRightPixels(
                                            MC.font
                                            ,"      [name]"
                                            ,17*4)+"|"
                                            + " ".repeat(7)
                                            +"     [id]\n"
                            ).withStyle(ChatFormatting.LIGHT_PURPLE)
                    );
            if(cache.favoritesSet() == null|| cache.favoritesSet().isEmpty()){
                text.append(Component.literal("(empty)"));
            }
            else {
                for (Favorite<Identifier> favorite : cache.favoritesSet()) {
                    if (type.equals(TownType.name())) {
                        text.append(Component
                                .literal(padRightPixels(MC.font
                                        , favorite.name()
                                        , 17 * 4) + "|     " + favorite.id() + "\n")
                                .withStyle(Style.EMPTY.withColor(5635925)
                                        .withHoverEvent(new HoverEvent.ShowText(Component.literal("/favorites -t show "+favorite.name())))
                                        .withClickEvent(new ClickEvent.RunCommand("/favorites -t show "+favorite.name())))
                        );
                    }
                    if (type.equals(NationType.name())) {
                        text.append(Component
                                .literal(padRightPixels(MC.font
                                        , favorite.name()
                                        , 17 * 4) + "|     " + favorite.id() + "\n")
                                .withStyle(Style.EMPTY.withColor(5635925)
                                        .withHoverEvent(new HoverEvent.ShowText(Component.literal("/favorites -n show "+favorite.name())))
                                        .withClickEvent(new ClickEvent.RunCommand("/favorites -n show "+favorite.name())))
                        );
                    }
                    if (type.equals(PlayerType.name())) {
                        text.append(Component
                                .literal(padRightPixels(MC.font
                                        , favorite.name()
                                        , 17 * 4) + "|     " + favorite.id() + "\n")
                                .withStyle(Style.EMPTY.withColor(5635925)
                                        .withHoverEvent(new HoverEvent.ShowText(Component.literal("/favorites -p show "+favorite.name())))
                                        .withClickEvent(new ClickEvent.RunCommand("/favorites -p show "+favorite.name())))
                        );
                    }
                }
            }
            text.append(Component
                            .literal("-".repeat(35)+"\n")
                            .withStyle(ChatFormatting.WHITE))
                    .append(Component
                            .literal("=".repeat(35))
                            .withStyle(ChatFormatting.YELLOW)
                    );
        MC.execute(()-> MC.gui.getChat().addMessage(text));
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
