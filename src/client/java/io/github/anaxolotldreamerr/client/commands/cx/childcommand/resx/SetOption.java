package io.github.anaxolotldreamerr.client.commands.cx.childcommand.resx;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import io.github.anaxolotldreamerr.client.commands.ECommand;
import io.github.anaxolotldreamerr.client.config.Config;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class SetOption implements ECommand {
    @Override
    public String execute() {
        return "";
    }

    @Override
    public void register(CommandNode<FabricClientCommandSource> node) {
        io.github.anaxolotldreamerr.client.commands.set.SetOption.add(node, java.util.Set.of(
                Config.HATRED_PLAYER_NAME_COLOR
        ));
    }

    @Override
    public int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        return 0;
    }
}
