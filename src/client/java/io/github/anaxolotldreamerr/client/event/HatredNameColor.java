package io.github.anaxolotldreamerr.client.event;

import io.github.anaxolotldreamerr.client.util.ChatUtil;
import io.github.anaxolotldreamerr.client.util.HatredManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.ChatFormatting;

import java.util.UUID;

public class HatredNameColor {

    private static final String TEAM_NAME = "hatred_red";
    public static void register(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            try {
                HatredNameColor.update();
            }catch (Exception e){
                ChatUtil.sendException(e);
            }
        });
    }
    public static void update() {
        HatredManager.refresh();
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        Scoreboard scoreboard = mc.level.getScoreboard();

        PlayerTeam team = scoreboard.getPlayerTeam("hatred_red");

        if (team == null) {
            team = scoreboard.addPlayerTeam("hatred_red");
            team.setColor(ChatFormatting.DARK_RED);
        }

        for (Player player : mc.level.players()) {

            if (player == mc.player) continue;

            UUID uuid = player.getUUID();

            if (io.github.anaxolotldreamerr.client.util.HatredManager.isHatredPlayer(uuid.toString())) {

                scoreboard.addPlayerToTeam(player.getName().getString(), team);

            } else {
                scoreboard.addPlayerToTeam(player.getName().getString(), team);
                scoreboard.removePlayerFromTeam(player.getName().getString(), team);
            }
        }
    }
}