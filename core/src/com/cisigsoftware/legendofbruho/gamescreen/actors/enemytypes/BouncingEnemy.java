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
public class BouncingEnemy extends Enemy {

  private static final String TAG = BouncingEnemy.class.getSimpleName();

  private static final float GRAVITY = -20f;
  private static final float SIZE = 0.5f;

  private static final float MAX_HP = 5;
  private static final float DAMAGE = 5;

  private static final float NEAR_DISTANCE = 3f;
  private static final float VY = 9f;
  private static final float VX = 2f;

  public BouncingEnemy(Level level, float x, float y) {
    super(Type.BOUNCING, x,y,SIZE,SIZE);

    setNearThreshold(NEAR_DISTANCE);
    setGrounded(true);
    setState(State.IDLE);
    setGravity(GRAVITY);
    setHp(MAX_HP);
    setMaxHp(MAX_HP);
    setDamage(DAMAGE);
    setLevel(level);

    Gdx.app.log(TAG, String.format("Created BouncingEnemy with HP=%f\tDamage=%f.", getHp(), getDamage()));
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    acceleration.y = gravity;

    if (!level.isComplete()) {
      /**
       * Attack if the target is near Otherwise, do standby movement
       */
      if (isNearTarget() && !isStateAttacking()) {
        // If the enemy is on the ground, toggle the flag and bounce up to attack.
        // While suspended in the air, move to the left or to the right depending on the target's
        // direction
        if (isGrounded()) {
          velocity.y = VY;
          setState(State.ATTACKING);
          setGrounded(false);
          Gdx.app.log(TAG, "Bounce up attack");
        } else {
          if (target.getX() > getX() + getWidth()) {
            velocity.x = VX;
            Gdx.app.log(TAG, "Bounce right attack");
          } else if (target.getX() + target.getWidth() < getX()) {
            velocity.x = -VX;
            Gdx.app.log(TAG, "Bounce left attack");
          }
        }
      } else if (isStateMoving() && isGrounded()) {
        // Bounce in place
        velocity.y = VY;
        setGrounded(false);
        Gdx.app.log(TAG, "Bounce in place");
      }

      /**
       * Deal damage on the target upon collision
       */
      if (!isGrounded()) {
        
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
  }

  @Override
  protected void checkCollisionWithBlocks(float delta) {
    int startX, endX, startY, endY;
    Rectangle rectBounds = bounds.getBoundingRectangle();

    // scale velocity to the frame
    velocity.scl(delta);

    /*
     * Check for collision along the x-axis
     */
    startY = (int) rectBounds.y;
    endY = (int) (rectBounds.y + rectBounds.height);

    // If he is moving to the left, check if he collides with the block to the left
    if (velocity.x < 0)
      startX = (int) Math.floor(rectBounds.x + velocity.x);
    else // check if he collides with the block to the right
      startX = (int) Math.floor(rectBounds.x + rectBounds.width + velocity.x);

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
    startX = (int) rectBounds.x;
    endX = (int) (rectBounds.x + rectBounds.width);

    // If he is standing or falling, check if he collides with the block below
    if (velocity.y < 0)
      startY = (int) Math.floor(rectBounds.y + velocity.y);
    else // otherwise check the block above
      startY = (int) Math.floor(rectBounds.y + rectBounds.height + velocity.y);

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
    bounds.translate(velocity.x, velocity.y);
//    bounds.x = getX();
//    bounds.y = getY();

    // un-scale the velocity
    velocity.scl(1 / delta);
  }
}
