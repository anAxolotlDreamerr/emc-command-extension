package io.github.anaxolotldreamerr.client.util;

import io.github.anaxolotldreamerr.client.commands.page.CPage;
import io.github.anaxolotldreamerr.client.commands.page.Pages;
import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.identifier.NationIdentifier;
import io.github.anaxolotldreamerr.client.identifier.PlayerIdentifier;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.*;

import java.util.*;


public class FavoritesUtil {
    private static final Minecraft MC = Minecraft.getInstance();

    public static Integer maxId(Set<? extends Favorite<?>> set){
        int max = 0;
        for(Favorite<?> favorite :set){
             if(favorite.id().matches("^[1-9]\\d*$")) {
                 Integer temp =Integer.decode(favorite.id());
                 max = Integer.max(temp,max);
             }
        }
        return max;
    }
    public static void show(Favorite<? extends Identifier> favorite){

        Component title = Component.literal("Favorite\n")
                        .withStyle(ChatFormatting.BLUE);
        Set<? extends Identifier> objects = favorite.objects();
        Pages.LimitedPage page = new Pages.LimitedPage(title,12);
        page.addLine(Component.literal("Name: " + favorite.name() + "\n")
                        .withStyle(ChatFormatting.GREEN)
                .append(Component.literal("Objects:\n")
                        .withStyle(ChatFormatting.YELLOW)));
        if (objects == null || objects.isEmpty()) {
            CPage.getInstance().pages(new Pages(new Pages.Page(title).addLine(Component.literal("  (empty)\n")
                    .withStyle(ChatFormatting.GRAY))));
        } else {
            for (Identifier obj : objects) {
                if(obj instanceof TownIdentifier) {
                    page.addLine(Component.literal(
                            "  - " + obj.name() + "\n"
                    ).withStyle(Style.EMPTY.withColor(16733695)
                            .withHoverEvent(new HoverEvent.ShowText(Component.literal("/t spawn "+obj.name())))
                            .withClickEvent(new ClickEvent.RunCommand("/t spawn "+obj.name()))
                    ));
                }
                if(obj instanceof NationIdentifier) {
                    page.addLine(Component.literal(
                            "  - " + obj.name() + "\n"
                    ).withStyle(Style.EMPTY.withColor(16733695)
                            .withHoverEvent(new HoverEvent.ShowText(Component.literal("/n spawn "+obj.name())))
                            .withClickEvent(new ClickEvent.RunCommand("/n spawn "+obj.name()))
                    ));
                }
                if(obj instanceof PlayerIdentifier) {
                    page.addLine(Component.literal(
                            "  - " + obj.name() + "\n"
                    ).withStyle(Style.EMPTY.withColor(16733695)
                            .withHoverEvent(new HoverEvent.ShowText(Component.literal("/res "+obj.name())))
                            .withClickEvent(new ClickEvent.RunCommand("/res "+obj.name()))
                    ));
                }
            }
        }
        CPage.getInstance().pages(page.generate());
        MC.player.connection.sendCommand("page 1");
    }
}
