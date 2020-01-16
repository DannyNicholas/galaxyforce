package com.danosoftware.galaxyforce.waves.config.aliens.types;

import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienType;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.ExplosionConfig;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class StaticExplosionConfig extends AlienConfig {

    private final AlienCharacter alienCharacter;
    private final ExplosionConfig explosionConfig;

    @Builder
    public StaticExplosionConfig(
            @NonNull final AlienCharacter alienCharacter,
            final ExplosionConfig explosionConfig) {
        super(AlienType.STATIC_EXPLOSION);
        this.alienCharacter = alienCharacter;
        this.explosionConfig = explosionConfig;
    }
}
