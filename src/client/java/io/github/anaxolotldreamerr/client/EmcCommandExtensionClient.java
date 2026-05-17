package io.github.anaxolotldreamerr.client;

import io.github.anaxolotldreamerr.client.commands.CommandRegistry;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmcCommandExtensionClient implements ClientModInitializer {
	public static final String MOD_ID = "emccommandextension";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitializeClient() {
		CommandRegistry.register();

		LOGGER.info("Hello Fabric");
	}
}