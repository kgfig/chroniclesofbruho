/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisiglabs.bruhoplatformer.gamescreen.world.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.cisiglabs.bruhoplatformer.gamescreen.Level;

/**
 * @author kg
 *
 */
public abstract class PhysicsActor extends BoundedActor {

  private static final String TAG = PhysicsActor.class.getSimpleName();

  // May have to turn these to instance variables
  private static final float DAMP = 0.9f; // smooth out movement so he won't stop abruptly, ignore
                                          // for jump

  /*
   * Physics properties
   */
  protected float gravity;
  protected float maxVel;

  /*
   * Collision variables
   */
  protected Level level;
  protected Pool<Rectangle> rectPool;
  protected Array<BoundedActor> collidable;

  /*
   * Movement control variables
   */
  protected Vector2 acceleration, velocity;
  protected boolean grounded;
  protected boolean facingLeft;

  public PhysicsActor(float x, float y, float width, float height) {
    super(x, y, width, height);
    Gdx.app.log(TAG,
        String.format("Created PhysicsActor at position (%f,%f)", bounds.getX(), bounds.getY()));
    initPhysicsProperties();
  }


  /**
   * Override these methods to implement the collision behavior of this actor with blocks
   */
  protected abstract void collideAlongX(float delta, boolean left);

  protected abstract void collideAlongY(float delta, boolean top);

  @Override
  public void act(float delta) {
    super.act(delta);

    acceleration.scl(delta);
    velocity.add(acceleration);

    checkCollisionWithBlocks(delta);

    velocity.x = velocity.x * DAMP;

    if (velocity.x > maxVel) // cap x-velocity by MAX_VEL
      velocity.x = maxVel;

    if (velocity.x < -maxVel)
      velocity.x = -maxVel;
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);

    if (getX() < 0) {
      resetX();
    }

    if (getY() < 0) {
      resetY();
    }

    float rightX = level.getWidth() - bounds.getBoundingRectangle().getWidth();

    if (getX() > rightX) {
      setX(rightX);
    }
  }

  /**
   * Initializes physics properties, flags and collision variables
   */
  private void initPhysicsProperties() {
    gravity = 0;
    maxVel = 0;

    collidable = new Array<BoundedActor>();
    rectPool = new Pool<Rectangle>() {
      @Override
      protected Rectangle newObject() {
        return new Rectangle(bounds.getBoundingRectangle().x, bounds.getBoundingRectangle().y,
            bounds.getBoundingRectangle().width, bounds.getBoundingRectangle().height);
      }
    };

    acceleration = new Vector2();
    velocity = new Vector2();
    grounded = false;
    facingLeft = true;
  }

  protected void checkCollisionWithBlocks(float delta) {
    int startX, endX, startY, endY;
    boolean left, top;
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
    for (BoundedActor block : collidable) {
      if (block != null && block.collidesWith(box)) {
        collideAlongX(delta, left);
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
    if (velocity.y < 0) {
      startY = (int) Math.floor(rectBounds.y + velocity.y);
      top = false;
    } else { // otherwise check the block above
      startY = (int) Math.floor(rectBounds.y + rectBounds.height + velocity.y);
      top = true;
    }

    endY = startY;

    // Get the candidate blocks for collision
    collidable = level.getCollidableBlocks(collidable, startX, startY, endX, endY);

    // Create a copy of hero's bounds and set it to his future position based on his velocity
    box.y = box.y + velocity.y;

    // If he collides, set his y-velocity to 0 and set grounded to true
    for (BoundedActor block : collidable) {
      if (block != null && block.collidesWith(box)) {
        collideAlongY(delta, top);
        break;
      }
    }

    // Then reset his collision box to his current position
    box.y = getY();

    // Update his current position and the bounds
    moveBy(velocity.x, velocity.y);
    bounds.setScale(getScaleX(), getScaleY());

    // un-scale the velocity
    velocity.scl(1 / delta);
  }

  /**
   * Keeps him inside the world
   */
  protected void resetX() {
    setX(0);
  }

  /**
   * Keeps him inside the world
   */
  protected void resetY() {
    setY(0);
  }

  /**
   * Sets the current level of the actor
   */
  public void setLevel(Level level) {
    this.level = level;
  }
  
  /**
   * @return the level
   */
  public Level getLevel() {
    return level;
  }


  /**
   * @param gravity the gravity to set
   */
  public void setGravity(float gravity) {
    this.gravity = gravity;
  }

  /**
   * @param maxVel the maxVel to set
   */
  public void setMaxVel(float maxVel) {
    this.maxVel = maxVel;
  }

  /**
   * @return the grounded
   */
  public boolean isGrounded() {
    return grounded;
  }

  /**
   * @param grounded the grounded to set
   */
  public void setGrounded(boolean grounded) {
    this.grounded = grounded;
  }

  /**
   * @return the facingLeft
   */
  public boolean isFacingLeft() {
    return facingLeft;
  }

  /**
   * @param facingLeft the facingLeft to set
   */
  public void setFacingLeft(boolean facingLeft) {
    this.facingLeft = facingLeft;
  }

  /**
   * Returns the velocity of the actor
   * 
   * @return the velocity of the actor
   */
  public Vector2 getVelocity() {
    return velocity;
  }
}
