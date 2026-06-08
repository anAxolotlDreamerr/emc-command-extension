package io.github.anaxolotldreamerr.client.util;

import io.github.anaxolotldreamerr.client.identifier.Identifier;
import io.github.anaxolotldreamerr.client.identifier.NationIdentifier;
import io.github.anaxolotldreamerr.client.identifier.PlayerIdentifier;
import io.github.anaxolotldreamerr.client.identifier.TownIdentifier;
import io.github.anaxolotldreamerr.client.model.Favorite;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.*;

import java.util.Set;


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
        MutableComponent text = Component.literal("")
                .append(Component.literal("=".repeat(35) + "\n")
                        .withStyle(ChatFormatting.YELLOW))
                .append(Component.literal("Favorite\n")
                        .withStyle(ChatFormatting.BLUE))
                .append(Component.literal("-".repeat(35) + "\n")
                        .withStyle(ChatFormatting.WHITE))
                .append(Component.literal("Name: " + favorite.name() + "\n")
                        .withStyle(ChatFormatting.GREEN))
                .append(Component.literal("Objects:\n")
                        .withStyle(ChatFormatting.YELLOW));

        Set<? extends Identifier> objects = favorite.objects();

        if (objects == null || objects.isEmpty()) {
            text.append(Component.literal("  (empty)\n")
                    .withStyle(ChatFormatting.GRAY));
        } else {
            for (Identifier obj : objects) {
                if(obj instanceof TownIdentifier) {
                    text.append(Component.literal(
                            "  - " + obj.name() + "\n"
                    ).withStyle(Style.EMPTY.withColor(16733695)
                            .withHoverEvent(new HoverEvent.ShowText(Component.literal("/t spawn "+obj.name())))
                            .withClickEvent(new ClickEvent.RunCommand("/t spawn "+obj.name()))
                    ));
                }
                if(obj instanceof NationIdentifier) {
                    text.append(Component.literal(
                            "  - " + obj.name() + "\n"
                    ).withStyle(Style.EMPTY.withColor(16733695)
                            .withHoverEvent(new HoverEvent.ShowText(Component.literal("/n spawn "+obj.name())))
                            .withClickEvent(new ClickEvent.RunCommand("/n spawn "+obj.name()))
                    ));
                }
                if(obj instanceof PlayerIdentifier) {
                    text.append(Component.literal(
                            "  - " + obj.name() + "\n"
                    ).withStyle(Style.EMPTY.withColor(16733695)
                            .withHoverEvent(new HoverEvent.ShowText(Component.literal("/res "+obj.name())))
                            .withClickEvent(new ClickEvent.RunCommand("/res "+obj.name()))
                    ));
                }
            }
        }

        text.append(Component.literal("-".repeat(35) + "\n")
                        .withStyle(ChatFormatting.WHITE))
                .append(Component.literal("=".repeat(35))
                        .withStyle(ChatFormatting.YELLOW));

        MC.execute(() -> MC.gui.getChat().addMessage(text));
    }
}
