package net.cosmogrp.thousing.user.service;

import net.cosmogrp.thousing.terrain.Terrain;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface UserService {

    void tryToClaim(Player player, Block signBlock);

    void authorizePlayer(Player sender, OfflinePlayer target);

    void disavowPlayer(Player sender, OfflinePlayer target);

    void tryToClaim(Player player, Terrain terrain);

    void loadUser(Player player);

    void saveUser(Player player);

    void saveAllUsers();

}
