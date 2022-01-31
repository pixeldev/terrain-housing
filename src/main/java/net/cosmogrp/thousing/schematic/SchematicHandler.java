package net.cosmogrp.thousing.schematic;

import net.cosmogrp.thousing.cuboid.Cuboid;

public interface SchematicHandler {

    void pasteSchematic(String schematic, Cuboid cuboid);

    void saveSchematic(String schematic, Cuboid cuboid);

}
