/**
 * Copyright 2015 CISIG Software Labs Inc.
 * All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors;

import com.badlogic.gdx.math.Vector2;

/**
 * @author kg
 *
 */
public class Block extends GameActor {

  private static final float SIZE = 1f;
  
  public Block(Vector2 position) {
    super(position.x, position.y, SIZE, SIZE);
    setPosition(position.x, position.y);
    setSize(SIZE, SIZE);
  }
}
