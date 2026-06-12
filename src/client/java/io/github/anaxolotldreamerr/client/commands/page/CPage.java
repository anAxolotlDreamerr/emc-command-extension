package io.github.anaxolotldreamerr.client.commands.page;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import io.github.anaxolotldreamerr.client.commands.EMCCommand;
import io.github.anaxolotldreamerr.client.util.ChatUtil;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class CPage implements EMCCommand {
    private final static CPage INSTANCE = new CPage();
    private Pages pages;
    private CPage(){};
    @Override
    public EMCCommand register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager
                .literal("page")
                .then(ClientCommandManager
                        .argument("pages", IntegerArgumentType.integer(1))
                        .executes(context -> {
                            int page = context.getArgument("pages", Integer.class);
                            if(pages.exist(page)) {
                                pages.show(page);
                            }else {
                                ChatUtil.sendWarning("this page isn't exist");
                            }
                            return 0;
                        })));
        return this;
    }
    public CPage pages(Pages pages){
        this.pages = pages;
        return this;
    }
    public static CPage getInstance(){return INSTANCE;}
}
