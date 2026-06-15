package io.github.anaxolotldreamerr.client;

import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.CommandRegistry;
import io.github.anaxolotldreamerr.client.event.EventRegistry;
import io.github.anaxolotldreamerr.client.event.RenderLines;
import io.github.anaxolotldreamerr.client.model.Chunk;
import io.github.anaxolotldreamerr.client.network.EMCApiRequest;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

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
			}
		);
	}

}