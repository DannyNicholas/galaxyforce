package com.danosoftware.galaxyforce.waves.utilities;

import static com.danosoftware.galaxyforce.waves.utilities.WaveFactoryHelper.DOWNWARDS;
import static com.danosoftware.galaxyforce.waves.utilities.WaveFactoryHelper.HALF_PI;
import static com.danosoftware.galaxyforce.waves.utilities.WaveFactoryHelper.NO_POWER_UPS;
import static com.danosoftware.galaxyforce.waves.utilities.WaveFactoryHelper.QUARTER_PI;

import com.danosoftware.galaxyforce.enumerations.AlienMissileCharacter;
import com.danosoftware.galaxyforce.enumerations.AlienMissileSpeed;
import com.danosoftware.galaxyforce.enumerations.AlienMissileType;
import com.danosoftware.galaxyforce.enumerations.AlienSpeed;
import com.danosoftware.galaxyforce.enumerations.PowerUpType;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.waves.AlienCharacter;
import com.danosoftware.galaxyforce.waves.AlienCharacterWithEnergy;
import com.danosoftware.galaxyforce.waves.ChangingAlienCharacter;
import com.danosoftware.galaxyforce.waves.config.aliens.AlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.DelayedFollowerExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.ExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.MultiExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.NormalExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.exploding.SpawningExplosionConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileFiringConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.missiles.MissileMultiFiringConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawnOnDemandConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spawning.SpawningAlienConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.NoSpinningConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningBySpeedConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.spinning.SpinningFixedAngularConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.BoundariesConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.ChangingConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DirectionalDestroyableConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DirectionalResettableConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DriftingConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.DriftingConfig.DriftingConfigBuilder;
import com.danosoftware.galaxyforce.waves.config.aliens.types.ExplodingConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.FollowableHunterConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.FollowerConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.HunterConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.PathConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.SplitterConfig;
import com.danosoftware.galaxyforce.waves.config.aliens.types.StaticConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlienConfigBuilder {

  /**
   * Create missile config for character
   */
  private static MissileConfig missileConfig(
      AlienCharacter character,
      AlienMissileSpeed speed,
      Float missileFrequency) {

    switch (character) {
      // downwards laser missile
      case NULL:
      case BLOCK:
      case PINKO:
      case PISTON:
      case PURPLE_MEANIE:
      case PAD:
      case HOPPER:
      case AMOEBA:
      case CHEEKY:
      case FIGHTER:
      case RIPLEY:
      case WALKER:
      case ALL_SEEING_EYE:
      case FROGGER:
      case WHIRLPOOL:
      case FISH:
      case LEMMING:
      case SQUEEZE_BOX:
      case YELLOW_BEARD:
      case PILOT:
      case ARACNOID:
      case CRAB:
      case DINO:
      case FRISBIE:
      case OCTOPUS:
      case MINION:
      case SPINNER_GREEN:
      case SPINNER_PULSE_GREEN:
      case MOLECULE:
      case MOLECULE_MINI:
      case BIG_BOSS:
      case ASTEROID:
      case ASTEROID_MINI:
      case DRAGON_BODY:
      case BABY_DRAGON_BODY:
      case SKULL:
      case DROID:
      case INSECT:
      case GOBBY:
      case BOOK:
      case ZOGG:
      case BOMB:
      case CLOUD:
      case BOUNCER:
      case DROOPY:
      case BAD_CAT:
      case TELLY:
      case HELMET:
      case EGG:
      case BARRIER:
      case CYCLONE:
      case GREMLIN:
      case PINCER:
      case SPARKLE:
      case SPECTATOR:
      case SQUASHER:
      case TINY_DANCER:
      case WILD_STYLE:
      case CONFUSER:
      case SAUCER:
      case JUMPER:
      case BUBBLE:
      case BUSHEY:
      case CHOPPY:
      case CLOWN:
      case CROSSER:
      case FROG_KING:
      case GREEN_BOY:
      case JELLYFISH:
      case SWEEP:
        return MissileFiringConfig
            .builder()
            .missileType(AlienMissileType.DOWNWARDS)
            .missileCharacter(AlienMissileCharacter.LASER)
            .missileSpeed(speed)
            .missileFrequency(missileFrequency)
            .build();
      // downwards lightning missile
      case CIRCUIT:
        return MissileFiringConfig
            .builder()
            .missileType(AlienMissileType.DOWNWARDS)
            .missileCharacter(AlienMissileCharacter.LIGHTNING)
            .missileSpeed(speed)
            .missileFrequency(missileFrequency)
            .build();
      // rotated downwards laser missile (will not fire upwards)
      case GHOST:
      case FOXY:
      case FOXY_SHRINKING_LARGE:
      case FOXY_SHRINKING_MEDIUM:
      case FOXY_SHRINKING_NORMAL:
      case FOXY_SHRINKING_SMALL:
      case CHARLIE:
      case DEVIL:
      case ZOOM:
        return MissileFiringConfig
            .builder()
            .missileType(AlienMissileType.ROTATED_DOWNWARDS)
            .missileCharacter(AlienMissileCharacter.LASER)
            .missileSpeed(speed)
            .missileFrequency(missileFrequency)
            .build();
      // rotated laser missile (any direction)
      case BATTLE_DROID:
      case JOKER:
      case BEAR:
      case ROTATOR:
      case INSECT_MOTHERSHIP:
      case SPACE_STATION:
        return MissileFiringConfig
            .builder()
            .missileType(AlienMissileType.ROTATED)
            .missileCharacter(AlienMissileCharacter.LASER)
            .missileSpeed(speed)
            .missileFrequency(missileFrequency)
            .build();
      // rotated fireball missile
      case DRAGON_HEAD:
      case BABY_DRAGON_HEAD:
      case BATTY:
        return MissileFiringConfig
            .builder()
            .missileType(AlienMissileType.ROTATED)
            .missileCharacter(AlienMissileCharacter.FIREBALL)
            .missileSpeed(speed)
            .missileFrequency(missileFrequency)
            .build();
      // spray missiles
      case LADY_BIRD:
      case SPIRAL:
        return MissileFiringConfig
            .builder()
            .missileType(AlienMissileType.SPRAY)
            .missileCharacter(AlienMissileCharacter.LASER)
            .missileSpeed(speed)
            .missileFrequency(missileFrequency)
            .build();
      // guided missiles
      case SMOKEY:
        return MissileFiringConfig
            .builder()
            .missileType(AlienMissileType.GUIDED)
            .missileCharacter(AlienMissileCharacter.FIREBALL)
            .missileSpeed(speed)
            .missileFrequency(missileFrequency)
            .build();
      default:
        throw new GalaxyForceException(
            "Can not create missile config for unknown character: " + character);
    }
  }

  /**
   * Create variable speed spinning config for character
   */
  private static SpinningConfig spinningConfigBySpeed(
      AlienCharacter character) {

    switch (character) {

      case SPINNER_PULSE_GREEN:
      case MOLECULE:
      case MOLECULE_MINI:
      case ASTEROID:
      case ASTEROID_MINI:
      case SPACE_STATION:
        return SpinningBySpeedConfig
            .builder()
            .build();
      default:
        return NoSpinningConfig
            .builder()
            .build();
    }
  }

  /**
   * Create fixed speed spinning config for character
   */
  private static SpinningConfig spinningConfigFixed(
      AlienCharacter character) {

    switch (character) {

      case SPINNER_PULSE_GREEN:
      case ASTEROID:
      case ASTEROID_MINI:
        return SpinningFixedAngularConfig
            .builder()
            .angularSpeed(70)
            .build();
      case MOLECULE:
      case MOLECULE_MINI:
        return SpinningFixedAngularConfig
            .builder()
            .angularSpeed(280)
            .build();
      default:
        return NoSpinningConfig
            .builder()
            .build();
    }
  }

  /**
   * Create explosion config for character
   */
  private static ExplosionConfig explosionConfig(
      AlienCharacter character) {

    return explosionConfig(character, AlienSpeed.MEDIUM, -HALF_PI, false);
  }

  /**
   * Create explosion config for character
   */
  private static ExplosionConfig explosionConfig(
      AlienCharacter character,
      AlienSpeed speed) {

    return explosionConfig(character, speed, -HALF_PI, false);
  }

  /**
   * Create explosion config for character and speed
   */
  private static ExplosionConfig explosionConfig(
      AlienCharacter character,
      AlienSpeed speed,
      float angle,
      boolean isDrifting) {

    switch (character) {
      case CLOUD:
        return MultiExplosionConfig.builder()
            .numberOfExplosions(10)
            .maximumExplosionStartTime(0.5f)
            .build();
      case MOLECULE:
        return splitFourWayOnExplodeConfig(
            speed,
            AlienCharacter.MOLECULE_MINI);
      case ASTEROID:
        return isDrifting ?
            splitOnExplodeDriftingConfig(speed, AlienCharacter.ASTEROID_MINI, angle) :
            splitOnExplodeConfig(speed, AlienCharacter.ASTEROID_MINI, angle);
      case DRAGON_HEAD:
        return MultiExplosionConfig.builder()
            .numberOfExplosions(10)
            .maximumExplosionStartTime(1.5f)
            .build();
      case DRAGON_BODY:
        // delayed body explosions to sync with head explosions
        return DelayedFollowerExplosionConfig
            .builder()
            .delayTime(1.5f)
            .build();
      case BIG_BOSS:
        // aliens spawn from each of the multi-explosions
        return MultiExplosionConfig.builder()
            .numberOfExplosions(10)
            .maximumExplosionStartTime(1.5f)
            .explosionConfig(SpawningExplosionConfig
                .builder()
                .spawnConfig(
                    SpawningAlienConfig
                        .builder()
                        .spawnedAlienConfig(
                            directionalAlienConfig(
                                AlienCharacter.INSECT,
                                DOWNWARDS,
                                AlienSpeed.SLOW,
                                AlienMissileSpeed.MEDIUM,
                                1.5f
                            ))
                        .minimumSpawnDelayTime(0.5f)
                        .maximumAdditionalRandomSpawnDelayTime(0.25f)
                        .spawnedPowerUpTypes(
                            Arrays.asList(
                                PowerUpType.MISSILE_GUIDED,
                                PowerUpType.MISSILE_FAST,
                                PowerUpType.MISSILE_PARALLEL))
                        .build())
                .build())
            .build();
      default:
        return NormalExplosionConfig.builder()
            .build();
    }
  }

  /**
   * Create energy levels for character
   */
  private static Integer energy(
      AlienCharacter character) {

    switch (character) {
      case NULL:
      case PISTON:
      case PURPLE_MEANIE:
      case PAD:
      case HOPPER:
      case AMOEBA:
      case CHEEKY:
      case FIGHTER:
      case RIPLEY:
      case WALKER:
      case JUMPER:
      case ROTATOR:
      case FROGGER:
      case BATTLE_DROID:
      case SQUEEZE_BOX:
      case ARACNOID:
      case CRAB:
      case BEAR:
      case DINO:
      case JOKER:
      case OCTOPUS:
      case MINION:
      case MOLECULE_MINI:
      case BIG_BOSS:
      case ASTEROID_MINI:
      case DRAGON_BODY:
      case BABY_DRAGON_BODY:
      case SKULL:
      case DROID:
      case INSECT:
      case GOBBY:
      case BOMB:
      case BOUNCER:
      case DROOPY:
      case BAD_CAT:
      case TELLY:
      case HELMET:
      case EGG:
      case CYCLONE:
      case GREMLIN:
      case SPARKLE:
      case SPECTATOR:
      case TINY_DANCER:
      case WILD_STYLE:
      case BUBBLE:
      case BUSHEY:
      case CHOPPY:
      case CLOWN:
      case CROSSER:
      case FROG_KING:
      case GREEN_BOY:
      case JELLYFISH:
      case SWEEP:
      case ZOOM:
      case FOXY_SHRINKING_LARGE:
      case FOXY_SHRINKING_MEDIUM:
      case FOXY_SHRINKING_NORMAL:
      case FOXY_SHRINKING_SMALL:
        return 1;
      case CIRCUIT:
      case PILOT:
      case FRISBIE:
      case ZOGG:
      case LEMMING:
      case YELLOW_BEARD:
      case FISH:
      case ALL_SEEING_EYE:
      case PINKO:
      case GHOST:
      case ASTEROID:
      case MOLECULE:
      case SPINNER_GREEN:
      case SPINNER_PULSE_GREEN:
      case FOXY:
      case BABY_DRAGON_HEAD:
      case CHARLIE:
      case SQUASHER:
      case SPACE_STATION:
      case SPIRAL:
        return 2;
      case PINCER:
      case CONFUSER:
        return 3;
      case DEVIL:
      case WHIRLPOOL:
      case SMOKEY:
        return 5;
      case INSECT_MOTHERSHIP:
      case BATTY:
      case SAUCER:
      case LADY_BIRD:
      case CLOUD:
        return 10;
      case BOOK:
        return 15;
      case DRAGON_HEAD:
        return 20;
      case BARRIER:
      case BLOCK:
        return Integer.MAX_VALUE;
      default:
        throw new GalaxyForceException("Can not create energy for unknown character: " + character);
    }
  }

  /**
   * Is character angled to path?
   */
  private static Boolean isAngledToPath(
      AlienCharacter character) {

    switch (character) {
      case LADY_BIRD:
      case FIGHTER:
      case MINION:
      case ZOOM:
        return true;
      default:
        return false;
    }

  }

  /**
   * Create changing alien config builder for character without missiles
   */
  public static ChangingConfig changingConfig(ChangingAlienCharacter changingCharacter) {
    return changingConfigBuilder(changingCharacter)
        .build();
  }

  /**
   * Create changing alien config for character with missiles
   */
  public static ChangingConfig changingConfig(
      ChangingAlienCharacter changingCharacter,
      AlienMissileSpeed speed,
      Float missileFrequency) {

    final List<AlienCharacter> characters = changingCharacter.getCharacters();
    final MissileConfig missileConfig = missileConfig(characters.get(0), speed, missileFrequency);

    return changingConfigBuilder(changingCharacter)
        .missileConfig(missileConfig)
        .build();
  }

  private static ChangingConfig.ChangingConfigBuilder changingConfigBuilder(
      ChangingAlienCharacter changingCharacter) {

    final List<AlienCharacter> characters = changingCharacter.getCharacters();

    int totalEnergy = 0;
    List<AlienCharacterWithEnergy> charactersWithEnergy = new ArrayList<>();
    for (AlienCharacter character : characters) {
      int characterEnergy = energy(character);
      charactersWithEnergy.add(new AlienCharacterWithEnergy(
          character,
          characterEnergy));
      totalEnergy += characterEnergy;
    }

    final AlienCharacter character = characters.get(0);
    final Boolean isAngledToPath = isAngledToPath(character);
    final ExplosionConfig explosionConfig = explosionConfig(character);
    final SpinningConfig spinningConfig = spinningConfigFixed(character);

    return ChangingConfig
        .builder()
        .alienCharacters(charactersWithEnergy)
        .energy(totalEnergy)
        .angledToPath(isAngledToPath)
        .explosionConfig(explosionConfig)
        .spinningConfig(spinningConfig);
  }

  /**
   * Create alien config for character with missiles
   */
  public static PathConfig alienConfig(AlienCharacter character,
      AlienMissileSpeed speed,
      Float missileFrequency) {

    final MissileConfig missileConfig = missileConfig(character, speed, missileFrequency);

    return alienConfigBuilder(character)
        .missileConfig(missileConfig)
        .build();
  }

  /**
   * Create alien config for character without missiles
   */
  public static PathConfig alienConfig(AlienCharacter character) {

    return alienConfigBuilder(character)
        .build();
  }

  /**
   * Create alien config for character with multiple missile configs
   */
  public static PathConfig alienConfig(
      AlienCharacter character,
      List<MissileFiringConfig> missileConfigs) {

    final MissileConfig missileConfig = MissileMultiFiringConfig
        .builder()
        .missileConfigs(missileConfigs)
        .build();

    return alienConfigBuilder(character)
        .missileConfig(missileConfig)
        .build();
  }

  /**
   * Create alien config builder for character without missiles
   */
  private static PathConfig.PathConfigBuilder alienConfigBuilder(AlienCharacter character) {

    final Integer energy = energy(character);
    final Boolean isAngledToPath = isAngledToPath(character);
    final ExplosionConfig explosionConfig = explosionConfig(character);
    final SpinningConfig spinningConfig = spinningConfigFixed(character);

    return PathConfig
        .builder()
        .alienCharacter(character)
        .energy(energy)
        .angledToPath(isAngledToPath)
        .explosionConfig(explosionConfig)
        .spinningConfig(spinningConfig);
  }

  /**
   * Create directional alien config builder for character without missiles
   */
  private static DirectionalDestroyableConfig.DirectionalDestroyableConfigBuilder directionalAlienConfigBuilder(
      AlienCharacter character,
      Float directionalAngle,
      AlienSpeed alienSpeed) {

    final Integer energy = energy(character);
    final ExplosionConfig explosionConfig = explosionConfig(character, alienSpeed);
    final SpinningConfig spinningConfig = spinningConfigBySpeed(character);

    return DirectionalDestroyableConfig
        .builder()
        .alienCharacter(character)
        .energy(energy)
        .speed(alienSpeed)
        .angle(directionalAngle)
        .explosionConfig(explosionConfig)
        .spinningConfig(spinningConfig);
  }

  /**
   * Create directional alien config for character with missiles
   */
  public static DirectionalDestroyableConfig directionalAlienConfig(
      AlienCharacter character,
      Float directionalAngle,
      AlienSpeed alienSpeed,
      AlienMissileSpeed missileSpeed,
      Float missileFrequency) {

    final MissileConfig missileConfig = missileConfig(character, missileSpeed, missileFrequency);

    return directionalAlienConfigBuilder(
        character,
        directionalAngle,
        alienSpeed)
        .missileConfig(missileConfig)
        .build();

  }

  /**
   * Create directional alien config for character without missiles
   */
  public static DirectionalDestroyableConfig directionalAlienConfig(
      AlienCharacter character,
      Float directionalAngle,
      AlienSpeed alienSpeed) {

    return directionalAlienConfigBuilder(
        character,
        directionalAngle,
        alienSpeed)
        .build();
  }

  /**
   * Create alien config row with missiles and power-ups
   */
  public static AlienRowConfig alienRowConfig(
      AlienCharacter character,
      AlienMissileSpeed speed,
      Float missileFrequency,
      List<PowerUpType> powerUps) {

    return AlienRowConfig
        .builder()
        .alienConfig(
            alienConfig(
                character,
                speed,
                missileFrequency
            )
        )
        .powerUps(powerUps)
        .build();
  }

  /**
   * Create hunter alien config for character with missiles
   */
  public static HunterConfig hunterAlienConfig(
      AlienCharacter character,
      AlienSpeed alienSpeed,
      BoundariesConfig boundariesConfig,
      AlienMissileSpeed missileSpeed,
      Float missileFrequency) {

    final MissileConfig missileConfig = missileConfig(character, missileSpeed, missileFrequency);
    return hunterAlienConfigBuilder(character, alienSpeed, boundariesConfig)
        .missileConfig(missileConfig)
        .build();
  }

  /**
   * Create hunter alien config for character without missiles
   */
  public static HunterConfig hunterAlienConfig(
      AlienCharacter character,
      AlienSpeed alienSpeed,
      BoundariesConfig boundariesConfig) {

    return hunterAlienConfigBuilder(character, alienSpeed, boundariesConfig)
        .build();
  }

  /**
   * Create hunter alien config builder
   */
  private static HunterConfig.HunterConfigBuilder hunterAlienConfigBuilder(
      AlienCharacter character,
      AlienSpeed alienSpeed,
      BoundariesConfig boundariesConfig) {

    final Integer energy = energy(character);
    final ExplosionConfig explosionConfig = explosionConfig(character, alienSpeed);
    final SpinningConfig spinningConfig = spinningConfigBySpeed(character);

    return HunterConfig
        .builder()
        .alienCharacter(character)
        .energy(energy)
        .speed(alienSpeed)
        .boundaries(boundariesConfig)
        .explosionConfig(explosionConfig)
        .spinningConfig(spinningConfig);
  }

  /**
   * Create spinning aliens that descend from screen.
   */
  public static DirectionalResettableConfig resettableDirectionalAlienConfig(
      AlienCharacter character,
      Float directionalAngle,
      AlienSpeed alienSpeed
  ) {
    return resettableDirectionalAlienConfigBuilder(character, directionalAngle, alienSpeed)
        .build();
  }

  private static DirectionalResettableConfig.DirectionalResettableConfigBuilder resettableDirectionalAlienConfigBuilder(
      AlienCharacter character,
      Float directionalAngle,
      AlienSpeed alienSpeed
  ) {
    final Integer energy = energy(character);
    final ExplosionConfig explosionConfig = explosionConfig(character, alienSpeed);
    final SpinningConfig spinningConfig = spinningConfigBySpeed(character);

    return DirectionalResettableConfig
        .builder()
        .alienCharacter(character)
        .energy(energy)
        .speed(alienSpeed)
        .angle(directionalAngle)
        .spinningConfig(spinningConfig)
        .explosionConfig(explosionConfig);
  }

  /**
   * Create 2-way explode config that splits and spawns new aliens. e.g. asteroids that split into
   * smaller asteroids
   */
  private static ExplosionConfig splitOnExplodeConfig(
      AlienSpeed alienSpeed,
      AlienCharacter spawnCharacter,
      float angle
  ) {
    return SpawningExplosionConfig
        .builder()
        .spawnConfig(
            SpawnOnDemandConfig
                .builder()
                .spawnedPowerUpTypes(
                    NO_POWER_UPS)
                .spawnedAlienConfig(SplitterConfig
                    .builder()
                    .alienConfigs(
                        Arrays.asList(
                            directionalAlienConfig(
                                spawnCharacter,
                                angle - QUARTER_PI,
                                alienSpeed),
                            directionalAlienConfig(
                                spawnCharacter,
                                angle + QUARTER_PI,
                                alienSpeed)))
                    .build())
                .build())
        .build();
  }

  /**
   * Create 2-way explode config that splits and spawns new drifting aliens. e.g. asteroids that
   * split into smaller asteroids
   */
  private static ExplosionConfig splitOnExplodeDriftingConfig(
      AlienSpeed alienSpeed,
      AlienCharacter spawnCharacter,
      float angle
  ) {
    return SpawningExplosionConfig
        .builder()
        .spawnConfig(
            SpawnOnDemandConfig
                .builder()
                .spawnedPowerUpTypes(
                    NO_POWER_UPS)
                .spawnedAlienConfig(SplitterConfig
                    .builder()
                    .alienConfigs(
                        Arrays.asList(
                            driftingAlienConfig(
                                spawnCharacter,
                                angle - QUARTER_PI,
                                alienSpeed),
                            driftingAlienConfig(
                                spawnCharacter,
                                angle + QUARTER_PI,
                                alienSpeed)))
                    .build())
                .build())
        .build();
  }

  /**
   * Create 4-way explode config that splits and spawns new aliens. e.g. asteroids that split into
   * smaller asteroids
   */
  private static ExplosionConfig splitFourWayOnExplodeConfig(
      AlienSpeed alienSpeed,
      AlienCharacter spawnCharacter
  ) {
    return SpawningExplosionConfig
        .builder()
        .spawnConfig(
            SpawnOnDemandConfig
                .builder()
                .spawnedPowerUpTypes(
                    NO_POWER_UPS)
                .spawnedAlienConfig(SplitterConfig
                    .builder()
                    .alienConfigs(
                        Arrays.asList(
                            directionalAlienConfig(
                                spawnCharacter,
                                HALF_PI - QUARTER_PI,
                                alienSpeed),
                            directionalAlienConfig(
                                spawnCharacter,
                                HALF_PI + QUARTER_PI,
                                alienSpeed),
                            directionalAlienConfig(
                                spawnCharacter,
                                -HALF_PI - QUARTER_PI,
                                alienSpeed),
                            directionalAlienConfig(
                                spawnCharacter,
                                -HALF_PI + QUARTER_PI,
                                alienSpeed)))
                    .build())
                .build())
        .build();
  }

  public static AlienConfig spawningPathAlienConfig(
      final AlienCharacter character,
      final SpawnConfig spawnConfig
  ) {

    return alienConfigBuilder(character)
        .spawnConfig(spawnConfig)
        .build();
  }

  public static FollowableHunterConfig followableHunterConfig(
      final AlienCharacter hunterCharacter,
      final AlienSpeed speed,
      final AlienMissileSpeed missileSpeed,
      final Float missileFrequency,
      final BoundariesConfig boundariesConfig,
      final int numberOfFollowers,
      final FollowerConfig followerConfig,
      final List<PowerUpType> followerPowerUps) {

    final MissileConfig missileConfig = missileConfig(hunterCharacter, missileSpeed,
        missileFrequency);
    return followableHunterConfigBuilder(
        hunterCharacter,
        speed,
        boundariesConfig,
        numberOfFollowers,
        followerConfig,
        followerPowerUps)
        .missileConfig(missileConfig)
        .build();
  }

  public static FollowableHunterConfig followableHunterConfig(
      final AlienCharacter hunterCharacter,
      final AlienSpeed speed,
      final AlienMissileSpeed missileSpeed,
      final Float missileFrequency,
      final BoundariesConfig boundariesConfig,
      final int numberOfFollowers,
      final FollowerConfig followerConfig,
      final List<PowerUpType> followerPowerUps,
      final SpawnConfig spawnConfig) {

    final MissileConfig missileConfig = missileConfig(hunterCharacter, missileSpeed,
        missileFrequency);
    return followableHunterConfigBuilder(
        hunterCharacter,
        speed,
        boundariesConfig,
        numberOfFollowers,
        followerConfig,
        followerPowerUps)
        .missileConfig(missileConfig)
        .spawnConfig(spawnConfig)
        .build();
  }

  public static FollowableHunterConfig followableHunterConfig(
      final AlienCharacter hunterCharacter,
      final AlienSpeed speed,
      final BoundariesConfig boundariesConfig,
      final int numberOfFollowers,
      final FollowerConfig followerConfig,
      final List<PowerUpType> followerPowerUps) {

    return followableHunterConfigBuilder(
        hunterCharacter,
        speed,
        boundariesConfig,
        numberOfFollowers,
        followerConfig,
        followerPowerUps)
        .build();
  }

  private static FollowableHunterConfig.FollowableHunterConfigBuilder followableHunterConfigBuilder(
      final AlienCharacter hunterCharacter,
      final AlienSpeed speed,
      final BoundariesConfig boundariesConfig,
      final int numberOfFollowers,
      final FollowerConfig followerConfig,
      final List<PowerUpType> followerPowerUps) {

    final Integer energy = energy(hunterCharacter);
    final ExplosionConfig explosionConfig = explosionConfig(hunterCharacter);
    final SpinningConfig spinningConfig = spinningConfigBySpeed(hunterCharacter);

    return FollowableHunterConfig
        .builder()
        .alienCharacter(hunterCharacter)
        .energy(energy)
        .speed(speed)
        .boundaries(boundariesConfig)
        .numberOfFollowers(numberOfFollowers)
        .followerConfig(followerConfig)
        .followerPowerUps(followerPowerUps)
        .spinningConfig(spinningConfig)
        .explosionConfig(explosionConfig);
  }

  public static FollowerConfig followerConfig(
      AlienCharacter character,
      AlienSpeed speed) {

    return followerConfigBuilder(character, speed)
        .build();
  }

  private static FollowerConfig.FollowerConfigBuilder followerConfigBuilder(
      AlienCharacter character,
      AlienSpeed speed) {

    final Integer energy = energy(character);
    final ExplosionConfig explosionConfig = explosionConfig(character);
    final SpinningConfig spinningConfig = spinningConfigBySpeed(character);

    return FollowerConfig
        .builder()
        .alienCharacter(character)
        .energy(energy)
        .speed(speed)
        .spinningConfig(spinningConfig)
        .explosionConfig(explosionConfig);
  }

  public static AlienConfig spawningStaticAlienConfig(
      final AlienCharacter character,
      final SpawnConfig spawnConfig
  ) {

    final Integer energy = energy(character);

    return StaticConfig
        .builder()
        .alienCharacter(character)
        .energy(energy)
        .spawnConfig(spawnConfig)
        .build();
  }

  // creates alien config for alien that explodes after a set time
  public static AlienConfig explodingAlienConfig(
      final AlienCharacter character,
      final AlienMissileCharacter missileCharacter,
      final Float explosionTime
  ) {
    final Integer energy = energy(character);
    final ExplosionConfig explosionConfig = explosionConfig(character);
    final SpinningConfig spinningConfig = spinningConfigFixed(character);

    return ExplodingConfig
        .builder()
        .alienCharacter(character)
        .energy(energy)
        .explosionTime(explosionTime)
        .explodingMissileCharacter(missileCharacter)
        .explosionConfig(explosionConfig)
        .spinningConfig(spinningConfig)
        .build();
  }

  // creates alien config builder for aliens that drift across screen
  public static DriftingConfigBuilder driftingAlienConfigBuilder(
      final AlienCharacter character,
      final float angle,
      final AlienSpeed speed) {

    final Integer energy = energy(character);
    final ExplosionConfig explosionConfig = explosionConfig(character, speed, angle, true);
    final SpinningConfig spinningConfig = spinningConfigBySpeed(character);

    return DriftingConfig
        .builder()
        .alienCharacter(character)
        .energy(energy)
        .speed(speed)
        .angle(angle)
        .spinningConfig(spinningConfig)
        .explosionConfig(explosionConfig);
  }

  // creates alien config for aliens that drift across screen
  public static AlienConfig driftingAlienConfig(
      final AlienCharacter character,
      final float angle,
      final AlienSpeed speed) {

    return driftingAlienConfigBuilder(character, angle, speed)
        .build();
  }

  // creates alien config for aliens that drift across screen and fires missiles
  public static AlienConfig driftingAlienConfig(
      final AlienCharacter character,
      final float angle,
      final AlienSpeed speed,
      final AlienMissileSpeed missileSpeed,
      final Float missileFrequency) {

    final MissileConfig missileConfig = missileConfig(character, missileSpeed,
        missileFrequency);

    return driftingAlienConfigBuilder(character, angle, speed)
        .missileConfig(missileConfig)
        .build();
  }
}