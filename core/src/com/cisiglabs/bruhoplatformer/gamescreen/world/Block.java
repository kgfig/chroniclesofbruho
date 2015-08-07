/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisiglabs.bruhoplatformer.gamescreen.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.cisiglabs.bruhoplatformer.gamescreen.world.base.BoundedActor;

/**
 * @author kg
 *
 */
public class Block extends BoundedActor {

  private static final String TAG = Block.class.getSimpleName();

  private static final float SIZE = 1f;
  private boolean goal;

  public Block(Vector2 position) {
    this(position.x, position.y);
  }

  public Block(float x, float y) {
    super(x, y, SIZE, SIZE);
    goal = false;

    Gdx.app.log(TAG,
        String.format("Created block at position (%f,%f) bounds at (%f,%f) origin at (%f,%f)", x, y,
            bounds.getX(), bounds.getY(), bounds.getOriginX(), bounds.getOriginY()));
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
