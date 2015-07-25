/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen.actors.enemytypes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.cisigsoftware.legendofbruho.gamescreen.Level;
import com.cisigsoftware.legendofbruho.gamescreen.actors.Block;
import com.cisigsoftware.legendofbruho.gamescreen.actors.Enemy;

/**
 * @author kg
 *
 */
public class BarricadeEnemy extends Enemy {

  private static final String TAG = BarricadeEnemy.class.getSimpleName();

  private static final float GRAVITY = -20f;
  private static final float SIZE = 1f;

  private static final float MAX_HP = 5;
  private static final float DAMAGE = 5;

  public BarricadeEnemy(Level level, float x, float y) {
    super(Type.BARRICADE, x, y, SIZE, SIZE);

    setNearThreshold(0);
    setGrounded(true);
    setState(State.IDLE);
    setGravity(GRAVITY);
    setHp(MAX_HP);
    setMaxHp(MAX_HP);
    setDamage(DAMAGE);
    setLevel(level);

    Gdx.app.log(TAG,
        String.format("Created BarricadeEnemy with HP=%f\tDamage=%f.", getHp(), getDamage()));
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    acceleration.y = gravity;

    if (!level.isComplete()) {
      // Unset attacked flag
      if (!target.isDying() && attacked && !this.collidesBeside(target)) {
        setAttacked(false);
        Gdx.app.log(TAG, "Collided with target. Unset attacked " + attacked());
      }

      if (!target.isDying() && this.collidesBeside(target) && !attacked) {
        setAttacked(true);
        target.hurt(damage);
        Gdx.app.log(TAG, "Collided with target. Attacked with " + getDamage() + " damage.");
      }
    }
  }

  @Override
  protected void checkCollisionWithBlocks(float delta) {
    int startX, endX, startY, endY;

    // scale velocity to the frame
    velocity.scl(delta);

    /*
     * Check for collision along the x-axis
     */
    startY = (int) bounds.y;
    endY = (int) (bounds.y + bounds.height);

    // If he is moving to the left, check if he collides with the block to the left
    if (velocity.x < 0)
      startX = (int) Math.floor(bounds.x + velocity.x);
    else // check if he collides with the block to the right
      startX = (int) Math.floor(bounds.x + bounds.width + velocity.x);

    endX = startX;

    // Get the candidate blocks for collision
    collidable = level.getCollidableBlocks(collidable, startX, startY, endX, endY);

    // Create a copy of hero's bounds and set it to his future position based on his velocity
    Rectangle box = rectPool.obtain();
    box.x = box.x + velocity.x;

    // If he collides, stop his x-velocity to 0
    for (Block block : collidable) {
      if (block != null && block.collidesBeside(box)) {
        velocity.x = 0;
        break;
      }
    }

    // Then reset his collision box to his current position
    box.x = getX();

    /**
     * Check for collision in the y-axis
     */
    startX = (int) bounds.x;
    endX = (int) (bounds.x + bounds.width);

    // If he is standing or falling, check if he collides with the block below
    if (velocity.y < 0)
      startY = (int) Math.floor(bounds.y + velocity.y);
    else // otherwise check the block above
      startY = (int) Math.floor(bounds.y + bounds.height + velocity.y);

    endY = startY;

    // Get the candidate blocks for collision
    collidable = level.getCollidableBlocks(collidable, startX, startY, endX, endY);

    // Create a copy of hero's bounds and set it to his future position based on his velocity
    box.y = box.y + velocity.y;

    // If he collides, set his y-velocity to 0 and set grounded to true
    for (Block block : collidable) {
      if (block != null && block.collidesBeside(box)) {
        if (velocity.y < 0)
          setGrounded(true);
        velocity.y = 0;
        break;
      }
    }

    // Then reset his collision box to his current position
    box.y = getY();

    // Update his current position
    moveBy(velocity.x, velocity.y);
    bounds.x = getX();
    bounds.y = getY();

    // un-scale the velocity
    velocity.scl(1 / delta);
  }
}
