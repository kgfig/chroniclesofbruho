/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
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

  private Vector2 acceleration, velocity;
  private State state;
  private boolean facingLeft = true;

  public Hero(Vector2 position) {
    super(position.x, position.y, SIZE, SIZE);

    setPosition(position.x, position.y);
    setSize(SIZE, SIZE);
    state = State.IDLE;
    facingLeft = true;

    initPhysicsSettings();
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    acceleration.y = GRAVITY;
    acceleration.scl(delta);
    velocity.add(acceleration);

    if (acceleration.x == 0)
      velocity.x = velocity.x * DAMP;

    if (velocity.x > MAX_VEL) // cap x-velocity by MAX_VEL
      velocity.x = MAX_VEL;

    if (velocity.x < -MAX_VEL)
      velocity.x = -MAX_VEL;

    Vector2 v = velocity.cpy().scl(delta);
    moveBy(v.x, v.y);
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

  public void stopWalking() {
    this.acceleration.x = 0;
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

}
