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
public class CrawlingEnemy extends Enemy {

  private static final String TAG = CrawlingEnemy.class.getSimpleName();

  private static final float GRAVITY = -20f;
  private static final float SIZE = 0.5f;

  private static final float MAX_HP = 5;
  private static final float DAMAGE = 5;
  private static final float VX = 1f;

  private boolean crawling;

  public CrawlingEnemy(Level level, float x, float y) {
    super(Type.CRAWLING, x, y, SIZE, SIZE);

    setNearThreshold(0);
    setGrounded(false);
    setGravity(GRAVITY);
    setHp(MAX_HP);
    setDamage(DAMAGE);
    setLevel(level);
    setState(State.IDLE);
    setAttacked(false);
    setCrawling(false);
    setFacingLeft(true);
    setOriginX(SIZE / 2);

    Gdx.app.log(TAG,
        String.format("Created StaticEnemy with HP=%f\tDamage=%f", getHp(), getDamage()));
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    // Enemy is just lying around. Apply gravity.
    acceleration.y = gravity;

    if (!level.isComplete()) {

      // Start crawling as soon as it gets into the player's view
      if (isStateMoving()) {
        if (!crawling) {
          Gdx.app.log(TAG, "Crawl");
          setCrawling(true);

          if (target.getX() + target.getWidth() < getX()) // Set initial direction of crawl based on target position
            setFacingLeft(true);
          else
            setFacingLeft(false);
        }

        if (facingLeft) // Crawl to the left
          velocity.x = -VX;
        else // crawl to the right
          velocity.x = VX;
      }

      // Unset attacked flag
      if (!target.isDying() && attacked && !this.collidesBeside(target)) {
        setAttacked(false);
        Gdx.app.log(TAG, "Collided with target. Unset attacked " + attacked());
      }

      // Deal damage to the target on collision
      if (!target.isDying() && this.collidesBeside(target) && !attacked) {
        setAttacked(true);
        target.hurt(damage);
        Gdx.app.log(TAG, "Collided with target. Attacked with " + getDamage() + " damage. attacked?" + attacked);
      }
      
    }

  }

  @Override
  protected void checkCollisionWithBlocks(float delta) {
    int startX, endX, startY, endY;
    boolean left;
    Rectangle rectBounds = bounds.getBoundingRectangle();

    // scale velocity to the frame
    velocity.scl(delta);

    /*
     * Check for collision along the x-axis
     */
    startY = (int) rectBounds.y;
    endY = (int) (rectBounds.y + rectBounds.height);

    // If he is moving to the left, check if he collides with the block to the left
    if (velocity.x < 0) {
      startX = (int) Math.floor(rectBounds.x + velocity.x);
      left = true;
    } else { // check if he collides with the block to the right
      startX = (int) Math.floor(rectBounds.x + rectBounds.width + velocity.x);
      left = false;
    }

    endX = startX;

    // Get the candidate blocks for collision
    collidable = level.getCollidableBlocks(collidable, startX, startY, endX, endY);

    // Create a copy of hero's bounds and set it to his future position based on his velocity
    Rectangle box = rectPool.obtain();
    box.x = box.x + velocity.x;

    // If he collides, stop his x-velocity to 0
    for (Block block : collidable) {
      if (block != null && block.collidesBeside(box)) {
        setFacingLeft(!left);  
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

    // un-scale the velocity
    velocity.scl(1 / delta);
  }

  /**
   * @param crawling true if the enemy is crawling
   */
  private void setCrawling(boolean crawling) {
    this.crawling = crawling;
  }

}
