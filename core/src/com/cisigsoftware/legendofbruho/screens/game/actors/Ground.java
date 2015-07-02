/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * @author kg
 *
 */
public class Ground extends GameActor {
  
  public static final float X = 0;
  public static final float Y = 5;
  public static final float WIDTH = 80f;
  public static final float HEIGHT = 4f;
  public static final float DENSITY = 0f;
  
  public Ground(Body body) {
    super(body);
  }
}
