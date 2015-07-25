/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen.actors.base;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.cisigsoftware.legendofbruho.gamescreen.collision.RectangularBounds;

/**
 * @author kg
 *
 */
public class BoundedActor extends Actor {

  protected RectangularBounds bounds;

  public BoundedActor(float x, float y, float width, float height) {
    setPosition(x, y);
    setSize(width, height);

    bounds = new RectangularBounds(x, y, width, height, width / 2, height / 2);
  }

  /**
   * @return bounds the collision bounds of the actor
   */
  public Polygon getBounds() {
    return bounds;
  }

  /**
   * Returns true if the bounds of this actor collides at the side parameter GameActor bounds
   * 
   * @param other GameActor
   */
  public boolean collidesBeside(BoundedActor other) {
    return Intersector.overlapConvexPolygons(bounds, other.getBounds());
  }

  /**
   * Returns true if the bounds of this actor collides at the side parameter GameActor bounds
   * 
   * @param other GameActor
   */
  public boolean collidesBeside(Rectangle otherBounds) {
    return Intersector.overlaps(bounds.getBoundingRectangle(), otherBounds);
  }

}
