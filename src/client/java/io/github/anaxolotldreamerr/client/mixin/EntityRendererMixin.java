package io.github.anaxolotldreamerr.client.mixin;

import io.github.anaxolotldreamerr.client.config.Config;
import io.github.anaxolotldreamerr.client.config.ConfigManager;
import io.github.anaxolotldreamerr.client.util.HatredManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Inject(
            method = "getNameTag",
            at = @At("RETURN"),
            cancellable = true
    )
    private void changeNameTag(
            Entity entity,
            CallbackInfoReturnable<Component> cir
    ){
        HatredManager.refresh();
        Component original = cir.getReturnValue();

        if(entity instanceof Player player){

            if(HatredManager.isHatredPlayer(
                    player.getUUID().toString()
            )){
                cir.setReturnValue(
                        Component.literal(original.getString())
                                .withStyle(style -> style.withColor(
                                        TextColor.fromRgb((int)(ConfigManager.getLong(Config.HATRED_PLAYER_NAME_COLOR)%0x01000000L))
                                ))
                );
            }
        }
    }
}
