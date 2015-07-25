/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen.actors.base;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author kg
 *
 */
public class BoundedActor extends Actor {

  protected Polygon bounds;

  public BoundedActor(float x, float y, float width, float height) {
    setPosition(x, y);
    setSize(width, height);
    
    float[] vertices = {0,0,0, height, width,height,width, 0};
    bounds = new Polygon(vertices);
    bounds.setPosition(x, y);
    bounds.setOrigin(width/2, height/2);
  }

  /**
   * Returns rectangular bounds
   * 
   * @param x
   * @param y
   * @param width
   * @param height
   * @return rectangular bounds
   */
  public static Polygon newRectangularBounds(float x, float y, float width, float height) {
    Polygon polygon =
        new Polygon(new float[] {x, y, x, y + height, x + width, y + height, x + width, y});
    return polygon;
  }

  /**
   * Returns circular bounds
   * 
   * @param x
   * @param y
   * @param a
   * @param b
   * @return circular bounds
   */
  public static Polygon newCirclularBounds(float x, float y, float r) {
    return new Polygon(new float[] {x - r, y, x, y + r, x + r, y, x, y - r});
  }

  /**
   * Returns elliptical bounds
   * 
   * @param x
   * @param y
   * @param a
   * @param b
   * @return elliptical bounds
   */
  public static Polygon newEllipticalBounds(float x, float y, float a, float b) {
    return new Polygon(new float[] {x - a, y, x, y + b, x + a, y, x, y - b});
  }

  /**
   * @return bounds the collision bounds of the actor
   */
  public Polygon getBounds() {
    return bounds;
  }

  /**
   * @param bounds the bounds to set
   */
  public void setBounds(Polygon bounds) {
    this.bounds = bounds;
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
