package io.github.anaxolotldreamerr.client.commands;

import com.mojang.brigadier.tree.CommandNode;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

/**
 * Every ECommand implementation class must follow these conventions:
 * 1.The class is equipped with a static load method, which is the same as the register method
 * 2.You need to call the load method of that class in the register method of the CFavorites class
 * 3.This class must be a non-instantiable class
 */
public interface ECommand {
    String execute();
    void register(CommandNode<FabricClientCommandSource>node);
}
