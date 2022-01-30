package net.cosmogrp.thousing.user.repo;

import net.cosmogrp.thousing.user.User;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

public class BinaryUserRepository implements UserRepository {

    @Inject private Executor executor;
    @Inject private Logger logger;

    private final Map<UUID, User> users;
    private final File usersFolder;

    public @Inject BinaryUserRepository(Plugin plugin) throws IOException {
        this.users = new HashMap<>();
        this.usersFolder = new File(plugin.getDataFolder(), "users");

        if (!usersFolder.exists()) {
            if (!usersFolder.mkdirs()) {
                throw new IOException("Could not create users folder");
            }
        }
    }

    @Override
    public User getUser(Player player) {
        return users.get(player.getUniqueId());
    }

    @Override
    public User getOrCreate(Player player) {
        User user = getUser(player);

        if (user != null) {
            return user;
        } else {
            user = User.from(player);
        }

        users.put(player.getUniqueId(), user);
        return user;
    }

    @Override
    public void loadUser(Player player) {
        UUID playerId = player.getUniqueId();

        executor.execute(() -> {
            File file = makeUserFile(playerId, false);

            if (file == null) {
                return;
            }

            try (DataInputStream input = new DataInputStream(new FileInputStream(file))) {
                User user = User.from(input);
                users.put(playerId, user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void saveUser(Player player) {
        UUID playerId = player.getUniqueId();

        executor.execute(() -> {
            File file = makeUserFile(playerId, true);

            if (file == null) {
                return;
            }

            User user = users.get(playerId);

            if (user == null) {
                return;
            }

            try (DataOutputStream input = new DataOutputStream(new FileOutputStream(file))) {
                user.write(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private @Nullable File makeUserFile(UUID playerId, boolean create) {
        File file = new File(
                usersFolder,
                playerId + ".bin"
        );

        boolean created = file.exists();

        if (create && !created) {
            try {
                created = file.createNewFile();
            } catch (IOException e) {
                logger.warning("Could not create user file for "
                        + playerId);
            }
        }

        if (!created) {
            logger.warning("Could not create user file for "
                    + playerId);
            return null;
        }

        return file;
    }
}
