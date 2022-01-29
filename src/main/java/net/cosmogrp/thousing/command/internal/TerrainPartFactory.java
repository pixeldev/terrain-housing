package net.cosmogrp.thousing.command.internal;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.cosmogrp.thousing.terrain.Terrain;
import net.cosmogrp.thousing.terrain.repo.TerrainRepository;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TerrainPartFactory implements PartFactory {

    @Inject private TerrainRepository terrainRepository;

    @Override
    public CommandPart createPart(
            String name,
            List<? extends Annotation> annotations
    ) {
        return new ArgumentPart() {
            @Override
            public List<Terrain> parseValue(
                    CommandContext commandContext,
                    ArgumentStack argumentStack,
                    CommandPart commandPart
            ) throws ArgumentParseException {
                String terrainId = argumentStack.next();
                Terrain terrain = terrainRepository.getTerrain(terrainId);

                if (terrain == null) {
                    throw new ArgumentParseException("%translatable:terrain.not-found%");
                }

                return Collections.singletonList(terrain);
            }

            @Override
            public List<String> getSuggestions(
                    CommandContext commandContext,
                    ArgumentStack stack
            ) {
                String terrainId = !stack.hasNext() ? "" : stack.next();
                List<String> suggestions = new ArrayList<>();

                for (Terrain terrain : terrainRepository.getTerrains()) {
                    String id = terrain.getId();
                    if (id.startsWith(terrainId)) {
                        suggestions.add(id);
                    }
                }

                return suggestions;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }
}
