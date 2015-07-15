/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors;

import com.badlogic.gdx.math.Vector2;

/**
 * @author kg
 *
 */
public class Block extends BoundedActor {

  private static final float SIZE = 1f;
  private boolean goal;

  public Block(Vector2 position) {
    this(position.x, position.y);
  }

  public Block(float x, float y) {
    super(x, y, SIZE, SIZE);
    setPosition(x, y);
    setSize(SIZE, SIZE);
    
    goal = false;
  }

  /**
   * @return the goal
   */
  public boolean isGoal() {
    return goal;
  }

  /**
   * @param goal the goal to set
   */
  public void setGoal(boolean goal) {
    this.goal = goal;
  }
}
