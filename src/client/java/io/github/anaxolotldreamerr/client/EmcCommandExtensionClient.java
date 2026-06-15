package io.github.anaxolotldreamerr.client;

import com.ibm.icu.util.LocalePriorityList;
import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.CommandRegistry;
import io.github.anaxolotldreamerr.client.event.EventRegistry;
import io.github.anaxolotldreamerr.client.network.EMCApiRequest;
import io.github.anaxolotldreamerr.client.util.ChatUtil;
import io.github.anaxolotldreamerr.client.util.Render;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import static net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.AFTER_ENTITIES;

public class EmcCommandExtensionClient implements ClientModInitializer {
	public static final String MOD_ID = "emccommandextension";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitializeClient() {
		CommandRegistry.register();
		EMCApiRequest.configure();
		Cache.start();
		EventRegistry.register();
			AFTER_ENTITIES.register(context -> {
						Minecraft MC = Minecraft.getInstance();
						LocalPlayer player =MC.player;
						if(player == null) return;
				Render.drawLine(
						context,
						new Vec3(0, player.position().y+0.5f , 0),
						new Vec3(10,player.position().y+0.5f , 0),
						player
				);
		}
		);
	}

}