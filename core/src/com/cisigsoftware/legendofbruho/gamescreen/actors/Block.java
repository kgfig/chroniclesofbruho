/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen.actors;

import com.badlogic.gdx.math.Vector2;
import com.cisigsoftware.legendofbruho.gamescreen.actors.base.BoundedActor;

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
