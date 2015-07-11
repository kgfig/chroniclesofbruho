/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.cisigsoftware.legendofbruho.screens.game.Level;

/**
 * @author kg
 *
 */
public class BouncingEnemy extends Enemy {

  private static final String TAG = BouncingEnemy.class.getSimpleName();

  private static final float GRAVITY = -20f; // gravity acceleration
  private static final float SIZE = 0.5f;
  private static final float NEAR_DISTANCE = 5f;
  private static final float VY = 9f;
  private static final float VX = 2f;

  public BouncingEnemy(Level level, float x, float y) {
    super(level, x, y, SIZE, SIZE);

    setNearThreshold(NEAR_DISTANCE);
    setGrounded(true);
    setState(State.IDLE);
    setGravity(GRAVITY);
  }

  @Override
  public void act(float delta) {
    Gdx.app.log(TAG, "Step " + delta);
    super.act(delta);

    acceleration.y = gravity;

    /**
     * Attack if the target is near
     * Otherwise, do standby movement
     */
    if (isNearTarget() && !isStateAttacking()) {

      // If the enemy is on the ground, toggle the flag and bounce up to attack.
      // While suspended in the air, move to the left or to the right depending on the target's
      // direction

      if (isGrounded()) {
        velocity.y = VY;
        setState(State.ATTACKING);
        setGrounded(false);
      } else {
        if (target.getX() > getX()) {
          velocity.x = VX;
        } else {
          velocity.x = -VX;
        }
      }
    } else if (isStateMoving() && isGrounded()) {
      
      // Bounce in place
      
      velocity.y = VY;
      setGrounded(false);
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
        Gdx.app.log(TAG, "Stop velocity grounded=" + isGrounded());
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
