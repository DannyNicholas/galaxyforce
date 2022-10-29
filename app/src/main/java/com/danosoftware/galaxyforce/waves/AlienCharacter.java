package com.danosoftware.galaxyforce.waves;

import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.view.Animation;

/**
 * Enum representing the different types of alien that can be created (with their associated
 * animations).
 * <p>
 * Used by AlienFactory to create instances of the wanted alien.
 */
public enum AlienCharacter {

  // special null character reserved for multi-explosions that spawn immediately as exploding aliens
  NULL(
      new SpriteDetails[]{SpriteDetails.NULL},
      new SpriteDetails[]{SpriteDetails.NULL},
      0f),

  BLOCK(
      new SpriteDetails[]{
          SpriteDetails.BLOCK_01,
          SpriteDetails.BLOCK_02},
      new SpriteDetails[]{
          SpriteDetails.BLOCK_01,
          SpriteDetails.BLOCK_02},
      0.5f
  ),

  FROGGER(
      new SpriteDetails[]{
          SpriteDetails.FROGGER,
          SpriteDetails.FROGGER_TALL,
          SpriteDetails.FROGGER,
          SpriteDetails.FROGGER_WIDE},
      new SpriteDetails[]{
          SpriteDetails.FROGGER_HIT,
          SpriteDetails.FROGGER_TALL_HIT,
          SpriteDetails.FROGGER_HIT,
          SpriteDetails.FROGGER_WIDE_HIT},
      0.25f
  ),

  CYCLONE(
      new SpriteDetails[]{
          SpriteDetails.CYCLONE_01,
          SpriteDetails.CYCLONE_02,
          SpriteDetails.CYCLONE_03,
          SpriteDetails.CYCLONE_04,
          SpriteDetails.CYCLONE_05},
      new SpriteDetails[]{
          SpriteDetails.CYCLONE_01_HIT,
          SpriteDetails.CYCLONE_02_HIT,
          SpriteDetails.CYCLONE_03_HIT,
          SpriteDetails.CYCLONE_04_HIT,
          SpriteDetails.CYCLONE_05_HIT},
      0.25f
  ),

  DEVIL(
      new SpriteDetails[]{
          SpriteDetails.DEVIL,
          SpriteDetails.DEVIL_DOWN,
          SpriteDetails.DEVIL,
          SpriteDetails.DEVIL_UP},
      new SpriteDetails[]{
          SpriteDetails.DEVIL_HIT,
          SpriteDetails.DEVIL_DOWN_HIT,
          SpriteDetails.DEVIL_HIT,
          SpriteDetails.DEVIL_UP_HIT},
      0.25f
  ),

  GREMLIN(
      new SpriteDetails[]{
          SpriteDetails.GREMLIN_01,
          SpriteDetails.GREMLIN_02,
          SpriteDetails.GREMLIN_03,
          SpriteDetails.GREMLIN_04,
          SpriteDetails.GREMLIN_03,
          SpriteDetails.GREMLIN_02},
      new SpriteDetails[]{
          SpriteDetails.GREMLIN_01_HIT,
          SpriteDetails.GREMLIN_02_HIT,
          SpriteDetails.GREMLIN_03_HIT,
          SpriteDetails.GREMLIN_04_HIT,
          SpriteDetails.GREMLIN_03_HIT,
          SpriteDetails.GREMLIN_02_HIT},
      0.25f
  ),

  SPARKLE(
      new SpriteDetails[]{
          SpriteDetails.SPARKLE,
          SpriteDetails.SPARKLE_TALL,
          SpriteDetails.SPARKLE,
          SpriteDetails.SPARKLE_WIDE},
      new SpriteDetails[]{
          SpriteDetails.SPARKLE_HIT,
          SpriteDetails.SPARKLE_TALL_HIT,
          SpriteDetails.SPARKLE_HIT,
          SpriteDetails.SPARKLE_WIDE_HIT},
      0.25f
  ),

  PINCER(
      new SpriteDetails[]{
          SpriteDetails.PINCER,
          SpriteDetails.PINCER_LEFT,
          SpriteDetails.PINCER,
          SpriteDetails.PINCER_RIGHT},
      new SpriteDetails[]{
          SpriteDetails.PINCER_HIT,
          SpriteDetails.PINCER_LEFT_HIT,
          SpriteDetails.PINCER_HIT,
          SpriteDetails.PINCER_RIGHT_HIT},
      0.25f
  ),

  SPECTATOR(
      new SpriteDetails[]{
          SpriteDetails.SPECTATOR,
          SpriteDetails.SPECTATOR_UP,
          SpriteDetails.SPECTATOR,
          SpriteDetails.SPECTATOR_DOWN},
      new SpriteDetails[]{
          SpriteDetails.SPECTATOR_HIT,
          SpriteDetails.SPECTATOR_UP_HIT,
          SpriteDetails.SPECTATOR_HIT,
          SpriteDetails.SPECTATOR_DOWN_HIT},
      0.25f
  ),

  SQUASHER(
      new SpriteDetails[]{
          SpriteDetails.SQUASHER,
          SpriteDetails.SQUASHER_WIDE,
          SpriteDetails.SQUASHER,
          SpriteDetails.SQUASHER_TALL},
      new SpriteDetails[]{
          SpriteDetails.SQUASHER_HIT,
          SpriteDetails.SQUASHER_WIDE_HIT,
          SpriteDetails.SQUASHER_HIT,
          SpriteDetails.SQUASHER_TALL_HIT},
      0.25f
  ),

  TINY_DANCER(
      new SpriteDetails[]{
          SpriteDetails.TINY_DANCER,
          SpriteDetails.TINY_DANCER_LEFT,
          SpriteDetails.TINY_DANCER,
          SpriteDetails.TINY_DANCER_RIGHT},
      new SpriteDetails[]{
          SpriteDetails.TINY_DANCER_HIT,
          SpriteDetails.TINY_DANCER_LEFT_HIT,
          SpriteDetails.TINY_DANCER_HIT,
          SpriteDetails.TINY_DANCER_RIGHT_HIT},
      0.25f
  ),

  WILD_STYLE(
      new SpriteDetails[]{
          SpriteDetails.WILD_STYLE,
          SpriteDetails.WILD_STYLE_FLAT,
          SpriteDetails.WILD_STYLE,
          SpriteDetails.WILD_STYLE_TALL},
      new SpriteDetails[]{
          SpriteDetails.WILD_STYLE_HIT,
          SpriteDetails.WILD_STYLE_FLAT_HIT,
          SpriteDetails.WILD_STYLE_HIT,
          SpriteDetails.WILD_STYLE_TALL_HIT},
      0.25f
  ),

  CONFUSER(
      new SpriteDetails[]{
          SpriteDetails.CONFUSER,
          SpriteDetails.CONFUSER_FLAT,
          SpriteDetails.CONFUSER,
          SpriteDetails.CONFUSER_TALL},
      new SpriteDetails[]{
          SpriteDetails.CONFUSER_HIT,
          SpriteDetails.CONFUSER_FLAT_HIT,
          SpriteDetails.CONFUSER_HIT,
          SpriteDetails.CONFUSER_TALL_HIT},
      0.25f
  ),

  SAUCER(
      new SpriteDetails[]{
          SpriteDetails.SAUCER,
          SpriteDetails.SAUCER_RIGHT,
          SpriteDetails.SAUCER,
          SpriteDetails.SAUCER_LEFT},
      new SpriteDetails[]{
          SpriteDetails.SAUCER_HIT,
          SpriteDetails.SAUCER_RIGHT_HIT,
          SpriteDetails.SAUCER_HIT,
          SpriteDetails.SAUCER_LEFT_HIT},
      0.25f
  ),

  PINKO(
      new SpriteDetails[]{
          SpriteDetails.PINKO,
          SpriteDetails.PINKO_TALL,
          SpriteDetails.PINKO,
          SpriteDetails.PINKO_WIDE},
      new SpriteDetails[]{
          SpriteDetails.PINKO_HIT,
          SpriteDetails.PINKO_TALL_HIT,
          SpriteDetails.PINKO_HIT,
          SpriteDetails.PINKO_WIDE_HIT},
      0.25f
  ),

  PISTON(
      new SpriteDetails[]{
          SpriteDetails.PISTON,
          SpriteDetails.PISTON_TALL,
          SpriteDetails.PISTON,
          SpriteDetails.PISTON_WIDE},
      new SpriteDetails[]{
          SpriteDetails.PISTON_HIT,
          SpriteDetails.PISTON_TALL_HIT,
          SpriteDetails.PISTON_HIT,
          SpriteDetails.PISTON_WIDE_HIT},
      0.25f
  ),

  PURPLE_MEANIE(
      new SpriteDetails[]{
          SpriteDetails.PURPLE_MEANIE_01,
          SpriteDetails.PURPLE_MEANIE_02,
          SpriteDetails.PURPLE_MEANIE_03,
          SpriteDetails.PURPLE_MEANIE_04,
          SpriteDetails.PURPLE_MEANIE_05},
      new SpriteDetails[]{
          SpriteDetails.PURPLE_MEANIE_01_HIT,
          SpriteDetails.PURPLE_MEANIE_02_HIT,
          SpriteDetails.PURPLE_MEANIE_03_HIT,
          SpriteDetails.PURPLE_MEANIE_04_HIT,
          SpriteDetails.PURPLE_MEANIE_05_HIT},
      0.25f
  ),

  PAD(
      new SpriteDetails[]{
          SpriteDetails.PAD_01,
          SpriteDetails.PAD_02,
          SpriteDetails.PAD_03,
          SpriteDetails.PAD_04,
          SpriteDetails.PAD_05},
      new SpriteDetails[]{
          SpriteDetails.PAD_01_HIT,
          SpriteDetails.PAD_02_HIT,
          SpriteDetails.PAD_03_HIT,
          SpriteDetails.PAD_04_HIT,
          SpriteDetails.PAD_05_HIT},
      0.25f
  ),

  GHOST(
      new SpriteDetails[]{
          SpriteDetails.GHOST,
          SpriteDetails.GHOST_LEFT,
          SpriteDetails.GHOST,
          SpriteDetails.GHOST_RIGHT},
      new SpriteDetails[]{
          SpriteDetails.GHOST_HIT,
          SpriteDetails.GHOST_LEFT_HIT,
          SpriteDetails.GHOST_HIT,
          SpriteDetails.GHOST_RIGHT_HIT},
      0.25f
  ),

  HOPPER(
      new SpriteDetails[]{
          SpriteDetails.HOPPER,
          SpriteDetails.HOPPER_LEFT,
          SpriteDetails.HOPPER,
          SpriteDetails.HOPPER_RIGHT},
      new SpriteDetails[]{
          SpriteDetails.HOPPER_HIT,
          SpriteDetails.HOPPER_LEFT_HIT,
          SpriteDetails.HOPPER_HIT,
          SpriteDetails.HOPPER_RIGHT_HIT},
      0.25f
  ),

  AMOEBA(
      new SpriteDetails[]{
          SpriteDetails.AMOEBA,
          SpriteDetails.AMOEBA_LEFT,
          SpriteDetails.AMOEBA,
          SpriteDetails.AMOEBA_RIGHT},
      new SpriteDetails[]{
          SpriteDetails.AMOEBA_HIT,
          SpriteDetails.AMOEBA_LEFT_HIT,
          SpriteDetails.AMOEBA_HIT,
          SpriteDetails.AMOEBA_RIGHT_HIT},
      0.25f
  ),

  CHEEKY(
      new SpriteDetails[]{
          SpriteDetails.CHEEKY,
          SpriteDetails.CHEEKY_LEFT,
          SpriteDetails.CHEEKY,
          SpriteDetails.CHEEKY_RIGHT},
      new SpriteDetails[]{
          SpriteDetails.CHEEKY_HIT,
          SpriteDetails.CHEEKY_LEFT_HIT,
          SpriteDetails.CHEEKY_HIT,
          SpriteDetails.CHEEKY_RIGHT_HIT},
      0.25f
  ),

  FOXY(
      new SpriteDetails[]{
          SpriteDetails.FOXY,
          SpriteDetails.FOXY_TALL,
          SpriteDetails.FOXY,
          SpriteDetails.FOXY_WIDE},
      new SpriteDetails[]{
          SpriteDetails.FOXY_HIT,
          SpriteDetails.FOXY_TALL_HIT,
          SpriteDetails.FOXY_HIT,
          SpriteDetails.FOXY_WIDE_HIT},
      0.25f
  ),

  FOXY_SMALL(
      new SpriteDetails[]{
          SpriteDetails.FOXY_SMALL,
          SpriteDetails.FOXY_TALL_SMALL,
          SpriteDetails.FOXY_SMALL,
          SpriteDetails.FOXY_WIDE_SMALL},
      new SpriteDetails[]{
          SpriteDetails.FOXY_HIT_SMALL,
          SpriteDetails.FOXY_TALL_HIT_SMALL,
          SpriteDetails.FOXY_HIT_SMALL,
          SpriteDetails.FOXY_WIDE_HIT_SMALL},
      0.25f
  ),

  FOXY_MEDIUM(
      new SpriteDetails[]{
          SpriteDetails.FOXY_MEDIUM,
          SpriteDetails.FOXY_TALL_MEDIUM,
          SpriteDetails.FOXY_MEDIUM,
          SpriteDetails.FOXY_WIDE_MEDIUM},
      new SpriteDetails[]{
          SpriteDetails.FOXY_HIT_MEDIUM,
          SpriteDetails.FOXY_TALL_HIT_MEDIUM,
          SpriteDetails.FOXY_HIT_MEDIUM,
          SpriteDetails.FOXY_WIDE_HIT_MEDIUM},
      0.25f
  ),

  FOXY_LARGE(
      new SpriteDetails[]{
          SpriteDetails.FOXY_LARGE,
          SpriteDetails.FOXY_TALL_LARGE,
          SpriteDetails.FOXY_LARGE,
          SpriteDetails.FOXY_WIDE_LARGE},
      new SpriteDetails[]{
          SpriteDetails.FOXY_HIT_LARGE,
          SpriteDetails.FOXY_TALL_HIT_LARGE,
          SpriteDetails.FOXY_HIT_LARGE,
          SpriteDetails.FOXY_WIDE_HIT_LARGE},
      0.25f
  ),

  FIGHTER(
      new SpriteDetails[]{
          SpriteDetails.FIGHTER,
          SpriteDetails.FIGHTER_TALL,
          SpriteDetails.FIGHTER,
          SpriteDetails.FIGHTER_WIDE},
      new SpriteDetails[]{
          SpriteDetails.FIGHTER_HIT,
          SpriteDetails.FIGHTER_TALL_HIT,
          SpriteDetails.FIGHTER_HIT,
          SpriteDetails.FIGHTER_WIDE_HIT},
      0.25f
  ),

  RIPLEY(
      new SpriteDetails[]{
          SpriteDetails.RIPLEY,
          SpriteDetails.RIPLEY_LEFT,
          SpriteDetails.RIPLEY,
          SpriteDetails.RIPLEY_RIGHT},
      new SpriteDetails[]{
          SpriteDetails.RIPLEY_HIT,
          SpriteDetails.RIPLEY_LEFT_HIT,
          SpriteDetails.RIPLEY_HIT,
          SpriteDetails.RIPLEY_RIGHT_HIT},
      0.25f
  ),

  WALKER(
      new SpriteDetails[]{
          SpriteDetails.WALKER,
          SpriteDetails.WALKER_RIGHT,
          SpriteDetails.WALKER_LEFT},
      new SpriteDetails[]{
          SpriteDetails.WALKER_HIT,
          SpriteDetails.WALKER_RIGHT_HIT,
          SpriteDetails.WALKER_LEFT_HIT},
      0.25f
  ),

  CHARLIE(
      new SpriteDetails[]{
          SpriteDetails.CHARLIE,
          SpriteDetails.CHARLIE_LEFT,
          SpriteDetails.CHARLIE,
          SpriteDetails.CHARLIE_RIGHT},
      new SpriteDetails[]{
          SpriteDetails.CHARLIE_HIT,
          SpriteDetails.CHARLIE_LEFT_HIT,
          SpriteDetails.CHARLIE_HIT,
          SpriteDetails.CHARLIE_RIGHT_HIT},
      0.25f
  ),

  ALL_SEEING_EYE(
      new SpriteDetails[]{
          SpriteDetails.ALL_SEEING_EYE,
          SpriteDetails.ALL_SEEING_EYE_LEFT,
          SpriteDetails.ALL_SEEING_EYE,
          SpriteDetails.ALL_SEEING_EYE_RIGHT},
      new SpriteDetails[]{
          SpriteDetails.ALL_SEEING_EYE_HIT,
          SpriteDetails.ALL_SEEING_EYE_LEFT_HIT,
          SpriteDetails.ALL_SEEING_EYE_HIT,
          SpriteDetails.ALL_SEEING_EYE_RIGHT_HIT},
      0.25f
  ),

  JUMPER(
      new SpriteDetails[]{
          SpriteDetails.JUMPER,
          SpriteDetails.JUMPER_UP,
          SpriteDetails.JUMPER,
          SpriteDetails.JUMPER_DOWN},
      new SpriteDetails[]{
          SpriteDetails.JUMPER_HIT,
          SpriteDetails.JUMPER_UP_HIT,
          SpriteDetails.JUMPER_HIT,
          SpriteDetails.JUMPER_DOWN_HIT},
      0.25f
  ),

  ROTATOR(
      new SpriteDetails[]{
          SpriteDetails.ROTATOR_01,
          SpriteDetails.ROTATOR_02,
          SpriteDetails.ROTATOR_03,
          SpriteDetails.ROTATOR_04,
          SpriteDetails.ROTATOR_05,},
      new SpriteDetails[]{
          SpriteDetails.ROTATOR_01_HIT,
          SpriteDetails.ROTATOR_02_HIT,
          SpriteDetails.ROTATOR_03_HIT,
          SpriteDetails.ROTATOR_04_HIT,
          SpriteDetails.ROTATOR_05_HIT,},
      0.15f
  ),

  WHIRLPOOL(
      new SpriteDetails[]{
          SpriteDetails.WHIRLPOOL_01,
          SpriteDetails.WHIRLPOOL_02,
          SpriteDetails.WHIRLPOOL_03,
          SpriteDetails.WHIRLPOOL_04,
          SpriteDetails.WHIRLPOOL_05,},
      new SpriteDetails[]{
          SpriteDetails.WHIRLPOOL_01_HIT,
          SpriteDetails.WHIRLPOOL_02_HIT,
          SpriteDetails.WHIRLPOOL_03_HIT,
          SpriteDetails.WHIRLPOOL_04_HIT,
          SpriteDetails.WHIRLPOOL_05_HIT,},
      0.15f
  ),

  BATTLE_DROID(
      new SpriteDetails[]{
          SpriteDetails.BATTLE_DROID_01,
          SpriteDetails.BATTLE_DROID_02,
          SpriteDetails.BATTLE_DROID_01,
          SpriteDetails.BATTLE_DROID_03},
      new SpriteDetails[]{
          SpriteDetails.BATTLE_DROID_01_HIT,
          SpriteDetails.BATTLE_DROID_02_HIT,
          SpriteDetails.BATTLE_DROID_01_HIT,
          SpriteDetails.BATTLE_DROID_03_HIT},
      0.20f
  ),

  FISH(
      new SpriteDetails[]{
          SpriteDetails.FISH_01,
          SpriteDetails.FISH_02,
          SpriteDetails.FISH_01,
          SpriteDetails.FISH_03},
      new SpriteDetails[]{
          SpriteDetails.FISH_01_HIT,
          SpriteDetails.FISH_02_HIT,
          SpriteDetails.FISH_01_HIT,
          SpriteDetails.FISH_03_HIT},
      0.20f
  ),

  LEMMING(
      new SpriteDetails[]{
          SpriteDetails.LEMMING_01,
          SpriteDetails.LEMMING_02,
          SpriteDetails.LEMMING_01,
          SpriteDetails.LEMMING_03},
      new SpriteDetails[]{
          SpriteDetails.LEMMING_01_HIT,
          SpriteDetails.LEMMING_02_HIT,
          SpriteDetails.LEMMING_01_HIT,
          SpriteDetails.LEMMING_03_HIT},
      0.20f
  ),

  SQUEEZE_BOX(
      new SpriteDetails[]{
          SpriteDetails.SQUEEZE_BOX_01,
          SpriteDetails.SQUEEZE_BOX_02,
          SpriteDetails.SQUEEZE_BOX_01,
          SpriteDetails.SQUEEZE_BOX_03},
      new SpriteDetails[]{
          SpriteDetails.SQUEEZE_BOX_01_HIT,
          SpriteDetails.SQUEEZE_BOX_02_HIT,
          SpriteDetails.SQUEEZE_BOX_01_HIT,
          SpriteDetails.SQUEEZE_BOX_03_HIT},
      0.20f
  ),

  YELLOW_BEARD(
      new SpriteDetails[]{
          SpriteDetails.YELLOW_BEARD_01,
          SpriteDetails.YELLOW_BEARD_02,
          SpriteDetails.YELLOW_BEARD_01,
          SpriteDetails.YELLOW_BEARD_03},
      new SpriteDetails[]{
          SpriteDetails.YELLOW_BEARD_01_HIT,
          SpriteDetails.YELLOW_BEARD_02_HIT,
          SpriteDetails.YELLOW_BEARD_01_HIT,
          SpriteDetails.YELLOW_BEARD_03_HIT},
      0.20f
  ),

  PILOT(
      new SpriteDetails[]{
          SpriteDetails.PILOT_01,
          SpriteDetails.PILOT_02,
          SpriteDetails.PILOT_01,
          SpriteDetails.PILOT_03},
      new SpriteDetails[]{
          SpriteDetails.PILOT_01_HIT,
          SpriteDetails.PILOT_02_HIT,
          SpriteDetails.PILOT_01_HIT,
          SpriteDetails.PILOT_03_HIT},
      0.20f
  ),

  ARACNOID(
      new SpriteDetails[]{
          SpriteDetails.ARACNOID_01,
          SpriteDetails.ARACNOID_02,
          SpriteDetails.ARACNOID_01,
          SpriteDetails.ARACNOID_03},
      new SpriteDetails[]{
          SpriteDetails.ARACNOID_01_HIT,
          SpriteDetails.ARACNOID_02_HIT,
          SpriteDetails.ARACNOID_01_HIT,
          SpriteDetails.ARACNOID_03_HIT},
      0.20f
  ),

  CRAB(
      new SpriteDetails[]{
          SpriteDetails.CRAB_01,
          SpriteDetails.CRAB_02,
          SpriteDetails.CRAB_01,
          SpriteDetails.CRAB_03},
      new SpriteDetails[]{
          SpriteDetails.CRAB_01_HIT,
          SpriteDetails.CRAB_02_HIT,
          SpriteDetails.CRAB_01_HIT,
          SpriteDetails.CRAB_03_HIT},
      0.20f
  ),

  BEAR(
      new SpriteDetails[]{
          SpriteDetails.BEAR_01,
          SpriteDetails.BEAR_02,
          SpriteDetails.BEAR_01,
          SpriteDetails.BEAR_03},
      new SpriteDetails[]{
          SpriteDetails.BEAR_01_HIT,
          SpriteDetails.BEAR_02_HIT,
          SpriteDetails.BEAR_01_HIT,
          SpriteDetails.BEAR_03_HIT},
      0.20f
  ),

  DINO(
      new SpriteDetails[]{
          SpriteDetails.DINO_01,
          SpriteDetails.DINO_02,
          SpriteDetails.DINO_03,
          SpriteDetails.DINO_04,
          SpriteDetails.DINO_03,
          SpriteDetails.DINO_02},
      new SpriteDetails[]{
          SpriteDetails.DINO_01_HIT,
          SpriteDetails.DINO_02_HIT,
          SpriteDetails.DINO_03_HIT,
          SpriteDetails.DINO_04_HIT,
          SpriteDetails.DINO_03_HIT,
          SpriteDetails.DINO_02_HIT},
      0.20f
  ),

  FRISBIE(
      new SpriteDetails[]{
          SpriteDetails.FRISBIE_01,
          SpriteDetails.FRISBIE_02,
          SpriteDetails.FRISBIE_03,
          SpriteDetails.FRISBIE_04,
          SpriteDetails.FRISBIE_05},
      new SpriteDetails[]{
          SpriteDetails.FRISBIE_01_HIT,
          SpriteDetails.FRISBIE_02_HIT,
          SpriteDetails.FRISBIE_03_HIT,
          SpriteDetails.FRISBIE_04_HIT,
          SpriteDetails.FRISBIE_05_HIT},
      0.20f
  ),

  JOKER(
      new SpriteDetails[]{
          SpriteDetails.JOKER_01,
          SpriteDetails.JOKER_02,
          SpriteDetails.JOKER_03,
          SpriteDetails.JOKER_02},
      new SpriteDetails[]{
          SpriteDetails.JOKER_01_HIT,
          SpriteDetails.JOKER_02_HIT,
          SpriteDetails.JOKER_03_HIT,
          SpriteDetails.JOKER_02_HIT},
      0.20f
  ),

  // octopus alien with flapping legs
  OCTOPUS(
      new SpriteDetails[]{
          SpriteDetails.OCTOPUS_LEFT,
          SpriteDetails.OCTOPUS_RIGHT},
      new SpriteDetails[]{
          SpriteDetails.OCTOPUS_LEFT_HIT,
          SpriteDetails.OCTOPUS_RIGHT_HIT},
      0.5f),

  MINION(
      new SpriteDetails[]{
          SpriteDetails.MINION},
      new SpriteDetails[]{
          SpriteDetails.MINION_HIT},
      0f),

  // green spinning balls
  SPINNER_GREEN(
      new SpriteDetails[]{
          SpriteDetails.SPINNER_GREEN_01,
          SpriteDetails.SPINNER_GREEN_02,
          SpriteDetails.SPINNER_GREEN_03},
      new SpriteDetails[]{
          SpriteDetails.SPINNER_GREEN_01_HIT,
          SpriteDetails.SPINNER_GREEN_02_HIT,
          SpriteDetails.SPINNER_GREEN_03_HIT},
      0.05f),

  // green pulsating spinning balls
  SPINNER_PULSE_GREEN(
      new SpriteDetails[]{
          SpriteDetails.SPINNER_PULSE_GREEN_01,
          SpriteDetails.SPINNER_PULSE_GREEN_02,
          SpriteDetails.SPINNER_PULSE_GREEN_03},
      new SpriteDetails[]{
          SpriteDetails.SPINNER_PULSE_GREEN_01_HIT,
          SpriteDetails.SPINNER_PULSE_GREEN_02_HIT,
          SpriteDetails.SPINNER_PULSE_GREEN_03_HIT},
      0.2f),

  // molecule - made from joined spheres
  MOLECULE(
      new SpriteDetails[]{
          SpriteDetails.MOLECULE},
      new SpriteDetails[]{
          SpriteDetails.MOLECULE_HIT},
      0f),
  MOLECULE_MINI(
      new SpriteDetails[]{
          SpriteDetails.MOLECULE_MINI},
      new SpriteDetails[]{
          SpriteDetails.MOLECULE_MINI_HIT},
      0f),

  // big boss alien with claws
  BIG_BOSS(
      new SpriteDetails[]{
          SpriteDetails.BIG_BOSS_01,
          SpriteDetails.BIG_BOSS_02},
      new SpriteDetails[]{
          SpriteDetails.BIG_BOSS_01_HIT,
          SpriteDetails.BIG_BOSS_02_HIT},
      0.25f),

  // flapping lady bird
  LADY_BIRD(
      new SpriteDetails[]{
          SpriteDetails.LADYBIRD_01,
          SpriteDetails.LADYBIRD_02},
      new SpriteDetails[]{
          SpriteDetails.LADYBIRD_01_HIT,
          SpriteDetails.LADYBIRD_02_HIT},
      0.25f),


  // rocky asteroid
  ASTEROID(
      new SpriteDetails[]{
          SpriteDetails.ASTEROID},
      new SpriteDetails[]{
          SpriteDetails.ASTEROID_HIT},
      0f),

  // small rocky asteroid
  ASTEROID_MINI(
      new SpriteDetails[]{
          SpriteDetails.ASTEROID_MINI},
      new SpriteDetails[]{
          SpriteDetails.ASTEROID_MINI_HIT},
      0f),

  // dragon
  DRAGON_HEAD(
      new SpriteDetails[]{
          SpriteDetails.DRAGON_HEAD_LEFT,
          SpriteDetails.DRAGON_HEAD_RIGHT},
      new SpriteDetails[]{
          SpriteDetails.DRAGON_HEAD_LEFT_HIT,
          SpriteDetails.DRAGON_HEAD_RIGHT_HIT},
      0.5f),

  DRAGON_BODY(
      new SpriteDetails[]{
          SpriteDetails.DRAGON_BODY},
      new SpriteDetails[]{
          SpriteDetails.DRAGON_BODY_HIT},
      0.5f),

  // baby dragon
  BABY_DRAGON_HEAD(
      new SpriteDetails[]{
          SpriteDetails.BABY_DRAGON_HEAD_LEFT,
          SpriteDetails.BABY_DRAGON_HEAD_RIGHT},
      new SpriteDetails[]{
          SpriteDetails.BABY_DRAGON_HEAD_LEFT_HIT,
          SpriteDetails.BABY_DRAGON_HEAD_RIGHT_HIT},
      0.5f),

  BABY_DRAGON_BODY(
      new SpriteDetails[]{
          SpriteDetails.BABY_DRAGON_BODY},
      new SpriteDetails[]{
          SpriteDetails.BABY_DRAGON_BODY_HIT},
      0.5f),

  // skull
  SKULL(
      new SpriteDetails[]{
          SpriteDetails.SKULL},
      new SpriteDetails[]{
          SpriteDetails.SKULL_HIT},
      0f),

  // droid
  DROID(
      new SpriteDetails[]{
          SpriteDetails.DROID},
      new SpriteDetails[]{
          SpriteDetails.DROID_HIT},
      0f),

  // big flapping mothership alien
  INSECT_MOTHERSHIP(
      new SpriteDetails[]{
          SpriteDetails.MOTHER_BUZZER_WINGS_DOWN,
          SpriteDetails.MOTHER_BUZZER_WINGS_UP},
      new SpriteDetails[]{
          SpriteDetails.MOTHER_BUZZER_WINGS_DOWN_HIT,
          SpriteDetails.MOTHER_BUZZER_WINGS_UP_HIT},
      0.5f),

  // flying insects
  INSECT(
      new SpriteDetails[]{
          SpriteDetails.INSECT_WINGS_UP,
          SpriteDetails.INSECT_WINGS_DOWN},
      new SpriteDetails[]{
          SpriteDetails.INSECT_WINGS_UP,
          SpriteDetails.INSECT_WINGS_DOWN},
      0.5f),

  // gobby alien
  GOBBY(
      new SpriteDetails[]{
          SpriteDetails.GOBBY_LEFT,
          SpriteDetails.GOBBY_RIGHT},
      new SpriteDetails[]{
          SpriteDetails.GOBBY_LEFT_HIT,
          SpriteDetails.GOBBY_RIGHT_HIT},
      0.5f),

  // flapping book
  BOOK(
      new SpriteDetails[]{
          SpriteDetails.BOOK_FLAT,
          SpriteDetails.BOOK_BEND,
          SpriteDetails.BOOK_CLOSED,
          SpriteDetails.BOOK_BEND},
      new SpriteDetails[]{
          SpriteDetails.BOOK_FLAT_HIT,
          SpriteDetails.BOOK_BEND_HIT,
          SpriteDetails.BOOK_CLOSED_HIT,
          SpriteDetails.BOOK_BEND_HIT},
      0.2f),

  // big blobby alien
  ZOGG(
      new SpriteDetails[]{
          SpriteDetails.ZOGG_UP,
          SpriteDetails.ZOGG_DOWN},
      new SpriteDetails[]{
          SpriteDetails.ZOGG_UP_HIT,
          SpriteDetails.ZOGG_DOWN_HIT},
      0.5f),

  // bomb
  BOMB(
      new SpriteDetails[]{
          SpriteDetails.BOMB_01,
          SpriteDetails.BOMB_02,
          SpriteDetails.BOMB_03},
      new SpriteDetails[]{
          SpriteDetails.BOMB_01_HIT,
          SpriteDetails.BOMB_02_HIT,
          SpriteDetails.BOMB_03_HIT},
      0.5f),

  // circuit board
  CIRCUIT(
      new SpriteDetails[]{
          SpriteDetails.CIRCUIT_LEFT,
          SpriteDetails.CIRCUIT_RIGHT},
      new SpriteDetails[]{
          SpriteDetails.CIRCUIT_LEFT_HIT,
          SpriteDetails.CIRCUIT_RIGHT_HIT},
      0.5f),

  // flying fire bat
  BATTY(
      new SpriteDetails[]{
          SpriteDetails.BATTY_FLAP_DOWN,
          SpriteDetails.BATTY_FLAP_UP},
      new SpriteDetails[]{
          SpriteDetails.BATTY_FLAP_DOWN_HIT,
          SpriteDetails.BATTY_FLAP_UP_HIT},
      0.2f),

  // circuit board
  CLOUD(
      new SpriteDetails[]{
          SpriteDetails.CLOUD},
      new SpriteDetails[]{
          SpriteDetails.CLOUD_HIT},
      0f,
      new SpriteDetails[]{
          SpriteDetails.CLOUD_EXPLODE_01,
          SpriteDetails.CLOUD_EXPLODE_02,
          SpriteDetails.CLOUD_EXPLODE_03,
          SpriteDetails.CLOUD_EXPLODE_04,
          SpriteDetails.CLOUD_EXPLODE_05},
      0.075f),

  // green bouncy alien
  BOUNCER(
      new SpriteDetails[]{
          SpriteDetails.BOUNCER_IN,
          SpriteDetails.BOUNCER_OUT},
      new SpriteDetails[]{
          SpriteDetails.BOUNCER_IN_HIT,
          SpriteDetails.BOUNCER_OUT_HIT},
      0.2f),

  // alien with droopy tendrils
  DROOPY(
      new SpriteDetails[]{
          SpriteDetails.DROOPY_UP,
          SpriteDetails.DROOPY_DOWN},
      new SpriteDetails[]{
          SpriteDetails.DROOPY_UP_HIT,
          SpriteDetails.DROOPY_DOWN_HIT},
      0.2f),

  // evil yellow cat
//    BAD_CAT(
//            new SpriteDetails[]{
//                    SpriteDetails.BAD_CAT},
//            new SpriteDetails[]{
//                    SpriteDetails.BAD_CAT_HIT},
//            0f),
  BAD_CAT(
      new SpriteDetails[]{
          SpriteDetails.BAD_CAT_SQUEEZE,
          SpriteDetails.BAD_CAT_SQUEEZE_WIDE,
          SpriteDetails.BAD_CAT_SQUEEZE,
          SpriteDetails.BAD_CAT_SQUEEZE_TALL},
      new SpriteDetails[]{
          SpriteDetails.BAD_CAT_SQUEEZE_HIT,
          SpriteDetails.BAD_CAT_SQUEEZE_WIDE_HIT,
          SpriteDetails.BAD_CAT_SQUEEZE_HIT,
          SpriteDetails.BAD_CAT_SQUEEZE_TALL_HIT},
      0.25f),

  SMOKEY(
      new SpriteDetails[]{
          SpriteDetails.SMOKEY_FLAME_NONE,
          SpriteDetails.SMOKEY_FLAME_SMALL,
          SpriteDetails.SMOKEY_FLAME_BIG,
          SpriteDetails.SMOKEY_FLAME_SMALL},
      new SpriteDetails[]{
          SpriteDetails.SMOKEY_FLAME_NONE_HIT,
          SpriteDetails.SMOKEY_FLAME_SMALL_HIT,
          SpriteDetails.SMOKEY_FLAME_BIG_HIT,
          SpriteDetails.SMOKEY_FLAME_SMALL_HIT},
      0.2f),

  TELLY(
      new SpriteDetails[]{
          SpriteDetails.TELLY_FUZZ_ONE,
          SpriteDetails.TELLY_FUZZ_TWO},
      new SpriteDetails[]{
          SpriteDetails.TELLY_FUZZ_ONE_HIT,
          SpriteDetails.TELLY_FUZZ_TWO_HIT},
      0.3f),

  HELMET(
      new SpriteDetails[]{
          SpriteDetails.HELMET},
      new SpriteDetails[]{
          SpriteDetails.HELMET_HIT},
      0f),

  EGG(
      new SpriteDetails[]{
          SpriteDetails.EGG_CRACK_01,
          SpriteDetails.EGG_CRACK_02,
          SpriteDetails.EGG_CRACK_03,
          SpriteDetails.EGG_CRACK_02,
          SpriteDetails.EGG_CRACK_03,
          SpriteDetails.EGG_CRACK_02,
          SpriteDetails.EGG_CRACK_03,
          SpriteDetails.EGG_CRACK_02,
          SpriteDetails.EGG_CRACK_03},
      new SpriteDetails[]{
          SpriteDetails.EGG_CRACK_01_HIT,
          SpriteDetails.EGG_CRACK_02_HIT,
          SpriteDetails.EGG_CRACK_03_HIT,
          SpriteDetails.EGG_CRACK_02_HIT,
          SpriteDetails.EGG_CRACK_03_HIT,
          SpriteDetails.EGG_CRACK_02_HIT,
          SpriteDetails.EGG_CRACK_03_HIT,
          SpriteDetails.EGG_CRACK_02_HIT,
          SpriteDetails.EGG_CRACK_03_HIT},
      0.25f,
      new SpriteDetails[]{
          SpriteDetails.EGG_CRACK_04,
          SpriteDetails.EGG_CRACK_05,
          SpriteDetails.EGG_CRACK_06,
          SpriteDetails.EGG_CRACK_07},
      0.075f),

  BARRIER(
      new SpriteDetails[]{
          SpriteDetails.BARRIER_01,
          SpriteDetails.BARRIER_02,
          SpriteDetails.BARRIER_03},
      new SpriteDetails[]{
          SpriteDetails.BARRIER_01,
          SpriteDetails.BARRIER_02,
          SpriteDetails.BARRIER_03},
      0.75f),

  SPACE_STATION(
      new SpriteDetails[]{
          SpriteDetails.SPACE_STATION},
      new SpriteDetails[]{
          SpriteDetails.SPACE_STATION_HIT},
      0f),

  BUBBLE(
      new SpriteDetails[]{
          SpriteDetails.BUBBLE_01,
          SpriteDetails.BUBBLE_02,
          SpriteDetails.BUBBLE_01,
          SpriteDetails.BUBBLE_03},
      new SpriteDetails[]{
          SpriteDetails.BUBBLE_01_HIT,
          SpriteDetails.BUBBLE_02_HIT,
          SpriteDetails.BUBBLE_01_HIT,
          SpriteDetails.BUBBLE_03_HIT},
      0.20f
  ),

  BUSHEY(
      new SpriteDetails[]{
          SpriteDetails.BUSHEY_01,
          SpriteDetails.BUSHEY_02,
          SpriteDetails.BUSHEY_01,
          SpriteDetails.BUSHEY_03},
      new SpriteDetails[]{
          SpriteDetails.BUSHEY_01_HIT,
          SpriteDetails.BUSHEY_02_HIT,
          SpriteDetails.BUSHEY_01_HIT,
          SpriteDetails.BUSHEY_03_HIT},
      0.20f
  ),

  CHOPPY(
      new SpriteDetails[]{
          SpriteDetails.CHOPPY_01,
          SpriteDetails.CHOPPY_02,
          SpriteDetails.CHOPPY_03,
          SpriteDetails.CHOPPY_02},
      new SpriteDetails[]{
          SpriteDetails.CHOPPY_01_HIT,
          SpriteDetails.CHOPPY_02_HIT,
          SpriteDetails.CHOPPY_03_HIT,
          SpriteDetails.CHOPPY_02_HIT},
      0.20f
  ),

  CLOWN(
      new SpriteDetails[]{
          SpriteDetails.CLOWN_01,
          SpriteDetails.CLOWN_02,
          SpriteDetails.CLOWN_03,
          SpriteDetails.CLOWN_02},
      new SpriteDetails[]{
          SpriteDetails.CLOWN_01_HIT,
          SpriteDetails.CLOWN_02_HIT,
          SpriteDetails.CLOWN_03_HIT,
          SpriteDetails.CLOWN_02_HIT},
      0.20f
  ),

  CROSSER(
      new SpriteDetails[]{
          SpriteDetails.CROSSER_01,
          SpriteDetails.CROSSER_02,
          SpriteDetails.CROSSER_03,
          SpriteDetails.CROSSER_02},
      new SpriteDetails[]{
          SpriteDetails.CROSSER_01_HIT,
          SpriteDetails.CROSSER_02_HIT,
          SpriteDetails.CROSSER_03_HIT,
          SpriteDetails.CROSSER_02_HIT},
      0.20f
  ),

  FROG_KING(
      new SpriteDetails[]{
          SpriteDetails.FROG_KING_01,
          SpriteDetails.FROG_KING_02,
          SpriteDetails.FROG_KING_03,
          SpriteDetails.FROG_KING_02},
      new SpriteDetails[]{
          SpriteDetails.FROG_KING_01_HIT,
          SpriteDetails.FROG_KING_02_HIT,
          SpriteDetails.FROG_KING_03_HIT,
          SpriteDetails.FROG_KING_02_HIT},
      0.20f
  ),

  GREEN_BOY(
      new SpriteDetails[]{
          SpriteDetails.GREEN_BOY_01,
          SpriteDetails.GREEN_BOY_02,
          SpriteDetails.GREEN_BOY_03,
          SpriteDetails.GREEN_BOY_02},
      new SpriteDetails[]{
          SpriteDetails.GREEN_BOY_01_HIT,
          SpriteDetails.GREEN_BOY_02_HIT,
          SpriteDetails.GREEN_BOY_03_HIT,
          SpriteDetails.GREEN_BOY_02_HIT},
      0.20f
  ),

  JELLYFISH(
      new SpriteDetails[]{
          SpriteDetails.JELLYFISH_01,
          SpriteDetails.JELLYFISH_02,
          SpriteDetails.JELLYFISH_03,
          SpriteDetails.JELLYFISH_02},
      new SpriteDetails[]{
          SpriteDetails.JELLYFISH_01_HIT,
          SpriteDetails.JELLYFISH_02_HIT,
          SpriteDetails.JELLYFISH_03_HIT,
          SpriteDetails.JELLYFISH_02_HIT},
      0.20f
  ),

  SPIRAL(
      new SpriteDetails[]{
          SpriteDetails.SPIRAL_01,
          SpriteDetails.SPIRAL_02,
          SpriteDetails.SPIRAL_03,
          SpriteDetails.SPIRAL_04,
          SpriteDetails.SPIRAL_05},
      new SpriteDetails[]{
          SpriteDetails.SPIRAL_01_HIT,
          SpriteDetails.SPIRAL_02_HIT,
          SpriteDetails.SPIRAL_03_HIT,
          SpriteDetails.SPIRAL_04_HIT,
          SpriteDetails.SPIRAL_05_HIT},
      0.20f
  ),

  SWEEP(
      new SpriteDetails[]{
          SpriteDetails.SWEEP_01,
          SpriteDetails.SWEEP_02,
          SpriteDetails.SWEEP_03,
          SpriteDetails.SWEEP_02},
      new SpriteDetails[]{
          SpriteDetails.SWEEP_01_HIT,
          SpriteDetails.SWEEP_02_HIT,
          SpriteDetails.SWEEP_03_HIT,
          SpriteDetails.SWEEP_02_HIT},
      0.20f
  ),

  ZOOM(
      new SpriteDetails[]{
          SpriteDetails.ZOOM_01,
          SpriteDetails.ZOOM_02,
          SpriteDetails.ZOOM_03,
          SpriteDetails.ZOOM_02},
      new SpriteDetails[]{
          SpriteDetails.ZOOM_01_HIT,
          SpriteDetails.ZOOM_02_HIT,
          SpriteDetails.ZOOM_03_HIT,
          SpriteDetails.ZOOM_02_HIT},
      0.20f
  );

  private static final float DEFAULT_EXPLOSION_FRAME_DURATION = 0.075f;

  // alien animation frames
  private final SpriteDetails[] animationFrames;

  // alien animation frames when hit - to be in sync with normal animation frames
  private final SpriteDetails[] hitAnimationFrames;

  // time between frame transitions
  private final float frameDuration;

  // alien animation frames when exploding
  private final SpriteDetails[] explosionAnimationFrames;
  private final float explosionFrameDuration;

  AlienCharacter(
      final SpriteDetails[] animationFrames,
      final SpriteDetails[] hitAnimationFrames,
      final float frameDuration) {
    this(
        animationFrames,
        hitAnimationFrames,
        frameDuration,
        new SpriteDetails[]{
            SpriteDetails.EXPLODE_BIG_01,
            SpriteDetails.EXPLODE_BIG_02,
            SpriteDetails.EXPLODE_BIG_03,
            SpriteDetails.EXPLODE_BIG_04,
            SpriteDetails.EXPLODE_BIG_05},
        DEFAULT_EXPLOSION_FRAME_DURATION);
  }

  AlienCharacter(
      final SpriteDetails[] animationFrames,
      final SpriteDetails[] hitAnimationFrames,
      final float frameDuration,
      final SpriteDetails[] explosionAnimationFrames,
      final float explosionFrameDuration) {
    this.animationFrames = animationFrames;
    this.hitAnimationFrames = hitAnimationFrames;
    this.frameDuration = frameDuration;
    this.explosionAnimationFrames = explosionAnimationFrames;
    this.explosionFrameDuration = explosionFrameDuration;
  }

  public Animation getAnimation() {
    return new Animation(frameDuration, animationFrames);
  }

  public Animation getHitAnimation() {
    return new Animation(frameDuration, hitAnimationFrames);
  }

  public Animation getExplosionAnimation() {
    return new Animation(explosionFrameDuration, explosionAnimationFrames);
  }
}
