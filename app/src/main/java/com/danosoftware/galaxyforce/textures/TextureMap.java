package com.danosoftware.galaxyforce.textures;

/**
 * Enum representing each texture map.
 * <p>
 * Each texture map describes the PNG image file, XML property file, associated sprite identifiers
 * and font identifiers.
 */
public enum TextureMap {

  GAME(
      "gameTextures.png",
      "gameTextures.xml"),
  MENU(
      "menuTextures.png",
      "menuTextures.xml");

  private final String textureImage;
  private final String textureXml;

  TextureMap(
      String textureImage,
      String textureXml) {
    this.textureImage = textureImage;
    this.textureXml = textureXml;
  }

  public String getTextureImage() {
    return textureImage;
  }

  public String getTextureXml() {
    return textureXml;
  }
}
