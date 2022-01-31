package net.cosmogrp.thousing.region;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.cosmogrp.thousing.cuboid.Cuboid;
import net.cosmogrp.thousing.terrain.ClaimedTerrain;
import net.cosmogrp.thousing.terrain.Terrain;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class WorldGuardRegionHandler implements RegionHandler {

    @Inject private Logger logger;

    @Override
    public void createRegion(Terrain terrain) {
        Cuboid cuboid = terrain.getCuboid();

        if (cuboid != null) {
            Region cuboidRegion = cuboid.toWorldEditCuboid();
            RegionManager regionManager = getRegionManager(cuboidRegion);

            if (regionManager == null) {
                return;
            }

            ProtectedRegion protectedRegion = new ProtectedCuboidRegion(
                    terrain.getRegionId(),
                    cuboidRegion.getMinimumPoint(),
                    cuboidRegion.getMaximumPoint()
            );

            protectedRegion.setPriority(100);

            Map<Flag<?>, Object> flags = new HashMap<>();
            flags.put(Flags.WATER_FLOW, StateFlag.State.DENY);
            flags.put(Flags.LAVA_FLOW, StateFlag.State.DENY);

            protectedRegion.setFlags(flags);
            regionManager.addRegion(protectedRegion);
        }
    }

    @Override
    public void removeRegion(Terrain terrain) {
        Cuboid cuboid = terrain.getCuboid();

        if (cuboid != null) {
            Region cuboidRegion = cuboid.toWorldEditCuboid();
            RegionManager regionManager = getRegionManager(cuboidRegion);

            if (regionManager == null) {
                return;
            }

            regionManager.removeRegion(terrain.getRegionId());
        }
    }

    @Override
    public void authorizePlayers(
            Terrain terrain,
            ClaimedTerrain claimedTerrain
    ) {
        DefaultDomain domain = getDomain(terrain);

        if (domain == null) {
            return;
        }

        domain.addPlayer(claimedTerrain.getOwnerId());

        for (UUID player : claimedTerrain.getAuthorizedPlayers()) {
            domain.addPlayer(player);
        }
    }

    @Override
    public void authorizePlayer(Terrain terrain, UUID target) {
        DefaultDomain domain = getDomain(terrain);

        if (domain == null) {
            return;
        }

        domain.addPlayer(target);
    }

    @Override
    public void disavowPlayers(
            Terrain terrain,
            ClaimedTerrain claimedTerrain
    ) {
        DefaultDomain domain = getDomain(terrain);

        if (domain == null) {
            return;
        }

        domain.removePlayer(claimedTerrain.getOwnerId());

        for (UUID player : claimedTerrain.getAuthorizedPlayers()) {
            domain.removePlayer(player);
        }
    }

    @Override
    public void disavowPlayer(Terrain terrain, UUID target) {
        DefaultDomain domain = getDomain(terrain);

        if (domain == null) {
            return;
        }

        domain.removePlayer(target);
    }

    private @Nullable DefaultDomain getDomain(Terrain terrain) {
        RegionManager regionManager = getRegionManager(
                BukkitAdapter.adapt(terrain.getWorld())
        );

        if (regionManager == null) {
            return null;
        }

        ProtectedRegion protectedRegion = regionManager
                .getRegion(terrain.getRegionId());

        if (protectedRegion == null) {
            return null;
        }

        return protectedRegion.getMembers();
    }

    private @Nullable RegionManager getRegionManager(Region region) {
        World world = region.getWorld();

        if (world == null) {
            return null;
        }

        return getRegionManager(world);
    }

    private @Nullable RegionManager getRegionManager(World world) {
        RegionManager regionManager = WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer()
                .get(world);

        if (regionManager == null) {
            logger.warning("Couldn't find region manager " +
                    "for world " + world.getName());
            return null;
        }

        return regionManager;
    }

}
