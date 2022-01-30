package net.cosmogrp.thousing.user.service;

import net.cosmogrp.thousing.terrain.Terrain;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface UserService {

    void tryToClaim(Player player, Block signBlock);

    void tryToClaim(Player player, Terrain terrain);

}
