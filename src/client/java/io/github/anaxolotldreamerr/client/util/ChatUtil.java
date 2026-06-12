package io.github.anaxolotldreamerr.client.util;

import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.NationType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.PlayerType;
import io.github.anaxolotldreamerr.client.commands.favorites.argument.type.TownType;
import io.github.anaxolotldreamerr.client.commands.page.CPage;
import io.github.anaxolotldreamerr.client.commands.page.Pages;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.*;

public class ChatUtil {
    private static final Minecraft MC = Minecraft.getInstance();
    public static void send(Component component){
        if(component.getString().isEmpty()){ return; }
        MC.execute(()-> MC.gui.getChat().addMessage(Component.literal("[ECE]").withStyle(component.getStyle()).append(component)));
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
        Pages.LimitedPage page = new Pages.LimitedPage(Component
                .translatable(listKey)
                .append("\n")
                .withStyle(ChatFormatting.BLUE),14);
        page.addLine(Component.literal("     [id] -> [name]\n").withStyle(ChatFormatting.YELLOW));
        if(cache.favoritesSet() == null|| cache.favoritesSet().isEmpty()){
            page.addLine(Component.literal("(empty)\n").withStyle(ChatFormatting.WHITE));
        }
        else {
            for (Favorite<Identifier> favorite : cache.favoritesSet()) {
                String string = " " + favorite.id() + " -> " + favorite.name() + "\n";
                if (type.equals(TownType.name())) {
                    page.addLine(Component
                            .literal(string)
                            .withStyle(Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE)
                                    .withHoverEvent(new HoverEvent.ShowText(Component.literal("/favorites -t show "+favorite.name())))
                                    .withClickEvent(new ClickEvent.RunCommand("/favorites -t show "+favorite.name())))
                    );
                }
                if (type.equals(NationType.name())) {
                    page.addLine(Component
                            .literal(string)
                            .withStyle(Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE)
                                    .withHoverEvent(new HoverEvent.ShowText(Component.literal("/favorites -n show "+favorite.name())))
                                    .withClickEvent(new ClickEvent.RunCommand("/favorites -n show "+favorite.name())))
                    );
                }
                if (type.equals(PlayerType.name())) {
                    page.addLine(Component
                            .literal(string)
                            .withStyle(Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE)
                                    .withHoverEvent(new HoverEvent.ShowText(Component.literal("/favorites -p show "+favorite.name())))
                                    .withClickEvent(new ClickEvent.RunCommand("/favorites -p show "+favorite.name())))
                    );
                }
            }
        }
        CPage.getInstance().pages(page.generate());
        if (MC.player != null) {
            MC.player.connection.sendCommand("page 1");
        }
    }
}
