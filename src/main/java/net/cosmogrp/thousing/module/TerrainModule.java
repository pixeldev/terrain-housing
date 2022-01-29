package net.cosmogrp.thousing.module;

import me.yushust.inject.AbstractModule;
import net.cosmogrp.thousing.terrain.repo.BinaryTerrainRepository;
import net.cosmogrp.thousing.terrain.repo.TerrainRepository;

public class TerrainModule extends AbstractModule {

    @Override
    public void configure() {
        bind(TerrainRepository.class).to(BinaryTerrainRepository.class).singleton();
    }

}
