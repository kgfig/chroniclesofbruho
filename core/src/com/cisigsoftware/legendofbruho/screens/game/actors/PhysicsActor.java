/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.cisigsoftware.legendofbruho.screens.game.Level;

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

  public PhysicsActor() {
    super();
  }

  public PhysicsActor(Level level, float x, float y, float width, float height) {
    super(x, y, width, height);
    setPosition(x, y);
    setSize(width, height);

    init(level);
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

    float rightX = level.getWidth() - bounds.getWidth();

    if (getX() > rightX) {
      setX(rightX);
    }
  }

  /**
   * Initializes physics properties, flags and collision variables
   */
  private void init(Level level) {
    this.level = level;
    gravity = 0;
    maxVel = 0;
    
    collidable = new Array<Block>();
    rectPool = new Pool<Rectangle>() {
      @Override
      protected Rectangle newObject() {
        return new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
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

}
