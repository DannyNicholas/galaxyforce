package com.danosoftware.galaxyforce.sprites.game.aliens.implementations.helpers;

import android.util.Log;
import com.danosoftware.galaxyforce.sprites.common.ISprite;
import com.danosoftware.galaxyforce.waves.config.aliens.types.BoundaryLanePolicy;
import java.util.Random;

/**
 * Checks if alien is within supplied boundaries.
 */
public class BoundariesChecker {

  private static final Random rand = new Random();

  private final ISprite sprite;
  private final int minX;
  private final int maxX;
  private final int minY;
  private final int maxY;
  private final int centreX;
  private final int centreY;

  public BoundariesChecker(
      ISprite sprite,
      int boundaryMinX,
      int boundaryMaxX,
      int boundaryMinY,
      int boundaryMaxY,
      BoundaryLanePolicy lanePolicy,
      int laneCount) {

    // if we only have 1 lane then we use the whole boundary area
    if (laneCount == 1) {
      lanePolicy = BoundaryLanePolicy.NONE;
    }

    // lanes sub-divide the boundary area into either horizontal or vertical lanes.
    // if lanes are wanted then pick a random lane and calculate the actual lane boundary.
    switch (lanePolicy) {
      case HORIZONTAL:
        // choose a random horizontal lane
        int horizontalLane = rand.nextInt(laneCount);
        int horizontalLaneWidth = (boundaryMaxY - boundaryMinY) / laneCount;
        this.minY = boundaryMinY + (horizontalLane * horizontalLaneWidth);
        this.maxY = minY + horizontalLaneWidth;
        this.minX = boundaryMinX;
        this.maxX = boundaryMaxX;
        break;
      case VERTICAL:
        // choose a random vertical lane
        int verticalLane = rand.nextInt(laneCount);
        Log.i("TAG", Integer.toString(verticalLane));
        int verticalLaneWidth = (boundaryMaxX - boundaryMinX) / laneCount;
        this.minX = boundaryMinX + (verticalLane * verticalLaneWidth);
        this.maxX = minX + verticalLaneWidth;
        this.minY = boundaryMinY;
        this.maxY = boundaryMaxY;
        break;
      case NONE:
      default:
        // use entire area
        this.minX = boundaryMinX;
        this.maxX = boundaryMaxX;
        this.minY = boundaryMinY;
        this.maxY = boundaryMaxY;
        break;
    }

    this.sprite = sprite;
    this.centreX = minX + ((maxX - minX) / 2);
    this.centreY = minY + ((maxY - minY) / 2);
  }

  /**
   * Tests if the sprite is outside supplied boundaries by testing it's position/dimensions against
   * all boundary edges.
   */
  public boolean isOutsideBoundaries() {
    return (sprite.y() >= maxY + sprite.halfHeight()
        || sprite.y() <= minY - sprite.halfHeight()
        || sprite.x() >= maxX + sprite.halfWidth()
        || sprite.x() <= minX - sprite.halfWidth());
  }

  /**
   * X centre position of boundary
   */
  public int centreX() {
    return centreX;
  }

  /**
   * Y centre position of boundary
   */
  public int centreY() {
    return centreY;
  }
}
