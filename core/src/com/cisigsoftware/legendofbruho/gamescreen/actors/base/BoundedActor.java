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
    bounds = new RectangularBounds(x, y, width, height, width / 2, height / 2);
    
    setPosition(x, y);
    setSize(width, height);
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
  public boolean collidesWith(BoundedActor other) {
    return collidesWith(other.getBounds().getBoundingRectangle());
  }

  /**
   * Returns true if the bounds of this actor collides at the side parameter GameActor bounds
   * 
   * @param other GameActor
   */
  public boolean collidesWith(Rectangle otherBounds) {
    return Intersector.overlaps(bounds.getBoundingRectangle(), otherBounds);
  }

  /**
   * Applies changes in the dimension, position, rotation and scaling of the actor to its bounds
   */
  
  @Override
  public void setPosition(float x, float y) {
    super.setPosition(x, y);
    bounds.setPosition(x, y);
  }
  
  @Override
  public void setSize(float width, float height) {
    super.setSize(width, height);
    bounds.setWidth(width);
    bounds.setHeight(height);
  }
  
  @Override
  public void moveBy(float dx, float dy) {
    super.moveBy(dx, dy);
    bounds.translate(dx, dy);
  }
  
  @Override
  public void setScale(float scaleXY) {
    super.setScale(scaleXY);
    bounds.scale(scaleXY);
  }
  
  @Override
  public void scaleBy(float scaleX, float scaleY) {
    super.scaleBy(scaleX, scaleY);
    bounds.setScale(scaleX, scaleY);
  }

  @Override
  public void rotateBy(float amountInDegrees) {
    super.rotateBy(amountInDegrees);
    bounds.rotate(amountInDegrees);
  }

  @Override
  public void setOrigin(float originX, float originY) {
    super.setOrigin(originX, originY);
    bounds.setOrigin(originX, originY);
  }
  
  @Override
  public void setRotation(float amountInDegrees) {
    super.setRotation(amountInDegrees);
    bounds.setRotation(amountInDegrees);
  }
}
