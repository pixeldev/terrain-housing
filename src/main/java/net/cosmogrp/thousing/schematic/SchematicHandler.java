package net.cosmogrp.thousing.schematic;

import net.cosmogrp.thousing.terrain.ClaimedTerrain;

public interface SchematicHandler {

    void pasteSchematic(ClaimedTerrain terrain);

    void saveSchematic(ClaimedTerrain terrain);

}
