package net.cosmogrp.thousing.listener;

import net.cosmogrp.thousing.user.service.UserService;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.inject.Inject;

public class PlayerInteractListener implements Listener {

    @Inject private UserService userService;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();

        if (block == null) {
            return;
        }

        if (block.getState() instanceof Sign) {
            userService.tryToClaim(event.getPlayer(), block);
        }
    }

}
