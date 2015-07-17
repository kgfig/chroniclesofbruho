/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author kg
 *
 */
public class BoundedActor extends Actor {

  protected Rectangle bounds;

  public BoundedActor() {
    this(new Rectangle());
  }

  public BoundedActor(float x, float y, float width, float height) {
    this(new Rectangle(x, y, width, height));
  }

  public BoundedActor(Rectangle bounds) {
    this.bounds = bounds;
  }

  /**
   * @return the bounds
   */
  public Rectangle getBounds() {
    return bounds;
  }

  /**
   * @param bounds the bounds to set
   */
  public void setBounds(Rectangle bounds) {
    this.bounds = bounds;
  }

  /**
   * Returns true if the bounds of this actor collides at the side parameter GameActor bounds
   * 
   * @param other GameActor
   */
  public boolean collidesBeside(BoundedActor other) {
    return this.bounds.overlaps(other.getBounds());
  }

  /**
   * Returns true if the bounds of this actor collides at the side parameter GameActor bounds
   * 
   * @param other GameActor
   */
  public boolean collidesBeside(Rectangle otherBounds) {
    return this.bounds.overlaps(otherBounds);
  }

  /**
   * Returns true if the bounds of this actor collides at the side parameter GameActor bounds
   * 
   * @param other GameActor
   */
  public boolean contains(BoundedActor other) {
    return bounds.contains(other.getBounds());
  }

  public boolean collidesByLeftHalf(BoundedActor other) {
    Rectangle otherBounds = other.getBounds();
    return bounds.x < otherBounds.x + otherBounds.width
        && bounds.x + bounds.width > otherBounds.x + (otherBounds.width / 2)
        && bounds.y < otherBounds.y + otherBounds.height
        && bounds.y + bounds.height > otherBounds.y + (otherBounds.height / 2);
  }


}
