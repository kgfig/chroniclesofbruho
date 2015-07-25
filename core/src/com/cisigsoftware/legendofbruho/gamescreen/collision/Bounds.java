/**
 * Copyright 2015 CISIG Software Labs Inc.
 * All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen.collision;

import com.badlogic.gdx.math.Polygon;

/**
 * @author kg
 *
 */
public abstract class Bounds extends Polygon {
  
  public Bounds(float[] vertices){
    super(vertices);
  }
  
  public abstract float getWidth();
  public abstract float getHeight();
}
