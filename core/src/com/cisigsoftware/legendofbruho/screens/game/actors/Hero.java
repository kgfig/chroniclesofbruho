/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.cisigsoftware.legendofbruho.screens.game.Level;

/**
 * @author kg
 *
 */
public class Hero extends GameActor {

  private enum State {
    IDLE, WALKING, JUMPING, DYING
  }

  private static final float ACCELERATION = 20f; // for walking and running
  private static final float MAX_JUMP_SPEED = 7f; // terminal and maximum velocity when jumping
  private static final float SIZE = 0.5f;

  private State state;

  public Hero(Level level, Vector2 position) {
    super(level, position.x, position.y, SIZE, SIZE);

    state = State.IDLE;
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    if (isGrounded() && isJumping())
      idle();
  }
  
  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);
    
    float rightX = level.getWidth() - bounds.getWidth();
    
    if (getX() > rightX && !isJumping()) {
        idle();
    }
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

}
