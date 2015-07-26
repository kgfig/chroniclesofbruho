/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen.actors;

import com.badlogic.gdx.math.Rectangle;
import com.cisigsoftware.legendofbruho.gamescreen.actors.base.PhysicsActor;

/**
 * @author kg
 *
 */
public class Bullet extends PhysicsActor {

  private static final float WIDTH = 0.35f;
  private static final float HEIGHT = 0.25f;

  private static float VX = 12f;

  public Bullet(float x, float y) {
    super(x, y, WIDTH, HEIGHT);

    setGrounded(false);
  }
  
  @Override
  public void act(float delta) {
    super.act(delta);
    
    velocity.x = VX;
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
    if (velocity.x < 0) {
      startX = (int) Math.floor(rectBounds.x + velocity.x);
    } else { // check if he collides with the block to the right
      startX = (int) Math.floor(rectBounds.x + rectBounds.width + velocity.x);
    }

    endX = startX;

    // Get the candidate blocks for collision
    collidable = level.getCollidableBlocks(collidable, startX, startY, endX, endY);

    // Create a copy of hero's bounds and set it to his future position based on his velocity
    Rectangle box = rectPool.obtain();
    box.x = box.x + velocity.x;

    // If he collides, stop his x-velocity to 0
    for (Block block : collidable) {
      if (block != null && block.collidesWith(box)) {
        velocity.x = 0;  
        remove();
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
      if (block != null && block.collidesWith(box)) {
        if (velocity.y < 0)
          setGrounded(true);
        velocity.y = 0;
        remove();
        break;
      }
    }

    // Then reset his collision box to his current position
    box.y = getY();

    // Update his current position
    moveBy(velocity.x, velocity.y);

    // un-scale the velocity
    velocity.scl(1 / delta);
  }

  
}
