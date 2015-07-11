/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors;

import com.badlogic.gdx.Gdx;
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
public class GameActor extends BoundedActor {
  
  private static final String TAG = GameActor.class.getSimpleName();

  // May have to turn these to instance variables
  private static final float GRAVITY = -20f; // gravity acceleration
  private static final float DAMP = 0.9f; // smooth out movement so he won't stop abruptly, ignore for jump
  private static final float MAX_VEL = 4f; // maximum velocity for movement along horizontal axis while walking or running

  protected Level level;
  private Pool<Rectangle> rectPool;
  private Array<Block> collidable;

  protected Vector2 acceleration, velocity;
  protected boolean grounded;
  protected boolean facingLeft;

  public GameActor() {
    super();
  }

  public GameActor(Level level, float x, float y, float width, float height) {
    super(x, y, width, height);
    setPosition(x, y);
    setSize(width, height);

    this.level = level;

    initDefaultFlags();
    initPhysicsSettings();
    initObjects();
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    acceleration.y = GRAVITY;
    acceleration.scl(delta);
    velocity.add(acceleration);

    checkCollisionWithBlocks(delta);

    velocity.x = velocity.x * DAMP;

    if (velocity.x > MAX_VEL) // cap x-velocity by MAX_VEL
      velocity.x = MAX_VEL;

    if (velocity.x < -MAX_VEL)
      velocity.x = -MAX_VEL;
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
   * Initializes physics settings
   */
  private void initPhysicsSettings() {
    acceleration = new Vector2();
    velocity = new Vector2();
  }

  /**
   * Sets the default value of the state and other flags
   */
  private void initDefaultFlags() {
    grounded = false;
    facingLeft = true;
  }

  /**
   * Initializes other object properties
   */
  private void initObjects() {
    collidable = new Array<Block>();

    rectPool = new Pool<Rectangle>() {
      @Override
      protected Rectangle newObject() {
        return new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
      }
    };
  }

  private void checkCollisionWithBlocks(float delta) {
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

    // Clear the collision boxes
//    stage.clearCollisionBoxes();

    // If he collides, stop his x-velocity to 0
    for (Block block : collidable) {
      if (block != null && block.collidesBeside(box)) {
        velocity.x = 0;
        Gdx.app.log(TAG, "Reset v.x="+velocity.x);
//        stage.addCollisionBox(block.getBounds());
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
      if (block!= null && block.collidesBeside(box)) {
        if (velocity.y < 0)
          setGrounded(true);
        velocity.y = 0;
        Gdx.app.log(TAG, "Reset v.y="+velocity.y);
//        stage.addCollisionBox(block.getBounds());
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
