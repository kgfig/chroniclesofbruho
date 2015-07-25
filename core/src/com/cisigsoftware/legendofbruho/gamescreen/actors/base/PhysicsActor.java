/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen.actors.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.cisigsoftware.legendofbruho.gamescreen.Level;
import com.cisigsoftware.legendofbruho.gamescreen.actors.Block;

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
  protected Array<Block> collidable;

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
   * Override this method to implement the collision behavior of this actor with blocks
   */
  protected abstract void checkCollisionWithBlocks(float delta);

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

    collidable = new Array<Block>();
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
   * @return the velocity of the actor
   */
  public Vector2 getVelocity() {
    return velocity;
  }
}
