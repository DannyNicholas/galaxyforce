package com.danosoftware.galaxyforce.sprites.properties;

import com.danosoftware.galaxyforce.textures.TextureRegion;

import java.util.EnumMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public enum SpriteDetails {

  // Block
  BLOCK_01,
  BLOCK_02,

  // Foxy
  FOXY,
  FOXY_TALL,
  FOXY_WIDE,
  FOXY_HIT,
  FOXY_TALL_HIT,
  FOXY_WIDE_HIT,

  // Cyclone
  CYCLONE_01,
  CYCLONE_02,
  CYCLONE_03,
  CYCLONE_04,
  CYCLONE_05,
  CYCLONE_01_HIT,
  CYCLONE_02_HIT,
  CYCLONE_03_HIT,
  CYCLONE_04_HIT,
  CYCLONE_05_HIT,

  // Devil
  DEVIL,
  DEVIL_UP,
  DEVIL_DOWN,
  DEVIL_HIT,
  DEVIL_UP_HIT,
  DEVIL_DOWN_HIT,

  // Gremlin
  GREMLIN_01,
  GREMLIN_02,
  GREMLIN_03,
  GREMLIN_04,
  GREMLIN_01_HIT,
  GREMLIN_02_HIT,
  GREMLIN_03_HIT,
  GREMLIN_04_HIT,

  // Pincer
  PINCER,
  PINCER_LEFT,
  PINCER_RIGHT,
  PINCER_HIT,
  PINCER_LEFT_HIT,
  PINCER_RIGHT_HIT,

  // Sparkle
  SPARKLE,
  SPARKLE_TALL,
  SPARKLE_WIDE,
  SPARKLE_HIT,
  SPARKLE_TALL_HIT,
  SPARKLE_WIDE_HIT,

  // Spectator
  SPECTATOR,
  SPECTATOR_UP,
  SPECTATOR_DOWN,
  SPECTATOR_HIT,
  SPECTATOR_UP_HIT,
  SPECTATOR_DOWN_HIT,

  // Squasher
  SQUASHER,
  SQUASHER_WIDE,
  SQUASHER_TALL,
  SQUASHER_HIT,
  SQUASHER_WIDE_HIT,
  SQUASHER_TALL_HIT,

  // Tiny Dancer
  TINY_DANCER,
  TINY_DANCER_LEFT,
  TINY_DANCER_RIGHT,
  TINY_DANCER_HIT,
  TINY_DANCER_LEFT_HIT,
  TINY_DANCER_RIGHT_HIT,

  // Wild Style
  WILD_STYLE,
  WILD_STYLE_FLAT,
  WILD_STYLE_TALL,
  WILD_STYLE_HIT,
  WILD_STYLE_FLAT_HIT,
  WILD_STYLE_TALL_HIT,

  // Confuser
  CONFUSER,
  CONFUSER_FLAT,
  CONFUSER_TALL,
  CONFUSER_HIT,
  CONFUSER_FLAT_HIT,
  CONFUSER_TALL_HIT,

  // Saucer
  SAUCER,
  SAUCER_RIGHT,
  SAUCER_LEFT,
  SAUCER_HIT,
  SAUCER_RIGHT_HIT,
  SAUCER_LEFT_HIT,

  // Pinko
  PINKO,
  PINKO_TALL,
  PINKO_WIDE,
  PINKO_HIT,
  PINKO_TALL_HIT,
  PINKO_WIDE_HIT,

  // Piston
  PISTON,
  PISTON_TALL,
  PISTON_WIDE,
  PISTON_HIT,
  PISTON_TALL_HIT,
  PISTON_WIDE_HIT,

  // Hopper
  HOPPER,
  HOPPER_LEFT,
  HOPPER_RIGHT,
  HOPPER_HIT,
  HOPPER_LEFT_HIT,
  HOPPER_RIGHT_HIT,

  // Ghost
  GHOST,
  GHOST_LEFT,
  GHOST_RIGHT,
  GHOST_HIT,
  GHOST_LEFT_HIT,
  GHOST_RIGHT_HIT,

  // Anomeba
  AMOEBA,
  AMOEBA_LEFT,
  AMOEBA_RIGHT,
  AMOEBA_HIT,
  AMOEBA_LEFT_HIT,
  AMOEBA_RIGHT_HIT,

  // Cheeky
  CHEEKY,
  CHEEKY_LEFT,
  CHEEKY_RIGHT,
  CHEEKY_HIT,
  CHEEKY_LEFT_HIT,
  CHEEKY_RIGHT_HIT,

  // Purple Meanie
  PURPLE_MEANIE_01,
  PURPLE_MEANIE_02,
  PURPLE_MEANIE_03,
  PURPLE_MEANIE_04,
  PURPLE_MEANIE_05,
  PURPLE_MEANIE_01_HIT,
  PURPLE_MEANIE_02_HIT,
  PURPLE_MEANIE_03_HIT,
  PURPLE_MEANIE_04_HIT,
  PURPLE_MEANIE_05_HIT,

  // Pad
  PAD_01,
  PAD_02,
  PAD_03,
  PAD_04,
  PAD_05,
  PAD_01_HIT,
  PAD_02_HIT,
  PAD_03_HIT,
  PAD_04_HIT,
  PAD_05_HIT,

  // Jumper
  JUMPER,
  JUMPER_UP,
  JUMPER_DOWN,
  JUMPER_HIT,
  JUMPER_UP_HIT,
  JUMPER_DOWN_HIT,

  // Ripley
  RIPLEY,
  RIPLEY_LEFT,
  RIPLEY_RIGHT,
  RIPLEY_HIT,
  RIPLEY_LEFT_HIT,
  RIPLEY_RIGHT_HIT,

  // All Seeing Eye
  ALL_SEEING_EYE,
  ALL_SEEING_EYE_LEFT,
  ALL_SEEING_EYE_RIGHT,
  ALL_SEEING_EYE_HIT,
  ALL_SEEING_EYE_LEFT_HIT,
  ALL_SEEING_EYE_RIGHT_HIT,

  // Charlie
  CHARLIE,
  CHARLIE_LEFT,
  CHARLIE_RIGHT,
  CHARLIE_HIT,
  CHARLIE_LEFT_HIT,
  CHARLIE_RIGHT_HIT,

  // Walker
  WALKER,
  WALKER_RIGHT,
  WALKER_LEFT,
  WALKER_HIT,
  WALKER_RIGHT_HIT,
  WALKER_LEFT_HIT,

  // Frogger
  FROGGER,
  FROGGER_TALL,
  FROGGER_WIDE,
  FROGGER_HIT,
  FROGGER_TALL_HIT,
  FROGGER_WIDE_HIT,

  // Fighter
  FIGHTER,
  FIGHTER_TALL,
  FIGHTER_WIDE,
  FIGHTER_HIT,
  FIGHTER_TALL_HIT,
  FIGHTER_WIDE_HIT,

  // Rotator
  ROTATOR_01,
  ROTATOR_02,
  ROTATOR_03,
  ROTATOR_04,
  ROTATOR_05,
  ROTATOR_01_HIT,
  ROTATOR_02_HIT,
  ROTATOR_03_HIT,
  ROTATOR_04_HIT,
  ROTATOR_05_HIT,

  // Whirlpool
  WHIRLPOOL_01,
  WHIRLPOOL_02,
  WHIRLPOOL_03,
  WHIRLPOOL_04,
  WHIRLPOOL_05,
  WHIRLPOOL_01_HIT,
  WHIRLPOOL_02_HIT,
  WHIRLPOOL_03_HIT,
  WHIRLPOOL_04_HIT,
  WHIRLPOOL_05_HIT,

  // Battle Droid
  BATTLE_DROID_01,
  BATTLE_DROID_02,
  BATTLE_DROID_03,
  BATTLE_DROID_01_HIT,
  BATTLE_DROID_02_HIT,
  BATTLE_DROID_03_HIT,

  // Fish
  FISH_01,
  FISH_02,
  FISH_03,
  FISH_01_HIT,
  FISH_02_HIT,
  FISH_03_HIT,

  // Lemming
  LEMMING_01,
  LEMMING_02,
  LEMMING_03,
  LEMMING_01_HIT,
  LEMMING_02_HIT,
  LEMMING_03_HIT,

  // Squeeze Box
  SQUEEZE_BOX_01,
  SQUEEZE_BOX_02,
  SQUEEZE_BOX_03,
  SQUEEZE_BOX_01_HIT,
  SQUEEZE_BOX_02_HIT,
  SQUEEZE_BOX_03_HIT,

  // Yellow Beard
  YELLOW_BEARD_01,
  YELLOW_BEARD_02,
  YELLOW_BEARD_03,
  YELLOW_BEARD_01_HIT,
  YELLOW_BEARD_02_HIT,
  YELLOW_BEARD_03_HIT,

  // Pilot
  PILOT_01,
  PILOT_02,
  PILOT_03,
  PILOT_01_HIT,
  PILOT_02_HIT,
  PILOT_03_HIT,

  // Aracnoid
  ARACNOID_01,
  ARACNOID_02,
  ARACNOID_03,
  ARACNOID_01_HIT,
  ARACNOID_02_HIT,
  ARACNOID_03_HIT,

  // Crab
  CRAB_01,
  CRAB_02,
  CRAB_03,
  CRAB_01_HIT,
  CRAB_02_HIT,
  CRAB_03_HIT,

  // Bear
  BEAR_01,
  BEAR_02,
  BEAR_03,
  BEAR_01_HIT,
  BEAR_02_HIT,
  BEAR_03_HIT,

  // Dino
  DINO_01,
  DINO_02,
  DINO_03,
  DINO_04,
  DINO_01_HIT,
  DINO_02_HIT,
  DINO_03_HIT,
  DINO_04_HIT,

  // Frisbie
  FRISBIE_01,
  FRISBIE_02,
  FRISBIE_03,
  FRISBIE_04,
  FRISBIE_05,
  FRISBIE_01_HIT,
  FRISBIE_02_HIT,
  FRISBIE_03_HIT,
  FRISBIE_04_HIT,
  FRISBIE_05_HIT,

  // Joker
  JOKER_01,
  JOKER_02,
  JOKER_03,
  JOKER_01_HIT,
  JOKER_02_HIT,
  JOKER_03_HIT,

  // octopus
  OCTOPUS_LEFT,
  OCTOPUS_RIGHT,
  OCTOPUS_LEFT_HIT,
  OCTOPUS_RIGHT_HIT,

  // yellow minion
  MINION,
  MINION_HIT,

  // green ball spinner
  SPINNER_GREEN_01,
  SPINNER_GREEN_02,
  SPINNER_GREEN_03,
  SPINNER_GREEN_01_HIT,
  SPINNER_GREEN_02_HIT,
  SPINNER_GREEN_03_HIT,

  // pulsating green ball spinner
  SPINNER_PULSE_GREEN_01,
  SPINNER_PULSE_GREEN_02,
  SPINNER_PULSE_GREEN_03,
  SPINNER_PULSE_GREEN_01_HIT,
  SPINNER_PULSE_GREEN_02_HIT,
  SPINNER_PULSE_GREEN_03_HIT,

  // spinning space stations
  SPACE_STATION,
  SPACE_STATION_HIT,

  // pulsating barrier
  BARRIER_01,
  BARRIER_02,
  BARRIER_03,

  // molecule
  MOLECULE,
  MOLECULE_HIT,
  MOLECULE_MINI,
  MOLECULE_MINI_HIT,

  // big boss
  BIG_BOSS_01,
  BIG_BOSS_02,
  BIG_BOSS_01_HIT,
  BIG_BOSS_02_HIT,

  // flapping lady bird
  LADYBIRD_01,
  LADYBIRD_02,
  LADYBIRD_01_HIT,
  LADYBIRD_02_HIT,

  // zogg
  ZOGG_UP,
  ZOGG_DOWN,
  ZOGG_UP_HIT,
  ZOGG_DOWN_HIT,

  // asteroid
  ASTEROID,
  ASTEROID_HIT,

  // mini-asteroid
  ASTEROID_MINI,
  ASTEROID_MINI_HIT,

  // egg
  EGG_CRACK_01,
  EGG_CRACK_02,
  EGG_CRACK_03,
  EGG_CRACK_01_HIT,
  EGG_CRACK_02_HIT,
  EGG_CRACK_03_HIT,

  // egg explosion
  EGG_CRACK_04,
  EGG_CRACK_05,
  EGG_CRACK_06,
  EGG_CRACK_07,

  // dragon - head and body
  DRAGON_HEAD_LEFT,
  DRAGON_HEAD_RIGHT,
  DRAGON_HEAD_LEFT_HIT,
  DRAGON_HEAD_RIGHT_HIT,
  DRAGON_BODY,
  DRAGON_BODY_HIT,

  // baby dragon - head and body
  BABY_DRAGON_HEAD_LEFT,
  BABY_DRAGON_HEAD_RIGHT,
  BABY_DRAGON_HEAD_LEFT_HIT,
  BABY_DRAGON_HEAD_RIGHT_HIT,
  BABY_DRAGON_BODY,
  BABY_DRAGON_BODY_HIT,

  // buzzer - small flying insect
  INSECT_WINGS_UP,
  INSECT_WINGS_DOWN,
  INSECT_WINGS_UP_HIT,
  INSECT_WINGS_DOWN_HIT,

  // mother buzzer - big flying insect
  MOTHER_BUZZER_WINGS_DOWN,
  MOTHER_BUZZER_WINGS_UP,
  MOTHER_BUZZER_WINGS_DOWN_HIT,
  MOTHER_BUZZER_WINGS_UP_HIT,

  // gobby
  GOBBY_LEFT,
  GOBBY_RIGHT,
  GOBBY_LEFT_HIT,
  GOBBY_RIGHT_HIT,

  // bad cat
  BAD_CAT,
  BAD_CAT_HIT,

  // bad cat squeeze
  BAD_CAT_SQUEEZE,
  BAD_CAT_SQUEEZE_WIDE,
  BAD_CAT_SQUEEZE_TALL,
  BAD_CAT_SQUEEZE_HIT,
  BAD_CAT_SQUEEZE_WIDE_HIT,
  BAD_CAT_SQUEEZE_TALL_HIT,

  // bomb
  BOMB_01,
  BOMB_02,
  BOMB_03,
  BOMB_01_HIT,
  BOMB_02_HIT,
  BOMB_03_HIT,

  // batty
  BATTY_FLAP_DOWN,
  BATTY_FLAP_UP,
  BATTY_FLAP_DOWN_HIT,
  BATTY_FLAP_UP_HIT,

  // book
  BOOK_FLAT,
  BOOK_BEND,
  BOOK_CLOSED,
  BOOK_FLAT_HIT,
  BOOK_BEND_HIT,
  BOOK_CLOSED_HIT,

  // bouncer
  BOUNCER_IN,
  BOUNCER_OUT,
  BOUNCER_IN_HIT,
  BOUNCER_OUT_HIT,

  // droopy
  DROOPY_DOWN,
  DROOPY_UP,
  DROOPY_DOWN_HIT,
  DROOPY_UP_HIT,

  // circuit
  CIRCUIT_LEFT,
  CIRCUIT_RIGHT,
  CIRCUIT_LEFT_HIT,
  CIRCUIT_RIGHT_HIT,

  // smokey
  SMOKEY_FLAME_BIG,
  SMOKEY_FLAME_SMALL,
  SMOKEY_FLAME_NONE,
  SMOKEY_FLAME_BIG_HIT,
  SMOKEY_FLAME_SMALL_HIT,
  SMOKEY_FLAME_NONE_HIT,

  // telly
  TELLY_FUZZ_ONE,
  TELLY_FUZZ_TWO,
  TELLY_FUZZ_ONE_HIT,
  TELLY_FUZZ_TWO_HIT,

  // cloud
  CLOUD,
  CLOUD_HIT,

  // helmet
  HELMET,
  HELMET_HIT,

  // skull alien
  SKULL,
  SKULL_HIT,

  // droid alien
  DROID,
  DROID_HIT,

  // bases
  BASE,
  BASE_RIGHT,
  BASE_LEFT,
  HELPER,

  // pulsing shield when base stationary
  BASE_SHIELD_ONE,
  BASE_SHIELD_TWO,
  BASE_SHIELD_THREE,
  BASE_SHIELD_FOUR,

  // pulsing shield when base turning left
  BASE_LEFT_SHIELD_ONE,
  BASE_LEFT_SHIELD_TWO,
  BASE_LEFT_SHIELD_THREE,
  BASE_LEFT_SHIELD_FOUR,

  // pulsing shield when base turning right
  BASE_RIGHT_SHIELD_ONE,
  BASE_RIGHT_SHIELD_TWO,
  BASE_RIGHT_SHIELD_THREE,
  BASE_RIGHT_SHIELD_FOUR,

  // pulsing shield for helper base
  HELPER_SHIELD_ONE,
  HELPER_SHIELD_TWO,
  HELPER_SHIELD_THREE,
  HELPER_SHIELD_FOUR,

  // power ups
  POWERUP_LIFE,
  POWERUP_MISSILE_FAST,
  POWERUP_MISSILE_BLAST,
  POWERUP_MISSILE_GUIDED,
  POWERUP_MISSILE_PARALLEL,
  POWERUP_MISSILE_SPRAY,
  POWERUP_MISSILE_LASER,
  POWERUP_SHIELD,
  POWERUP_HELPER_BASES,

  // base missiles
  BASE_MISSILE,
  BASE_MISSILE_LASER,
  BASE_MISSILE_ROCKET,
  BASE_MISSILE_BLAST,

  // alien laser
  ALIEN_LASER,

  // fireball
  FIREBALL01,
  FIREBALL02,

  // lightning and rain missile
  LIGHTNING_01,
  LIGHTNING_02,
  RAIN_01,
  RAIN_02,

  // explosions
  EXPLODE_01,
  EXPLODE_02,
  EXPLODE_03,
  EXPLODE_04,
  EXPLODE_05,

  // cloud explosions
  CLOUD_EXPLODE_01,
  CLOUD_EXPLODE_02,
  CLOUD_EXPLODE_03,
  CLOUD_EXPLODE_04,
  CLOUD_EXPLODE_05,

  // big-explosions
  EXPLODE_BIG_01,
  EXPLODE_BIG_02,
  EXPLODE_BIG_03,
  EXPLODE_BIG_04,
  EXPLODE_BIG_05,

  // massive-explosions
  BASE_EXPLODE_01,
  BASE_EXPLODE_02,
  BASE_EXPLODE_03,
  BASE_EXPLODE_04,
  BASE_EXPLODE_05,
  BASE_EXPLODE_06,
  BASE_EXPLODE_07,
  BASE_EXPLODE_08,
  BASE_EXPLODE_09,
  BASE_EXPLODE_10,
  BASE_EXPLODE_11,

  // stars
  STAR,
  STAR_BLACK,
  STAR_RED,
  STAR_BLUE,
  STAR_SPARKLE,

  // pause buttons
  PAUSE_BUTTON_UP,
  PAUSE_BUTTON_DOWN,

  // wave flags
  FLAG_1,
  FLAG_5,
  FLAG_10,
  FLAG_50,

  // menu buttons
  MENU_BUTTON_UP,
  MENU_BUTTON_DOWN,

  // lives remaining
  LIVES,

  // main logo
  GALAXY_FORCE,

  // google play icon
  GOOGLE_PLAY,

  // menu buttons
  MAIN_MENU,
  MAIN_MENU_PRESSED,

  // wave select buttons
  LEVEL_FRAME,
  LEVEL_FRAME_PRESSED,

  // locked wave buttons
  LEVEL_FRAME_LOCKED,
  LEVEL_FRAME_LOCKED_PRESSED,

  // next zone buttons
  NEXT_LEVEL,
  NEXT_LEVEL_PRESSED,

  // previous zone buttons
  PREVIOUS_LEVEL,
  PREVIOUS_LEVEL_PRESSED,

  // option buttons
  OPTION_UNSELECTED,
  OPTION_SELECTED,

  // pluto
  PLUTO,

  // bases
  BASE_LARGE,
  BASE_LARGE_TILT,

  // fonts
  FONT_MAP,

  // null - transparent 1 x 1 sprite (essentially an invisible sprite)
  NULL;

  // sprite's texture region - represents image's position in texture map.
  // this will be initialised lazily once texture map is loaded.
  private TextureRegion textureRegion;

  // sprite's dimensions
  private SpriteDimensions spriteDimensions;

  public TextureRegion getTextureRegion() {
    return textureRegion;
  }

  private void setTextureRegion(TextureRegion textureRegion) {
    this.textureRegion = textureRegion;
  }

  public SpriteDimensions getDimensions() {
    return spriteDimensions;
  }

  private void setDimensions(SpriteDimensions spriteDimensions) {
    this.spriteDimensions = spriteDimensions;
  }

  /**
   * Change each sprite's texture regions and dimensions.
   * Normally triggered when the TextureMap changes.
   *
   * @param textureRegions
   * @param spriteDimensions
   */
  public static void initialise(
          EnumMap<SpriteDetails, TextureRegion> textureRegions,
          EnumMap<SpriteDetails, SpriteDimensions> spriteDimensions) {

    // remove any previously initialised sprite details
    for (SpriteDetails spriteDetails : SpriteDetails.values()) {
      spriteDetails.setTextureRegion(null);
      spriteDetails.setDimensions(null);
    }

    ExecutorService executor = Executors.newSingleThreadExecutor();

    // initialising is time consuming so spin-up a thread for this.
    // it is possible that later attempts to draw sprites may occur before they
    // are initialised.
    // If this happens, the sprites will be skipped by the draw routines.
    Runnable runnableTask = () -> {
      for (SpriteDetails spriteDetails : SpriteDetails.values()) {
        spriteDetails.setTextureRegion(textureRegions.get(spriteDetails));
        spriteDetails.setDimensions(spriteDimensions.get(spriteDetails));
      }
    };

    executor.execute(runnableTask);
    executor.shutdown();
  }
}
