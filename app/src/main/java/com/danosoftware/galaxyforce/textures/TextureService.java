package com.danosoftware.galaxyforce.textures;

import static com.danosoftware.galaxyforce.textures.TextureMap.GAME;
import static com.danosoftware.galaxyforce.textures.TextureMap.MENU;

import android.util.Log;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.exceptions.GalaxyForceException;
import com.danosoftware.galaxyforce.sprites.properties.GameSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.MenuSpriteIdentifier;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDetails;
import com.danosoftware.galaxyforce.sprites.properties.SpriteDimensions;
import com.danosoftware.galaxyforce.tasks.TaskService;
import com.danosoftware.galaxyforce.text.Font;
import java.util.EnumMap;

/**
 * Service to create or retrieve textures.
 */
public class TextureService {

  private static final String TAG = "Textures";

  // font glyphs per row - i.e. characters in a row within texture map
  private final static int FONT_GLYPHS_PER_ROW = 8;

  // font glyphs width - i.e. width of individual character
  private final static int FONT_GLYPHS_WIDTH = 30;

  // font glyphs height - i.e. height of individual character
  private final static int FONT_GLYPHS_HEIGHT = 38;

  // XML file loader
  private final TextureRegionXmlParser xmlParser;

  // PNG texture image file loader
  private final TextureLoader textureLoader;

  // textures for each TextureMap
  private final Texture menuTexture;
  private final Texture gameTexture;

  // texture regions for each TextureMap.
  // can't be populated until we load the texture for the first time
  // as we need to know the texture image's dimensions.
  private final EnumMap<SpriteDetails, TextureRegion> menuTextureRegions;
  private final EnumMap<SpriteDetails, TextureRegion> gameTextureRegions;

  // dimensions for each sprite within each TextureMap
  private final EnumMap<SpriteDetails, SpriteDimensions> menuSpriteDimensions;
  private final EnumMap<SpriteDetails, SpriteDimensions> gameSpriteDimensions;

  // fonts for each TextureMap
  private Font menuFont;
  private Font gameFont;

  // are the textures loaded?
  private boolean texturesLoaded;

  // are the regions and dimensions loaded?
  private boolean dimensionsLoaded;

  // are the fonts loaded?
  private boolean fontsLoaded;

  // current texture map enabled
  private TextureMap currentTextureMap;

  private final TaskService taskService;

  public TextureService(
      final TextureRegionXmlParser xmlParser,
      final TextureLoader textureLoader,
      TaskService taskService) {
    this.xmlParser = xmlParser;
    this.textureLoader = textureLoader;
    this.menuTexture = createTexture(MENU);
    this.gameTexture = createTexture(GAME);
    this.menuTextureRegions = new EnumMap<>(SpriteDetails.class);
    this.gameTextureRegions = new EnumMap<>(SpriteDetails.class);
    this.menuSpriteDimensions = new EnumMap<>(SpriteDetails.class);
    this.gameSpriteDimensions = new EnumMap<>(SpriteDetails.class);
    this.currentTextureMap = null;
    this.texturesLoaded = false;
    this.dimensionsLoaded = false;
    this.fontsLoaded = false;
    this.taskService = taskService;
  }

  /**
   * Retrieve a texture from cache if available. Otherwise create a new one.
   */
  public Texture getOrCreateTexture(TextureMap textureMap) {

    Log.i(TAG, "Selected texture map: " + textureMap.name());

    // typically we will need to load textures of the first attempt
    // to get a texture or following an application resume.
    // we can't load textures on construction as we must wait for the
    // surface view to be created first.
    if (!texturesLoaded) {
      reloadTextures();
    }

    boolean switchingTextures = textureMap != currentTextureMap;

    final Texture texture;
    switch (textureMap) {
      case GAME:
        texture = gameTexture;
        if (switchingTextures) {
          initaliseSprites(gameTextureRegions, gameSpriteDimensions);
        }
        break;
      case MENU:
        texture = menuTexture;
        if (switchingTextures) {
          initaliseSprites(menuTextureRegions, menuSpriteDimensions);
        }
        break;
      default:
        throw new GalaxyForceException("Unknown texture map: " + textureMap);
    }

    currentTextureMap = textureMap;
    return texture;
  }

  /**
   * Retrieve a font from cache if available. Otherwise create a new one.
   */
  public Font getOrCreateFont(TextureMap textureMap) {

    if (!texturesLoaded) {
      throw new GalaxyForceException(
          "Textures not loaded. Can not create font for texture map: " + textureMap.name());
    }

    if (!fontsLoaded) {
      createFonts();
      fontsLoaded = true;
    }

    final Font font;
    switch (textureMap) {
      case GAME:
        font = gameFont;
        break;
      case MENU:
        font = menuFont;
        break;
      default:
        throw new GalaxyForceException("Unknown texture map: " + textureMap);
    }

    return font;
  }

  private Texture createTexture(TextureMap textureMap) {
    Log.i(TAG, "Create new texture for: " + textureMap.name());
    return new Texture(xmlParser, textureLoader, textureMap);
  }

  /**
   * Dispose textures - typically when application is being paused. Textures will need be reloaded
   * and next time screen resumes.
   */
  public void disposeTextures() {
    menuTexture.dispose();
    gameTexture.dispose();
    texturesLoaded = false;
  }

  /**
   * Load textures - typically when application is resuming. Textures can be lost while the
   * application is paused.
   */
  public void reloadTextures() {
    menuTexture.load();
    gameTexture.load();
    texturesLoaded = true;

    // check if dimensions are loaded.
    // we only do this once per game.
    // we don't need to re-load every time with textures
    if (!dimensionsLoaded) {
      createTextureRegionsAndDimensions();
      dimensionsLoaded = true;
    }
  }

  /**
   * create each sprite's texture regions/dimensions. must be created after a texture is loaded and
   * before sprites can be displayed.
   */
  private void createTextureRegionsAndDimensions() {

    // create caches for menu sprites
    for (MenuSpriteIdentifier menuSprite : MenuSpriteIdentifier.values()) {
      TextureDetail textureDetail = menuTexture.getTextureDetail(menuSprite.getImageName());
      menuTextureRegions.put(
          menuSprite.getSprite(),
          createTextureRegion(menuTexture, textureDetail));
      menuSpriteDimensions.put(
          menuSprite.getSprite(),
          createMenuSpriteDimensions(textureDetail));
    }

    // create caches for game sprites
    for (GameSpriteIdentifier gameSprite : GameSpriteIdentifier.values()) {
      TextureDetail textureDetail = gameTexture.getTextureDetail(gameSprite.getImageName());
      gameTextureRegions.put(
          gameSprite.getSprite(),
          createTextureRegion(gameTexture, textureDetail));
      gameSpriteDimensions.put(
          gameSprite.getSprite(),
          createGameSpriteDimensions(textureDetail, gameSprite.getBoundsReduction()));
    }
  }

  /**
   * Create a texture region for a specific image within the texture map.
   *
   * @param texture       - loaded texture
   * @param textureDetail - sprite's texture details
   * @return texture region
   */
  private TextureRegion createTextureRegion(Texture texture, TextureDetail textureDetail) {
    return
        new TextureRegion(
            texture,
            textureDetail.getXPos(),
            textureDetail.getYPos(),
            textureDetail.getWidth(),
            textureDetail.getHeight());
  }

  /**
   * Create a sprite dimension for a specific image within the texture map.
   *
   * @param textureDetail - sprite's texture details
   * @return sprite dimension
   */
  private SpriteDimensions createMenuSpriteDimensions(TextureDetail textureDetail) {
    return
        new SpriteDimensions(
            textureDetail.getWidth(),
            textureDetail.getHeight());
  }

  /**
   * Create a sprite dimension for a specific image within the texture map.
   *
   * @param textureDetail   - sprite's texture details
   * @param boundsReduction - bounds to reduce sprite by
   * @return sprite dimension
   */
  private SpriteDimensions createGameSpriteDimensions(
      TextureDetail textureDetail,
      int boundsReduction) {
    return
        new SpriteDimensions(
            textureDetail.getWidth(),
            textureDetail.getHeight(),
            boundsReduction);
  }

  /**
   * Create fonts for menu and game
   */
  private void createFonts() {
    String gameFontImageName = GameSpriteIdentifier.FONT_MAP.getImageName();
    gameFont = createFont(gameTexture, gameFontImageName);

    String menuFontImageName = MenuSpriteIdentifier.FONT_MAP.getImageName();
    menuFont = createFont(menuTexture, menuFontImageName);
  }

  /**
   * Create font from texture and font image name
   *
   * @param texture   - texture holding font
   * @param imageName - name of font in texture
   * @return font
   */
  private Font createFont(Texture texture, String imageName) {
    TextureDetail textureDetail = texture.getTextureDetail(imageName);
    return new Font(
        texture,
        textureDetail.getXPos(),
        textureDetail.getYPos(),
        FONT_GLYPHS_PER_ROW,
        FONT_GLYPHS_WIDTH,
        FONT_GLYPHS_HEIGHT,
        GameConstants.FONT_CHARACTER_MAP);
  }

  /**
   * Initialise sprites for new texture map
   *
   * @param textureRegions   - texture regions to initialise
   * @param spriteDimensions - sprite dimensions to initialise
   */
  private void initaliseSprites(
      EnumMap<SpriteDetails, TextureRegion> textureRegions,
      EnumMap<SpriteDetails, SpriteDimensions> spriteDimensions) {
    SpriteDetails.initialise(textureRegions, spriteDimensions);
  }
}
