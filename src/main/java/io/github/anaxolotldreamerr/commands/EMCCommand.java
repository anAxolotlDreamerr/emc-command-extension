package io.github.anaxolotldreamerr.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public interface EMCCommand {
    EMCCommand register(CommandDispatcher<FabricClientCommandSource> dispatcher);
}
