package net.cosmogrp.thousing.user.repo;

import net.cosmogrp.thousing.user.User;
import org.bukkit.entity.Player;

public interface UserRepository {

    User getUser(Player player);

    User getOrCreate(Player player);

    void loadUser(Player player);

    void saveUser(Player player);

}
