/**
 * Copyright 2015 CISIG Software Labs Inc.
 * All Rights Reserved.
 */
package com.cisiglabs.bruhoplatformer.gamescreen.world.base;

import com.badlogic.gdx.math.Polygon;

/**
 * @author kg
 *
 */
public abstract class Bounds extends Polygon {
  
  public Bounds(float[] vertices){
    super(vertices);
  }
  
  public abstract void setWidth(float width);
  public abstract void setHeight(float height);
  
  public abstract float getWidth();
  public abstract float getHeight();
}
