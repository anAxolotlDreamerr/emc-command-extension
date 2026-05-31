package io.github.anaxolotldreamerr.client;

import io.github.anaxolotldreamerr.client.cache.Cache;
import io.github.anaxolotldreamerr.client.commands.CommandRegistry;
import io.github.anaxolotldreamerr.client.network.EMCApiRequest;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmcCommandExtensionClient implements ClientModInitializer {
	public static final String MOD_ID = "assets/emccommandextension";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitializeClient() {
		CommandRegistry.register();
		EMCApiRequest.configure();
		Cache.start();
		LOGGER.info("Hello Fabric");
	}
}