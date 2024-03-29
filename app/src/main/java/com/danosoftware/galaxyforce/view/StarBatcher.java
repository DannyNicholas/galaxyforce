package com.danosoftware.galaxyforce.view;

import android.opengl.GLES20;
import com.danosoftware.galaxyforce.constants.GameConstants;
import com.danosoftware.galaxyforce.models.screens.background.RgbColour;
import com.danosoftware.galaxyforce.sprites.game.starfield.Star;
import com.danosoftware.galaxyforce.sprites.game.starfield.StarColour;

public class StarBatcher {

  private static final int POSITION_COORDS_PER_VERTEX = 2;  // x,y
  private static final int COLOUR_COORDS_PER_VERTEX = 4;   // r,g,b,a
  private static final int COORDS_PER_VERTEX =
      POSITION_COORDS_PER_VERTEX + COLOUR_COORDS_PER_VERTEX;
  private static final int VERTICES_PER_STAR = 1;

  // This is a calculation optimisation.
  // The size of each star should be:
  // (device_screen_height / game_height) * 2.5f
  // since game_height and 2.5 are constants we can calculate their ratio up-front.
  // we then simply multiply this by device_screen_height to get star size at runtime.
  private static final float STAR_SCALAR = 2.5f / GameConstants.GAME_HEIGHT;

  private final float[] verticesBuffer;
  private final Vertices vertices;
  private final int numStars;
  private final Star[] starField;
  private final GLGraphics graphics;

  public StarBatcher(GLGraphics graphics, Star[] starField) {
    this.graphics = graphics;
    this.starField = starField;
    this.numStars = starField.length;

    // create an vertices buffer that holds star's position and colour per vertex
    this.verticesBuffer = new float[numStars * (POSITION_COORDS_PER_VERTEX
        + COLOUR_COORDS_PER_VERTEX)];

    // create vertices object to hold all vertices
    this.vertices = new Vertices(
        numStars * VERTICES_PER_STAR,
        0,
        true,
        false);

    setUpVertices();
  }

  /**
   * initialises the vertices buffer with the initial star-field.
   */
  private void setUpVertices() {
    int index = 0;
    for (Star star : starField) {
      // position vertex - x,y position
      verticesBuffer[index++] = star.x;
      verticesBuffer[index++] = star.y;

      // colour vertex - rgba colour
      final StarColour starColour = star.colour;
      final RgbColour colour = starColour.getRgb();
      verticesBuffer[index++] = colour.getRed(); // red
      verticesBuffer[index++] = colour.getGreen(); // green
      verticesBuffer[index++] = colour.getBlue(); // blue
      verticesBuffer[index++] = 1.0f; // alpha
    }
  }

  public void drawStars() {
    // since each star's x-position or colour will never change,
    // we will only modify the y-position of our stars.

    // the first star's y-position within the buffer is at index = 1
    int bufferIndex = 1;

    // update y-position and colour of each star and increment index to next vertex.
    for (Star star : starField) {
      verticesBuffer[bufferIndex] = star.y;

      // increment index to next star's y-position
      bufferIndex += 6;
    }

    // Calculate the stars point size so it scales correctly for screen size.
    // We re-calculate each frame since surface size may not be set on construction
    // and screen could be re-sized mid-game (e.g. split-screen)
    final float pointSize = STAR_SCALAR * graphics.getHeight();
    GLES20.glUniform1f(GLShaderHelper.sPointSizeHandle, pointSize);

    // draw the points in the vertices buffer
    vertices.setVertices(verticesBuffer, 0, numStars * COORDS_PER_VERTEX);
    vertices.bind();
    vertices.draw(GLES20.GL_POINTS, 0, numStars * VERTICES_PER_STAR);
    vertices.unbind();
  }
}
