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
import com.cisigsoftware.legendofbruho.screens.game.GameStage;

/**
 * @author kg
 *
 */
public class Hero extends GameActor {

  public enum State {
    IDLE, WALKING, JUMPING, DYING
  }

  private static final float ACCELERATION = 20f; // for walking and running
  private static final float GRAVITY = -20f; // gravity acceleration
  private static final float MAX_JUMP_SPEED = 7f; // terminal and maximum velocity when jumping
  private static final float DAMP = 0.9f; // smooth out movement so he won't stop abruptly, ignore
                                          // for jump
  private static final float MAX_VEL = 4f; // maximum velocity for movement along horizontal axis
                                           // while walking or running

  private static final float SIZE = 0.5f;

  private GameStage stage;
  private Vector2 acceleration, velocity;
  private State state;
  private boolean grounded;
  private boolean facingLeft;

  private Pool<Rectangle> rectPool;
  private Array<Block> collidable;


  public Hero(GameStage stage, Vector2 position) {
    super(position.x, position.y, SIZE, SIZE);
    setPosition(position.x, position.y);
    setSize(SIZE, SIZE);

    this.stage = stage;
    state = State.IDLE;
    grounded = false;
    facingLeft = true;

    initPhysicsSettings();
    initRectPool();
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    if (isGrounded() && isJumping())
      stand();

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

    // Keep the hero inside the world
    if (getX() < 0) {
      resetX();
    }

    if (getY() < 0) {
      resetY();
    }

    if (getX() > GameStage.WORLD_WIDTH - bounds.getWidth()) {
      setX(GameStage.WORLD_WIDTH - bounds.getWidth());

      if (!isJumping())
        stand();
    }
  }

  private void initPhysicsSettings() {
    acceleration = new Vector2();
    velocity = new Vector2();
  }

  private void initRectPool() {
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
    collidable = stage.populateCollidableBlocks(startX, startY, endX, endY);

    // Create a copy of hero's bounds and set it to his future position based on his velocity
    Rectangle heroBox = rectPool.obtain();
    heroBox.x = heroBox.x + velocity.x;

    // Clear the collision boxes
    stage.clearCollisionBoxes();

    // If he collides, stop his x-velocity to 0
    for (Block block : collidable) {
      if (block != null && block.collidesBeside(heroBox)) {
        Gdx.app.log("Hero", "Collidable block " + block.getX() + "," + block.getY());
        stopMoving();
        stage.addCollisionBox(block.getBounds());
        break;
      }
    }

    // Then reset his collision box to his current position
    heroBox.x = getX();

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
    collidable = stage.populateCollidableBlocks(startX, startY, endX, endY);

    // Create a copy of hero's bounds and set it to his future position based on his velocity
    heroBox.y = heroBox.y + velocity.y;

    // If he collides, set his y-velocity to 0 and set grounded to true
    for (Block block : collidable) {
      if (block!= null && block.collidesBeside(heroBox)) {
        if (velocity.y < 0)
          setGrounded(true);
        stopFalling();
        stage.addCollisionBox(block.getBounds());
        break;
      }
    }

    // Then reset his collision box to his current position
    heroBox.y = getY();
    
    // Update his current position
    moveBy(velocity.x, velocity.y);
    bounds.x = getX();
    bounds.y = getY();
    
    // un-scale the velocity
    velocity.scl(1 / delta);
  }


  /**
   * Keeps him inside the world and resets the jumping state
   */
  private void resetX() {
    setX(0);

    if (!isJumping())
      stand();
  }

  /**
   * Keeps him inside the world and resets the jumping state
   */
  private void resetY() {
    setY(0);

    if (isJumping())
      stand();
  }

  /**
   * Returns true if the current state of hero is JUMPING
   * 
   * @return true if the current state of hero is JUMPING
   */
  public boolean isJumping() {
    return getState() == State.JUMPING;
  }

  /**
   * Returns true if the current state of hero is WALKING
   * 
   * @return true if the current state of hero is WALKING
   */
  public boolean isWalking() {
    return getState() == State.WALKING;
  }

  /**
   * Returns true if the current state of hero is IDLE
   * 
   * @return true if the current state of hero is IDLE
   */
  public boolean isIdle() {
    return getState() == State.IDLE;
  }

  /**
   * Returns true if the current state of hero is DYING
   * 
   * @return true if the current state of hero is DYING
   */
  public boolean isDying() {
    return getState() == State.DYING;
  }

  /**
   * @return the state
   */
  private State getState() {
    return state;
  }

  /**
   * @param state the state to set
   */
  private void setState(State state) {
    this.state = state;
  }

  /**
   * Sets the acceleration to 0
   */
  public void stopWalking() {
    this.acceleration.x = 0;
  }

  /**
   * Sets the x-velocity to 0
   */
  public void stopMoving() {
    this.velocity.x = 0;
  }
  
  /**
   * Sets the y-velocity to 0
   */
  public void stopFalling() {
    this.velocity.y = 0;
  }

  /**
   * Sets the hero in an idle state
   */
  public void stand() {
    setState(State.IDLE);
  }

  /**
   * Makes the character jump
   */
  public void jump() {
    jump(true, MAX_JUMP_SPEED);
  }

  /**
   * Makes the character propel up
   */
  public void propelUp() {
    jump(false, MAX_JUMP_SPEED);
  }

  /**
   * Makes the character jump with the given y-velocity
   */
  private void jump(boolean setJumping, float vy) {
    velocity.y = vy;
    if (setJumping)
      setState(State.JUMPING);
  }

  /**
   * Makes the hero face and walk to the left
   */
  public void walkLeft() {
    state = State.WALKING;
    moveLeft();
  }

  /**
   * Makes the hero face and walk to the right
   */
  public void walkRight() {
    state = State.WALKING;
    moveRight();
  }

  /*
   * Sets facing left flag to true
   */
  public void moveLeft() {
    setFacingLeft(true);
    this.acceleration.x = -ACCELERATION;
  }

  /*
   * Sets facing left flag to false
   */
  public void moveRight() {
    setFacingLeft(false);
    this.acceleration.x = ACCELERATION;
  }


  /**
   * @return the facingLeft
   */
  public boolean isFacingLeft() {
    return facingLeft;
  }

  /**
   * @param facingLeft sets the flag for facing left
   */
  public void setFacingLeft(boolean facingLeft) {
    this.facingLeft = facingLeft;
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
  
}
