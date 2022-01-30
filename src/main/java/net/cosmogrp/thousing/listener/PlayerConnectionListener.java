package net.cosmogrp.thousing.listener;

import net.cosmogrp.thousing.user.service.UserService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.inject.Inject;

public class PlayerConnectionListener implements Listener {

    @Inject private UserService userService;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        userService.loadUser(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        userService.saveUser(event.getPlayer());
    }

}
