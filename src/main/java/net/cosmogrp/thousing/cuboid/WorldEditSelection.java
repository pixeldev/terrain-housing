package net.cosmogrp.thousing.cuboid;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.Region;
import net.cosmogrp.thousing.message.MessageHandler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorldEditSelection {

    @Inject private MessageHandler messageHandler;

    public @Nullable Cuboid createCuboidFromSelection(Player player) {
        try {
            Region selection = WorldEditPlugin
                    .getPlugin(WorldEditPlugin.class)
                    .getSession(player)
                    .getSelection();

            return Cuboid.from(selection);
        } catch (IncompleteRegionException e) {
            messageHandler.sendMessage(
                    player, "terrain.no-selection"
            );
            return null;
        }
    }

}
