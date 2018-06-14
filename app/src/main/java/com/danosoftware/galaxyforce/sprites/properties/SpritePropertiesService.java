package com.danosoftware.galaxyforce.sprites.properties;

import java.util.HashMap;
import java.util.Map;

public class SpritePropertiesService {

    private final Map<ISpriteIdentifier, SpriteProperties> properties;

    public SpritePropertiesService() {
        this.properties = new HashMap<>();
    }

    public SpriteProperties getProperties(ISpriteIdentifier spriteId) {
        return properties.get(spriteId);
    }
}
