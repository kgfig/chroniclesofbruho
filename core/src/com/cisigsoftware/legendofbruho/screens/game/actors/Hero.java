/**
 * Copyright 2015 CISIG Software Labs Inc. All Rights Reserved.
 */
package com.cisigsoftware.legendofbruho.screens.game.actors;

import com.badlogic.gdx.math.Vector2;

/**
 * @author kg
 *
 */
public class Hero extends GameActor {

  public enum State {
    IDLE, WALKING, JUMPING, DYING
  }

  private static final float WALKING_SPEED = 4f;
  private static final float JUMP_VELOCITY = 1f;
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

    Vector2 v = velocity.cpy().scl(delta);
    moveBy(v.x, v.y);
  }

  private void initPhysicsSettings() {
    acceleration = new Vector2();
    velocity = new Vector2();
  }
  
  /**
   * Sets the hero in an idle state
   */
  public void stand() {
    velocity.x = 0;
    acceleration.x = 0;
    setState(State.IDLE);
  }

  /**
   * Sets the hero to walk with the given speed and face in the given orientation
   * 
   * @param facingLeft
   * @param vx
   */
  private void walk(boolean facingLeft, float vx) {
    setFacingLeft(facingLeft);
    state = State.WALKING;
    this.velocity.x = vx;
  }

  /**
   * Makes the hero face and walk to the left
   */
  public void walkLeft() {
    walk(true, -WALKING_SPEED);
  }

  /**
   * Makes the hero face and walk to the right
   */
  public void walkRight() {
    walk(false, WALKING_SPEED);
  }

  /**
   * @return the state
   */
  public State getState() {
    return state;
  }

  /**
   * @param state the state to set
   */
  public void setState(State state) {
    this.state = state;
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
   * @return the acceleration
   */
  public Vector2 getAcceleration() {
    return acceleration;
  }

  /**
   * @param acceleration the acceleration to set
   */
  public void setAcceleration(Vector2 acceleration) {
    this.acceleration = acceleration;
  }

  /**
   * @return the velocity
   */
  public Vector2 getVelocity() {
    return velocity;
  }

  /**
   * @param velocity the velocity to set
   */
  public void setVelocity(Vector2 velocity) {
    this.velocity = velocity;
  }

}
