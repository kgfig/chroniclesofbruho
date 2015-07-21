/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.gamescreen.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.cisigsoftware.legendofbruho.gamescreen.Level;
import com.cisigsoftware.legendofbruho.gamescreen.actors.base.GameActor;

/**
 * @author kg
 *
 */
public class Hero extends GameActor {

  private static final String TAG = Hero.class.getSimpleName();

  private static final float GRAVITY = -20f; // gravity acceleration

  // HP-related stats
  private static final float MAX_HP = 10;
  private static final float DAMAGE = 5;

  private enum State {
    IDLE, WALKING, JUMPING, DYING
  }

  private static final float MAX_VEL = 4f; // maximum velocity for movement along horizontal axis
                                           // while walking or running
  private static final float ACCELERATION = 20f; // for walking and running
  private static final float MAX_JUMP_SPEED = 7f; // terminal and maximum velocity when jumping
  private static final float WIDTH = 0.5f;
  private static final float HEIGHT = 0.5f;

  private State state;
  private Block goal;

  public Hero(Vector2 position) {
    super(position.x, position.y, WIDTH, HEIGHT);

    state = State.IDLE;
    setGrounded(false);
    setGravity(GRAVITY);
    setMaxVel(MAX_VEL);
    setHp(MAX_HP);
    setMaxHp(MAX_HP);
    setDamage(DAMAGE);

    Gdx.app.log(TAG, "Initialized Hero. HP=" + getHp() + "\tdamage=" + getDamage());
  }

  @Override
  public void act(float delta) {
    acceleration.y = gravity;

    super.act(delta);

    if (isGrounded() && isJumping())
      idle();

    if (!level.isComplete()) {
      if (!hasHp() && !isDying()) {
        Gdx.app.log(TAG, "Hero died.");
        die();
      }

      if (goal != null && this.collidesBeside(goal)) {
        level.setComplete(true);
      }
    }
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);

    float rightX = level.getWidth() - bounds.getWidth();

    if (getX() > rightX && !isJumping()) {
      idle();
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
      if (block != null && !block.isGoal() && block.collidesBeside(box)) {
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
      if (block != null && !block.isGoal() && block.collidesBeside(box)) {
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

  /**
   * Keeps him inside the world and resets the jumping state
   */
  @Override
  public void resetX() {
    super.resetX();

    if (!isJumping())
      idle();
  }

  /**
   * Keeps him inside the world and resets the jumping state
   */
  @Override
  public void resetY() {
    super.resetY();

    if (isJumping())
      idle();
  }

  /**
   * @param state the state to set
   */
  private void setState(State state) {
    this.state = state;
  }

  private void die() {
    setState(State.DYING);
    remove();
  }

  /**
   * Returns true if the current state of hero is JUMPING
   * 
   * @return true if the current state of hero is JUMPING
   */
  public boolean isJumping() {
    return state == State.JUMPING;
  }

  /**
   * Returns true if the current state of hero is DYING
   * 
   * @return true if the current state of hero is DYING
   */
  public boolean isDying() {
    return state == State.DYING;
  }

  /**
   * Returns true if the current state of hero is WALKING
   * 
   * @return true if the current state of hero is WALKING
   */
  public boolean isWalking() {
    return state == State.WALKING;
  }

  /**
   * Sets the acceleration to 0
   */
  public void stopWalking() {
    this.acceleration.x = 0;
  }

  /**
   * Sets the hero in an idle state
   */
  public void idle() {
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

  @Override
  public void setLevel(Level level) {
    this.level = level;
    goal = level.getGoal();
  }
}
