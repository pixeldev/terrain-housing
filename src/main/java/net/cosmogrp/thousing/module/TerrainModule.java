package net.cosmogrp.thousing.module;

import me.yushust.inject.AbstractModule;
import net.cosmogrp.thousing.terrain.repo.BinaryTerrainRepository;
import net.cosmogrp.thousing.terrain.repo.TerrainRepository;
import net.cosmogrp.thousing.terrain.service.SimpleTerrainService;
import net.cosmogrp.thousing.terrain.service.TerrainService;

public class TerrainModule extends AbstractModule {

    @Override
    public void configure() {
        bind(TerrainRepository.class).to(BinaryTerrainRepository.class).singleton();
        bind(TerrainService.class).to(SimpleTerrainService.class).singleton();
    }

}
